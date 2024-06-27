package team.project.module._template.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.base.controller.response.Response;
import team.project.base.service.status.ServiceStatus;
import team.project.module._template.internal.service.TmplService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Tag(name="【测试】模板示例")
@RestController
@Slf4j
public class TmplController {

    @Autowired
    TmplService service; /* 示例 */

    /* 示例 */
    @Operation(summary="返回 hello world! 文本")
    @GetMapping("/tmpl/hello-world")
    Object helloWorld() {
        String str = "hello world!";

        return new Response<>(ServiceStatus.SUCCESS) /* 返回 service 状态码 */
            .statusText("OK") /* 可选，携带状态信息（可以用于前端的报错展示，如果前端懒的话） */
            .data(str);       /* 可选，携带数据 */
    }

    /* 示例 */
    @Operation(summary="使用 mybatis 查询 tmp_test 表中的所有记录")
    @GetMapping("/tmpl/list")
    Object list() {
        return new Response<>(ServiceStatus.SUCCESS)
            .statusText("查询成功")
            .data(service.list());
    }

    /* 示例 */
    @Operation(summary="使用 mybatis-plus 查询 tmp_test 表中的所有记录")
    @GetMapping("/tmpl/list_mp")
    Object list2() {
        return new Response<>(ServiceStatus.SUCCESS)
            .statusText("查询成功")
            .data(service.list_mp());
    }

    /* 示例 */
    @Operation(summary="返回一个json对象")
    @GetMapping("/tmpl/object")
    Object object() {
        Map<String, Object> result = new HashMap<>();
        result.put("int", Integer.MAX_VALUE);
        result.put("long", Long.MAX_VALUE);
        result.put("float", 3.1415926);

        Map<String, Object> obj = new HashMap<>();
        Object[] arr = {123, 456L, "hello world"};
        obj.put("date", new Date());
        obj.put("arr", arr);
        result.put("obj", obj);

        return new Response<>(ServiceStatus.SUCCESS).data(result);
    }
}
