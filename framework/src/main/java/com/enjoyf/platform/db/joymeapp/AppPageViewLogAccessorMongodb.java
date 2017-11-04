package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.joymeapp.AppPageViewEntry;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class AppPageViewLogAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<AppPageViewInfo> {

    private static final String KEY_COLLECTION_PAGEVIEW_LOG = "app_pageview_log_";
    private static final String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    private static String KEY_SEQ_NAME = "SEQ_APP_PAGEVIEWINFO_ID";

    public AppPageViewInfo insert(AppPageViewInfo info, DB db) throws DbException {

        Map<String, List<DBObject>> basicDBObjectList = new HashMap<String, List<DBObject>>();
        for (AppPageViewEntry appPageViewEntry : info.getPageViewList()) {
            String collectionKey = KEY_COLLECTION_PAGEVIEW_LOG + TableUtil.getTableDateSuffix(appPageViewEntry.getCreateTime(), KEY_TABLE_SUFFIX_FMT);

            if (!basicDBObjectList.containsKey(collectionKey)) {
                basicDBObjectList.put(collectionKey, new ArrayList<DBObject>());
            }

            BasicDBObject dbObject = null;
            try {
                appPageViewEntry.setId(getSeqNo(KEY_SEQ_NAME, db));
                dbObject = objectToDBObject(info, appPageViewEntry);
            } catch (DbException e) {
                GAlerter.lab(this.getClass().getName() + " occured DbExcpetion.e", e);
            }

            if (dbObject != null) {
                basicDBObjectList.get(collectionKey).add(dbObject);
            }
        }

        for (Map.Entry<String, List<DBObject>> entry : basicDBObjectList.entrySet()) {
            if (CollectionUtil.isEmpty(entry.getValue())) {
                continue;
            }

            DBCollection pvLogCollection = db.getCollection(entry.getKey());
            pvLogCollection.insert(entry.getValue());
        }
        return info;
    }

    @Override
    protected AppPageViewInfo dbObjToEntry(DBObject dbObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BasicDBObject entryToDBObject(AppPageViewInfo entry) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private BasicDBObject objectToDBObject(AppPageViewInfo info, AppPageViewEntry entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getId());
        dbObject.put("clientid", info.getClientId());
        dbObject.put("appkey", info.getAppKey());
        dbObject.put("platform", info.getPlatform());
        dbObject.put("appversion", info.getAppVersion());
        dbObject.put("channelid", info.getChannelid());
        dbObject.put("device", info.getDevice());
        dbObject.put("screen", info.getScreen());
        dbObject.put("osversion", info.getOsVersion());
        dbObject.put("ip", info.getIp());
        dbObject.put("reportdate", info.getReportDate());
        dbObject.put("access_token", info.getAccess_token());
        dbObject.put("token_secr", info.getToken_secr());
        dbObject.put("reportdate", info.getReportDate() == null ? new Date() : info.getReportDate());

        dbObject.put("pv_createtime", entry.getCreateTime());
        dbObject.put("pv_refer", entry.getRefer());
        dbObject.put("pv_location", entry.getLocation());
        dbObject.put("pv_locationtype", entry.getLocationtype());
        dbObject.put("pv_rtime", entry.getRtime());
        dbObject.put("stat_date", DateUtil.formatDateToString(entry.getCreateTime(), DateUtil.PATTERN_DATE));

        return dbObject;
    }
}
