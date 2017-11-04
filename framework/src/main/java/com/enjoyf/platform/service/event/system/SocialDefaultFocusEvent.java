package com.enjoyf.platform.service.event.system;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-23
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public class SocialDefaultFocusEvent extends SystemEvent{

    private String uno;

    public SocialDefaultFocusEvent() {
        super(SystemEventType.SOCIAL_DEFAULT_FOCUS);
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }
}
