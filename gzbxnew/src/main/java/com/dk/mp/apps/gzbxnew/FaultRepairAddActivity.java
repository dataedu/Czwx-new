package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.entity.Bxlx;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.Result;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.TimeUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 com.dk.mp.apps.gzbxnew.TagLayout mFlowLayout;
	 List<Bxlx> bxlxs;
	 private ScrollView sv_parent,sv_child;
	 private TextView num,num_bxnr;
	 private EditText bxdd,bxnr;
//	 private Map<String,String> map = new HashMap<String,String>();//存放报修类型信息
	 private Button ok;
	 private String dh="";
	 private String bxlxid = "";//报修类型id
	 private Map<String,CheckedTextView> map = new HashMap<String,CheckedTextView>();
	 private Gzbx gzbx;

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_add;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fault_repair_add);
//		setTitle("故障报修");
//		mContext = this;
//		helper = new CoreSharedPreferencesHelper(mContext);
//		mFlowLayout = (com.dk.mp.apps.gzbxnew.TagLayout) findViewById(R.id.tags);
//		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbx");
//		initView();
//	}



	public void initView(){
		setTitle("故障报修");
		mContext = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		mFlowLayout = (com.dk.mp.apps.gzbxnew.TagLayout) findViewById(R.id.tags);
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbx");
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
			dh = gzbx.getBxdh();
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
		sv_child.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewParent parent1 = v.getParent();
                parent1.requestDisallowInterceptTouchEvent(true); //不允许父类截断
                return false;
            }
        });
		
		bxdd.setOnTouchListener(new OnTouchListener() {
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
		
		bxnr.setOnTouchListener(new OnTouchListener() {
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
					submit();
				}
			}
		});
	}
	
	/**
	 * 创建报修单号
	 * @return
	 */
	private String createBxdh(){
		return "bx"+TimeUtils.getNowTime();
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
		HttpClientUtil.post("apps/gzbx/getTypeList", map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				hideProgressDialog();
				bxlxs = HttpUtil.getBxlxs(arg0);
				if(bxlxs.size()>0)
					mHandler.sendEmptyMessage(1);
				else
					mHandler.sendEmptyMessage(0);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
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
	 * 
	 */
	public void setSubmitUi(){
		if(bxdd.getText().toString().length()>0 && bxnr.getText().toString().length()>0 && StringUtils.isNotEmpty(bxlxid)){
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
	public void submit(){
		showProgressDialog();
		Map<String,String> map = new HashMap<String, String>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("bxdd", bxdd.getText().toString());
		map.put("bxnr", bxnr.getText().toString());
		map.put("dh", dh);
		map.put("bxlx", bxlxid);

		HttpClientUtil.post("apps/gzbx/tjbx", map, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				hideProgressDialog();
				showMessage("提交失败，请稍后再试");
				ok.setEnabled(true);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
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
}
