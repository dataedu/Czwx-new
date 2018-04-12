package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.adapter.FaultRepairAuditingSelectAdapter;
import com.dk.mp.apps.gzbxnew.adapter.FaultRepairAuditingSelectAdapter.MyView;
import com.dk.mp.apps.gzbxnew.entity.Bxlx;
import com.dk.mp.apps.gzbxnew.entity.Result;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障报修选人
 * @author admin
 *
 */
@SuppressLint("HandlerLeak")
public class FaultRepairAuditingSelectActivity extends MyActivity implements IXListViewListener,OnItemClickListener{
	private FaultRepairAuditingSelectAdapter mAdapter;
	private Context mContext;
	private String title;
	private String type;
	private LinearLayout wxry_lin,wxlx_lin;
	private EditText shyj;
	private TextView num_shyj;
	private CoreSharedPreferencesHelper helper;
	List<Bxlx> wxlxs;
	List<Bxlx> rylbs;
	XListView listView;
	private Button ok;
	private String gzbxid;
	com.dk.mp.apps.gzbxnew.widget.TagLayout mFlowLayout;
	private String ryid = "";//人员id
	private ScrollView sv_parent,sv_child;
	private Map<String,CheckedTextView> map = new HashMap<String,CheckedTextView>();


	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_auditing_select;
	}

	@Override
	protected void initialize() {
		super.initialize();
		initView();
	}

	protected void initView(){
		mContext = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		title = getIntent().getStringExtra("title");
		setTitle(title);
		type = getIntent().getStringExtra("type");
		gzbxid = getIntent().getStringExtra("gzbxid");
		mFlowLayout = (com.dk.mp.apps.gzbxnew.widget.TagLayout) findViewById(R.id.tags);
		wxry_lin = (LinearLayout) findViewById(R.id.wxry_lin);
		wxlx_lin = (LinearLayout) findViewById(R.id.wxlx_lin);
		listView = (XListView) findViewById(R.id.listView);
		shyj = (EditText) findViewById(R.id.shyj);
		num_shyj = (TextView) findViewById(R.id.num_shyj);
		ok = (Button) findViewById(R.id.ok);
		shyj.addTextChangedListener(mWatcher);
		listView.hideFooter();
		listView.hideHeader();
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setOnItemClickListener(this);
		if("tg".equals(type)){
			wxry_lin.setVisibility(View.VISIBLE);
			wxlx_lin.setVisibility(View.VISIBLE);
		}else{
			wxry_lin.setVisibility(View.GONE);
			wxlx_lin.setVisibility(View.GONE);
		}
		if(DeviceUtil.checkNet2()){
			getWxlxs();
			if("tg".equals(type)){
				getRylbs();
			}
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
		
		shyj.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
			    //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
			    if ((view.getId() == R.id.shyj && canVerticalScroll(shyj))) {
			      view.getParent().requestDisallowInterceptTouchEvent(true);
			      if (event.getAction() == MotionEvent.ACTION_UP) {
			        view.getParent().requestDisallowInterceptTouchEvent(false);
			      }
			    }
				return false;
			}
		});
	}
	
	/**
	 * 获取维修类型
	 */
	private void getWxlxs(){
		showProgressDialog();
		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/wxlx", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject arg0) {
				hideProgressDialog();
				wxlxs = HttpUtil.getBxlxs(arg0);
				if(wxlxs.size()>0)
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
	
	/**
	 * 获取人员列表
	 */
	private void getRylbs(){
		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/rylb", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject arg0) {
				rylbs = HttpUtil.getBxlxs(arg0);
				if(rylbs.size()>0)
					mHandler.sendEmptyMessage(2);
				else
					mHandler.sendEmptyMessage(0);
			}

			@Override
			public void onError(VolleyError error) {
				setErrorDate(null);
			}
		});
	}
	
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setNoDate(null);
				break;
			case 1:
				mAdapter = new FaultRepairAuditingSelectAdapter(mContext, wxlxs);
				listView.setAdapter(mAdapter);
				setListViewHeightBasedOnChildren(listView);
				break;
			case 2:
				for (int i = 0; i < rylbs.size(); i++) {
		            final CheckedTextView tv = new CheckedTextView(mContext);
		            tv.setText("["+rylbs.get(i).getId() +"]"+rylbs.get(i).getName());
		            tv.setTextColor(Color.BLACK);
		            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		            params.setMargins(DeviceUtil.dip2px(mContext, 15), DeviceUtil.dip2px(mContext, 15), 0, 0);
		            tv.setLayoutParams(params);
		            tv.setMaxWidth(DeviceUtil.getScreenWidth(mContext) - DeviceUtil.dip2px(mContext, 20));//这句话是为了限制过长的内容顶出屏幕而设置的
		            tv.setHeight(DeviceUtil.dip2px(mContext, 44));
		            tv.setEllipsize(TruncateAt.END);
		            tv.setGravity(Gravity.CENTER);
		            tv.setTextSize(17);
		            tv.setMaxLines(1);
		            tv.setTextColor(Color.rgb(97, 129, 221));
		            tv.setBackgroundResource(R.drawable.text_background);
		            map.put(rylbs.get(i).getId(), tv);//所有的放集合中
		            final String bxid = rylbs.get(i).getId();
		            tv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(!v.isSelected() && !ryid.equals(bxid)){
								dealBxlxUi();
								ryid = bxid;
								v.setSelected(true);
								tv.setTextColor(Color.rgb(255, 255, 255));
								setSubmitUi();
							}else{
								ryid ="";
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
		};
	};

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		
	}

	@Override
	public void stopLoad() {
		
	}

	@Override
	public void getList() {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Logger.info("position=" + position);
		MyView holder = (MyView) view.getTag();
		mAdapter.clean();
		if (holder.getCheckBox().isChecked()) {
			holder.getCheckBox().setChecked(false);
		} else {
			holder.getCheckBox().setChecked(true);
		}
		mAdapter.getIsSelected().put(position-1, holder.getCheckBox().isChecked());
		mAdapter.notifyDataSetChanged();
		
		setSubmitUi();
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);   
    }   
	
	/**
	 * 监听报修内容 输入
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
			num_shyj.setText(shyj.getText().toString().length()+"");
			setSubmitUi();
		}
	};
	
	/**
	 * 
	 */
	public void setSubmitUi(){
		if((shyj.getText().toString().length()>0 && !"tg".equals(type)) ||
				(shyj.getText().toString().length()>0 && mAdapter.getChecked().size()>0 && "tg".equals(type) && StringUtils.isNotEmpty(ryid))){
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
		Map<String,Object> map = new HashMap<String, Object>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("message", shyj.getText().toString());
		map.put("id", gzbxid);
		map.put("type", type);
		if("tg".equals(type)){
			map.put("wxlb", wxlxs.get(mAdapter.getChecked().get(0)).getName());
			map.put("wxry", ryid);
		}else{
			map.put("wxlb", "");
		}

		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/sh", map, new HttpListener<JSONObject>() {

			@Override
			public void onSuccess(JSONObject arg0) {
				hideProgressDialog();
				ok.setEnabled(true);
				Result result = HttpUtil.getResult(arg0);
				if(result.getCode() == 200 && (Boolean)result.getData()){
					showMessage(result.getMsg());
					finish();
					if(com.dk.mp.apps.gzbxnew
							.FaultRepairAuditingDetailActivity.instance != null){
						com.dk.mp.apps.gzbxnew
								.FaultRepairAuditingDetailActivity.instance.finish();
					}
				}else{
					showMessage(result.getMsg());
				}
			}

			@Override
			public void onError(VolleyError error) {
				hideProgressDialog();
				showMessage("提交失败，请稍后再试");
				ok.setEnabled(true);
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
