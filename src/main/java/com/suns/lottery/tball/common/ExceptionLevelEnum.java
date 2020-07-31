package com.suns.lottery.tball.common;

/**
 * @program: recordcenter
 * @description:
 * @author: chopin.sun
 * @create: 2019-06-21 16:59
 **/

public enum ExceptionLevelEnum {

    LEVEL_0(0,"最高级"),
    LEVEL_1(1,"较高级"),
    LEVEL_2(2,"一般"),
    LEVEL_3(3,"不重要");

    private Integer code;
    private String name;

    ExceptionLevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ExceptionLevelEnum of(Integer code) {
        for (ExceptionLevelEnum enumObj : ExceptionLevelEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }
}
