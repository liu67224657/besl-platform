/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.PrivacyType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description: 按照时间分表，每月一表。
 */
public class ResourceFile implements Serializable {
    //the unique id of the media, include the type,store hostname, data and md5 code.
    private String fileId;

    //the owner uno
    private String ownUno;

    //the media type, such as the default content image, headicon, app, game etc.
    private ResourceFileType resourceFileType;

    //file bytes.
    private Long fileBytes = 0l;

    //隐私设置
    private PrivacyType privacy = PrivacyType.PUBLIC;

    //使用状态，有些图片上传后并不被使用．
    private int usedTimes = 0;

    //the create date and ip info.
    private Date createDate;
    private String createIp;

    //resource file audit status
    private ActStatus auditStatus = ActStatus.UNACT;
    private Date auditDate;

    //表记录删除标记
    private ActStatus tableRemoveStatus = ActStatus.UNACT;
    private Date tableRemoveDate;

    //实际删除标识
    private ActStatus fileRemoveStatus = ActStatus.UNACT;
    private Date fileRemoveDate;

    //constructor.
    public ResourceFile() {
    }

    public ResourceFile(String fileId) {
        this.fileId = fileId;
    }

    //
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public ResourceFileType getResourceFileType() {
        return resourceFileType;
    }

    public void setResourceFileType(ResourceFileType resourceFileType) {
        this.resourceFileType = resourceFileType;
    }

    public Long getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(Long fileBytes) {
        this.fileBytes = fileBytes;
    }

    public PrivacyType getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyType privacy) {
        this.privacy = privacy;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
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

    public ActStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(ActStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public ActStatus getTableRemoveStatus() {
        return tableRemoveStatus;
    }

    public void setTableRemoveStatus(ActStatus tableRemoveStatus) {
        this.tableRemoveStatus = tableRemoveStatus;
    }

    public Date getTableRemoveDate() {
        return tableRemoveDate;
    }

    public void setTableRemoveDate(Date tableRemoveDate) {
        this.tableRemoveDate = tableRemoveDate;
    }

    public ActStatus getFileRemoveStatus() {
        return fileRemoveStatus;
    }

    public void setFileRemoveStatus(ActStatus fileRemoveStatus) {
        this.fileRemoveStatus = fileRemoveStatus;
    }

    public Date getFileRemoveDate() {
        return fileRemoveDate;
    }

    public void setFileRemoveDate(Date fileRemoveDate) {
        this.fileRemoveDate = fileRemoveDate;
    }

    @Override
    public int hashCode() {
        return fileId != null ? fileId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
