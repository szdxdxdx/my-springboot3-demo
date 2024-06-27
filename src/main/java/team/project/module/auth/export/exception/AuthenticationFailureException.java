package team.project.module.auth.export.exception;

import lombok.Getter;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

@Getter
public class AuthenticationFailureException extends ServiceException {
    public AuthenticationFailureException(String message) {
        super(ServiceStatus.FORBIDDEN, message);
    }
}
