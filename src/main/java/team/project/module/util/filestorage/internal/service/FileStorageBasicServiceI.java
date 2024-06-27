package team.project.module.util.filestorage.internal.service;

import org.springframework.web.multipart.MultipartFile;
import team.project.module.util.filestorage.export.exception.FileStorageException;
import team.project.module.util.filestorage.export.model.query.UploadFileQO;

public interface FileStorageBasicServiceI {

    /**
     * 上传文件到指定目录
     * @param toUploadFile 要上传的文件
     * @param uploadFileQO 详见：{@link UploadFileQO}
     * @return fileId
     * @throws FileStorageException 如果上传失败则抛出此异常
     * */
    String uploadFile(MultipartFile toUploadFile, UploadFileQO uploadFileQO);

    /**
     * <p>  通过 fileId 判断文件是否可能在此存储空间中（简单判断）
     * <p>  如果文件在此存储空间中，则 fileId 一定符合规则
     * <br> 操作文件前，先判断 fileId 是否符合这个规则
     * <br> 若不符合，则认为文件不存在，不必再进行后续操作
     * @return 如果文件可能在此存储空间中返回 true，否则返回 false
     * */
    boolean mayBeStored(String fileId);

    /**
     * <p>  通过 fileId 获取访问该文件的 URL
     * <p>  但该 URL 不一定真的能访问到文件，比如下述情况无法访问到文件：
     * <ol>
     * <li> 文件不存在（此时访问该 URL 可能会返回 404）
     * <li> URL 设置了有效访问时长，访问时可能已经过期
     * <li> ...
     * </ol>
     * @return 如果文件可能在此存储空间中，则返回 URL，否则返回 null
     * */
    String getFileUrl(String fileId);

    /**
     * 删除 fileId 指向的文件（无论要删除的文件是否存在，只要执行操作时没有抛出异常都视为删除成功）
     * @return 删除成功返回 true，否则返回 false
     * */
    boolean deleteFile(String fileId);
}
