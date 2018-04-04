package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dk.mp.apps.gzbxnew.adapter.FaultRepairApplyAdapter;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修申请列表页
 * @author admin
 *
 */
public class FaultRepairApplyActivity extends MyActivity implements IXListViewListener{
	private Context mContext;
	private XListView listView;
	private CoreSharedPreferencesHelper helper;
	private int pageNo = 1;
	private int countPage = 0;
	private ImageView  addRepair;//添加按钮
	private List<Gzbx> mList = new ArrayList<Gzbx>();
	private FaultRepairApplyAdapter mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fault_repair_apply;
    }

//    @Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fault_repair_apply);
//		mContext  = this;
//		helper = new CoreSharedPreferencesHelper(mContext);
//		setTitle("故障报修");
//		initView();
////		onRefresh();
//	}

	@Override
	protected void initView(){
		mContext  = this;
		helper = new CoreSharedPreferencesHelper(mContext);
		setTitle("故障报修");
		listView = (XListView) findViewById(R.id.listView);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		
		addRepair = (ImageView) findViewById(R.id.add_repair);
		addRepair.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, com.dk.mp.apps.gzbxnew.FaultRepairAddActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRefresh() {
		if(DeviceUtil.checkNet2()){
			pageNo = 1;
			getData();
		}else{
			if(mList.size()>0){
				MsgDialog.show(mContext, getString(R.string.net_no2));
				listView.stopRefresh();
			}else{
				setNoWorkNet();
			}
		}
	}


	@Override
	public void onLoadMore() {
		if(DeviceUtil.checkNet()){
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
		//hideError();
		Map<String,String> map = new HashMap<String,String>();
		if(helper.getLoginMsg()!=null){
			map.put("userId", helper.getLoginMsg().getUid());
		}
		map.put("pjzt", "");
		map.put("jklx", "sq");
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
					mAdapter = new FaultRepairApplyAdapter(mContext,mList,"2","申请","1");
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
