package team.project.base.service.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> 自定义的服务端响应状态码
 * <p> 虽然和 http 的状态码一致，但注意：这个是 ServiceStatus，而不是 HttpStatus
 * */
@AllArgsConstructor
@Getter
public enum ServiceStatus {
    /* 成功响应 */
    SUCCESS              (200, "执行成功"), /* 请求成功，请求所希望数据将随此响应返回 */
    CREATED              (201, "创建成功"), /* 请求成功，并创建了新的资源 */
    NO_CONTENT           (204, "执行成功"), /* 请求成功，但服务器没有返回任何内容 */
    /* 客户端异常 */
    BAD_REQUEST          (400, "无效请求"), /* 客户端错误（请求语法错误、无效的请求），服务器无法理解 */
    UNAUTHORIZED         (401, "身份验证未通过"), /* 当前请求需要用户验证 */
    FORBIDDEN            (403, "拒绝执行"), /* 服务器理解请求，服务器拒绝执行 */
    NOT_FOUND            (404, "找不到资源"), /* 在服务器上找不到请求的资源 */
    REQUEST_TIMEOUT      (408, "超时"), /* 服务器等待请求时间过长 */
    CONFLICT             (409, "冲突"), /* 请求与服务器的当前状态冲突 */
    PAYLOAD_TOO_LARGE    (413, "请求体过大"), /* 上传的文件太大 */
    UNPROCESSABLE_ENTITY (422, "无法处理"), /* 请求格式正确，但语义错误 */
    TOO_MANY_REQUESTS    (429, "请求过多"), /* 短时间内发送过多请求 */
    /* 服务端异常 */
    INTERNAL_SERVER_ERROR(500, "服务器内部异常"), /* 服务器遇到了不知道如何处理的情况 */
    NOT_IMPLEMENTED      (501, "没有实现该功能"), /* 请求了未实现的功能 */

    ;
    private final int    statusCode;
    private final String statusText;

    /**
     * 判断是否是 5 开头的状态码（服务端异常）
     * */
    public boolean is5XX() {
        return statusCode / 100 == 5;
    }
}
