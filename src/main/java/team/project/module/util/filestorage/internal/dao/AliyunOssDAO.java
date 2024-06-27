package team.project.module.util.filestorage.internal.dao;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.internal.config.AliyunOssConfig;

import java.io.*;
import java.net.URL;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

/* 对象（Object）是 OSS 存储数据的基本单元，也被称为 OSS 的文件
   和传统的文件系统不同，Object 没有文件目录层级结构的关系，OSS 通过键名（Key）唯一标识存储的 Object
   Object 的命名规范如下：
    - 使用 UTF-8 编码
    - 长度必须在 1~1023 字符之间
    - 不能以正斜线（/）或者反斜线（\）开头
    - 区分大小写

    默认情况下，OSS 存储空间中文件的读写权限是私有，仅文件拥有者具有访问文件的权限
    但是，文件拥有者可以通过创建签名 URL 的方式与第三方用户分享文件，
    签名 URL 使用安全凭证的方式授权第三方用户在指定时间内下载或者预览文件
*/

@Component
public class AliyunOssDAO {

    private final String bucketName;
    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;

    AliyunOssDAO(AliyunOssConfig cfg) {
        this.endpoint        = cfg.endpoint;
        this.accessKeyId     = cfg.accessKeyId;
        this.accessKeySecret = cfg.accessKeySecret;
        this.bucketName      = cfg.bucketName;
    }

    /* -- OSS 客户端 -- */

    /* 2024-06-03 ljh
       ---------
        原先设计是，将 ossClient 作为类成员（单例），即每次发起请求都用同一个 ossClient

        当短时间内发送多个请求时，大概率有个别请求会失败，如果开启 com.aliyun.oss 日志输出会看到如下内容：
        ```
        WARN com.aliyun.oss:
            Failed to reset the request input stream: Resetting to invalid mark
        WARN com.aliyun.oss:
            [Client]Unable to execute HTTP request: Failed to reset the request input stream:
            [ErrorCode]: Unknown
            [RequestId]: Unknown
        ```

        现改成，每次发请求都创建新的 ossClient，上述问题得以解决
    */

    private OSS newOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    private void closeOssClient(OSS ossClient) {
        if (ossClient != null)
            ossClient.shutdown();
    }

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 上传文件（采用简单上传的方式，上传不超过5 GB大小的文件）
     * */
    public void uploadFile(MultipartFile file, String key, boolean overwrite) throws IOException {
        OSS ossClient = newOssClient();
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream());

            if ( ! overwrite) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setHeader("x-oss-forbid-overwrite", "true");
                request.setMetadata(metadata);
            }

            ossClient.putObject(request);
        }
        finally { closeOssClient(ossClient); }
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExist(String key) {
        OSS ossClient = newOssClient();
        try {
            return ossClient.doesObjectExist(bucketName, key);
        }
        finally { closeOssClient(ossClient); }
    }

    /**
     * 获取访问文件的 URL（fileId 指向的文件不存在也会返回 URL，访问这个 URL 会响应文件不存在）
     * */
    public String getFileUrl(String key) {
        OSS ossClient = newOssClient();
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
            request.setExpiration(new Date(System.currentTimeMillis() + 60 * 1000L)); /* 设置过期时间 1 分钟 */

            URL url = ossClient.generatePresignedUrl(request);
            return url.toString();
        }
        finally { closeOssClient(ossClient); }
    }

    /**
     * 删除单个文件
     * */
    public void deleteFile(String key) {
        OSS ossClient = newOssClient();
        try {
            ossClient.deleteObject(bucketName, key); /* <- 无论要删除的文件是否存在，删除成功后均会返回 204 状态码 */
        }
        finally { closeOssClient(ossClient); }
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 上传一段以 UTF8 编码的文本，以文本文件的形式保存
     * */
    public void uploadTextToFile(String text, String key, boolean overwrite) {
        OSS ossClient = newOssClient();
        try {
            InputStream inputStream = new ByteArrayInputStream(text.getBytes(UTF_8));
            PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream);

            if ( ! overwrite) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setHeader("x-oss-forbid-overwrite", "true");
                request.setMetadata(metadata);
            }

            ossClient.putObject(request);
        }
        finally { closeOssClient(ossClient); }
    }

    /**
     * <p>  以 UTF8 编码规则来读取文件
     * <br> 如果文件不是纯文本文件，或者编码不匹配，则结果可能呈现乱码
     * */
    public String readTextFromFile(String key) throws IOException {
        OSS            ossClient = newOssClient();
        OSSObject      ossObject = ossClient.getObject(bucketName, key);
        BufferedReader reader    = new BufferedReader(new InputStreamReader(ossObject.getObjectContent(), UTF_8));
        try (ossObject; reader) {

            StringBuilder result = new StringBuilder();
            int readBufLen = 2048;
            char[] buffer = new char[readBufLen];
            int numCharRead;
            while (-1 != (numCharRead = reader.read(buffer, 0, readBufLen))) {
                result.append(buffer, 0, numCharRead);
            }
            return result.toString();
        }
        finally { closeOssClient(ossClient); }
    }
}
