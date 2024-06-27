package team.project.base.exceptionhandler.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.response.Response;
import team.project.base.exceptionhandler.order.ExceptionHandlerOrder;
import team.project.base.service.status.ServiceStatus;
import team.project.module.auth.export.model.enums.AuthRole;

/* 处理 Sa-Token 权限认证相关的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.saTokenExceptionHandler)
public class SaTokenExceptionHandler {

    /* 未登录 */
    @ExceptionHandler(NotLoginException.class)
    Object handle(NotLoginException exception) {
        String message = switch (exception.getType()) {
            case NotLoginException.NOT_TOKEN     -> "未能读取到有效 token";
            case NotLoginException.INVALID_TOKEN -> "token 无效";
            case NotLoginException.TOKEN_TIMEOUT -> "token 已过期";
            case NotLoginException.BE_REPLACED   -> "token 已被顶下线";
            case NotLoginException.TOKEN_FREEZE  -> "token 已被冻结";
            case NotLoginException.NO_PREFIX     -> "未按照指定前缀提交 token";
            default                              -> "当前会话未登录";
        };

        return new Response<>(ServiceStatus.UNAUTHORIZED).data(message + "，请重新登录");
    }

    /* 角色验证失败 */
    @ExceptionHandler(NotRoleException.class)
    Object handle(NotRoleException exception) {
        String roleName = switch (exception.getRole()) {
            case AuthRole.ROLE_1 -> "1";
            case AuthRole.ROLE_2 -> "2";
            case AuthRole.ROLE_3 -> "3";
            default -> "？"; /* <- 不应该匹配到这个 case */
        };
        return new Response<>(ServiceStatus.FORBIDDEN).data("未拥有[" + roleName + "]角色，无权执行请求");
    }

    /* 权限验证失败 */
    @ExceptionHandler(NotPermissionException.class)
    Object handle(NotPermissionException exception) {
        return new Response<>(ServiceStatus.FORBIDDEN).data("未拥有指定权限，无权执行请求");
    }
}
