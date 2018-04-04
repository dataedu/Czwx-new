package com.dk.mp.xxgk;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xxgk.db.IntroDBHelper;
import com.dk.mp.xxgk.entity.History;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 学院简介.
 * 
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
@SuppressLint("HandlerLeak")
public class SchIntroHistoryActivity extends BaseFragment {

	private DetailView t;

	private ErrorLayout errorLayout;

	CoreSharedPreferencesHelper helper;

	@Override
	protected int getLayoutId() {
		return R.layout.app_intro_desc;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		helper = new CoreSharedPreferencesHelper(mContext);

		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);

		t = (DetailView) view.findViewById(R.id.content);
		if(DeviceUtil.checkNet()){//判断是否有网络，没网络到本地数据库获取
			init();
		}else{
			if (StringUtils.isNotEmpty(helper.getValue("xsyg"))){
				t.setText(helper.getValue("xsyg"));
				t.setVisibility(View.VISIBLE);
				MsgDialog.show(mContext, mContext.getString(R.string.net_no2));
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			}else {
				errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
			}
		}
	}

	private void init() {

		HttpUtil.getInstance().postJsonObjectRequest("apps/introduRest/history", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
                        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						if (StringUtils.isNotEmpty(result.getString("data"))){
							t.setText(result.getString("data").toString());
							t.setVisibility(View.VISIBLE);
							helper.setValue("xsyg",result.getString("data").toString());
						}else {
							errorLayout.setErrorType(ErrorLayout.NODATA);
						}
                    }
				} catch (JSONException e) {
					e.printStackTrace();
					if (StringUtils.isNotEmpty(helper.getValue("xsyg"))){
						t.setText(helper.getValue("xsyg"));
						t.setVisibility(View.VISIBLE);
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
					}else {
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					}
				}
			}

			@Override
			public void onError(VolleyError error) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				if (StringUtils.isNotEmpty(helper.getValue("xsyg"))){
					t.setText(helper.getValue("xsyg"));
					t.setVisibility(View.VISIBLE);
					errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				}else {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			}
		});
	}

}
