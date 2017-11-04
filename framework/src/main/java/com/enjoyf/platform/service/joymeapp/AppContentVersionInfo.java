package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-24
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value = {"id", "appKey", "publishDate", "createUserId","createIp","removeStatus","necessaryUpdate"})
public class AppContentVersionInfo implements Serializable, Comparable<AppContentVersionInfo> {
    private long id;
    private String appKey;
    private long current_version;
    private String version_url;
    private String version_info;
    private Date publishDate;
    private String createUserId;
    private String createIp;
    private ActStatus removeStatus = ActStatus.UNACT;

    private boolean necessaryUpdate;
    private int packageType;  //0--patch 1---complete


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public long getCurrent_version() {
        return current_version;
    }

    public void setCurrent_version(long current_version) {
        this.current_version = current_version;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getVersion_info() {
        return version_info;
    }

    public void setVersion_info(String version_info) {
        this.version_info = version_info;
    }

    public boolean isNecessaryUpdate() {
        return necessaryUpdate;
    }

    public void setNecessaryUpdate(boolean necessaryUpdate) {
        this.necessaryUpdate = necessaryUpdate;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    @Override
    public int compareTo(AppContentVersionInfo o) {
        return this.getCurrent_version() >= o.getCurrent_version() ? -1 : 1;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
