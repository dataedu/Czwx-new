package com.dk.mp.apps.hy.entity;

import java.util.List;

public class Ks {
	private String idUser;//用户id
	private String name;//用户姓名
	private String photo;//用户姓名用户姓名
	private String tels;
	
	public String getTels() {
		return tels;
	}
	public void setTels(String tels) {
		this.tels = tels;
	}
	private List<String> list;
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
