package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import com.dk.mp.apps.gzbxnew.adapter.FaultRepairApplyAdapter;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障报修评价
 * @author admin
 *
 */
public class FaultRepairCommentOnActivity extends MyActivity implements IXListViewListener{
	private Context mContext;
	private XListView listView;
	private FaultRepairApplyAdapter mAdapter;
	private List<Gzbx> mList = new ArrayList<Gzbx>();
	private int pageNo = 1;
	private int countPage = 0;
	private CoreSharedPreferencesHelper helper;
	private String pjzt;
	private String shzt;
	private String type = "1";
	private String category ="1";
	private String jklx;

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_commenton;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mContext = FaultRepairCommentOnActivity.this;
//		helper = new CoreSharedPreferencesHelper(mContext);
//		pjzt = getIntent().getStringExtra("pjzt");
//		shzt = getIntent().getStringExtra("shzt");
//		jklx = getIntent().getStringExtra("jklx");
//		category = getIntent().getStringExtra("category");
//		if(StringUtils.isNotEmpty(pjzt)){
//			type = "1";
//		}else{
//			type = "3";
//		}
//		setContentView(R.layout.fault_repair_commenton);
//		initView();
//		setTitle("故障报修");
//
//	}

	protected void initView(){
		mContext = FaultRepairCommentOnActivity.this;
		helper = new CoreSharedPreferencesHelper(mContext);
		pjzt = getIntent().getStringExtra("pjzt");
		shzt = getIntent().getStringExtra("shzt");
		jklx = getIntent().getStringExtra("jklx");
		category = getIntent().getStringExtra("category");
		if(StringUtils.isNotEmpty(pjzt)){
			type = "1";
		}else{
			type = "3";
		}
		setContentView(R.layout.fault_repair_commenton);
		setTitle("故障报修");
		listView = (XListView) findViewById(R.id.listView);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
	}

	@Override
	public void onRefresh() {
		if(DeviceUtil.checkNet2()){//判断网络
	//		hideError();
			pageNo = 1;
			getData();
		}else{
			if(mList.size()>0){
				MsgDialog.show(mContext, getString(R.string.net_no2));
				listView.stopRefresh();
				listView.stopLoadMore();
			}else{
				setNoWorkNet();
			}
		}
	}

	@Override
	public void onLoadMore() {
		if(DeviceUtil.checkNet2()){//判断网络
			pageNo++;
			getData();
		}else{
			listView.stopLoadMore();
		}
	}

	@Override
	public void stopLoad() {
		
	}

	@Override
	public void getList() {
		
	}
	
	
	private void getData(){
		showProgressDialog();
		Map<String,String> map = new HashMap<String,String>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("pjzt", pjzt);
		map.put("jklx", jklx);
		map.put("shzt", shzt);
		map.put("pageNo", pageNo+"");
		HttpClientUtil.post("apps/gzbx/getList", map, new RequestCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				hideProgressDialog();
				PageMsg pageMsg = HttpUtil.getList(arg0);
				countPage = (int) pageMsg.getTotalPages();
				if(pageMsg.getList().size()>0){
					if(pageNo ==1){
						mList.clear();
					}
					mList.addAll(pageMsg.getList());
					mHandler.sendEmptyMessage(1);
				}else{
					mHandler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
				hideProgressDialog();
				if(pageNo ==1){
					setErrorDate(null);
				}else{
					showMessage(getString(R.string.data_fail));
				}
			}


		});
	}
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0://没获取到数据
				if(pageNo ==1){
					setNoDate(null);
				}else{
					showMessage("没有更多数据");
					listView.stopLoadMore();
					listView.hideFooter();
				}
				break;
			case 1:
				if(mAdapter== null){
					mAdapter = new FaultRepairApplyAdapter(mContext,mList,type,pjzt,category);
					listView.setAdapter(mAdapter);
				}else{
					mAdapter.setmData(mList);
					mAdapter.notifyDataSetChanged();
				}
				if (pageNo >= countPage) {
					listView.hideFooter();
				} else {
					listView.showFooter();
				}
				listView.stopRefresh();
				listView.stopLoadMore();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		onRefresh();
	};
}
