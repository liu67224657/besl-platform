package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-9 下午5:51
 * Description:
 */
public class ClientLineItem implements Serializable, Comparable {
    private long itemId;

    //the line id
    private long lineId;

    private String title;
    private String desc;
    private String picUrl;
    private String url;
    private String rate;//评分
    //the direct uno and id,
    private String directId;//手游排行榜里此字段为游戏库ID


    private ClientItemType itemType;
    //if type is article  cms wiki
    private ClientItemDomain itemDomain;

    private AppRedirectType redirectType;

    //
    private Date itemCreateDate;

    //一直按照display order asc排序。
    private int displayOrder;

    // 标签
    private String category;    //  在新游开测榜中 0,不出现在热门页中的热门块，1出现
    //标签颜色
    private String categoryColor;

    private AppDisplayType appDisplayType;


    //是否有效.
    private ValidStatus validStatus = ValidStatus.VALID;

    private String typeName;
    private String typeColor;

    private String author;

    //推荐墙
    private Date stateDate;  //显示用的时间字段
    private int displayType; //1-new 2-hot     在新游开测榜中 0,不出现在热门页，1出现

    private long tagId;//是否出现标签

    private ParamTextJson param;


    private long contentid;//手游排行榜--forign_content,摇一摇权重

    public long getContentid() {
        return contentid;
    }

    public void setContentid(long contentid) {
        this.contentid = contentid;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public Date getItemCreateDate() {
        return itemCreateDate;
    }

    public void setItemCreateDate(Date itemCreateDate) {
        this.itemCreateDate = itemCreateDate;
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

    public ClientItemType getItemType() {
        return itemType;
    }

    public void setItemType(ClientItemType itemType) {
        this.itemType = itemType;
    }

    public ClientItemDomain getItemDomain() {
        return itemDomain;
    }

    public void setItemDomain(ClientItemDomain itemDomain) {
        this.itemDomain = itemDomain;
    }

    public AppRedirectType getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(AppRedirectType redirectType) {
        this.redirectType = redirectType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public AppDisplayType getAppDisplayType() {
        return appDisplayType;
    }

    public void setAppDisplayType(AppDisplayType appDisplayType) {
        this.appDisplayType = appDisplayType;
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

    public ParamTextJson getParam() {
        return param;
    }

    public void setParam(ParamTextJson param) {
        this.param = param;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String toJson(){
        return JsonBinder.buildNormalBinder().toJson(this);
    }

public static ClientLineItem parse(String jsonStr){
    ClientLineItem clientLineItem = null;
    if(!Strings.isNullOrEmpty(jsonStr)){
        try {
            clientLineItem = JsonBinder.buildNormalBinder().getMapper().enableDefaultTyping().readValue(jsonStr, new TypeReference<ClientLineItem>() {
            });
        } catch (IOException e) {
            GAlerter.lab("ClientLineItem parse occur IOException.e", e);
        }
    }
    return clientLineItem;
}

    //用于在List中去掉重复的的List.contains()方法
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ClientLineItem && ((ClientLineItem) obj).itemId == this.itemId) {
            return true;
        }
        return false;
    }


    //用于GameClientGameWebViewController 中的 新增游戏关注和设置正在玩的排序
    @Override
    public int compareTo(Object o) {
        ClientLineItem dto = (ClientLineItem) o;
        int result = this.displayOrder - dto.displayOrder;
        if (result > 0) {
            return 1;
        } else if (result == 0L) {
            return 0;
        } else {
            return -1;
        }
    }

}
