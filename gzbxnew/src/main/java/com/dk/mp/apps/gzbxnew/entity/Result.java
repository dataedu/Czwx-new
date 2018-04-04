package com.dk.mp.apps.gzbxnew.entity;

/**
 * 返回结果
 * @author admin
 *
 */
public class Result {
	private Object data;
	private String msg;
	private int status;
	private int code;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
}
