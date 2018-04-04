package com.dk.mp.cjcx;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;

import org.json.JSONObject;

/**
 * 作者：janabo on 2017/10/26 16:00
 */
public class ScoreInquiryDetailActivity extends MyActivity {
    private OADetailView content;
    private ErrorLayout mError;
    private String xqid;

    @Override
    protected int getLayoutID() {
        return R.layout.app_scoreinquiry_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("成绩查询");
        content = (OADetailView) findViewById(R.id.content);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        xqid = getIntent().getStringExtra("xqid");
        if(DeviceUtil.checkNet()){//判断网络
            getDetail();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getDetail() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/cjcx/detail?xueqiId="+xqid, null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String str = result.getString("data");
                        content.setText(StringUtils.checkEmpty(str));
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
