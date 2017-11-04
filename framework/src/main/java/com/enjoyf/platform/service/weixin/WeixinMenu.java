package com.enjoyf.platform.service.weixin;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class WeixinMenu implements Serializable {
    private BasicButton[] button;

    public BasicButton[] getButton() {
        return button;
    }

    public void setButton(BasicButton[] button) {
        this.button = button;
    }
}
