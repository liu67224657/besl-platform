/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.db.joymeapp.AppInstallLogAccessorMongodb;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlClickInfo;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.util.FiveProps;
import com.mongodb.DB;

import java.sql.Connection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class AdvertiseStatHandler {
    private static final String DB_ADVERTISE = "advertise";
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private AppUrlStatMongoAccessor appUrlStatMongoAccessor;

    //
//    private AdvertiseEventStatAccessor eventStatAccessor;


    public AdvertiseStatHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        MongoSourceManager.get().append(dsn, props);

        appUrlStatMongoAccessor = new AppUrlStatMongoAccessor();
    }

    //insert
    public Example insert(Example entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return null;//agentAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //insert
    public AdvertiseAppUrlClickInfo insert(AdvertiseAppUrlClickInfo entry) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE);
        return appUrlStatMongoAccessor.insert(entry,db);
    }


}
