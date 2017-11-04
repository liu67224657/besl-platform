package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.db.joymeapp.AppErrorLogAccessorMongodb;
import com.enjoyf.platform.db.joymeapp.AppInstallLogAccessorMongodb;
import com.enjoyf.platform.db.joymeapp.AppPageViewLogAccessorMongodb;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.service.joymeapp.AppInstallInfo;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;
import com.enjoyf.platform.service.misc.SMSLog;
import com.enjoyf.platform.util.FiveProps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:06
 * To change this template use File | Settings | File Templates.
 */
public class MiscMogoHandler {

    private String dataSourceName;

    private static final String DB_JOYMEAPP = "misc";

    private SmsLogAccessorMongodb smsLogAccessorMongodb;

    public MiscMogoHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn;

        MongoSourceManager.get().append(dsn, props);
        smsLogAccessorMongodb = new SmsLogAccessorMongodb();

    }

    public SMSLog insertSMSLog(SMSLog smsLog) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return smsLogAccessorMongodb.insert(smsLog, db);
    }
}
