package team.project.config.apidoc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* Swagger 用来自动生成 API 文档的配置类 */
@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
            .group("所有 api")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    GroupedOpenApi tmplTest() {
        return GroupedOpenApi.builder()
            .group("测试")
            .packagesToScan("team.project.module._template")
            .build();
    }
}
