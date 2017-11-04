package com.enjoyf.platform.service.naming;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-6
 * Time: 下午6:21
 * To change this template use File | Settings | File Templates.
 */
public class BackupResourceDomain implements Serializable {
    private String rd;
    private String reRd;

    public BackupResourceDomain(String rd, String reRd) {
        this.rd = rd;
        this.reRd = reRd;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getReRd() {
        return reRd;
    }

    public void setReRd(String reRd) {
        this.reRd = reRd;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
