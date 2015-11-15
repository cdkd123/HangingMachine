package com.fungame.hangingmachine.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.adapter.DataAdapter;
import com.fungame.hangingmachine.entity.User;
import com.fungame.hangingmachine.entity.UserItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class NavInfoFragment extends BaseFragment {


    ListView listView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_info, container, false);
        initView(rootView);
        initParams();
        initListeners();
        Log.i("--tom", "this is nav info fragment");
        return rootView;
    }

    @Override
    public void initParams() {

    }

    @Override
    public void initView(View view) {

        User user = new User();
        listView = (ListView)view.findViewById(R.id.listView);
        List<UserItem> userItems = new ArrayList<UserItem>();
        userItems.add(new UserItem(user.getAccountName(), "修改密码"));
        userItems.add(new UserItem(user.getAcountType(), "用户升级"));
        userItems.add(new UserItem(user.getAccountMoney(), "账号充值"));
        userItems.add(new UserItem(user.getSalarySpeed(), "提高佣金"));
        userItems.add(new UserItem(user.getAccountLevel(), "提交等级"));
        userItems.add(new UserItem(user.getTodayTime(), "开始推广"));
        listView.setAdapter(new DataAdapter(this.getActivity(), userItems));
    }


    @Override
    public void initListeners() {

    }
}
