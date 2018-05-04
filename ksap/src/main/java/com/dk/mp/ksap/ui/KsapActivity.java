package com.dk.mp.ksap.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
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


public class KsapActivity extends KsapCommonActivity {

    private List<Pc> pcs = new ArrayList<Pc>();
    TextView pc,bjxx,ksrs;
    @Override
    protected int getLayoutID() {
        return R.layout.app_ksap;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        pc = (TextView) findViewById(R.id.pc);
        bjxx = (TextView) findViewById(R.id.bjxx);
        ksrs = (TextView) findViewById(R.id.ksrs);
        findViewById(R.id.ss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KsapActivity.this, KsapSearchActivity.class);
                startActivity(intent);
            }
        });

        if(DeviceUtil.checkNet()){
            showProgressDialog();
            onRefresh();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }


        setRightText("筛选",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pcs.size()>0){
                    Intent intent = new Intent(KsapActivity.this, TextPickActivity.class);
                    String str="";
                    for(int i=0;i<pcs.size();i++){
                        if(i==0){
                            str+=pcs.get(i).getName();
                        }else{
                            str+=","+pcs.get(i).getName();
                        }
                    }
                    intent.putExtra("items",str);
                    startActivityForResult(intent, 4);
                }else{
                    Map<String,Object> map = new HashMap<>();

                    HttpUtil.getInstance().postJsonObjectRequest("apps/ksap/pc", map, new HttpListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result)  {
                            try {
                                pcs.clear();
                                String json =  result.getJSONArray("data").toString();
                                pcs = new Gson().fromJson(json,new TypeToken<List<Pc>>(){}.getType());

                                Intent intent = new Intent(KsapActivity.this, TextPickActivity.class);
                                String str="";
                                for(int i=0;i<pcs.size();i++){
                                    if(i==0){
                                        str+=pcs.get(i).getName();
                                    }else{
                                        str+=","+pcs.get(i).getName();
                                    }
                                }
                                intent.putExtra("items",str);
                                startActivityForResult(intent, 4);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(VolleyError error) {
                            showMessage(error.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if(resultCode == RESULT_OK){
                    int   index = data.getIntExtra("index",0);
                    if(!pcs.get(index).getName().equals(pc.getText().toString())) {
                        pcId = pcs.get(index).getId();
                        pc.setText(pcs.get(index).getName());
                        showProgressDialog();
                        onRefresh();
                    }
                }
                break;
        }
    }

    @Override
    public void odSomething(KsapBean bean){
          if(StringUtils.isNotEmpty(bean.getBj())) {
          ksrs.setText("考试人数："+bean.getKsrs());
          bjxx.setText("班级："+bean.getBj());
          bjxx.setVisibility(View.VISIBLE);
          ksrs.setVisibility(View.VISIBLE);
      }else{
          ksrs.setVisibility(View.GONE);
          bjxx.setVisibility(View.GONE);
      }
        if(StringUtils.isNotEmpty(bean.getPc())) {
            pc.setText(bean.getPc());
        }

    }

}
