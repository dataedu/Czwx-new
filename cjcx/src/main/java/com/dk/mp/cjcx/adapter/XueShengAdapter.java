package com.dk.mp.cjcx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.cjcx.BaiJiListActivity;
import com.dk.mp.cjcx.CjDetailActivity;
import com.dk.mp.cjcx.R;
import com.dk.mp.cjcx.entity.ScoreInquiry;
import com.dk.mp.core.entity.User;
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;

import java.util.List;

/**
 * 作者：janabo on 2017/10/26 15:17
 */
public class XueShengAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<ScoreInquiry> mData;
    LayoutInflater inflater;
String xqid;
    public XueShengAdapter(Context mContext, List mData,String xqid) {
        this.mContext = mContext;
        this.mData = mData;
        this.xqid = xqid;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_scoreinquiry_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScoreInquiry j = mData.get(position);
        ((MyViewHolder) holder).name.setText(j.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(mContext,HttpWebActivity.class);//跳转工资详情
                        ScoreInquiry si = mData.get(getLayoutPosition());
                        intent.putExtra("url","apps/cjcx/detail?xueqiId="+xqid+"&userId="+si.getId());
                        intent.putExtra("title",si.getName());
                        mContext.startActivity(intent);
                }
            });
        }
    }
}
