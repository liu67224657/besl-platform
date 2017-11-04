package com.enjoyf.platform.service.ask.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
public class WanbaSearchDeleteEntry implements Serializable {
    private String qid;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getDeleteId() {
        StringBuffer sb = new StringBuffer();
        sb.append("(id:" + this.qid + ")");
        return sb.toString();
    }
}
