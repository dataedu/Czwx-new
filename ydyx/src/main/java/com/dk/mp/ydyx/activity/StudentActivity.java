package com.dk.mp.ydyx.activity;

import android.os.Bundle;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.ydyx.R;

/**
 * 同学圈.
 * @author rm.zhao
 *
 * 2015-5-25
 */
public class StudentActivity extends MyActivity {

	@Override
	protected int getLayoutID() {
		return R.layout.app_students;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("同学圈");
	}
}
