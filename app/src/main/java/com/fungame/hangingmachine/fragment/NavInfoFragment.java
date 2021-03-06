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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fungame.hangingmachine.ClockService;
import com.fungame.hangingmachine.MainActivity;
import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.adapter.DataAdapter;
import com.fungame.hangingmachine.adapter.SimpleBaseAdapter;
import com.fungame.hangingmachine.dialog.InputDialog;
import com.fungame.hangingmachine.dialog.UIItemView;
import com.fungame.hangingmachine.entity.Const;
import com.fungame.hangingmachine.entity.User;
import com.fungame.hangingmachine.entity.UserItem;
import com.fungame.hangingmachine.util.LoginUtils;
import com.fungame.hangingmachine.util.NetworkUtils;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by tom on 2015/11/13.
 * descripton : account basic infomation show fragment
 */
public class NavInfoFragment extends BaseFragment {

    private static final int STOP_GUAJI = 200;
    public static final int DELAY_MILLIS = 60 * 1000;
    private ServerUtils utils;
    private boolean start = false;
    private TextView tvMesage;
    private MainActivity mainActivity;
    private ClockService service;
    private static final int RELOGIN = 201;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity)context;
    }

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

    UIItemView uiItemAds, uiItem1, uiItem2, uiItem3, uiItem4, uiItem5, uiItem6;


    @Override
    public void initView(View view) {

        uiItemAds = (UIItemView) view.findViewById(R.id.publishAds);
        uiItem1 = (UIItemView) view.findViewById(R.id.uiItem1);
        uiItem2 = (UIItemView) view.findViewById(R.id.uiItem2);
        uiItem3 = (UIItemView) view.findViewById(R.id.uiItem3);
        uiItem4 = (UIItemView) view.findViewById(R.id.uiItem4);
        uiItem5 = (UIItemView) view.findViewById(R.id.uiItem5);
        uiItem6 = (UIItemView) view.findViewById(R.id.uiItem6);

        initUserData();

        tvMesage = (TextView) view.findViewById(R.id.tvMessage);
    }

    private void initUserData() {
        UIItemView[] uiItems = {uiItemAds, uiItem1, uiItem2, uiItem3,
                uiItem4, uiItem5, uiItem6};
        User user = new User();
        user.initDataFromPreference(pref);
        List<UserItem> userItems = new ArrayList<UserItem>();

        userItems.add(new UserItem(getString(R.string.public_ads), user.getPublicAds()));
        userItems.add(new UserItem(user.getUserName(), getString(R.string.modify_pwd)));
        userItems.add(new UserItem(user.getAcountType(), getString(R.string.update_user_level)));
        userItems.add(new UserItem(user.getAccountMoney(), getString(R.string.account_fill_money)));
        String basicAdd = basicHourMeta().toString() + "元/每小时";
        userItems.add(new UserItem(basicAdd, getString(R.string.increase_commision)));
        userItems.add(new UserItem(user.getUserLevel(), getString(R.string.increase_level)));
        try{
            int todayNum = Integer.valueOf(user.getTodayNum());
            if(todayNum > 36000){
                todayNum = 36000;
            }
            String pushTime = getSecond2HMS(todayNum);
            userItems.add(new UserItem(pushTime, getString(R.string.start_ads)));
        } catch (NumberFormatException ex){
            ex.printStackTrace();
            int todayNum = 0;
            String pushTime = getSecond2HMS(todayNum);
            userItems.add(new UserItem(user.getTodayNum(), getString(R.string.start_ads)));
        }


        if(mainActivity != null && mainActivity.getClockService() != null){
            service = mainActivity.getClockService();
            service.setHandler(mHandler);
            initResult();
        }

        for(int i = 0; i < userItems.size() ;i++){
            getItemView(i, uiItems[i], userItems.get(i));
        }
    }

    public void getItemView(final int position, UIItemView holder, UserItem meta) {

        final TextView tvLabel = (TextView) holder.getView(R.id.tvLabel);
        final Button btnClick = (Button) holder.getView(R.id.btn);
        String label = meta.getLabel();
        System.out.println("position:" + position + ":" + meta.getLabel() + ",");
        if(position == 0){
            tvLabel.setText(meta.getBtn());
            btnClick.setVisibility(View.GONE);
        } else {
            btnClick.setVisibility(View.VISIBLE);
            switch(position){
                case 1:
                    label = getString(R.string.login_account) + ":" + meta.getLabel();
                    break;
                case 2:
                    label = getString(R.string.account_type) + ":" + meta.getLabel();

                    break;
                case 3:
                    label = getString(R.string.account_money) + ":" + meta.getLabel();

                    break;
                case 4:
                    label = getString(R.string.account_money_speed) + ":" + meta.getLabel();

                    break;
                case 5:
                    label = getString(R.string.account_level) + ":" + meta.getLabel();
                    break;
                case 6:
                    label = "今日推广:" + meta.getLabel();
                    break;
                default:
                    label = "index error!";
                    break;
            }

            tvLabel.setText(label);
            if(position - 1 == 6){
                // 判断一下当前是否再推广
                if(service != null){
                    btnClick.setText(R.string.pause_ads);
                } else {
                    btnClick.setText(R.string.start_ads);
                }
            } else {
                btnClick.setText(meta.getBtn());
            }
        }

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnItemClick(position);
            }
        });
    }


    @Override
    public void initListeners() {

    }

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
                pushClick();
                break;
            default:
                break;
        }
    }


    // push button was click
    private void pushClick() {
        final TextView tvRight = (TextView) uiItem6.getView(R.id.btn);
        String text = tvRight.getText().toString();
        if(mainActivity.getString(R.string.start_push).equals(text)) {
            startMakeMoney();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvRight.setText(R.string.start_push);
                }
            }, 1000);

            stopMakeMoney();
        }
    }


    /**
     * 开始发送挂机命令，并启动后台挂机服务
     */
    private void startMakeMoney() {

        initResult();
        String command = "挂机|开始";

        System.out.println("start result:" + result);
        // 開一个服务，后台启动定时器定时更新，同时记录时间
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

            }
        });
        startGuaji("");
    }

    // read
    private void initResult() {
        String acountMoney = getPreferenct(getActivity()).getString(Const.ACCOUNT_MONEY, "0");
//        int tdNums = Integer.valueOf(todayNums);
//        if(tdNums > 0){
//            BigDecimal metaDecimal = basicAddMeta();
          result = new BigDecimal(acountMoney);
    }

    // start clockservice to start a new thread, and change text
    private void startGuaji(String s) {

        // 先绑定服务器
        if(mainActivity != null){
            if(mainActivity.getClockService() != null){
                mHandler.sendEmptyMessageDelayed(RELOGIN, DELAY_MILLIS);
                mainActivity.getClockService().startClock();
            } else {
                mainActivity.bindClockService(new MainActivity.BindResult() {
                    @Override
                    public void onResult(ClockService service) {
                        mHandler.sendEmptyMessageDelayed(RELOGIN, DELAY_MILLIS);
                        service.setHandler(mHandler);
                    }
                });
            }

        }
    }

    private UIItemView getUiItem(int position){
        switch (position){
            case 0:
                return uiItemAds;
            case 1:
                return uiItem1;
            case 2:
                return uiItem2;
            case 3:
                return uiItem3;
            case 4:
                return uiItem4;
            case 5:
                return uiItem5;
            case 6:
                return uiItem6;
            default:
            break;
        }
        return uiItem6;
    }

    private TextView getRight(int postion){
        TextView right = (TextView) getUiItem(postion).findViewById(R.id.btn);
        return right;
    }

    private TextView getLeft(int position){
        TextView left = (TextView) getUiItem(position).findViewById(R.id.tvLabel);
        return left;
    }

    BigDecimal result = new BigDecimal("0.0001");

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 接收service发送过来的消息
            if(msg.what == 0){
                showLinkMessage();
            } else if(msg.what == STOP_GUAJI){
                stopGuaji();
            } else if(msg.what == RELOGIN){
                relogin(true);
            }
        }
    };


    private void relogin(final boolean flag) {

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
                    initUserData();
                } else {
                    System.out.println("--tom: login info empty");
                }
                if(flag){
                    mHandler.sendEmptyMessageDelayed(RELOGIN,
                            DELAY_MILLIS);
                }

            }
        });
    }

    // 解除挂机服务
    private void stopGuaji() {
        if(mainActivity.getClockService() != null){
            mainActivity.getClockService().stopClock();
            mainActivity.setClockServiceStop();
            mHandler.removeMessages(RELOGIN);
            System.out.println("stop guaiji!");
        }
    }

    private BigDecimal basicHourMeta() {
        try{
            String sAccountLevel =  getPreferenct(getContext()).getString(Const.ACCOUNT_LEVEL, "1");
            BigDecimal meta = new BigDecimal("0.36");
            BigDecimal level = new BigDecimal(sAccountLevel);
            System.out.println("account level:" + sAccountLevel);
            return meta.multiply(level);
        } catch(NumberFormatException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private BigDecimal basicAddMeta() {
        try{
            String sAccountLevel =  getPreferenct(getContext()).getString(Const.ACCOUNT_LEVEL, "1");
            BigDecimal meta = new BigDecimal("0.0001");
            BigDecimal level = new BigDecimal(sAccountLevel);
            System.out.println("account level:" + sAccountLevel);
            return meta.multiply(level);
        } catch(NumberFormatException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private void showLinkMessage() {

        if(getContext() == null){
            return;
        }

        String todayNums = getPreferenct(getActivity()).getString(Const.TODAY_PUSH_NUMS, "1");
        if(TextUtils.isEmpty(todayNums)){
            // logout
            return;
        }


        // calc current account money
        DateFormat format = new SimpleDateFormat("连接广告 yyyy年mm月dd日 hh时mm分ss秒\n连接广告成功");
        long mills = System.currentTimeMillis();
        String time = format.format(mills);
        BigDecimal metaDecimal = basicAddMeta();
        result = result.add(metaDecimal);
        System.out.println("meta:" + metaDecimal + "result:" + result);
//         calc today push ads number
        int todayNum = Integer.valueOf(todayNums) + 1;
        if(todayNum > 36000){
            todayNum = 36000;
        }
        LoginUtils.saveInfo(getPreferenct(getContext()), Const.TODAY_PUSH_NUMS, todayNum + "");

        try{
            if(uiItem1 != null){

                // 取消按钮
                TextView tvStart = (TextView) uiItem6.getView(R.id.btn);
                tvStart.setText(R.string.pause_ads);

                // 设置账户余额
//                String num = getFormatNum();
                TextView tvAccountMoney = getLeft(3);
                tvAccountMoney.setText("账户余额：" + result + "元");

                // 设置推广时间
                String pushTime = getSecond2HMS(todayNum);
                System.out.println("get push times:" + pushTime + ",push num:" + todayNums);
                TextView tvPushTime = getLeft(6);
                tvPushTime.setText("推广时间：" + pushTime);

                String date = time;
                String message = tvMesage.getText().toString();
                message += time + "\n";
                // 长度多余4行
                if(message.length() > ((time.length() + 1) * 4)){
                    // 去最后4行
                    int begin = message.length() - ((time.length() + 1) * 4);
                    message = message.substring(begin);
                    tvMesage.setText(message);
                }
                tvMesage.setText(message);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getSecond2HMS(int num){
        int time = 1*60*60;
        int hour = (int) (num / time);
        int leftTime = (int) (num % time);
        int minute = leftTime / 60;
        int second = leftTime % 60;
        String result = hour + "时" + minute + "分" + second + "秒";
        return result;
    }


    /* 获取4位小数格式的数字 */
    private String getFormatNum() {
        NumberFormat nformat = NumberFormat.getCurrencyInstance();
        nformat.setMaximumFractionDigits(4);
        return nformat.format(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void stopMakeMoney() {
        String command = "挂机|取消";
        // 開一个服务，后台启动定时器定时更新，同时记录时间
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {

            }
        });

        stopGuaji();
    }

    // 升级账户类型
    private void changeAccountType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.setTitle("提示：")
                .setMessage("立即升级为正式版288元，开通下级正式版188元?")
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
                                    relogin(false);
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
