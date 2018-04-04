package com.dk.mp.wspj.entity;

import java.util.List;
/**
 * 
* 教务管理系统
* 江苏达科版权所有，禁止外泄以及用于其他的商业目
* http://www.datatech-info.com/
* @ClassName: Pjzbx
* @Description: 评教指标项
* @author wangqiang 
* @date 2014-3-24
 */
public class Pjzbx{
	
	private String zbdm;
	private String zbmc;
	private Float lhfz;
	private Integer xh;
	private List<Pjzbx> ejzbList;
	private List<Pfbz> pfbzList;
	private Float pjdf;
	private String pjyj;
	private String sfmf;
	
	
	
	
	public String getZbdm() {
		return zbdm;
	}
	public void setZbdm(String zbdm) {
		this.zbdm = zbdm;
	}
	public String getZbmc() {
		return zbmc;
	}
	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}
	public Float getLhfz() {
		return lhfz;
	}
	public void setLhfz(Float lhfz) {
		this.lhfz = lhfz;
	}
	public Integer getXh() {
		return xh;
	}
	public void setXh(Integer xh) {
		this.xh = xh;
	}
	public List<Pjzbx> getEjzbList() {
		return ejzbList;
	}
	public void setEjzbList(List<Pjzbx> ejzbList) {
		this.ejzbList = ejzbList;
	}
	public List<Pfbz> getPfbzList() {
		return pfbzList;
	}
	public void setPfbzList(List<Pfbz> pfbzList) {
		this.pfbzList = pfbzList;
	}
	public Float getPjdf() {
		return pjdf;
	}
	public void setPjdf(Float pjdf) {
		this.pjdf = pjdf;
	}
	public String getPjyj() {
		return pjyj;
	}
	public void setPjyj(String pjyj) {
		this.pjyj = pjyj;
	}
	public String getSfmf() {
		return sfmf;
	}
	public void setSfmf(String sfmf) {
		this.sfmf = sfmf;
	}
	
	
	
	
}
