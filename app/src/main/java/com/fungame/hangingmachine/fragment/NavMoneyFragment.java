package com.fungame.hangingmachine.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fungame.hangingmachine.R;
import com.fungame.hangingmachine.util.ServerUtils;
import com.fungame.hangingmachine.util.TostHelper;

import java.math.BigDecimal;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class NavMoneyFragment extends BaseFragment {

    private EditText etAccount;
    private EditText etName;
    private EditText etEmail;
    private EditText etGetMoney;
    private EditText etQQ;
    private TextView tvMessage;
    private Button btnGetMoney;
    private Button btnRecharge;
    ServerUtils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_money, container, false);
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
        etAccount = (EditText)view.findViewById(R.id.etAccount);
        etName = (EditText)view.findViewById(R.id.etName);
        etEmail = (EditText)view.findViewById(R.id.etEmail);
        etGetMoney = (EditText)view.findViewById(R.id.etGetMoney);
        etQQ = (EditText)view.findViewById(R.id.etQQ);
        tvMessage = (TextView)view.findViewById(R.id.tvMessage);
        btnGetMoney = (Button)view.findViewById(R.id.btnGetMoney);
        btnRecharge = (Button)view.findViewById(R.id.btnRecharge);

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

    // 提现
    private void getMoney() {

        String aliPayAccountName = etAccount.getText().toString();
        String aliPayNickName = etName.getText().toString();
        String email = etEmail.getText().toString();
        String upMoney = etGetMoney.getText().toString();
        String QQ号码 = etQQ.getText().toString();
        String sxf = "";

        if("0".equals(upMoney) || "".equals(upMoney)){
            TostHelper.shortToast(getContext(), "提现不能为0");
            return;
        }
        BigDecimal metaMoney = new BigDecimal(upMoney);
        BigDecimal sxfMeta = new BigDecimal("0.9");
//        BigDecimal finalUpMoney = new BigDecimal("0.9");
        sxf = metaMoney.multiply(sxfMeta).toString();
//        String finalMoney = metaMoney.multiply(finalUpMoney).toString();
        String command =
                "提现|" + aliPayAccountName + "|" + aliPayNickName + "|"  + email + "|" + upMoney + "|" + sxf + "|" + QQ号码;
        System.out.println("提现命令:" + command);
        utils.sendCommand(command, new ServerUtils.SocketCallBack() {
            @Override
            public void getCallBack(String back) {
                //  tip
                if(!TextUtils.isEmpty(back)){
                    TostHelper.shortToast(getActivity(), back);
                }
            }
        });
    }

    private void rechange() {
        showTipDialog("请充值转账给guajicz@126.com\n付款说明填写自己的挂机账号");
    }

    @Override
    public void initListeners() {
        // 提现
        btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoney();
            }
        });
        // 充值
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechange();
            }
        });
    }
}
