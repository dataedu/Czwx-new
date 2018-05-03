package com.dk.mp.apps.gzbxnew;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.R;
import com.dk.mp.apps.gzbxnew.entity.Bxlx;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.Result;
import com.dk.mp.apps.gzbxnew.entity.ResultCode;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.apps.gzbxnew.widget.TagLayout;

import com.dk.mp.core.entity.LoginMsg;

import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;

import com.dk.mp.core.util.encrypt.Base64Utils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 添加故障报修
 * @author admin
 *
 */
@SuppressLint("HandlerLeak")
public class FaultRepairAddActivity extends MyActivity {
	CoreSharedPreferencesHelper helper;
	private TextView bxr,bxsj,bxbm,bxdh,bxbm_txt;
	private Context mContext;
	TagLayout mFlowLayout;
	List<Bxlx> bxlxs;
	private ScrollView sv_parent,sv_child;
	private TextView num,num_bxnr;
	private EditText bxdd,bxnr,lxdh;
	//	 private Map<String,String> map = new HashMap<String,String>();//存放报修类型信息
	private Button ok;
	private String dh="";
	private String bxlxid = "";//报修类型id
	private Map<String,CheckedTextView> map = new HashMap<String,CheckedTextView>();
	private Gzbx gzbx;
	public static final String BASEPICPATH = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";
	//    private ListView gRecyclerView;//图片
	List<String> imgs = new ArrayList<String>();//保存图片地址
	//    FaultRepairImageAdapter wImageAdapter;
	private String noCutFilePath ="";
	private LinearLayout addImg;
	private FrameLayout showImg;
	private ImageView mDelete,mShow;
	private BitmapUtils utils;
	private String tpid="";

	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
	};

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_add;
	}



	protected void initView(){

		setTitle("故障报修");
		mContext = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		mFlowLayout = (TagLayout) findViewById(R.id.tags);
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbx");

		utils = new BitmapUtils(mContext);
		addImg = (LinearLayout) findViewById(R.id.addimg);
		showImg = (FrameLayout)findViewById(R.id.showimg);
		mDelete = (ImageView) findViewById(R.id.delete_img);
		mShow = (ImageView) findViewById(R.id.show_img);

		lxdh = (EditText) findViewById(R.id.lxdh);
		bxr = (TextView) findViewById(R.id.bxr);
		bxsj = (TextView) findViewById(R.id.bxsj);
		bxbm = (TextView) findViewById(R.id.bxbm);
		bxdh = (TextView) findViewById(R.id.bxdh);
		bxbm_txt = (TextView) findViewById(R.id.bxbm_txt);
		num = (TextView) findViewById(R.id.num);
		num_bxnr = (TextView) findViewById(R.id.num_bxnr);
		bxdd = (EditText)findViewById(R.id.bxdd);
		bxnr = (EditText)findViewById(R.id.bxnr);
		bxdd.addTextChangedListener(mWatcher);
		bxnr.addTextChangedListener(mWatcher2);
		lxdh.addTextChangedListener(mWatcher3);
		ok = (Button) findViewById(R.id.ok);
		if(helper.getUser()!=null){
			bxr.setText(helper.getUser().getUserName());
			if("teacher".equals(helper.getUser().getRoles())){
				bxbm_txt.setText("报修部门：");
			}else{
				bxbm_txt.setText("报修院系：");
			}
			if(StringUtils.isNotEmpty(helper.getUser().getDepartName())){
				bxbm.setText(helper.getUser().getDepartName());
			}
		}
		if(gzbx == null){
			bxsj.setText(TimeUtils.getNowTime());
			dh = createBxdh();
			bxdh.setText(dh);
		}else{
			bxsj.setText(TimeUtils.getNowTime());
			bxdh.setText(gzbx.getBxdh());
			bxdd.setText(gzbx.getBxdd());
			bxnr.setText(gzbx.getBxnr());
			bxbm.setText(gzbx.getBxbm());
			lxdh.setText(gzbx.getBxrlxdh());
			dh = gzbx.getBxdh();

			if(StringUtils.isNotEmpty(gzbx.getFj())){
				tpid = dh;
				utils.display(mShow,gzbx.getFj());
				addImg.setVisibility(View.GONE);
				showImg.setVisibility(View.VISIBLE);
				imgs.clear();
				imgs.add(gzbx.getFj());
			}else{
				addImg.setVisibility(View.VISIBLE);
				showImg.setVisibility(View.GONE);
			}
		}

		sv_parent = (ScrollView) findViewById(R.id.sv_parent);
		sv_child = (ScrollView) findViewById(R.id.sv_child);
		sv_parent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewParent parent = findViewById(R.id.sv_child).getParent();
				parent.requestDisallowInterceptTouchEvent(false);  //允许父类截断
				return false;
			}
		});
		//子ScrollView
		sv_child = (ScrollView) findViewById(R.id.sv_child);
		sv_child.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewParent parent1 = v.getParent();
				parent1.requestDisallowInterceptTouchEvent(true); //不允许父类截断
				return false;
			}
		});

		bxdd.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				//触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
				if ((view.getId() == R.id.bxdd && canVerticalScroll(bxdd))) {
					view.getParent().requestDisallowInterceptTouchEvent(true);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						view.getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				return false;
			}
		});

		bxnr.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				//触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
				if ((view.getId() == R.id.bxnr && canVerticalScroll(bxnr))) {
					view.getParent().requestDisallowInterceptTouchEvent(true);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						view.getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				return false;
			}
		});

		if(DeviceUtil.checkNet2()){
			getBxlx();
		}else{
			setNoWorkNet();
		}
		ok.setEnabled(false);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(DeviceUtil.checkNet()){
					ok.setEnabled(false);
					submitWs();
				}
			}
		});

		imgs.add("addImage");
//        gRecyclerView = (ListView) findViewById(R.id.imgView);
//        wImageAdapter = new FaultRepairImageAdapter(mContext,FaultRepairAddActivity.this,imgs);
//        gRecyclerView.setAdapter(wImageAdapter);

		addImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ablum();
			}
		});
		mDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearImg();
				showImg.setVisibility(View.GONE);
				addImg.setVisibility(View.VISIBLE);
			}
		});
		mShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImagePreviewActivity.class);
				intent.putExtra("index", 0);
				intent.putStringArrayListExtra("list", (ArrayList<String>) imgs);
				mContext.startActivity(intent);
			}
		});


			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					this,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);

	}

	/**
	 * 提交宿舍卫生检查情况

	 */
	public void submitWs(){
		if (DeviceUtil.checkNet()){
			if(StringUtils.isNotEmpty(noCutFilePath)){
				File f = new File(noCutFilePath);
				if(f.exists() && f.isFile()){
					updateImg();
				}else{
					submit(tpid);
				}
			}else{
				submit(tpid);
			}
		}else {
			showMessage(getReString(R.string.net_no2));
			mHandler.sendEmptyMessage(-1);
		}

	}


	/**
	 * 创建报修单号
	 * @return
	 */
	private String createBxdh(){
		Date now = new Date();
		String hehe = "bx"+new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		return hehe;
	}

	/**
	 * 获取报修类型数据
	 */
	private void getBxlx(){
		showProgressDialog();
		Map<String,String> map = new HashMap<String, String>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}



		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/getTypeList", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				hideProgressDialog();
				bxlxs = HttpUtil.getBxlxs(result);
				if(bxlxs.size()>0)
					mHandler.sendEmptyMessage(1);
				else
					mHandler.sendEmptyMessage(0);
			}

			@Override
			public void onError(VolleyError error) {
				hideProgressDialog();
				setErrorDate(null);

			}
		});


	}


	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					setNoDate(null);
					break;
				case 1:
					for (int i = 0; i < bxlxs.size(); i++) {
						final CheckedTextView tv = new CheckedTextView(mContext);
						tv.setText(bxlxs.get(i).getName());
						tv.setTextColor(Color.BLACK);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(DeviceUtil.dip2px(mContext, 10), DeviceUtil.dip2px(mContext, 10), 0, 0);
						tv.setLayoutParams(params);
						tv.setMaxWidth(DeviceUtil.getScreenWidth(mContext) - DeviceUtil.dip2px(mContext, 15));//这句话是为了限制过长的内容顶出屏幕而设置的
						tv.setHeight(DeviceUtil.dip2px(mContext, 40));
						tv.setEllipsize(TruncateAt.END);
						tv.setGravity(Gravity.CENTER);
						tv.setTextSize(14);
						tv.setMaxLines(1);
						final String bxid = bxlxs.get(i).getId();
						tv.setBackgroundResource(R.drawable.text_background);
						if(gzbx != null && bxlxs.get(i).getName().equals(gzbx.getType())){
							dealBxlxUi();
							bxlxid = bxid;
							tv.setSelected(true);
							tv.setTextColor(Color.rgb(255, 255, 255));
							setSubmitUi();
						}else{
							tv.setSelected(false);
							tv.setTextColor(Color.rgb(97, 129, 221));
						}
						map.put(bxlxs.get(i).getId(), tv);//所有的放集合中
						tv.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(!v.isSelected() && !bxlxid.equals(bxid)){
									bxlxid = bxid;
									dealBxlxUi();
									v.setSelected(true);
									tv.setTextColor(Color.rgb(255, 255, 255));
								}else{
									bxlxid ="";
									v.setSelected(false);
									tv.setTextColor(Color.rgb(97, 129, 221));
								}
								setSubmitUi();
							}
						});
						mFlowLayout.addView(tv);
					}
					break;
				case -1://失败
					showMessage("提交失败");
					ok.setEnabled(true);
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 监听报修地点
	 */
	TextWatcher mWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			num.setText(bxdd.getText().toString().length()+"");
			setSubmitUi();
		}
	};

	/**
	 * 监听报修内容 输入
	 */
	TextWatcher mWatcher2 = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			num_bxnr.setText(bxnr.getText().toString().length()+"");
			setSubmitUi();
		}
	};

	/**
	 * 监听报修内容 输入
	 */
	TextWatcher mWatcher3 = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			setSubmitUi();
		}
	};

	/**
	 *
	 */
	public void setSubmitUi(){
		if(bxdd.getText().toString().length()>0 && bxnr.getText().toString().length()>0 && StringUtils.isNotEmpty(bxlxid) && lxdh.getText().toString().length()>0){
			ok.setEnabled(true);
			ok.setBackgroundResource(R.drawable.shape_corner_button);
			ok.setTextColor(Color.WHITE);
		}else{
			ok.setEnabled(false);
			ok.setBackgroundResource(R.drawable.shape_corner_button_no);
			ok.setTextColor(Color.rgb(172, 187, 234));
		}
	}

	/**
	 * 提交报修
	 */
	public void submit(String imgid){
		showProgressDialog();
		Map<String,Object> map = new HashMap<String, Object>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("bxdd", bxdd.getText().toString());
		map.put("bxnr", bxnr.getText().toString());
		map.put("lxdh", lxdh.getText().toString());
		map.put("dh", dh);
		map.put("bxlx", bxlxid);
		map.put("fjName",imgid);


		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/tjbx", map, new HttpListener<JSONObject>() {
			@Override
			public void onError(VolleyError error) {
				hideProgressDialog();
				showMessage("提交失败，请稍后再试");
				ok.setEnabled(true);
			}

			@Override
			public void onSuccess(JSONObject arg0) {
				hideProgressDialog();
				ok.setEnabled(true);
				Result result = HttpUtil.getResult(arg0);
				if(result.getCode() == 200 && (Boolean)result.getData()){
					showMessage(result.getMsg());
					if(FaultRepairApplyDetailActivity.instance != null){
						FaultRepairApplyDetailActivity.instance.finish();
					}
					finish();
				}else{
					showMessage(result.getMsg());
				}
			}
		});




	}

	/**
	 * 设置报修类型的背景色
	 */
	public void dealBxlxUi(){
		for(Map.Entry<String, CheckedTextView> entry : map.entrySet()){
			CheckedTextView tv = entry.getValue();
			tv.setSelected(false);//清楚选中的值
			tv.setTextColor(Color.rgb(97, 129, 221));
		}
	}

	/**
	 * EditText竖直方向是否可以滚动
	 * @param editText 需要判断的EditText
	 * @return true：可以滚动  false：不可以滚动
	 */
	private boolean canVerticalScroll(EditText editText) {
		//滚动的距离
		int scrollY = editText.getScrollY();
		//控件内容的总高度
		int scrollRange = editText.getLayout().getHeight();
		//控件实际显示的高度
		int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
		//控件内容总高度与实际显示高度的差值
		int scrollDifference = scrollRange - scrollExtent;

		if(scrollDifference == 0) {
			return false;
		}

		return (scrollY > 0) || (scrollY < scrollDifference - 1);
	}

	public void clearImg(){
		noCutFilePath="";
	}

	public void ablum(){
		File appDir = new File(BASEPICPATH);
		if(!appDir.exists()){
			appDir.mkdir();
		}
		noCutFilePath = BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
		File file = new File(noCutFilePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	       /*获取当前系统的android版本号*/
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion<24){
			getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}else{
			ContentValues contentValues = new ContentValues(1);
			contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
			Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		startActivityForResult(getImageByCamera, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1://拍照
				if (resultCode == RESULT_OK) {
					if (StringUtils.isNotEmpty(noCutFilePath) && new File(noCutFilePath).exists()) {
						Bitmap bitmap = createThumbnail(noCutFilePath);
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(new File(noCutFilePath));
							bitmap.compress(Bitmap.CompressFormat.JPEG,30,fos);// 把数据写入文件
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						utils.display(mShow,noCutFilePath);
						addImg.setVisibility(View.GONE);
						showImg.setVisibility(View.VISIBLE);
						imgs.remove(0);
						imgs.add(noCutFilePath);
//	                        wImageAdapter.notifyDataSetChanged();
					}
				}
				break;
		}
	}

	/**
	 * 压缩图片
	 *
	 */
	private Bitmap createThumbnail(String filepath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Config.RGB_565;
		BitmapFactory.decodeFile(filepath, options);

		options.inJustDecodeBounds = false;
//	        int w = options.outWidth;
//	        int h = options.outHeight;
		int w = options.outWidth;
		int h = options.outHeight;
		// 想要缩放的目标尺寸
		float hh = h/2;// 设置高度为240f时，可以明显看到图片缩小了
		float ww = w/2;// 设置宽度为120f，可以明显看到图片缩小了
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (options.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (options.outHeight / hh);
		}
		if (be <= 0) be = 1;
//	        options.inSampleSize = be;//设置缩放比例
		options.inSampleSize = 4;//设置缩放比例
		// 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了

		return BitmapFactory.decodeFile(filepath, options);
	}

	/**
	 * 上传图片
	 */
	public void updateImg(){
		final String uuid = dh;
		LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
		String mUrl = getReString(R.string.uploadUrlHq);
		if(loginMsg != null) {//loginMsg.getUid()
			mUrl +="/independent.service?.lm=bxgl-dwjk&.ms=view&action=fjscjk&.ir=true&type=bxsqAttachment&userId="+loginMsg.getUid()+"&password="+ loginMsg.getEncpsw() +"&ownerId="+uuid;
		}else{
			mUrl +="/independent.service?.lm=bxgl-dwjk&.ms=view&action=fjscjk&.ir=true&type=bxsqAttachment&ownerId="+uuid;
		}

		Logger.info("POST 请求连接=======" + mUrl);
		List<File> files = new ArrayList<File>();
		files.add(new File(noCutFilePath));
		Logger.info("filepath"+noCutFilePath);

//		com.dk.mp.core.http.HttpUtil.getInstance().uploadImg( mUrl, files,new  okhttp3.Callback (){
//
//			@Override
//			public void onFailure(Call call, IOException e) {
//				mHandler.sendEmptyMessage(-1);
//				showMessage("上传附件失败"+e.getMessage());
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				showMessage("上传附件cg"+response.message());
////				JSONObject result = HttpClientUtil.getJSONObject(response.toString());
////				Logger.info("######################result="+result);
////				if(result != null) {
////					ResultCode rcode = new Gson().fromJson(result.toString(), ResultCode.class);
////					if (rcode.getCode() == 200) {
////						submit(uuid);
////					} else {
////						mHandler.sendEmptyMessage(-1);
////						showMessage(rcode.getMsg());
////					}
////				}else{
////					mHandler.sendEmptyMessage(-1);
////					showMessage("上传附件失败");
////				}
//			}
//		});
		Logger.info(mUrl);
		HttpClientUtil.upload(mContext, mUrl, noCutFilePath, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mHandler.sendEmptyMessage(-1);
				Logger.info(arg0.getExceptionCode()+arg0.getMessage()+"---"+arg1);
				showMessage("上传附件失败"+arg1);
			}
			@Override
			public void onSuccess(ResponseInfo<String> response) {
				JSONObject result = HttpClientUtil.getJSONObject(response);
				Logger.info("######################result="+result);
				if(result != null) {
					ResultCode rcode = new Gson().fromJson(result.toString(), ResultCode.class);
					if (rcode.getCode() == 200) {
						submit(uuid);
					} else {
						mHandler.sendEmptyMessage(-1);
						showMessage(rcode.getMsg());
					}
				}else{
					mHandler.sendEmptyMessage(-1);
					showMessage("上传附件失败");
				}
			}
		});
	}
}
