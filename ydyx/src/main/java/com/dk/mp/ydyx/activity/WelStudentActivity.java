package com.dk.mp.ydyx.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.ydyx.R;

/**
 * 迎新首页.
 * 
 * @author rm.zhao
 * 
 *         2015-5-25
 */
public class WelStudentActivity extends MyActivity implements OnClickListener {
	private LinearLayout student, weldo, stuinfo;
	private String idUser = "";

	@Override
	protected int getLayoutID() {
		return R.layout.app_wel_student;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("移动迎新");
		CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(this);
		idUser = helper.getUser().getUserId();
		findView();

	}

	private void findView() {
		student = (LinearLayout) findViewById(R.id.student);
		weldo = (LinearLayout) findViewById(R.id.weldo);
		stuinfo = (LinearLayout) findViewById(R.id.stuinfo);
		student.setOnClickListener(this);
		weldo.setOnClickListener(this);
		stuinfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		 if (v == weldo) {
			Intent intent = new Intent();
			intent.setClass(WelStudentActivity.this, WelDoActivity.class);
			intent.putExtra("idUser", idUser);
			startActivity(intent);
		} else if (v == stuinfo) {
			Intent intent = new Intent();
			intent.setClass(WelStudentActivity.this, StudentInfoActivity.class);
			intent.putExtra("idUser", idUser);
			startActivity(intent);
		}
	}
}
