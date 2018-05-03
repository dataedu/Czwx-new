package com.dk.mp.ksap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ksap.R;
import com.dk.mp.ksap.adapter.KsapDetailAdapter;
import com.dk.mp.ksap.entity.Ksap;
import com.dk.mp.ksap.entity.Kskc;
import com.dk.mp.ksap.entity.KskcItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class KsapDetailActivity extends MyActivity{
    ErrorLayout mError;
    private ListView myListView;
    private List<KskcItem> list = new ArrayList<KskcItem>();
    Kskc item;
    @Override
    protected int getLayoutID() {
        return R.layout.app_ksap_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle("考试安排");
//        mRealmHelper = new JyxxHelper(this);
        TextView biaoti = (TextView) findViewById(R.id.biaoti);

        myListView = (ListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);

        item= (Kskc) getIntent().getSerializableExtra("kskc");
        biaoti.setText(item.getKcmc());
        list.add(new KskcItem("主考老师",item.getZkls()));
        list.add(new KskcItem("考试时间",item.getKssj()));
        list.add(new KskcItem("监考老师",item.getJkls()));
        list.add(new KskcItem("考试地点",item.getKsdd()));

        myListView.setAdapter(new KsapDetailAdapter(KsapDetailActivity.this,list));

    }




}
