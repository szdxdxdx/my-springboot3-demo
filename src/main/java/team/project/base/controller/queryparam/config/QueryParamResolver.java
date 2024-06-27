package team.project.base.controller.queryparam.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.base.controller.exception.InvalidParamException;
import team.project.base.controller.queryparam.QueryParam;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 配置 @QueryParam 注解的参数解析器
 * */
@Slf4j
@Configuration
public class QueryParamResolver implements WebMvcConfigurer, HandlerMethodArgumentResolver {

    /**
     * 向框架中添加本参数解析器
     * */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this);
    }

    /**
     * <p>  如果 controller 的函数中的某个形参由 @QueryParam 注解修饰，
     * <br> 则这个形参接收 HTTP 请求中的查询参数时，会调用本解析器
     * */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(QueryParam.class);
    }

    @Autowired
    private Validator validator;

    /**
     * <p> 解析参数，将结果装填入对象，并做校验
     * <p> 如果解析失败或校验失败，则抛出异常，交由全局异常捕获器处理，表现上和框架自带的异常处理一致
     * */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Object result;
        try {
            result = processFiledValue(webRequest, parameter.getParameterType());
        } catch (Exception e) {
            log.warn("@QueryParam 参数解析失败", e);
            throw new InvalidParamException("参数解析失败，请求参数的类型与所需的类型不匹配");
        }

        if (parameter.hasParameterAnnotation(Valid.class)) {
            Set<ConstraintViolation<Object>> violations = validator.validate(result);
            if ( ! violations.isEmpty()) {
                for (ConstraintViolation<Object> violation : violations) {
                    throw new InvalidParamException(violation.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * 解析请求中的参数，将结果装填进 clazz 类型的对象中，返回该对象
     * */
    private Object processFiledValue(NativeWebRequest webRequest, Class<?> clazz) throws Exception {

        /* 创建对象实例，然后解析请求中的参数，将结果装填进这个实例中 */
        Object target = clazz.getDeclaredConstructor().newInstance();

        /* 遍历类的所有字段 */
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {

            /* 如果这个字段由 JsonProperty 注解修饰，则使用注解的 value 值作为入参名，否则用字段名作为入参名 */
            JsonProperty jsonProperty = declaredField.getAnnotation(JsonProperty.class);
            String ParamName = jsonProperty != null ? jsonProperty.value() : declaredField.getName();

            /* 依据入参名获取请求中的入参值，如果获取不到，则说明 http 请求中没有传递这个参数 */
            String inputValue = webRequest.getParameter(ParamName);
            if (inputValue == null || inputValue.isEmpty()) {
                continue;
            }

            /* 如果能获取到入参值，则解析入参值 */
            Object parsedResult = parseInputValueToJavaObject(declaredField, inputValue);

            /* 然后获取这个字段的 setter 方法 */
            String fieldName = declaredField.getName();
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method declaredMethod = clazz.getMethod(setMethodName, declaredField.getType());

            /* 调用这个 setter 方法，将解析后的结果装填进这个字段 */
            declaredField.setAccessible(true);
            declaredMethod.invoke(target, parsedResult);
        }

        return target;
    }

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 将请求中的参数值（字符串值）解析为 Java 对象（只支持解析为基本的数据类型）
     * */
    private Object parseInputValueToJavaObject(Field declaredField, String value) throws JsonProcessingException {
        return objectMapper.readValue("\"" + value + "\"", declaredField.getType());
    }
}
