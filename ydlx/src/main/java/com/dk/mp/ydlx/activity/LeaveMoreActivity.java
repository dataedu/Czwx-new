package com.dk.mp.ydlx.activity;

import java.util.List;

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
import com.dk.mp.ydlx.R;
import com.dk.mp.ydlx.adapter.MoreAdapter;
import com.dk.mp.ydlx.entity.LeaveMore;
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
public class LeaveMoreActivity extends MyActivity {
	private TextView remind, titles;
	private ListView listview;
	private MoreAdapter adapter;
	private List<LeaveMore> list;
	private LeaveMore leave;
	private String id, name, tip;
	private LinearLayout lr;

	@Override
	protected int getLayoutID() {
		return R.layout.app_leave_more;
	}

	@Override
	protected void initialize() {
		super.initialize();

		findView();
		id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		tip = getIntent().getStringExtra("tip");
		titles = (TextView) findViewById(R.id.title);
		titles.setTextSize(17);
		if (StringUtils.isNotEmpty(tip)) {
			remind.setText(tip);
		} else {
			lr.setVisibility(View.GONE);
		}
		setTitle(name);
		getDate(id);
	}

	private void findView() {
		remind = (TextView) findViewById(R.id.remind);
		listview = (ListView) findViewById(R.id.listmore);
		lr = (LinearLayout) findViewById(R.id.lr);
	}

	public void getDate(final String idProcess) {
		if (DeviceUtil.checkNet()) {

			HttpUtil.getInstance().postJsonObjectRequest("apps/lx/getSubProcess?idProcess=" + idProcess, null, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
                            list = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<LeaveMore>>(){}.getType());
							if (list.size() > 0) {
								if (adapter == null) {
									adapter = new MoreAdapter(LeaveMoreActivity.this, list);
									listview.setAdapter(adapter);
								} else {
									listview.setAdapter(adapter);
								}
							}
                        }
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(VolleyError error) {

				}
			});
		}
	}


}
