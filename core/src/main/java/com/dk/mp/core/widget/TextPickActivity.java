package com.dk.mp.core.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.R;

import java.util.Calendar;


/**
 * 时间选择
 * @author dake
 *
 */
public class TextPickActivity extends Activity {
	private WheelView text;
	private Context mContext = TextPickActivity.this;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_text);
		Intent intent = getIntent();
		String items = intent.getStringExtra("items");
        	getDatePick(items);
	}

	private void getDatePick(String items) {
		text = (WheelView) findViewById(R.id.text);
		text.setAdapter(new ArrayWheelAdapter(items.split(",")));
		text.setCyclic(false);

		text.setCurrentItem(0);


		Button bt = (Button) findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent();
				in.putExtra("index", text.getCurrentItem());
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




	public void back() {
		finish();
		overridePendingTransition(0, R.anim.push_down_out);
	}

}
