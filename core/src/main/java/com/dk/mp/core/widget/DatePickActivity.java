package com.dk.mp.core.widget;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.R;
import com.dk.mp.core.ui.MyActivity;


/**
 * 时间选择
 * @author dake
 *
 */
public class DatePickActivity extends Activity {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private Context mContext = DatePickActivity.this;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_date);
		Intent intent = getIntent();
		String rq = intent.getStringExtra("rq");
		getDatePick();
	}

	private void getDatePick() {
		Calendar c = Calendar.getInstance();
		final int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDate = c.get(Calendar.DATE);

		year = (WheelView) findViewById(R.id.year);
		month = (WheelView) findViewById(R.id.month);
		day = (WheelView) findViewById(R.id.day);

		year.setAdapter(new NumericWheelAdapter(curYear, curYear + 5, ""));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month.setAdapter(new NumericWheelAdapter(1, 12, ""));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		initDay(curYear, curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 1952);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mon = month.getCurrentItem() + 1 + "";
				if (mon.length() < 2) {
					mon = "0" + mon;
				}
				
				String tian = day.getCurrentItem() + 1 + "";
				if (tian.length() < 2) {
					tian = "0" + tian;
				}
				
				
				String str = (year.getCurrentItem() + curYear) + "-" + mon + "-" +tian;
				Intent in = new Intent();
				in.putExtra("date", str);
				setResult(RESULT_OK, in);
				back();
			}
		});
		Button cancel = (Button) findViewById(R.id.cancel);
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
			initDay(n_year, n_month);
		}
	};

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d", ""));
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


	public void back() {
		finish();
		overridePendingTransition(0, R.anim.push_down_out);
	}

}
