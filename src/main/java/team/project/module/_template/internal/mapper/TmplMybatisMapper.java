package team.project.module._template.internal.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.project.module._template.internal.model.entity.TmplDO;

import java.util.List;

/* 封装对数据库的操作，是 sql 语句的直接映射 */
@Mapper
public interface TmplMybatisMapper {
    /* 采用 mybatis 的方式（java 接口与 xml SQL） */
    List<TmplDO> list(); /* 示例 */
}
