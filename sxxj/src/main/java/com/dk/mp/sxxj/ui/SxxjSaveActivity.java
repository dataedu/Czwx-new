package com.dk.mp.sxxj.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dk.mp.sxxj.widget.MyGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leon.lfilepickerlibrary.LFilePicker;
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


import static com.dk.mp.core.util.ImageUtil.BASEPICPATH;

/**
 * 学校新闻列表
 * 作者：janabo on 2016/12/19 10:08
 */
public class SxxjSaveActivity extends MyActivity {
    private ListView myListView;
    LinearLayout xjlx_bg;
    TextView xjlx,add;
    Button tj,bc;
    EditText xjnr;
    String id,xjlxId;

    Detail detail=null;
    AppCompatActivity context;

    private final int EX_FILE_PICKER_RESULT = 0xfa01;
    private String startDirectory = null;// 记忆上一次访问的文件目录路径


    private ArrayList<Type> type = new ArrayList<Type>();
    public List<String> fjIds = new ArrayList<>();// 保存图片地址
    public List<String> fjNames = new ArrayList<>();// 保存图片地址

    AddImageAdapter wImageAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.app_sxxj_save;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        context=SxxjSaveActivity.this;
        type = (ArrayList<Type>) getIntent().getSerializableExtra("types");

        id=getIntent().getStringExtra("id");
        if(id==null){
            id=UUID.randomUUID().toString();
        }
        myListView = (ListView) findViewById(R.id.newslist);
        xjlx_bg = (LinearLayout) findViewById(R.id.xjlx_bg);
        xjlx = (TextView) findViewById(R.id.xjlx);
        add = (TextView) findViewById(R.id.add);
        tj = (Button) findViewById(R.id.tj);
        bc = (Button) findViewById(R.id.bc);
        xjnr = (EditText) findViewById(R.id.xjnr);
        if(StringUtils.isNotEmpty(getIntent().getStringExtra("type"))) {
            xjlx.setText(StringUtils.checkEmpty(getIntent().getStringExtra("type")));
            xjlxId =StringUtils.checkEmpty(getIntent().getStringExtra("type"));
        }else{
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
                if(type.size()>0){
                    Intent intent = new Intent(SxxjSaveActivity.this, TextPickActivity.class);
                    String xjlxNames="";
                    if( xjlxNames!=null){
                        for(int i=0;i<type.size();i++){
                            if(i==0){
                                xjlxNames+=type.get(i).getName();
                            }else{
                                xjlxNames+=","+type.get(i).getName();
                            }


                        }
                    }
                    intent.putExtra("items",xjlxNames);
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

                                Intent intent = new Intent(SxxjSaveActivity.this, TextPickActivity.class);
                                String str="";
                                for(int i=0;i<type.size();i++){
                                    if(i==0){
                                        str+=type.get(i).getName();
                                    }else{
                                        str+=","+type.get(i).getName();
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

        wImageAdapter = new AddImageAdapter(mContext, SxxjSaveActivity.this,
                fjNames);
        myListView.setAdapter(wImageAdapter);


    }

    public void open(int position){
        FileUtil.openFileByUrl(mContext, getDownLoadUrl(fjIds.get(position)), fjNames.get(position));
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
                if(resultCode == RESULT_OK){
                  int   index = data.getIntExtra("index",0);
                    xjlxId=type.get(index).getId();
                    xjlx.setText(type.get(index).getName());
                }
                break;

            case EX_FILE_PICKER_RESULT:
                if (resultCode == RESULT_OK) {
                        //If it is a file selection mode, you need to get the path collection of all the files selected
                        //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                        List<String> list = data.getStringArrayListExtra("paths");
                        if(list.size()>0) {
                            Toast.makeText(getApplicationContext(), "selected " + list.get(0), Toast.LENGTH_SHORT).show();
                            updateImg( list.get(0));
                        }
                }
        }
    }


    public void getCg(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        HttpUtil.getInstance().gsonRequest(new TypeToken<Detail>(){}, "apps/sxxj/cg", map, new HttpListener<Detail>() {
            @Override
            public void onSuccess(Detail result) {
                detail=result;
                if(detail!=null){
                    xjnr.setText(detail.getNr());
                    if(detail.getFjId()!=null){
                      String str1[] =  detail.getFjId().split(",");
                        String str2[] =  detail.getFjName().split(",");
                        for(int i=0;i<str1.length;i++){
                            if(StringUtils.isNotEmpty(str1[i])) {
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
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(EX_FILE_PICKER_RESULT)
                .withStartPath("/sdcard/")
                .withMutilyMode(false)
                .start();
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
