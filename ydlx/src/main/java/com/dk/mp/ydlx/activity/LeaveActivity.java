package com.dk.mp.ydlx.activity;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ydlx.R;
import com.dk.mp.ydlx.adapter.ProcessAdapter;
import com.dk.mp.ydlx.entity.LeaveLinkd;
import com.dk.mp.ydlx.entity.LeaveStudent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 离校手续.
 * 
 * @since
 * @version2015-3-25
 * @author rm.zhao
 */
public class LeaveActivity extends MyActivity{
	private ImageView im;// 头像
	private LeaveStudent student;// 学生实体类对象
	private TextView name, sno, lxpc, stime, etime,paiming;// 姓名、学号、离校批次、开始时间、结束时间、排名
	private ListView listView;// 离校流程
	private Bitmap bitmap;
	private List<LeaveLinkd> linkds;
	private ProcessAdapter adapter;
	private LinearLayout lshare;
	private Button share;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_leave;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(getIntent().getStringExtra("title"));
		findView();

		if (DeviceUtil.checkNet()) {
			getDate();
		}else{
//			setNoWorkNet();
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	// 初始化控件
	private void findView() {
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = lif.inflate(R.layout.app_leave_info, null);
		im = (ImageView) convertView.findViewById(R.id.iheader);
		name = (TextView) convertView.findViewById(R.id.name);
		sno = (TextView) convertView.findViewById(R.id.sno);
		lxpc = (TextView) convertView.findViewById(R.id.lxpc);
		stime = (TextView) convertView.findViewById(R.id.stime);
		etime = (TextView) convertView.findViewById(R.id.etime);
		listView = (ListView) findViewById(R.id.listv);
//		right = (ImageButton) findViewById(R.id.right);
		listView.addHeaderView(convertView);
//		right.setOnClickListener(this);
		lshare = (LinearLayout) findViewById(R.id.lshare);
		paiming = (TextView) findViewById(R.id.paiming);
		share = (Button) findViewById(R.id.share);
//		share.setOnClickListener(this);
	}

	/*@Override
	public void onClick(View v) {
		if (v == right) {
			Intent intent = new Intent();
			intent.setClass(LeaveActivity.this, RemarkActivity.class);
			intent.putExtra("id", student.getBatchid());
			Logger.info("" + student.getBatchid());
			startActivity(intent);
		}else if (v == share) {                                
			DeviceUtil.share(LeaveActivity.this, "离校办理成功", "我已成功办理移动离校，学校排名" + student.getPaiming() + "名");
		}
	}*/

	public void getDate() {
		HttpUtil.getInstance().postJsonObjectRequest("apps/leaveStuRest/getStuInfo", null, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result) {
				try {
					if (result.getInt("code") == 200){
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						student = new Gson().fromJson(result.getJSONObject("data").getJSONObject("info").toString(),LeaveStudent.class);
						linkds = new Gson().fromJson(result.getJSONObject("data").getJSONArray("process").toString(),new TypeToken<List<LeaveLinkd>>(){}.getType());
						if (StringUtils.isNotEmpty(student.getName())) {
							if ("null" != student.getPhoto() && !"".equals(student.getPhoto())
									&& null != student.getPhoto()) {
								bitmap = ImageUtil.getImage(student.getPhoto());
							}

							setValues();
							if (adapter == null) {
								adapter = new ProcessAdapter(LeaveActivity.this, linkds);
								listView.setAdapter(adapter);
							} else {
								listView.setAdapter(adapter);
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
	}

	public void setValues() {
		name.setText(student.getName());//姓名
		sno.setText(student.getIdUser());//学号
		lxpc.setText(student.getBatch());//离校批次
		stime.setText(student.getTimeStart());//离校开始时间
		etime.setText(student.getTimeEnd());//离校结束时间
//		paiming.setText(student.getPaiming());
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

}
