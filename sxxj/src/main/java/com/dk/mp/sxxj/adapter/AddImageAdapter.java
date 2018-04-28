package com.dk.mp.sxxj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.util.FileUtil;
import com.dk.mp.sxxj.R;

import com.dk.mp.sxxj.ui.SxxjSaveActivity;
import com.lidroid.xutils.BitmapUtils;

public class AddImageAdapter extends BaseAdapter{
	private SxxjSaveActivity activity;
    private Context mContext;
    private List<String> mData;
    private LayoutInflater inflater;
	
    public AddImageAdapter(Context context, SxxjSaveActivity activity, List<String> basicList) {
        this.mContext = context;
        this.mData = basicList;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
    }

	private class MyView {
		private TextView img;
        private ImageButton delete;
	}
	
	
	public List<String> getmData() {
		return mData;
	}

	public void setmData(List<String> mData) {
		this.mData = mData;
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
			convertView = inflater.inflate(R.layout.app_sxxj_fj_item, parent,false);// 设置要转化的layout文件
			mv.img = (TextView) convertView.findViewById(R.id.img);
			mv.delete = (ImageButton) convertView.findViewById(R.id.delete);
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}


            mv.img.setText(mData.get(position));

        mv.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activity.remove(position);
            }
        });

		mv.img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.open(position);
			}
		});

		return convertView;
	}




}
