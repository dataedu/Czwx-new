package com.dk.mp.tsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.listview.XListView;
import com.dk.mp.core.view.listview.XListView.IXListViewListener;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tsg.adapter.BookAdapter;
import com.dk.mp.tsg.adapter.RecordAdapter;
import com.dk.mp.tsg.entity.Book;
import com.dk.mp.tsg.entity.BookRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;


@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class LibrarySearchActivity extends MyActivity implements IXListViewListener{
	private BookAdapter mAdapter;
	private XListView mListView;
	private EditText searchKeywords;
	private List<Book> list = new ArrayList<>();
	private TextView cancle_search;
	int pageNo=1;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_library_query;
	}

	@Override
	protected void initialize() {
		super.initialize();

		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

		if (!DeviceUtil.checkNet()){
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}

		mListView = (XListView) findViewById(R.id.listView);
		mListView.hideHeader();
		mListView.hideFooter();
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		cancle_search = (TextView) findViewById(R.id.cancle_search);
		searchKeywords = (EditText) findViewById(R.id.search_Keywords);
		searchKeywords.setHint("搜索图书名称或者关键字");
		searchKeywords.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((actionId == 0 || actionId == 3) && event != null) {
					final String keywords = searchKeywords.getText().toString();
					Logger.info(keywords);
					if (StringUtils.isNotEmpty(keywords)) {
						if (DeviceUtil.checkNet()) {
							query();
						}else{
							mListView.setVisibility(View.GONE);
							errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
						}
					} else {
						showErrorMsg("请输入图书名称或者关键字");
					}
				}
				return false;
			}
		});
		cancle_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化列表.
	 * @return List<App>
	 */
	public void query() {
		list.clear();
		errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", searchKeywords.getText().toString());
		map.put("pageNo", pageNo+"");

		HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Book>>(){}, "apps/tsg/query", map, new HttpListener<PageMsg<Book>>() {
			@Override
			public void onSuccess(PageMsg<Book> result) {
				errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);

				if (result.getList() != null && result.getList().size()>0){
					list.addAll(result.getList());
					mListView.setVisibility(View.VISIBLE);
					if (mAdapter == null){
						mAdapter = new BookAdapter(LibrarySearchActivity.this,list);
						mListView.setAdapter(mAdapter);
					}else {
						mAdapter.setData(list);
						mAdapter.notifyDataSetChanged();
					}

					if(list.size()<20){
						mListView.hideFooter();
					}else {
						mListView.showFooter();
					}
				}else {
					if (pageNo>1){
						showErrorMsg("没有更多了");
					}else {
						errorLayout.setErrorType(ErrorLayout.SEARCHNODATA);
					}
				}

				mListView.stopLoadMore();
				mListView.stopRefresh();
			}

			@Override
			public void onError(VolleyError error) {
				mListView.stopLoadMore();
				mListView.stopRefresh();
				mListView.setVisibility(View.GONE);
				errorLayout.setErrorType(ErrorLayout.DATAFAIL);
			}
		});
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		pageNo++;
		if (DeviceUtil.checkNet()) {
			query();
		}
	}

	@Override
	public void stopLoad() {

	}

	@Override
	public void getList() {

	}

}
