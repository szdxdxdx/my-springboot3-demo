package team.project.module._template.internal.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    TmplInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /* 配置拦截器
        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        */

        /* 设置拦截的路径
        registration.addPathPatterns( ... );
        */

        /* 设置不拦截的路径
        registration.excludePathPatterns( ... );
        */
    }
}
