package com.dk.mp.apps.gzbxnew.entity;

/**
 * 故障报修角色权限
 * @author admin
 *
 */
public class GzbxRole {
	private boolean bxsh;//有报修审核（管理员）权限,false:没有
	private boolean xctb;//有现场提报（维修）权限,false:没有
	
	public boolean isBxsh() {
		return bxsh;
	}
	public void setBxsh(boolean bxsh) {
		this.bxsh = bxsh;
	}
	public boolean isXctb() {
		return xctb;
	}
	public void setXctb(boolean xctb) {
		this.xctb = xctb;
	}
	
	
}
