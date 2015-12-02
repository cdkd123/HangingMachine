package com.fungame.hangingmachine.entity;

/**
 * Created by tom on 2015/11/13.
 * descripton :
 */
public enum AccountType {

    NormalType("正式版"),
    FreeType("免费版");

    private String name;

    private AccountType(String name){
        this.name = name;
    }

    public static AccountType getEnumByName(String name) {
        AccountType[] enums = AccountType.values();
        for (AccountType formatEnum : enums) {
            if (formatEnum.name.equals(name)) {
                return formatEnum;
            }
        }
        return NormalType;
    }

    public String getName() {
        return name;
    }
}
