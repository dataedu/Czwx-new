package com.dk.mp.apps.gzbxnew;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.RatingBarView.OnRatingListener;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.Result;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * 未评详情
 * @author admin
 *
 */
public class FaultRepairCommentOnWpDetailActivity extends MyActivity {
	CoreSharedPreferencesHelper helper;
	private Context mContext;
	private TextView bxdh,bxrq,wxry,bxr,bxdd,bxlx,bxnr;
	private Gzbx gzbx;
	private RatingBarView rBar;
	private EditText pjnr;
	private TextView num;
	private Button ok;
	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_commenton_wp;
	}
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fault_repair_commenton_wp);
//		setTitle("故障报修");
//		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
//		mContext = this;
//		helper = new CoreSharedPreferencesHelper(mContext);
//		initView();
//	}

	protected void initView(){
		setTitle("故障报修");
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
		mContext = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		bxdh = (TextView) findViewById(R.id.bxdh);
		bxr = (TextView) findViewById(R.id.bxr);
		bxrq = (TextView) findViewById(R.id.bxrq);
		bxlx = (TextView) findViewById(R.id.bxlx);
		wxry = (TextView) findViewById(R.id.wxry);
		bxdd = (TextView) findViewById(R.id.bxdd);
		bxnr = (TextView) findViewById(R.id.bxnr);
		rBar = (RatingBarView) findViewById(R.id.ratingBarwp);
		pjnr = (EditText) findViewById(R.id.pjnr);
		num = (TextView) findViewById(R.id.num);
		ok = (Button) findViewById(R.id.ok);
		ok.setEnabled(false);
		
		bxdh.setText(gzbx.getBxdh());
		bxr.setText(gzbx.getName());
		bxrq.setText(gzbx.getTime());
		bxlx.setText(gzbx.getType());
		wxry.setText(gzbx.getWxry());
		bxdd.setText(gzbx.getBxdd());
		bxnr.setText(gzbx.getBxnr());
		
		pjnr.addTextChangedListener(mWatcher);
		
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(DeviceUtil.checkNet()){
					ok.setEnabled(false);
					submit();
				}
			}
		});
		rBar.setOnRatingListener(new OnRatingListener() {
			@Override
			public void onRating(Object bindObject, int RatingScore) {
				if(RatingScore >0){
					setBackUi();
				}
			}
		});
		
		pjnr.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
			    //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
			    if ((view.getId() == R.id.pjnr && canVerticalScroll(pjnr))) {
			      view.getParent().requestDisallowInterceptTouchEvent(true);
			      if (event.getAction() == MotionEvent.ACTION_UP) {
			        view.getParent().requestDisallowInterceptTouchEvent(false);
			      }
			    }
				return false;
			}
		});
	}
	
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
			num.setText(pjnr.getText().toString().length()+"");
			setBackUi();
		}
	};
	
	public void setBackUi(){
		if(pjnr.getText().toString().length()>0 && rBar.getStarCount()>0){
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
		map.put("id", gzbx.getId());
		map.put("message", pjnr.getText().toString());
		map.put("star", rBar.getStarCount()+"");
		
		HttpClientUtil.post("apps/gzbx/pj", map, new RequestCallBack<String>() {
			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
				hideProgressDialog();
				showMessage("提交失败，请稍后再试");
				ok.setEnabled(true);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				hideProgressDialog();
				ok.setEnabled(true);
				Result result = HttpUtil.getResult(arg0);
				if(result.getCode() == 200 && Boolean.parseBoolean(String.valueOf(result.getData()))){
					showMessage(result.getMsg());
					finish();
				}else{
					showMessage(result.getMsg());
				}
			}
		});
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
