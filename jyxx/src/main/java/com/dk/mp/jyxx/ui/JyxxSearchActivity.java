package com.dk.mp.jyxx.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.jyxx.R;
import com.dk.mp.jyxx.entity.Jyxx;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录搜索
 * 作者：janabo on 2016/12/26 14:46
 */
public class JyxxSearchActivity extends MyActivity implements View.OnClickListener{
    ErrorLayout mError;
    private MyListView myListView;
    private List<Jyxx> list = new ArrayList<Jyxx>();
    private EditText mKeywords;//搜索关键字
    @Override
    protected int getLayoutID() {
        return R.layout.app_jyxx_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
//        mRealmHelper = new JyxxHelper(this);

        mKeywords = (EditText) findViewById(R.id.search_Keywords);
        myListView = (MyListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

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
                            getList();
                            list.clear();
                        }else{
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        }
                    } else {
                        MsgDialog.show(JyxxSearchActivity.this,"请输入关键字");
                    }
                }else if(actionId == 3 && event == null){
                    final String keywords = mKeywords.getText().toString();
                    if (!StringUtils.isNotEmpty(keywords)) {
                        hideSoftInput();
                        MsgDialog.show(JyxxSearchActivity.this,"请输入关键字");
                    }
                }
                return false;
            }
        });

        myListView.setAdapterInterface(list, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_jyxx_item, parent, false);// 设置要转化的layout文件
                return new JyxxSearchActivity.MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                ((JyxxSearchActivity.MyView)holder).title.setText(list.get(position).getName());
                ((JyxxSearchActivity.MyView)holder).time.setText(list.get(position).getPublishTime());

                if (list.get(position).getImage() == null ){
                    ((JyxxSearchActivity.MyView)holder).image.setVisibility(View.GONE);
                } else {
                    ((JyxxSearchActivity.MyView)holder).image.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(list.get(position).getImage()).fitCenter().into(((JyxxSearchActivity.MyView)holder).image);
                }
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });

    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getList();
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        map.put("key",mKeywords.getText().toString());
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Jyxx>>(){}, "apps/jyxx/list", map, new HttpListener<PageMsg<Jyxx>>() {
            @Override
            public void onSuccess(PageMsg<Jyxx> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
//                    if(myListView.pageNo == 1){
//                        mRealmHelper.deleteAllNews();//删除之前的数据
//                    }
//                    mRealmHelper.addNews(result.getList());
                    list.addAll(result.getList());
                    myListView.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(myListView.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(myListView, R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
//                if(myListView.pageNo == 1) {
//                    mError.setErrorType(ErrorLayout.DATAFAIL);
//                }else{
//                    SnackBarUtil.showShort(myListView,R.string.data_fail);
//                }
//                getNewsLocal(2,myListView.pageNo);
            }
        });
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    private class MyView extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title,time;
        public MyView(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_img);// 取得实例
            title = (TextView) itemView.findViewById(R.id.news_title);// 取得实例
            time = (TextView) itemView.findViewById(R.id.news_time);// 取得实例

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Jyxx news = list.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, HttpWebActivity.class);
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("url",news.getUrl());
                    startActivity(intent);

                }
            });
        }
    }

}
