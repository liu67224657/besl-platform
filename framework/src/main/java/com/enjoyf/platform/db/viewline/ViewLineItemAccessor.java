/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ViewLineItemAccessor {
    //insert
    public ViewLineItem insert(ViewLineItem entry, Connection conn) throws DbException;

    public List<Integer> queryCategoryIds(QueryExpress queryExpress, Connection conn) throws DbException;

    //query all items.
    public List<ViewLineItem> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //query  by  Pagination
    public List<ViewLineItem> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    //query by  Rangination
    public List<ViewLineItem> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public ViewLineItem get(QueryExpress queryExpress, Connection conn) throws DbException;
}
