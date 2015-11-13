package com.fungame.hangingmachine.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

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
}
