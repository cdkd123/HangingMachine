package com.fungame.hangingmachine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.fragment.HtmlFragment;
import com.fungame.hangingmachine.fragment.NavInfoFragment;
import com.fungame.hangingmachine.fragment.NavMoneyFragment;
import com.fungame.hangingmachine.fragment.OneClickFragment;
import com.fungame.hangingmachine.fragment.UserManagerFragment;
import com.fungame.hangingmachine.util.AssetUtils;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import org.w3c.dom.Text;

public class LoginActivity extends BaseActivity {

    Toolbar toolbar;
    EditText etUser,etPwd;
    Button btnLogin, btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initParams();
        initListeners();

        String loginInfo = getPreferenct().getString(Const.LOGIN_INFO, "");
        if(!TextUtils.isEmpty(loginInfo)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    @Override
    public void initParams() {
        toolbar.setTitle("登录");
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etUser = (EditText) findViewById(R.id.etUser);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    @Override
    public void initListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void login() {

        final String user = etUser.getText().toString();
        String pwd = etPwd.getText().toString();

        // 登陆
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
            TostHelper.shortToast(getBaseContext(), "用户名或密码为空");
            return;
        }

        // 发送用户名和密码给服务器
        String command = "登录" + "|" + user + "|" + pwd;

        ServerUtils util = new ServerUtils();
        util.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

                if (!TextUtils.isEmpty(back) && back.contains("YES") ){
                    // 获取开头的登录信息
                    processLoginInfo(back, user);
                    startMainAndFinish();
                } else {
                    TostHelper.shortToast(getBaseContext(), back);
                }
            }
        });

//        saveLoginInfoAndJump();
    }

    public  void processLoginInfo(String back, String user) {
        int firstSpace = back.indexOf(" ");

        String loginInfo = back.substring(0, firstSpace);
        String publicAds = back.substring(firstSpace);
        String[] loginInfos = loginInfo.split("\\|");

        saveInfo(Const.ACCOUNT_TYPE, loginInfos[1]);
        saveInfo(Const.ACCOUNT_MONEY, loginInfos[2]);
        saveInfo(Const.ACCOUNT_LEVEL, loginInfos[3]);
        saveInfo(Const.TODAY_PUSH_NUMS, loginInfos[4]);
        saveInfo(Const.LOGIN_USER, user);
        saveInfo(Const.PUBLIC_ADS, publicAds);
    }

    private void startMainAndFinish() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        TostHelper.shortToast(getBaseContext(), "登录成功");
        finish();
    }

    private void register() {
        String user = etUser.getText().toString();
        String pwd = etPwd.getText().toString();

        // 登陆
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
            TostHelper.shortToast(getBaseContext(), "用户名或密码为空");
            return;
        }

        // 发送用户名和密码给服务器
        String command = "注册" + "|" + user + "|" + pwd;

        ServerUtils util = new ServerUtils();
        util.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                TostHelper.shortToast(getBaseContext(), back);
                login();
            }
        });
    }



    private void saveLoginInfoAndJump() {
        String loginInfo = AssetUtils.getDataFromAssets(this, "login_success.txt");
        getPreferenct().edit().putString(Const.LOGIN_INFO, loginInfo).commit();
        // 转主界面
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
