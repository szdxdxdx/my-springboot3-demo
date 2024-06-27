package team.project.base.exceptionhandler.handler;

import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.base.controller.response.Response;
import team.project.base.exceptionhandler.order.ExceptionHandlerOrder;
import team.project.base.service.status.ServiceStatus;

import java.sql.SQLException;

/* 处理 mybatis-plus 抛出的异常 */
@RestControllerAdvice
@Order(ExceptionHandlerOrder.mybatisPlusExceptionHandler)
public class MybatisPlusExceptionHandler {

    /* 执行 SQL 出错 */
    @ExceptionHandler(SQLException.class)
    Object handle(SQLException exception) {
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* “唯一键”约束 */
    @ExceptionHandler(DuplicateKeyException.class)
    Object handle(DuplicateKeyException exception) {
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }

    /* “完整性”约束 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    Object handle(DataIntegrityViolationException exception) {
        return new Response<>(ServiceStatus.INTERNAL_SERVER_ERROR);
    }
}
