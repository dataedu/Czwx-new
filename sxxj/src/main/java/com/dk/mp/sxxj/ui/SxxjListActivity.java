package com.dk.mp.sxxj.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.DatePickActivity;
import com.dk.mp.core.widget.ErrorLayout;

import com.dk.mp.core.widget.TextPickActivity;
import com.dk.mp.sxxj.R;
import com.dk.mp.sxxj.adapter.SxxjAdapter;
import com.dk.mp.sxxj.entity.SxxjList;
import com.dk.mp.sxxj.entity.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学校新闻列表
 * 作者：janabo on 2016/12/19 10:08
 */
public class SxxjListActivity extends MyActivity {
    ErrorLayout mError;
    private ListView myListView;
    private List<SxxjList> list = new ArrayList<SxxjList>();
    private ArrayList<Type> type = new ArrayList<Type>();
    String xjlx="";

    @Override
    protected int getLayoutID() {
        return R.layout.app_sxxj;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
//        mRealmHelper = new JyxxHelper(this);
        myListView = (ListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("草稿".equals(list.get(position).getStatus())){
                    Intent intent = new Intent(SxxjListActivity.this, SxxjSaveActivity.class);
                    intent.putExtra("title",list.get(position).getDate());
                    intent.putExtra("id",list.get(position).getId());
                    intent.putExtra("type",list.get(position).getType());
                    Bundle bundle = new Bundle();
                    if(type.contains("所有")){
                        type.remove("所有");
                    }
                    bundle.putSerializable("types", type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SxxjListActivity.this, HttpWebActivity.class);
                    intent.putExtra("title",list.get(position).getDate());
                    intent.putExtra("url","apps/sxxj/detail?id="+list.get(position).getId());
                    startActivity(intent);
                }
            }
        });

      setRightText("筛选",new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(type.size()>0){
                  Intent intent = new Intent(SxxjListActivity.this, TextPickActivity.class);
                  String str="所有";
                  for(int i=0;i<type.size();i++){
                      str+=","+type.get(i).getName();
                  }
                  intent.putExtra("items",str);
                  startActivityForResult(intent, 4);
              }else{
                  Map<String,Object> map = new HashMap<>();

                  HttpUtil.getInstance().postJsonObjectRequest("apps/sxxj/xjlx", map, new HttpListener<JSONObject>() {
                      @Override
                      public void onSuccess(JSONObject result)  {
                          try {
                              type.clear();
                              String json =  result.getJSONArray("data").toString();
                              type = new Gson().fromJson(json,new TypeToken<List<Type>>(){}.getType());

                              Intent intent = new Intent(SxxjListActivity.this, TextPickActivity.class);
                              String str="所有";
                              for(int i=0;i<type.size();i++){
                                  str+=","+type.get(i).getName();
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


        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SxxjListActivity.this, SxxjSaveActivity.class);
                intent.putExtra("title","填写小结");
                Bundle bundle = new Bundle();
                if(type.contains("所有")){
                    type.remove("所有");
                }
                bundle.putSerializable("types", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        if(DeviceUtil.checkNet()) {
            getList();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }


        BroadcastUtil.registerReceiver(this,receiver,"sxxj_refresh");
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("sxxj_refresh")) {
                getList();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if(resultCode == RESULT_OK){
                  int   index = data.getIntExtra("index",0);
                  if(index==0){
                      xjlx="";
                  }else{
                      xjlx=type.get(index-1).getId();
                  }
                    getList();
                }
                break;
        }
    }


    public void getList(){
        Map<String,Object> map = new HashMap<>();
        map.put("xjlx",xjlx);
        HttpUtil.getInstance().postJsonObjectRequest("apps/sxxj/list", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        list.clear();
                        String json =  result.getJSONArray("data").toString();
                        List<SxxjList> list1 = new Gson().fromJson(json,new TypeToken<List<SxxjList>>(){}.getType());
                        list.addAll(list1);
                        myListView.setAdapter(new SxxjAdapter(SxxjListActivity.this,list));
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
