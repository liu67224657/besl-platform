package com.enjoyf.platform.service.weixin;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class ComplexButton extends BasicButton implements Serializable {
    private BasicButton [] sub_button;

    public BasicButton[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(BasicButton[] sub_button) {
        this.sub_button = sub_button;
    }
}
