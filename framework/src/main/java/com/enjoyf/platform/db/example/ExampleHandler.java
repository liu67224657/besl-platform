/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.example;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class ExampleHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ExampleAccessor exampleAccessor;

    public ExampleHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        exampleAccessor = TableAccessorFactory.get().factoryAccessor(ExampleAccessor.class, dataBaseType);
    }

    //insert
    public Example insert(Example entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exampleAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by id
    public Example get(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exampleAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //query
    public PageRows<Example> query(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<Example> returnValue = new PageRows<Example>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(exampleAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public RangeRows<Example> query(QueryExpress queryExpress, Rangination range) throws DbException {
        RangeRows<Example> returnValue = new RangeRows<Example>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(exampleAccessor.query(queryExpress, range, conn));
            returnValue.setRange(range);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return exampleAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
