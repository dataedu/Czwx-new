package com.dk.mp.apps.gzbxnew;

import android.widget.TextView;

import com.dk.mp.apps.gzbxnew.entity.Gzbx;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;

/**
 * 故障报修已评价
 * @author admin
 *
 */
public class FaultRepairCommentOnYpDetailActivity extends MyActivity {
	private TextView bxdh,bxrq,wxry,bxr,bxdd,bxlx,bxnr,pjnr;
	private Gzbx gzbx;
	private RatingBarView rBar;

	@Override
	protected int getLayoutID() {
		return R.layout.fault_repair_commenton_yp;
	}

	@Override
	protected void initialize() {
		super.initialize();
		initView();
	}

	public void initView(){
		setTitle("故障报修");
		gzbx = (Gzbx) getIntent().getSerializableExtra("gzbxs");
		bxdh = (TextView) findViewById(R.id.bxdh);
		bxr = (TextView) findViewById(R.id.bxr);
		bxrq = (TextView) findViewById(R.id.bxrq);
		bxlx = (TextView) findViewById(R.id.bxlx);
		wxry = (TextView) findViewById(R.id.wxry);
		bxdd = (TextView) findViewById(R.id.bxdd);
		bxnr = (TextView) findViewById(R.id.bxnr);
		rBar = (RatingBarView) findViewById(R.id.ratingBar1);
		pjnr = (TextView) findViewById(R.id.pjnr);
		
		bxdh.setText(gzbx.getBxdh());
		bxr.setText(gzbx.getName());
		bxrq.setText(gzbx.getTime());
		bxlx.setText(gzbx.getType());
		wxry.setText(gzbx.getWxry());
		bxdd.setText(gzbx.getBxdd());
		bxnr.setText(gzbx.getBxnr());
		pjnr.setText(gzbx.getPjyj());
		
		rBar.setClickable(false);
		int pjdj = 0;
		if(StringUtils.isNotEmpty(gzbx.getPjdj())){
			pjdj = Integer.parseInt(gzbx.getPjdj());
		}
		rBar.setStar(pjdj, true);
	}
	
	
}
