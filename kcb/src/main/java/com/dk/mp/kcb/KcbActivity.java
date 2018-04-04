package com.dk.mp.kcb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.kcb.adapter.KcbAdapter;
import com.dk.mp.kcb.entity.Week;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * 作者：janabo on 2017/10/24 11:16
 */
public class KcbActivity extends MyActivity implements OnItemClickListener{
    private ErrorLayout mError;
    private LinearLayout week_linear,select_week;
    private ListView listView;
    private KcbAdapter sAdapter;
    private List<Week> weeks = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private int select_str=0;
    private WebView mWebView;
    private TextView title;

    private RelativeLayout webviewlayout;
    private ImageView pullimageview;

    @Override
    protected int getLayoutID() {
        return R.layout.app_kbcx;
    }

    @Override
    protected void initView() {
        super.initView();
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        week_linear = (LinearLayout) findViewById(R.id.week_linear);
        select_week = (LinearLayout) findViewById(R.id.select_week);
        listView = (ListView) findViewById(R.id.listView);
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(KcbActivity.this,HttpWebActivity.class).putExtra("title", "课表详情").putExtra("url", url));
                return true;// true表示此事件在此处被处理，不需要再广播
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        title = (TextView) findViewById(R.id.title);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
        //点击选择周次
        week_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(select_str == 0){//显示日期选择框
                select_str = 1;
                select_week.setVisibility(View.VISIBLE);
            }else{
                select_str = 0;
                select_week.setVisibility(View.GONE);
            }
            }
        });
        webviewlayout = (RelativeLayout) findViewById(R.id.webviewlayout);
        pullimageview = (ImageView) findViewById(R.id.pullimageview);
        try {
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        if(DeviceUtil.checkNet()){//判断是否有网络
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            webviewlayout.setVisibility(View.GONE);
        }
    }

    public void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/kcb/weekList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        JSONArray jo = result.optJSONArray("data");
                        if(jo == null || jo.length()<=0) {//是否为空
                            mHandler.sendEmptyMessage(0);
                        }else {
                            for (int i=0;i<jo.length();i++){
                                Week w = new Gson().fromJson(jo.get(i).toString(),Week.class);
                                weeks.add(w);
                            }
                            mHandler.sendEmptyMessage(1);
                        }
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

    Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0://没有数据
                    mError.setErrorType(ErrorLayout.NODATA);
                    webviewlayout.setVisibility(View.GONE);
                    break;
                case 1://第一次获取数据
                    pullimageview.setVisibility(View.VISIBLE);
                    String currentWeekNo =weeks.get(0).getNow();
                    title.setText("第"+currentWeekNo+"周");
                    LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
                    if (loginMsg != null) {
                        loadurl("apps/kcb/getEveryWeekCouse?weekNo="+currentWeekNo+"&uid="+loginMsg.getUid());
                    }
                    int item = 0;
                    for(int i=0;i<weeks.size();i++){
                        Week w = weeks.get(i);
                        if(w.getNo().equals(w.getNow())){
                            item = i;
                            list.add("第"+w.getNo()+"周(本周)");
                        }else{
                            list.add("第"+w.getNo()+"周");
                        }
                    }

                    if (sAdapter == null) {
                        sAdapter = new KcbAdapter(mContext, list,item);
                        listView.setAdapter(sAdapter);
                    } else {
                        sAdapter.setList(list);
                        sAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2://请求出错
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                    webviewlayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        };
    };

    @SuppressLint("JavascriptInterface")
    private void loadurl(String url) {
        mError.setErrorType(ErrorLayout.LOADDATA);
        if (DeviceUtil.checkNet()) {
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            url = getUrl(mContext,url);
            Logger.info("url:" + url);
            WebSettings webSetting = mWebView.getSettings();
            //设置js可用
            webSetting.setJavaScriptEnabled(true);
            webSetting.setBuiltInZoomControls(false);
            webSetting.setLightTouchEnabled(true);
            webSetting.setSupportZoom(false);
            mWebView.setHapticFeedbackEnabled(false);
            mWebView.addJavascriptInterface(this, "android");

            //载入具体的web地址
            mWebView.setVisibility(View.VISIBLE);
            mWebView.requestFocus();
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(new HttpWebViewClient());
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else {
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            mWebView.setVisibility(View.GONE);
        }
    }

    //web视图客户端
    public class HttpWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
            startActivity(new Intent(KcbActivity.this,HttpWebActivity.class).putExtra("title", "课表详情").putExtra("url", url));
            return true;
        }

        //开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        //结束加载
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            } else {
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        sAdapter.setSelectItem(position);
        sAdapter.notifyDataSetInvalidated();
        String cWeek = list.get(position);
        position = position +1;
        Logger.info("position:" + position);
        if(cWeek.indexOf("(本周)")>0){
            cWeek = cWeek.substring(0, cWeek.indexOf("(本周)"));
        }
        title.setText(cWeek);
        select_str = 0;
        select_week.setVisibility(View.GONE);
        if(DeviceUtil.checkNet()){
            LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
            if (loginMsg != null) {
                loadurl("apps/kcb/getEveryWeekCouse?weekNo="+weeks.get(position).getNo()+"&uid="+loginMsg.getUid());
                mWebView.clearHistory();
            }
        }else {
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }


    /**
     * 处理请求链接.
     *
     * @return 处理后的请求链接
     */
    private static String getUrl(Context context, String url) {
        if (url.startsWith("http://")) {
            return url;
        } else {
            return context.getResources().getString(R.string.rootUrl) + url;
        }
    }

}
