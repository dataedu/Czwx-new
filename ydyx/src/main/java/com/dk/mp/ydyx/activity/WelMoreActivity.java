package com.dk.mp.ydyx.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ydyx.R;
import com.dk.mp.ydyx.adapter.MoreAdapter;
import com.dk.mp.ydyx.entity.WelMore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 离校子项.
 * 
 * @since
 * @version2015-3-25
 * @author rm.zhao
 */
public class WelMoreActivity extends MyActivity {
	private TextView remind,titles;
	private ListView listview;
	private MoreAdapter adapter;
	private List<WelMore> list=new ArrayList<WelMore>();
	private WelMore leave;
	private String id, name, tip;
	private LinearLayout lr;
	private String idUser;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_wel_more;
	}

	@Override
	protected void initialize() {
		super.initialize();

		findView();
		id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		tip = getIntent().getStringExtra("tip");
		titles=(TextView) findViewById(R.id.title);
		idUser = getIntent().getStringExtra("idUser");
		titles.setTextSize(17);
		if(StringUtils.isNotEmpty(tip)){
		remind.setText(tip);
		}else{
			lr.setVisibility(View.GONE)	;
		}
		setTitle(name);
		getDate(id);
	}

	private void findView() {
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		remind = (TextView) findViewById(R.id.remind);
		listview = (ListView) findViewById(R.id.listmore);
		lr=(LinearLayout) findViewById(R.id.lr);
	}

	public void getDate(final String idProcess) {
		if (DeviceUtil.checkNet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idProcess", idProcess);
			map.put("idUser", idUser);
			HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/getSubProcess", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							list = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<WelMore>>(){}.getType());
							handler.sendEmptyMessage(0);
                        }
					} catch (JSONException e) {
						e.printStackTrace();
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					}
				}

				@Override
				public void onError(VolleyError error) {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			});
		}else {
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(list.size()>0){
				if (adapter == null) {
					adapter = new MoreAdapter(WelMoreActivity.this, list);
					listview.setAdapter(adapter);
				} else {
					listview.setAdapter(adapter);
				}
			}else {
				errorLayout.setErrorType(ErrorLayout.NODATA);
			}
		}
	};

}
