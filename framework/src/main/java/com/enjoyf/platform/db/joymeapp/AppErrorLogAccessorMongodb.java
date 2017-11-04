package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;

import com.mongodb.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class AppErrorLogAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<AppErrorInfo> {

    private String KEY_COLLECTION_ERROR_LOG = "app_error_log_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    private static String KEY_SEQ_NAME = "SEQ_APP_ERRORINFO_ID";

    public AppErrorInfo insert(AppErrorInfo appErrorInfo, DB db) {

        try {
            long curValue = getSeqNo(KEY_SEQ_NAME, db);
            appErrorInfo.setId(curValue);
            DBCollection installLogCollection = db.getCollection(getCollection(appErrorInfo.getErrorDate()));

            installLogCollection.insert(entryToDBObject(appErrorInfo));
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " occured DbException.e:", e);
        }

        return appErrorInfo;
    }

    @Override
    protected AppErrorInfo dbObjToEntry(DBObject dbObject) {
        return null;
    }

    @Override
    protected BasicDBObject entryToDBObject(AppErrorInfo entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getId());
        dbObject.put("clientid", entry.getClientId());
        dbObject.put("appkey", entry.getAppKey());
        dbObject.put("platform", entry.getPlatform());
        dbObject.put("appversion", entry.getAppVersion());
        dbObject.put("errorinfo", entry.getErrorInfo());
        dbObject.put("errordate", entry.getErrorDate());
        dbObject.put("device", entry.getDevice());
        dbObject.put("screen", entry.getScreen());
        dbObject.put("osversion", entry.getOsVersion());
        dbObject.put("ip", entry.getIp());
        dbObject.put("access_token", entry.getAccess_token());
        dbObject.put("token_secr", entry.getToken_scr());
        dbObject.put("reportdate", entry.getReportDate() == null ? new Date() : entry.getReportDate());
        dbObject.put("channel_id", entry.getChannelId());
        dbObject.put("stat_date", DateUtil.formatDateToString(entry.getErrorDate(), DateUtil.PATTERN_DATE));
        return dbObject;
    }

    public List<AppErrorInfo> query(BasicDBObject queryObject, Date date, DB db) {
        List<AppErrorInfo> appErrorInfoList = new ArrayList<AppErrorInfo>();
        DBCursor cursor = db.getCollection(getCollection(date)).find(queryObject);
        while (cursor.hasNext()) {
            AppErrorInfo appErrorInfo = new AppErrorInfo();
            DBObject dbObject = cursor.next();
            appErrorInfo.setClientId((String) dbObject.get("clientid"));
            appErrorInfo.setAppKey((String) dbObject.get("appkey"));
            appErrorInfo.setPlatform(((Integer) dbObject.get("platform")));
            appErrorInfo.setAppVersion((String) dbObject.get("appversion"));
            appErrorInfo.setErrorInfo((String) dbObject.get("errorinfo"));
            appErrorInfo.setErrorDate((Date) dbObject.get("errordate"));
            appErrorInfo.setDevice((String) dbObject.get("device"));
            appErrorInfo.setScreen((String) dbObject.get("screen"));
            appErrorInfo.setOsVersion((String) dbObject.get("osversion"));
            appErrorInfo.setIp((String) dbObject.get("ip"));
            appErrorInfo.setAccess_token((String) dbObject.get("access_token"));
            appErrorInfo.setToken_scr((String) dbObject.get("token_secr"));
            appErrorInfo.setReportDate((Date) dbObject.get("reportdate"));
            appErrorInfoList.add(appErrorInfo);
        }
        return appErrorInfoList;
    }

    public boolean modify(BasicDBObject queryDBObject, BasicDBObject updateDBObject, DB db) {
        DBCollection dbCollection = db.getCollection(getCollection(new Date()));
        return dbCollection.update(queryDBObject, updateDBObject, false, true).getN() > 0;
    }

    private String getCollection(Date date) {
        return KEY_COLLECTION_ERROR_LOG + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

}
