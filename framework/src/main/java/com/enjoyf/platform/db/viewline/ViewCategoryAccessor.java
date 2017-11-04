/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ViewCategoryAccessor {
    //insert
    public ViewCategory insert(ViewCategory entry, Connection conn) throws DbException;

    //get
    public ViewCategory get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    public List<ViewCategory> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //query by page
    public List<ViewCategory> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    public int delete(QueryExpress queryExpress, Connection conn) throws  DbException;
}
