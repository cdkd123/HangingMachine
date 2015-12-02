package com.fungame.hangingmachine.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.entity.AccountType;
import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.util.LoginUtils;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import org.w3c.dom.Text;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class OneClickFragment extends BaseFragment {

    private Button btnOpenGuaji;
    private Button btnFull;
    ServerUtils utils;
    private boolean normalUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_oneclick, container, false);
        initView(rootView);
        initParams();
        initListeners();
        return rootView;
    }

    @Override
    public void initParams() {
        utils = new ServerUtils();
    }

    @Override
    public void initView(View view) {
        btnOpenGuaji = (Button)view.findViewById(R.id.btnOpenGuaji);
        btnFull = (Button)view.findViewById(R.id.btnFull);
    }

    @Override
    public void initListeners() {
        btnOpenGuaji.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String guajiAuth = getPreferenct(v.getContext())
                        .getString(Const.CAN_JIAMAN, "");

                // 如果不是正式版用户，不让加满
                if(!isNormalUser()){
                    TostHelper.shortToast(getContext(), getContext().getString(R.string.not_normal_user));
                }

                if(!"真".equals(guajiAuth)){
                    // 一键挂机
                    String command = "开通挂满资格|0";
                    utils.sendCommand(command, new ServerUtils.SocketCallBack() {
                        @Override
                        public void getCallBack(String back) {
                            if(!TextUtils.isEmpty(back)) {
                                TostHelper.shortToast(getActivity(), back);
                                LoginUtils.saveInfo(getPreferenct(getContext()),
                                        Const.CAN_JIAMAN,
                                        "真");
                                relogin();
                            }
                        }
                    });
                } else {
                    TostHelper.shortToast(getActivity(), v.getContext().getString(R.string.has_open_guaji));
                }

            }
        });
        btnFull.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String jiamanAuth = getPreferenct(v.getContext()).getString(Const.LOGIN_USER + "jiamanAuth",
                    "");
                // 如果不是正式版用户，不让加满
                if(!isNormalUser()){
                    TostHelper.shortToast(getContext(), getContext().getString(R.string.not_normal_user));
                }

                if(!"true".equals(jiamanAuth)){
                    String command = "加满时间|0";
                    utils.sendCommand(command, new ServerUtils.SocketCallBack() {
                        @Override
                        public void getCallBack(String back) {
                            if (!TextUtils.isEmpty(back)) {
                                LoginUtils.saveInfo(getPreferenct(getContext()),
                                        Const.LOGIN_USER + "jiamanAuth",
                                        "true");
                                TostHelper.shortToast(getActivity(), back);
                                relogin();
                            }
                        }
                    });
                } else {
                    TostHelper.shortToast(getActivity(), v.getContext().getString(R.string.has_open_jiaman2));
                }


            }
        });
    }


    private void relogin() {

        // 如果没有网络，发送消息停止挂机，也不用loginle
        System.out.println("--tom: relogin");
        if(getContext() == null){
            return;
        }
        final String user = getPreferenct(getContext()).getString(Const.LOGIN_USER, "");
        final String pwd = getPreferenct(getContext()).getString(Const.LOGIN_PWD, "");
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
            return;
        }

        String command = "登录" + "|" + user + "|" + pwd;
        System.out.println("--tom: relogin command:" + command);

        ServerUtils util = new ServerUtils();
        util.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

                if (!TextUtils.isEmpty(back)) {
                    System.out.println("--tom: relogin info:" + back);
                    // 获取开头的登录信息
                    LoginUtils.processLoginInfo(getContext(), getPreferenct(getContext()), back, user, pwd);
                } else {
                    System.out.println("--tom: login info empty");
                }
            }
        });
    }

    public boolean isNormalUser() {
        SharedPreferences pref = getPreferenct(getContext());
        if(AccountType.NormalType.getName().equals(pref.getString(Const.ACCOUNT_TYPE, ""))) {
            return true;
        }
        return false;
    }
}
