package com.dk.mp.sxxj.entity;

/**
 * Created by abc on 2018-4-17.
 */

public class Detail {
	private String nr;
	private String fjId;
	private String fjName;
	private String typeName;
	private String typeId;
	private String downloadUrl;
	private String downloadType;

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getFjId() {
		return fjId;
	}

	public void setFjId(String fjId) {
		this.fjId = fjId;
	}

	public String getFjName() {
		return fjName;
	}

	public void setFjName(String fjName) {
		this.fjName = fjName;
	}


}
