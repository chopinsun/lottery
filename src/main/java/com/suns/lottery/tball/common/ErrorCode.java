package com.suns.lottery.tball.common;

public enum ErrorCode implements CodeMessage{
    FAILURE(1, "失败"),
    INVALID_PARAMS(2, "请求参数错误"),
    DB(3, "数据库操作异常"),
    NULL_POINTER(4,"空指针"),
    FILE_UPLOAD(5,"文件上传"),
    GET_ERP(6,"获取erp异常"),
    CALL_OUTER_API(7,"调用外部接口异常"),


    UNKNOWN(999,"未知异常"),
    ;

    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
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
