package com.dk.mp.ydlx.entity;

import java.util.List;

/**
 * 流程办理实体类.
 * 
 * @since
 * @version2015-3-19
 * @author rm.zhao
 */

public class LeaveLinkd {
	private String linkId;// 流程id
	private String name;// 办理项
	private String status;// 办理状态 0:通过 1:不通过 2:可办理 3:无需办理
	private String content;// 办理内容
	private String address;// 办理地址
	private String tip;// 提示信息
	private String tipDepart;//提示
	private Boolean haveSub;//是否有子项 
    private List<LeaveMore> list;
	
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getHaveSub() {
		return haveSub;
	}

	public String getTipDepart() {
		return tipDepart;
	}

	public void setTipDepart(String tipDepart) {
		this.tipDepart = tipDepart;
	}

	public void setHaveSub(Boolean haveSub) {
		this.haveSub = haveSub;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<LeaveMore> getList() {
		return list;
	}

	public void setList(List<LeaveMore> list) {
		this.list = list;
	}


}
