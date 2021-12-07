package com.yuma.apigateway.taskappapigateway.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * errorハンドリングクラス
 * 
 */
@ControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @org.springframework.web.bind.annotation.ExceptionHandler({ NoNormalResponseError.class })
    @ResponseBody
    public Map<String, Object> handleError() {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("message", "なんか無理でした。詳細はLog見てください");
        errorMap.put("status", HttpStatus.BAD_GATEWAY);
        return errorMap;
    }
}