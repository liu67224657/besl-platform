/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class NoticeInsertEvent extends SystemEvent {
    //
    private String ownUno;
    private NoticeType noticeType;
    private Integer count;

    //
    public NoticeInsertEvent() {
        super(SystemEventType.NOTICE_INSERT);
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return ownUno.hashCode();
    }
}
