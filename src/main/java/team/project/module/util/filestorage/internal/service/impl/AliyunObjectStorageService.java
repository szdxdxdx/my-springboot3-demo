package team.project.module.util.filestorage.internal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.internal.config.AliyunOssConfig;
import team.project.module.util.filestorage.internal.dao.AliyunOssDAO;
import team.project.module.util.filestorage.internal.service.FileStorageBasicServiceI;
import team.project.module.util.filestorage.internal.service.TextFileStorageServiceI;
import team.project.module.util.filestorage.internal.util.FilepathUtil;

@Service
@Slf4j
public class AliyunObjectStorageService implements FileStorageBasicServiceI, TextFileStorageServiceI {

    private static final String uploadedFilesFolder  = AliyunOssConfig.uploadedFilesFolder;
    private static final String uploadedFileIdPrefix = AliyunOssConfig.fileIdPrefix;

    @Autowired
    private AliyunOssDAO aliyunOssDAO;

    /**
     * 详见：{@link FileStorageBasicServiceI#mayBeStored}
     * */
    @Override
    public boolean mayBeStored(String fileId) {
        return fileId != null && fileId.startsWith(uploadedFileIdPrefix + "/");
    }

    /**
     * 判断 fileId 格式是否错误（若格式错误，则认为文件不存在，不必再进行后续操作）
     * */
    private boolean isIncorrectFormat(String fileId) {
        return ! mayBeStored(fileId) || FilepathUtil.hasInvalidChar(fileId) || FilepathUtil.hasRelativePathPart(fileId);
    }

    /**
     * 依据文件路径生成 fileId
     * */
    private String generateFileId(String folderPath, String filename) {
        String fileId = FilepathUtil.fixSeparator(uploadedFileIdPrefix + "/" + folderPath + "/" + filename);

        if (FilepathUtil.hasInvalidChar(fileId) || FilepathUtil.hasRelativePathPart(fileId))
            throw new FileStorageException("无法生成 fileId，目标目录路径或目标文件名不合约束");

        return fileId;
    }

    /**
     * 从 fileId 解析出 fileKey（解析前先确保 fileId 的格式正确）
     * */
    private String parseFileIdToFileKey(String fileId) {
        return uploadedFilesFolder + fileId.substring(uploadedFileIdPrefix.length());
    }

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 详见：{@link FileStorageBasicServiceI#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, UploadFileQO uploadFileQO) {

        String targetFolder   = StringUtils.isBlank(uploadFileQO.getTargetFolder())
                                  ? "/"
                                  : uploadFileQO.getTargetFolder();
        String targetFilename = StringUtils.isBlank(uploadFileQO.getTargetFilename())
                                  ? toUploadFile.getOriginalFilename()
                                  : uploadFileQO.getTargetFilename();

        String fileId  = generateFileId(targetFolder, targetFilename);
        String fileKey = parseFileIdToFileKey(fileId);

        try {
            aliyunOssDAO.uploadFile(toUploadFile, fileKey, uploadFileQO.isOverwrite());
            return fileId;
        }
        catch (com.aliyun.oss.ServiceException e) {
            if ("FileAlreadyExists".equals(e.getErrorCode()))
                throw new FileStorageException("文件已存在，且无法覆盖");
            else
                throw new FileStorageException(e, "上传文件失败");
        }
        catch (Exception e) {
            throw new FileStorageException(e, "上传文件失败");
        }
    }

    /**
     * 详见：{@link FileStorageBasicServiceI#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        if (isIncorrectFormat(fileId))
            return null;

        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.getFileUrl(fileKey);
        }
        catch (Exception e) {
            log.warn("获取 url 时出现异常", e);
            return null;
        }
    }

    /**
     * 详见：{@link FileStorageBasicServiceI#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        if (isIncorrectFormat(fileId))
            return true;

        try {
            String fileKey = parseFileIdToFileKey(fileId);
            aliyunOssDAO.deleteFile(fileKey);
            return true;
        }
        catch (Exception e) {
            log.warn("删除文件时出现异常", e);
            return false;
        }
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 详见：{@link TextFileStorageServiceI#uploadTextToFile}
     * */
    @Override
    public String uploadTextToFile(String text, UploadFileQO uploadFileQO) {

        if (StringUtils.isBlank(uploadFileQO.getTargetFilename()))
            throw new FileStorageException("未指定文件名");

        String targetFolder   = StringUtils.isBlank(uploadFileQO.getTargetFolder()) ? "/" : uploadFileQO.getTargetFolder();
        String targetFilename = uploadFileQO.getTargetFilename();

        String fileId = generateFileId(targetFolder, targetFilename);
        String fileKey = parseFileIdToFileKey(fileId);
        try {
            aliyunOssDAO.uploadTextToFile(text, fileKey, uploadFileQO.isOverwrite());
            return fileId;
        }
        catch (com.aliyun.oss.ServiceException e) {
            if ("FileAlreadyExists".equals(e.getErrorCode()))
                throw new FileStorageException("文件已存在，且无法覆盖");
            else
                throw new FileStorageException(e, "上传文件失败");
        }
        catch (Exception e) {
            throw new FileStorageException(e, "上传文件失败");
        }
    }

    /**
     * 如果文件不是纯文本文件，或是编码不匹配，会读取出乱码文本
     * 其他介绍详见：{@link TextFileStorageServiceI#getTextFromFile}
     * */
    @Override
    public String getTextFromFile(String fileId) {
        if (isIncorrectFormat(fileId))
            return null;

        try {
            String fileKey = parseFileIdToFileKey(fileId);
            return aliyunOssDAO.readTextFromFile(fileKey);
        }
        catch (Exception e) {
            log.warn("读取文件时出现异常", e);
            return null;
        }
    }
}
