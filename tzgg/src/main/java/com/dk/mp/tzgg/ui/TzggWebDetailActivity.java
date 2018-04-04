package com.dk.mp.tzgg.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tzgg.R;

/**
 * 作者：janabo on 2017/2/20 11:56
 */
public class TzggWebDetailActivity extends MyActivity {

    WebView mWebView;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;

    private TextView textView,webtime;

    @Override
    protected int getLayoutID() {
        return R.layout.tzgg_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        mWebView = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        textView = (TextView) findViewById(R.id.webtitle);
        webtime = (TextView) findViewById(R.id.webtime);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");

        mError.setErrorType(ErrorLayout.LOADDATA);

        String webtitle = getIntent().getStringExtra("webtitle");
        if (webtitle != null && StringUtils.isNotEmpty(webtitle)){
            textView.setVisibility(View.VISIBLE);
            textView.setText(webtitle);
        }

        String time = getIntent().getStringExtra("webtime");
        if (time != null && StringUtils.isNotEmpty(time)){
            webtime.setVisibility(View.VISIBLE);
            webtime.setText(time);
        }

        if(DeviceUtil.checkNet()){
            setMUrl(url);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void setMUrl(String url){
        setWebView();
        url = getUrl(url);
        Logger.info("##########murl="+url);
        mWebView.removeAllViews();
        mWebView.clearCache(true);
        mWebView.loadUrl (url);
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient ( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient ( mProgressBar ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
    }


    public class MyWebViewClient extends WebViewClient {
        ProgressBar mProgressBar;
        public MyWebViewClient ( ProgressBar progressBar ) {
            super ( );
            mProgressBar = progressBar;
        }

        @Override
        public void onPageStarted ( WebView view, String url, Bitmap favicon ) {
            super.onPageStarted ( view, url, favicon );
            mProgressBar.setVisibility ( View.VISIBLE );
        }

        @Override
        public void onPageFinished ( WebView webView, String url ) {
            super.onPageFinished ( webView, url );
            mProgressBar.setVisibility ( View.INVISIBLE );
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        ProgressBar mWebProgressBar;

        public MyWebChromeClient ( ProgressBar mWebProgressBar ) {
            this.mWebProgressBar = mWebProgressBar;
        }

        @Override
        public void onProgressChanged ( WebView view, int newProgress ) {
            mWebProgressBar.setProgress ( newProgress );
            Logger.info("##########newProgress="+newProgress);
            if(newProgress>=100){
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        }

        @Override
        public void onReceivedTitle ( WebView view, String title ) {
            super.onReceivedTitle ( view, title );
        }
    }


    /**
     * 处理url
     * @param url
     * @return
     */
    private String getUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getString(R.string.rootUrl)+url;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }
}
