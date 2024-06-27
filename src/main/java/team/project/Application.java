package team.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class Application {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Application.class, args);
        tmp();
    }

    private static void tmp() {
        Logger log = LoggerFactory.getLogger("【临时测试用】");
        StringBuilder sb = new StringBuilder();

        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
            host = "localhost";
        }
        String port = ctx.getEnvironment().getProperty("server.port");

        sb.append("""
            api 说明文档：{}/swagger-ui/index.html
            api 说明文档：{}/html/test/swagger-with-login.html（ <- 快速登录 ）
            文件存储测试：{}/html/test/file-storage.html
            """.replace("{}", "http://" + host + ":" + port)
        );

        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        if ( ! assertionsEnabled)
            sb.append("\033[31m开发阶段请启用 JVM 断言\n开启方式见：\033[40m/doc/IntelliJ IDEA 如何开启 JVM 断言.jpg\033[0m");
        else
            sb.append("\033[32m已启用JVM 断言\033[0m");

        log.info(sb.toString());
    }
}
