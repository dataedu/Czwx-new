package com.dk.mp.ydyx.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.mp.ydyx.R;

public class NewListRadioDialog {
	Context context;
	Dialog dlg;

	ListView listview;
	TextView title;
	Button cancel;
	RadioAdapter radioAdapter;
	
	int index;

	public NewListRadioDialog(Context context) {
		this.context = context;
		dlg = new Dialog(context, R.style.MyDialog);
		Window window = dlg.getWindow();
		window.setContentView(R.layout.core_dialog_radio);
		listview = (ListView) window.findViewById(R.id.listView);
		cancel = (Button) window.findViewById(R.id.cancel_btn);
		title = (TextView) window.findViewById(R.id.title);
		cancel.setText("确定");

	}

	public void show(String[] items, OnClickListener onClickListener) {
		radioAdapter = new RadioAdapter(context, items);
		listview.setAdapter(radioAdapter);
		dlg.show();

		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				index=arg2;
				radioAdapter.setSelectedPosition(arg2);
				radioAdapter.notifyDataSetInvalidated();
			}});
		
		
		cancel.setOnClickListener(onClickListener);
	}
	
	public int selected(){
		return index;
	}
	
	

	public void cancel() {
		if (dlg != null) {
			dlg.cancel();
		}
	}

	class RadioAdapter extends BaseAdapter {
		private Context context;
		private String[] list;
		private int selectedPosition;
		
        public void setSelectedPosition(int position) {  
            selectedPosition = position;  
        }  

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

		@SuppressLint("WrongViewCast") public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.core_dialog_radio_item, null);
			TextView textView = (TextView) view.findViewById(R.id.txt);
			textView.setText(list[position]);
//			textView.setHeight(StringUtils.dip2px(context, 40));
//			textView.setGravity(Gravity.CENTER_VERTICAL);
//			textView.setTextSize(18);
//			textView.setPadding(StringUtils.dip2px(context, 10), 0, 0, 0);
//			textView.setTextColor(context.getResources().getColor(R.color.listradio_item));
			
			if (selectedPosition == position) { 
				textView.setBackgroundResource(R.drawable.aaaa);
            } else { 
            	textView.setBackgroundResource(R.drawable.bb);
            }
			
			
			return view;
		}
	}
}