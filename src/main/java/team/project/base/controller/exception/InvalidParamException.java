package team.project.base.controller.exception;

import lombok.Getter;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

/**
 * <p> 自定义的 controller 层异常类
 * <p> 入参校验失败时抛出此异常
 * */
@Getter
public class InvalidParamException extends ServiceException {
    public InvalidParamException(String message) {
        super(ServiceStatus.BAD_REQUEST, message);
    }
}
