package com.dk.mp.apps.gzbxnew.http;

import com.dk.mp.apps.gzbxnew.entity.Bxlx;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.GzbxDetail;
import com.dk.mp.apps.gzbxnew.entity.GzbxRole;
import com.dk.mp.apps.gzbxnew.entity.Result;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpUtil {
	private static final Gson gson;
	static{
		gson = new Gson();
	}
	
	/**
	 * 获取故障报修用户角色
	 * @param responseInfo
	 * @return
	 */
	public static GzbxRole getRole(ResponseInfo<String> responseInfo) {
		GzbxRole role = null;
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				role = gson.fromJson(json.getJSONObject("data").toString(), GzbxRole.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	public static PageMsg getList(ResponseInfo<String> responseInfo) {
		PageMsg page = new PageMsg();
		List<Gzbx> mList = new ArrayList<Gzbx>();
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				page.setTotalPages(json.getJSONObject("data").getInt("totalPages"));
				JSONArray array = json.getJSONObject("data").getJSONArray("list");
				mList = gson.fromJson(array.toString(), new TypeToken<ArrayList<Gzbx>>(){}.getType());
				page.setList(mList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	/**
	 * 获取故障报修操作详情
	 * @param responseInfo
	 * @return
	 */
	public static GzbxDetail getDetail(ResponseInfo<String> responseInfo) {
		GzbxDetail role = null;
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				role = gson.fromJson(json.getJSONObject("data").toString(), GzbxDetail.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	/**
	 * 获取故障报修操作详情
	 * @param responseInfo
	 * @return
	 */
	public static Result getResult(ResponseInfo<String> responseInfo) {
		Result role = null;
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
				role = gson.fromJson(json.toString(), Result.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	/**
	 * 获取故障报修--报修类型
	 * @param responseInfo
	 * @return
	 */
	public static List<Bxlx> getBxlxs(ResponseInfo<String> responseInfo) {
		List<Bxlx> bxlxs = new ArrayList<Bxlx>();
		try {
			JSONObject json = HttpClientUtil.getJSONObject(responseInfo);
			if (json != null && json.getInt("code") == 200) {
				JSONArray array = json.getJSONArray("data");
				bxlxs = gson.fromJson(array.toString(), new TypeToken<ArrayList<Bxlx>>(){}.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bxlxs;
	}
}
