package team.project.module.auth.internal.config;

import cn.dev33.satoken.log.SaLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaTokenLogger implements SaLog {
    Logger logger = LoggerFactory.getLogger("【Sa-Token】");

    @Override
    public void trace(String str, Object... args) {
        logger.trace(str, args);
    }

    @Override
    public void debug(String str, Object... args) {
        logger.debug(str, args);
    }

    @Override
    public void info(String str, Object... args) {
        logger.info(str, args);
    }

    @Override
    public void warn(String str, Object... args) {
        logger.warn(str, args);
    }

    @Override
    public void error(String str, Object... args) {
        logger.error(str, args);
    }

    @Override
    public void fatal(String str, Object... args) {
        logger.error(str, args);
    }
}
