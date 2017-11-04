package com.enjoyf.platform.service.profile;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-2-14
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class ProfileAuditStatus implements Serializable {
    public static int HAS_AUDIT = 1;
    public static int ILLEGAL_BLOG_DESCRIPTION = 2;

    private int value = 0;

    //私有的
    private ProfileAuditStatus(int value) {
        this.value = value;
    }

    public boolean hasAudit(){
        return (value & HAS_AUDIT) > 0;
    }

    public boolean isBlogDescIllegal(){
        return (value & ILLEGAL_BLOG_DESCRIPTION) > 0;
    }

    public int getValue() {
        return value;
    }

    public static ProfileAuditStatus getByValue(int value) {
        return new ProfileAuditStatus(value);
    }

    @Override
    public String toString() {
        return "ProfileAuditStatus{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o instanceof ProfileAuditStatus){
            return ((ProfileAuditStatus)o).getValue() == value;
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value;
    }
}
