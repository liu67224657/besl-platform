package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.service.joymeapp.AppInstallInfo;
import com.enjoyf.platform.service.joymeapp.AppStatInstallLog;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.mongodb.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class AppInstallLogAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<AppInstallInfo> {

    private String KEY_COLLECTION_INSTALL_LOG = "app_install_log_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    private static String KEY_SEQ_NAME = "SEQ_APP_INSTALLINFO_ID";

    public AppInstallInfo insert(AppInstallInfo appInstallInfo, DB db) {
        try {
            long curValue = getSeqNo(KEY_SEQ_NAME, db);
            appInstallInfo.setId(curValue);
            DBCollection installLogCollection = db.getCollection(KEY_COLLECTION_INSTALL_LOG + TableUtil.getTableDateSuffix(appInstallInfo.getInstallDate(), KEY_TABLE_SUFFIX_FMT));
            installLogCollection.insert(entryToDBObject(appInstallInfo));
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " occured DbException.e:", e);
        }
        return appInstallInfo;
    }

    @Override
    protected AppInstallInfo dbObjToEntry(DBObject dbObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BasicDBObject entryToDBObject(AppInstallInfo entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getId());
        dbObject.put("clientid", entry.getClientId());
        dbObject.put("appkey", entry.getAppKey());
        dbObject.put("platform", entry.getPlatform());
        dbObject.put("appversion", entry.getAppVersion());
        dbObject.put("channelid", entry.getChannelid());
        dbObject.put("installtype", entry.getInstallType());
        dbObject.put("device", entry.getDevice());
        dbObject.put("screen", entry.getScreen());
        dbObject.put("osversion", entry.getOsVersion());
        dbObject.put("ip", entry.getIp());
        dbObject.put("installdate", entry.getInstallDate());
        dbObject.put("access_token", entry.getAccess_token());
        dbObject.put("token_secr", entry.getToken_scr());
        dbObject.put("reportdate", entry.getReportDate() == null ? new Date() : entry.getReportDate());
        dbObject.put("stat_date", DateUtil.formatDateToString(entry.getInstallDate(),DateUtil.PATTERN_DATE));

        return dbObject;
    }

}
