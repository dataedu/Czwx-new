package com.dk.mp.ydyx.entity;

/**
 * 信息采集实体类.
 * 
 * @author rm.zhao
 * 
 *         2015-5-25
 */
public class UserInfo {
	private String photoUser;// 用户头像
	private String address;// 到达地点
	private String timeArrival;// 到达时间
	private String way;// 到达方式
	private String banci;// 车次班次
	private String renshu;// 随行人数
	private String xszj;// 随行人数

	public String getXszj() {
		return xszj;
	}

	public void setXszj(String xszj) {
		this.xszj = xszj;
	}

	public String getPhotoUser() {
		return photoUser;
	}

	public void setPhotoUser(String photoUser) {
		this.photoUser = photoUser;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimeArrival() {
		return timeArrival;
	}

	public void setTimeArrival(String timeArrival) {
		this.timeArrival = timeArrival;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getBanci() {
		return banci;
	}

	public void setBanci(String banci) {
		this.banci = banci;
	}

	public String getRenshu() {
		return renshu;
	}

	public void setRenshu(String renshu) {
		this.renshu = renshu;
	}

}
