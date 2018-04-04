package com.dk.mp.wspj;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.core.R;
import com.dk.mp.core.application.MyApplication;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.AppUtil;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.PinyinUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2016/12/14 14:53
 */
public abstract class MyActivitySim extends AppCompatActivity{
    public Context mContext = this;
    private CoreSharedPreferencesHelper preference;
    private Gson gson;

    private DrawerLayout drawerLayout;
    private Button homeButton;
    private LinearLayout itemContent;
    private String classname;
    FrameLayout frameLayout;

    private int x;
    private int y;

    /**
     * @return 界面布局
     */
    protected abstract
    @LayoutRes
    int getLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        classname = this.getClass().getName();
        x = getIntent().getIntExtra("x",-10);
        y = getIntent().getIntExtra("y",-10);

        setContentView (R.layout.core);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutID(), null);
        frameLayout = (FrameLayout)findViewById(R.id.id_content);
        frameLayout.addView(view);
        intentFilter2.addAction("flishall");
        registerReceiver(receiver, intentFilter2);
        /*if (x != -10) {
            frameLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Animator animator = createRevealAnimator(false, x, y);
                        Animator animator = MyApplication.createRevealAnimator(false,x,y,frameLayout,MyActivitySim.this);
                        animator.start();
                    }
                }
            });
        }*/

    }

    private IntentFilter intentFilter2 = new IntentFilter();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!"com.dk.mp.main.home.ui.HomeActivity".equals(classname)){
                finish();
            }
        }
    };

    /**
     * 初始化皮肤
     */
    protected void initTheme ( ) {
        preference = getSharedPreferences();
        String value = preference.getValue("font_type");
        if("大".equals(value)) {
          this.setTheme(R.style.style_large);
        }else if("特大".equals(value)){
            this.setTheme(R.style.style_big);
        }else{
            this.setTheme(R.style.style_norm);
        }
    }

    /**
     * 初始化皮肤.
     * @param title 标题栏文字
     */
    public void setTitle(int title) {
        setTitle(getReString(title));
    }

    /**
     * 获取string
     * @param string R.string.x
     * @return  ""
     */
    public String getReString(int string) {
        return getResources().getString(string);
    }

    public CoreSharedPreferencesHelper getSharedPreferences() {
        if (preference == null){
            preference = new CoreSharedPreferencesHelper(this);
        }
        return preference;
    }

    public Gson getGson() {
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }

    /**
     * 显示提示信息.
     * @param message 提示信息
     */
    public void showMessage(String message) {
        MsgDialog.show(this, message);
    }


    /**
     * @param title 标题栏文字
     */
    public void setTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(title);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 返回
     */
    public void back() {
        if (x != -10) {
            frameLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Animator animator = createRevealAnimator(true, x, y);
                        Animator animator = MyApplication.createRevealAnimator(true,x,y,frameLayout,MyActivitySim.this);
                        animator.start();
                    }else{
                        finish();
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                onBackPressed();
            }else{
                finish();
            }
        }
    }

    public void initDock(){
        final Gson gson = new Gson();
        final LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        homeButton = (Button) findViewById(R.id.homebutton);
        Drawable drawable1 = ContextCompat.getDrawable(this, R.mipmap.home);
        drawable1.setBounds(0, 0, 40, 40);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        homeButton.setCompoundDrawables(drawable1, null, null, null);//只放左边
        homeButton.setBackground(null);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                sendBroadcast(new Intent().setAction("flishall"));
            }
        });
        itemContent = (LinearLayout) findViewById(R.id.itemContent);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {
                itemContent.removeAllViews();
                List<App> apps = gson.fromJson(preference.getValue("preferenceItem"),new TypeToken<ArrayList<App>>() {}.getType());
                if (apps != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    View endview = new View(mContext);
                    endview.setLayoutParams(params);
                    for (final App app : apps) {
                        View addview = new View(mContext);
                        addview.setLayoutParams(params);
                        itemContent.addView(addview);
                        View view = inflater.inflate(R.layout.gridview_item, null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
                        TextView textView= (TextView) view.findViewById(R.id.item_text);
                        textView.setText(app.getName());
                        imageView.setImageResource(ImageUtil.getResource(app.getIcon()));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                drawerLayout.closeDrawers();
                                sendBroadcast(new Intent().setAction("flishall"));
                                new AppUtil(mContext).checkApp(app);
                            }
                        });
                        itemContent.addView(view);
                    }
                    itemContent.addView(endview);
                }
            }
            @Override
            public void onDrawerClosed(View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        drawerLayout.closeDrawers();
    }

    /**
     * 公共手机返回按钮事件.
     * @param keyCode keyCode
     * @param event  KeyEvent
     * @return  boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
