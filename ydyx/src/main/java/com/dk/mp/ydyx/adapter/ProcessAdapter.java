package com.dk.mp.ydyx.adapter;

import java.util.List;

import android.annotation.SuppressLint;
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

import com.dk.mp.core.util.StringUtils;
import com.dk.mp.ydyx.R;
import com.dk.mp.ydyx.activity.WelMoreActivity;
import com.dk.mp.ydyx.entity.WelLinkd;

/**
 * 流程办理适配器.
 * 
 * @since
 * @version2015-3-19
 * @author rm.zhao
 */
public class ProcessAdapter extends BaseAdapter {
	private List<WelLinkd> list;
	private Context context;
	private LayoutInflater lif;
	private String idUser;

	public ProcessAdapter(Context context, List<WelLinkd> list,String idUser) {
		this.context = context;
		this.list = list;
		this.idUser = idUser;
		lif = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			convertView = lif.inflate(R.layout.app_wel_item, null);
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		final WelLinkd process = list.get(position);
		mv.name = (TextView) convertView.findViewById(R.id.name);// 流程名称
		mv.datail = (TextView) convertView.findViewById(R.id.xiangqing);// 详情
		mv.area = (TextView) convertView.findViewById(R.id.area);// 地点
		mv.tip = (TextView) convertView.findViewById(R.id.tip);// 前提
		mv.tishi = (TextView) convertView.findViewById(R.id.tishi);// 提示
		mv.leaveb = (RelativeLayout) convertView.findViewById(R.id.leaveb);

		mv.img = (ImageView) convertView.findViewById(R.id.status);
		mv.name.setText((position + 1) + "." + process.getName());
		if (StringUtils.isNotEmpty(process.getAddress())) {
			mv.datail.setText(StringUtils.checkEmpty(process.getTipDepart()));
		} else {
			mv.datail.setText("");
		}
		if (StringUtils.isNotEmpty(process.getAddress())) {
			mv.area.setText(StringUtils.checkEmpty(process.getAddress()));
		} else {
			mv.area.setText("");
		}
		mv.tishi.setText(StringUtils.checkEmpty(process.getTip()));
		mv.more = (ImageView) convertView.findViewById(R.id.more);

		if (process.getHaveSub() == true) {
			mv.more.setVisibility(View.VISIBLE);
			if ("0".equals(process.getStatus())) {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds3);
				mv.img.setImageResource(R.drawable.wel_do);
			} else if ("1".equals(process.getStatus())) {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds1);
				mv.img.setImageResource(R.drawable.wel_not);
			} else {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds2);
				mv.img.setImageResource(R.drawable.wel_cando);
			}
			mv.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("id", process.getLinkId());
					intent.putExtra("name", process.getName());
					intent.putExtra("tip", process.getTipDepart());
					intent.putExtra("cont", process.getContent());
					intent.putExtra("idUser", idUser);
					intent.setClass(context, WelMoreActivity.class);
					context.startActivity(intent);
				}
			});
		} else {
			mv.more.setVisibility(View.GONE);
			if ("0".equals(process.getStatus())) {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds6);
				mv.img.setImageResource(R.drawable.wel_do);
			} else if ("1".equals(process.getStatus())) {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds4);
				mv.img.setImageResource(R.drawable.wel_not);
			} else {
				mv.leaveb.setBackgroundResource(R.drawable.wel_backgrounds5);
				mv.img.setImageResource(R.drawable.wel_cando);
			}
		}

		if (StringUtils.isNotEmpty(process.getTip())) {
			mv.tishi.setVisibility(View.VISIBLE);
		} else {
			mv.tishi.setVisibility(View.GONE);
		}

		return convertView;
	}

	private static class MyView {
		private TextView name, datail, area, tip, tishi;
		private ImageView img, areas, more;
		private RelativeLayout leaveb;
	}

}
