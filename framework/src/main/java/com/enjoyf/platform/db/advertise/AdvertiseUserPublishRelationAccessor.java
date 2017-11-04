/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseUserPublishRelation;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:56
 * Description:
 */
public interface AdvertiseUserPublishRelationAccessor {
    //insert
    public AdvertiseUserPublishRelation insert(AdvertiseUserPublishRelation entry, Connection conn) throws DbException;

    //get by id
    public AdvertiseUserPublishRelation get(QueryExpress getExpress, Connection conn) throws DbException;
}
