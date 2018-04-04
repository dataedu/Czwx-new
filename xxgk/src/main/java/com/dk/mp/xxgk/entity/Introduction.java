package com.dk.mp.xxgk.entity;

/**功能:学院简介.
 * 
 * @since 
 * @version 2013-1-6
 * @author admin
 */
public class Introduction {

	private String idIntroduction="";
	private String content="";
    private String timeStamp="";
    private String img="";
	public String getIdIntroduction() {
		return idIntroduction;
	}

	public void setIdIntroduction(String idIntroduction) {
		this.idIntroduction = idIntroduction;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
