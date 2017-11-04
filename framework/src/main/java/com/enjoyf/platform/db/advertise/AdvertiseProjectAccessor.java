/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseProject;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:56
 * Description:
 */
public interface AdvertiseProjectAccessor {
    //insert
    public AdvertiseProject insert(AdvertiseProject entry, Connection conn) throws DbException;

    //get by id
    public AdvertiseProject get(QueryExpress getExpress, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<AdvertiseProject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<AdvertiseProject> query(QueryExpress queryExpress, Connection conn) throws DbException;
}
