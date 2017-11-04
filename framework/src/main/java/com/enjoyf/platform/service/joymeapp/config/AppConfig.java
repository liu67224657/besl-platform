package com.enjoyf.platform.service.joymeapp.config;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-3
 * Time: 下午6:45
 * To change this template use File | Settings | File Templates.
 */
public class AppConfig implements Serializable {

    public static final String DEFAULT_BUCKET = "joymepic";

    private String configId;   //MD5(appKey+"|"+platform+"|"+version+"|"+channel+"|"+ent)
    private String appKey;
    private AppPlatform platform;
    private String version;
    private String channel;
    private AppEnterpriserType enterpriseType;
    private AppConfigInfo info;
    private String appSecret;

    private Date createDate;
    private String createUserId;
    private Date modifyDate;
    private String modifyUserId;
    private String bucket = DEFAULT_BUCKET;  //上传图片的bucket


    public String getConfigId() {
        return StringUtil.isEmpty(configId) ? Md5Utils.md5(appKey + "|" + platform.getCode() + "|" + version + "|" + channel + "|" + enterpriseType.getCode()) : configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public AppConfigInfo getInfo() {
        return info;
    }

    public void setInfo(AppConfigInfo info) {
        this.info = info;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public AppEnterpriserType getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(AppEnterpriserType enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
