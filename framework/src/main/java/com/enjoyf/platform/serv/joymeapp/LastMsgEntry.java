/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;


import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author <a href=mailto:EricLiu@staff.joyme.com>Eric List</a>
 */

public class LastMsgEntry {
    private String appKey;
    private Long lastMsgId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(Long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
