package team.project.module.util.filestorage.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.export.model.enums.FileStorageType;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;
import team.project.module.util.filestorage.export.service.FileStorageServiceI;
import team.project.module.util.filestorage.internal.service.impl.AliyunObjectStorageService;
import team.project.module.util.filestorage.internal.service.impl.LocalFileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageServiceI {

    @Autowired
    LocalFileStorageService localStorageService;

    @Autowired
    AliyunObjectStorageService cloudStorageService;

    /* -- 基本操作（上传、获取 url、删除） -- */

    /**
     * 详见：{@link FileStorageServiceI#uploadFile}
     * */
    @Override
    public String uploadFile(MultipartFile toUploadFile, FileStorageType storageType, UploadFileQO uploadFileQO) {
        assert toUploadFile != null;
        assert storageType  != null;
        assert uploadFileQO != null;

        return switch (storageType) {
            case LOCAL -> localStorageService.uploadFile(toUploadFile, uploadFileQO);
            case CLOUD -> cloudStorageService.uploadFile(toUploadFile, uploadFileQO);
        };
    }

    /**
     * 详见：{@link FileStorageServiceI#getFileUrl}
     * */
    @Override
    public String getFileUrl(String fileId) {
        assert fileId != null;

        if (localStorageService.mayBeStored(fileId))
            return localStorageService.getFileUrl(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.getFileUrl(fileId);

        return null;
    }

    /**
     * 详见：{@link FileStorageServiceI#deleteFile}
     * */
    @Override
    public boolean deleteFile(String fileId) {
        assert fileId != null;

        if (localStorageService.mayBeStored(fileId))
            return localStorageService.deleteFile(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.deleteFile(fileId);

        return true;
    }

    /* -- 读写纯文本文件 -- */

    /**
     * 详见：{@link FileStorageServiceI#uploadTextToFile}
     */
    @Override
    public String uploadTextToFile(FileStorageType storageType, String text, UploadFileQO uploadFileQO) {
        assert storageType  != null;
        assert text         != null;
        assert uploadFileQO != null;

        return switch (storageType) {
            case LOCAL -> localStorageService.uploadTextToFile(text, uploadFileQO);
            case CLOUD -> cloudStorageService.uploadTextToFile(text, uploadFileQO);
        };
    }

    /**
     * 详见：{@link FileStorageServiceI#getTextFromFile}
     */
    @Override
    public String getTextFromFile(String fileId) {
        assert fileId != null;

        if (localStorageService.mayBeStored(fileId))
            return localStorageService.getTextFromFile(fileId);

        if (cloudStorageService.mayBeStored(fileId))
            return cloudStorageService.getTextFromFile(fileId);

        return null;
    }
}
