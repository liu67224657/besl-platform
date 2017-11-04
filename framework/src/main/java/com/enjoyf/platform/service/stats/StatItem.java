/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class StatItem implements Serializable {
    //the stat item sequence id stored in database.
    private long itemId;

    //the stat domain and stat section.
    private StatDomain statDomain;//统计的分类
    private String domainName;

    private StatSection statSection;//统计分类中的分块
    private String sectionName;

    //the stat data date setting
    private StatDateType dateType;//统计的时间种类
    private Date statDate;//统计的时间

    //the stat value.
    private long statValue;//统计的数据
    private StatItemExtData extData = new StatItemExtData(); //扩展数据

    //
    private Date reportDate;//采集数据的时间

    private String publishId;
    private String viewUrl;

    /////////////////////////////////////////////////////////
    public StatItem() {

    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public StatDomain getStatDomain() {
        return statDomain;
    }

    public void setStatDomain(StatDomain statDomain) {
        this.statDomain = statDomain;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public StatSection getStatSection() {
        return statSection;
    }

    public void setStatSection(StatSection statSection) {
        this.statSection = statSection;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public StatDateType getDateType() {
        return dateType;
    }

    public void setDateType(StatDateType dateType) {
        this.dateType = dateType;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public long getStatValue() {
        return statValue;
    }

    public void setStatValue(long statValue) {
        this.statValue = statValue;
    }

    public StatItemExtData getExtData() {
        return extData;
    }

    public void setExtData(StatItemExtData extData) {
        this.extData = extData;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}

