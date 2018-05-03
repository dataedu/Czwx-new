package com.dk.mp.ksap.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.TextPickActivity;
import com.dk.mp.ksap.R;
import com.dk.mp.ksap.adapter.KsapAdapter;
import com.dk.mp.ksap.entity.Ksap;
import com.dk.mp.ksap.entity.KsapBean;
import com.dk.mp.ksap.entity.Kskc;
import com.dk.mp.ksap.entity.Pc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KsapCommonActivity extends MyActivity implements XListView.IXListViewListener {
    public ErrorLayout mError;
    private XListView myListView;
    private List<Ksap> list = new ArrayList<Ksap>();

    private int pageNo = 1;
    public String pcId = "";
    public String pcName = "";
    public String key = "";
    KsapAdapter ksapAdapter;

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initialize() {
        super.initialize();
        myListView = (XListView) findViewById(R.id.newslist);
        myListView.setPullLoadEnable(true);
        myListView.setXListViewListener(this);
        myListView.hideHeader();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        getList();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        getList();
    }

    @Override
    public void stopLoad() {
        myListView.stopLoadMore();
        myListView.stopRefresh();
        hideProgressDialog();
    }

    public void getList() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("pcId", pcId);
        map.put("key", key);
        map.put("pcName", pcName);

        HttpUtil.getInstance().postJsonObjectRequest("apps/ksap/index", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                try {
                    String json = result.getJSONObject("data").toString();
                    KsapBean bean = new Gson().fromJson(json, new TypeToken<KsapBean>() {
                    }.getType());
                    if (bean.getMsg() != null) {
                        showMessage(bean.getMsg());
                    } else {
                        List<Ksap> temp = bean.getList();
                        if (pageNo == 1) {
                            list = temp;
                        } else {
                            list.addAll(temp);
                        }
                        odSomething(bean);

                        if(list.size()<20){
                            myListView.hideFooter();
                        }else{
                            myListView.stopLoadMore();
                        }

                        if (ksapAdapter == null) {
                            ksapAdapter = new KsapAdapter(mContext, list);
                            myListView.setAdapter(ksapAdapter);
                        } else {
                            ksapAdapter.setList(list);
                            ksapAdapter.notifyDataSetChanged();
                        }
                    }
                    stopLoad();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                showMessage(error.getMessage());
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        });
    }



    public void odSomething(KsapBean bean){

    }

}
