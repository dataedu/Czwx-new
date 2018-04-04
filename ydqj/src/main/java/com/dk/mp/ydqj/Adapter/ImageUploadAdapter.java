package com.dk.mp.ydqj.Adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dk.mp.ydqj.R;
import com.dk.mp.ydqj.ui.AddLeaveActivity;
import com.dk.mp.ydqj.ui.ImagePreviewActivity;

public class ImageUploadAdapter extends BaseAdapter {

		private AddLeaveActivity activity;
		private Context context;
		private List<String> data;
		private LayoutInflater inflater;

		public List<String> getData() {
			return data;
		}

		public void setData(List<String> data) {
			this.data = data;
		}

		public ImageUploadAdapter(AddLeaveActivity activity, Context a, List<String> basicList) {
			this.activity = activity;
			this.context = a;
			data = basicList;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public String getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.img_upload, parent, false);
				holder.delete = (ImageView) convertView.findViewById(R.id.delete);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.fragment1 = (FrameLayout) convertView.findViewById(R.id.fragment1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			utils.display(holder.img, data.get(position));
			Glide.with(context).load(data.get(position)).into(holder.img);
			holder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					data.remove(position);
					if(data.size()<=0){
						activity.dealImage();
					}
					activity.showAddButton();
					notifyDataSetChanged();
				}
			});
			holder.fragment1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ImagePreviewActivity.class);
					intent.putExtra("index", position);
					intent.putStringArrayListExtra("list", (ArrayList<String>) data);
					context.startActivity(intent);
				}
			});
			return convertView;
		}

		/**
		 * 
		 */

		public static class ViewHolder {
			ImageView img, delete;
			FrameLayout fragment1;
		}
		
		/**
		* 加载本地图片
		* http://bbs.3gstdy.com
		* @param url
		* @return
		*/
		public static Bitmap getLoacalBitmap(String url) {
		     try {
		          FileInputStream fis = new FileInputStream(url);
		          return BitmapFactory.decodeStream(fis);
		     } catch (FileNotFoundException e) {
		          e.printStackTrace();
		          return null;
		     }
		}
		
}
