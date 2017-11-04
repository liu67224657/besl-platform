package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-5 上午9:31
 * Description:
 */
//ALTER TABLE `MISC`.`index_cache` ADD COLUMN `type` smallint DEFAULT '1' COMMENT '1-pc 0-mobile' AFTER `version`;
public class IndexCache implements Serializable {
    private long indexCacheId;

    private String serverId;//唯一索引

    private String html;

    private String version;//预留字段

    private IndexCacheType indexCacheType=IndexCacheType.PC;

    private ActStatus removeStatus = ActStatus.UNACT;

    public long getIndexCacheId() {
        return indexCacheId;
    }

    public void setIndexCacheId(long indexCacheId) {
        this.indexCacheId = indexCacheId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public IndexCacheType getIndexCacheType() {
        return indexCacheType;
    }

    public void setIndexCacheType(IndexCacheType indexCacheType) {
        this.indexCacheType = indexCacheType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}

