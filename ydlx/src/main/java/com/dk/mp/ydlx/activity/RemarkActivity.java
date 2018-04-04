package com.dk.mp.ydlx.activity;

import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.ydlx.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 离校须知.
 * @since 
 * @version2015-3-25
 * @author rm.zhao
 */
public class RemarkActivity extends MyActivity {
	private DetailView remark;
	private String content;
	private String id;
	private TextView titles;

	@Override
	protected int getLayoutID() {
		return R.layout.app_leave_remark;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(R.string.app_leave_know);
		id = getIntent().getStringExtra("id");
		titles = (TextView) findViewById(R.id.title);
		titles.setTextSize(17);
		findView();
		getData();
	}

	private void findView() {
		remark = (DetailView) findViewById(R.id.remark);
	}

	public void getData() {
		if (DeviceUtil.checkNet()) {

			HttpUtil.getInstance().postJsonObjectRequest("apps/lx/notice?idProcess=" + id, null, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
                            content = result.getString("data").toString();
							remark.setText(content);
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
