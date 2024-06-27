package team.project.base.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 用来封装分页查询的请求
 * */
@Getter
@Setter
public class PagingQueryReq {
    @Min(value=1, message="分页查询指定“当前页码”不能小于 1")
    @NotNull(message="分页查询指定“当前页码”不能为空")
    @JsonProperty("page_num")
    private Long pageNum;

    @Min(value=1, message="分页查询指定“页大小”最小不能小于 1")
    @NotNull(message="分页查询指定“页大小”不能为空")
    @JsonProperty("page_size")
    private Long pageSize;
}
