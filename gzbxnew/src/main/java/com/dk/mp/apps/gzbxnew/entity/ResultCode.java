package com.dk.mp.apps.gzbxnew.entity;

/**
 * 网络请求 返回结果实体
 * @author admin
 *
 */
public class ResultCode {
	private String data;
    private int status;
    private String msg;
    private int code;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
