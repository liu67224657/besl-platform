/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ShortUrlSumIncreaseEvent extends SystemEvent {
    //
    private String urlKey;
    private ObjectField field;
    private Integer count;

    //
    public ShortUrlSumIncreaseEvent() {
        super(SystemEventType.SHORTURL_SUM_INCREASE);
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public ObjectField getField() {
        return field;
    }

    public void setField(ObjectField field) {
        this.field = field;
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
        return urlKey.hashCode();
    }
}
