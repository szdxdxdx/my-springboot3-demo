package team.project.base.service.exception;

import lombok.Getter;
import team.project.base.service.status.ServiceStatus;

/**
 * 自定义的 service 层异常类
 * */
@Getter
public class ServiceException extends RuntimeException {
    private final ServiceStatus status;

    public ServiceException(ServiceStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(Throwable cause, ServiceStatus status, String message) {
        super(message, cause);
        this.status = status;
    }
}
