package com.dk.mp.dltj;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;

public class DltjActivity extends BaseFragment {
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
		String type=getArguments().getString("type");

		mError.setErrorType(ErrorLayout.LOADDATA);
		if(DeviceUtil.checkNet()){
			setMUrl(type);
		}else{
			mError.setErrorType(ErrorLayout.NETWORK_ERROR);
		}

	}


	public void setMUrl(String type){
		setWebView();

		Logger.info("##########murl="+type);
		mWebView.removeAllViews();
		mWebView.clearCache(true);
		mWebView.loadUrl (UrlUtils.getUrl(getContext(),type));
	}

	private void setWebView ( ) {
		WebSettings settings = mWebView.getSettings ( );
		mWebView.setWebViewClient ( new DltjActivity.MyWebViewClient( mProgressBar ) );
		mWebView.setWebChromeClient ( new DltjActivity.MyWebChromeClient( mProgressBar ) );
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
