package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/9
 * Description:
 */
public class AppSum implements Serializable {
    private String appSumId;
    private String appKey;
    private String subKey = "def";
    private int activitySum;
    private int activityLogSum;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public int getActivitySum() {
        return activitySum;
    }

    public void setActivitySum(int activitySum) {
        this.activitySum = activitySum;
    }

    public String getAppSumId() {
        return appSumId;
    }

    public void setAppSumId(String appSumId) {
        this.appSumId = appSumId;
    }

    public int getActivityLogSum() {
        return activityLogSum;
    }

    public void setActivityLogSum(int activityLogSum) {
        this.activityLogSum = activityLogSum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
