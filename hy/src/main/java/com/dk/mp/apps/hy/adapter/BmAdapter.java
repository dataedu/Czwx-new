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
import com.dk.mp.apps.hy.entity.Bm;

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
public class BmAdapter extends BaseAdapter{

	private Context context;
	private List<Bm> list=new ArrayList<Bm>();
	private LayoutInflater lif;
	
	public List<Bm> getList() {
		return list;
	}

	public void setList(List<Bm> list) {
		this.list = list;
	}

	/**
	 * 构�?方法.
	 * @param context Context
	 * @param list List<Person>
	 */
	public BmAdapter(Context context, List<Bm> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
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
			convertView = lif.inflate(R.layout.app_yellowpage_item, null);// 设置要转化的layout文件
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		final Bm examInfo = list.get(position);
		mv.area = (TextView) convertView.findViewById(R.id.name);// 取得实例
		mv.area.setText(examInfo.getNameDepart());
//		convertView.setTag(examInfo.getIdDepart());
		return convertView;
	}

	/**
	 * .
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class MyView {
		private TextView area;
	}
}
