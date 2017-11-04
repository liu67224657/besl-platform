package com.enjoyf.platform.webapps.common.dto.index;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ViewLineItemElementDTO implements Cloneable{
    private String elementId; //cid gameresid
    private String title;//titile  gameName
    private String desc; //content review

    private String thumbimg;//image src  gameicons or desc
    private String thumbimgSmall;//from gameres thumbimg
    private String videoCoverSrc;

    private String link;//link

    private String code;//gamecode
    private String elementType;//gameres domain code
    private String device;
    private Map<String, String> gameResourceLinkMap;//other link gameresource

    private String uno;//用户 uno
    private String domain; //域名
    private String screenName; //昵称
    private String verifyType;
    private List<Content> lastContentList;

    private Date createDate; //时间

    private Date dateValue;
    private int intValue1;
    private int intValue2;

    private int favorTimes;//喜欢数
    private int replyTimes;//评论数
    private int viewTimes;
    private int postTimes;
    private int forwardTimes;

    //-1粉丝未关注 0未关注 1关注  2相互关注
    private String relationFlag;//关系数

    private ViewLineItemDisplayType displayType;

    private String iconType;

    private String extField1;
    private String extField2;
    private String extField3;
    private String extField4;
    private String extField5;

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumbimg() {
        return thumbimg;
    }

    public void setThumbimg(String thumbimg) {
        this.thumbimg = thumbimg;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getFavorTimes() {
        return favorTimes;
    }

    public void setFavorTimes(int favorTimes) {
        this.favorTimes = favorTimes;
    }

    public int getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(int replyTimes) {
        this.replyTimes = replyTimes;
    }

    public String getRelationFlag() {
        return relationFlag;
    }

    public void setRelationFlag(String relationFlag) {
        this.relationFlag = relationFlag;
    }

    public Map<String, String> getGameResourceLinkMap() {
        return gameResourceLinkMap;
    }

    public void setGameResourceLinkMap(Map<String, String> gameResourceLinkMap) {
        this.gameResourceLinkMap = gameResourceLinkMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public int getPostTimes() {
        return postTimes;
    }

    public void setPostTimes(int postTimes) {
        this.postTimes = postTimes;
    }

    public int getForwardTimes() {
        return forwardTimes;
    }

    public void setForwardTimes(int forwardTimes) {
        this.forwardTimes = forwardTimes;
    }

    public String getThumbimgSmall() {
        return thumbimgSmall;
    }

    public void setThumbimgSmall(String thumbimgSmall) {
        this.thumbimgSmall = thumbimgSmall;
    }

    public ViewLineItemDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ViewLineItemDisplayType displayType) {
        this.displayType = displayType;
    }

    public String getVideoCoverSrc() {
        return videoCoverSrc;
    }

    public void setVideoCoverSrc(String videoCoverSrc) {
        this.videoCoverSrc = videoCoverSrc;
    }

    public List<Content> getLastContentList() {
        return lastContentList;
    }

    public void setLastContentList(List<Content> lastContentList) {
        this.lastContentList = lastContentList;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getExtField1() {
        return extField1;
    }

    public void setExtField1(String extField1) {
        this.extField1 = extField1;
    }

    public String getExtField2() {
        return extField2;
    }

    public void setExtField2(String extField2) {
        this.extField2 = extField2;
    }

    public String getExtField3() {
        return extField3;
    }

    public void setExtField3(String extField3) {
        this.extField3 = extField3;
    }

    public String getExtField4() {
        return extField4;
    }

    public void setExtField4(String extField4) {
        this.extField4 = extField4;
    }

    public String getExtField5() {
        return extField5;
    }

    public void setExtField5(String extField5) {
        this.extField5 = extField5;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public int getIntValue1() {
        return intValue1;
    }

    public void setIntValue1(int intValue1) {
        this.intValue1 = intValue1;
    }

    public int getIntValue2() {
        return intValue2;
    }

    public void setIntValue2(int intValue2) {
        this.intValue2 = intValue2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
