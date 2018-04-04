package com.dk.mp.apps.hy.http;

import com.dk.mp.apps.hy.entity.Bm;
import com.dk.mp.apps.hy.entity.Ks;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.util.Logger;
import com.lidroid.xutils.http.ResponseInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 
 * @version 2013-4-2
 * @author wwang
 */
public class YellowPageHttpUtil {
	
	
	/**
	 * 解析刚刚下载下来的联系人文件，然后将数据保存到数据库表里.
	 * @param context Context
	 * @param path 保存路径
	 */
	public static List<Bm> getDepartList(ResponseInfo<String> responseInfo) {
		List<Bm> list = new ArrayList<Bm>();
		try {
			JSONObject s = HttpClientUtil.getJSONObject(responseInfo);
			if (s != null) {
				JSONArray array = s.getJSONArray("data");
				Logger.info(s.toString());
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					Bm o = new Bm();
					o.setIdDepart(object.getString("idDepart"));
					o.setNameDepart(object.getString("nameDepart"));
					list.add(o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/**
	 * 解析刚刚下载下来的联系人文件，然后将数据保存到数据库表里.
	 * @param context Context
	 * @param path 保存路径
	 */
	public static List<Ks> getPeopleList(ResponseInfo<String> responseInfo) {
		List<Ks> list = new ArrayList<Ks>();
		try {
			JSONObject s = HttpClientUtil.getJSONObject(responseInfo);
			if (s != null) {
				JSONArray array = s.getJSONArray("data");
				Logger.info(s.toString());
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					Ks p = new Ks();
					p.setIdUser(object2.getString("id"));
					p.setName(object2.getString("name"));
					
					String hm="";
					List<String> list2 = new ArrayList<String>();
					JSONArray values = object2.getJSONArray("values");
					for (int index = 0; index < values.length(); index++) {
						list2.add(values.getString(index));
						if(index==0){
							hm=	values.getString(index);	
						}else{
							hm+="/"+values.getString(index);
						}
					}
					p.setTels(hm);
					p.setList(list2);
					list.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
}
