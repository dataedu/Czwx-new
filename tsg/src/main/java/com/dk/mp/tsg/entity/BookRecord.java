package com.dk.mp.tsg.entity;

/**
 * 借书记录。
 * @since 
 * @version 2013-5-10
 * @author janabo
 */
public class BookRecord {
	private static final long serialVersionUID = 1L;
	private String id;//	id
	private String jssj;//	借书时间
	private String yjhssj;//	预计归还时间
	private String name;//	图书名称代码

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getYjhssj() {
		return yjhssj;
	}

	public void setYjhssj(String yjhssj) {
		this.yjhssj = yjhssj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
