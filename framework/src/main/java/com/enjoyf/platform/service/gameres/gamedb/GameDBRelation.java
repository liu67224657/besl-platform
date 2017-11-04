package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.IntValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/26
 * Description:
 */
public class GameDBRelation implements Serializable {
    private long relationId;
    private long gamedbId;

    private String title;
    private GameDBRelationType type;
    private String uri;

    private IntValidStatus validStatus = IntValidStatus.VALID;
    private int displayOrder;
    private Date modifyTime;
    private String modifyIp;
    private String modifyUserid;

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public long getGamedbId() {
        return gamedbId;
    }

    public void setGamedbId(long gamedbId) {
        this.gamedbId = gamedbId;
    }

    public GameDBRelationType getType() {
        return type;
    }

    public void setType(GameDBRelationType type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserid() {
        return modifyUserid;
    }

    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid;
    }

    public IntValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(IntValidStatus validStatus) {
        this.validStatus = validStatus;
    }
}
