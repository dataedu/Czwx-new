package com.dk.mp.apps.gzbxnew.entity;

import java.io.Serializable;

/**
 * 故障报修实体
 * @author admin
 *
 */
public class Gzbx implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;//报修人
	private String id;//报修id
	private String type;//类型
	private String bxbm;
	private String bxdh;
	private String wxlx;
	private String shzt;
	private String pjzt;
	private String bxnr;
	private String time;
	private String bxdd;
	private String wxry;
	private String pjdj;//评价等级
	private String pjyj;//评价意见
	private String bxrlxdh;//联系电话
	private String fj;//附件
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBxbm() {
		return bxbm;
	}
	public void setBxbm(String bxbm) {
		this.bxbm = bxbm;
	}
	public String getBxdh() {
		return bxdh;
	}
	public void setBxdh(String bxdh) {
		this.bxdh = bxdh;
	}
	public String getWxlx() {
		return wxlx;
	}
	public void setWxlx(String wxlx) {
		this.wxlx = wxlx;
	}
	public String getShzt() {
		return shzt;
	}
	public void setShzt(String shzt) {
		this.shzt = shzt;
	}
	public String getPjzt() {
		return pjzt;
	}
	public void setPjzt(String pjzt) {
		this.pjzt = pjzt;
	}
	public String getBxnr() {
		return bxnr;
	}
	public void setBxnr(String bxnr) {
		this.bxnr = bxnr;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBxdd() {
		return bxdd;
	}
	public void setBxdd(String bxdd) {
		this.bxdd = bxdd;
	}
	public String getWxry() {
		return wxry;
	}
	public void setWxry(String wxry) {
		this.wxry = wxry;
	}
	public String getPjdj() {
		return pjdj;
	}
	public void setPjdj(String pjdj) {
		this.pjdj = pjdj;
	}
	public String getPjyj() {
		return pjyj;
	}
	public void setPjyj(String pjyj) {
		this.pjyj = pjyj;
	}
	public String getBxrlxdh() {
		return bxrlxdh;
	}
	public void setBxrlxdh(String bxrlxdh) {
		this.bxrlxdh = bxrlxdh;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	
}
