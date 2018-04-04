package com.dk.mp.xyfg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dk.mp.xyfg.ImageUtil;
import com.dk.mp.xyfg.R;
import com.dk.mp.xyfg.SceneryDetailsActivity;
import com.dk.mp.xyfg.entity.SceneryEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SceneryGridAdapter extends RecyclerView.Adapter<SceneryGridAdapter.OneViewHolder>{
/*	private Context context;
	private List<SceneryEntity> list=new ArrayList<SceneryEntity>();
	private LayoutInflater lif;
	
	public List<SceneryEntity> getList() {
		return list;
	}

	public void setList(List<SceneryEntity> list) {
		this.list = list;
	}
	
	private class MyView {
		private ImageView image;
	}
	
	public SceneryGridAdapter(Context context, List<SceneryEntity> list) {
		this.context = context;
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.app_scenery_listview_item, null);// 设置要转化的layout文件
			mv.image = (ImageView) convertView.findViewById(R.id.scenerygridimage);// 取得实例
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		
//		mv.image.setImageURI(Uri.parse(list.get(position).getThumb()));
		Glide.with(context).load(list.get(position).getThumb()).placeholder(R.color.transparent).into(mv.image);
		
		return convertView;
	}*/

	private List<SceneryEntity> dataList;
	private Context context;

	public SceneryGridAdapter(Context context, List<SceneryEntity> list) {
		this.context = context;
		this.dataList = list;
	}

	@Override
	public OneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.app_scenery_listview_item, parent, false));
	}
	@Override
	public void onBindViewHolder(final OneViewHolder holder, int position) {
		SceneryEntity sceneryEntity = dataList.get(position);

//		Glide.with(context).load(sceneryEntity.getThumb()).placeholder(R.color.transparent).crossFade().into(holder.ivImage);
		ImageUtil.loadIntoUseFitWidth(context, sceneryEntity.getThumb(), R.color.transparent, holder.ivImage);
	}
	@Override
	public int getItemCount() {
		return dataList.size();
	}

	public class OneViewHolder extends RecyclerView.ViewHolder {
		private ImageView ivImage;
		public OneViewHolder(View view) {
			super(view);
			ivImage = (ImageView) view.findViewById(R.id.scenerygridimage);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,SceneryDetailsActivity.class);
//					intent.putExtra("list", new Gson.toJson(dataList));
					Bundle bundle = new Bundle();
					bundle.putSerializable("list", (Serializable) dataList);
					bundle.putString("title", (getLayoutPosition()+1)+"/"+ dataList.size());
					bundle.putInt("index", getLayoutPosition());
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
		}
	}

}

