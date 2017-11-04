package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePv;
import com.enjoyf.platform.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-16
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertisePvAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<AppAdvertisePv> {
    private String COLLECTION_NAME = "app_advertise_pv_";
    private String SEQ_NAME = "seq_advertise_db_id";

    private static String getDate() {
        return DateUtil.formatDateToString(new Date(), "yyyyMM");
    }

    public AppAdvertisePv insert(AppAdvertisePv appAdvertisePv, DB db) throws DbException {
        long curValue = getSeqNo(SEQ_NAME, db);
        appAdvertisePv.setPvId(curValue);
        WriteResult writeResult = db.getCollection(COLLECTION_NAME + getDate()).insert(entryToDBObject(appAdvertisePv));
        if (writeResult.getN() > 0) {
            return appAdvertisePv;
        }
        return null;
    }

    @Override
    protected AppAdvertisePv dbObjToEntry(DBObject dbObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BasicDBObject entryToDBObject(AppAdvertisePv entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getPvId());
        dbObject.put("appkey", entry.getAppkey());
        dbObject.put("platform", entry.getPlatform());
        dbObject.put("deviceId", entry.getDeviceId());
        dbObject.put("publishid", entry.getPublishid());
        dbObject.put("adId", entry.getAdId());
        dbObject.put("ip", entry.getIp());
        dbObject.put("createtime", new Date(System.currentTimeMillis()));
        return dbObject;
    }
}
