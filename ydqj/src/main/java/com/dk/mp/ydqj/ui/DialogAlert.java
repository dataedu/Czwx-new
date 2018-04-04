package com.dk.mp.ydqj.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dk.mp.ydqj.R;

public class DialogAlert {
	private Context context;
	private Dialog dlg;

	public DialogAlert(Context context) {
		this.context = context;
	}

	/**
	 * 公共对话框.
	 * @param title 标题
	 * @param content 内容
	 */
	public void show(String title, String content) {
		dlg = new Dialog(context, R.style.MyDialog);
		dlg.setCancelable(false);
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_alert);
		TextView contentView = (TextView) window.findViewById(R.id.content);
		contentView.setText(Html.fromHtml(content));
		Button cancel = (Button) window.findViewById(R.id.ok);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}
	
}
