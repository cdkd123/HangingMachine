package com.fungame.hangingmachine.entity;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class User {
    String accountName;  // 账户名称
    AccountType acoutType;   // 账户类型
    String accountMoney; // 账户余额
    String salarySpeed;  // 获取佣金速度
    String accountLevel; // 账户级别
    long todayWorkTime; // 今日推广时间(s)

    public User(){
        this.accountName = "张三";
        this.acoutType = AccountType.TestType;
        this.accountMoney = "0.0";
        this.salarySpeed = "0.5元/每小时";
        this.accountLevel = "1.5";
        this.todayWorkTime = 1500000;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public String getAccountMoney() {
        return accountMoney;
    }

    public String getSalarySpeed() {
        return salarySpeed;
    }

    public String getAcountType() {
        return acoutType.getName();
    }

    public String getTodayTime() {
        return todayWorkTime + "";
    }
}
