package com.dk.mp.tsg.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.tsg.R;
import com.dk.mp.tsg.entity.Book;


public class BookAdapter extends BaseAdapter {
	private Activity activity;
	private List<Book> data;
	private LayoutInflater inflater;

	public List<Book> getData() {
		return data;
	}

	public void setData(List<Book> data) {
		this.data = data;
	}

	public BookAdapter(Activity a, List<Book> basicList) {
		activity = a;
		data = basicList;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Book getItem(int position) {
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
			convertView = inflater.inflate(R.layout.app_library_book_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.jssj = (TextView) convertView.findViewById(R.id.jssj);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(data.get(position).getName());
		holder.jssj.setText(data.get(position).getStock()+"本");
		return convertView;
	}

	public static class ViewHolder {
		TextView name, jssj;
	}

}
