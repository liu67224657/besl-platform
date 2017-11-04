/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.tools.tablehashcode;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class HashCodeHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private HashCodeAccessor timeLineItemAccessor;

    public HashCodeHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        timeLineItemAccessor = new HashCodeAccessorMySql();
    }


    public void update(String tableName, String scrColumnName, String destColumnName) throws DbException {
        Connection conn = null;

        Pagination page = new Pagination();
        page.setPageSize(1000);

        try {
            conn = DbConnFactory.factory(dataSourceName);

            do {
                List<String> keys = timeLineItemAccessor.queryUniqueKeys(tableName, scrColumnName, page, conn);

                for (String key : keys) {
                    System.out.println(">>>>>>>" + key);

                    timeLineItemAccessor.updateHashCode(tableName, scrColumnName, key, destColumnName, conn);
                }

                if (page.isLastPage()) {
                    break;
                } else {
                    page.setCurPage(page.getCurPage() + 1);
                }
            } while (true);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
