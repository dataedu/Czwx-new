package com.dk.mp.wspj;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.View.OnClickListener;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.view.tab.MyTabActivity;
import com.dk.mp.core.widget.MyViewpager;

public class EvaluateTabActivity extends MyActivity{

	TabLayout mTabLayout;
	MyViewpager mViewpager;

	@Override
	protected int getLayoutID() {
		return R.layout.ac_evaluate_tab;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("网上评教");
		
		/*List<String> titles = new ArrayList<String>();
		titles.add("未评价");
		titles.add("已评价");
		List<Intent> intents = new ArrayList<Intent>();
		intents.add(new Intent(this, EvaluateTeacherActivity.class).putExtra("tag", "0").putExtra("deptId", getIntent().getStringExtra("deptId")));
		intents.add(new Intent(this, EvaluateTeacherActivity.class).putExtra("tag", "1").putExtra("deptId", getIntent().getStringExtra("deptId")));
		setTabs(titles, intents);
	}
*/
		findView();

		initViewPager();
	}

	private void findView(){
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
		mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
	}

	private void initViewPager(){
		List<String> titles = new ArrayList<>();
		titles.add("未评价");
		titles.add("已评价");

		for(int i=0;i<titles.size();i++){
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
		}

		List<BaseFragment> fragments = new ArrayList<>();

		fragments.add(EvaluateTeacherActivity.newInstance("0",getIntent().getStringExtra("deptId")));
		fragments.add(EvaluateTeacherActivity.newInstance("1",getIntent().getStringExtra("deptId")));

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
		mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
		mViewpager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewpager);

	}

}
