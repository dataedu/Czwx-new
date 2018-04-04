package com.dk.mp.xyfg;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xyfg.adapter.SceneryGridAdapter;
import com.dk.mp.xyfg.entity.SceneryEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneryListActivity extends MyActivity {
	
	private List<SceneryEntity> list = new ArrayList<SceneryEntity>();
	private RecyclerView scenerygridview;
	private SceneryGridAdapter adapter;

	private Gson gson = new Gson();

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_scenery_listview;
	}

	@Override
	protected void initView() {
		super.initView();

		setTitle(getIntent().getStringExtra("title"));

		initViews();

		if(DeviceUtil.checkNet()) {
			initDatas();
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	private void initViews(){
		scenerygridview = (RecyclerView) findViewById(R.id.scenerygridview);
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		adapter = new SceneryGridAdapter(this, list);
		scenerygridview.setHasFixedSize(true);
		scenerygridview.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));
		scenerygridview.setAdapter(adapter);

		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void initDatas(){
		errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String, Object> map = new HashMap<String, Object>();
		String idType = getIntent().getStringExtra("type");
		HttpUtil.getInstance().postJsonObjectRequest("apps/xyfg/getImageList?idType="+idType, map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result)  {
				try {
					if (result.getInt("code") != 200) {
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					}else{
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						list.clear();
						String json =  result.getJSONArray("data").toString();
						List<SceneryEntity> list1 = new Gson().fromJson(json,new TypeToken<List<SceneryEntity>>(){}.getType());
						list.addAll(list1);
						if (list.size() > 0){
							adapter.notifyDataSetChanged();
						}else {
							scenerygridview.setVisibility(View.GONE);
							errorLayout.setErrorType(ErrorLayout.NODATA);
						}
					}
				}catch (Exception e){
					e.printStackTrace();
					errorLayout.setErrorType(ErrorLayout.DATAFAIL);
				}
			}
			@Override
			public void onError(VolleyError error) {
				errorLayout.setErrorType(ErrorLayout.DATAFAIL);
			}
		});
	}

	/*@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,SceneryDetailsActivity.class);
		intent.putExtra("list", gson.toJson(list));
		intent.putExtra("title", (position+1)+"/"+list.size());
		intent.putExtra("index", position);
		startActivity(intent);
	}*/
}
