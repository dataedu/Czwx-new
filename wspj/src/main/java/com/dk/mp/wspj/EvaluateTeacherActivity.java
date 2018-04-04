package com.dk.mp.wspj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.wspj.adapter.EvaluateTeacherListAdapter;
import com.dk.mp.wspj.entity.TeacherEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class EvaluateTeacherActivity extends BaseFragment implements OnItemClickListener{

	private ListView listView; 
	private List<TeacherEntity> list = new ArrayList<TeacherEntity>();
	private EvaluateTeacherListAdapter adapter;
	private LoginMsg loginMsg;
	private LinearLayout top;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutId() {
		return R.layout.ac_evaluate_teacher;
	}

	public static EvaluateTeacherActivity newInstance(String tag, String deptId) {
		Bundle args = new Bundle();
		args.putString("tag",tag);
		args.putString("deptId",deptId);
		EvaluateTeacherActivity fragment = new EvaluateTeacherActivity();
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	protected void initialize(View view) {
		super.initialize(view);

		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);

		loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();

		listView = (ListView) view.findViewById(R.id.evaluate_partment_listview);
		adapter = new EvaluateTeacherListAdapter(mContext, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		top = (LinearLayout) view.findViewById(R.id.zwsj);
	}

	@Override
	public void onResume() {
		super.onResume();

		initDatas();
	}
	
	@SuppressLint("NewApi")
	private void initDatas(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptId", getArguments().getString("deptId"));
		map.put("tag", getArguments().getString("tag"));
		HttpUtil.getInstance().postJsonObjectRequest("apps/wspj/getTeacherList", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						list.clear();
						list = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<TeacherEntity>>(){}.getType());
						if (list.size() == 0){
							errorLayout.setErrorType(ErrorLayout.NODATA);
							return;
						}
						adapter.notifyDataSetChanged();
						top.setVisibility(View.GONE);
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
		Intent intent = new Intent(mContext, EvaluateDetails.class);
		intent.putExtra("pjztid", list.get(position).getPjztdm());
		intent.putExtra("type", getArguments().getString("tag"));
		intent.putExtra("userId", loginMsg.getUid());
		intent.putExtra("pcdm", list.get(position).getPcdm());
		intent.putExtra("kcdm", list.get(position).getKcdm());
		intent.putExtra("pjztid", list.get(position).getPjztdm());
		intent.putExtra("zgh", list.get(position).getTeacherId());
		intent.putExtra("cddw", list.get(position).getDeptId());
		intent.putExtra("teacherId", list.get(position).getTeacherId());
		intent.putExtra("status", list.get(position).getStatus());
		startActivity(intent);
	}


}
