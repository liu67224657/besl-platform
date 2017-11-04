package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-19
 * Time: 下午7:23
 * To change this template use File | Settings | File Templates.
 */
public class MobileProfileClientDTO implements Serializable {
    private MobileProfileDTO pc;
    private MobileProfileDetail detail;
    private MobileProfileSum sum;

    public MobileProfileDTO getPc() {
        return pc;
    }

    public void setPc(MobileProfileDTO pc) {
        this.pc = pc;
    }

    public MobileProfileDetail getDetail() {
        return detail;
    }

    public void setDetail(MobileProfileDetail detail) {
        this.detail = detail;
    }

    public MobileProfileSum getSum() {
        return sum;
    }

    public void setSum(MobileProfileSum sum) {
        this.sum = sum;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
