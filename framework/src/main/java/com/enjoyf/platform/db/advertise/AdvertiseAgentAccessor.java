/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseAgent;
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
public interface AdvertiseAgentAccessor {
    //insert
    public AdvertiseAgent insert(AdvertiseAgent entry, Connection conn) throws DbException;

    //get by id
    public AdvertiseAgent get(QueryExpress getExpress, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<AdvertiseAgent> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<AdvertiseAgent> query(QueryExpress queryExpress, Connection conn) throws DbException;
}
