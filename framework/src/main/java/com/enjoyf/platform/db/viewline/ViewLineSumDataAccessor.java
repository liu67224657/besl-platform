/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineSumData;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ViewLineSumDataAccessor {
    //insert
    public ViewLineSumData insert(ViewLineSumData entry, Connection conn) throws DbException;

    //get
    public ViewLineSumData get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    public List<ViewLineSumData> query(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
