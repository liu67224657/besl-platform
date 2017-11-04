package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-6-7
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertiseInfo implements Serializable {
    private AppAdvertise appAdvertise;

    //non db
    private Date startDate;
    private Date endDate;
    private long publishId;
    private PublishParam publishParam;

    public AppAdvertise getAppAdvertise() {
        return appAdvertise;
    }

    public void setAppAdvertise(AppAdvertise appAdvertise) {
        this.appAdvertise = appAdvertise;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getPublishId() {
        return publishId;
    }

    public void setPublishId(long publishId) {
        this.publishId = publishId;
    }

    public PublishParam getPublishParam() {
        return publishParam;
    }

    public void setPublishParam(PublishParam publishParam) {
        this.publishParam = publishParam;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
