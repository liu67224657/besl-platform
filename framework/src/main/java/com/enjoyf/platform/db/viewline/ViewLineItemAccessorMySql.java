/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class ViewLineItemAccessorMySql extends AbstractViewLineItemAccessor {
    @Override
    public List<ViewLineItem> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<ViewLineItem> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, range, conn);
    }


}
