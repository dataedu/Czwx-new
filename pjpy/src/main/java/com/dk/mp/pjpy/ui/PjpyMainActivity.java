package com.dk.mp.pjpy.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.tab.MyTabActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.pjpy.R;

public class PjpyMainActivity extends MyActivity {

	TabLayout mTabLayout;
	MyViewpager mViewpager;

	@Override
	protected int getLayoutID() {
		return R.layout.ac_pjpy_main;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("评奖评优");

		findView();
		initViewPager();

	}

	private void findView(){
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
		mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
	}


	private void initViewPager(){
		List<String> titles = new ArrayList<>();
		titles.add("奖学金");
		titles.add("荣誉称号");

		for(int i=0;i<titles.size();i++){
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
		}

		List<BaseFragment> fragments = new ArrayList<>();

		fragments.add(PjpyListActivity.newInstance("1"));
		fragments.add(PjpyListActivity.newInstance("2"));

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
		mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
		mViewpager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewpager);

	}
}
