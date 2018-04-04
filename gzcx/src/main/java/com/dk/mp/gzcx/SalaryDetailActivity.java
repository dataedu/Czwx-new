package com.dk.mp.gzcx;

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
 * 工资详情
 * @author dake
 *
 */
public class SalaryDetailActivity extends MyActivity {
	private String month="";
	private DetailView content;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.core_detail;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("工资查询");
		month = getIntent().getStringExtra("month");
		content = (DetailView) findViewById(R.id.content);
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		getDetail();
	}

	public void getDetail() {
		if (DeviceUtil.checkNet()) {
			errorLayout.setErrorType(ErrorLayout.LOADDATA);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("month", month);
			HttpUtil.getInstance().postJsonObjectRequest("apps/gzcx/list", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") != 200) {
							errorLayout.setErrorType(ErrorLayout.DATAFAIL);
						} else {
							String str = result.getString("data");
							content.setText(StringUtils.checkEmpty(str));
//							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
