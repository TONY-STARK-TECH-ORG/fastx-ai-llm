package com.fastx.ai.llm.web.controller.entity;

/**
 * @author stark
 */
public class Response<T> {

    /**
     * indicate that request or visit limited state
     * warning: do not use this as a biz field.
     */
    private int code;
    private String msg;
    private T data;

    public static <T> Response<T> success(T data) {
        Response response = new Response();
        response.setCode(200);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> error(String msg) {
        Response response = new Response();
        response.setCode(500);
        response.setMsg(msg);
        return response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
