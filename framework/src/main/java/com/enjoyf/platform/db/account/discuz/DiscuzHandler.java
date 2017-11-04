package com.enjoyf.platform.db.account.discuz;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.account.discuz.DiscuzMember;
import com.enjoyf.platform.service.account.discuz.DiscuzMemberField;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-4 上午11:59
 * Description:
 */
public class DiscuzHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private DiscuzMemberAccessor discuzAccessor;

    public DiscuzHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        discuzAccessor = TableAccessorFactory.get().factoryAccessor(DiscuzMemberAccessor.class, dataBaseType);
    }


    public DiscuzMember getByProfileUno(String profileUno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return discuzAccessor.get(new QueryExpress().add(QueryCriterions.eq(DiscuzMemberField.UNO, profileUno)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
