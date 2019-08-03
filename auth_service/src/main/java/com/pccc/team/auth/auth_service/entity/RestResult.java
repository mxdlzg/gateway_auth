package com.pccc.team.auth.auth_service.entity;




import com.pccc.team.auth.auth_service.enums.ResponseEnums;

import java.sql.Timestamp;
import java.util.Date;

public class RestResult<T> {
    // 状态码
    private String code;

    /**
     * 请求返回的提示信息，给前端进行页面展示的信息
     */
    private String msg;

    private boolean success;
    /**
     * 请求是否成功
     */
    private String status;

    /**
     * 成功时返回的数据，失败时返回具体的异常信息
     */
    private T data;

    /**
     * 服务器当前时间（添加该字段的原因是便于查找定位请求时间，因为实际开发过程中服务器时间可能跟本地时间不一致，加上这个时间戳便于日后定位）
     */
    private Timestamp currentTime;

    public RestResult() {
        this.status = "ok";
        this.success = true;
        this.code = ResponseEnums.SUCCESS_OPTION.getCode();
        this.msg = ResponseEnums.SUCCESS_OPTION.getMsg();
        this.data = null;
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public RestResult(boolean success, ResponseEnums enums) {
        super();
        this.success = success;
        this.status = "error";
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public RestResult(T data) {
        this.status = "ok";
        this.success = true;
        this.code = ResponseEnums.SUCCESS_OPTION.getCode();
        this.msg = ResponseEnums.SUCCESS_OPTION.getMsg();
        this.data = data;
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public RestResult(ResponseEnums enums, T data) {
        this.status = "ok";
        this.success = true;
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.data = data;
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public RestResult(ResponseEnums enums, T data,String status) {
        this.status = status;
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.data = data;
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public RestResult(boolean success, String code, String msg) {
        this.success = success;
        this.code=  code;
        this.msg = msg;
        this.currentTime = new Timestamp(new Date().getTime());
    }

    public static RestResult<ResponseEnums> fail(ResponseEnums error){
        return new RestResult<>(false,error);
    }

    @Override
    public String toString() {
        return "{" +
                "\"status\":\"" + status +
                "\", \"code\":\"" + code + '\"' +
                ", \"data\":" + data +
                ", \"msg\":\"" + msg +
                "\", \"currentTime\":\"" + currentTime +"\"}";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Timestamp currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
