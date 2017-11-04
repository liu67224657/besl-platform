package com.enjoyf.platform.service.tools;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-16
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class ContentAuditStatus implements Serializable {
    //标记：文本审核过与否
    public static final int AUDIT_TEXT = 1;
    //标记：审核过的文本通过与否
    public static final int ILLEGAL_TEXT = 2;
    //标记：图片审核过与否
    public static final int AUDIT_IMG = 4;
    //标记：审核过的图片通过与否
    public static final int ILLEGAL_IMG = 8;

    private int value = 0;
    private int result = 0;

    public ContentAuditStatus() {
    }

    public ContentAuditStatus(int value, int result) {
        this.value = value;
        this.result = result;
    }

    public ContentAuditStatus(int value) {
        this.value = value;
    }

    public boolean hasAuditText() {
        return (value & AUDIT_TEXT) > 0;
    }


    public boolean hasAuditIMG() {
        return (value & AUDIT_IMG) > 0;
    }

    public boolean isTextPass() {
        return (value & ILLEGAL_TEXT) <= 0;
    }

    public boolean isIMGPass() {
        return (value & ILLEGAL_IMG) <= 0;
    }

    public int getValue() {
        return value;
    }

    public int getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ContentAuditStatus value=" + value + ":result=" + result;
    }

    public static ContentAuditStatus getByValue(Integer v) {
        return new ContentAuditStatus(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ContentAuditStatus) {
            return value == ((ContentAuditStatus) obj).getValue() && result == ((ContentAuditStatus) obj).getResult();
        } else {
            return false;
        }
    }

}
