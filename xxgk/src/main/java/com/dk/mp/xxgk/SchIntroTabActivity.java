package com.dk.mp.xxgk;

		import android.support.design.widget.TabLayout;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * 动画页签使用方法.
 *
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
public class SchIntroTabActivity extends MyActivity {

	TabLayout mTabLayout;
	MyViewpager mViewpager;

	@Override
	protected int getLayoutID() {
		return R.layout.ac_sch_introtab;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("学校概况");
		findView();

		initViewPager();
	}

	private void findView(){
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
		mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
	}

	private void initViewPager(){
		List<String> titles = new ArrayList<>();
		titles.add(getResources().getString(R.string.schinfo_desc));
		titles.add(getResources().getString(R.string.schinfo_history));
//		titles.add(getResources().getString(R.string.schinfo_depart));

		for(int i=0;i<titles.size();i++){
			mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
		}

		List<BaseFragment> fragments = new ArrayList<>();

		fragments.add(new SchIntroDescActivity());
		fragments.add(new SchIntroHistoryActivity());
//		fragments.add(new SchIntroCollegeActivity());

		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
		mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
		mViewpager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewpager);

	}

}
