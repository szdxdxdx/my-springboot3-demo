package team.project.util.texttmpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>  简单的文本模板工具
 * <p>  在模板文本中定义占位参数，运行时将这些占位参数替换为实际的值，动态地生成文本内容
 * <p>
 * <b>  占位参数的命名规则： </b>
 * <ol>
 * <li> 可以自定义占位参数的分界符，例如： #{  }、{{  }}、<-!-  -->、甚至是 % 和空格
 * <br> 接下来的介绍中，使用 #{ } 来作占位参数的分界符
 * <br>
 * <li> #{ } 所包裹的字符串截取前后空格后就是参数的名称
 * <p>  模板："Hello, #{ name }! Today is #{ day }."
 * <br> 参数：[ "name", "day" ]
 * <br>
 * <li> 占位参数命名只要求前后不含空白字符，不必遵守标识符的起名规则，可以包含特殊字符
 * <p>  模板："#{ @ }, #{ ## }, #{ the answer }, #{ 1 } "
 * <br> 参数：[ "@", "##", "the answer", "1" ]
 * </ol>
 * <b>  占位参数的识别规则： </b>
 * <ol>
 * <br> 如果 #{ } 内部为空，则忽略这个占位符
 * <br> 模板："#{} #{a}"
 * <br> 参数：["a"]
 * <br>
 * <li> 如果 #{ } 内部还有 #{ }，则将内部的 #{ } 识别为参数，外部的识别为文本
 * <br> 模板："#{ #{} 1! 5!  }"
 * <br> 参数：[]
 * <br> 解释："#{ #{} 1! 5!  }" 内部还有 #{ }，所以外部的 #{ } 识别为文本，
 * <br> 内部的 #{ } 识别为占位参数，但由于内部的 #{ }  为空，则忽略这个占位参数，
 * <br> 所以整个模板没有参数，只有一段文本，即 "#{  1! 5!  }"
 * </ol>
 * */
public class TextTemplate {

    /**
     * <p>  创建模板
     * <p>  一旦创建模板，它就会解析模板字符串中的文本部分和参数部分
     *      频繁创建模板会导致不必要的性能开销，建议尽可能地复用已创建的模板
     * <br> 模板创建完毕后只会持有分析后的模板结构，而不会持有原始的模板文本
     * <p>
     * @param templateText 模板文本
     * @param paramBegin   占位参数的起始字符
     * @param paramEnd     占位参数的结束字符
     * */
    public TextTemplate(String templateText, String paramBegin, String paramEnd) {
        parts = new Analyzer(templateText).analyze(paramBegin, paramEnd);
    }

    /**
     * <p>  渲染模板，根据提供的参数映射替换模板中的占位参数，并返回生成的文本
     * <p>  以键值对的形式提供参数，键是占位参数的名称，值是要替换成的内容
     * <br> 如果提供的参数有缺失，则该占位参数会被替换成空文本
     * @param params 参数
     * */
    public String render(Map<String, Object> params) {
        StringBuilder result = new StringBuilder(256);
        for (Part part : parts) {
            switch (part.type) {
                case TEXT  -> result.append(part.content);
                case PARAM -> result.append(params.getOrDefault(part.content, ""));
            }
        }
        return result.toString();
    }

    /**
     * <p>  渲染模板，根据提供的参数映射替换模板中的占位参数，并返回生成的文本
     * <p>  以变长参数列表的形式提供参数，列表中的元素会按位置依次替换模板中的占位参数
     * <br> 如果提供的参数数量不足，则剩余占位参数会被替换成空文本
     * <br> 如果提供的参数数量过多，则忽略多出的那部分参数
     * @param params 参数
     * */
    public String render(Object ...params) {
        int i = 0;
        StringBuilder result = new StringBuilder(256);
        for (Part part : parts) {
            switch (part.type) {
                case TEXT  -> result.append(part.content);
                case PARAM -> result.append( (i < params.length) ? params[i++] : "" );
            }
        }
        return result.toString();
    }

    /**
     * <p>  获取模板中所有占位的参数名
     * <p>  注意：
     * <br> 该操作为线性时间复杂度，不要频繁地调用该方法
     * <br> 该方法只是为了方便在开发过程中验证模板参数是否符合预期
     * */
    public List<String> getParams() {
        List<String> result = new ArrayList<>();

        for (Part part : parts)
            if (part.type == Type.PARAM)
                result.add(part.content);

        return result;
    }

    /* --------- */

    /* 2024-05-21 szdxdxdx
       ---------
        构建模板对象时将文本模板解析成一个 Part 列表，

        每个 Part 表示一个文本片段，或者一个占位参数

        例如，如果以 #{ } 作为占位参数的分界符，则
            "Hello, #{ name }! Today is #{ day }."
        解析成
            parts = [
                (TEXT,  "Hello, "),
                (PARAM, "name"),
                (TEXT,  "! Today is "),
                (PARAM, "day"),
                (TEXT,  ".")
            ];

        可见，TEXT 和 PARAM 总是交替出现，所以只需交替提取模板文本中的 TEXT 和 PARAM 部分即可
     */

    private enum Type { TEXT, PARAM }

    private record Part (Type type, String content) { }

    /* 存储分析后的模板结构 */
    private final List<Part> parts;

    private record Analyzer(String template) {

        List<Part> analyze(String paramBegin, String paramEnd) {

            if (template == null || template.isEmpty())
                return List.of(new Part(Type.TEXT, ""));

            if (paramBegin == null || paramBegin.isEmpty() || paramEnd == null || paramEnd.isEmpty())
                return List.of(new Part(Type.TEXT, template));

            List<Part> result = new ArrayList<>();

            int idxCurrent = 0;
            while (idxCurrent < template.length()) {

                /* 寻找 PARAM */

                int idxParamBegin = indexOf(paramBegin, idxCurrent);
                int idxParamEnd   = indexOf(paramEnd,   idxParamBegin + paramBegin.length());

                if (idxParamEnd == template.length()) {
                    idxParamBegin = template.length();
                } else {
                    int idxNextParamBegin = indexOf(paramBegin, idxParamBegin + paramBegin.length());

                    while ( ! (idxParamBegin < idxParamEnd && idxParamEnd <= idxNextParamBegin)) {
                        idxParamBegin = idxNextParamBegin;
                        idxNextParamBegin = indexOf(paramBegin, idxParamBegin + paramBegin.length());
                    }
                }

                /* 提取 TEXT */

                String text = template.substring(idxCurrent, idxParamBegin);
                if (text.length() != 0) {
                    result.add(new Part(Type.TEXT, text));
                    idxCurrent = idxParamBegin;
                }

                /* 提取 PARAM */

                if (idxParamBegin != template.length()) {
                    String param = template.substring(idxParamBegin + paramBegin.length(), idxParamEnd).trim();
                    if (param.length() != 0) {
                        result.add(new Part(Type.PARAM, param));
                    }
                    idxCurrent = idxParamEnd + paramEnd.length();
                }
            }

            return result;
        }

        int indexOf(String str, int fromIndex) {
            int result = template.indexOf(str, fromIndex);
            return result != -1 ? result : template.length();
        }
    }
}
