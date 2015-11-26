package com.fungame.hangingmachine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.util.TostHelper;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class OneClickFragment extends BaseFragment {

    private Button btnOpenGuaji;
    private Button btnFull;

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
                // 一键挂机
                TostHelper.shortToast(getActivity(), "一键挂机");
            }
        });
        btnFull.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 一键加满
                TostHelper.shortToast(getActivity(), "一键加满");
            }
        });
    }
}
