package com.dk.mp.apps.hy;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dk.mp.apps.hy.adapter.KsAdapter;
import com.dk.mp.apps.hy.entity.Ks;
import com.dk.mp.apps.hy.http.YellowPageHttpUtil;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HySearchActivity extends MyActivity {
	private KsAdapter mAdapter;
	private ListView mListView;
	private EditText searchKeywords;
	private List<Ks> list;
	private TextView cancle_search;

	@Override
	protected int getLayoutID() {
		return R.layout.app_yellowpage_query;
	}

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.app_yellowpage_query);
//		initView();
//		setTitle("搜索");
//		if (!DeviceUtil.checkNet2()) {
//			setNoWorkNet();
//			mListView.setVisibility(View.GONE);
//		}else{
//			mListView.setVisibility(View.VISIBLE);
//		}
//	}

	/**
	 * 初始化控件.
	 */
	protected void initView() {
		setTitle("搜索");
		if (!DeviceUtil.checkNet2()) {
			setNoWorkNet();
			mListView.setVisibility(View.GONE);
		}else{
			mListView.setVisibility(View.VISIBLE);
		}

		mListView = (ListView) findViewById(R.id.listView);
		cancle_search = (TextView) findViewById(R.id.cancle_search);
		searchKeywords = (EditText) findViewById(R.id.search_Keywords);
		searchKeywords.setHint("科室、电话");
		searchKeywords.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((actionId == 0 || actionId == 3) && event != null) {
					final String keywords = searchKeywords.getText().toString();
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
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", searchKeywords.getText().toString());
			HttpClientUtil.post("apps/yellowpage/query", map, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					hideProgressDialog();
					list = YellowPageHttpUtil.getPeopleList(responseInfo);
					if (list.size() == 0) {
						mListView.setVisibility(View.GONE);
						setNoDate("搜索无结果");
					} else {
						mAdapter = new KsAdapter(HySearchActivity.this, list);
						mListView.setAdapter(mAdapter);
						mListView.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					hideProgressDialog();
					setErrorDate(null);
					mListView.setVisibility(View.GONE);
				}
			});
		}else{
			setNoWorkNet();
			mListView.setVisibility(View.GONE);
		}
	}

}
