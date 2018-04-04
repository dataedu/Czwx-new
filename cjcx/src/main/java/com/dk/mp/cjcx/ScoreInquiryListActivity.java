package com.dk.mp.cjcx;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.dk.mp.cjcx.adapter.ScoreInquiryAdapter;
import com.dk.mp.cjcx.entity.ScoreInquiry;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 成绩查询
 * 作者：janabo on 2017/10/26 15:04
 */
public class ScoreInquiryListActivity extends MyActivity {
    private ErrorLayout mError;
    private RecyclerView mList;
    private List<ScoreInquiry> mData = new ArrayList<>();
    private ScoreInquiryAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_scoreinquiry;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mList = (RecyclerView) findViewById(R.id.mListView);
        mList.setHasFixedSize ( true );
        mList.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new ScoreInquiryAdapter(mContext,mData);
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
    }

    @Override
    protected void initialize() {
        super.initialize();
        if(DeviceUtil.checkNet()){
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getData(){
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
        String userId = "";
        if (loginMsg != null) {
            userId = loginMsg.getUid();
        }
        HttpUtil.getInstance().postJsonObjectRequest("apps/cjcx/listXueqi?userId=" + userId, null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        if(result.optJSONArray("data") != null && result.optJSONArray("data").length()>0){
                            for (int i=0;i<result.optJSONArray("data").length();i++){
                                ScoreInquiry si = new Gson().fromJson(result.getJSONArray("data").get(i).toString(),ScoreInquiry.class);
                                mData.add(si);
                            }
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }
}
