package team.project.module.util.filestorage.export.model.query;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import team.project.module.util.filestorage.export.util.FileStorageUtil;

@Getter
@Setter
public class UploadFileQO {

    /**
     * <p>  指定存储目录
     * <br> 默认使用根目录，根目录用 "/"（传 null 或 "" 也表示使用根目录）
     *
     * <p>  路径规则见本模块的 package-info.java
     * <p>  处理路径、文件名可以使用工具类：{@link FilenameUtils}、{@link FileStorageUtil}
     * */
    String targetFolder = "/";

    /**
     * <p> 指定文件名（包括扩展名！）
     * <p> 传 null 或 "" 则不指定
     * */
    String targetFilename = null;

    /**
     * <p>  如果文件已存在，是否覆盖
     *
     * <p>  注意，该值传 ture 仍可能覆盖失败，例如：
     * <p>  如果将文件上传到本地文件系统：
     * <br> 当存在文件夹 /a/b 时，无法在文件夹 /a 下创建文件名为 b（无扩展名）的文件
     * <br> 因为文件系统要求文件夹和文件不能同名，而这里的命名 'b' 冲突
     * <br> 即使 overwrite = true 也无法覆盖
     * <p>  如果将文件上传到云存储：
     * <br> 云存储通过键名唯一标识存储的文件，没有文件目录层级结构的关系
     * <br> 当存在文件 /a/b/c.txt 时，其实并不存在目录 /a/b
     * <br> 创建名为 /a/b（无扩展名）的文件不会发生冲突
     * */
    boolean overwrite = false;
}
