package com.dk.mp.dltj;

import android.content.Context;

import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;

/**
 * Created by abc on 2018-4-17.
 */

public class UrlUtils {


    public static String getUrl(Context context, String type) {
        String url="apps/dlxx/index?type="+type;
        String user="";
        LoginMsg loginMsg = new CoreSharedPreferencesHelper(context).getLoginMsg();
        if (loginMsg != null) {
            if(url.contains("?")){
                user="&uid="+  loginMsg.getUid()+"&pwd="+loginMsg.getPsw();
            }else{
                user="?uid="+  loginMsg.getUid()+"&pwd="+loginMsg.getPsw();
            }
        }
        return context.getString(com.dk.mp.core.R.string.rootUrl)+url+user;

    }
}
