package com.fungame.hangingmachine.entity;

import android.content.SharedPreferences;

import java.math.BigDecimal;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class User {
    String userName;  // 账户名称
    String accountMoney; // 账户余额
    String salarySpeed;  // 获取佣金速度
    long todayWorkTime; // 今日推广时间(s)
    private String publishAds;
    private AccountType accountType;
    private String userLevel;
    private String todayNums;

    public String getUserName() {
        return userName;
    }

    public String getUserLevel() {
        return this.userLevel;
    }

    public String getAccountMoney() {
        return accountMoney;
    }

    public String getSalarySpeed() {
        return this.userLevel ;
    }

    public String getAcountType() {
        return accountType.getName();
    }

    public String getTodayNum() {
        return todayNums + "";
    }

    public void initDataFromPreference(SharedPreferences pref) {
        this.publishAds = pref.getString(Const.PUBLIC_ADS, "");
        this.userName = pref.getString(Const.LOGIN_USER, "");
        String acType = pref.getString(Const.ACCOUNT_TYPE, "");
        this.accountType = AccountType.FreeType;
        if(acType.equals(AccountType.NormalType.getName())) {
            accountType = AccountType.NormalType;
        }
        this.userLevel = pref.getString(Const.ACCOUNT_LEVEL, "");
        this.todayNums = pref.getString(Const.TODAY_PUSH_NUMS, "");
        this.accountMoney = pref.getString(Const.ACCOUNT_MONEY, "");
    }

    public String getPublicAds() {
        return this.publishAds;
    }
}
