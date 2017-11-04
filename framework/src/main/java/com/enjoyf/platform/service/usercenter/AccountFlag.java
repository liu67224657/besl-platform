package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericliu on 14/10/23.
 */
public class AccountFlag implements Serializable {

    public static final int FLAG_PHONE_VERIFY =1;
    public static final int FLAG_EMAIL_VERIFY =2;

    private int value;

    public AccountFlag() {
    }

    public AccountFlag(int v) {
        value = v;
    }

    public AccountFlag has(int v) {
        value += (1<<v);

        return this;
    }

    public AccountFlag reduce(int v) {
        value -= (1<<v);

        return this;
    }

    public int getValue() {
        return value;
    }

    public boolean hasFlag(int length) {
        return (value &(1<<length))>0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static void main(String[] args) {
        AccountFlag accountFlag = new AccountFlag();
        System.out.println(accountFlag.has(0).getValue());
    }

}
