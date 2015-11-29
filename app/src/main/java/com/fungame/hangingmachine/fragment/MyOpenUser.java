package com.fungame.hangingmachine.fragment;

/**
 * Created by admin on 15/11/29.
 */
public class MyOpenUser {

    private String id;
    private String accountName;
    private String type;
    private String level;
    private String tuigua;

    public MyOpenUser(String id, String accountName, String type, String level, String tuigua) {
        this.id = id;
        this.accountName = accountName;
        this.type = type;
        this.level = level;
        this.tuigua = tuigua;
    }

    public String getAccountName() {
        return accountName;
    }
}
