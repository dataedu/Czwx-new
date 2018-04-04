package com.dk.mp.ykt;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.core.widget.ErrorLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * @version 2013-3-21
 * @author wangw
 */
public class CardMonthActivity extends MyActivity {
	private DetailView content;
	private String month = "";
	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.core_detail;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(getIntent().getStringExtra("title"));

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		content = (DetailView) findViewById(R.id.content);
		month = getIntent().getStringExtra("month");
		getDetail();
	}

	public void getDetail() {
		if (DeviceUtil.checkNet()) {
			errorLayout.setErrorType(ErrorLayout.LOADDATA);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("month", month.replace("年", "-").replace("月", ""));
			map.put("cardNo", getIntent().getStringExtra("cardNo"));
			HttpUtil.getInstance().postJsonObjectRequest("apps/ykt/list", map, new HttpListener<JSONObject>() {

				@Override
				public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") != 200) {
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					} else {
						String str = result.getString("data");
						content.setText(StringUtils.checkEmpty(str));
//						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
					errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			}

				@Override
				public void onError(VolleyError error) {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			});
		}else {
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

}
