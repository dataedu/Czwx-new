package com.dk.mp.ydqj.entity;

/**
 * 请假实体
 * @author admin
 *
 */
public class Leave {
	private String id;//id
	private String status;//状态
	private String userId;//用户id
	private String jssj;//结束时间
	private String kssj;//开始时间
	private String qjlx;//请假类型
	private String sqsj;//申请时间
	private String spsj;//审批时间
	private String qjts;//请假天数
	private String fdy;//辅导员
	private String sqr;//申请人
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj) {
		this.jssj = jssj;
	}
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj) {
		this.kssj = kssj;
	}
	public String getQjlx() {
		return qjlx;
	}
	public void setQjlx(String qjlx) {
		this.qjlx = qjlx;
	}
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	public String getSpsj() {
		return spsj;
	}
	public void setSpsj(String spsj) {
		this.spsj = spsj;
	}
	public String getQjts() {
		return qjts;
	}
	public void setQjts(String qjts) {
		this.qjts = qjts;
	}
	public String getFdy() {
		return fdy;
	}
	public void setFdy(String fdy) {
		this.fdy = fdy;
	}
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	
	
}
