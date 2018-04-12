package com.dk.mp.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.dk.mp.core.R;

public class ListRadioDialog {
	Context context;
	Dialog dlg;

	ListView listview;
//	TextView title;
TextView cancel;
	RadioAdapter radioAdapter;

	public ListRadioDialog(Context context) {
		this.context = context;
		dlg = new Dialog(context, R.style.MyDialog);
		Window window = dlg.getWindow();
		window.setContentView(R.layout.core_dialog_list);
		listview = (ListView) window.findViewById(R.id.listView);
		cancel = (TextView) window.findViewById(R.id.cancel_btn);
//		title = (TextView) window.findViewById(R.id.title);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});

	}

	public void show(String[] items, OnItemClickListener itemClickListener) {
		radioAdapter = new RadioAdapter(context, items);
		listview.setAdapter(radioAdapter);
		dlg.show();

		listview.setOnItemClickListener(itemClickListener);
	}

	public void cancel() {
		if (dlg != null) {
			dlg.cancel();
		}
	}

	class RadioAdapter extends BaseAdapter {
		private Context context;
		private String[] list;

		public RadioAdapter(Context context, String[] list) {
			this.context = context;
			this.list = list;
		}

		public int getCount() {
			return list.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.core_dialog_list_item, null);
			TextView textView = (TextView) view.findViewById(R.id.txt);
			textView.setText(list[position]);
//			textView.setHeight(StringUtils.dip2px(context, 40));
//			textView.setGravity(Gravity.CENTER_VERTICAL);
//			textView.setTextSize(18);
//			textView.setPadding(StringUtils.dip2px(context, 10), 0, 0, 0);
//			textView.setTextColor(context.getResources().getColor(R.color.listradio_item));
//			textView.setBackgroundResource(R.drawable.mylist_item);
			return view;
		}
	}
}