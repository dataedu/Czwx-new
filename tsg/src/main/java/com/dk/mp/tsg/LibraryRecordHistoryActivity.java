package com.dk.mp.tsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tsg.entity.BookRecord;
import com.google.gson.reflect.TypeToken;

public class LibraryRecordHistoryActivity extends BaseFragment implements View.OnClickListener {

	private List<BookRecord> list = new ArrayList<BookRecord>();
	private MyListView listView;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutId() {
		return R.layout.ac_library_record_history;
	}

	@Override
	protected void initialize(View view) {
		super.initialize(view);

		errorLayout = (ErrorLayout) view.findViewById(R.id.error_layout);
		listView = (MyListView) view.findViewById(R.id.newslist);

		errorLayout.setOnLayoutClickListener(this);

		LinearLayoutManager manager = new LinearLayoutManager(getContext());
		listView.setLayoutManager(manager);
		listView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
		listView.setAdapterInterface(list, new AdapterInterface() {
			@Override
			public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
				View view =  LayoutInflater.from(mContext).inflate(R.layout.app_library_record_item, parent, false);// 设置要转化的layout文件
				return new MyView(view);
			}

			@Override
			public void setItemValue(RecyclerView.ViewHolder holder, int position) {
				((MyView)holder).name.setText(list.get(position).getName());
				((MyView)holder).jssj.setText(list.get(position).getJssj());
				((MyView)holder).ghsj.setText(list.get(position).getYjhssj());
			}

			@Override
			public void loadDatas() {
				getData();
			}
		});

		getData();
	}

	public void getData(){
		if(DeviceUtil.checkNet()) {
			update();
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	public void update() {
//		listView.startRefresh();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", listView.pageNo);
		Log.e("eeeeeeeeeeeeeeeeee",listView.pageNo+"");
		HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<BookRecord>>(){},"apps/tsg/getHistory", map, new HttpListener<PageMsg<BookRecord>>() {
			@Override
			public void onSuccess(PageMsg<BookRecord> result) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				if (result.getList() != null && result.getList().size() > 0) {
					list.addAll(result.getList());
					listView.finish(result.getTotalPages(), result.getCurrentPage());
				} else {
					if(listView.pageNo == 1) {
						errorLayout.setErrorType(ErrorLayout.NODATA);
						errorLayout.setTextv("暂无借阅记录");
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

	private class MyView extends RecyclerView.ViewHolder{
		private TextView name, jssj,ghsj;

		public MyView(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.name);// 取得实例
			jssj = (TextView) itemView.findViewById(R.id.jssj);// 取得实例
			ghsj = (TextView) itemView.findViewById(R.id.ghsj);// 取得实例

		}
	}


}
