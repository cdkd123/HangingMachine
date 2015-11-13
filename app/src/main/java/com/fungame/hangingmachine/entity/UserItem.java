package com.fungame.hangingmachine.entity;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public class UserItem {
    String labelButton;
    String label;

    public UserItem(String accountName, String button) {
        this.label = accountName;
        this.labelButton = button;
    }
}
