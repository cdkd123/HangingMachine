package com.fungame.hangingmachine.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.entity.Const;

/**
 * Created by admin on 15/12/1.
 */
public class LoginUtils {

    // 保存一堆key－value字段到pref
    public static void saveInfo(SharedPreferences pref,
                                String key,
                                String value) {
        pref.edit().putString(key, value).commit();
    }

    // 处理登录信息，并保存一些字段到pref
    public static boolean processLoginInfo(Context context,
                                        SharedPreferences pref,
                                        String back,
                                        String user,
                                        String pwd) {

        String[] loginInfos =  back.split("\\|");
        try{
            if(loginInfos == null){
                TostHelper.shortToast(context, context.getString(R.string.login_fail));
                return false;
            } else if (loginInfos.length <= 2){
                TostHelper.shortToast(context, loginInfos[1]);
                return false;
            } else {
                saveInfo(pref, Const.ACCOUNT_TYPE, loginInfos[1]);
                saveInfo(pref, Const.ACCOUNT_MONEY, loginInfos[2]);
                saveInfo(pref, Const.ACCOUNT_LEVEL, loginInfos[3]);
                saveInfo(pref, Const.TODAY_PUSH_NUMS, loginInfos[4]);
                saveInfo(pref, Const.PUBLIC_ADS, loginInfos[5]);
                saveInfo(pref, Const.CAN_GUAJI, loginInfos[6]);
                saveInfo(pref, Const.CAN_JIAMAN, loginInfos[9]);
                saveInfo(pref, Const.LOGIN_USER, user);
                saveInfo(pref, Const.LOGIN_PWD, pwd);
                return true;
            }
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

    }
}
