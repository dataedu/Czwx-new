package com.dk.mp.ydqj.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.ydqj.R;
import com.dk.mp.ydqj.entity.Leave;

public class LeaveApprovalAdapter extends BaseAdapter {
	private Context context;
	private List<Leave> list;
	private boolean bool;

	public List<Leave> getList() {
		return list;
	}

	public void setList(List<Leave> list) {
		this.list = list;
	}

	public LeaveApprovalAdapter(Context context, List<Leave> list,boolean bool) {
		this.context = context;
		this.list = list;
		this.bool = bool;
	}

	public int getCount() {
		return list.size();
	}

	public Leave getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.app_myleave_list_item, parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.db_lx = (TextView) convertView.findViewById(R.id.db_lx);
		holder.bumen = (TextView) convertView.findViewById(R.id.bumen);
		holder.shijian = (TextView) convertView.findViewById(R.id.shijian);
		
		holder.title.setText(list.get(position).getQjlx());
		holder.bumen.setText(list.get(position).getSqr());
		holder.shijian.setText(list.get(position).getSqsj());
		return convertView;
	}

	/**
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class ViewHolder {
		TextView title, bumen, shijian,db_lx;
	}

}
