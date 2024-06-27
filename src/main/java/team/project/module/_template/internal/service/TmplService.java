package team.project.module._template.internal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.module._template.internal.mapper.TmplMybatisMapper;
import team.project.module._template.internal.mapper.TmplMybatisPlusMapper;

@Service
@Slf4j
public class TmplService {

    @Autowired
    TmplMybatisMapper tmplMybatisMapper;    /* mybatis（java 接口与 xml SQL） */

    @Autowired
    TmplMybatisPlusMapper tmplMybatisPlusMapper;  /* mybatis-plus（只配 java 接口） */

    public Object list() { /* 示例 */
        return tmplMybatisMapper.list();
    }

    public Object list_mp() { /* 示例 */
        return tmplMybatisPlusMapper.selectList(null);
    }
}
