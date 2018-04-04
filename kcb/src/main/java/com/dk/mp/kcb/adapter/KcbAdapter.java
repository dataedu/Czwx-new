package com.dk.mp.kcb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.kcb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/10/24 11:25
 */
public class KcbAdapter extends BaseAdapter {

    private Context context;
    private List<String> list=new ArrayList<String>();
    private LayoutInflater lif;
    private int selectItem = -1;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }


    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }



    /**
     * 构造方法.
     * @param context Context
     * @param list List<Person>
     */
    public KcbAdapter(Context context, List<String> list, int selectItem) {
        this.context = context;
        this.list = list;
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyView mv;
        if (convertView == null) {
            mv = new MyView();
            lif = LayoutInflater.from(context);// 转化到context这个容器
            convertView = lif.inflate(R.layout.app_kbcx_item, null);// 设置要转化的layout文件
            convertView.setTag(mv);
        } else {
            mv = (MyView) convertView.getTag();
        }
        mv.name = (TextView) convertView.findViewById(R.id.name);// 取得实例
        mv.layout_bg = (RelativeLayout)convertView.findViewById(R.id.layout_bg);
        mv.name.setText(list.get(position));
        if (position == selectItem) {	//选中状态 高亮
            mv.layout_bg.setBackgroundResource(R.color.sylla_bg_color);
            mv.name.setTextColor(context.getResources().getColor(R.color.white));
        } else {	//正常状态
            mv.layout_bg.setBackgroundResource(R.color.white);
            mv.name.setTextColor(context.getResources().getColor(R.color.sylla_text_color));
        }
        return convertView;
    }

    private static class MyView {
        private TextView name;
        private RelativeLayout layout_bg;
    }
}
