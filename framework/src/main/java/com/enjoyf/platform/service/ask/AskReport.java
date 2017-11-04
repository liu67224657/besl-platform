package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2016/12/7 0007.
 */
public class AskReport implements Serializable {
    private String reportId;//(md5 reportType+destId)
    private ItemType itemType; //举报类型：问题、答案
    private Long destId;  //举报问题id、举报答案id
    private String destProfileId;// extr 扩展字段
    private AskReportType askReportType = AskReportType.CONTENT;//举报类型
    private Date createTime;
    private ValidStatus validStatus = ValidStatus.VALID;
    private String extstr;  //扩展字段

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public AskReportType getAskReportType() {
        return askReportType;
    }

    public void setAskReportType(AskReportType askReportType) {
        this.askReportType = askReportType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getExtstr() {
        return extstr;
    }

    public void setExtstr(String extstr) {
        this.extstr = extstr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
