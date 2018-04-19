package com.dk.mp.ksap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.ksap.R;
import com.dk.mp.ksap.entity.Ksap;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        mv.kclist = (LinearLayout) convertView.findViewById(R.id.kclist);// 取得实例
        mv.ksrq.setText(list.get(position).getDate());

        for(int i=0;i<list.get(position).getList().size();i++){
            View  subview = lif.inflate(R.layout.app_kskc_item, null);
            TextView kssj= (TextView) subview.findViewById(R.id.kssj);// 取得实例
            TextView kcmc= (TextView) subview.findViewById(R.id.kcmc);// 取得实例
            TextView ksdd= (TextView) subview.findViewById(R.id.ksdd);// 取得实例
            kssj.setText(list.get(position).getList().get(i).getKssj());
            kcmc.setText(list.get(position).getList().get(i).getKcmc());
            ksdd.setText(list.get(position).getList().get(i).getKsdd());
            mv.kclist.addView(subview);
        }



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
        private LinearLayout kclist;
    }
}
