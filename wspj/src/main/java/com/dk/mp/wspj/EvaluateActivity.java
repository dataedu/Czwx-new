package com.dk.mp.wspj;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.User;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.wspj.adapter.EvaluatePartmentListAdapter;
import com.dk.mp.wspj.entity.PartmentEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateActivity extends MyActivitySim implements OnItemClickListener{

	private ListView listView; 
	private LoginMsg loginMsg;
	private CoreSharedPreferencesHelper helper;
	private List<PartmentEntity> list = new ArrayList<PartmentEntity>();
	private EvaluatePartmentListAdapter adapter;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_evaluate_partment;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("网上评教");

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		helper = new CoreSharedPreferencesHelper(this);
		loginMsg = new CoreSharedPreferencesHelper(this).getLoginMsg();
		User user = helper.getUser();
		if (user != null) {
			if(!"teacher".equals(user.getRole())){
				Intent intent = new Intent(EvaluateActivity.this, EvaluateTabActivity.class);
				intent.putExtra("userId", loginMsg.getUid());
				startActivity(intent);
				finish();
				return;
			}
		}
		
		initViews();
		
		initDatas();
		
	}
	
	private void initViews(){
		listView = (ListView)findViewById(R.id.evaluate_partment_listview);
		adapter = new EvaluatePartmentListAdapter(EvaluateActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	@SuppressLint("NewApi")
	private void initDatas(){
		HttpUtil.getInstance().postJsonObjectRequest("apps/wspj/getbmList", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
                        if (StringUtils.isNotEmpty(result.getString("data"))){
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							list.clear();
							list = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<PartmentEntity>>(){}.getType());

							if (list.size() == 0) {
								errorLayout.setErrorType(ErrorLayout.NODATA);
							}
							adapter.notifyDataSetChanged();
						}
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(EvaluateActivity.this, EvaluateTabActivity.class);
		intent.putExtra("deptId", list.get(position).getId());
		intent.putExtra("userId", loginMsg.getUid());
		startActivity(intent);
	}
}
