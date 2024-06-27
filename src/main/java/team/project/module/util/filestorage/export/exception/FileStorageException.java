package team.project.module.util.filestorage.export.exception;

import lombok.Getter;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

@Getter
public class FileStorageException extends ServiceException {

    public FileStorageException(String message) {
        super(ServiceStatus.INTERNAL_SERVER_ERROR, message);
    }

    public FileStorageException(Throwable cause, String message) {
        super(cause, ServiceStatus.INTERNAL_SERVER_ERROR, message);
    }
}
