package com.fungame.hangingmachine.entity;

/**
 * Created by admin on 15/11/29.
 */
public class MyOpenUser {

    private String userName;
    private String version;
    private String level;
    private String isTuiguang;

    public MyOpenUser(String userName, String version, String level, String isTuiguang) {
        this.userName = userName;
        this.version = version;
        this.level = level;
        this.isTuiguang = isTuiguang;
    }

    public String getUserName() {
        return userName;
    }

    public String getVersion() {
        return version;
    }

    public String getLevel() {
        return level;
    }

    public String getIsTuiguang() {
        return isTuiguang;
    }
}
