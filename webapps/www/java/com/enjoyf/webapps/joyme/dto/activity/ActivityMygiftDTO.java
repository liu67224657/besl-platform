package com.enjoyf.webapps.joyme.dto.activity;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-9-17
 * Time: 下午8:08
 * To change this template use File | Settings | File Templates.
 */
public class ActivityMygiftDTO {
    private String gipic;
    private String title;
    private String desc;
    private Date endTime;
    private Long clientEndTime;
    private Long itemId;
    private Long gid;
    private Long aid;//活动id
    private String itemName1;
    private String itemValue1;
    private Long lid;//logId
    private int cn;//总数
    private int sn;//剩余

    private boolean expire;//是否  过期
    private boolean remove;//是否  下架

    public String getGipic() {
        return gipic;
    }

    public void setGipic(String gipic) {
        this.gipic = gipic;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getItemName1() {
        return itemName1;
    }

    public void setItemName1(String itemName1) {
        this.itemName1 = itemName1;
    }

    public String getItemValue1() {
        return itemValue1;
    }

    public void setItemValue1(String itemValue1) {
        this.itemValue1 = itemValue1;
    }

    public boolean getExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public Long getClientEndTime() {
        return clientEndTime;
    }

    public void setClientEndTime(Long clientEndTime) {
        this.clientEndTime = clientEndTime;
    }

    public boolean getRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public int getCn() {
        return cn;
    }

    public void setCn(int cn) {
        this.cn = cn;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }
}
