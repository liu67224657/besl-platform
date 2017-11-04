package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/22
 */
public class WanbaNoticeSumDTO {
    private String type;//app 约定好的type
    private String value;//数字
    private int dtype;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDtype() {
        return dtype;
    }

    public void setDtype(int dtype) {
        this.dtype = dtype;
    }
}
