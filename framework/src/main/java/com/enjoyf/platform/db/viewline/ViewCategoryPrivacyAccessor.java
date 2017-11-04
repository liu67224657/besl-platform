/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacy;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ViewCategoryPrivacyAccessor {
    //insert
    public ViewCategoryPrivacy insert(ViewCategoryPrivacy entry, Connection conn) throws DbException;

    //get by id
    public ViewCategoryPrivacy get(QueryExpress getExpress, Connection conn) throws DbException;

    //query
    public List<ViewCategoryPrivacy> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Integer> queryCategoryIds(QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //delete
    public int delete(QueryExpress queryExpress, Connection conn) throws  DbException;
}
