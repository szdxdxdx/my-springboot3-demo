package team.project.base.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跨模块 SQL 操作标记注解
 *
 * <p>  如果 mapper 中的要执行的某个 SQL 是跨模块的，建议加上此注解
 * <br> 对此注解“查询用法”，可快速找到跨模块的 SQL
 * <p>
 * <b>  加上此注解，相当于给函数加上了如下说明：   </b>
 * <br> 此 SQL 涉及对 tbl_XXX 表的操作，而 tbl_XXX 表并非由本模块管理
 * <br> 本模块自行保证该 SQL 执行结果的数据正确性
 * <br> 如果 tbl_XXX 表有更新，本模块自行负责同步跟进此 SQL
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface CrossModuleSQL {

    /**
     * <p> 表名
     * <p> 如果这个数据表是其他模块负责的，则将表名写在这里（自己模块负责的表名不用写）
     * */
    String[] value();
}
