package com.dake.mp.zslq;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

public class ZslqActivity extends BaseFragment {
	WebView mWebView;
	private ErrorLayout mError;
	private ProgressBar mProgressBar;

	@Override
	protected int getLayoutId() {
		return com.dk.mp.core.R.layout.core_webview;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		view.findViewById(com.dk.mp.core.R.id.layout_top).setVisibility(View.GONE);
		mWebView = (WebView) view.findViewById(com.dk.mp.core.R.id.webview);
		mError = (ErrorLayout) view.findViewById(com.dk.mp.core.R.id.error_layout);
		mProgressBar = (ProgressBar) view.findViewById(com.dk.mp.core.R.id.progressbar);

		String url = "<img src='"+getUrl("apps/zsxx/"+getArguments().getString("type"))+"' width='100%'>";



		mError.setErrorType(ErrorLayout.LOADDATA);
		if(DeviceUtil.checkNet()){
			setMUrl(url);
		}else{
			mError.setErrorType(ErrorLayout.NETWORK_ERROR);
		}

	}


	public void setMUrl(String url){
		setWebView();

		Logger.info("##########murl="+url);
		mWebView.removeAllViews();
		mWebView.clearCache(true);
		mWebView.loadUrl (url);
		mWebView.loadData(url, "text/html; charset=UTF-8", null);
	}

	private void setWebView ( ) {
		WebSettings settings = mWebView.getSettings ( );
		mWebView.setWebViewClient ( new ZslqActivity.MyWebViewClient( mProgressBar ) );
		mWebView.setWebChromeClient ( new ZslqActivity.MyWebChromeClient( mProgressBar ) );
		settings.setSupportZoom ( true );          //支持缩放
		settings.setBlockNetworkImage ( false );  //设置图片最后加载
		settings.setDatabaseEnabled ( true );
		settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
		settings.setJavaScriptEnabled ( true );    //启用JS脚本
		settings.setUseWideViewPort(true);
		settings.setBuiltInZoomControls(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
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

			return mContext.getString(com.dk.mp.core.R.string.rootUrl)+url+user;
		}


	}



}
