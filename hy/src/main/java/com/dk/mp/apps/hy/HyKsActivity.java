package com.dk.mp.apps.hy;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dk.mp.apps.hy.adapter.KsAdapter;
import com.dk.mp.apps.hy.db.YellowPageDBHelper;
import com.dk.mp.apps.hy.entity.Ks;
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


/**
 * @since 
 * @version 2013-4-2
 * @author wwang
 */
public class HyKsActivity extends MyActivity {
	private Context context = HyKsActivity.this;
	private List<Ks> list;
	private String id;//群组或部门或跟人分组的id
	private ListView listView;
	private KsAdapter adapter;
	private String name;

    @Override
    protected int getLayoutID() {
        return R.layout.core_mylistview;
    }

//    @Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.core_mylistview);
//		id = getIntent().getStringExtra("id");
//		name = getIntent().getStringExtra("name");
//		setTitle(name);
//		initViews();
//		if(DeviceUtil.checkNet2()){
//			getPeopleList();
//		}else{
//			getList();
//		}
//	}

	/**
	 * 初始化控�?
	 */
    protected void initView() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        setTitle(name);

		listView = (ListView) this.findViewById(R.id.listView);
//		listView.setBackgroundResource(R.color.view_bg);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				final String[] str = getTel(adapter.getItem(position).getList());
				if (str.length > 0) {
					final String[] str1 = str[0].split("/");
					final ListRadioDialog l = new ListRadioDialog(context);
					l.show(str1, new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							l.cancel();
							DeviceUtil.call(context, str1[position]);
						}
					});
				}

			}
		});
        if(DeviceUtil.checkNet2()){
            getPeopleList();
        }else{
            getList();
        }
	}

	private String[] getTel(List<String> tels) {
		String[] temp = new String[tels.size()];
		for (int i = 0; i < tels.size(); i++) {
			temp[i] = tels.get(i);
		}
		return temp;
	}

	/**
	 * 获取联系人列�?
	 */
	private void getList() {
		list = new YellowPageDBHelper(context).getPersonListByDepart(id);
		if (list.size() > 0) {
			adapter = new KsAdapter(context, list);
			listView.setAdapter(adapter);
			MsgDialog.show(context, context.getString(R.string.net_no2));
		}else{
			setNoWorkNet();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (adapter == null) {
					adapter = new KsAdapter(context, list);
					listView.setAdapter(adapter);
				} else {
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}
				break;
			case 1:
				if (adapter == null) {
					adapter = new KsAdapter(context, list);
					listView.setAdapter(adapter);
				} else {
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 初始化列表.

	 * @return List<App>
	 */
	public void getPeopleList() {
		if (DeviceUtil.checkNet()) {
			showProgressDialog();
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			HttpClientUtil.post("apps/yellowpage/getKsList", map, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					hideProgressDialog();
					list = YellowPageHttpUtil.getPeopleList(responseInfo);
					if(list.size()>0){
						new YellowPageDBHelper(context).insertPeopleList(id, list);
						handler.sendEmptyMessage(1);
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

}
