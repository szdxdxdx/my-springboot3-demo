package team.project.module._template.internal.model.datatransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/* DTO (Data Transfer Object) 数据传输对象，
   用于拆解 DO 以重新组装，也可用于封装“返回给前端”响应字段 */
@Getter
@Setter
public class TmplDTO {
    /* 查询数据库得到的 DO 对象可能字段过多，或者是多表查询涉及到多个 DO，
       业务逻辑需处理其中的部分字段，可以拆解 DO 组装成 DTO */

    /* 示例 */
    @JsonProperty("output_str") /* 返回给前端的字段名，可能是下划线命名规则 */
    private String outputStr;   /* 后端必须要保持小驼峰命名规则 */
}
