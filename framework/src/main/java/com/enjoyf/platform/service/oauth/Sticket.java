package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午2:34
 * Description:
 */
public class Sticket implements Serializable {
    private String uno;
    private String secr;
    private String rCode;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getSecr() {
        return secr;
    }

    public void setSecr(String secr) {
        this.secr = secr;
    }

    public String getrCode() {
        return rCode;
    }

    public void setrCode(String rCode) {
        this.rCode = rCode;
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
