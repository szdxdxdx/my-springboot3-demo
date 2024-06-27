package team.project.module._template.internal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import team.project.module._template.internal.model.entity.TmplDO;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface TmplMybatisPlusMapper extends BaseMapper<TmplDO> {
    /* 采用 mybatis-plus 的方式（只配 java 接口） */
}
