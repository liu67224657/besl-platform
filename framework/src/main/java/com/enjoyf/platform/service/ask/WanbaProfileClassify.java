package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */
public class WanbaProfileClassify implements Serializable {
    private String classifyid;
    private String profileid;
    private Date create_time;
    private IntValidStatus removeStatus = IntValidStatus.VALID;
    private WanbaProfileClassifyType classify_type = WanbaProfileClassifyType.WANBA_ASK_VIRTUAL;
    private String ext;

    public String getClassifyid() {
        return classifyid;
    }

    public void setClassifyid(String classifyid) {
        this.classifyid = classifyid;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public IntValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(IntValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public WanbaProfileClassifyType getClassify_type() {
        return classify_type;
    }

    public void setClassify_type(WanbaProfileClassifyType classify_type) {
        this.classify_type = classify_type;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static WanbaProfileClassify toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, WanbaProfileClassify.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
