package com.dk.mp.ksap.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.ksap.R;
import com.dk.mp.ksap.entity.Ksap;
import com.dk.mp.ksap.ui.KsapDetailActivity;
import com.dk.mp.ksap.widget.ChildLiistView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018-4-18.
 */

public class KsapAdapter  extends BaseAdapter {


    private Context context;
    private List<Ksap> list=new ArrayList<Ksap>();
    private LayoutInflater lif;

    public List<Ksap> getList() {
        return list;
    }

    public void setList(List<Ksap> list) {
        this.list = list;
    }

    /**
     * 构�?方法.
     * @param context Context
     * @param list List<Person>
     */
    public KsapAdapter(Context context, List<Ksap> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyView mv;
        if (convertView == null) {
            mv = new MyView();
            lif = LayoutInflater.from(context);// 转化到context这个容器
            convertView = lif.inflate(R.layout.app_ksap_item, null);// 设置要转化的layout文件
            convertView.setTag(mv);
        } else {
            mv = (MyView) convertView.getTag();
        }
        mv.icon = (ImageView) convertView.findViewById(R.id.icon);// 取得实例
        mv.ksrq = (TextView) convertView.findViewById(R.id.ksrq);// 取得实例
        mv.kclist = (ChildLiistView) convertView.findViewById(R.id.kclist);// 取得实例
        mv.ksrq.setText(list.get(position).getDate());
        mv.kclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                Intent intent = new Intent(context, KsapDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("kskc", list.get(position).getKslist().get(index));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        mv.kclist.setAdapter(new ItemAdapter(context,list.get(position).getKslist()));




//		convertView.setTag(examInfo.getIdDepart());
        return convertView;
    }

    /**
     * .
     * @version 2013-3-21
     * @author wangw
     */
    private static class MyView {
        private ImageView icon;
        private TextView ksrq;
        private ChildLiistView kclist;
    }
}
