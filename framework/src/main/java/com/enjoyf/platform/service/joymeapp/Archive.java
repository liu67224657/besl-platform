package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-9 下午7:37
 * Description:
 */
public class Archive implements Serializable {
    private int archiveId;
    private String title;
    private String desc;
    private Date createTime;
    private List<ArchiveTag> archiveTagList;
    private String icon;

    private String typeName;
    private String typeColor;

    private String author;

    private String webviewUrl = "";
    private int showios;
    private int showandroid;
    private String htlistimg;

    private Integer typeid;

    /**
     * arcrank
     * 0 开放浏览
     * -1 待审核稿件
     * 10 注册会员
     * 50 中级会员
     * 100 高级会员
     */
    private Integer arcrank;
    
    /**
     * 是否生成静态页面 1是已生成
     */
    private int isMake;

    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public List<ArchiveTag> getArchiveTagList() {
        return archiveTagList;
    }

    public void setArchiveTagList(List<ArchiveTag> archiveTagList) {
        this.archiveTagList = archiveTagList;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWebviewUrl() {
        return webviewUrl;
    }

    public void setWebviewUrl(String webviewUrl) {
        this.webviewUrl = webviewUrl;
    }

    public int getShowios() {
        return showios;
    }

    public void setShowios(int showios) {
        this.showios = showios;
    }

    public int getShowandroid() {
        return showandroid;
    }

    public void setShowandroid(int showandroid) {
        this.showandroid = showandroid;
    }

    public String getHtlistimg() {
        return htlistimg;
    }

    public void setHtlistimg(String htlistimg) {
        this.htlistimg = htlistimg;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Integer getArcrank() {
        return arcrank;
    }

    public void setArcrank(Integer arcrank) {
        this.arcrank = arcrank;
    }

	public int getIsMake() {
		return isMake;
	}

	public void setIsMake(int isMake) {
		this.isMake = isMake;
	}
    
}
