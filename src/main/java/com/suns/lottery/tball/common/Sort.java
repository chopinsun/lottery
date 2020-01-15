package com.suns.lottery.tball.common;

public enum Sort {
    ASC("asc"),
    DESC("desc"),

    ;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    Sort(String code) {
        this.code = code;
    }
}
