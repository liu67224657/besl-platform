package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppShareChannel;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-9
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
public class SocialLog implements Serializable {

    private ObjectId SLogId;

    private long foreignId;
    private String uno;

    private long contentId;

    private SocialLogType logType;

    private SocialLogCategory logCategory;
    //分享渠道
    private AppPlatform platform;
    private AppShareChannel shareChannel;

    private Date createDate;
    private String createIp;
    private String createUserId;

    public ObjectId getSLogId() {
        return SLogId;
    }

    public void setSLogId(ObjectId SLogId) {
        this.SLogId = SLogId;
    }

    public long getForeignId() {
        return foreignId;
    }

    public void setForeignId(long foreignId) {
        this.foreignId = foreignId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public SocialLogType getLogType() {
        return logType;
    }

    public void setLogType(SocialLogType logType) {
        this.logType = logType;
    }

    public SocialLogCategory getLogCategory() {
        return logCategory;
    }

    public void setLogCategory(SocialLogCategory logCategory) {
        this.logCategory = logCategory;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public AppShareChannel getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(AppShareChannel shareChannel) {
        this.shareChannel = shareChannel;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
