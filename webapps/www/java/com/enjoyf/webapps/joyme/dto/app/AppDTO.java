package com.enjoyf.webapps.joyme.dto.app;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/8
 * Description:
 */
public class AppDTO {
    private String appkey;
    private String shake_open;
    private String defad_open;
    private String defad_url;
    private String gift_open;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getShake_open() {
        return shake_open;
    }

    public void setShake_open(String shake_open) {
        this.shake_open = shake_open;
    }

    public String getDefad_open() {
        return defad_open;
    }

    public void setDefad_open(String defad_open) {
        this.defad_open = defad_open;
    }

    public String getDefad_url() {
        return defad_url;
    }

    public void setDefad_url(String defad_url) {
        this.defad_url = defad_url;
    }

    public String getGift_open() {
        return gift_open;
    }

    public void setGift_open(String gift_open) {
        this.gift_open = gift_open;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
