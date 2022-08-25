package com.admin.tool.common.config.exception;

public class ApiException extends RuntimeException{

    // Server API 응답값 주려면 추가 후 Global에서 Body에 Write

    public ApiException(String msg){
        super(msg);
    }
}
