package team.project.debug.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExceptionHandlerLog {
    private static final Logger log = LoggerFactory.getLogger("【全局异常捕获】");

    @Before("""
        (   execution(* team.project.base..SaTokenExceptionHandler.*(..))         ||
            execution(* team.project.base..SpringframeworkExceptionHandler.*(..))
        ) && args(exception)
        """)
    public void log(JoinPoint jp, Exception exception) {
        Signature signature       = jp.getSignature();
        String    simpleClassName = signature.getDeclaringType().getSimpleName();

        log.info(
            "【" + simpleClassName + "】捕获到异常\n" +
            exception.getClass().getSimpleName() + ": " + exception.getMessage()
        );
    }

    @Before("execution(* team.project.base..GeneralExceptionHandler.*(..)) && args(exception)")
    public void logGeneralException(JoinPoint jp, Exception exception) {
        Signature signature       = jp.getSignature();
        String    simpleClassName = signature.getDeclaringType().getSimpleName();

        log.error(
            "【" + simpleClassName + "】捕获到异常（该异常由通用异常处理器捕获，请考虑是否为其配备专门的异常处理器）\n" +
            exception.getClass().getSimpleName() + ": " + exception.getMessage()
            , exception
        );
    }

    @Before("execution(* team.project.base..MybatisPlusExceptionHandler.*(..)) && args(exception)")
    public void logMybatisPlusException(JoinPoint jp, Exception exception) {
        Signature signature       = jp.getSignature();
        String    simpleClassName = signature.getDeclaringType().getSimpleName();

        log.error(
            "【" + simpleClassName + "】捕获到异常（执行 SQL 时出现的异常最好在 service 层或 dao 层将其捕获）\n" +
            exception.getClass().getSimpleName() + ": " + exception.getMessage()
            , exception
        );
    }
}
