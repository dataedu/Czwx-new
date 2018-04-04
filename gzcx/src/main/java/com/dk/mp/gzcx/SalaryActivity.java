package com.dk.mp.gzcx;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.gzcx.adapter.SalaryAdapter;
import com.dk.mp.gzcx.entity.Salary;

import java.util.ArrayList;
import java.util.List;

/**
 * 工资查询
 * @author dake
 *
 */
public class SalaryActivity extends MyActivity implements OnItemClickListener{
	private ListView listView;
	private SalaryAdapter sAdapter;
	private Context mContext;

	@Override
	protected int getLayoutID() {
		return R.layout.app_salary;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle(getIntent().getStringExtra("title"));
		mContext = SalaryActivity.this;
		findView();
	}

	public void findView(){
		listView = (ListView) findViewById(R.id.listView);
		List<Salary> list = new ArrayList<Salary>();
		
		for(int i=0;i<=11;i++){//显示12个月的工资查询
			Salary s = new Salary();
			String rq = TimeUtils.getBeforeMonth(i);
			s.setTitle(rq.replace("-", "年")+"月工资账单");
			s.setRq(rq);
			list.add(s);
		}
		sAdapter = new SalaryAdapter(mContext, list);
		listView.setAdapter(sAdapter);
		listView.setFooterDividersEnabled(false);
		listView.setHeaderDividersEnabled(false);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(mContext,SalaryDetailActivity.class);//跳转工资详情
		Salary salary = sAdapter.getItem(position);
		intent.putExtra("month",salary.getRq());
		startActivity(intent);
	}
}
