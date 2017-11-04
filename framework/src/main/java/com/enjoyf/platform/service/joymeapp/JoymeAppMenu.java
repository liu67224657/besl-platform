package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-18
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value = {"appkey", "createUserId", "createDate", "createIp", "lastModifyUserId", "lastModifyDate", "lastModifyIp", "removeStatus", "display_order"})
public class JoymeAppMenu implements Serializable {
    private long menuId;
    private long parentId;
    private int menuType;//0--native 1--webView    jt
    private String url;                           //ji
    //todo
    private String picUrl;
    private JoymeAppMenuPic pic;

    private String menuName;
    private String menuDesc;
    private boolean isHot;
    private boolean isNew;
    private int display_order;
    private String appkey;


    private String createUserId;
    private Date createDate;
    private String createIp;
    private String lastModifyUserId;
    private Date lastModifyDate;
    private String lastModifyIp;

    private ActStatus removeStatus = ActStatus.UNACT;  //删除状态

    private JoymeAppMenuDisplayType displayType = JoymeAppMenuDisplayType.DEFAULT;

    private long tagId;

    //todo
    private JoymeAppMenuModuleType moduleType = JoymeAppMenuModuleType.DEFAULT;  //菜单展示位置类别
    private String statusDesc;  //状态描述
    private int recommendStar = 0; //推荐星级

    private JoymeAppMenuContentType contentType = JoymeAppMenuContentType.DEFAULT;

    private JoymeAppMenuExpField expField;

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean New) {
        isNew = New;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }


    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public JoymeAppMenuDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(JoymeAppMenuDisplayType displayType) {
        this.displayType = displayType;
    }

    public JoymeAppMenuModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(JoymeAppMenuModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public int getRecommendStar() {
        return recommendStar;
    }

    public void setRecommendStar(int recommendStar) {
        this.recommendStar = recommendStar;
    }

    public JoymeAppMenuContentType getContentType() {
        return contentType;
    }

    public void setContentType(JoymeAppMenuContentType contentType) {
        this.contentType = contentType;
    }

    public JoymeAppMenuExpField getExpField() {
        return expField;
    }

    public void setExpField(JoymeAppMenuExpField expField) {
        this.expField = expField;
    }

    public JoymeAppMenuPic getPic() {
        return pic;
    }

    public void setPic(JoymeAppMenuPic pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
