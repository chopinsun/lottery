package com.suns.lottery.tball.common;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.PageInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @program: recordcenter
 * @description: json返回类型
 * @author: chopin.sun
 * @create: 2019-06-14 15:13
 **/
public class JsonResult<T> extends BaseResult<T>{
    private static ThreadLocal<JsonResult> local = new ThreadLocal<>();

    private JsonResult(){}

    private JsonResult(JsonResult.Builder<T> builder){
        this.setRequestId(builder.requestId);
        this.setData(builder.data);
        this.setSuccess(builder.isSuccess);
        this.setPage(builder.page);
        this.setExtra(builder.extra);
        this.setCode(builder.codeMessage.getCode());
        this.setMessage(builder.codeMessage.getMessage());
        this.setError(builder.error);
    }

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }

    public static <T> JsonResult<T> success(){
        return success(null);
    }
    public static <T> JsonResult<T> success(T data){
        return success(null,data);
    }
    public static <T> JsonResult<T> success(T data,Map<String,Object> extra){
        return success(ResultCode.SUCCESS,data,null,extra);
    }
    public static <T> JsonResult<T> success(T data, PageInfo pageInfo){
        return success(ResultCode.SUCCESS,data,pageInfo,null);
    }
    public static <T> JsonResult<T> success(String msg, T data){
        return success(CustomCode.builder().code(ResultCode.SUCCESS.getCode()).message(StringUtils.isNotBlank(msg)?msg:ResultCode.SUCCESS.getMessage()).build(),data,null,null);
    }

    public static <T> JsonResult<T> success(CodeMessage code, T data, PageInfo pageInfo, Map<String,Object> extra){
        JsonResult<T> result = getInstance();
        result.setSuccess(true);
        result.setData(data);
        result.setCode(code.getCode());
        result.setMessage(code.getMessage());
        result.setPage(pageInfo);
        result.setExtra(extra);
        return result;
    }

    public static <T> JsonResult<T> fail(CodeMessage code){
        JsonResult<T> result = getInstance();
        result.setCode(code.getCode());
        result.setMessage(code.getMessage());
        result.setSuccess(false);
        return result;
    }


    public static <T> JsonResult<T> fail(String message){
        return fail(ResultCode.ERROR_UNKNOWN,message);
    }
    public static <T> JsonResult<T> fail(String message,T data){
        return fail(ResultCode.ERROR_UNKNOWN,message,data);
    }
    public static <T> JsonResult<T> fail(CodeMessage code,String message){
        CodeMessage cm = CustomCode.builder().code(code.getCode()).message(code.getMessage() +" | detail--->"+ message).build();
        return fail(cm);
    }

    public static <T> JsonResult<T> fail(CodeMessage code,String message,T data){
        CodeMessage cm = CustomCode.builder().code(code.getCode()).message(code.getMessage() +" | detail--->"+ message).build();
        return fail(cm,data);
    }

    public static <T> JsonResult<T> fail(CodeMessage code,String message,T data,Map<String,Object> extra){
        CodeMessage cm = CustomCode.builder().code(code.getCode()).message(code.getMessage() +" | detail--->"+ message).build();
        return fail(cm,data,extra);
    }

    public static <T> JsonResult<T> fail(CodeMessage code, T data){
        return fail(code,data,null);
    }

    public static <T> JsonResult<T> fail(CodeMessage code, T data, Map<String,Object> extra){
        JsonResult<T> result = getInstance();
        result.setSuccess(false);
        result.setData(data);
        result.setCode(code.getCode());
        result.setMessage(code.getMessage());
        result.setExtra(extra);
        return result;
    }
    public static <T> JsonResult<T> error(CodeMessage code, T data, Map<String,Object> extra,Throwable e){
        JsonResult<T> result = getInstance();
        result.setSuccess(false);
        result.setData(data);
        result.setCode(code.getCode());
        result.setMessage(code.getMessage());
        result.setExtra(extra);
        result.setError(e);
        return result;
    }

    public static <T> JsonResult<T> getInstance(){
        JsonResult<T> result = local.get();
        if(result == null){
            result = JsonResult.<T>builder().build();
            local.set(result);
        }
        return result;
    }

    public static void release(){
        local.remove();
    }

    public JSONObject toJson(){
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    public String toJsonString(){
        return JSONObject.toJSONString(this);
    }

    public static class Builder<T>{
        private String requestId="";
        private T data = null;
        private CodeMessage codeMessage = ResultCode.SUCCESS;
        private boolean isSuccess = true;
        private PageInfo page = null;
        private Map<String,Object> extra = null;
        private Throwable error;


        public JsonResult.Builder<T> data(T data){
            this.data= data;
            return this;
        }

        public JsonResult.Builder<T> code(CodeMessage codeMessage){
            this.codeMessage= codeMessage;
            return this;
        }

        public JsonResult.Builder<T> isSuccess(boolean isSuccess){
            this.isSuccess= isSuccess;
            return this;
        }

        public JsonResult.Builder<T> page(PageInfo page){
            this.page= page;
            return this;
        }

        public JsonResult.Builder<T> extra(Map<String,Object> extra){
            this.extra= extra;
            return this;
        }
        public JsonResult.Builder<T> error(Throwable error){
            this.error= error;
            return this;
        }

        public JsonResult<T> build(){
            return new JsonResult<>(this);
        }

    }

}
