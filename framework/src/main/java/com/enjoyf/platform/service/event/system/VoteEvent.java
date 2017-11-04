/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">pengxu</a>
 * Create time: 2016-07-12
 * Description:
 */
public class VoteEvent extends SystemEvent {
    //
    private String key; //投票的key

    //
    public VoteEvent() {
        super(SystemEventType.VOTE_KEY);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
