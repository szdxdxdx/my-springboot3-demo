package team.project.module._template.internal.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/* DO (Domain Object) 领域对象
   与数据库表结构一一对应 */
@Getter
@Setter
@TableName("tbl_tmp_test")
public class TmplDO { /* <- DO 的类名不需要起得和表名相同（如果表名实在太长） */

    /*
    create table `tbl_tmp_test` (
        `id`          bigint   not null auto_increment                comment '自增主键',
        `is_deleted`  tinyint  not null default 0                     comment '逻辑删除',
        `create_time` datetime not null default now()                 comment '创建时间',
        `update_time` datetime not null default now() on update now() comment '更新时间',

        `str`         varchar(96) default '' not null comment '文本',
        primary key (`id`)
    ) comment '测试用';
    */

    /* 示例（与 tbl_tmp_test 表结构对应） */
    @TableId(value="id")               private Long      id;         /* <- 主键 */
    @TableLogic(value="0", delval="1")                               /* <- 用于 mybatis-plus 做逻辑删除的判断 */
    @TableField(value="is_deleted")    private Boolean   deleted;    /* <- java bool 类型的字段名不以 is 开头 */
 /* @TableField(value="create_time")   private Timestamp createTime;    <- 一般的业务逻辑不会处理这两个字段
    @TableField(value="update_time")   private Timestamp updateTime; */
    @TableField(value="str")           private String    str;
}
