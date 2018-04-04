package com.dk.mp.xyrl;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.core.widget.ErrorLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class XyrlBzActivity extends MyActivity {
	
	DetailView content;
	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.xyrl_bz;
	}

	@Override
	protected void initialize() {
		super.initialize();

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		content = (DetailView)findViewById(R.id.content);
		setTitle("备注");

		if (DeviceUtil.checkNet()){
			initData();
		}else {
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}

	}

	private void initData() {
		HttpUtil.getInstance().postJsonObjectRequest("apps/xyrl/bz", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						String str = result.getString("data");
						if (StringUtils.isNotEmpty(str)){
							content.setText(StringUtils.checkEmpty(str));
						}else {
							errorLayout.setErrorType(ErrorLayout.NODATA);
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

}
