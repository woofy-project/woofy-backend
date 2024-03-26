package com.hmpr.woofy.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private String message;
    private T data;

    public static <T> CommonApiResponse<T> createSuccess(T data) {
        return new CommonApiResponse<>(SUCCESS_STATUS, null, data);
    }

    public static CommonApiResponse createSuccessWithNoContent() {
        return new CommonApiResponse<>(SUCCESS_STATUS, null, null);
    }

    public static <T> CommonApiResponse<T> createError(String message) {
        return new CommonApiResponse<>(ERROR_STATUS, message, null);
    }

    private CommonApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;

    }
}
