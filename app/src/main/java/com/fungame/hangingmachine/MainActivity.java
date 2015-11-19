package com.fungame.hangingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.fungame.hangingmachine.fragment.HtmlFragment;
import com.fungame.hangingmachine.fragment.NavInfoFragment;
import com.fungame.hangingmachine.fragment.NavMoneyFragment;
import com.fungame.hangingmachine.fragment.OneClickFragment;
import com.fungame.hangingmachine.fragment.UserManagerFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mfManger;
    private FrameLayout flContainer;
    private Fragment fragment;
    private View headMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initParams();
        initListeners();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean needChange = false;
        if (id == R.id.nav_info) { // 我的信息
            needChange = true;
        } else if (id == R.id.nav_money) { // 我的推广、提现
            needChange = true;
        } else if (id == R.id.nav_users_manager) { // 下线管理
            needChange = true;
        } else if (id == R.id.nav_one_click) { // 一键挂机
            needChange = true;
        } else if (id == R.id.nav_official_site) { // 官方网站
            needChange = true;
        }

        if(needChange){
            mfManger.beginTransaction().replace(R.id.flContainer, getFragment(id)).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initParams() {
        mfManger = getSupportFragmentManager();
        mfManger.beginTransaction().add(R.id.flContainer, getFragment(R.id.nav_info)).commit();
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////////////////自己添加的控件
        flContainer = (FrameLayout)findViewById(R.id.flContainer);
//        headMainView = navigationView.getRootView().findViewById(R.id.head_main);

        headMainView = navigationView.inflateHeaderView(R.layout.nav_header_main);
    }

    @Override
    public void initListeners() {
        headMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), LoginActivity.class));
            }
        });
    }

    public Fragment getFragment(int id) {
        Fragment infoFragment = null;
        if(id == R.id.nav_info) {
            infoFragment = new NavInfoFragment();
        } else if (id == R.id.nav_money){
            infoFragment = new NavMoneyFragment();
        } else if (id == R.id.nav_one_click){
            infoFragment = new OneClickFragment();
        } else if (id == R.id.nav_users_manager){
            infoFragment = new UserManagerFragment();
        } else if (id == R.id.nav_official_site){
            infoFragment = new HtmlFragment();
            Bundle bundle = new Bundle();
            bundle.putString("data", "http://www.eguaji.cc/index.html");
            infoFragment.setArguments(bundle);
        }
        return infoFragment;
    }
}
