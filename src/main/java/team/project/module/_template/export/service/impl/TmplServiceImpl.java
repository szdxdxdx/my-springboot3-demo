package team.project.module._template.export.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.export.model.query.TmplQO;
import team.project.module._template.export.service.TmplServiceI;

/* 注意接口实现类是以 "ServiceImpl" 结尾 */
@Service
@Slf4j
public class TmplServiceImpl implements TmplServiceI {

    public TmplDTO example(TmplQO queryObject) {
        assert queryObject != null; /* <- export 包下提供的方法最好加上简单的断言 */

        /* 这里可以使用 internal 的 service */
        return null;
    }
}
