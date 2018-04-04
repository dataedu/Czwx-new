package com.dk.mp.ydqj.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ydqj.R;
import com.dk.mp.ydqj.entity.Leave;
import com.google.gson.reflect.TypeToken;

/**
 * 我的请假列表
 * @author admin
 *
 */
public class LeaveListActivity extends MyActivity implements OnClickListener {

	public static final String ACTION_REFRESH = "com.test.action.refresh";

	private MyListView mListView;
	private List<Leave> mList = new ArrayList<>();

	private String title="";

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_myleave_list;
	}

	@Override
	protected void initialize() {
		super.initialize();

		title = getIntent().getStringExtra("title");
		setTitle(title);

		findView();

		BroadcastUtil.registerReceiver(mContext, receiver, ACTION_REFRESH);
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_REFRESH.equals(intent.getAction())) {
				getData();
			}
		}
	};
	
	private void findView(){
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		errorLayout.setOnClickListener(this);

		mListView = (MyListView) findViewById(R.id.listView);

		LinearLayoutManager manager = new LinearLayoutManager(this);
		mListView.setLayoutManager(manager);
		mListView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

		mListView.setAdapterInterface(mList, new AdapterInterface() {
			@Override
			public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
				View view =  LayoutInflater.from(mContext).inflate(R.layout.app_myleave_list_item, parent, false);// 设置要转化的layout文件
				return new MyView(view);
			}

			@Override
			public void setItemValue(RecyclerView.ViewHolder holder, int position) {

				((MyView)holder).titles.setText(mList.get(position).getQjlx());
				((MyView)holder).bumen.setText(mList.get(position).getStatus());
				((MyView)holder).shijian.setText(mList.get(position).getSqsj());
			}

			@Override
			public void loadDatas() {
				getData();
			}
		});
		getData();

		setRightText("新建", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LeaveListActivity.this,AddLeaveActivity.class);
				startActivity(intent);
			}
		});
	}

	private class MyView extends RecyclerView.ViewHolder{
		TextView titles, bumen, shijian,db_lx;
		public MyView(View itemView) {
			super(itemView);
			titles = (TextView) itemView.findViewById(R.id.title);
			db_lx = (TextView) itemView.findViewById(R.id.db_lx);
			bumen = (TextView) itemView.findViewById(R.id.bumen);
			shijian = (TextView) itemView.findViewById(R.id.shijian);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(LeaveListActivity.this,LeaveDetailActivity.class);
					intent.putExtra("html_id", mList.get(getLayoutPosition()).getId());
					intent.putExtra("type", "student");
					intent.putExtra("title", title);
					startActivity(intent);
				}
			});
		}
	}

	public void getData(){
		if(DeviceUtil.checkNet()) {
			getList();
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	public void getList(){
		mListView.startRefresh();
		Map<String,Object> map = new HashMap<>();
		map.put("pageNo",mListView.pageNo);
		HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Leave>>(){}, "apps/qingjia/getMyQj", map, new HttpListener<PageMsg<Leave>>() {
			@Override
			public void onSuccess(PageMsg<Leave> result) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				if(result.getList() != null && result.getList().size()>0) {

					mList.addAll(result.getList());
					mListView.finish(result.getTotalPages(),result.getCurrentPage());
				}else{
					errorLayout.setErrorType(ErrorLayout.NODATA);
				}
			}
			@Override
			public void onError(VolleyError error) {
				errorLayout.setErrorType(ErrorLayout.DATAFAIL);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastUtil.unregisterReceiver(mContext, receiver);
	};

	@Override
	public void onClick(View v) {
		getData();
	}

}
