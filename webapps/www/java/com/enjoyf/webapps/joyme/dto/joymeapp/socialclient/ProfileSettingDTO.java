package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-9
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSettingDTO {
    private String uno;
    private String agree;
    private String reply;
    private String focus;
    private String sound;

    public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return uno.hashCode();
    }
}
