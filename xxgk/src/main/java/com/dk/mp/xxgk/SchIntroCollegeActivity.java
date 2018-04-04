package com.dk.mp.xxgk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xxgk.adapter.DepartAdapter;
import com.dk.mp.xxgk.db.IntroDBHelper;
import com.dk.mp.xxgk.entity.Depart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 院系介绍.
 * 
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
@SuppressLint("HandlerLeak")
public class SchIntroCollegeActivity extends BaseFragment implements OnItemClickListener {
	private ListView listView;
	private List<Depart> departs;
	private DepartAdapter mAdapter;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutId() {
		return R.layout.app_intro_college;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);
		listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);

		if(DeviceUtil.checkNet()){//判断是否有网络，没网络到本地数据库获取
			init();
		}else{
			IntroDBHelper introDBHelper = new IntroDBHelper(mContext);
			departs = introDBHelper.getDepartList("");
			if (departs == null || departs.size()<=0) {
				listView.setVisibility(View.GONE);
				errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
			}else{
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				MsgDialog.show(mContext, mContext.getString(R.string.net_no2));
				handler.sendEmptyMessage(1);
			}
		}
	}

	private void init() {
		HttpUtil.getInstance().postJsonObjectRequest("apps/introduRest/depart", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200) {
						String json = result.getJSONArray("data").toString();
						if (json != null && !json.equals("null")) {
							List<Depart> departments = new Gson().fromJson(json, new TypeToken<List<Depart>>() {
							}.getType());
							departs = departments;
							if (null != departs && departs.size() > 0) {
								new IntroDBHelper(mContext).insertTable(departs);
							}
							handler.sendEmptyMessage(1);
						} else {
							errorLayout.setErrorType(ErrorLayout.NODATA);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
					showMessage(getString(R.string.data_fail));
					IntroDBHelper introDBHelper = new IntroDBHelper(mContext);
					departs = introDBHelper.getDepartList("");
					handler.sendEmptyMessage(1);
				}
			}

			@Override
			public void onError(VolleyError error) {
				showMessage(getString(R.string.data_fail));
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				IntroDBHelper introDBHelper = new IntroDBHelper(mContext);
				departs = introDBHelper.getDepartList("");
				handler.sendEmptyMessage(1);
			}
		});
	}

	/**
	 * 计时器执行的任务.
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			if (mAdapter == null) {
				mAdapter = new DepartAdapter(mContext, departs);
				listView.setAdapter(mAdapter);
			} else {
				mAdapter.setList(departs);
				mAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Depart depart = departs.get(position);
		Intent intent = new Intent(mContext, SchIntroCollegeDetailActivity.class);
		intent.putExtra("name", depart.getName());
		intent.putExtra("content", depart.getContent());
		startActivity(intent);

	}


}
