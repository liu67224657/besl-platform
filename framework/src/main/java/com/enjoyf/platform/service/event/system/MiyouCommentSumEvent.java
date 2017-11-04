/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class MiyouCommentSumEvent extends SystemEvent {
    private String commentId;

    //
    public MiyouCommentSumEvent() {
        super(SystemEventType.MIYOU_ADD_LONGSUM);
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
