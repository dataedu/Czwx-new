package com.dk.mp.apps.gzbxnew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.apps.gzbxnew.entity.GzbxDetail;
import com.dk.mp.apps.gzbxnew.http.HttpUtil;

import com.dk.mp.core.entity.LoginMsg;

import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpClientUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.Logger;

import com.dk.mp.core.util.StringUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

/**
 * 故障报修
 * @author admin
 *
 */
public class FaultRepairApplyDetailActivity extends MyActivity {
	private TextView bxdh,bxrq,bxdd,bxr,bxbm,bxlx,bxnr,czhj,czz,czsj,czyj,wxlb,wxry,bxrlxdh;
	private Gzbx gzbx;
	GzbxDetail detail;
	private ImageView czjg;//操作结果
	private LinearLayout layout_bottom,czjg_lin;
	private Button ok;
	public static FaultRepairApplyDetailActivity instance;
	private BitmapUtils utils;
	private ImageView img_detail;//报修图片
	private Context mContext;
	List<String> imgs = new ArrayList<String>();

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_apply_detail;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fault_repair_apply_detail);
//		setTitle("故障报修");
//		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
//		mContext = FaultRepairApplyDetailActivity.this;
//		initView();
//		getDetail();
//		instance = this;
//
//		img_detail.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, ImagePreviewActivity.class);
//				intent.putExtra("index", 0);
//				intent.putStringArrayListExtra("list", (ArrayList<String>) imgs);
//				startActivity(intent);
//			}
//		});
//	}

	protected void initView(){
		setTitle("故障报修");
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
		mContext = FaultRepairApplyDetailActivity.this;

		utils = new BitmapUtils(mContext);
		img_detail = (ImageView) findViewById(R.id.img_detail);
		layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
		czjg_lin = (LinearLayout) findViewById(R.id.czjg_lin);
		bxdh = (TextView) findViewById(R.id.bxdh);
		bxrq = (TextView) findViewById(R.id.bxrq);
		bxdd = (TextView) findViewById(R.id.bxdd);
		bxr = (TextView) findViewById(R.id.bxr);
		bxbm = (TextView) findViewById(R.id.bxbm);
		bxlx = (TextView) findViewById(R.id.bxlx);
		bxnr = (TextView) findViewById(R.id.bxnr);
		wxlb = (TextView) findViewById(R.id.wxlb);
		wxry = (TextView) findViewById(R.id.wxry);
		czhj = (TextView) findViewById(R.id.czhj);
		czz = (TextView) findViewById(R.id.czz);
		czsj = (TextView) findViewById(R.id.czsj);
		czyj = (TextView) findViewById(R.id.czyj);
		czjg = (ImageView) findViewById(R.id.czjg);
		bxrlxdh = (TextView) findViewById(R.id.bxrlxdh);
		ok = (Button) findViewById(R.id.ok);

		bxdh.setText(gzbx.getBxdh());
		bxrq.setText(gzbx.getTime());
		bxdd.setText(gzbx.getBxdd());
		bxr.setText(gzbx.getName());
		bxbm.setText(gzbx.getBxbm());
		bxlx.setText(gzbx.getType());
		bxnr.setText(gzbx.getBxnr());
		wxlb.setText(gzbx.getWxlx());
		wxry.setText(gzbx.getWxry());
//		 LoginMsg loginMsg = new CoreSharedPreferencesHelper(mContext).getLoginMsg();
//		String url = getReString(R.string.uploadUrl)+"/attachmentDownload.portal?notUseCache=true&type=xyZp&ownerId="+gzbx.getBxdh()+"&userId="+loginMsg.getUid()+"&password="+ loginMsg.getPsw();
//		Logger.info("imgUrl="+url);
		layout_bottom.setVisibility(View.GONE);
		czjg_lin.setVisibility(View.VISIBLE);
		if("已审核".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shtg_repair);
		}else if("不通过".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shbtg_repair);
		}else if("草稿".equals(gzbx.getShzt()) || "退回".equals(gzbx.getShzt())){//南交特有
			czjg.setImageResource(R.drawable.th_repair);
			layout_bottom.setVisibility(View.VISIBLE);
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(FaultRepairApplyDetailActivity.this,FaultRepairAddActivity.class);
					gzbx.setBxrlxdh(detail.getBxrlxdh());
					intent.putExtra("gzbx", gzbx);
					startActivity(intent);
				}
			});
		}else if("审核中".equals(gzbx.getShzt())){
			czjg.setImageResource(R.drawable.shz_repair);
			czjg_lin.setVisibility(View.GONE);
		}

		getDetail();
		instance = this;

		img_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImagePreviewActivity.class);
				intent.putExtra("index", 0);
				intent.putStringArrayListExtra("list", (ArrayList<String>) imgs);
				startActivity(intent);
			}
		});
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
			public void onSuccess(JSONObject result) {
				hideProgressDialog();
				detail = HttpUtil.getDetail(result);
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
					bxrlxdh.setText(detail.getBxrlxdh());
					if(StringUtils.isNotEmpty(detail.getFj())){
						img_detail.setVisibility(View.VISIBLE);
						utils.display(img_detail,detail.getFj());
						gzbx.setFj(detail.getFj());
						imgs.clear();
						imgs.add(detail.getFj());
					}else{
						img_detail.setVisibility(View.GONE);
					}

					break;
				default:
					break;
			}
		};
	};
}
