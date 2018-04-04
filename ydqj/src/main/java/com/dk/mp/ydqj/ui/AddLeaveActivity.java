package com.dk.mp.ydqj.ui;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.ydqj.Adapter.ImageUploadAdapter;
import com.dk.mp.ydqj.R;
import com.dk.mp.ydqj.util.ImageDialogAcitivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新增加请假
 * @author admin
 *
 */
public class AddLeaveActivity extends MyActivity implements OnClickListener {
	private LinearLayout stime_lin;//开始时间按钮
	private LinearLayout etime_lin;//结束时间按钮
	private LinearLayout type_lin;//请假类型按钮
	private TextView stime_text;//开始时间
	private TextView etime_text;//结束时间
	private TextView days;//请假天数
	private TextView type_text;//请假类型

	private ImageView addphoto;//添加图片
	private GridView grid;
	private List<String> images = new ArrayList<String>();
	private ImageUploadAdapter imageAdapter;
	private LinearLayout img_lin;//请假图片列表

	private TextView att_text;
	private Button submit;//提交
	private EditText suggesstion;//请假理由

	@Override
	protected int getLayoutID() {
		return R.layout.app_myleave_add;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("新建请假");
		findView();

	}
	
	public void findView(){
		stime_lin = (LinearLayout) findViewById(R.id.stime_lin);
		etime_lin = (LinearLayout) findViewById(R.id.etime_lin);
		stime_text = (TextView) findViewById(R.id.stime_text);
		etime_text = (TextView) findViewById(R.id.etime_text);
		type_text = (TextView) findViewById(R.id.type_text);
		att_text = (TextView) findViewById(R.id.att_text);
		img_lin = (LinearLayout) findViewById(R.id.img_lin);
		type_lin = (LinearLayout)findViewById(R.id.type_lin);
		suggesstion = (EditText) findViewById(R.id.suggesstion);
		days = (TextView) findViewById(R.id.days);
		addphoto = (ImageView) findViewById(R.id.addphoto);
		submit = (Button) findViewById(R.id.submit);

		grid = (GridView) findViewById(R.id.grid);
		grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(AddLeaveActivity.this, ImagePreviewActivity.class);
				intent.putExtra("index", position);
				intent.putStringArrayListExtra("list", (ArrayList<String>) images);
				startActivity(intent);
			}
		});
		stime_lin.setOnClickListener(this);
		etime_lin.setOnClickListener(this);
		type_lin.setOnClickListener(this);
		addphoto.setOnClickListener(this);
		submit.setOnClickListener(this);
		suggesstion.addTextChangedListener(mTextWatcher3);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.stime_lin){//选择开始时间
			Intent in = new Intent(mContext, DatePickActivity.class);
			in.putExtra("rq", stime_text.getText());
			in.putExtra("compRq", etime_text.getText());
			in.putExtra("type", 1);
			startActivityForResult(in, 1);
		}else if(v.getId() == R.id.etime_lin){//选择结束时间
			Intent in = new Intent(mContext, DatePickActivity.class);
			in.putExtra("rq", etime_text.getText());
			in.putExtra("compRq", stime_text.getText());
			in.putExtra("type", 2);
			startActivityForResult(in, 2);
		}else if(v.getId() == R.id.addphoto){//添加图片
			startActivityForResult(new Intent(mContext, ImageDialogAcitivity.class), 3);
		}else if(v.getId() == R.id.type_lin){//选择请假类型
			Intent in = new Intent(mContext, LeaveTypeActivity.class);
			startActivityForResult(in, 4);
		}else if(v.getId() == R.id.submit){//提交请假
			if(stime_text.getText().length()>0 && etime_text.getText().length()>0 && type_text.getText().length()>0 && suggesstion.getText().length()>0){
				if(DeviceUtil.checkNet()){
					if(suggesstion.getText().length()>50){
						DialogAlert dialog = new DialogAlert(mContext);
						dialog.show("确定", "请假字数不能超过50字");
					}else{
//						showProgressDialog();
						if(images.size()>0){
							uploadImage();
						}else{
							submit("");
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				String date = data.getStringExtra("date");
				if (date != null) {
					stime_text.setText(date);
					try {
						if(StringUtils.isNotEmpty(etime_text.getText().toString())){
							days.setText(Integer.parseInt(TimeUtils.daysBetween(stime_text.getText().toString(), etime_text.getText().toString()))+1+"");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				dealButtonColor();
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				String date2 = data.getStringExtra("date");
				if (date2 != null) {
					etime_text.setText(date2);
					try {
						if(StringUtils.isNotEmpty(stime_text.getText().toString())){
							days.setText(Integer.parseInt(TimeUtils.daysBetween(stime_text.getText().toString(), etime_text.getText().toString()))+1+"");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				dealButtonColor();
			}
			break;
		case 3:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				com.dk.mp.core.util.Logger.info("==path==" + path);
				if (StringUtils.isNotEmpty(path)&&new File(path).exists()&&new File(path).isFile()&&new File(path).length()>0) {
					images.add(0, path);
					setImageList();
				}
			}
			break;
		case 4:
			if (resultCode == RESULT_OK) {
				String qjlx = data.getStringExtra("qjlx");
				if (qjlx != null) {
					type_text.setText(qjlx);
				}
			}
			dealButtonColor();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog(mContext).show("", "是否放弃编辑？", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void back() {
		new AlertDialog(mContext).show("", "是否放弃编辑？", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
	}
	
	private void setImageList() {
		if(images.size()>0){
			img_lin.setVisibility(View.VISIBLE);
			att_text.setVisibility(View.INVISIBLE);
		}
		if (images.size() >= 4) {
			addphoto.setVisibility(View.INVISIBLE);
		}
		if (imageAdapter == null) {
			imageAdapter = new ImageUploadAdapter(AddLeaveActivity.this,mContext, images);
			grid.setAdapter(imageAdapter);
		} else {
			imageAdapter.setData(images);
			imageAdapter.notifyDataSetChanged();
		}

	}
	
	public void dealImage(){
		img_lin.setVisibility(View.GONE);
		att_text.setVisibility(View.VISIBLE);
	}
	
	public void showAddButton(){
		addphoto.setVisibility(View.VISIBLE);
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
			dealButtonColor();
			if(suggesstion.getText().length()>50){
				DialogAlert dialog = new DialogAlert(mContext);
				dialog.show("确定", "请假字数不能超过50字");
				suggesstion.setText(suggesstion.getText().toString().substring(0, 50));
			}
		}
	};
	
	/**
	 * 处理提交按钮颜色
	 */
	public void dealButtonColor(){
		if(stime_text.getText().length()>0 && etime_text.getText().length()>0 && type_text.getText().length()>0 && suggesstion.getText().length()>0){
			submit.setBackgroundColor(getResources().getColor(R.color.submit));
		}else{
			submit.setBackgroundColor(getResources().getColor(R.color.nosubmit));
		}
	}
	
	public void submit(String imgs){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kssj", stime_text.getText().toString());
		map.put("jssj", etime_text.getText().toString());
		map.put("qjly", suggesstion.getText().toString());
		map.put("qjts", days.getText().toString());
		map.put("zp", imgs);
		map.put("qjlx", type_text.getText().toString());

		HttpUtil.getInstance().postJsonObjectRequest("apps/qingjia/save", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
                        String msg = result.getString("msg");
						showMessage(msg);
						BroadcastUtil.sendBroadcast(mContext, "com.test.action.refresh");
						finish();
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(VolleyError error) {
				showMessage("操作失败");
			}
		});
	}
	
	public void uploadImage(){
		/*HttpClientUtil.upload("fileUpload", images, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String imgs = HttpUtil.imgs(responseInfo);
				submit(imgs);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				hideProgressDialog();
				showMessage("上传附件失败");
			}
		});*/
	}

}
