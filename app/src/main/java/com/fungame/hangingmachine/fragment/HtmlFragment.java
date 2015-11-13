package com.fungame.hangingmachine.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fungame.hangingmachine.R;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class HtmlFragment extends BaseFragment {

    String url;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if(bundle != null){
            url = bundle.getString("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_html, container, false);
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

        WebView webView = (WebView) view.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(false);
        webView.loadUrl(url);
    }

    @Override
    public void initListeners() {

    }
}
