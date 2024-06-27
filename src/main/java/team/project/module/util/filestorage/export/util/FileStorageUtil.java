package team.project.module.util.filestorage.export.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileStorageUtil {

    /**
     * 生成指定后缀的随机的文件名
     * @param extension 文件后缀（例子：".txt"、".html"、".png"）
     * */
    public static String randomFilename(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    /**
     * 生成随机的文件名，文件后缀与给定文件后缀名相同
     * */
    public static String randomFilename(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return randomFilename(StringUtils.isBlank(extension) ? "" : "." + extension);
    }
}
