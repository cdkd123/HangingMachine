package com.fungame.hangingmachine;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract void initParams();
    public abstract void initView();
    public abstract void initListeners();

}
