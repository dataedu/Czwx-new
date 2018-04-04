package com.dk.mp.wspj.view;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.wspj.R;
import com.dk.mp.wspj.entity.Pjzbx;

public class ChildenView extends LinearLayout{
	
	private LinearLayout linearLayout;//布局文件
	private LinearLayout selected;//布局文件
	private Context context;
	private Pjzbx obj;
	private boolean type;
	private TextView erzbmc;//二级指标名称
	
	public ChildenView(Context context,Pjzbx obj,boolean type) {
		super(context);
		linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.childen_view, this,false);
		this.obj = obj;
		this.type = type;
		this.context = context;
		initViews();
	}

	private void initViews(){
		erzbmc = (TextView) linearLayout.findViewById(R.id.erzbmc);
		erzbmc.setText(Html.fromHtml(obj.getZbmc()+"("+obj.getLhfz().intValue()+"分)"));
		selected = (LinearLayout) linearLayout.findViewById(R.id.selected);
		
		PfType.getPfType(obj, selected, context, type);
	}
	
	public LinearLayout getView(){
		return linearLayout;
	}
}
