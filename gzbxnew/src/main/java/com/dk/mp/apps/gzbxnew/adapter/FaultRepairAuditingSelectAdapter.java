package com.dk.mp.apps.gzbxnew.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.R;
import com.dk.mp.apps.gzbxnew.entity.Bxlx;
import com.dk.mp.core.util.Logger;

/**
 * 选择报修类型
 * @author admin
 *
 */
@SuppressLint("UseSparseArrays") public class FaultRepairAuditingSelectAdapter  extends BaseAdapter {
	private Context context;
	private List<Bxlx> list;
	private LayoutInflater lif;
	private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

	public List<Bxlx> getList() {
		return list;
	}

	public void setList(List<Bxlx> list) {
		this.list = list;
	}
	
	public FaultRepairAuditingSelectAdapter(Context context, List<Bxlx> list) {
		this.context = context;
		this.list = list;
		// 初始化数据
		initDate(false);
	}

	public void notify(List<Bxlx> list) {
		this.list = list;
		notifyDataSetChanged();
		isSelected.clear();
		initDate(false);
	}

	public void clean() {
		isSelected.clear();
		initDate(false);
	}

	// 初始化isSelected的数据
	private void initDate(boolean bool) {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, bool);
		}
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.fault_repair_auditing_item,  parent,false);// 设置要转化的layout文件
			mv.name = (TextView) convertView.findViewById(R.id.name);
			mv.check = (CheckBox) convertView.findViewById(R.id.check);
		} else {
			mv = (MyView) convertView.getTag();
		}
		mv.name.setText(list.get(position).getName());
		mv.check.setChecked(getIsSelected().get(position));
		convertView.setTag(mv);
		return convertView;
	}

	public static class MyView {
		private TextView name;
		private CheckBox check;

		public CheckBox getCheckBox() {
			return check;
		}
	}

	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	public List<Integer> getChecked() {
		List<Integer> checkList = new ArrayList<Integer>();
		if (isSelected.size() > 0) {
			Object s[] = isSelected.keySet().toArray();
			for (int i = 0; i < s.length; i++) {
				if (isSelected.get(s[i])) {
					checkList.add((Integer) s[i]);
					Logger.info("checkList:" + i);
				}
			}
		}
		return checkList;
	}
}
