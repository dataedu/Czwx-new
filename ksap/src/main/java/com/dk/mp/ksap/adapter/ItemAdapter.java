package com.dk.mp.ksap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.ksap.R;
import com.dk.mp.ksap.entity.Ksap;
import com.dk.mp.ksap.entity.Kskc;
import com.dk.mp.ksap.widget.ChildLiistView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018-4-18.
 */

public class ItemAdapter extends BaseAdapter {


    private Context context;
    private List<Kskc> list=new ArrayList<Kskc>();
    private LayoutInflater lif;

    public List<Kskc> getList() {
        return list;
    }

    public void setList(List<Kskc> list) {
        this.list = list;
    }

    /**
     * 构�?方法.
     * @param context Context
     * @param list List<Person>
     */
    public ItemAdapter(Context context, List<Kskc> list) {
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
            convertView = lif.inflate(R.layout.app_kskc_item, null);// 设置要转化的layout文件
            convertView.setTag(mv);
        } else {
            mv = (MyView) convertView.getTag();
        }


        mv. kssj= (TextView) convertView.findViewById(R.id.kssj);// 取得实例
        mv. kcmc= (TextView) convertView.findViewById(R.id.kcmc);// 取得实例
        mv. ksdd= (TextView) convertView.findViewById(R.id.ksdd);// 取得实例

        mv.  kssj.setText(list.get(position).getKssj());
        mv. kcmc.setText(list.get(position).getKcmc());
        mv. ksdd.setText(list.get(position).getKsdd());





//		convertView.setTag(examInfo.getIdDepart());
        return convertView;
    }

    /**
     * .
     * @version 2013-3-21
     * @author wangw
     */
    private static class MyView {
        private TextView kssj,kcmc,ksdd;
    }
}
