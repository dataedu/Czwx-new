package com.dk.mp.apps.hy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.VolleyError;
import com.dk.mp.apps.hy.adapter.KsAdapter;
import com.dk.mp.apps.hy.db.YellowPageDBHelper;
import com.dk.mp.apps.hy.entity.Ks;
import com.dk.mp.apps.hy.http.YellowPageHttpUtil;

import com.dk.mp.core.dialog.ListRadioDialog;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;

import com.dk.mp.core.widget.ErrorLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

public class HySearchActivity extends MyActivity {
	private KsAdapter mAdapter;
	private ListView mListView;
	private EditText searchKeywords;
	private List<Ks> list;
	private TextView cancle_search;
	private ErrorLayout errorLayout;
	@Override
	protected int getLayoutID() {
		return R.layout.app_yellowpage_query;
	}

	@Override
	protected void initialize() {
		super.initialize();

		findView();

		if (!DeviceUtil.checkNet2()) {
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
			mListView.setVisibility(View.GONE);
		}else{
			mListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化控件.
	 */
	private void findView() {
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		mListView = (ListView) findViewById(R.id.listView);
		cancle_search = (TextView) findViewById(R.id.cancle_search);
		searchKeywords = (EditText) findViewById(R.id.search_Keywords);
		searchKeywords.setHint("科室、电话");
		searchKeywords.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((actionId == 0 || actionId == 3) && event != null) {
					final String keywords = searchKeywords.getText().toString();
					Logger.info(keywords);
					if (StringUtils.isNotEmpty(keywords)) {
						query();
					} else {
						showMessage("请输入关键字");
					}
				}
				return false;
			}
		});
		cancle_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				final String[] str = getTel(list.get(position).getList());
				if (str.length > 0) {
					final ListRadioDialog l = new ListRadioDialog(HySearchActivity.this);
					l.show(str, new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							l.cancel();
							DeviceUtil.call(HySearchActivity.this, str[position]);
						}
					});
				}

			}
		});
	}
	
	private String[] getTel(List<String> tels) {
		String[] temp = new String[tels.size()];
		for (int i = 0; i < tels.size(); i++) {
			temp[i] = tels.get(i);
		}
		return temp;
	}


	/**
	 * 初始化列表.
	 * @return List<App>
	 */
	public void query() {
		if (DeviceUtil.checkNet2()) {
			errorLayout.setErrorType(ErrorLayout.LOADDATA);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", searchKeywords.getText().toString());
			HttpUtil.getInstance().postJsonObjectRequest("apps/yellowpage/query", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
					list = YellowPageHttpUtil.getPeopleList(result);
					if (list.size() == 0) {
						mListView.setVisibility(View.GONE);
						errorLayout.setErrorType(ErrorLayout.NODATA);
					} else {
						mAdapter = new KsAdapter(HySearchActivity.this, list);
						mListView.setAdapter(mAdapter);
						mListView.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onError(VolleyError error) {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					mListView.setVisibility(View.GONE);

				}
			});


		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
			mListView.setVisibility(View.GONE);
		}
	}

}
