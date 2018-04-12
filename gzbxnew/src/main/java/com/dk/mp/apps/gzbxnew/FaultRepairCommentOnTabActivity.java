package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.view.tab.MyTabActivity;
import com.dk.mp.core.widget.MyViewpager;

import java.util.ArrayList;
import java.util.List;

public class FaultRepairCommentOnTabActivity extends MyActivity{



	TabLayout mTabLayout;
	MyViewpager mViewpager;

	@Override
	protected int getLayoutID() {
		return R.layout.gzbx_pl_tab;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("报修评价");
		findView();
		initViewPager();
	}


	private void findView(){
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
		mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
	}


	private void initViewPager(){
		List<String> titles = new ArrayList<>();
		titles.add("未评");
		titles.add("已评");

		for(int i=0;i<titles.size();i++){
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
		}

		List<BaseFragment> fragments = new ArrayList<>();


		Bundle args = new Bundle();
		args.putString("pjzt", "wpj");
		args.putString("shzt", "");
		args.putString("jklx", "pj");

		FaultRepairComment pageFragment = new FaultRepairComment();
		pageFragment.setArguments(args);

		Bundle args2 = new Bundle();
		args2.putString("pjzt", "ypj");
		args2.putString("shzt", "");
		args2.putString("jklx", "pj");

		FaultRepairComment pageFragment2 = new FaultRepairComment();
		pageFragment2.setArguments(args2);

		fragments.add(pageFragment);
		fragments.add(pageFragment2);

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
		mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
		mViewpager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewpager);

	}



}
