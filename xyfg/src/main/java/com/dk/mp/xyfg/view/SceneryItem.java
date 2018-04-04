package com.dk.mp.xyfg.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.mp.xyfg.R;
import com.dk.mp.xyfg.SceneryListActivity;
import com.dk.mp.xyfg.entity.SceneryEntity;

import java.util.List;

public class SceneryItem{

public static LinearLayout getViews(final Context context, List<SceneryEntity> list){
		
		LinearLayout line = new LinearLayout(context);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		line.setOrientation(LinearLayout.VERTICAL);
		line.setLayoutParams(param);
		
		for(final SceneryEntity entity : list){
			View view = LayoutInflater.from(context).inflate(R.layout.app_scenery_item,null);
			ImageView sceneryimage = (ImageView) view.findViewById(R.id.sceneryimage);
			TextView scenerytext = (TextView) view.findViewById(R.id.scenerytext);
			
//			sceneryimage.setImageURI(Uri.parse(entity.getImage()));
			Log.e("图片展示", entity.getImage().toString());
			scenerytext.setText(entity.getName());
			
			Glide.with(context).load(entity.getImage()).placeholder(R.drawable.moren).into(sceneryimage);

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,SceneryListActivity.class);
					intent.putExtra("type", entity.getId());
					intent.putExtra("title", entity.getName());
					context.startActivity(intent);
				}
			});

			view.setTag(entity);
			line.addView(view);
		}
		
		return line;
	}

}
