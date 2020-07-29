package com.suns.lottery.tball.common;

/**
 * @program: recordcenter
 * @description:
 * @author: chopin.sun
 * @create: 2019-06-14 11:08
 **/

public enum ResultCode implements CodeMessage{
    SUCCESS(0,"成功"),

    ERROR_INNER(9998,"内部异常,联系管理员"),
    ERROR_UNKNOWN(9999,"未知异常,联系管理员"),
    ERROR_FILEUPLOAD(9001,"文件上传失败"),

    ILLEGAL_PARAMS(8888,"非法参数"),
    FAIL_BUSINESS(2000,"业务异常"),
    FAIL_BUSINESS_ILLEGAL_STATUS(2001,"非法的备案状态"),
    FAIL_BUSINESS_UNSUPPORT_FILETYPE(2002,"文件格式不支持"),
    FAIL_RECORD_ALREADY_EXIST(2003,"记录已经存在"),
    FAIL_BUSINESS_ILLEGAL_USER(2004,"非法用户"),

    FAIL_FILE_IDCARD_OCR(2101,"身份证校验失败，身份证照片无法识别"),
    FAIL_FILE_IDCARD_ILLAGE(2102,"身份证校验失败，身份证照片和填写信息不一致"),
    FAIL_FILE_IDCARD_SM(2103,"身份证校验失败，身份证信息实名验证不通过"),
    FAIL_FILE_IDCARD_WW(2104,"身份证校验失败，身份证照片网纹校验不通过"),
    FAIL_FILE_IDCARD_NET(2105,"身份证校验失败，网络异常"),

    FAIL_FILE_COMPANY_OCR(2201,"营业执照校验失败，营业执照照片无法识别"),
    FAIL_FILE_COMPANY_ILLAGE(2202,"营业执照校验失败，营业执照照片和填写信息不一致"),
    FAIL_FILE_COMPANY_FE(2203,"营业执照校验失败，营业执照信息验证不通过"),
    FAIL_FILE_COMPANY_NET(2204,"营业执照校验失败，网络异常"),

//    FAIL_FILE_PERSON(2301,"活体校验失败"),
    FAIL_FILE_PERSON_ILLAGE_PARAM(2301,"您的备案信息不存在"),
    FAIL_FILE_PERSON_ILLAGE_STAUTS(2302,"当前备案状态不符"),
    FAIL_FILE_PERSON_SAVE_FAIL(2303,"当面核验照片保存失败"),
    FAIL_FILE_PERSON_PIC(2304,"当面核验照片比对失败"),
    FAIL_FILE_PERSON_NET(2305,"当面核验照验证失败"),

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
