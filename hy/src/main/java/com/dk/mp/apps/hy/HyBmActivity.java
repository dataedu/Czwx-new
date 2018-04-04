package com.dk.mp.apps.hy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dk.mp.apps.hy.adapter.BmAdapter;
import com.dk.mp.apps.hy.db.YellowPageDBHelper;
import com.dk.mp.apps.hy.entity.Bm;
import com.dk.mp.apps.hy.http.YellowPageHttpUtil;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @since 
 * @version 2013-4-2
 * @author wwang
 */
public class HyBmActivity extends MyActivity implements OnItemClickListener {
	private Context context = HyBmActivity.this;
	private ListView listView;
	private BmAdapter adapter1;
	private List<Bm> list;
	private LinearLayout search_linearlayout;

    @Override
    protected int getLayoutID() {
        return R.layout.app_yellowpage;
    }

//    @Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.app_yellowpage);
//		setTitle("黄页");
//		initViews();
//		search_linearlayout.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(HyBmActivity.this, HySearchActivity.class));
//			}
//		});
//		if(DeviceUtil.checkNet2()){
//			getDepartList();
//		}else{
//			getList();
//		}
//	}

	/**
	 * 初始化控件
	 */
    protected void initView() {

        setTitle("黄页");
//        initViews();
        search_linearlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HyBmActivity.this, HySearchActivity.class));
            }
        });
        if(DeviceUtil.checkNet2()){
            getDepartList();
        }else{
            getList();
        }
		search_linearlayout = (LinearLayout) findViewById(R.id.search_linearlayout);
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				showProgressDialog();
				break;
			case 1:
				if (adapter1 == null) {
					adapter1 = new BmAdapter(context, list);
					listView.setAdapter(adapter1);
				} else {
					adapter1.setList(list);
					adapter1.notifyDataSetChanged();
				}
				break;
			case 2:
				if (adapter1 == null) {
					adapter1 = new BmAdapter(context, list);
					listView.setAdapter(adapter1);
				} else {
					adapter1.setList(list);
					adapter1.notifyDataSetChanged();
				}
				hideProgressDialog();
				break;
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(context, HyKsActivity.class);
		intent.putExtra("id", list.get(position).getIdDepart());
		intent.putExtra("type", "depart");
		intent.putExtra("name", list.get(position).getNameDepart());
		startActivity(intent);
	}


	/**
	 * 初始化数据
	 */
	private void getList() {
		list = new YellowPageDBHelper(context).getDepartMentList();
		if (list.size() > 0) {
			adapter1 = new BmAdapter(context, list);
			listView.setAdapter(adapter1);
			MsgDialog.show(context, context.getString(R.string.net_no2));
		}else{
			setNoWorkNet();
		}
	}


	/**
	 * 初始化列表.
	 * @param context Context
	 * @param time 时间戳
	 * @return List<App>
	 */
	public  void getDepartList() {
		showProgressDialog();
		Map<String, String> map = new HashMap<String, String>();
		HttpClientUtil.post("apps/yellowpage/getBmList",map, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				hideProgressDialog();
				list=YellowPageHttpUtil.getDepartList(responseInfo);
				if(list.size()>0){
					new YellowPageDBHelper(context).insertDepartList(list);
					handler.sendEmptyMessage(2);
				}else{
					setNoDate(null);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				setErrorDate(null);
				hideProgressDialog();
			}
		});
	}
}
