package com.dk.mp.ykt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.ykt.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 通知适配器
 * @version 2013-3-21
 * @author wangw
 */
public class MonthAdapter  extends BaseAdapter {
	private Context context;
	private List<String> list=new ArrayList<String>();
	private LayoutInflater lif;
	
	/**
	 * 构造方法.
	 * @param context Context
	 */
	public MonthAdapter(Context context) {
		this.context = context;
		for(int i=0;i<6;i++){
			list.add(getBeforeMonth(i));
		}
	}
	
	
	/**
	 * 构造方法.
	 */
	public void clean() {
		list.clear();
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size();
	}

	public String getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.app_card_item, null);// 设置要转化的layout文件
			mv.title = (TextView) convertView.findViewById(R.id.title);// 取得实例
		} else {
			mv = (MyView) convertView.getTag();
		}
		
		mv.title.setText(list.get(position)+"一卡通消费明细");
		convertView.setTag(mv);
		return convertView;
	}

	private static class MyView {
		private TextView title;

	}
	
	
	/**
	 * 当前月的前n月.
	 * @param n 推前月份数
	 * @return 月份
	 */
	public static String getBeforeMonth(int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -n);
		String haha = sdf.format(calendar.getTime());
		return haha;
	}
	
}
