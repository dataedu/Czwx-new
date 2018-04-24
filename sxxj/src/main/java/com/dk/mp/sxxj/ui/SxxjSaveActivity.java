package com.dk.mp.sxxj.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.ResultCode;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.TextPickActivity;
import com.dk.mp.sxxj.R;
import com.dk.mp.sxxj.adapter.AddImageAdapter;
import com.dk.mp.sxxj.entity.Detail;
import com.dk.mp.sxxj.entity.Type;
import com.dk.mp.sxxj.widget.MyGridView;
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

import static com.dk.mp.core.util.ImageUtil.BASEPICPATH;

/**
 * 学校新闻列表
 * 作者：janabo on 2016/12/19 10:08
 */
public class SxxjSaveActivity extends MyActivity {
    private MyGridView myListView;
    LinearLayout xjlx_bg;
    TextView xjlx;
    Button tj,bc;
    EditText xjnr;
    String id,xjlxId;

    Detail detail=null;

    String  imgTemp;



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

        type = (ArrayList<Type>) getIntent().getSerializableExtra("types");

        id=getIntent().getStringExtra("id");
        myListView = (MyGridView) findViewById(R.id.newslist);
        xjlx_bg = (LinearLayout) findViewById(R.id.xjlx_bg);
        xjlx = (TextView) findViewById(R.id.xjlx);
        tj = (Button) findViewById(R.id.tj);
        bc = (Button) findViewById(R.id.bc);
        xjnr = (EditText) findViewById(R.id.xjnr);
        xjlx.setText(StringUtils.checkEmpty(getIntent().getStringExtra("type")));
        getCg();

        xjlx_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.size()>0){
                    Intent intent = new Intent(SxxjSaveActivity.this, TextPickActivity.class);
                    String xjlxNames="";
                    if( xjlxNames!=null){
                        for(int i=0;i<type.size();i++){
                            xjlxNames+=type.get(i).getName()+",";
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

        wImageAdapter = new AddImageAdapter(mContext, SxxjSaveActivity.this,
                fjIds);
        myListView.setAdapter(wImageAdapter);


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

            case 6: // 拍照
                if (resultCode == RESULT_OK) {
                    if(imgTemp!=null){
                        if (new File(imgTemp).exists()) {
                            updateImg();
                        }
                    }
                }
                break;
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
                            fjIds.add(str1[i]);
                            fjNames.add(str2[i]);
                        }
                        wImageAdapter.setmData(fjIds);
                        wImageAdapter.notifyDataSetChanged();
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
        File appDir = new File(BASEPICPATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        imgTemp = BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
        File file = new File(imgTemp);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		/* 获取当前系统的android版本号 */
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            getImageByCamera
                    .setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,
                    file.getAbsolutePath());
            Uri uri = mContext.getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues);
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // Intent getImageByCamera = new
        // Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        // getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
        // File(noCutFilePath)));
        startActivityForResult(getImageByCamera, 6);
    }

    /**
     * 上传图片
     */
    public void updateImg() {
            LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext)
                    .getLoginMsg();
            String mUrl = getReString(R.string.uploadUrlXg);

          final  String fjId=new Date().getTime()+id;

            if (loginMsg != null) {
                mUrl += "/independent.service?.lm=ssgl-dwjk-upv&.ms=view&action=fjscjk&.ir=true&type=sswzdjAttachment&userId="
                        + "2016110508"
                        + "&password="
                        + loginMsg.getPsw()
                        + "&ownerId="
                        + fjId;
            }

        HttpClientUtil.upload(this,mUrl, imgTemp,
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
                                fjNames.add("IMG_"+new Date().getTime()+".png");
                                wImageAdapter.setmData(fjIds);
                                wImageAdapter.notifyDataSetChanged();
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
        wImageAdapter.setmData(fjIds);
        wImageAdapter.notifyDataSetChanged();
    }

}
