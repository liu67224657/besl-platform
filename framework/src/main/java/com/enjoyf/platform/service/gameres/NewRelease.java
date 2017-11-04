package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
public class NewRelease implements Serializable {

    private long newReleaseId;
    private String newGameName; //名称
    private String newGameIcon; //图标
    private String newGameDesc; //简介
    private NewReleasePicSet newGamePicSet;//游戏截图集合
    private int displayOrder;//排序
    private ValidStatus validStatus;//是否审核通过
    private String companyName;//团体、公司名称
    private PeopleNumType peopleNumType;//团体、公司人数
    private CooprateType cooprateType;//团体、公司合作类型
    private String contacts;//联系人姓名
    private String email;//联系人邮箱
    private String phone; //联系人电话
    private String qq;   //联系人QQ
    private Date publishDate;  //游戏预计上线时间
    private PublishArea publishArea; //游戏发行的地区范围
    private Date createDate;//提交时间
    private String createIp; //提交人ip
    private String createUno;
    private Date verifyDate;
    private Date lastModifyDate; //最后修改时间
    private String lastModifyIp; //最后修改人ip
    private LastModifyType lastModifyType;//修改类型
    private int focusNum;
    private long shareId;
    private String notice;

    public long getNewReleaseId() {
        return newReleaseId;
    }

    public void setNewReleaseId(long newReleaseId) {
        this.newReleaseId = newReleaseId;
    }

    public String getNewGameName() {
        return newGameName;
    }

    public void setNewGameName(String newGameName) {
        this.newGameName = newGameName;
    }

    public String getNewGameIcon() {
        return newGameIcon;
    }

    public void setNewGameIcon(String newGameIcon) {
        this.newGameIcon = newGameIcon;
    }

    public String getNewGameDesc() {
        return newGameDesc;
    }

    public void setNewGameDesc(String newGameDesc) {
        this.newGameDesc = newGameDesc;
    }

    public NewReleasePicSet getNewGamePicSet() {
        return newGamePicSet;
    }

    public void setNewGamePicSet(NewReleasePicSet newGamePicSet) {
        this.newGamePicSet = newGamePicSet;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public PeopleNumType getPeopleNumType() {
        return peopleNumType;
    }

    public void setPeopleNumType(PeopleNumType peopleNumType) {
        this.peopleNumType = peopleNumType;
    }

    public CooprateType getCooprateType() {
        return cooprateType;
    }

    public void setCooprateType(CooprateType cooprateType) {
        this.cooprateType = cooprateType;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public PublishArea getPublishArea() {
        return publishArea;
    }

    public void setPublishArea(PublishArea publishArea) {
        this.publishArea = publishArea;
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

    public String getCreateUno() {
        return createUno;
    }

    public void setCreateUno(String createUno) {
        this.createUno = createUno;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public LastModifyType getLastModifyType() {
        return lastModifyType;
    }

    public void setLastModifyType(LastModifyType lastModifyType) {
        this.lastModifyType = lastModifyType;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
