package com.dk.mp.tsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.Department;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tsg.adapter.RecordAdapter;
import com.dk.mp.tsg.entity.BookRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

public class LibraryRecordActivity extends BaseFragment {
	List<BookRecord> list = new ArrayList<>();
	ListView listview;
	private RecordAdapter newsAdapter;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutId() {
		return R.layout.ac_library_record;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		listview = (ListView) view.findViewById(R.id.listView);
		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);

		update();
	}

	public void update() {
		list.clear();
		if (DeviceUtil.checkNet()) {
			HttpUtil.getInstance().postJsonObjectRequest("apps/tsg/getBorrowNow", null, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") != 200) {
							errorLayout.setErrorType(ErrorLayout.DATAFAIL);
						}else{
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							String json =  result.getJSONArray("data").toString();
							List<BookRecord> departments = new Gson().fromJson(json,new TypeToken<List<BookRecord>>(){}.getType());
							list.addAll(departments);
							if (list.size()>0){
								if (newsAdapter == null) {
									newsAdapter = new RecordAdapter(getActivity(), list);
									listview.setAdapter(newsAdapter);
								} else {
									newsAdapter.setData(list);
									newsAdapter.notifyDataSetChanged();
								}
							}else {
								errorLayout.setErrorType(ErrorLayout.NODATA);
								errorLayout.setTextv("暂无在借图书");
							}
						}
					}catch (Exception e){
						e.printStackTrace();
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					}
				}

				@Override
				public void onError(VolleyError error) {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			});
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}


}
