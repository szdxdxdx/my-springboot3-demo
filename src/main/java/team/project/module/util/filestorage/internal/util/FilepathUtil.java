package team.project.module.util.filestorage.internal.util;

/**
 * <p> 处理文件路径的工具类
 * <p> 路径规则详见 {@link team.project.module.util.filestorage package-info.java}
 * */
public class FilepathUtil {

    /**
     * <p>  按如下规则处理路径中的分割符：
     * <ol>
     * <li> 替换 '\' 为 '/'，统一使用 '/' 作为文件夹的分隔符
     * <li> 合并连续的斜杠为单个斜杠
     * <li> 移除路径末尾的斜杠（如果存在）
     * <li> <u>不移除</u>路径最打头的斜杠（如果存在）
     * <li> <u>不处理</u>路径中的 ".." 和 "."
     * </ol>
     * @return 处理后的路径字符串
     * */
    public static String fixSeparator(String path) {
        String replaced = path.replace('\\', '/').replaceAll("/+", "/");
        return replaced.endsWith("/") ? replaced.substring(0, replaced.length() - 1) : replaced;
    }

    /**
     * 判断文件路径中是否含有非法字符
     * */
    public static boolean hasInvalidChar(String filePath) {
        return -1 != filePath.indexOf(':')
            || -1 != filePath.indexOf('*')
            || -1 != filePath.indexOf('?')
            || -1 != filePath.indexOf('\"')
            || -1 != filePath.indexOf('\\')
            || -1 != filePath.indexOf('<')
            || -1 != filePath.indexOf('>')
            || -1 != filePath.indexOf('|');
    }

    /**
     * 判断文件路径中是否含 "/.." 或 "/."表示相对路径的部分
     * */
    public static boolean hasRelativePathPart(String filePath) {
        return filePath.contains("/.");
    }
}
