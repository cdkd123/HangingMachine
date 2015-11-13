package com.fungame.hangingmachine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fungame.hangingmachine.R;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class UserManagerFragment extends BaseFragment {

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

    }

    @Override
    public void initListeners() {

    }
}
