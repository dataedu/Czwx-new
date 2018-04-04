package com.dk.mp.xxgk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;

/**
 * 页面滑动.
 * 
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
public class SchIntroCollegeDetailActivity extends MyActivity {
	private TextView textContent;


	@Override
	protected int getLayoutID() {
		return R.layout.app_intro_college_details;
	}

	@Override
	protected void initialize() {
		super.initialize();

		Intent intent = this.getIntent();
		setTitle(intent.getStringExtra("name"));
		textContent = (TextView) findViewById(R.id.content);
		String html = intent.getStringExtra("content");
		if (StringUtils.isNotEmpty(html)) {
			textContent.setText(Html.fromHtml(html));
		}
	}

}
