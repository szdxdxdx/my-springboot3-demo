package team.project.module._template.internal.util;

import team.project.module._template.export.model.datatransfer.TmplDTO;
import team.project.module._template.internal.model.view.TmplVO;

// @Component("模块包名-util-ModelConverter") /* 如果工具类起名比较简单，又要装配成 bean，此时最好自定义 bean 的名字，防止冲突 */
public class ModelConverter {

    /*  DO、DTO、VO、QO 之间的转换代码不宜大篇幅的出现在 service 层，service 层应重点突出业务逻辑
        将转换的代码移到 ModelConverter 类中，使得 service 层业务代码更清晰 */

    public TmplVO toTmplVO(TmplDTO tmplDTO) {
        if (tmplDTO == null) /* <- 或者 assert tmplDTO != null */
            return null;

        TmplVO tmplVO = new TmplVO();

        /*  tmplVO.setXXX(tmplDTO.getXXX());
            tmplVO.setYYY(tmplDTO.getYYY());
            tmplVO.setZZZ(tmplDTO.getZZZ());
            ...
        */

        return tmplVO;
    }
}
