package com.fungame.hangingmachine.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fungame.hangingmachine.entity.Const;

/**
 * Created by tom on 2015/11/13.
 * descripton : 作为所有Fragment的父类
 * 实现例如设置标题，隐藏actionbar，获取上下文
 * 获取网络句柄等。暂未实现。
 */
public abstract class BaseFragment extends Fragment {

    public abstract void initParams();
    public abstract void initView(View view);
    public abstract void initListeners();

    SharedPreferences pref;

    protected SharedPreferences getPreferenct(Context context) {
        pref = context.getSharedPreferences(Const.SHARE_PRE_NAME, Context.MODE_PRIVATE);
        return pref;
    }

    protected void saveInfo(String key, String value) {
        pref.edit().putString(key, value).commit();
    }
}
