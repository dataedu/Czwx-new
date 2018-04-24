package com.dk.mp.sxxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dk.mp.sxxj.R;
import com.dk.mp.sxxj.entity.SxxjList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018-4-18.
 */

public class SxxjAdapter extends BaseAdapter {


    private Context context;
    private List<SxxjList> list=new ArrayList<SxxjList>();
    private LayoutInflater lif;

    public List<SxxjList> getList() {
        return list;
    }

    public void setList(List<SxxjList> list) {
        this.list = list;
    }

    /**
     * 构�?方法.
     * @param context Context
     * @param list List<Person>
     */
    public SxxjAdapter(Context context, List<SxxjList> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyView mv;
        if (convertView == null) {
            mv = new MyView();
            lif = LayoutInflater.from(context);// 转化到context这个容器
            convertView = lif.inflate(R.layout.app_sxxj_item, null);// 设置要转化的layout文件
            convertView.setTag(mv);
        } else {
            mv = (MyView) convertView.getTag();
        }
        mv.img = (ImageView) convertView.findViewById(R.id.img);// 取得实例
        mv.riqi = (TextView) convertView.findViewById(R.id.riqi);// 取得实例
        mv.zt = (TextView) convertView.findViewById(R.id.zt);// 取得实例
        mv.lx = (TextView) convertView.findViewById(R.id.lx);// 取得实例
        mv.riqi.setText(list.get(position).getDate());
        mv.zt.setText(list.get(position).getStatus());
        mv.lx.setText(list.get(position).getType());


//		convertView.setTag(examInfo.getIdDepart());
        return convertView;
    }

    /**
     * .
     * @version 2013-3-21
     * @author wangw
     */
    private static class MyView {
        private ImageView img;
        private TextView riqi,zt,lx;
    }
}
