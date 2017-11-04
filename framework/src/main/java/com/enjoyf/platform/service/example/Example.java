/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-20 上午10:02
 * Description: The class must implements Serializable for network trans.
 */
public class Example implements Serializable {
    //
    private long exampleId;

    //
    private String exampleName;
    private String exampleDiscription;

    //
    private int clickTimes;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    private Date lastClickDate;

    //
    public Example() {
    }

    public long getExampleId() {
        return exampleId;
    }

    public void setExampleId(long exampleId) {
        this.exampleId = exampleId;
    }

    public String getExampleName() {
        return exampleName;
    }

    public void setExampleName(String exampleName) {
        this.exampleName = exampleName;
    }

    public String getExampleDiscription() {
        return exampleDiscription;
    }

    public void setExampleDiscription(String exampleDiscription) {
        this.exampleDiscription = exampleDiscription;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getLastClickDate() {
        return lastClickDate;
    }

    public void setLastClickDate(Date lastClickDate) {
        this.lastClickDate = lastClickDate;
    }

    //this's needed to print readable log.
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
