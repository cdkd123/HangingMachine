package com.fungame.hangingmachine.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telecom.ConnectionService;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fungame.hangingmachine.ClockService;
import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.adapter.DataAdapter;
import com.fungame.hangingmachine.dialog.InputDialog;
import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.entity.User;
import com.fungame.hangingmachine.entity.UserItem;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class NavInfoFragment extends BaseFragment implements DataAdapter.InfoListListener {


    ListView listView;
    DataAdapter mAdapter;
    ServerUtils utils;
    private boolean start = false;
    TextView tvMesage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getPreferenct(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_nav_info, container, false);
        initView(rootView);
        initParams();
        initListeners();
        Log.i("--tom", "this is nav info fragment");
        return rootView;
    }

    @Override
    public void initParams() {
        utils = new ServerUtils();
    }

    @Override
    public void initView(View view) {

        User user = new User();
        String publishAds = pref.getString(Const.LOGIN_INFO, "");
        String userName = pref.getString(Const.LOGIN_USER, "");
        listView = (ListView)view.findViewById(R.id.listView);
        List<UserItem> userItems = new ArrayList<UserItem>();
        userItems.add(new UserItem("公告", publishAds));
        userItems.add(new UserItem(userName, "修改密码"));
        userItems.add(new UserItem(user.getAcountType(), "用户升级"));
        userItems.add(new UserItem(user.getAccountMoney(), "账号充值"));
        userItems.add(new UserItem(user.getSalarySpeed(), "提高佣金"));
        userItems.add(new UserItem(user.getAccountLevel(), "提交等级"));
        userItems.add(new UserItem(user.getTodayTime(), "开始推广"));

        DataAdapter mAdapter = new DataAdapter(this.getActivity(), userItems);
        listView.setAdapter(mAdapter);
        mAdapter.setBtnItemClickListener(this);

        tvMesage = (TextView) View.inflate(getActivity(), R.layout.text_view, null);
        listView.addFooterView(tvMesage);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void onBtnItemClick(int position) {
        switch(position - 1){
            case 0:
                modifyPassword();
                break;
            case 1:
                changeAccountType();
                break;
            case 2:
                fillMoney2Account();
                break;
            case 3:
                upgradeMakeMoneySpeed();
                break;
            case 4:
                updateUserLevel();
                break;
            case 5:
                startMakeMoney();
                break;
            default:
                break;
        }
    }


    TextView left, right;
    @Override
    public void onBtnItemClick(int position, TextView left, TextView right) {
        if(position == 6){
            String text = right.getText().toString();
            this.right = right;
            this.left = left;
            if("开始推广".equals(text)) {
                right.setText("取消推广");
                startMakeMoney();
            } else {
                right.setText("开始推广");
                stopMakeMoney();
            }

        }
    }

    long guaji = 0;

    private void startMakeMoney() {
        String command = "挂机|开始";

        startGuaji("");
        // 開一个服务，后台启动定时器定时更新，同时记录时间
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                // 开启服务，开始计时
                // thread emulator
                startGuaji(back);
            }
        });
    }

    double result = 0.0001;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                DateFormat format = new SimpleDateFormat("连接广告 yyyy年mm月dd日 hh时mm分ss秒");
                long mills = System.currentTimeMillis();
                String time = format.format(mills);
                result += 0.0001;

                try{
                    NumberFormat nformat = NumberFormat.getCurrencyInstance();
                    nformat.setMaximumFractionDigits(4);
                    String num = nformat.format(result);
                    left.setText("账户余额：" + num + "元");

                    String date = time;
                    String message = tvMesage.getText().toString();
                    message += time + "\n";
                    // 长度多余4行
                    if(message.length() > (time.length() * 4 + 3)){
                        // 去最后4行
                        int begin = message.length() - (time.length() * 4 + 3);
                        message = message.substring(begin, message.length() - 1);
                        tvMesage.setText(message);
                    }
                    tvMesage.setText(message);
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }
    };

    ServiceConnection connection;
    ClockService cockService = null;
    private void startGuaji(String back) {

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                cockService = ((ClockService.ServiceBinder)(service)).getService();
                System.out.println("tom, connection!");
                if(cockService != null){
                    System.out.println("tom, 222!");
                    cockService.setHandler(mHandler);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                cockService = null;

            }
        };
        Intent i = new Intent(getActivity(), ClockService.class);
        getActivity().bindService(i, connection, Context.BIND_AUTO_CREATE);

        System.out.println("gauji result---" + back);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(connection != null){
            getActivity().unbindService(connection);
        }
    }

    private void stopMakeMoney() {
        String command = "挂机|结束";
        // 開一个服务，后台启动定时器定时更新，同时记录时间
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

            }
        });
        start = false;
    }

    private void changeAccountType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.setTitle("提示：")
                .setMessage("立即升级为正式版188元，开通下级正式版188元?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 发送升级指令
//                        TostHelper.shortToast(getActivity(), "升级失败");
                        String upLevel = "升级|正式版";
                        utils.sendCommand(upLevel, new ServerUtils.SocketCallBack() {
                            @Override
                            public void getCallBack(String back) {
                                if (!TextUtils.isEmpty(back)) {
                                    TostHelper.shortToast(getActivity(), back);
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void upgradeMakeMoneySpeed() {
        showTipDialog(getString(R.string.tip_for_update_user_level));
    }

    public void showTipDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.setTitle("提示：")
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void fillMoney2Account() {

        TostHelper.shortToast(getActivity(), "暂时不支持手机充值，请在window客户端充值");
    }

    private void updateUserLevel() {
        showTipDialog(getString(R.string.tip_for_update_user_level));
    }

    private void modifyPassword() {
        final InputDialog modPwd = new InputDialog(getActivity());
        modPwd.setConfirmListener(new InputDialog.InputDialogInterface() {
            @Override
            public void onBtnClick(String et1, String et2) {
                if(TextUtils.isEmpty(et1)){
                    TostHelper.shortToast(getActivity(), "新密码为空");
                    return;
                }
//                if(TextUtils.isEmpty(et2)){
//                    TostHelper.shortToast(getActivity(), "确认密码为空");
//                    return;
//                }
//
//                if(!et1.equals(et2)){
//                    TostHelper.shortToast(getActivity(), "2个密码不一致");
//                    return;
//                }

                String command = "修改密码|" + et1;
                new ServerUtils().sendCommand(command, new ServerUtils.SocketCallBack() {
                    @Override
                    public void getCallBack(String back) {
                        // 提示修改密码成功
                        if(!TextUtils.isEmpty(back)) {
                            TostHelper.shortToast(getActivity(), back);
                        }
                    }
                });
                modPwd.dismiss();
            }
        });
        modPwd.getText2().setVisibility(View.GONE);
        modPwd.show();
    }
}
