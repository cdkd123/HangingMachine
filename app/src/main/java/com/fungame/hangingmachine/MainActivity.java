package com.fungame.hangingmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.fragment.HtmlFragment;
import com.fungame.hangingmachine.fragment.NavInfoFragment;
import com.fungame.hangingmachine.fragment.NavMoneyFragment;
import com.fungame.hangingmachine.fragment.OneClickFragment;
import com.fungame.hangingmachine.fragment.UserManagerFragment;
import com.fungame.hangingmachine.util.ServerUtils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mfManger;
    private FrameLayout flContainer;
    private Fragment fragment;
    private View headMainView;
    private TextView tvName;

    private ClockService clockService;
    private boolean hasBind = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initParams();
        initListeners();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
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
            ServerUtils serverUtils = new ServerUtils();
            serverUtils.sendCommand("close", new ServerUtils.SocketCallBack() {
                @Override
                public void getCallBack(String back) {
                    clearUserData();
                    if(clockService != null){
                        clockService.stopClock();
                    }
                    MainActivity.this.finish();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearUserData() {

        getPreferenct().edit().putString(Const.ACCOUNT_TYPE, "").commit();
        getPreferenct().edit().putString(Const.ACCOUNT_MONEY, "").commit();
        getPreferenct().edit().putString(Const.ACCOUNT_LEVEL, "").commit();
        getPreferenct().edit().putString(Const.TODAY_PUSH_NUMS, "").commit();
        getPreferenct().edit().putString(Const.LOGIN_USER, "").commit();
        getPreferenct().edit().putString(Const.PUBLIC_ADS, "").commit();
        startActivity(new Intent(this, LoginActivity.class));
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

        tvName = (TextView)headMainView.findViewById(R.id.tvName);
        String userName = getPreferenct().getString(Const.LOGIN_USER, "");
        tvName.setText(userName);
    }

    @Override
    public void initListeners() {
        headMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginInfo = getPreferenct().getString(Const.LOGIN_INFO, "");
                if(TextUtils.isEmpty(loginInfo)) {
                    startActivity(new Intent(view.getContext(), LoginActivity.class));
                }
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            clockService = ((ClockService.ServiceBinder)(service)).getService();
            System.out.println("tom, connection!");
            if(clockService != null){
                System.out.println("tom, 222!");
                mHandler.sendEmptyMessage(BIND_SUCCESS);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            clockService = null;
            System.out.println("--server disconnect");

        }
    };

    private int BIND_SUCCESS = 200;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == BIND_SUCCESS){
                if(binderListener != null){
                    binderListener.onResult(clockService);
                }
            }
        }
    };

    private void startGuaji(String back) {


        Intent i = new Intent(this, ClockService.class);
        hasBind = bindService(i, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unbindClockService();
        super.onDestroy();
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

    public ClockService getClockService() {
        return clockService;
    }

    public void unbindClockService() {
        if(hasBind){
            System.out.println("has bind-->" + hasBind);
            unbindService(connection);
            hasBind = false;
        }
    }

    public void bindClockService(BindResult result) {
        this.binderListener = result;
        if(!hasBind) {
            Intent i = new Intent(this, ClockService.class);
            bindService(i, connection, Context.BIND_AUTO_CREATE);
        }
    }

    BindResult binderListener;

    public interface BindResult{
        public void onResult(ClockService service);
    }
}
