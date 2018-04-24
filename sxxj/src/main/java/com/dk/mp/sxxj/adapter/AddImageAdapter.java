package com.dk.mp.sxxj.adapter;

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
import com.dk.mp.sxxj.R;

import com.dk.mp.sxxj.ui.SxxjSaveActivity;
import com.lidroid.xutils.BitmapUtils;

public class AddImageAdapter extends BaseAdapter{
	private SxxjSaveActivity activity;
    private Context mContext;
    private List<String> mData;
    private LayoutInflater inflater;
    private BitmapUtils utils;
	
    public AddImageAdapter(Context context, SxxjSaveActivity activity, List<String> basicList) {
        this.mContext = context;
        this.mData = basicList;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
        mData.add("addImage");
    }

	private class MyView {
		private ImageView img;
        private ImageButton delete;
	}
	
	
	public List<String> getmData() {
		return mData;
	}

	public void setmData(List<String> mData) {
		this.mData = mData;
		if(mData.size()==6){
			mData.remove(5);
		}else{
			if(!mData.contains("addImage")){
				mData.add("addImage");
			}
		}
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
			convertView = inflater.inflate(R.layout.app_wsjc_img, parent,false);// 设置要转化的layout文件
			mv.img = (ImageView) convertView.findViewById(R.id.img);
			mv.delete = (ImageButton) convertView.findViewById(R.id.delete);
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		String fjId = mData.get(position);
        if("addImage".equals(fjId)){
            mv.img.setImageResource(R.drawable.addfile);
            mv.delete.setVisibility(View.GONE);
        }else{
//            Glide.with(mContext).load(imgurl).fitCenter().into(mv.img);
            utils.display(mv.img,getImaheUrl(fjId));
            mv.delete.setVisibility(View.VISIBLE);
        }
		
        mv.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activity.remove(position);
            }
        });

        mv.img.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
           if("addImage".equals(mData.get(position))){//添加按钮
               activity.ablum();
           }else{//预览图片
//               Intent intent = new Intent(mContext, ImagePreviewActivity.class);
//               intent.putExtra("index", 0);
//               intent.putStringArrayListExtra("list", (ArrayList<String>) mData);
//               mContext.startActivity(intent);
           }
           }
       });
		return convertView;
	}


	private String getImaheUrl(String fjId){
    	return "https://csdnimg.cn/pubfooter/images/csdn_cs_qr.png";
	}

}
