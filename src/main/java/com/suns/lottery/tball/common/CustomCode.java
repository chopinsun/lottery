package com.suns.lottery.tball.common;

import lombok.Builder;
import lombok.Data;

/**
 * @program: recordcenter
 * @description: 自定义code
 * @author: chopin.sun
 * @create: 2019-06-14 15:26
 **/
@Builder
@Data
public class CustomCode implements CodeMessage{

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static CustomCode illegalArgument(String message){
        return CustomCode.builder().code(ResultCode.ILLEGAL_PARAMS.getCode()).message(message).build();
    }
    public static CustomCode innerError(String message){
        return CustomCode.builder().code(ResultCode.ERROR_INNER.getCode()).message(message).build();
    }
}
