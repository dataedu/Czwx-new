package com.dk.mp.core.dialog;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.mp.core.R;

public class MsgDialog {

	/**
	 * 公共对话框.
	 * 
	 *            标题
	 *            内容
	 *            确定按钮点击事件.
	 * @return AlertDialog
	 */
	public static void show(Context context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.core_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(Html.fromHtml(message));
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	public static void show(Context context, String message, boolean isLong) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.core_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(Html.fromHtml(message));
		Toast toast = new Toast(context);
		if (isLong) {
			toast.setDuration(Toast.LENGTH_LONG);
		} else {
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	/**
	 * 公共对话框.
	 * 
	 *            标题
	 *            内容
	 *            确定按钮点击事件.
	 * @return AlertDialog
	 */
	public static void show(Context context, int message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.core_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(message);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}
	
	public static void show(Context context, int message, boolean isLong) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.core_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(message);
		Toast toast = new Toast(context);
		if (isLong) {
			toast.setDuration(Toast.LENGTH_LONG);
		} else {
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	public static void showOrgin(Context context,String mess){
		Toast toast = Toast.makeText(context,mess, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
