package team.project.base.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import team.project.base.service.status.ServiceStatus;

/**
 * 统一的后端响应
 * */
@Getter
public class Response<T> {

    @JsonProperty("status_code") private final int statusCode;
    @JsonProperty("status_text") private String    statusText;
    @JsonProperty("data")        private Object    data;

    public Response(ServiceStatus status) {
        this.statusCode = status.getStatusCode();
        this.statusText = status.getStatusText();
        this.data       = null;
    }

    public Response<T> statusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public Response<T> data(T data) {
        this.data = data;
        return this;
    }
}
