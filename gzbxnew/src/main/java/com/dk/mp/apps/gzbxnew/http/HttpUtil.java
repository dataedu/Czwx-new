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
	 * @param json
	 * @return
	 */
	public static GzbxRole getRole(JSONObject json) {
		GzbxRole role = null;
		try {
			if (json != null && json.getInt("code") == 200) {
				role = gson.fromJson(json.getJSONObject("data").toString(), GzbxRole.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	public static PageMsg getList(JSONObject json) {
		PageMsg page = new PageMsg();
		List<Gzbx> mList = new ArrayList<Gzbx>();
		try {
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
	 * @param json
	 * @return
	 */
	public static GzbxDetail getDetail(JSONObject json) {
		GzbxDetail role = null;
		try {
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
	 * @param json
	 * @return
	 */
	public static Result getResult(JSONObject json) {
		Result role = null;
		try {
				role = gson.fromJson(json.toString(), Result.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	
	/**
	 * 获取故障报修--报修类型
	 * @param json
	 * @return
	 */
	public static List<Bxlx> getBxlxs(JSONObject json) {
		List<Bxlx> bxlxs = new ArrayList<Bxlx>();
		try {
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
