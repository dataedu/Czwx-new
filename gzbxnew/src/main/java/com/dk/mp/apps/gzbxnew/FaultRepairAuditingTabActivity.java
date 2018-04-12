package com.dk.mp.apps.gzbxnew;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.tab.MyTabActivity;
import com.dk.mp.core.widget.MyViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * 报修审核
 * @author admin
 *
 */
public class FaultRepairAuditingTabActivity extends MyActivity {

	TabLayout mTabLayout;
	MyViewpager mViewpager;
	@Override
	protected int getLayoutID() {
		return R.layout.gzbx_pl_tab;
	}

	@Override
	protected void initialize() {
		super.initialize();
		setTitle("报修审核");
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
		mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
		initData();
	}
	//报修申请
	public void initData(){
		List<String> titles = new ArrayList<>();
		titles.add("待办");
		titles.add("已办");

		for(int i=0;i<titles.size();i++){
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
		}

		List<BaseFragment> fragments = new ArrayList<>();


		Bundle args = new Bundle();
		args.putString("pjzt", "");
		args.putString("shzt", "SH");
		args.putString("jklx", "sh");
		FaultRepairComment pageFragment = new FaultRepairComment();
		pageFragment.setArguments(args);

		Bundle args2 = new Bundle();
		args2.putString("pjzt", "");
		args2.putString("shzt", "YB");
		args2.putString("jklx", "sh");
		args2.putString("category", "2");
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
