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
    public void onBackPressed() {
        super.onBackPressed();
        // 如果没登陆，退出程序
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
//                return ;

                if (!TextUtils.isEmpty(back) && back.contains("YES") ){
                    saveInfo(Const.LOGIN_INFO, back);
                    saveInfo(Const.LOGIN_USER, user);
                    TostHelper.shortToast(getBaseContext(), "登录成功");
                } else {
                    TostHelper.shortToast(getBaseContext(), back);
                }
            }
        });

//        saveLoginInfoAndJump();
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
//                return ;
                TostHelper.shortToast(getBaseContext(), back);
                login();
            }
        });
//        saveLoginInfoAndJump();
    }

    private void saveInfo(String key, String value) {
        getPreferenct().edit().putString(key, value).commit();
        // 转主界面
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void saveLoginInfoAndJump() {
        String loginInfo = AssetUtils.getDataFromAssets(this, "login_success.txt");
        getPreferenct().edit().putString(Const.LOGIN_INFO, loginInfo).commit();
        // 转主界面
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
