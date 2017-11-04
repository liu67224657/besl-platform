package com.enjoyf.webapps.joyme.webpage.base.mvc;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-9-16
 * Time: 下午8:30
 * To change this template use File | Settings | File Templates.
 */
public class LoginInfo {
    private String userid;
    private boolean isSuccess;

    public LoginInfo(String userid, boolean isSuccess) {
        this.userid = userid;
        this.isSuccess = isSuccess;
    }

    public String getUserid() {
        return userid;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
