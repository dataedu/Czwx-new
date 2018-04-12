package com.dk.mp.apps.gzbxnew.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dk.mp.apps.gzbxnew.FaultRepairAddActivity;
import com.dk.mp.apps.gzbxnew.ImagePreviewActivity;
import com.dk.mp.apps.gzbxnew.R;
import com.lidroid.xutils.BitmapUtils;

public class FaultRepairImageAdapter extends BaseAdapter{
	private FaultRepairAddActivity activity;
    private Context mContext;
    private List<String> mData;
    private LayoutInflater inflater;
    private BitmapUtils utils;
	
    public FaultRepairImageAdapter(Context context, FaultRepairAddActivity activity,List<String> basicList) {
        this.mContext = context;
        this.mData = basicList;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
    }

	private class MyView {
		private ImageView img;
        private ImageButton delete;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			convertView = inflater.inflate(R.layout.app_repair_img, parent,false);// 设置要转化的layout文件
			mv.img = (ImageView) convertView.findViewById(R.id.img);
			mv.delete = (ImageButton) convertView.findViewById(R.id.delete);
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		String imgurl = mData.get(position);
        if("addImage".equals(imgurl)){
            mv.img.setImageResource(R.drawable.addfile);
            mv.delete.setVisibility(View.GONE);
        }else{
            utils.display(mv.img,imgurl);
            mv.delete.setVisibility(View.VISIBLE);
        }
		
        mv.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	mData.remove(position);
            	mData.add("addImage");
                notifyDataSetChanged();
                activity.clearImg();
            }
        });

        mv.img.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
           if("addImage".equals(mData.get(position))){//添加按钮
               activity.ablum();
           }else{//预览图片
               Intent intent = new Intent(mContext, ImagePreviewActivity.class);
               intent.putExtra("index", 0);
               intent.putStringArrayListExtra("list", (ArrayList<String>) mData);
               mContext.startActivity(intent);
           }
           }
       });
		return convertView;
	}
}
