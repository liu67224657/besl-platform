package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:01
 */
public class TagDedearchives implements Serializable {
    private String id;
    private Long tagid;//标签ID
    private TagDisplyType tagDisplyType = TagDisplyType.SMALL_PIC;//图片的显示类型
    private String dede_archives_id;//文章ID
    private String dede_archives_title;//文章的标题
    private String dede_archives_description;//文章的描述
    private String dede_archives_litpic;//文章的头图
    private Long dede_archives_pubdate;//文章的发布时间
    private String dede_archives_pubdate_str;
    private int dede_archives_showios = 1; //ios显示
    private int dede_archives_showandroid = 1;//android显示
    private String dede_archives_htlistimg = "";//显示方式为通栏的时：缩列图
    private String dede_archives_url = "";//文章的链接
    private AppRedirectType dede_redirect_type = AppRedirectType.REDIRECT_WEBVIEW;
    private String dede_redirect_url = "";//跳转的链接


    private Long display_order;
    private ValidStatus remove_status = ValidStatus.VALID;
    private Long display_tag = 0L;  //玩霸2.2里面表示圈子ID

    private ArchiveRelationType archiveRelationType = ArchiveRelationType.TAG_RELATION; //表示元素类型
    private ArchiveContentType archiveContentType = ArchiveContentType.NEWS_ARCHIVE; //每种元素类型的子类型

    private String profileId;//PROFILEid

    private String timerDate;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Long getDisplay_tag() {
        return display_tag;
    }

    public void setDisplay_tag(Long display_tag) {
        this.display_tag = display_tag;
    }

    public Long getTagid() {
        return tagid;
    }

    public void setTagid(Long tagid) {
        this.tagid = tagid;
    }

    public TagDisplyType getTagDisplyType() {
        return tagDisplyType;
    }

    public void setTagDisplyType(TagDisplyType tagDisplyType) {
        this.tagDisplyType = tagDisplyType;
    }

    public String getDede_archives_id() {
        return dede_archives_id;
    }

    public void setDede_archives_id(String dede_archives_id) {
        this.dede_archives_id = dede_archives_id;
    }

    public String getDede_archives_title() {
        return dede_archives_title;
    }

    public void setDede_archives_title(String dede_archives_title) {
        this.dede_archives_title = dede_archives_title;
    }

    public String getDede_archives_description() {
        return dede_archives_description;
    }

    public void setDede_archives_description(String dede_archives_description) {
        this.dede_archives_description = dede_archives_description;
    }

    public String getDede_archives_litpic() {
        return dede_archives_litpic;
    }

    public void setDede_archives_litpic(String dede_archives_litpic) {
        this.dede_archives_litpic = dede_archives_litpic;
    }

    public Long getDede_archives_pubdate() {
        return dede_archives_pubdate;
    }

    public void setDede_archives_pubdate(Long dede_archives_pubdate) {
        this.dede_archives_pubdate = dede_archives_pubdate;
    }


    public Long getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(Long display_order) {
        this.display_order = display_order;
    }

    public ValidStatus getRemove_status() {
        return remove_status;
    }

    public void setRemove_status(ValidStatus remove_status) {
        this.remove_status = remove_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDede_archives_showios() {
        return dede_archives_showios;
    }

    public void setDede_archives_showios(int dede_archives_showios) {
        this.dede_archives_showios = dede_archives_showios;
    }

    public int getDede_archives_showandroid() {
        return dede_archives_showandroid;
    }

    public void setDede_archives_showandroid(int dede_archives_showandroid) {
        this.dede_archives_showandroid = dede_archives_showandroid;
    }

    public String getDede_archives_htlistimg() {
        return dede_archives_htlistimg;
    }

    public void setDede_archives_htlistimg(String dede_archives_htlistimg) {
        this.dede_archives_htlistimg = dede_archives_htlistimg;
    }

    public AppRedirectType getDede_redirect_type() {
        return dede_redirect_type;
    }

    public void setDede_redirect_type(AppRedirectType dede_redirect_type) {
        this.dede_redirect_type = dede_redirect_type;
    }

    public String getDede_redirect_url() {
        return dede_redirect_url;
    }

    public void setDede_redirect_url(String dede_redirect_url) {
        this.dede_redirect_url = dede_redirect_url;
    }

    public String getDede_archives_url() {
        return dede_archives_url;
    }

    public void setDede_archives_url(String dede_archives_url) {
        this.dede_archives_url = dede_archives_url;
    }

    public String getDede_archives_pubdate_str() {
        return dede_archives_pubdate_str;
    }

    public void setDede_archives_pubdate_str(String dede_archives_pubdate_str) {
        this.dede_archives_pubdate_str = dede_archives_pubdate_str;
    }

    public ArchiveRelationType getArchiveRelationType() {
        return archiveRelationType;
    }

    public void setArchiveRelationType(ArchiveRelationType archiveRelationType) {
        this.archiveRelationType = archiveRelationType;
    }

    public ArchiveContentType getArchiveContentType() {
        return archiveContentType;
    }

    public void setArchiveContentType(ArchiveContentType archiveContentType) {
        this.archiveContentType = archiveContentType;
    }

    public String getTimerDate() {
        return timerDate;
    }

    public void setTimerDate(String timerDate) {
        this.timerDate = timerDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
