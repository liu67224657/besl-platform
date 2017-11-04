package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseTagRelation implements Serializable {

    private long newTagRelationId;
    private long newGameInfoId;
    private long newGameTagId;
    private ActStatus status = ActStatus.UNACT;

    public long getNewTagRelationId() {
        return newTagRelationId;
    }

    public void setNewTagRelationId(long newTagRelationId) {
        this.newTagRelationId = newTagRelationId;
    }

    public long getNewGameInfoId() {
        return newGameInfoId;
    }

    public void setNewGameInfoId(long newGameInfoId) {
        this.newGameInfoId = newGameInfoId;
    }

    public long getNewGameTagId() {
        return newGameTagId;
    }

    public void setNewGameTagId(long newGameTagId) {
        this.newGameTagId = newGameTagId;
    }

    public ActStatus getStatus() {
        return status;
    }

    public void setStatus(ActStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
