package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2015/7/28.
 */
public class RefreshCMSTiming implements Serializable {

    private long time_id;//主键
    private String cms_id;//cms的id
    private String cms_name;//cms的name
    private RefreshReleaseType refreshReleaseType = RefreshReleaseType.ONE_TIME;//刷新类型：0-单次 1-每天
    private long release_time;//发布时间
    private ActStatus remove_status = ActStatus.ACTED;
    private String modify_user;
    private Date modify_time;

    public long getTime_id() {
        return time_id;
    }

    public void setTime_id(long time_id) {
        this.time_id = time_id;
    }

    public String getCms_id() {
        return cms_id;
    }

    public void setCms_id(String cms_id) {
        this.cms_id = cms_id;
    }

    public String getCms_name() {
        return cms_name;
    }

    public void setCms_name(String cms_name) {
        this.cms_name = cms_name;
    }

    public RefreshReleaseType getRefreshReleaseType() {
        return refreshReleaseType;
    }

    public void setRefreshReleaseType(RefreshReleaseType refreshReleaseType) {
        this.refreshReleaseType = refreshReleaseType;
    }

    public long getRelease_time() {
        return release_time;
    }

    public void setRelease_time(long release_time) {
        this.release_time = release_time;
    }

    public ActStatus getRemove_status() {
        return remove_status;
    }

    public void setRemove_status(ActStatus remove_status) {
        this.remove_status = remove_status;
    }

    public String getModify_user() {
        return modify_user;
    }

    public void setModify_user(String modify_user) {
        this.modify_user = modify_user;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}

