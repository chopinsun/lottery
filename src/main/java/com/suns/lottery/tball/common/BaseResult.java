package com.suns.lottery.tball.common;

import com.alibaba.fastjson.JSONObject;
import com.jcloud.recordcenter.client.vo.PageInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @program: recordcenter
 * @description: 通用响应
 * @author: chopin.sun
 * @create: 2019-06-14 10:48
 **/

public class BaseResult<T> implements Serializable{
    @Setter
    @Getter
    private T data;
    @Setter
    @Getter
    private Integer code;
    @Setter
    @Getter
    private String message;
    @Setter
    @Getter
    private boolean isSuccess;
    @Setter
    @Getter
    private PageInfo page;
    @Setter
    @Getter
    private Map<String,Object> extra;
    @Setter
    @Getter
    private Throwable error;

    private String requestId;


    public BaseResult(){
        this.isSuccess = true;
    }

    public BaseResult(T data) {
        this.data = data;
        this.isSuccess = true;
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMessage();
    }

    public BaseResult(T data, Integer code, String message, boolean isSuccess) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public BaseResult(T data, Integer code, String message, boolean isSuccess, PageInfo page) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
        this.page = page;
    }

    public BaseResult(T data, Integer code, String message, boolean isSuccess, PageInfo page, Map<String, Object> extra) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
        this.page = page;
        this.extra = extra;
    }

    public String getRequestId() {
        if(StringUtils.isBlank(requestId)){
            this.requestId = UUID.randomUUID().toString().replaceAll("-","");
        }
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + JSONObject.toJSONString(data) +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                ", page=" + page +
                ", extra=" + JSONObject.toJSONString(extra) +
                '}';
    }
}
