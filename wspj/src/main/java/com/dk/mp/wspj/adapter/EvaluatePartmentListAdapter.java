package com.dk.mp.wspj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.wspj.R;
import com.dk.mp.wspj.entity.PartmentEntity;

public class EvaluatePartmentListAdapter extends BaseAdapter {
	private Context context;                        //运行上下文   
	private List<PartmentEntity> listItems;    //商品信息集合
	private LayoutInflater listContainer;           //视图容器 

	public final class ItemView{                //自定义控件集合     
		public TextView name;
	}  

	public EvaluatePartmentListAdapter(Context context, List<PartmentEntity> listItems){
		this.context = context;            
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
		this.listItems = listItems;  
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图   
		ItemView  listItemView = null;   
		if (convertView == null) {   
			listItemView = new ItemView();    
			//获取list_item布局文件的视图   
			convertView = listContainer.inflate(R.layout.app_evaluate_item, null);   
			//获取控件对象   
			listItemView.name = (TextView)convertView.findViewById(R.id.searchfinancialshowname);
			//设置控件集到convertView   
			convertView.setTag(listItemView);   
		}else {   
			listItemView = (ItemView)convertView.getTag();   
		}   

		//设置文字和图片    
		String name = listItems.get(position).getName();
		listItemView.name.setText(name);  
		return convertView;   
	}
	@Override
	public int getCount() {
		return listItems.size();   
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
}
