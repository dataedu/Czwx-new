package com.dk.mp.pjpy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.pjpy.R;
import com.dk.mp.pjpy.entity.PjpyEntity;
import com.google.gson.reflect.TypeToken;

public class PjpyListActivity extends BaseFragment implements View.OnClickListener {

	private List<PjpyEntity> list = new ArrayList<>();
	private MyListView listView;

	private ErrorLayout errorLayout;

	private String type;
	private String murl = "";
	private String detailsurl = "";

	private LoginMsg loginMsg;

	@Override
	protected int getLayoutId() {
		return R.layout.sw_main;
	}

	public static PjpyListActivity newInstance(String type) {
		Bundle args = new Bundle();
		args.putString("type",type);
		PjpyListActivity fragment = new PjpyListActivity();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);
		listView = (MyListView) view.findViewById(R.id.newslist);

		loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
		type = getArguments().getString("type");

		if (getActivity().getIntent().getAction().equals("pjpy")){
			murl = "apps/pjpy/getList";
			detailsurl = "apps/pjpy/detail?id=";
		}else {
			murl = "apps/zizhu/getList";
			detailsurl = "apps/zizhu/detail?idZiZhu=";
		}

		errorLayout.setOnLayoutClickListener(this);

		LinearLayoutManager manager = new LinearLayoutManager(getContext());
		listView.setLayoutManager(manager);
		listView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
		listView.setAdapterInterface(list, new AdapterInterface() {
			@Override
			public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
				View view =  LayoutInflater.from(mContext).inflate(R.layout.sw_main_item, parent, false);// 设置要转化的layout文件
				return new MyView(view);
			}

			@Override
			public void setItemValue(RecyclerView.ViewHolder holder, int position) {
				((MyView)holder).swname.setText(list.get(position).getName());
				((MyView)holder).swstatus.setText(list.get(position).getStatus());
				((MyView)holder).swtime.setText(list.get(position).getTime());
			}

			@Override
			public void loadDatas() {
				getData();
			}
		});

		getData();

	}

	private class MyView extends RecyclerView.ViewHolder{
		private TextView swname, swstatus,swtime;

		public MyView(View itemView) {
			super(itemView);
			swname = (TextView) itemView.findViewById(R.id.swname);// 取得实例
			swstatus = (TextView) itemView.findViewById(R.id.swstatus);// 取得实例
			swtime = (TextView) itemView.findViewById(R.id.swtime);// 取得实例

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), HttpWebActivity.class);
					String title = "";
					if (getActivity().getIntent().getAction().equals("pjpy")){
						 title = type.equals("1")?"奖学金":"荣誉称号";
					}else {
						title = type.equals("1")?"助学金":"困难生认定";
					}

					intent.putExtra("title", title);
					intent.putExtra("url", getResources().getString(R.string.rootUrl) + detailsurl +list.get(getLayoutPosition()).getId()+"&uid="+loginMsg.getUid());
					startActivity(intent);
				}
			});

		}
	}

	public void getData(){
		if(DeviceUtil.checkNet()) {
			update();
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	public void update() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", listView.pageNo);
		map.put("type", getArguments().getString("type"));

		HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<PjpyEntity>>(){},murl, map, new HttpListener<PageMsg<PjpyEntity>>() {
			@Override
			public void onSuccess(PageMsg<PjpyEntity> result) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				if (result.getList() != null && result.getList().size() > 0) {
					list.addAll(result.getList());
					listView.finish(result.getTotalPages(), result.getCurrentPage());
				} else {
					if(listView.pageNo == 1) {
						errorLayout.setErrorType(ErrorLayout.NODATA);
					}else{
						SnackBarUtil.showShort(listView,R.string.nodata);
						listView.stopRefresh(true);
					}
				}
			}
			@Override
			public void onError(VolleyError error) {
				errorLayout.setErrorType(ErrorLayout.DATAFAIL);
			}
		});
	}

	@Override
	public void onClick(View v) {

		getData();
	}
}
