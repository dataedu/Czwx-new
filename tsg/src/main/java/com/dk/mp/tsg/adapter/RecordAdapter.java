package com.dk.mp.tsg.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.tsg.R;
import com.dk.mp.tsg.entity.BookRecord;

public class RecordAdapter extends BaseAdapter {
	private Activity activity;
	private List<BookRecord> data;
	private LayoutInflater inflater;

	public List<BookRecord> getData() {
		return data;
	}

	public void setData(List<BookRecord> data) {
		this.data = data;
	}

	public RecordAdapter(Activity a, List<BookRecord> basicList) {
		activity = a;
		data = basicList;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public BookRecord getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.app_library_record_item, parent, false);
			holder.ghsj = (TextView) convertView.findViewById(R.id.ghsj);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.jssj = (TextView) convertView.findViewById(R.id.jssj);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(data.get(position).getName());
		holder.jssj.setText(data.get(position).getJssj());
		holder.ghsj.setText(data.get(position).getYjhssj());
		return convertView;
	}

	public static class ViewHolder {
		TextView name, jssj,ghsj;
	}

}
