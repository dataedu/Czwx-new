package com.dk.mp.dltj;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书馆首页面.
 * @since
 * @version 2014-9-25
 * @author zhaorm
 */
public class DltjMainActivity extends MyActivity {

    TabLayout mTabLayout;
    MyViewpager mViewpager;

    @Override
    protected int getLayoutID() {
        return R.layout.dltj_main;
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle(getIntent().getStringExtra("title"));
        findView();
        initViewPager();


    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);

        setRightText("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DltjMainActivity.this, DltjSearchActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("今天");
        titles.add("一周");
        titles.add("半个月");
        titles.add("一个月");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();


        Bundle args = new Bundle();
        args.putString("type", "today");
        DltjActivity pageFragment = new DltjActivity();
        pageFragment.setArguments(args);

        Bundle args2 = new Bundle();
        args2.putString("type", "week");
        DltjActivity pageFragment2 = new DltjActivity();
        pageFragment2.setArguments(args2);

        Bundle args3 = new Bundle();
        args3.putString("type", "hmonth");
        DltjActivity pageFragment3 = new DltjActivity();
        pageFragment3.setArguments(args3);

        Bundle args4= new Bundle();
        args4.putString("type", "month");
        DltjActivity pageFragment4 = new DltjActivity();
        pageFragment4.setArguments(args4);

        fragments.add(pageFragment);
        fragments.add(pageFragment2);
        fragments.add(pageFragment3);
        fragments.add(pageFragment4);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);

    }



}
