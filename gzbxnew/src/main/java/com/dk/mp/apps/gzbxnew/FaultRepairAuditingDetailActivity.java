package com.dk.mp.apps.gzbxnew;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.core.ui.MyActivity;

/**
 * 代办详情页
 * @author admin
 *
 */
public class FaultRepairAuditingDetailActivity extends MyActivity implements OnClickListener{
	private Context mContext;
	private TextView bxdh;//报修单号
	private TextView content;//报修内容
	private TextView bxr;//报修人
	private TextView bxsj;//报修时间
	private LinearLayout pass,notpass,retract;//通过，不通过，退回
	private Gzbx gzbx;
	public static FaultRepairAuditingDetailActivity instance;
	private TextView txt_l,txt_d,txt_r;

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_auditing_detail;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fault_repair_auditing_detail);
//		setTitle("故障报修");
//		mContext = this;
//		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
//		initView();
//		instance = FaultRepairAuditingDetailActivity.this;
//	}

	public void initView(){
		setTitle("故障报修");
		mContext = this;
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
		instance = FaultRepairAuditingDetailActivity.this;
		bxdh = (TextView) findViewById(R.id.bxdh);
		content = (TextView) findViewById(R.id.content);
		bxr = (TextView) findViewById(R.id.bxr);
		bxsj = (TextView) findViewById(R.id.bxsj);
		pass = (LinearLayout) findViewById(R.id.pass);
		notpass = (LinearLayout) findViewById(R.id.notpass);
		retract = (LinearLayout) findViewById(R.id.retract);
		txt_l = (TextView) findViewById(R.id.txt_l);
		txt_d = (TextView) findViewById(R.id.txt_d);
		txt_r = (TextView) findViewById(R.id.txt_r);
		
		pass.setOnClickListener(this);
		notpass.setOnClickListener(this);
		retract.setOnClickListener(this);
		bxdh.setText("报修单号："+gzbx.getBxdh());
		content.setText(gzbx.getBxnr());
		bxr.setText(gzbx.getName());
		bxsj.setText(gzbx.getTime());
		
		txt_l.setText(gzbx.getType());
		txt_d.setText(gzbx.getBxdd());
		txt_r.setText(gzbx.getBxbm());
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this,FaultRepairAuditingSelectActivity.class);
		if(v.getId() == R.id.pass){
			intent.putExtra("title", "通过");
			intent.putExtra("type", "tg");
		}else if(v.getId() == R.id.notpass){
			intent.putExtra("title", "不通过");
			intent.putExtra("type", "btg");
		}else if(v.getId() == R.id.retract){
			intent.putExtra("title", "退回");
			intent.putExtra("type", "th");
		}
		intent.putExtra("gzbxid", gzbx.getId());
		startActivity(intent);
	}
	
}
