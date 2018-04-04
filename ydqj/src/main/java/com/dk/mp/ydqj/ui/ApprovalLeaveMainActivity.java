package com.dk.mp.ydqj.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.dk.mp.core.view.tab.MyTabActivity;

public class ApprovalLeaveMainActivity extends MyTabActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("请假审批",true);
		
		List<String> names = new ArrayList<String>();
		List<Intent> intents = new ArrayList<Intent>();
		
		Intent intent1 = new Intent(ApprovalLeaveMainActivity.this,ApprovalLeaveListActivity.class);
		intent1.putExtra("type", "db");
		names.add("待办");
		intents.add(intent1);
		
		Intent intent2 = new Intent(ApprovalLeaveMainActivity.this,ApprovalLeaveListActivity.class);
		intent2.putExtra("type", "yb");
		intents.add(intent2);
		names.add("办结");
		
		setTabs(names, intents);
	}
}
