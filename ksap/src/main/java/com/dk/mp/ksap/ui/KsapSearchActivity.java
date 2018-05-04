package com.dk.mp.ksap.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.Jbxx;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ksap.R;
import com.dk.mp.ksap.adapter.KsapAdapter;
import com.dk.mp.ksap.entity.Ksap;
import com.dk.mp.ksap.entity.Kskc;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KsapSearchActivity extends KsapCommonActivity {

    private EditText mKeywords;//搜索关键字

    @Override
    protected int getLayoutID() {
        return R.layout.app_ksap_search;
    }

    @Override
    protected void initialize() {
        super.initialize();

        mKeywords = (EditText) findViewById(R.id.search_Keywords);

        mKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = mKeywords.getText().toString();
                    Logger.info(keywords);
                    hideSoftInput();
                    if (StringUtils.isNotEmpty(keywords)) {
                        if(DeviceUtil.checkNet()){//判断是否有网络
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            key=keywords;
                            getList();
                        }else{
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        }
                    } else {
                        MsgDialog.show(KsapSearchActivity.this,"请输入关键字");
                    }
                }else if(actionId == 3 && event == null){
                    final String keywords = mKeywords.getText().toString();
                    if (!StringUtils.isNotEmpty(keywords)) {
                        hideSoftInput();
                        MsgDialog.show(KsapSearchActivity.this,"请输入关键字");
                    }
                }
                return false;
            }
        });

    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

}
