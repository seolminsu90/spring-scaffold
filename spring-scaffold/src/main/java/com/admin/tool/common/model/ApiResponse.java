package com.admin.tool.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int code;
    private List<T> list;
    private T data;

    private ApiResponse(int code) {
        this.code = code;
    }

    private ApiResponse(int code, List<T> list) {
        this.code = code;
        this.list = list;
    }

    private ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static ApiResponse<Void> of(int code) {
        return new ApiResponse<>(code);
    }

    public static <T> ApiResponse<T> of(int code, List<T> list) {
        return new ApiResponse<>(code, list);
    }

    public static <T> ApiResponse<T> of(int code, T data) {
        return new ApiResponse<>(code, data);
    }

    private Long cnt;

    public void setPageCnt(long cnt) {
        this.cnt = cnt;
    }
}
