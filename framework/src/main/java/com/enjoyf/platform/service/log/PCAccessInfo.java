package com.enjoyf.platform.service.log;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-9-30
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class PCAccessInfo {
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return info;
    }
}
