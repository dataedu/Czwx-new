package com.dk.mp.ydqj.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.DetailView;
import com.dk.mp.ydqj.R;


/**
 * 请假详情
 * @author admin
 *
 */
public class LeaveDetailActivity extends MyActivity implements OnClickListener{
	DetailView content;
	private TextView agree,noagree;
	private String type;
	private LinearLayout lin_footer;
	private String html_id="";
	public static LeaveDetailActivity instance;

	@Override
	protected int getLayoutID() {
		return R.layout.app_detail;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(getIntent().getStringExtra("title"));
		instance = LeaveDetailActivity.this; 
		type = getIntent().getStringExtra("type");
		content = (DetailView) findViewById(R.id.content);
		agree = (TextView) findViewById(R.id.agree);
		noagree = (TextView) findViewById(R.id.noagree);
		lin_footer = (LinearLayout) findViewById(R.id.lin_footer);
		if("db".equals(type)){
			lin_footer.setVisibility(View.VISIBLE);
		}else{
			lin_footer.setVisibility(View.GONE);
		}
		agree.setOnClickListener(this);
		noagree.setOnClickListener(this);
		mContext = LeaveDetailActivity.this;
		html_id = getIntent().getStringExtra("html_id");
		if(DeviceUtil.checkNet()){
			getData(html_id);
		}else{
//			setNoWorkNet();
		}
	}
	
	public void getData(String id){
		/*showProgressDialog();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		HttpClientUtil.post("apps/qingjia/detail", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String html = HttpUtil.getDetailHtml(responseInfo);
				if(StringUtils.isNotEmpty(html)){
					content.setText(html);
				}else{
					setNoDate(null);
				}
				hideProgressDialog();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				hideProgressDialog();
				setErrorDate(null);
			}
		});*/
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.agree){
			if(DeviceUtil.checkNet()){
				submit();
			}
		}else if(v.getId() == R.id.noagree){
			Intent intent = new Intent(LeaveDetailActivity.this,ApprovalLeaveSubmitActivity.class);
			intent.putExtra("html_id", html_id);
			startActivity(intent);
		}
	}
	
	public void submit(){
		/*showProgressDialog();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", html_id);
		map.put("status", "1");
		HttpClientUtil.post("apps/qingjia/check", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String msg = HttpUtil.check(responseInfo);
				showMessage(msg);
				BroadcastUtil.sendBroadcast(mContext, "com.test.action.refresh");
				finish();
				hideProgressDialog();
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				hideProgressDialog();
				setErrorDate(null);
			}
		});*/
	}
}
