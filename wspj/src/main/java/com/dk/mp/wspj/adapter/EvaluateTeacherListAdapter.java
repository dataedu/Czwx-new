package com.dk.mp.wspj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dk.mp.wspj.R;
import com.dk.mp.wspj.entity.TeacherEntity;
import com.dk.mp.wspj.view.DVHolder;

public class EvaluateTeacherListAdapter extends BaseAdapter {
	private Context context;                        //运行上下文   
	private List<TeacherEntity> listItems;    //商品信息集合

	/*public final class ItemView{                //自定义控件集合
		public TextView name;
		public TextView kcmc;
		public TextView status;
	}  
*/
	public EvaluateTeacherListAdapter(Context context, List<TeacherEntity> listItems){
		this.context = context;            
		this.listItems = listItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图   
		/*ItemView  listItemView = null;
		if (convertView == null) {   
			listItemView = new ItemView();    
			//获取list_item布局文件的视图   
			convertView = listContainer.inflate(R.layout.app_evaluate_teacher_item, null);
			//获取控件对象   
			listItemView.name = (TextView)convertView.findViewById(R.id.teachername);   
			listItemView.kcmc = (TextView)convertView.findViewById(R.id.kcmc);   
			listItemView.status = (TextView)convertView.findViewById(R.id.status);   
			//设置控件集到convertView   
			convertView.setTag(listItemView);   
		}else {   
			listItemView = (ItemView)convertView.getTag();   
		}   

		//设置文字和图片    
		listItemView.name.setText(listItems.get(position).getName());  
		listItemView.kcmc.setText(listItems.get(position).getKcmc());  
		listItemView.status.setText(listItems.get(position).getStatus());  
		return convertView;   */

		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.app_evaluate_teacher_item,null);
		}

		TextView name = DVHolder.get(convertView, R.id.teachername);
		TextView kcmc = DVHolder.get(convertView, R.id.kcmc);
		TextView status = DVHolder.get(convertView, R.id.status);

		TeacherEntity teacherEntity = listItems.get(position);
		name.setText(teacherEntity.getName());
		kcmc.setText(teacherEntity.getKcmc());
		status.setText(teacherEntity.getStatus());

		return convertView;
	}
	@Override
	public int getCount() {
		return listItems.size();   
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
}
