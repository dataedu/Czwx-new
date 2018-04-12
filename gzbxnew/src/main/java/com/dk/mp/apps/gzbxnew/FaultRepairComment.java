package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.adapter.FaultRepairApplyAdapter;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.dk.mp.core.widget.ErrorLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障报修评价
 * @author admin
 *
 */
public class FaultRepairComment extends BaseFragment implements IXListViewListener{
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
    private String category="1";
	private String jklx;

	private ErrorLayout errorLayout;
	@Override
	protected int getLayoutId() {
		return R.layout.fault_repair_commenton;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);
		mContext = getActivity();
		helper = new CoreSharedPreferencesHelper(mContext);
		pjzt  = getArguments().getString("pjzt");;
		shzt = getArguments().getString("shzt");;
		jklx = getArguments().getString("jklx");;
		category= getArguments().getString("category");;

		if(StringUtils.isNotEmpty(pjzt)){
			type = "1";
		}else{
			type = "3";
		}
		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);
		listView = (XListView)view. findViewById(R.id.listView);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		onRefresh();
		BroadcastUtil.registerReceiver(mContext, mRefreshBroadcastReceiver, new String[]{"gzbxpl"});
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi") @Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("gzbxpl")) {
				onRefresh();
			}
		}
	};

	@Override
	public void onRefresh() {
		if(DeviceUtil.checkNet2()){//判断网络
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			pageNo = 1;
			getData();
		}else{
			if(mList.size()>0){
				MsgDialog.show(mContext, getString(R.string.net_no2));
				listView.stopRefresh();
				listView.stopLoadMore();
			}else{
				errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
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
		errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String,Object> map = new HashMap<String,Object>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("pjzt", pjzt);
		map.put("jklx", jklx);
		map.put("shzt", shzt);
		map.put("pageNo", pageNo+"");

		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/getList", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject  arg0) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
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
			public void onError(VolleyError error) {
				//hideProgressDialog();
				if(pageNo ==1){
					errorLayout.setErrorType(ErrorLayout.NODATA);
				}else{
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			}


		});
	}
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0://没获取到数据
				if(pageNo ==1){
					errorLayout.setErrorType(ErrorLayout.NODATA);
					listView.setVisibility(View.GONE);
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
	



}
