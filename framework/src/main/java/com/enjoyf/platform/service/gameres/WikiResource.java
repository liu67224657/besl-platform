package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class WikiResource implements Serializable {
    //pk
    private long resourceId;

    //通用
    private String wikiName;
    private String wikiCode;
    private String wikiUrl;


    private String icon;
    private String thumbIcon;

    private String wikiDesc;
    private ActStatus removeStatus = ActStatus.UNACT;

    private int totalPageNum;
    private int lastWeekUpdatePageNum;
    private Date lastUpdatePageDate;

    //todo not use
    private WikiRelationSet wikiRelationSet = new WikiRelationSet();
    private GameProperties wikiProperties;

    //for gameres
    private String createUserid;
    private Date createDate;

    private String lastModifyUserid;
    private Date lastModifyDate;


    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getWikiName() {
        return wikiName;
    }

    public void setWikiName(String wikiName) {
        this.wikiName = wikiName;
    }

    public String getWikiCode() {
        return wikiCode;
    }

    public void setWikiCode(String wikiCode) {
        this.wikiCode = wikiCode;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getThumbIcon() {
        return thumbIcon;
    }

    public void setThumbIcon(String thumbIcon) {
        this.thumbIcon = thumbIcon;
    }

    public String getWikiDesc() {
        return wikiDesc;
    }

    public void setWikiDesc(String wikiDesc) {
        this.wikiDesc = wikiDesc;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public WikiRelationSet getWikiRelationSet() {
        return wikiRelationSet;
    }

    public void setWikiRelationSet(WikiRelationSet wikiRelationSet) {
        this.wikiRelationSet = wikiRelationSet;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyUserid() {
        return lastModifyUserid;
    }

    public void setLastModifyUserid(String lastModifyUserid) {
        this.lastModifyUserid = lastModifyUserid;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getLastWeekUpdatePageNum() {
        return lastWeekUpdatePageNum;
    }

    public void setLastWeekUpdatePageNum(int lastWeekUpdatePageNum) {
        this.lastWeekUpdatePageNum = lastWeekUpdatePageNum;
    }

    public Date getLastUpdatePageDate() {
        return lastUpdatePageDate;
    }

    public void setLastUpdatePageDate(Date lastUpdatePageDate) {
        this.lastUpdatePageDate = lastUpdatePageDate;
    }

    public GameProperties getWikiProperties() {
        return wikiProperties;
    }

    public void setWikiProperties(GameProperties wikiProperties) {
        this.wikiProperties = wikiProperties;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
