package com.dk.mp.apps.gzbxnew;

import android.content.Intent;
import android.os.Bundle;

import com.dk.mp.core.view.tab.MyTabActivity;

import java.util.ArrayList;
import java.util.List;

public class FaultRepairCommentOnTabActivity extends MyTabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("故障报修",true);
		initData();
	}
	//报修申请
	public void initData(){

		List<String> tabs = new ArrayList<String>();
		List<Intent> intents = new ArrayList<Intent>();
		
		Intent intent = new Intent(this, com.dk.mp.apps.gzbxnew.FaultRepairCommentOnActivity.class);
		intent.putExtra("pjzt", "wpj");
		intent.putExtra("shzt", "");
		intent.putExtra("jklx", "pj");
		intents.add(intent);
		tabs.add("未评");
		
		Intent intent2 = new Intent(this, com.dk.mp.apps.gzbxnew.FaultRepairCommentOnActivity.class);
		intent2.putExtra("pjzt", "ypj");
		intent2.putExtra("shzt", "");
		intent2.putExtra("jklx", "pj");
		intents.add(intent2);
		tabs.add("已评");
		
		setTabs(tabs, intents);
	}
}
