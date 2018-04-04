package com.dk.mp.apps.gzbxnew;

import android.content.Intent;
import android.os.Bundle;

import com.dk.mp.core.view.tab.MyTabActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 报修审核
 * @author admin
 *
 */
public class FaultRepairAuditingTabActivity extends MyTabActivity{

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
		
		Intent intent = new Intent(this,FaultRepairCommentOnActivity.class);
		intent.putExtra("pjzt", "");
		intent.putExtra("shzt", "SH");
		intent.putExtra("jklx", "sh");
		intents.add(intent);
		tabs.add("待办");
		
		Intent intent2 = new Intent(this,FaultRepairCommentOnActivity.class);
		intent2.putExtra("pjzt", "");
		intent2.putExtra("shzt", "YB");
		intent2.putExtra("jklx", "sh");
		intent2.putExtra("category", "2");
		intents.add(intent2);
		tabs.add("已办");
		
		setTabs(tabs, intents);
	}
}
