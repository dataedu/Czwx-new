package com.dk.mp.apps.hy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.apps.hy.R;
import com.dk.mp.apps.hy.entity.Ks;

/**
 * .
 * @version 2013-3-21
 * @author wangw
 */
/**
 * .
 * @version 2013-3-21
 * @author wangw
 */
/**
 * .
 * @version 2013-3-21
 * @author wangw
 */
public class KsAdapter extends BaseAdapter {

	private Context context;
	private List<Ks> list = new ArrayList<Ks>();
	private LayoutInflater lif;

	public List<Ks> getList() {
		return list;
	}

	public void setList(List<Ks> list) {
		this.list = list;
	}

	/**
	 * 构�?方法.
	 * @param context Context
	 * @param list List<Person>
	 */
	public KsAdapter(Context context, List<Ks> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Ks getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
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
			convertView = lif.inflate(R.layout.app_yellowpage_ks_item, null);// 设置要转化的layout文件
		} else {
			mv = (MyView) convertView.getTag();
		}
		final Ks examInfo = list.get(position);

		mv.name = (TextView) convertView.findViewById(R.id.name);// 取得实例
		mv.name.setText(examInfo.getName());

		mv.tel = (TextView) convertView.findViewById(R.id.tel);// 取得实例
		mv.tel.setText(examInfo.getTels());
		convertView.setTag(mv);
		return convertView;
	}

	/**
	 * .
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class MyView {
		private TextView name;
		private TextView tel;
	}

}
