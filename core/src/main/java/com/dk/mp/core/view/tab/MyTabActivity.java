package com.dk.mp.core.view.tab;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.dk.mp.core.R;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SlideBg;

import java.util.List;

@SuppressWarnings("deprecation")
public class MyTabActivity extends TabActivity {

	private TabHost tabHost;
	private RadioGroup radioGroup;
	private RelativeLayout tabLayout;
	private TextView bar;
	public int avgWidth = 0;
	public int startX = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.core_tab);
		findView();
	}

	private void findView() {
		tabHost = getTabHost();
		bar = (TextView) findViewById(R.id.bar);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		tabLayout = (RelativeLayout) findViewById(R.id.tabLayout);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
	}

	public void setTabs(List<String> tabs, List<Intent> intents) {
		int width = DeviceUtil.getScreenWidth(this) / tabs.size();
		avgWidth=width;
		bar.setWidth(width);
		for (int i = 0; i < tabs.size(); i++) {
			tabHost.addTab(tabHost.newTabSpec(i + "").setIndicator(i + "").setContent(intents.get(i)));

			RadioButton radio = new RadioButton(this);
			radio.setTextSize(13);
			radio.setButtonDrawable(R.drawable.touming);
			radio.setText(tabs.get(i));
			radio.setTag(i + "");
			if (i == 0) {
				radio.setTextColor(getResources().getColor(R.color.tab_selected));
			} else {
				radio.setTextColor(getResources().getColor(R.color.tab_unselected));
			}
			radio.setId(i);
			radio.setSingleLine();
			radio.setGravity(Gravity.CENTER);
			radio.setWidth(width);
			radioGroup.addView(radio);
		}

		if (tabs.size() <= 1) {
			tabLayout.setVisibility(View.GONE);
		}

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			for (int i = 0; i < radioGroup.getChildCount(); i++) {
				RadioButton radio = (RadioButton) radioGroup.getChildAt(i);
				if (checkedId == radio.getId()) {
					tabHost.setCurrentTabByTag(radio.getTag().toString());
					radio.setTextColor(getResources().getColor(R.color.tab_selected));
					SlideBg.moveFrontBg(bar, startX, avgWidth * checkedId, 0, 0);
					startX = avgWidth * checkedId;
				} else {
					radio.setTextColor(getResources().getColor(R.color.tab_unselected));
				}
			}
		}
	};


	public void updateRadioText(List<String> tabs){
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			RadioButton radio = (RadioButton) radioGroup.getChildAt(i);
			radio.setText(tabs.get(i));
		}
	}

	/**
	 * 初始化皮肤.
	 * @param title 标题栏文字
	 */
	public void setTitle(String title, boolean hasBack) {
		try {
			TextView textView = (TextView) findViewById(R.id.title);
			textView.setText(title);
		} catch (Exception e) {
		}

		if (hasBack) {
			try {
				Button back = (Button) findViewById(R.id.back);
				back.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
			} catch (Exception e) {
			}
		} else {
			try {
				Button back = (Button) findViewById(R.id.back);
				back.setVisibility(View.GONE);
			} catch (Exception e) {
			}
		}
	}

	public void setRightText(String text, OnClickListener listener) {
		try {
			TextView right_txt = (TextView) findViewById(R.id.right_txt);
			right_txt.setText(text);
			right_txt.setVisibility(View.VISIBLE);
			right_txt.setOnClickListener(listener);
		} catch (Exception e) {
		}
	}

	public void setRightText(String text, OnClickListener listener,int textColor) {
		try {
			TextView right_txt = (TextView) findViewById(R.id.right_txt);
			right_txt.setText(text);
			right_txt.setVisibility(View.VISIBLE);
			right_txt.setOnClickListener(listener);
			right_txt.setTextColor(textColor);
		} catch (Exception e) {
		}
	}

	/**
	 * 显示提示信息.
	 * @param message 提示信息
	 */
	public void showMessage(String message) {
		MsgDialog.show(this, message);
	}

	/**
	 * 无网络提示
	 */
	public void setNoWorkNet() {
		LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
		ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
		TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
		zwsj.setVisibility(View.VISIBLE);
		zwsj_icon.setImageResource(R.mipmap.nonet);
		zwsj_text.setText(getString(R.string.net_no2));
	}

	/**
	 * 无数据提示
	 * @param text 提示文字
	 */
	public void setNoDate(String text) {
		LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
		ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
		TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
		zwsj.setVisibility(View.VISIBLE);
		zwsj_icon.setImageResource(R.mipmap.nodata_n);
		if(text!=null){
			zwsj_text.setText(text);
		}else{
			zwsj_text.setText(getString(R.string.nodata));
		}
	}

	/**
	 * 数据失败提示
	 * @param text 提示文字
	 */
	public void setErrorDate(String text) {
		LinearLayout zwsj = (LinearLayout) findViewById(R.id.zwsj);
		ImageView zwsj_icon = (ImageView) findViewById(R.id.zwsj_icon);
		TextView zwsj_text = (TextView) findViewById(R.id.zwsj_text);
		zwsj.setVisibility(View.VISIBLE);
		zwsj_icon.setImageResource(R.mipmap.noapply);
		if(text!=null){
			zwsj_text.setText(text);
		}else{
			zwsj_text.setText(getString(R.string.data_fail));
		}
	}
}
