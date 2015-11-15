package com.fungame.hangingmachine.fragment;

import android.os.Bundle;
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
public class UserManagerFragment extends BaseFragment {

    private ListView listView;

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

    }

    @Override
    public void initView(View view) {
        listView = (ListView)view.findViewById(R.id.listView);
        View headView = View.inflate(this.getActivity(), R.layout.user_manager_header, null);
        // 设置一些事件
        listView.addHeaderView(headView);
        List<UserItem> list = new ArrayList<UserItem>();
        list.add(new UserItem("test", "testButton"));
        DataAdapter mAdapter = new DataAdapter(getActivity(), list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {

    }
}
