package team.project.module._template.export.service;

import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;

/* 接口统一 "ServiceI" 结尾，不需要加 @Service */
public interface TmplServiceI {
    TmplDTO example(TmplQO queryObject);
    /* 注意这里的 DTO 和 QO 都是 export 包下的，不是 internal 包下的 */
}
