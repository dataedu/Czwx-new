package com.dk.mp.xxgk.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.xxgk.R;
import com.dk.mp.xxgk.entity.Depart;

/**
 * 院系适配器.
 * 
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
public class DepartAdapter extends BaseAdapter {
	private Context context;
	private List<Depart> list;
	private LayoutInflater lif;

	public List<Depart> getList() {
		return list;
	}

	public void setList(List<Depart> list) {
		this.list = list;
	}

	public DepartAdapter(Context context, List<Depart> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.app_intro_college_item, null);// 设置要转化的layout文件
		} else {
			mv = (MyView) convertView.getTag();
		}
		final Depart depart = list.get(position);
		mv.name = (TextView) convertView.findViewById(R.id.name);// 取得实例
		// mv.tips = (TextView) convertView.findViewById(R.id.txt_tip);// 取得实例

		mv.name.setText(depart.getName());

		convertView.setTag(mv);
		return convertView;
	}

	private static class MyView {
		private TextView name;
	}
}
