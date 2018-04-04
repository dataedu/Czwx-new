package com.dk.mp.ydqj.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.ydqj.R;

/**
 * 请假审批
 * @author admin
 *
 */
public class ApprovalLeaveSubmitActivity extends MyActivity {
	private EditText spyj_edit;
	private Button submit;
	private String html_id="";

	@Override
	protected int getLayoutID() {
		return R.layout.app_approval_sub;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("不同意");
		html_id = getIntent().getStringExtra("html_id");
		findView();
	}
	
	public void findView(){
		spyj_edit = (EditText) findViewById(R.id.spyj_edit);
		submit = (Button) findViewById(R.id.submit);
		spyj_edit.addTextChangedListener(mTextWatcher3);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(spyj_edit.getText().length()<=0){
					showMessage("请填写审批意见");
				}else if(spyj_edit.getText().length()>200){
					showMessage("审批意见不能大于200字");
				}else{
					if(spyj_edit.getText().toString().trim().length() >0){
						if (DeviceUtil.checkNet()) {
							submit();
						}
					}
				}
			}
		});
	}
	public void submit(){
		/*showProgressDialog();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", html_id);
		map.put("status", "0");
		map.put("bytly", spyj_edit.getText().toString());
		HttpClientUtil.post("apps/qingjia/check", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String msg = HttpUtil.check(responseInfo);
				showMessage(msg);
				BroadcastUtil.sendBroadcast(mContext, "com.test.action.refresh");
				LeaveDetailActivity.instance.finish();
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
	
	TextWatcher mTextWatcher3 = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(spyj_edit.getText().toString().trim().length() >0){
				submit.setBackgroundColor(getResources().getColor(R.color.submit));
			}else{
				submit.setBackgroundColor(getResources().getColor(R.color.nosubmit));
			}
		}
	};
}
