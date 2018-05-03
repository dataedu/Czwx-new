package com.dk.mp.ksap.entity;

import java.util.List;

/**
 * Created by abc on 2018-4-17.
 */

public class KsapBean {
	private String bj;
	private String pc;
	private String ksrs;
	List<Ksap> list;
	private long totalCount=0;//总记录条数
	private int pageSize=0;//每页数据量
	private int currentPage=1;//当前页
	private int nextPage=0;//下一页
	private long totalPages=0;//总页数
	private String msg;//总页数

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBj() {
		return bj;
	}

	public void setBj(String bj) {
		this.bj = bj;
	}

	public String getKsrs() {
		return ksrs;
	}

	public void setKsrs(String ksrs) {
		this.ksrs = ksrs;
	}

	public List<Ksap> getList() {
		return list;
	}

	public void setList(List<Ksap> list) {
		this.list = list;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}
}
