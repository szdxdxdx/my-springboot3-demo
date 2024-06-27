package team.project.module.util.filestorage.internal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOssConfig {

    @Value("${file-storage.aliyun-oss.endpoint}")
    public String endpoint;

    @Value("${file-storage.aliyun-oss.access-key-id}")
    public String accessKeyId;

    @Value("${file-storage.aliyun-oss.access-key-secret}")
    public String accessKeySecret;

    @Value("${file-storage.aliyun-oss.bucket-name}")
    public String bucketName;

    /* 存储“上传的文件”的目录
       依 package.info 给出的路径规则，此路径应以 / 开头，但 aliyunOSS 要求路径不以 / 开头，故此处特别处理 */
    public static final String uploadedFilesFolder = "upload";

    /* 文件 id 前缀，标记文件存储在阿里云 */
    public static final String fileIdPrefix = "/a";
}
