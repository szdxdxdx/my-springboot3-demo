package team.project.module.util.filestorage.internal.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.internal.config.LocalFileStorageConfig;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class LocalFileStorageDAO {

    private final File   rootFolder; /* <- 本地文件系统中，存储用户数据文件的根目录（在配置文件中以绝对路径形式给出）*/
    private final String baseUrl;

    LocalFileStorageDAO(LocalFileStorageConfig cfg) {
        this.rootFolder = new File(cfg.rootFolder);
        this.baseUrl    = cfg.baseUrl;
    }

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 将上传的文件保存到指定路径下（如果文件已存在，则覆盖）
     * */
    public void saveFile(MultipartFile file, String filePath) throws IOException {
        File fileToSave = new File(rootFolder, filePath);
        boolean ignored = fileToSave.getParentFile().mkdirs();
        file.transferTo(fileToSave);
    }

    /**
     * 判断文件是否存在
     * */
    public boolean isFileExist(String filePath) {
        return new File(rootFolder, filePath).exists();
    }

    /**
     * 获取访问文件的 URL（fileId 指向的文件不存在则会返回 null）
     * */
    public String getFileUrl(String filePath) {

        File file = new File(rootFolder, filePath);
        if ( ! file.exists() || file.isDirectory()) {
            return null;
        }

        String[] pathSplit = filePath.split("/");
        StringBuilder url = new StringBuilder(baseUrl);
        for (int i = 1; i < pathSplit.length; i++) { /* <- i 从 1 开始，因为[0]是空字符串 "" */
            url.append("/").append(URLEncoder.encode(pathSplit[i], UTF_8));
        }
        return url.toString();
    }

    /**
     * 删除文件
     * */
    public boolean deleteFile(String filePath) {

        File file = new File(rootFolder,  filePath);
        if ( ! file.exists() || file.isDirectory()) {
            return false;
        }

        boolean result = file.delete();

        if (result) {
            /* 如果文件删除成功后文件夹为空，则一路删除空的文件夹，直到根文件夹 */
            File parent = file.getParentFile();
            while ( ! rootFolder.equals(parent)) {

                String[] files = parent.list();
                if (files == null || files.length != 0)
                    break;
                if ( ! parent.delete())
                    break;

                parent = parent.getParentFile();
            }
        }

        return result;
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 将一段文本以 UTF8 编码保存到文本文件中（如果文件已存在，则覆盖）
     * */
    public void saveTextToFile(String text, String filePath) throws IOException {
        File fileToSave = new File(rootFolder, filePath);
        { boolean ignored = fileToSave.getParentFile().mkdirs(); }
        { boolean ignored = fileToSave.createNewFile(); }
        Files.writeString(fileToSave.toPath(), text, UTF_8);
    }

    /**
     * <p>  以 UTF8 编码规则来读取文件
     * <br> 如果文件不是纯文本文件，或者编码不匹配，则读取失败，抛出异常
     * */
    public String readTextFromFile(String filePath) throws IOException {
        File file = new File(rootFolder, filePath);
        return Files.readString(file.toPath(), UTF_8);
    }
}
