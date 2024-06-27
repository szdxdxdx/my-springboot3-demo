/**
 * <h1> 介绍
 * <p>  本模块负责处理用户角色的校验，提供用于角色鉴权的方法
 * <p>  同时，本模块整合了 Sa-Token 框架，使得项目可以使用框架提供的注解和方法进行鉴权
 * <p><a href="https://sa-token.cc/doc.html#/">
 *      Sa-Token 说明文档 </a>
 * <br> F12 在控制台执行 <b> localStorage.isStarRepo = new Date().getTime() </b> 后刷新页面，可绕过弹窗
 * <p>
 * <h1> 对谁鉴权
 * <p>  对请求的发起者进行鉴权，鉴定其“是否拥有权力”来执行该操作
 * <p>  例如：<u>管理员给小组成员发送通知</u>
 * <br> 需验证发起请求的用户是否拥有“管理员”角色，确保其有“发送通知”的权力。
 *      业务逻辑还可能会要求：只能给“该管理员所管理的小组的成员”发送通知，不能给其他小组的成员。
 * <br> “小组成员”不是执行者，其身份的校验不属于鉴权，更多是属于保证业务逻辑正确和保证数据正确
 * <p>
 * <h1> 使用 Sa-Token 提供的注解进行鉴权
 * <p>  Sa-Token 提供注解鉴权和方法鉴权两种方式，推荐使用注解鉴权
 * <p>  使用注解使得将鉴权与业务代码更分离，而且代码写起来更简单，只需在 controller 的方法上加上鉴权注解即可
 * <p>  框架会识别出携带有鉴权注解的方法，收到 http 请求时，会先在 interceptor 层校验身份，
 *      校验通过后请求才会进入 controller 层，所以用注解比调方法执行起来更高效
 * <p>
 * <h2> 登录校验
 * <p>  使用 <b> @SaCheckLogin </b> 修饰的方法，只有登录之后才能进入
 * <p>  已做了配置，用户登录后框架保存 userId，之后都是通过 userId 进行其他校验
 * <p>
 * <h2> 获取当前登录用户的 userId
 * <p>  <b> String userId = (String)( StpUtil.getLoginId() ); </b>
 * <p>
 * <h2> 角色校验
 * <p>  使用 <b> @SaCheckRole </b> 修饰的方法，必须具有指定角色才能进入
 * <p>  注意：
 * <ol>
 * <li> 角色码请使用枚举常量：{@link team.project.module.auth.export.model.enums.AuthRole AuthRole}
 * <li> @SaCheckRole 自带登录校验，不需要多写 @SaCheckLogin
 * </ol>
 * <h1> 进一步鉴权
 * <p>  Sa-Token 框架可以完成一部分鉴权工作，除此之外本模块还提供有用于进一步角色鉴权的方法，
 * <br> 这些进一步的鉴权方法，实际上是整合了各模块所提供的，关于判断用户身份、角色的方法
 * <br> 详见：{@link team.project.module.auth.export.service.AuthServiceI AuthServiceI}
 * <p>
 */
package team.project.module.auth;
