/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.tools.contenttrans;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.tools.contenttrans.OldContent;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class OldContentHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private OldContentAccessor accessor;

    public OldContentHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        accessor = new OldContentAccessorMySql();
    }

    public PageRows<OldContent> query(Pagination page) throws DbException {
        PageRows<OldContent> returnValue = new PageRows<OldContent>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(accessor.query(page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }
}
