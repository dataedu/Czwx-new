package com.dk.mp.wspj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.wspj.R;
import com.dk.mp.wspj.entity.Pfbz;
import com.dk.mp.wspj.entity.Pjzbx;

public class PfType {
    @SuppressLint("NewApi")
    public static void getPfType(final Pjzbx obj, LinearLayout selected, final Context context,
								 boolean type) {
        if (obj.getPfbzList().size() == 0) {//输入分数
            final EditText editview = new EditText(context);
            editview.setHint("请输入分值");
            editview.setBackground(context.getResources().getDrawable(R.drawable.editbackground));
            editview.setTextSize(12);
            editview.setTextColor(Color.rgb(33, 33, 33));
            editview.setPadding(StringUtils.dip2px(context, 10), StringUtils.dip2px(context, 10),
					StringUtils.dip2px(context, 10), StringUtils.dip2px(context, 10));
            editview.setInputType(InputType.TYPE_CLASS_NUMBER);
            editview.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {//如果没有输入
                        obj.setPjdf(null);
                        BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                        return;
                    }
                    if (s.toString().equals("00")) {//如果输入00
                        obj.setPjdf(0.00f);
                        s = (Editable) s.subSequence(0, s.length() - 1);
                        editview.setText(s);
                        editview.setSelection(editview.getText().length());
                        BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                        return;
                    }
                    if (s.length() == 2 && s.toString().startsWith("0")) {//输入01
                        s = (Editable) s.subSequence(1, s.length());
                        editview.setText(s);
                        obj.setPjdf(Float.valueOf(s.toString()));
                        editview.setSelection(editview.getText().length());
                        BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                        return;
                    }
                    if (s.toString().endsWith("分")) {
                        s = (Editable) s.subSequence(0, s.length() - 1);
                        if (s.length() == 0) {
                            obj.setPjdf(null);
                            BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                            return;
                        }
                    }
                    float score = Float.valueOf(s.toString());
                    if (score > obj.getLhfz()) {
                        s = (Editable) s.subSequence(0, s.length() - 1);
                        Toast.makeText(context, "当前输入分数不能大于最高分", Toast.LENGTH_SHORT).show();
                        editview.setText(s);
                        editview.setSelection(editview.getText().length());
                    }
                    obj.setPjdf(Float.valueOf(s.toString()));
                    BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                }
            });
            editview.setOnFocusChangeListener(new OnFocusChangeListener() {//拼接‘分’到目前的输入框
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText e = (EditText) v;
                    if (!hasFocus) {
                        if (e.getText() == null || e.getText().toString().equals("")) {
                            e.setText("");
                        } else {
                            e.setText(e.getText() + "分");
                        }
                    } else {
                        if (e.getText().toString().endsWith("分")) {
                            e.setText(e.getText().toString().substring(0, e.getText().toString()
									.length() - 1));
                        }
                    }
                }
            });
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
					.WRAP_CONTENT);
            params.setMargins(
                    0,
                    StringUtils.dip2px(context, 10),
                    0,
                    StringUtils.dip2px(context, 10));
            if (!type) {
                editview.setFocusable(false);
                editview.setEnabled(false);
                String showtext = (obj.getPjdf() == null || obj.getPjdf().equals("")) ? "" : (obj
						.getPjdf().intValue()) + "分";
                editview.setText(showtext);
            }
            selected.addView(editview, params);
        } else {//选择项
            RadioGroup group = new RadioGroup(context);
//			if(!type){
//				group.setEnabled(false);
//			}
            int wh = StringUtils.dip2px(context, 16);
            final Drawable checked = context.getResources().getDrawable(R.drawable.checkbox_check);
            checked.setBounds(0, 0, wh, wh);//第一0是距左边距离，第二0是距上边距离，wh分别是长宽
            final Drawable unchecked = context.getResources().getDrawable(R.drawable.checkbox_nor);
            unchecked.setBounds(0, 0, wh, wh);//第一0是距左边距离，第二0是距上边距离，wh分别是长宽

            for (int i = 0; i < obj.getPfbzList().size(); i++) {
                final Pfbz p = obj.getPfbzList().get(i);
                final RadioButton radio = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radioview, null, true);
                radio.setCompoundDrawables(unchecked, null, null, null);
                radio.setText(p.getPfzbmc());
                radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            radio.setCompoundDrawables(checked, null, null, null);
                            obj.setPjdf(Float.valueOf(p.getPfbzfz()));
                            BroadcastUtil.sendBroadcast(context, "wspjcanpost");
                        } else {
                            radio.setCompoundDrawables(unchecked, null, null, null);
                        }
                    }
                });
                if (!type) {
                    radio.setClickable(false);
                    if (Float.floatToIntBits(p.getPfbzfz()) == Float.floatToIntBits(obj.getPjdf())) {
                        radio.setChecked(true);
                    }
                }
                group.addView(radio, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                //循环加入横线
                if (i != obj.getPfbzList().size() - 1) {
                    View view = new View(context);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.dip2px(context, (float) 0.8));
                    view.setLayoutParams(params);
                    view.setBackgroundColor(Color.rgb(201, 201, 201));
                    group.addView(view);
                }
            }
            selected.addView(group);
        }
    }

}
