package com.dk.mp.xyfg;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xyfg.entity.SceneryEntity;
import com.dk.mp.xyfg.view.SceneryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by cobb on 2017/6/24.
 */

public class XyfgActivity extends MyActivity {

    private List<SceneryEntity> list;
    private ScrollView scroll;

    private ErrorLayout errorLayout;
    LinearLayout rootview;

    @Override
    protected int getLayoutID() {
        return R.layout.app_scenery;
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle("校园风光");

        rootview = (LinearLayout) findViewById(R.id.rootview);
        scroll = (ScrollView) findViewById(R.id.sceneryScrollView);
        errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

        getData();
    }

    private void initDatas(){
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
        scroll.removeAllViews();

        HttpUtil.getInstance().postJsonObjectRequest("apps/xyfg/getTypeList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String json =  result.getJSONArray("data").toString();
                        list = new Gson().fromJson(json,new TypeToken<List<SceneryEntity>>(){}.getType());
                        if (list.size() > 0){
                            LinearLayout layout = SceneryItem.getViews(mContext, list);
                            scroll.addView(layout);

                        }else {
                            errorLayout.setErrorType(ErrorLayout.NODATA);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    error();
                }
            }
            @Override
            public void onError(VolleyError error) {
                error();
            }
        });
    }

    /**
     * 获取数据
     */
    public void getData(){
        if(DeviceUtil.checkNet()){
            initDatas();
        }else{
            if(list != null && list.size()>0){
                return;
            }else {
                errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        }
    }

    /**
     * 加载失败处理信息
     */
    public void error(){
        if(list != null && list.size()>0){
            return;
        }else {
            errorLayout.setErrorType(ErrorLayout.DATAFAIL);
        }
    }


}
