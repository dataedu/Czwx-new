package com.dk.mp.ydqj.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.dk.mp.ydqj.Adapter.LeaveApprovalAdapter;
import com.dk.mp.ydqj.R;
import com.dk.mp.ydqj.entity.Leave;

/**
 * 请假审批
 * @author admin
 *
 */
@SuppressLint("HandlerLeak") public class ApprovalLeaveListActivity extends MyActivity implements OnItemClickListener, IXListViewListener{
	public static final String ACTION_REFRESH = "com.test.action.refresh";
	private XListView mListView;
	private int curPage = 1;
	private List<Leave> mList;
	private LeaveApprovalAdapter mAdapter;
	private long countPage = 1;
	private String type = "db";

	@Override
	protected int getLayoutID() {
		return R.layout.app_approval_list;
	}

	@Override
	protected void initialize() {
		super.initialize();

		type = getIntent().getStringExtra("type");
		findView();
		onRefresh();
		BroadcastUtil.registerReceiver(mContext, receiver, ACTION_REFRESH);
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_REFRESH.equals(intent.getAction())) {
				onRefresh();
			}
		}
	};
	
	public void findView(){
		mListView = (XListView) findViewById(R.id.listView);
		mListView.setPullLoadEnable(true);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
	}

	@Override
	public void onRefresh() {
		if (DeviceUtil.checkNet()) {
			curPage = 1;
			getTodo();
		} else {
//			setNoWorkNet();
			mListView.stopRefresh();
		}		
	}

	@Override
	public void onLoadMore() {
		if (DeviceUtil.checkNet()) {
			curPage++;
			getTodoLoadMore();
		}
	}

	@Override
	public void stopLoad() {
		
	}

	@Override
	public void getList() {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Leave l = mAdapter.getItem(position-1);
		Intent intent = new Intent(ApprovalLeaveListActivity.this,LeaveDetailActivity.class);
		intent.putExtra("html_id", l.getId());
		intent.putExtra("type", type);
		intent.putExtra("title", "请假审批");
		startActivity(intent);
	}
	
	
	private void getTodo() {
		/*showProgressDialog();
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("pageNo", String.valueOf(curPage));
		HttpClientUtil.post("apps/qingjia/getList", map, new RequestCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				PageMsg page = HttpUtil.getMyLeaves(responseInfo);
				mList = page.getList();
				if(mList.size()<=0){
					mHandler.sendEmptyMessage(2);
				}else{
					countPage = page.getTotalPages();
					mHandler.sendEmptyMessage(1);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				hideProgressDialog();
				setErrorDate(null);
			}
		});*/
	}
	
	private void getTodoLoadMore(){
		/*showProgressDialog();
		Map<String,String> map = new HashMap<String,String>();
		map.put("pageNo", String.valueOf(curPage));
		map.put("type", type);
		HttpClientUtil.post("apps/qingjia/getList", map, new RequestCallBack<String>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				PageMsg page = HttpUtil.getMyLeaves(responseInfo);
				mList.addAll(page.getList());
				mHandler.sendEmptyMessage(1);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				hideProgressDialog();
				showMessage(R.string.server_error);
			}
		});*/
	}
	
	/*Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mAdapter == null) {
					mAdapter = new LeaveApprovalAdapter(mContext, mList,false);
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.setList(mList);
					mAdapter.notifyDataSetChanged();
				}
				if (curPage >= countPage) {
					mListView.hideFooter();
				} else {
					mListView.showFooter();
				}
				mListView.stopRefresh();
				mListView.stopLoadMore();
				hideProgressDialog();
				break;
			case 2:
				hideProgressDialog();
				mListView.setVisibility(View.GONE);
				setNoDate(null);
				break;
			default:
				break;
			}
		};
	};
	*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastUtil.unregisterReceiver(mContext, receiver);
	};
}
