package team.project.config.mybatisplus;

import org.apache.ibatis.logging.stdout.StdOutImpl;

/* 配置 MyBatis-Plus 的日志输出 */
public class SqlLogConfig extends StdOutImpl {
    private static final String color = "\033[35m";

    public SqlLogConfig(String clazz) {
        super(clazz);
    }

    @Override
    public void debug(String s) {
        super.debug(color + s);
    }

    @Override
    public void trace(String s) {
        super.trace(color + s);
    }

    @Override
    public void warn(String s) {
        super.warn(color + s);
    }
}