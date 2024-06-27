package team.project.base.model.view;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用来封装分页查询的返回结果
 * */
@Getter
@Setter
public class PageVO<VO> {
    @JsonProperty("records")      private List<VO> records;
    @JsonProperty("current_page") private Integer  currentPage;
    @JsonProperty("page_size")    private Integer  pageSize;
    @JsonProperty("total_pages")  private Integer  totalPages;
    @JsonProperty("total_item")   private Integer  totalItem;

    /* 如果分页查询得出的结果还需要进一步转换（例如，查出的是 DO，需要转换成 VO），则用这个构造器 */
    public PageVO(List<VO> records, Page<?> page) {

        this.records = records;

        this.currentPage = (int)( Math.min(page.getCurrent(), Integer.MAX_VALUE) );
        this.pageSize    = (int)( Math.min(page.getSize(),    Integer.MAX_VALUE) );

        if (page.searchCount()) {
            this.totalPages = (int)( Math.min(page.getPages(), Integer.MAX_VALUE) );
            this.totalItem  = (int)( Math.min(page.getTotal(), Integer.MAX_VALUE) );
        }
    }

    /* 如果分页查询得出的结果不需要转换，则用这个构造器 */
    public PageVO(Page<VO> page) {
        this(page.getRecords(), page);
    }
}

/* 2024-04-27 szdxdxdx

  mybatis-plus 分页查询器的入参（页大小、页码）均为 long 型
  为了匹配查询器，所有分页查询的 api 接口均把查询相关的参数设为 Long 型

  考虑到 Java 的 Long（64位）转 JavaScript Number 可能会丢失精度
  项目已配置序列化器，将 Long 序列化成 String

  原先的设计是，本类中的分页查询相关的字段均为 Long 型，返给前端的是 String 值
  如果前端要做数值运算，由前端自行决定转换方式来保证精度

  现前端提出，ui 组件要求页码必须为数字，不能是 String，并且前端不愿意做类型转换工作
  依前端要求，将本类中的 Long 型降为 Integer 型，以返给前端 Number

  考虑到数据量较小，故在此不考虑精度丢失问题
*/