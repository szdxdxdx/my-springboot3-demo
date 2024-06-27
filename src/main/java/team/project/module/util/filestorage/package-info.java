/**
 * <h1> 介绍
 * <p>  本模块负责处理文件存储，为其他业务模块提供文件存储服务
 * <p>  本模块是一个服务模块，而不是一个业务模块
 * <p>  本模块绑定如下文件
 * <ul>
 * <li> /config/file-storage-config.yml
 * <br> 配置文件
 * <li> /static/html/test/file-storage.html
 * <br> 用于开发阶段的文件存取测试
 * </ul>
 * <h1> 存储方式
 * <p>  本模块提供“本地存储”和“云存储”两类存储方式接口
 * <p>
 * <h2> 本地存储
 * <p>  在哪里运行这个后端，哪里就是“本地”
 * <p>  开发阶段，后端运行在各编码人员的 Windows 电脑上，那么本地文件系统就是本地硬盘
 * <p>  部署后，后端可能运行在 linux 服务器上，那么本地文件系统就是服务器的硬盘
 * <p>
 * <h2> 云存储
 * <p>  目前用阿里云 OSS 作云存储
 * <a href="https://help.aliyun.com/zh/oss/developer-reference/java/">
 *      如何快速使用OSS Java SDK完成常见操作
 * </a>
 * <p>
 * <h1> 路径规则
 * <p>  本模块中，所有涉及“文件路径”的填写（配置文件、接口入参），均遵守如下规则：
 * <ul>
 * <li> 使用 / 作为分隔符
 * <li> 不要出现连续的 /
 * <li> 如果是相对路径，以 / 开头
 * <li> 不以 / 或 . 结尾
 * <li> 不要使用 /.. 和 /. 跨文件夹
 * <li> 不含非法字符： * : ? " ' < > |
 * </ul>
 * <p>
 */
package team.project.module.util.filestorage;
