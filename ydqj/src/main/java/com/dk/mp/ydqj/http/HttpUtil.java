package com.dk.mp.ydqj.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 数据组装
 * @author admin
 *
 */
public class HttpUtil {

	/**
	 * 获取请假列表
	 * @param responseInfo
	 * @return
	 */
	/*public static PageMsg getMyLeaves(ResponseInfo<String> responseInfo) {
		PageMsg page = new PageMsg();
		List<Leave> mList = new ArrayList<Leave>();
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				page.setTotalPages(json.getJSONObject("data").getInt("totalPages"));
				JSONArray array = json.getJSONObject("data").getJSONArray("list");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jo = array.getJSONObject(i);
					Leave d = new Leave();
					d.setId(jo.optString("id"));
					d.setQjlx(getString(jo.optString("qjlx")));
					d.setStatus(getString(jo.optString("status")));
					d.setSqsj(getString(jo.optString("sqsj")));
					d.setSqr(getString(jo.optString("sqr")));
					mList.add(d);
				}
				page.setList(mList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	public static String getDetailHtml(ResponseInfo<String> responseInfo){
		String html="";
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				JSONObject jo = json.getJSONObject("data");
				html = jo.getString("html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
	
	public static String check(ResponseInfo<String> responseInfo){
		String html="";
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				html = json.getString("msg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
	
	public static String imgs(ResponseInfo<String> responseInfo){
		String imgs="";
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo).getJSONObject("jsonp");
			if (json != null && json.getInt("code") == 200) {
				JSONArray ja = json.getJSONArray("data");
				for(int i =0;i<ja.length();i++){
					String url = ja.getJSONObject(i).optString("url");
					if(i == ja.length()-1){
						imgs += url; 
					}else{
						imgs += url+",";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imgs;
	}

	public static String getString(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}
		return "";
	}*/

}
