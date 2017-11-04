/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class UpdateExpressWarp implements Serializable {
    private UpdateExpress updateExpress;
    private QueryExpress queryExpress;

    public UpdateExpressWarp() {
    }

    public UpdateExpressWarp(UpdateExpress updateExpress, QueryExpress queryExpress) {
        this.updateExpress = updateExpress;
        this.queryExpress = queryExpress;
    }

    public UpdateExpress getUpdateExpress() {
        return updateExpress;
    }

    public void setUpdateExpress(UpdateExpress updateExpress) {
        this.updateExpress = updateExpress;
    }

    public QueryExpress getQueryExpress() {
        return queryExpress;
    }

    public void setQueryExpress(QueryExpress queryExpress) {
        this.queryExpress = queryExpress;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
