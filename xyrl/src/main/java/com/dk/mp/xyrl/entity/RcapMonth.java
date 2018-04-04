package com.dk.mp.xyrl.entity;

import java.io.Serializable;

import com.dk.mp.core.util.TimeUtils;

public class RcapMonth implements Serializable {
	private  int jumpMonth = 0; //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private  int jumpYear = 0; //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	public  int getJumpMonth() {
		return jumpMonth;
	}
	public  void setJumpMonth(int jumpMonth) {
		this.jumpMonth = jumpMonth;
	}
	public  int getJumpYear() {
		return jumpYear;
	}
	public  void setJumpYear(int jumpYear) {
		this.jumpYear = jumpYear;
	}
	public int getYear_c() {
		return year_c;
	}
	public void setYear_c(int year_c) {
		this.year_c = year_c;
	}
	public int getMonth_c() {
		return month_c;
	}
	public void setMonth_c(int month_c) {
		this.month_c = month_c;
	}
	public int getDay_c() {
		return day_c;
	}
	public void setDay_c(int day_c) {
		this.day_c = day_c;
	}
	
	public String getshowMonth(){
		return TimeUtils.format(TimeUtils.getAfterMonth(jumpMonth), "yyyy-MM", "yyyy年MM月");
	}
	
	public String getNowMonth(){
		return TimeUtils.getAfterMonth(jumpMonth).replace("-0", "-");
	}
	
}
