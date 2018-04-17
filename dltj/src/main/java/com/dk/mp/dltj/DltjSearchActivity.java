package com.dk.mp.dltj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.DatePickActivity;
import com.dk.mp.core.widget.ErrorLayout;

public class DltjSearchActivity extends MyActivity {
	WebView mWebView;
	private ErrorLayout mError;
	private ProgressBar mProgressBar;
	String date;


	@Override
	protected int getLayoutID() {
		return R.layout.core_webview;
	}

	@Override
	public void initView() {
		date= TimeUtils.getToday();
		setTitle(date);
		mWebView = (WebView) findViewById(com.dk.mp.core.R.id.webview);
		mError = (ErrorLayout) findViewById(com.dk.mp.core.R.id.error_layout);
		mProgressBar = (ProgressBar) findViewById(com.dk.mp.core.R.id.progressbar);

		findViewById(com.dk.mp.core.R.id.title).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DltjSearchActivity.this, DatePickActivity.class);
				startActivityForResult(intent, 4);
			}
		});

		mError.setErrorType(ErrorLayout.LOADDATA);
		if(DeviceUtil.checkNet()){
			setMUrl();
		}else{
			mError.setErrorType(ErrorLayout.NETWORK_ERROR);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 4:
				if(resultCode == RESULT_OK){
					date = data.getStringExtra("date");
					setTitle(date);
					setMUrl();
				}
				break;
		}
	}

	public void setMUrl(){
		setWebView();
		mWebView.removeAllViews();
		mWebView.clearCache(true);
		mWebView.loadUrl (UrlUtils.getUrl(this,date));
	}

	@SuppressLint("NewApi")
	private void setWebView ( ) {
		WebSettings settings = mWebView.getSettings ( );
		mWebView.setWebViewClient ( new DltjSearchActivity.MyWebViewClient( mProgressBar ) );
		mWebView.setWebChromeClient ( new DltjSearchActivity.MyWebChromeClient( mProgressBar ) );
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
			view.loadUrl(url);
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






}
