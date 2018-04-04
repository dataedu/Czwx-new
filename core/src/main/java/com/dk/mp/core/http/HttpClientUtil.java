package com.dk.mp.core.http;

import android.content.Context;

import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.encrypt.Base64Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * @since
 * @version 2012-11-21
 * @author wangw
 */
public class HttpClientUtil {
	private static HttpUtils httpUtils = null;
	private static final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 60 * 1000; // 设置等待数据超时时间10秒钟
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	@SuppressWarnings("deprecation")
	public static void post(String url, Map<String, String> map, RequestCallBack<String> callBack) {
		Context context = MyApplication.newInstance().getApplicationContext();
		if (httpUtils == null) {
			httpUtils = MyApplication.httpUtils;
		}
		RequestParams params = new RequestParams();
		LoginMsg loginMsg = new CoreSharedPreferencesHelper(context).getLoginMsg();
		if (loginMsg != null&&!"login".equals(url)) {
			params.addBodyParameter("uid", loginMsg.getUid());
			params.addBodyParameter("pwd", Base64Utils.getBase64(loginMsg.getPsw()));
		}
		
//		params.addBodyParameter("uid", "J02084");
//		params.addBodyParameter("pwd", Base64Utils.getBase64("111111"));
		
		if (map != null) {
			
			for (Map.Entry<String, String> entry :  map.entrySet()) {
				Logger.info("POST 请求参数:" + (entry.getKey() + "=" + entry.getValue()));
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		url = getUrl(context,url);
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, callBack);
	}

	
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	@SuppressWarnings("deprecation")
	public static void post(String url, Map<String, String> map,final int what,final HttpClientCallBack callBack) {
		final Context context = MyApplication.newInstance().getApplicationContext();
		if (httpUtils == null) {
			httpUtils = MyApplication.httpUtils;
		}
		RequestParams params = new RequestParams();
		LoginMsg loginMsg = new CoreSharedPreferencesHelper(context).getLoginMsg();
		if (loginMsg != null&&!"login".equals(url)) {
			params.addBodyParameter("uid", loginMsg.getUid());
			params.addBodyParameter("pwd", Base64Utils.getBase64(loginMsg.getPsw()));
		}
		
		if (map != null) {
			for (Map.Entry<String, String> entry :  map.entrySet()) {
				Logger.info("POST 请求参数:" + (entry.getKey() + "=" + entry.getValue()));
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				callBack.success(what,getJSONObject(responseInfo));
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				MsgDialog.show(context, "服务发送错误，请稍后重试");
				callBack.fail(what);
			}
		});
	}
	
	
	
	public static void upload(Context context, String url, String filePth, RequestCallBack<String> callBack) {
		try {
			RequestParams params = new RequestParams();
			MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
			ContentBody cbFile = new FileBody(new File(filePth));
			mpEntity.addPart("file", cbFile); // <input type="file"
			params.setBodyEntity(mpEntity);
			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.POST, getUrl(context, url), params, callBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * json转换.
	 * 
	 * @param responseInfo
	 *            HttpResponse
	 * @return JSONObject
	 */
	public static JSONObject getJSONObject(ResponseInfo<String> responseInfo) {
		JSONObject json = null;
		try {
			Logger.info("返回原始数据:\n" + responseInfo.result);
			if (StringUtils.isNotEmpty(responseInfo.result)) {
				if (responseInfo.result.toLowerCase().startsWith("<html")) {
					
				} else {
					json = new JSONObject(responseInfo.result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 处理请求链接.
	 * 
	 * @param url
	 *            请求链接
	 * @return 处理后的请求链接
	 */
	private static String getUrl(Context context, String url) {
		if (url.startsWith("http://")) {
			return url;
		} else {
			return context.getResources().getString(R.string.rootUrl) + url;
		}
	}
	
	
	/**
	 * get请求获取json数据.
	 * @param url 请求链接
	 */
	public static void get(String url, RequestCallBack<String> callBack) {
		Context context = MyApplication.newInstance().getApplicationContext();
		if (httpUtils == null) {
			httpUtils = MyApplication.httpUtils;
		}
		Logger.info("POST 请求连接=======" + getUrl(context, url));
		httpUtils.send(HttpRequest.HttpMethod.GET, getUrl(context, url), null, callBack);
	}

}
