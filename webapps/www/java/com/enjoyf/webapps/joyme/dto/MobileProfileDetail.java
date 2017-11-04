package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-19
 * Time: 下午7:19
 * To change this template use File | Settings | File Templates.
 */
public class MobileProfileDetail implements Serializable {

    private String sex;
    private String vdesc;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
