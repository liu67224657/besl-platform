/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseProject;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class AdvertiseProjectAccessorMySql extends AbstractAdvertiseProjectAccessor {
    @Override
    public List<AdvertiseProject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }
}
