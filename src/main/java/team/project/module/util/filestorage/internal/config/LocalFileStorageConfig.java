package team.project.module.util.filestorage.internal.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class LocalFileStorageConfig implements WebMvcConfigurer {

    @Value("${file-storage.local-file-system.root-folder}")
    public String rootFolder;

    /* 存储“上传的文件”的目录，以 rootFolder 目录为基 */
    public static final String uploadedFilesFolder = "/upload";

    /* 文件 id 前缀，标记文件存储在本地服务器 */
    public static final String fileIdPrefix = "/f";

    public String baseUrl;

    @Value("${server.port}")
    private String port;

    @PostConstruct
    private void postConstruct() throws UnknownHostException {
        baseUrl = "http://" +  InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* 定义静态资源映射目录，注意映射的文件路径要以 file: 开头，以 / 结尾 */
        registry.addResourceHandler(uploadedFilesFolder + "/**")
                .addResourceLocations("file:" + rootFolder + uploadedFilesFolder + "/");
    }
}
