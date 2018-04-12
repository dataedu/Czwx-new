package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.entity.GzbxRole;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 故障报修首页
 * @author admin
 *
 */
@SuppressLint("HandlerLeak") public class FaultRepairMainActivity extends MyActivity implements OnClickListener{
	private final String faultrepair_key = "FAULTREPAIRROLE";
	private Context mContext;
	private CoreSharedPreferencesHelper helper;
	private LinearLayout bxsq,bxsq2;//报修申请
	private LinearLayout bxsh;//报修审核
	private LinearLayout bxpj,bxpj2;//报修评价
	GzbxRole role;
	private Button back;

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_main;
	}

	@Override
	protected void initialize() {
		super.initialize();
		mContext  = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		initView();
		if(helper.getLoginMsg() != null && StringUtils.isNotEmpty(helper.getValue(helper.getLoginMsg().getUid()+faultrepair_key))){//存在角色
			role = new GzbxRole();
			String value = helper.getValue(helper.getLoginMsg().getUid()+faultrepair_key);
			role.setBxsh(Boolean.parseBoolean(value));
			mHandler.sendEmptyMessage(1);
		}else{//不存在就重新获取
			if(DeviceUtil.checkNet2()){//判断网络
				getRole();
			}else{
				setNoWorkNet();
			}
		}
	}
	
	/**
	 * 初始化view
	 */
	public void initView(){
		mContext  = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		bxsq = (LinearLayout) findViewById(R.id.evaluate);
		bxsh = (LinearLayout) findViewById(R.id.appr);
		bxpj = (LinearLayout) findViewById(R.id.auditing);
		bxsq2 = (LinearLayout) findViewById(R.id.evaluate2);
		bxpj2 = (LinearLayout) findViewById(R.id.auditing2);
		bxsq.setOnClickListener(this);
		bxsh.setOnClickListener(this);
		bxpj.setOnClickListener(this);
		bxsq2.setOnClickListener(this);
		bxpj2.setOnClickListener(this);
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		if(helper.getLoginMsg() != null && StringUtils.isNotEmpty(helper.getValue(helper.getLoginMsg().getUid()+faultrepair_key))){//存在角色
			role = new GzbxRole();
			String value = helper.getValue(helper.getLoginMsg().getUid()+faultrepair_key);
			role.setBxsh(Boolean.parseBoolean(value));
			mHandler.sendEmptyMessage(1);
		}else{//不存在就重新获取
			if(DeviceUtil.checkNet2()){//判断网络
				getRole();
			}else{
				setNoWorkNet();
			}
		}
	}
	
	/**
	 * 获取用户角色
	 */
	private void getRole(){
		showProgressDialog();
		Map<String,Object> map = new HashMap<String, Object>();




		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/role", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				role = HttpUtil.getRole(result);
				if(role == null){
					mHandler.sendEmptyMessage(0);
				}else{
					mHandler.sendEmptyMessage(1);
				}
			}

			@Override
			public void onError(VolleyError error) {
				hideProgressDialog();
				setErrorDate(null);
				setContentView(R.layout.core_loadview);
			}
		});



	}
	

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if(v.getId() == R.id.evaluate){//报修申请
			intent.setClass(mContext, com.dk.mp.apps.gzbxnew.FaultRepairApplyActivity.class);
		}else if(v.getId() == R.id.auditing){
			intent.setClass(mContext, com.dk.mp.apps.gzbxnew.FaultRepairCommentOnTabActivity.class);//报修评价
		}else if(v.getId() == R.id.appr){
			intent.setClass(mContext, com.dk.mp.apps.gzbxnew.FaultRepairAuditingTabActivity.class);//报修审核
		}else if(v.getId() == R.id.evaluate2){//报修申请
			intent.setClass(mContext, com.dk.mp.apps.gzbxnew.FaultRepairApplyActivity.class);
		}else if(v.getId() == R.id.auditing2){
			intent.setClass(mContext, com.dk.mp.apps.gzbxnew.FaultRepairCommentOnTabActivity.class);//报修评价
		}
		startActivity(intent);
	}
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			switch (msg.what) {
			case 0://没获取到数据
				setNoDate(null);
				break;
			case 1:
				if(helper.getLoginMsg()!= null){
					String key = helper.getLoginMsg().getUid()+faultrepair_key;
					helper.setValue(key, role.isBxsh()+"");
				}
				if(role.isBxsh()){
					bxsq.setVisibility(View.VISIBLE);
					bxsh.setVisibility(View.VISIBLE);
					bxpj.setVisibility(View.VISIBLE);
					bxsq2.setVisibility(View.GONE);
					bxpj2.setVisibility(View.GONE);
				}else{
					bxsq.setVisibility(View.GONE);
					bxsh.setVisibility(View.GONE);
					bxpj.setVisibility(View.GONE);
					bxsq2.setVisibility(View.VISIBLE);
					bxpj2.setVisibility(View.VISIBLE);
				}
//				if(role.isXctb()){
//					
//				}
				break;
			default:
				break;
			}
		};
	};
	
}
