package com.dk.mp.apps.gzbxnew;

import java.util.List;

import android.os.Bundle;



import android.support.v4.view.ViewPager;

import com.dk.mp.apps.gzbxnew.adapter.ImagePagerAdapter;
import com.dk.mp.core.ui.MyActivity;

public class ImagePreviewActivity extends MyActivity {
	
	private ViewPager mViewPager;
    private List<String> mList;
    private int index;
    private ImagePagerAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.image_preview;
    }

	
    protected void initView() {
        setTitle("查看图片");
        mViewPager = (ViewPager) findViewById(R.id.viewpage);
        initialize();
    }
    
    protected void initialize() {
        mList = getIntent().getStringArrayListExtra("list");
        index = getIntent().getIntExtra("index", 0);
        mAdapter = new ImagePagerAdapter(ImagePreviewActivity.this, mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mViewPager.setCurrentItem(index);
    }
}
