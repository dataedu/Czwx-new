package com.dk.mp.gzcx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.gzcx.R;
import com.dk.mp.gzcx.entity.Salary;

import java.util.ArrayList;
import java.util.List;

public class SalaryAdapter extends BaseAdapter {

	private Context context;
	private List<Salary> list = new ArrayList<Salary>();
	private LayoutInflater lif;

	public List<Salary> getList() {
		return list;
	}

	public void setList(List<Salary> list) {
		this.list = list;
	}

	/**
	 * 构�?方法.
	 * @param context Context
	 * @param list List<Person>
	 */
	public SalaryAdapter(Context context, List<Salary> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Salary getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.app_salary_item, null);// 设置要转化的layout文件
		} else {
			mv = (MyView) convertView.getTag();
		}
		final Salary examInfo = list.get(position);

		mv.name = (TextView) convertView.findViewById(R.id.name);// 取得实例
		mv.name.setText(examInfo.getTitle());
		convertView.setTag(mv);
		return convertView;
	}

	private static class MyView {
		private TextView name;
	}
}
