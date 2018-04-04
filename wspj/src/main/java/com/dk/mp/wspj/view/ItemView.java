package com.dk.mp.wspj.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.core.util.StringUtils;
import com.dk.mp.wspj.R;
import com.dk.mp.wspj.entity.Pjzbx;

public class ItemView extends LinearLayout{

	private LinearLayout linearLayout;//布局文件
	private LinearLayout linearLayout2;//布局文件
	private LinearLayout linearLayout3;//布局文件
	private Context context;
	private Pjzbx obj;
	private boolean type;
	private TextView zbmc;//指标名称
	private View titleview;
	
	public ItemView(Context context,Pjzbx obj,boolean type) {
		super(context);
		linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_view,this,true);
		this.obj = obj;
		this.type = type;
		this.context = context;
		initViews();
	}
	
	private void initViews(){
		zbmc = (TextView) linearLayout.findViewById(R.id.zbmc);
		zbmc.setText(Html.fromHtml(obj.getZbmc()+"("+obj.getLhfz().intValue()+"分)"));
		linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.childen);
		linearLayout3 = (LinearLayout) linearLayout.findViewById(R.id.selectline);
		titleview = linearLayout.findViewById(R.id.titleview);
		if(obj.getEjzbList().size() == 0){//只有大项
			PfType.getPfType(obj, linearLayout3, context, type);
			titleview.setVisibility(View.GONE);
		}else{//存在小项
			linearLayout3.setVisibility(View.GONE);
			for(int i = 0;i<obj.getEjzbList().size();i++){
				Pjzbx p = obj.getEjzbList().get(i);
				ChildenView childen = new ChildenView(context, p, type);
				linearLayout2.addView(childen.getView());
				//循环加入横线
				if(i != obj.getEjzbList().size()-1){
					View view = new View(context);
					LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.dip2px(context, (float) 0.8));
					view.setLayoutParams(params);
					view.setBackgroundColor(Color.rgb(201, 201, 201));
					linearLayout2.addView(view);
				}
			}
		}
	}
}
