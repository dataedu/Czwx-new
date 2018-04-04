package com.dk.mp.core.view.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * 表格工具.
 * 如果需要左右滑动，需加调用setOnTouchListener(final GestureDetector detector)
 * @version 2013-4-23
 * @author wwang
 */
public class DetailView extends LinearLayout {
	WebView w;
	Context context;

	public DetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		w = new WebView(context);
		w.setInitialScale(100);
		WebSettings webSetting = w.getSettings();
		//设置js可用  
		webSetting.setUseWideViewPort(true); 
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setBuiltInZoomControls(false);
		webSetting.setSupportZoom(false);

		w.setBackgroundColor(0);

	}

	public DetailView(Context context) {
		this(context, null);
	}

	public void setOnTouchListener(final GestureDetector detector) {
		w.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				return false;
			}
		});
	}

	/**
	 * 内容.
	 * @param html 富文本字符串
	 */
	public void setText(String html) {
		removeAllViews();
		StringBuffer s = new StringBuffer();
		s.append("<!DOCTYPE HTML ><html><head>");
		s.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		s.append("<meta name='viewport' content='width=device-width,  initial-scale=1' />");
		s.append(style() + "</head> <body>");
		s.append(html);
		s.append("</body></html>");
		w.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", null);
		this.addView(w);
	}

	private String style() {
		StringBuffer s = new StringBuffer();
		s.append("<style type='text/css'>");
		s.append("html,body {");
		s.append("font-size:1em;");
		s.append("}");
		s.append("</style>");
		return s.toString();
	}
	
	/**
	 * 内容.
	 * @param html 富文本字符串
	 */
	public void setFullHtml(String html) {
		removeAllViews();
		w.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		this.addView(w);
	}

}
