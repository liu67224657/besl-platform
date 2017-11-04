package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-29
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class SocialBlack implements Serializable {
    private long black_id;
    private String srcUno;
    private String desUno;
    private Date createTime;
    private Date updateTime;
    private ActStatus removeStatus = ActStatus.UNACT;//删除标志位

    public long getBlack_id() {
        return black_id;
    }

    public void setBlack_id(long black_id) {
        this.black_id = black_id;
    }

    public String getSrcUno() {
        return srcUno;
    }

    public void setSrcUno(String srcUno) {
        this.srcUno = srcUno;
    }

    public String getDesUno() {
        return desUno;
    }

    public void setDesUno(String desUno) {
        this.desUno = desUno;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }
}
