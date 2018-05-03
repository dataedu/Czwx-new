package com.dk.mp.sxxj.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.ResultCode;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.FileUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.TextPickActivity;
import com.dk.mp.sxxj.R;
import com.dk.mp.sxxj.adapter.AddImageAdapter;
import com.dk.mp.sxxj.entity.Detail;
import com.dk.mp.sxxj.entity.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import cn.qqtheme.framework.picker.FilePicker;


public class SxxjSaveActivity extends MyActivity {
    private ListView myListView;
    LinearLayout xjlx_bg;
    TextView xjlx, add;
    Button tj, bc;
    EditText xjnr;
    String id, xjlxId;

    Detail detail = null;
    AppCompatActivity context;

    private ArrayList<Type> type = new ArrayList<Type>();
    public List<String> fjIds = new ArrayList<>();
    public List<String> fjNames = new ArrayList<>();

    AddImageAdapter wImageAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_sxxj_save;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        context = SxxjSaveActivity.this;
        type = (ArrayList<Type>) getIntent().getSerializableExtra("types");

        id = getIntent().getStringExtra("id");
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        myListView = (ListView) findViewById(R.id.newslist);
        xjlx_bg = (LinearLayout) findViewById(R.id.xjlx_bg);
        xjlx = (TextView) findViewById(R.id.xjlx);
        add = (TextView) findViewById(R.id.add);
        tj = (Button) findViewById(R.id.tj);
        bc = (Button) findViewById(R.id.bc);
        xjnr = (EditText) findViewById(R.id.xjnr);
        if (StringUtils.isNotEmpty(getIntent().getStringExtra("type"))) {
            xjlx.setText(StringUtils.checkEmpty(getIntent().getStringExtra("type")));
            xjlxId = StringUtils.checkEmpty(getIntent().getStringExtra("type"));
        } else {
            xjlx.setText("请选择");
        }
        getCg();


        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit("bc");
            }
        });

        tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit("tj");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ablum();
            }
        });

        xjlx_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.size() > 0) {
                    Intent intent = new Intent(SxxjSaveActivity.this, TextPickActivity.class);
                    String xjlxNames = "";
                    if (xjlxNames != null) {
                        for (int i = 0; i < type.size(); i++) {
                            if (i == 0) {
                                xjlxNames += type.get(i).getName();
                            } else {
                                xjlxNames += "," + type.get(i).getName();
                            }


                        }
                    }
                    intent.putExtra("items", xjlxNames);
                    startActivityForResult(intent, 4);
                } else {
                    Map<String, Object> map = new HashMap<>();

                    HttpUtil.getInstance().postJsonObjectRequest("apps/sxxj/xjlx", map, new HttpListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                type.clear();
                                String json = result.getJSONArray("data").toString();
                                type = new Gson().fromJson(json, new TypeToken<List<Type>>() {
                                }.getType());

                                Intent intent = new Intent(SxxjSaveActivity.this, TextPickActivity.class);
                                String str = "";
                                for (int i = 0; i < type.size(); i++) {
                                    if (i == 0) {
                                        str += type.get(i).getName();
                                    } else {
                                        str += "," + type.get(i).getName();
                                    }
                                }
                                intent.putExtra("items", str);
                                startActivityForResult(intent, 4);
                            } catch (Exception e) {
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

        wImageAdapter = new AddImageAdapter(mContext, SxxjSaveActivity.this,
                fjNames);
        myListView.setAdapter(wImageAdapter);


    }

    public void open(int position) {
        FileUtil.openFileByUrl(mContext, getDownLoadUrl(fjIds.get(position)), fjNames.get(position));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra("index", 0);
                    xjlxId = type.get(index).getId();
                    xjlx.setText(type.get(index).getName());
                }
                break;
        }
    }


    public void getCg() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        HttpUtil.getInstance().gsonRequest(new TypeToken<Detail>() {
        }, "apps/sxxj/cg", map, new HttpListener<Detail>() {
            @Override
            public void onSuccess(Detail result) {
                detail = result;
                if (detail != null) {
                    xjnr.setText(detail.getNr());
                    if (detail.getFjId() != null) {
                        String str1[] = detail.getFjId().split(",");
                        String str2[] = detail.getFjName().split(",");
                        for (int i = 0; i < str1.length; i++) {
                            if (StringUtils.isNotEmpty(str1[i])) {
                                fjIds.add(str1[i]);
                                fjNames.add(str2[i]);
                            }
                        }
                        wImageAdapter.setmData(fjNames);
                        wImageAdapter.notifyDataSetChanged();
                        ListViewUtil.setListViewHeightBasedOnChildren(myListView);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                showMessage(error.getMessage());
            }
        });
    }

    public void ablum() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        open();
    }


    private  void open(){
        FilePicker picker = new FilePicker(SxxjSaveActivity.this, 1);
        picker.setShowHideDir(false);
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setRootPath("/sdcard/");
        picker.setCanceledOnTouchOutside(true);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showMessage(currentPath);
                updateImg(currentPath);
            }
        });
        picker.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //这里实现用户操作，或同意或拒绝的逻辑
        switch (requestCode) {
            case 1: {
                // 授权被允许
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open();
                } else {
                    showMessage("授权请求被您拒绝");
                }
                return;
            }
        }
    }

    public void submit(String type) {
        if(!StringUtils.isNotEmpty(xjlxId)){
            showMessage("请选择小结类型");
            return ;
        }

        if(xjnr.getText().length()==0){
            showMessage("请输入小结内容");
            return ;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("id",id);
        map.put("xjlx",xjlxId);
        map.put("xjnr",xjnr.getText().toString());

        String fjid="";
        String fjname="";
        for(int i=0;i< fjIds.size();i++){
            fjid+=fjIds.get(i)+",";
            fjname+=fjNames.get(i)+",";
        }
        map.put("fjid",fjid);
        map.put("fjname",fjname);

        HttpUtil.getInstance().postJsonObjectRequest("apps/sxxj/tj", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    boolean json =  result.getBoolean("data");
                    if(json){
                        BroadcastUtil.sendBroadcast(mContext,"sxxj_refresh");
                        showMessage("提交成功");
                        finish();
                    }else{
                        result.getString("msg");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    showMessage("提交失败");
                }
            }
            @Override
            public void onError(VolleyError error) {
                showMessage(error.getMessage());
            }
        });
    }

    private String getDownLoadUrl(String fjId){
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext)
                .getLoginMsg();
        if(detail!=null&&loginMsg!=null){
            return detail.getDownloadUrl()+ "&type="+detail.getDownloadType()+"&ownerId=" + fjId + "&userId="
                    + loginMsg.getUid() + "&password=" + loginMsg.getEncpsw();
        }
        return null;
    }

    /**
     * 上传图片
     */
    public void updateImg(final String path) {
            LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext)
                    .getLoginMsg();
            String mUrl = getReString(R.string.uploadUrlXg);

          final  String fjId=new Date().getTime()+id;
            if (loginMsg != null) {
                mUrl += "/independent.service?.lm=tsyl-sxgl-dwjk&.ms=view&action=fjscjk&.ir=true&type=sxglAttachment&userId="
                        + loginMsg.getUid()
                        + "&password="
                        + loginMsg.getEncpsw()
                        + "&ownerId="
                        + fjId;
            }

        HttpClientUtil.upload(this,mUrl, path,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        hideProgressDialog();
                        showMessage("上传附件失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> response) {
                        JSONObject result = HttpClientUtil
                                .getJSONObject(response);
                        if (result != null) {
                            ResultCode rcode = new Gson().fromJson(
                                    result.toString(), ResultCode.class);
                            if (rcode.getCode() == 200) {
                                fjIds.add(fjId);
                                fjNames.add(new File(path).getName());
                                wImageAdapter.setmData(fjNames);
                                wImageAdapter.notifyDataSetChanged();
                                ListViewUtil.setListViewHeightBasedOnChildren(myListView);

                                if(fjIds.size()>=5){
                                    add.setVisibility(View.GONE);
                                }else{
                                    add.setVisibility(View.VISIBLE);
                                }

                            } else {
                                hideProgressDialog();
                                showMessage(rcode.getMsg());
                            }
                        } else {
                            hideProgressDialog();
                            showMessage("上传附件失败");
                        }
                    }
                });

    }

    public void remove(int index) {
        fjIds.remove(index);
        fjNames.remove(index);
        wImageAdapter.setmData(fjNames);
        wImageAdapter.notifyDataSetChanged();
        ListViewUtil.setListViewHeightBasedOnChildren(myListView);
        if(fjIds.size()>=5){
            add.setVisibility(View.GONE);
        }else{
            add.setVisibility(View.VISIBLE);
        }
    }

}
