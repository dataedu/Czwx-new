package com.dk.mp.apps.gzbxnew;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.GzbxDetail;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 故障报修详情页
 * @author admin
 *
 */
public class FaultRepairDetailActivity extends MyActivity {
	private TextView bxdh,bxrq,bxdd,bxr,bxbm,bxlx,bxnr,czhj,czz,czsj,czyj;
	private Gzbx gzbx;
	GzbxDetail detail;
	private ImageView czjg;//操作结果

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_detail;
	}

	@Override
	protected void initialize() {
		super.initialize();
		initView();
	}

	public void initView(){
		setTitle("故障报修");
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
		bxdh = (TextView) findViewById(R.id.bxdh);
		bxrq = (TextView) findViewById(R.id.bxrq);
		bxdd = (TextView) findViewById(R.id.bxdd);
		bxr = (TextView) findViewById(R.id.bxr);
		bxbm = (TextView) findViewById(R.id.bxbm);
		bxlx = (TextView) findViewById(R.id.bxlx);
		bxnr = (TextView) findViewById(R.id.bxnr);
		czhj = (TextView) findViewById(R.id.czhj);
		czz = (TextView) findViewById(R.id.czz);
		czsj = (TextView) findViewById(R.id.czsj);
		czyj = (TextView) findViewById(R.id.czyj);
		czjg = (ImageView) findViewById(R.id.czjg);
		
		bxdh.setText(gzbx.getBxdh());
		bxrq.setText(gzbx.getTime());
		bxdd.setText(gzbx.getBxdd());
		bxr.setText(gzbx.getName());
		bxbm.setText(gzbx.getBxbm());
		bxlx.setText(gzbx.getType());
		bxnr.setText(gzbx.getBxnr());
		
		if("已审核".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shtg_repair);
		}else if("不通过".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shbtg_repair);
		}else if("草稿".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.th_repair);
		}else if("审核中".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shz_repair);
		}
		getDetail();
	}
	
	/**
	 * 获取操作结果
	 */
	public void getDetail(){
		showProgressDialog();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", gzbx.getId());

		com.dk.mp.core.http.HttpUtil.getInstance().postJsonObjectRequest("apps/gzbx/getDetail", map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject arg0) {
				hideProgressDialog();
				detail = HttpUtil.getDetail(arg0);
				if(detail != null)
					mHandler.sendEmptyMessage(1);
				else
					mHandler.sendEmptyMessage(0);
			}
			
			@Override
			public void onError(VolleyError error) {
				hideProgressDialog();
				showMessage(getString(R.string.data_fail));
			}
		});
	}
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			switch (msg.what) {
			case 0://没获取到数据
				showMessage(getString(R.string.nodata));
				break;
			case 1:
				czhj.setText(detail.getCzhj());
				czz.setText(detail.getCzz());
				czsj.setText(detail.getCzsj());
				czyj.setText(detail.getCzyj());
				break;
			default:
				break;
			}
		};
	};
}

