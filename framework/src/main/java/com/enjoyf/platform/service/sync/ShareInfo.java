package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-6
 * Time: 下午12:01
 * To change this template use File | Settings | File Templates.
 */
public class ShareInfo implements Serializable {

    private ShareBaseInfo baseInfo;
    private ShareTopic shareTopic;
    private ShareBody shareBody;

    public ShareBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(ShareBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    public ShareTopic getShareTopic() {
        return shareTopic;
    }

    public void setShareTopic(ShareTopic shareTopic) {
        this.shareTopic = shareTopic;
    }

    public ShareBody getShareBody() {
        return shareBody;
    }

    public void setShareBody(ShareBody shareBody) {
        this.shareBody = shareBody;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
