package com.junhe.integral.common;

import java.io.Serializable;

/**
 * 响应数据
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;

    /**
     * 消息内容
     */
    private String msg = "操作成功";

    /**
     * 响应数据
     */
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public Result<T> ok(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.setData(data);
        return this;
    }

    public Result<T> ok(String msg, T data) {
        this.setMsg(msg);
        this.setData(data);
        return this;
    }

    public boolean success() {
        return code == 0;
    }

    public Result<T> error() {
        this.code = 500;
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> appOK(T data) {
        this.code = 200;
        this.setData(data);
        return this;
    }


    public Result<T> error(String msg) {
        this.code = 500;
        this.msg = msg;
        return this;
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
