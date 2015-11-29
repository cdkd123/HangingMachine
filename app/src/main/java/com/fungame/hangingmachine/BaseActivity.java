package com.fungame.hangingmachine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fungame.hangingmachine.entity.Const;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract void initParams();
    public abstract void initView();
    public abstract void initListeners();

    protected SharedPreferences getPreferenct() {
        SharedPreferences pref = getSharedPreferences(Const.SHARE_PRE_NAME, Context.MODE_PRIVATE);
        return pref;
    }

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
    }

    protected void saveInfo(String key, String value) {
        getPreferenct().edit().putString(key, value).commit();
    }

}
