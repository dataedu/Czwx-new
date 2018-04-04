package com.dk.mp.ydqj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.ArrayWheelAdapter;
import com.dk.mp.core.widget.WheelView;
import com.dk.mp.ydqj.R;

/**
 * 请假类型选择器
 * @author admin
 *
 */
public class LeaveTypeActivity extends MyActivity {
	private String[] PLANETM;
	private WheelView hys;

	@Override
	protected int getLayoutID() {
		return R.layout.pick_qjlx;
	}

	@Override
	protected void initialize() {
		super.initialize();

		PLANETM = new String[]{"事假","病假","其它"};
		findView();
	}
	
	private void findView(){
		hys = (WheelView) findViewById(R.id.hys);
		hys.setAdapter(new ArrayWheelAdapter<String>(PLANETM));
		hys.setCyclic(false);
		hys.setCurrentItem(0);
		Button bt = (Button)findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = PLANETM[hys.getCurrentItem()];
				Intent in = new Intent();
				in.putExtra("qjlx", str);
				setResult(RESULT_OK, in);
				back();
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
	@Override
	public void back() {
		finish();
		overridePendingTransition(0, R.anim.push_down_out);
	}
}
