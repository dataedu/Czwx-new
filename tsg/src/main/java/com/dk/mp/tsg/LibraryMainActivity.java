package com.dk.mp.tsg;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
public class LibraryMainActivity extends MyActivity {

    TabLayout mTabLayout;
    MyViewpager mViewpager;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_library_main;
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle("图书馆");
        findView();
        initViewPager();

        setRightText("搜索", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LibraryMainActivity.this, LibrarySearchActivity.class);
                startActivity(intent2);
            }
        });
    }

    private void findView(){
        mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mViewpager = (MyViewpager) findViewById(R.id.id_stickynavlayout_viewpager);
    }


    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("在借图书");
        titles.add("借阅记录");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(new LibraryRecordActivity());
        fragments.add(new LibraryRecordHistoryActivity());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);

    }

    public void setRightText(String text, OnClickListener listener) {
        try {
            TextView right_txt = (TextView) findViewById(R.id.right_txt);
            right_txt.setText(text);
            right_txt.setVisibility(View.VISIBLE);
            right_txt.setOnClickListener(listener);
        } catch (Exception e) {
        }
    }

}
