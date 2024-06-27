package team.project.module.util.filestorage.internal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FileStorageLog {

    private static final Logger log = LoggerFactory.getLogger("【file storage log aspect 文件存储日志切面】");

    @Around("execution(* team.project.module..filestorage.export.service.impl..*(..))")
    public Object fileStorageThrowLog(ProceedingJoinPoint jp) throws Throwable {
        try {
            return jp.proceed();
        }
        catch (Exception e) {
            log.warn("\033[30;41m文件存储模块抛出异常\n\033[0m{}", e.getMessage(), e);
            throw e;
        }
    }
}
