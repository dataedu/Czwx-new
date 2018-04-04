package com.dk.mp.wspj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.wspj.entity.Pjzbx;
import com.dk.mp.wspj.view.ItemView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint("NewApi")
public class EvaluateDetails extends MyActivity implements OnClickListener{
	private LinearLayout scrollview;
	private List<Pjzbx> list;
	private LinearLayout suggentionline;
	private Button upload;
	private ScrollView borderview;
	private Drawable undrawable;
	private Drawable drawable;
	private EditText suggention;
	private Gson gson = new Gson();
	
	private boolean canbeall;

	@Override
	protected int getLayoutID() {
		return R.layout.app_evaluate_details;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("网上评教");
		BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"wspjcanpost"});
		initViews();
		loadDatas();
	}
	
	//初始化视图
	private void initViews(){
		undrawable = getResources().getDrawable(R.drawable.unbuttonsubmit);
		drawable = getResources().getDrawable(R.drawable.buttonsubmit);
		scrollview = (LinearLayout) findViewById(R.id.scrollview);
		suggentionline = (LinearLayout) findViewById(R.id.suggentline);
		upload = (Button) findViewById(R.id.upload);
		suggention = (EditText) findViewById(R.id.suggention);
		suggention.setOnTouchListener(new OnTouchListener() {
			@Override  
			public boolean onTouch(View v, MotionEvent event) {  
				// 解决scrollView中嵌套EditText导致不能上下滑动的问题  
				v.getParent().requestDisallowInterceptTouchEvent(true);  
				switch (event.getAction() & MotionEvent.ACTION_MASK) {  
				   case MotionEvent.ACTION_UP:  
				         v.getParent().requestDisallowInterceptTouchEvent(false);  
				         break;  
				   }  
		    return false;  
		}});
		suggention.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 200){
					suggention.setText(s.toString().substring(0, 200));
					AlertDialog alertDialog = new AlertDialog(EvaluateDetails.this);
					alertDialog.show(null, "字数不能超过200字");
				}
			}
		});
		borderview = (ScrollView) findViewById(R.id.borderview);
		borderview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				if(imm.isActive()){
					imm.hideSoftInputFromWindow(borderview.getWindowToken(), 0);
				}
				return false;
			}
		});
	}
	
	//加载数据
	private void loadDatas(){

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", getIntent().getStringExtra("userId"));
		map.put("pjztid", getIntent().getStringExtra("pjztid"));
		map.put("bprid", getIntent().getStringExtra("teacherId"));
		map.put("pcdm", getIntent().getStringExtra("pcdm"));
		map.put("kcdm", getIntent().getStringExtra("kcdm"));
		map.put("pjztdm", getIntent().getStringExtra("pjztid"));

		HttpUtil.getInstance().postJsonObjectRequest("apps/wspj/getPingJia", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
						if (StringUtils.isNotEmpty(result.getString("data"))){
							list = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<Pjzbx>>(){}.getType());
							canbeall = list.get(0).getSfmf().toUpperCase().equals("TRUE");

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				suggentionline.setVisibility(View.VISIBLE);
				upload.setVisibility(View.VISIBLE);
				loadView();
			}

			@Override
			public void onError(VolleyError error) {

			}
		});
	}
	
	private void loadView(){
		for(Pjzbx p:list){
			ItemView i = new ItemView(this,p,getIntent().getStringExtra("type").equals("0"));
			scrollview.addView(i);
		}
		if(getIntent().getStringExtra("type").equals("1")){
			upload.setVisibility(View.GONE);
			String showtext = list.get(0).getPjyj() == null?"":list.get(0).getPjyj();
			suggention.setText(showtext);
//			suggention.setFocusable(false);
//			suggention.setEnabled(false);
			suggention.setKeyListener(null);
			suggention.setHint("");
		}
	}

	//更改提交按钮样式，判断是否可以提交评分
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi") @Override
		public void onReceive(Context context, Intent intent) {
			boolean canpost = true;
			for(Pjzbx p:list){
				if(p.getEjzbList().size() == 0){//只有大项
					if (p.getPjdf() == null) {
						canpost = false;
						break;
					}
				}else{//存在小项
					for(Pjzbx e:p.getEjzbList()){
						if (e.getPjdf() == null) {
							canpost = false;
							break;
						}
					}
				}
			}
			if(canpost){
				upload.setBackground(drawable);
				upload.setOnClickListener(EvaluateDetails.this);
			}else{
				upload.setBackground(undrawable);
				upload.setOnClickListener(null);
			}
		}
	};
	@Override
	public void onClick(View v) {
		String zbxdfs = "";
		float totaldefen = 0;
		float totalfen = 0;
		for(Pjzbx t:list){
			if(t.getEjzbList() == null || t.getEjzbList().size() == 0){
				zbxdfs += t.getZbdm()+"_"+t.getPjdf()+"@";
				totaldefen += t.getPjdf();
				totalfen += t.getLhfz();
			}else{
				for(Pjzbx f:t.getEjzbList()){
					zbxdfs += f.getZbdm()+"_"+f.getPjdf()+"@";
					totaldefen += f.getPjdf();
					totalfen += f.getLhfz();
				}
			}
		}
		if(Float.floatToIntBits(totaldefen) == Float.floatToIntBits(totalfen) && (!canbeall)){
			showMessage("总分不能为满分");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zbxdfs", zbxdfs);
		map.put("pcdm", getIntent().getStringExtra("pcdm"));
		map.put("kcdm", getIntent().getStringExtra("kcdm"));
		map.put("pjztid", getIntent().getStringExtra("pjztid"));
		map.put("zgh", getIntent().getStringExtra("zgh"));
		map.put("cddw", getIntent().getStringExtra("cddw"));
		map.put("pjyj", suggention.getText().toString());
		HttpUtil.getInstance().postJsonObjectRequest("apps/wspj/save", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
						if(result.getInt("status") == 1){
							showMessage("提交成功");
							finish();
						}else{
							showMessage("提交失败");
						}
                    }
				} catch (JSONException e) {
					e.printStackTrace();
					showMessage("提交失败");
				}
			}

			@Override
			public void onError(VolleyError error) {
				showMessage("提交失败");
			}
		});
	}
	
	@Override
	public void back() {
		if(getIntent().getStringExtra("type").equals("1")){
			super.back();
		}else{
			AlertDialog alertDialog = new AlertDialog(this);
			alertDialog.show(null, "确定退出网上评教？");
		}
	}
}
