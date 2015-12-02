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
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fungame.hangingmachine.dialog.InputDialog;
import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.fragment.HtmlFragment;
import com.fungame.hangingmachine.fragment.NavInfoFragment;
import com.fungame.hangingmachine.fragment.NavMoneyFragment;
import com.fungame.hangingmachine.fragment.OneClickFragment;
import com.fungame.hangingmachine.fragment.UserManagerFragment;
import com.fungame.hangingmachine.util.AssetUtils;
import com.fungame.hangingmachine.util.LoginUtils;
import com.fungame.hangingmachine.util.NetworkUtils;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import org.w3c.dom.Text;

import java.math.BigDecimal;

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
        String user = getPreferenct().getString(Const.LOGIN_USER, "");
        String pwd = getPreferenct().getString(Const.LOGIN_PWD, "");
        if(!TextUtils.isEmpty(user)){
            etUser.setText(user);
        }
        if(!TextUtils.isEmpty(pwd)){
            etPwd.setText(pwd);
        }
    }

    @Override
    public void initListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = etUser.getText().toString();
                final String pwd = etPwd.getText().toString();
                login(user, pwd);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // not network
                if (!NetworkUtils.getNetworkEnable(etUser.getContext())) {
                    TostHelper.shortToast(getBaseContext(), "没有网络");
                    return;
                }

                final InputDialog dialog = new InputDialog(view.getContext());
                dialog.getText1().setHint("请输入用户名");
                dialog.getText2().setHint("请输入密码");
                dialog.getConfirm().setText("注册");
                dialog.getText2().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                dialog.setConfirmListener(new InputDialog.InputDialogInterface() {
                    @Override
                    public void onBtnClick(String et1, String et2) {
                        register(et1, et2);
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("提示");
                dialog.show();
            }
        });
    }

    private void login(final String user, final String pwd) {
        // 登陆
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
            TostHelper.shortToast(getBaseContext(), "用户名或密码为空");
            return;
        }

        // not network
        if(!NetworkUtils.getNetworkEnable(etUser.getContext())) {
            TostHelper.shortToast(getBaseContext(), "没有网络");
            return;
        }

        // 发送用户名和密码给服务器
        String command = "登录" + "|" + user + "|" + pwd;

        ServerUtils util = new ServerUtils();
        util.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

                if (!TextUtils.isEmpty(back)) {
                    // 获取开头的登录信息
                    boolean logSuccess = LoginUtils.processLoginInfo(getBaseContext(), getPreferenct(), back, user, pwd);
                    if(logSuccess){
                        startMainAndFinish();
                    } else {
                        TostHelper.shortToast(getBaseContext(), getString(R.string.login_fail));
                    }

                } else {
                    TostHelper.shortToast(getBaseContext(), getString(R.string.login_fail));
                }
            }
        });

//        saveLoginInfoAndJump();
    }



    private void startMainAndFinish() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        TostHelper.shortToast(getBaseContext(), "登录成功");
        finish();
    }

    private void register(final String user, final String pwd) {

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
                login(user, pwd);
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
