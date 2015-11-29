package com.fungame.hangingmachine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.adapter.DataAdapter;
import com.fungame.hangingmachine.entity.User;
import com.fungame.hangingmachine.entity.UserItem;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class UserManagerFragment extends BaseFragment implements View.OnClickListener {

    private ListView listView;
    private EditText etOpenAccount;
    private EditText etOpenPwd;
    private Button btnOpenVersion;
    private Button btnOpen;
    private Button btnGetUser;
    private Button btnOpenAuth;
    private ServerUtils utils;
    DataAdapter mAdapter;
    final List<MyOpenUser> list = new ArrayList<MyOpenUser>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_users_manager, container, false);
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
        listView = (ListView)view.findViewById(R.id.listView);
        View headView = View.inflate(this.getActivity(), R.layout.user_manager_header, null);
        // 设置一些事件
        listView.addHeaderView(headView);

        // 从headview中获取一些控件
        etOpenAccount = (EditText)headView.findViewById(R.id.etOpenAccount);
        etOpenPwd = (EditText)headView.findViewById(R.id.etOpenPwd);
        btnOpenVersion = (Button)headView.findViewById(R.id.btnOpenVersion);
        btnOpen = (Button)headView.findViewById(R.id.btnOpen);
        btnGetUser = (Button)headView.findViewById(R.id.btnGetUser);
        btnOpenAuth = (Button)headView.findViewById(R.id.btnOpenAuth);
        btnOpen.setOnClickListener(this);
        btnGetUser.setOnClickListener(this);
        btnOpenAuth.setOnClickListener(this);
        list.add(new MyOpenUser("0", "test", "test", "2", "true"));
        mAdapter = new DataAdapter(getActivity(), list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOpen:
                openUser();
                break;
            case R.id.btnGetUser:
                getDownLineUser();
                break;
            case R.id.btnOpenAuth:
                getGuajiAuth();
                break;
        }
    }

    private void openUser() {
        String user = etOpenAccount.getText().toString();
        String pwd = etOpenPwd.getText().toString();

        if(TextUtils.isEmpty(user)){
            TostHelper.shortToast(getActivity(), "用户为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            TostHelper.shortToast(getActivity(), "密码为空");
            return;
        }
        String command = "开通|" + user + "|" + pwd + "|试用版" + "|28";
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                if(!TextUtils.isEmpty(back)) {
                    TostHelper.shortToast(getActivity(), back);
                }
             }
        });

    }

    private void getDownLineUser() {
//        String user = etOpenAccount.getText().toString();
//        String pwd = etOpenPwd.getText().toString();
        String command = "获取下线|11";

        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                if(!TextUtils.isEmpty(back)) {
                    TostHelper.shortToast(getActivity(), back);
                    // support a json string
//                    try {
//                        JSONArray json = new JSONArray(back);
//                        list.clear();
//                        for(int i = 0; i < json.length(); i++){
//                            MyOpenUser user = new MyOpenUser("id", "accountName", "type", "level", "tuigua");
//                            list.add(user);
//                        }
//                        if(list.size() > 0){
//                            mAdapter.addAll(list);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    } catch (Exception ex){
//
//                    }

                }
            }
        });
    }

    public void getGuajiAuth() {

        String user = etOpenAccount.getText().toString();
        if(TextUtils.isEmpty(user)){
            TostHelper.shortToast(getActivity(), "用户为空");
            return;
        }

        String command = "开通权限|" + user;
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                if(!TextUtils.isEmpty(back)) {
                    TostHelper.shortToast(getActivity(), back);
                }
            }
        });
    }
}
