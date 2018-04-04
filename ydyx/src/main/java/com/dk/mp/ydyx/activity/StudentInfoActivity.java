package com.dk.mp.ydyx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.AlertDialog;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.User;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.ydyx.R;
import com.dk.mp.ydyx.entity.Arrival;
import com.dk.mp.ydyx.entity.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 信息采集.
 * 
 * @author rm.zhao
 * 
 *         2015-5-25
 */
public class StudentInfoActivity extends MyActivity implements OnClickListener {
	private ImageView iheader;// 头像采集
	private ImageView camer;// 调用系统相册
	private TextView fangshi;// 到达方式
	private TextView dtime;// 到达时间
	private EditText banci;// 到达方式
	private TextView dear;// 到达地点
	private EditText renshu;// 随行人数
	private TextView names;//姓名
	private UserInfo info;
	private LinearLayout  cdidian;
	private Context mContext;
	private List<Arrival> dd = new ArrayList<Arrival>();
	private String[] dds;
	private String idUser;
	private User user;
	private boolean isSuccess = true;
	private CoreSharedPreferencesHelper h;
	private String dddddm;
	private LinearLayout dtime_lin;
	private Button submit;
	private CheckBox checkbox_settting;

	@Override
	protected int getLayoutID() {
		return R.layout.app_wel_stuinfo;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("学生信息采集");
		idUser = getIntent().getStringExtra("idUser");
		mContext = StudentInfoActivity.this;
		h = new CoreSharedPreferencesHelper(this);
		user = h.getUser();
		handler.sendEmptyMessage(1);
		finView();
		getUser();
	}

	private void finView() {
		iheader = (ImageView) findViewById(R.id.iheader);
		camer = (ImageView) findViewById(R.id.camer);
		fangshi = (TextView) findViewById(R.id.fangshi);
		dtime = (TextView) findViewById(R.id.dtime);
		banci = (EditText) findViewById(R.id.banci);
		dear = (TextView) findViewById(R.id.dear);
		names = (TextView) findViewById(R.id.names);
		renshu = (EditText) findViewById(R.id.renshu);
		cdidian = (LinearLayout) findViewById(R.id.cdidian);
		dtime_lin = (LinearLayout) findViewById(R.id.dtime_lin);
		checkbox_settting = (CheckBox) findViewById(R.id.checkbox_settting);
		submit = (Button)findViewById(R.id.submit);
		camer.setOnClickListener(this);
		cdidian.setOnClickListener(this);
		dtime.setOnClickListener(this);
		dtime_lin.setOnClickListener(this);
		submit.setOnClickListener(this);
		banci.addTextChangedListener(mTextWatcher3);
		renshu.addTextChangedListener(mTextWatcher3);
	}

	@Override
	public void onClick(View v) {
		 if (v == dtime_lin) {
			Intent in = new Intent(StudentInfoActivity.this,DatePickActivity.class);
			in.putExtra("rq", dtime.getText());
			startActivityForResult(in, 6);
		}else if(v == dtime){
			Intent in = new Intent(StudentInfoActivity.this,DatePickActivity.class);
			in.putExtra("rq", dtime.getText());
			startActivityForResult(in, 6);
		}else if (v == cdidian) {
			dds = new String[dd.size()];
			if (DeviceUtil.checkNet()) {
				for (int i = 0; i < dd.size(); i++) {
					dds[i] = dd.get(i).getName();
				}
				final NewListRadioDialog mp = new NewListRadioDialog(StudentInfoActivity.this);
				mp.show(dds, new OnClickListener() {
					@Override
					public void onClick(View v) {
						mp.cancel();
						if(dd.size()>0){
							dddddm = dd.get(mp.selected()).getId();
							dear.setText(dds[mp.selected()]);
						}
						dealButtonColor();
					}
				});
			}else{
				MsgDialog.show(StudentInfoActivity.this, "获取失败");
			}
		} else if (v == camer) {
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/*");
			startActivityForResult(getImage, 5);
		} else if (v == submit) {
			if (DeviceUtil.checkNet()) {
				if (!StringUtils.isNotEmpty(dtime.getText().toString())) {
					showMessage(" 请填写预计到达时间");
				} else if (!StringUtils.isNotEmpty(banci.getText().toString())) {
					showMessage(" 请填写车次班次");
				} else if (!StringUtils.isNotEmpty(dear.getText().toString())) {
					showMessage(" 请填写预计到达地点");
				} else if (!StringUtils.isNotEmpty(renshu.getText().toString())) {
					showMessage(" 请填写随行人数");
				} else {
					if (DeviceUtil.checkNet()) {
//						showProgressDialog();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("dddd", dddddm);
						map.put("ccbc", banci.getText().toString());
						map.put("sxrs", renshu.getText().toString());
						map.put("ddsj", dtime.getText().toString());
						if (checkbox_settting.isChecked()) {
							map.put("sfzbc", "true");
						} else{
							map.put("sfzbc", "false");
						}

						map.put("xszj", idUser);

						HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/setStuMsg", map, new HttpListener<JSONObject>() {
							@Override
							public void onSuccess(JSONObject result) {
								try {
									if (result.getInt("code") == 200){
                                        isSuccess = result.getBoolean("data");
										handler.sendEmptyMessage(2);
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
			}
		}
	}

	private void getUser() {
		if (DeviceUtil.checkNet()) {
			HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/getDddd", null, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
							dd = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<Arrival>>(){}.getType());
                        }
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(VolleyError error) {

				}
			});
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("xszj", idUser);
			HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/getStuMsg", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
							info = new Gson().fromJson(result.getJSONObject("data").toString(),UserInfo.class);
							if (info != null) {
								handler.sendEmptyMessage(0);
							}

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

	@SuppressLint("HandlerLeak") private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			hideProgressDialog();
			switch (msg.what) {
			case 0:
				if (info != null) {
					ImageUtil.setImageView(StudentInfoActivity.this, iheader, info.getPhotoUser() + "&w=" + 80,
							R.mipmap.image_defualt, R.mipmap.image_defualt);
					if ("1".equals(info.getWay())) {
						checkbox_settting.setChecked(false);
					} else {
						checkbox_settting.setChecked(true);
					}
					dtime.setText(StringUtils.checkEmpty(info.getTimeArrival()));
					banci.setText(StringUtils.checkEmpty(info.getBanci()));
					dear.setText(StringUtils.checkEmpty(info.getAddress()));
					renshu.setText(StringUtils.checkEmpty(info.getRenshu()));
				}

				break;
			case 1:
				if (user != null) {
					names.setText(StringUtils.checkEmpty(user.getUserName()));
				}
				break;
			case 2:
				if (isSuccess) {
					showMessage("操作成功");
				} else {
					showMessage("操作失败");
				}
				break;
			default:
				break;
			}
		};
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			iheader.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}else if(requestCode == 6 && resultCode == RESULT_OK && null != data ){
			String date = data.getStringExtra("date");
			if (date != null) {
				dtime.setText(date);
			}
			dealButtonColor();
		}
	}
	
	/**
	 * 处理提交按钮颜色
	 */
	public void dealButtonColor(){
		if(dtime.getText().length()>0 && banci.getText().length()>0 && dear.getText().length()>0 && renshu.getText().length()>0){
			submit.setBackgroundColor(getResources().getColor(R.color.submit));
		}else{
			submit.setBackgroundColor(getResources().getColor(R.color.nosubmit));
		}
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
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog(mContext).show("确定退出学生信息采集？", "", new DialogInterface.OnClickListener(){
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
		new AlertDialog(mContext).show("确定退出学生信息采集？", "", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
	}
}
