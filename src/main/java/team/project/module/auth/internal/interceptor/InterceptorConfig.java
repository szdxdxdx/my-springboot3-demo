package team.project.module.auth.internal.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.module.auth.export.model.enums.AuthRole;

import java.util.List;

@Configuration("auth-[SaToken]-InterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer, StpInterface {

    /* -- 配置拦截器 -- */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())/* <- 使用 Sa-Token 提供的综合拦截器 */
            .addPathPatterns("/**");
    }

    /* -- 校验权限和身份 -- */

    /**
     * 返回指定账号 id 所拥有的权限码集合
     * */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    private final List<String> allRoles = List.of(
        AuthRole.ROLE_1,
        AuthRole.ROLE_2,
        AuthRole.ROLE_3
    );

    /**
     * 返回指定账号 id 所拥有的角色码集合
     * */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
