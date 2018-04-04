package com.dk.mp.ydyx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ydyx.R;
import com.dk.mp.ydyx.adapter.ProcessAdapter;
import com.dk.mp.ydyx.entity.WelLinkd;
import com.dk.mp.ydyx.entity.WelStudent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 迎新办理.
 * @author rm.zhao
 *
 * 2015-5-25
 */

public class WelDoActivity extends MyActivity implements OnClickListener {
	private ImageView im;// 头像
	private WelStudent student = new WelStudent();// 学生实体类对象
	private TextView wname, wsno, wldp, wlcalss, wlfd,paiming;// 姓名、学号、院系、班级、辅导员、排名
	private ListView listView;// 离校流程
	private Bitmap bitmap;
	private List<WelLinkd> linkds;
	private ProcessAdapter adapter;
	private String idUser = "";
	private LinearLayout lshare;
	private Button share;

	private ImageButton right;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_wel_do;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("迎新办理");
//		setRightButton(R.drawable.note,this);
		CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(this);
		idUser = helper.getUser().getUserId();

		findView();

		getDate();
	}

	// 初始化控件
	private void findView() {
		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = lif.inflate(R.layout.app_wel_info, null);
		im = (ImageView) convertView.findViewById(R.id.wheader);
		wname = (TextView) convertView.findViewById(R.id.wname);
		wsno = (TextView) convertView.findViewById(R.id.wsno);
		wldp = (TextView) convertView.findViewById(R.id.wldp);
		wlcalss = (TextView) convertView.findViewById(R.id.wlcalss);
		wlfd = (TextView) convertView.findViewById(R.id.wlfd);

		lshare = (LinearLayout) findViewById(R.id.lshare);
		listView = (ListView) findViewById(R.id.listv);
		right = (ImageButton) findViewById(R.id.right);
		listView.addHeaderView(convertView);
		share = (Button) findViewById(R.id.share);
		paiming = (TextView) findViewById(R.id.paiming);

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		share.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					share.setTextColor(getResources().getColor(R.color.fenxiang_anxia));
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					share.setTextColor(getResources().getColor(R.color.white));
//					DeviceUtil.share(WelDoActivity.this, "报到办理成功", "我在报到办理过程中是第" + student.getPaiming() + "完成的，你呢?");
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == right) {
			Intent intent = new Intent();
			intent.setClass(WelDoActivity.this, RemarkActivity.class);
			//				if(StringUtils.isNotEmpty(student.getBatchid())){
			intent.putExtra("id", student.getIdBatch());
			//				}
			startActivity(intent);
		}else if (v == share) {                                
//			DeviceUtil.share(WelDoActivity.this, "报到办理成功", "我在移动迎新中成功办理，学校排名" + student.getPaiming() + "名");
		}
	}

	public void getDate() {
		if (DeviceUtil.checkNet()) {

			Map<String, Object> map = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(idUser)) {
				map.put("idUser", idUser);
			}
			HttpUtil.getInstance().postJsonObjectRequest("apps/welstu/getStuInfo", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200) {
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							student = new Gson().fromJson(result.getJSONObject("data").getJSONObject("info").toString(), WelStudent.class);
							if (student != null) {
								linkds = new Gson().fromJson(result.getJSONObject("data").getJSONArray("process").toString(),new TypeToken<List<WelLinkd>>(){}.getType());
								if (StringUtils.isNotEmpty(student.getName())) {
									if ("null" != student.getPhoto() && !"".equals(student.getPhoto()) && null != student.getPhoto()) {
										bitmap = ImageUtil.getImage(student.getPhoto());
									}
									handler.sendEmptyMessage(0);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						errorLayout.setErrorType(ErrorLayout.NODATA);
					}
				}

				@Override
				public void onError(VolleyError error) {
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			});
		}else {
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	public void setValues() {
		wname.setText(student.getName());
		wsno.setText(student.getIdUser());
		wldp.setText(student.getYuanxi());
		wlcalss.setText(student.getBanji());
		wlfd.setText(student.getFudaoyuan());
//		paiming.setText("学校排名第" + student.getPaiming() + "名");
		if (bitmap != null) {
			im.setImageBitmap(bitmap);
		}
		/*try {
			if ("1".equals(student.getStats()) && Integer.parseInt(student.getPaiming()) > 0) {
				lshare.setVisibility(View.VISIBLE);
			} else {
				lshare.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setValues();
				if (adapter == null) {
					adapter = new ProcessAdapter(WelDoActivity.this, linkds,idUser);
					listView.setAdapter(adapter);
				} else {
					listView.setAdapter(adapter);
				}
				break;
			}
		};
	};
}
