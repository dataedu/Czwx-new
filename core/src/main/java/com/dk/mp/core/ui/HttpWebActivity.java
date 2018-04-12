package com.dk.mp.core.ui;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.dk.mp.core.R;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.request.HttpRequest;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;

/**
 * 作者：janabo on 2017/2/20 11:56
 */
public class HttpWebActivity extends MyActivity {
    WebView mWebView;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayoutID() {
        return R.layout.core_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        mWebView = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        mError.setErrorType(ErrorLayout.LOADDATA);
        if(DeviceUtil.checkNet()){
            setMUrl(url);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

        try {
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        mWebView.loadUrl(null);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
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

        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            Logger.info("url==========="+url);
            view.loadUrl(getUrl(url));
            return true;
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
        String user="";
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
        if (loginMsg != null) {
            if(url.contains("?")){
                user="&uid="+  loginMsg.getUid()+"&pwd="+loginMsg.getPsw();
            }else{
                user="?uid="+  loginMsg.getUid()+"&pwd="+loginMsg.getPsw();
            }
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url+user;
        } else {

            return mContext.getString(R.string.rootUrl)+url+user;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            mWebView.loadUrl(null);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
