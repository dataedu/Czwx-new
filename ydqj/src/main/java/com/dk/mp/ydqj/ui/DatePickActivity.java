package com.dk.mp.ydqj.ui;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.NumericWheelAdapter;
import com.dk.mp.core.widget.OnWheelScrollListener;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.ydqj.R;

/**
 * 时间选择
 * @author dake
 *
 */
public class DatePickActivity extends MyActivity {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private Context mContext = DatePickActivity.this;
	private String rq="";
	private String compRq="";//比较日期
	private int type;//1,开始时间 2，结束时间
	int curYear;
	int curMonth;
	int curDate;

	@Override
	protected int getLayoutID() {
		return R.layout.pick_date;
	}

	@Override
	protected void initialize() {
		super.initialize();

		rq = getIntent().getStringExtra("rq");
		compRq = getIntent().getStringExtra("compRq");
		type = getIntent().getIntExtra("type", 0);
		getDatePick();
	}
	
	private void getDatePick(){
		if(StringUtils.isNotEmpty(rq)){
			curYear = Integer.parseInt(rq.substring(0, 4));
			curMonth = Integer.parseInt(rq.substring(5, 7));
			curDate = Integer.parseInt(rq.substring(8, rq.length()));
		}else{
			Calendar c = Calendar.getInstance();
			curYear = c.get(Calendar.YEAR);
			curMonth = c.get(Calendar.MONTH) + 1;
			curDate = c.get(Calendar.DATE);
		}
		year = (WheelView) findViewById(R.id.year);
		month = (WheelView) findViewById(R.id.month);
		day = (WheelView) findViewById(R.id.day);
		
		year.setAdapter(new NumericWheelAdapter(curYear, curYear+5,""));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		
		month.setAdapter(new NumericWheelAdapter(1, 12,""));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		
		initDay(curYear,curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		
		Button bt = (Button)findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mon = month.getCurrentItem()+1+"";
				if(mon.length()<2){
					mon = "0"+mon;
				}
				String d = day.getCurrentItem()+1+"";
				if(d.length()<2){
					d = "0"+d;
				}
				String str = (year.getCurrentItem()+curYear) + "-"+ mon +"-"+d;
				if(type == 1 && TimeUtils.comparedDateS(str, TimeUtils.parseDate(new Date()))){
					DialogAlert dialog = new DialogAlert(mContext);
					dialog.show("确定", "开始时间不能小于当前时间");
				}else if(type == 1 && StringUtils.isNotEmpty(compRq) && checkTime(str, compRq)){
					DialogAlert dialog = new DialogAlert(mContext);
					dialog.show("确定", "开始时间不能大于结束时间");
				}else if(type == 2 && StringUtils.isNotEmpty(compRq) && checkTime(compRq,str)){
					DialogAlert dialog = new DialogAlert(mContext);
					dialog.show("确定", "结束时间不能小于开始时间");
				}else{
					Intent in = new Intent();
					in.putExtra("date", str);
					setResult(-1, in);
					back();
					}
				}
		});
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		
	}
	
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			initDay(n_year,n_month);
		}
	};
	

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d",""));
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	
	@Override
	public void back() {
		finish();
		overridePendingTransition(0, R.anim.push_down_out);
	}

	private boolean checkTime(String starttime,String enttime){
		long a =Long.parseLong(starttime.replace("-", "").replace(" ", "").replace(":", ""));
		long b =Long.parseLong(enttime.replace("-", "").replace(" ", "").replace(":", ""));
		if(a>b){
			return true;
		}else{
			return false;
		}
	}
}
