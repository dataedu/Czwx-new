package com.dk.mp.ykt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ykt.adapter.MonthAdapter;
import com.dk.mp.ykt.entity.CardInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * .
 * @version 2013-3-21
 * @author wangw
 */
public class CardMainActivity extends MyActivity implements OnItemClickListener {
	private Context context = CardMainActivity.this;
	private ListView listView;
	private MonthAdapter adapter;
	private TextView money, name, cardNo;
	private CoreSharedPreferencesHelper h;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.core_listview_nodevider;
	}

	@Override
	protected void initialize() {
		super.initialize();

		h = new CoreSharedPreferencesHelper(context);

		initViews();
		adapter = new MonthAdapter(context);
		listView.setAdapter(adapter);

		//退出账号记得清空数据
		if (h.getValue("card_money") != null) {
			money.setText(h.getValue("card_money") + "元");
			money.setTextSize(50);
			name.setText("持卡人：" + h.getUser().getUserName());
			cardNo.setText("卡号：" + h.getValue("card_no"));
		}
		getInfo();
	}

	/**
	 * 初始化控件.
	 */
	private void initViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setBackgroundColor(Color.WHITE);
		LayoutInflater inflater = LayoutInflater.from(this);
		View convertView = inflater.inflate(R.layout.app_card_main, null, false);
		listView.addHeaderView(convertView);

		Button back = (Button) convertView.findViewById(R.id.back);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("title"));
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		money = (TextView) convertView.findViewById(R.id.money);
		name = (TextView) convertView.findViewById(R.id.name);
		cardNo = (TextView) convertView.findViewById(R.id.cardNo);
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(context, CardMonthActivity.class);
		intent.putExtra("month", adapter.getItem(position-1));
		intent.putExtra("title", getIntent().getStringExtra("title"));
		intent.putExtra("cardNo",h.getValue("card_no"));
		startActivity(intent);
	}

	public void getInfo() {
		if (DeviceUtil.checkNet()) {
			errorLayout.setErrorType(ErrorLayout.LOADDATA);
			Map<String, Object> map = new HashMap<String, Object>();
			HttpUtil.getInstance().postJsonObjectRequest("apps/ykt/getInfo", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") != 200) {
//							errorLayout.setErrorType(ErrorLayout.DATAFAIL);
//							adapter.clean();
							datafail();
						}else{
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							if(result.getString("data").toString() != "null" && result.getString("data").toString() != null){
								CardInfo cardInfo = new Gson().fromJson(result.getJSONObject("data").toString(),CardInfo.class);
								if (cardInfo != null){
									if (cardInfo.getMoney() != null) {
										double f = Double.parseDouble(cardInfo.getMoney());
										BigDecimal bg = new BigDecimal(f);
										double f1 = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

										money.setText(f1 + "元");
										money.setTextSize(50);
										h.setValue("card_money", f1+"");
									}else {
										money.setText("__");
										money.setTextSize(12);
									}

									name.setText("持卡人：" + h.getUser().getUserName());
									cardNo.setText("卡号：" + cardInfo.getCardNo());

									h.setValue("card_no", cardInfo.getCardNo());
								}else {
									money.setText("__");
									money.setTextSize(12);
									name.setText("持卡人：" + "__");
									cardNo.setText("卡号：" + "__");

									errorLayout.setErrorType(ErrorLayout.NODATA);
									adapter.clean();
								}
							}else {
								errorLayout.setErrorType(ErrorLayout.NODATA);
								adapter.clean();
							}
						}
					}catch (Exception e){
						e.printStackTrace();
						datafail();
					}
				}

				@Override
				public void onError(VolleyError error) {
//					adapter.clean();
//					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					datafail();
				}
			});
		}else{
			if(h.getValue("card_money") != null){
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
				showMessage(getReString(R.string.net_no2));
			}else {
				adapter.clean();
				errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
			}

		}
	}

	private void datafail(){
		if(h.getValue("card_money") != null){
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			showMessage(getReString(R.string.data_fail));
		}else {
			adapter.clean();
			errorLayout.setErrorType(ErrorLayout.DATAFAIL);
		}
	}

}
