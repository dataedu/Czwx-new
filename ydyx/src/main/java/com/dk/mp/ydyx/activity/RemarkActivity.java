package com.dk.mp.ydyx.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.ydyx.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 迎新须知.
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
		return R.layout.app_remark;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(R.string.app_welcome_know);
		id = getIntent().getStringExtra("id");
		titles=(TextView) findViewById(R.id.title);
		titles.setTextSize(17);
		findView();
		getData();
	}
	private void findView() {
		remark=(DetailView) findViewById(R.id.remark);
	}
	public void getData(){
		if (DeviceUtil.checkNet()) {
//			showProgressDialog();
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("idProcess", id);
			HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/notice", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
                            content = result.getString("data");
							handler.sendEmptyMessage(1);
                        }
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(VolleyError error) {
					showMessage(getString(R.string.data_fail));
				}
			});
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//					hideProgressDialog();
					switch (msg.what) {
					case 1:
						remark.setText(content);
						break;
					default:
						break;
					}
				};
			};
}
