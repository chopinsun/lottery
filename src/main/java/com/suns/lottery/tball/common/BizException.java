package com.suns.lottery.tball.common;

import com.jcloud.recordcenter.client.dictionary.ExceptionLevelEnum;
import lombok.Data;

/**
 * service异常
 */
@Data
public class BizException extends RuntimeException {

    private CodeMessage error;
    private Throwable cause;
    private ExceptionLevelEnum level= ExceptionLevelEnum.LEVEL_2;

    private Object data;


    public BizException(CodeMessage error) {
        super(error.getMessage());
        this.error = error;
    }
    public BizException(CodeMessage error,Object data) {
        super(error.getMessage());
        this.error = error;
        this.data = data;
    }
    public BizException(CodeMessage error,Throwable cause) {
        super(error.getMessage());
        this.error = error;
        this.cause = cause;
    }
    public BizException(CodeMessage error,Throwable cause,Object data) {
        super(error.getMessage());
        this.error = error;
        this.cause = cause;
        this.data = data;
    }

    public BizException(CodeMessage error,Throwable cause,ExceptionLevelEnum level) {
        super(error.getMessage());
        this.error = error;
        this.cause = cause;
        this.level = level;
    }

    public BizException(String message) {
        super(generateMessageString(message));
        this.error = defaultError(message);
    }
    public BizException(String message,Object data) {
        super(generateMessageString(message));
        this.error = defaultError(message);
        this.data = data;
    }
    public BizException(String message,CodeMessage error) {
        super(generateMessageString(error,message));
        this.error = error;
    }

    public BizException(String message,CodeMessage error,Object data) {
        super(generateMessageString(error,message));
        this.error = error;
        this.data = data;
    }

    public BizException(String message,CodeMessage error,Throwable cause) {
        super(generateMessageString(error,message),cause);
        this.error = error;
        this.cause = cause;
    }

    public BizException(String message,CodeMessage error,Throwable cause,Object data) {
        super(generateMessageString(error,message),cause);
        this.error = error;
        this.cause = cause;
        this.data = data;
    }


    public BizException(String message,Throwable cause,ExceptionLevelEnum level) {
        super(generateMessageString(message),cause);
        this.error = defaultError(message);
        this.cause = cause;
        this.level = level;

    }
    public BizException(String message,CodeMessage error,Throwable cause,ExceptionLevelEnum level) {
        super(generateMessageString(message),cause);
        this.error = CustomCode.builder().code(error.getCode()).message(error.getMessage()+" | detail--->"+message).build();
        this.cause = cause;
        this.level = level;

    }
    public BizException(Throwable cause) {
        super(cause);
        this.error = defaultError(cause.getMessage());
        this.cause = cause;
    }


    private static CodeMessage defaultError(String message){
        return CustomCode.builder()
                .code(ResultCode.FAIL_BUSINESS.getCode())
                .message(generateMessageString(message))
                .build();
    }

    private static String generateMessageString(String message){
        return "["+ ResultCode.FAIL_BUSINESS.getMessage() + "]  " +message;
    }

    private static String generateMessageString(CodeMessage error,String message){
        return message + " | detail--->"+error.getMessage();
    }

}
