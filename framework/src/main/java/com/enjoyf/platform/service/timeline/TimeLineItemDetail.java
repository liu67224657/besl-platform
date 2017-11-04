/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:47
 * Description: this a composite object which includes many messages.
 */
public class TimeLineItemDetail implements Serializable {

    //owner uno
    private String ownUno;
    //the time line domain.
    private TimeLineDomain domain;

    private Long tlId;

    private String detailUno;

     //the direct uno and id,
    // such the content id or the reply id.
    private String directUno;
    private String directId;

    private ActStatus removeStatus=ActStatus.UNACT;

    private TimeLineDetailType TimeLineDetailType;

    private Date createDate;
    private Date removeDate;

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public TimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(TimeLineDomain domain) {
        this.domain = domain;
    }

    public Long getTlId() {
        return tlId;
    }

    public void setTlId(Long tlId) {
        this.tlId = tlId;
    }

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public TimeLineDetailType getTimeLineDetailType() {
        return TimeLineDetailType;
    }

    public void setTimeLineDetailType(TimeLineDetailType timeLineDetailType) {
        TimeLineDetailType = timeLineDetailType;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    public String getDetailUno() {
        return detailUno;
    }

    public void setDetailUno(String detailUno) {
        this.detailUno = detailUno;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
