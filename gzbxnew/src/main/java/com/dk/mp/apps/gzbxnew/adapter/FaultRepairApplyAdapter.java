package com.dk.mp.apps.gzbxnew.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.FaultRepairApplyDetailActivity;
import com.dk.mp.apps.gzbxnew.FaultRepairAuditingDetailActivity;
import com.dk.mp.apps.gzbxnew.FaultRepairCommentOnWpDetailActivity;
import com.dk.mp.apps.gzbxnew.FaultRepairCommentOnYpDetailActivity;
import com.dk.mp.apps.gzbxnew.FaultRepairDetailActivity;
import com.dk.mp.apps.gzbxnew.R;
import com.dk.mp.apps.gzbxnew.entity.Gzbx;

/**
 * 报修申请页适配器
 * @author admin
 *
 */
public class FaultRepairApplyAdapter extends BaseAdapter{
	private Context mContext;
	private List<Gzbx> mData;
	private LayoutInflater mInflater;
	private String type; //1,评价
	private String category;//2,已办
	private String pjzt; //wpj,ypj
	
	public FaultRepairApplyAdapter(Context mContext, List<Gzbx> mData,String type,String pjzt,String category) {
		super();
		this.mContext = mContext;
		this.mData = mData;
		this.type = type;
		this.pjzt = pjzt;
		this.category = category;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setmData(List<Gzbx> mData) {
		this.mData = mData;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyView mView = null;
		if(convertView == null){
			mView = new MyView();
			convertView = mInflater.inflate(R.layout.fault_repair_apply_item, parent,false);
			convertView.setTag(mView);
		}else{
			mView = (MyView) convertView.getTag();
		}
		mView.bxzt = (ImageView) convertView.findViewById(R.id.bxzt);
		mView.bxdh = (TextView) convertView.findViewById(R.id.bxdh);
		mView.bxrq = (TextView) convertView.findViewById(R.id.bxrq);
		mView.bxdd = (TextView) convertView.findViewById(R.id.bxdd);
		mView.bxr = (TextView) convertView.findViewById(R.id.bxr);
		mView.bxbm = (TextView) convertView.findViewById(R.id.bxbm);
		mView.bxlx = (TextView) convertView.findViewById(R.id.bxlx);
		mView.bxnr = (TextView) convertView.findViewById(R.id.bxnr);
		mView.ckxq = (RelativeLayout) convertView.findViewById(R.id.ckxq);
		mView.bxbm_lin = (RelativeLayout) convertView.findViewById(R.id.bxbm_lin);
		mView.ztm = (TextView) convertView.findViewById(R.id.ztm);
		
		final Gzbx gzbx = mData.get(position);
		mView.bxdh.setText(gzbx.getBxdh());
		mView.bxrq.setText(gzbx.getTime());
		mView.bxdd.setText(gzbx.getBxdd());
		mView.bxr.setText(gzbx.getName());
		
		mView.bxlx.setText(gzbx.getType());
		mView.bxnr.setText(gzbx.getBxnr());
		
		if("1".equals(type)){
			mView.bxbm_lin.setVisibility(View.GONE);
			mView.ztm.setText("评价状态");
			if("wpj".endsWith(pjzt)){
				mView.bxzt.setImageResource(R.drawable.wpj_repair);
				mView.ckxq.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,FaultRepairCommentOnWpDetailActivity.class);
						intent.putExtra("gzbxs", gzbx);
						mContext.startActivity(intent);
					}
				});
			}else{
				mView.bxzt.setImageResource(R.drawable.ypj_repair);
				mView.ckxq.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,FaultRepairCommentOnYpDetailActivity.class);
						intent.putExtra("gzbxs", gzbx);
						mContext.startActivity(intent);
					}
				});
			}
		}else{
			mView.ztm.setText("审核状态");
			mView.bxbm_lin.setVisibility(View.VISIBLE);
			mView.bxbm.setText(gzbx.getBxbm());
			mView.bxzt.setVisibility(View.VISIBLE);
			if("已审核".equals(gzbx.getShzt())){
				mView.bxzt.setImageResource(R.drawable.shtg_repair);
			}else if("不通过".equals(gzbx.getShzt())){
				mView.bxzt.setImageResource(R.drawable.shbtg_repair);
			}else if("草稿".equals(gzbx.getShzt()) || "退回".equals(gzbx.getShzt())){//南交特有
				mView.bxzt.setImageResource(R.drawable.th_repair);
			}else if("审核中".equals(gzbx.getShzt())){
				mView.bxzt.setImageResource(R.drawable.shz_repair);
			}else{
				mView.bxzt.setVisibility(View.GONE);
			}
			
			mView.ckxq.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if("2".equals(type) || "2".equals(category)){
						Intent intent = new Intent(mContext,FaultRepairApplyDetailActivity.class);
						intent.putExtra("gzbxs", gzbx);
						mContext.startActivity(intent);
					}else{
						Intent intent = new Intent(mContext,FaultRepairAuditingDetailActivity.class);
						intent.putExtra("gzbxs", gzbx);
						mContext.startActivity(intent);
					}
				}
			});
		}
		
		return convertView;
	}

	private static class MyView{
		private ImageView bxzt;
		private TextView bxdh,bxrq,bxdd,bxr,bxbm,bxlx,bxnr,ztm;
		private RelativeLayout ckxq,bxbm_lin;
	}
}
