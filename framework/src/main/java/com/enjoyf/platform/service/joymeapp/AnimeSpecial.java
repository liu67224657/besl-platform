package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="pengxu@staff.joyme.com">PengXu</a>
 * Create time:  14-10-27 下午5:51
 * Description:
 */
public class AnimeSpecial implements Serializable {
    private Long specialId;
    private String specialName;
    private String specialTtile;
    private String coverPic;//封面图
    private String specialPic;//专题头图
    private String specialDesc;
    private String linkUrl;
    private int platform;
    private AnimeSpecialType specialType;
    private String specialTypeBgColor; //分类背景颜色
    private AnimeRedirectType animeRedirectType;
    //    private List<AnimeSpecialAttrJason> specialAttrJson;
    private ValidStatus removeStatus;
    private Date createDate;
    private Date updateDate;
    private String createUser;
    private String appkey;
    private int displayOrder;
    private int read_num=0;//阅读数
    private AnimeSpecialDisplayType display_type=AnimeSpecialDisplayType.SMALL_PIC;//图片的显示顺序

    public Long getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Long specialId) {
        this.specialId = specialId;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getSpecialDesc() {
        return specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public AnimeSpecialType getSpecialType() {
        return specialType;
    }

    public void setSpecialType(AnimeSpecialType specialType) {
        this.specialType = specialType;
    }

    public AnimeRedirectType getAnimeRedirectType() {
        return animeRedirectType;
    }

    public void setAnimeRedirectType(AnimeRedirectType animeRedirectType) {
        this.animeRedirectType = animeRedirectType;
    }

//
//    public List<AnimeSpecialAttrJason> getSpecialAttrJson() {
//        return specialAttrJson;
//    }
//
//    public void setSpecialAttrJson(List<AnimeSpecialAttrJason> specialAttrJson) {
//        this.specialAttrJson = specialAttrJson;
//    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSpecialTypeBgColor() {
        return specialTypeBgColor;
    }

    public void setSpecialTypeBgColor(String specialTypeBgColor) {
        this.specialTypeBgColor = specialTypeBgColor;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getSpecialPic() {
        return specialPic;
    }

    public void setSpecialPic(String specialPic) {
        this.specialPic = specialPic;
    }


    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSpecialTtile() {
        return specialTtile;
    }

    public void setSpecialTtile(String specialTtile) {
        this.specialTtile = specialTtile;
    }

    public int getRead_num() {
        return read_num;
    }

    public void setRead_num(int read_num) {
        this.read_num = read_num;
    }

    public AnimeSpecialDisplayType getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(AnimeSpecialDisplayType display_type) {
        this.display_type = display_type;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
