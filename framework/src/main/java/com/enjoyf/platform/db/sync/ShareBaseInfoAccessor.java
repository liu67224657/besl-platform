/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface ShareBaseInfoAccessor {

    public ShareBaseInfo insert(ShareBaseInfo shareInfo, Connection conn) throws DbException;

    public ShareBaseInfo get(QueryExpress queryExpress, Connection conn) throws DbException;


    public List<ShareBaseInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<ShareBaseInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;


    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}


