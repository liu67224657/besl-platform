package com.enjoyf.platform.thirddeposit;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by zhimingli on 2016/1/12 0012.
 */
public class CpResult {
    private int syncStatus;
    private String errormsg = "";

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    
}
