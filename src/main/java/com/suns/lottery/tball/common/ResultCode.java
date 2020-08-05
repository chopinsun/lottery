package com.suns.lottery.tball.common;

/**
 * @program: recordcenter
 * @description:
 * @author: chopin.sun
 * @create: 2019-06-14 11:08
 **/

public enum ResultCode implements CodeMessage{
    SUCCESS(0,"成功"),
    ILLEGAL_USER(9000,"用户名或密码不正确"),
    ERROR_INNER(9998,"内部异常,联系管理员"),
    ERROR_UNKNOWN(9999,"未知异常,联系管理员"),

    ILLEGAL_PARAMS(8888,"非法参数"),

    NON_DATA(4001,"未查询到数据"),
    ;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
