package com.dake.mp.zslq;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.core.adapter.MyFragmentPagerAdapter;

/**
 * 图书馆首页面.
 * @since
 * @version 2014-9-25
 * @author zhaorm
 */
public class ZslqMainActivity extends MyActivity {

    TabLayout mTabLayout;
    MyViewpager mViewpager;

    @Override
    protected int getLayoutID() {
        return R.layout.zslq_main;
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
    }


    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("招生计划");
        titles.add("招生简章");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();


        Bundle args = new Bundle();
        args.putString("type", "first");
        ZslqActivity pageFragment = new ZslqActivity();
        pageFragment.setArguments(args);

        Bundle args2 = new Bundle();
        args2.putString("type", "second");
        ZslqActivity pageFragment2 = new ZslqActivity();
        pageFragment2.setArguments(args2);

        fragments.add(pageFragment);
        fragments.add(pageFragment2);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);

    }



}
