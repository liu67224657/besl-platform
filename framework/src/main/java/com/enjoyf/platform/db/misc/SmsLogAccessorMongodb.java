package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.SMSLog;
import com.enjoyf.platform.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class SmsLogAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<SMSLog> {

    private static final String KEY_COLLECTION_SMS_LOG = "sms_log";

    public SMSLog insert(SMSLog log, DB db) throws DbException {
        DBCollection collection = db.getCollection(KEY_COLLECTION_SMS_LOG);
        collection.insert(entryToDBObject(log));
        return log;
    }

    @Override
    protected SMSLog dbObjToEntry(DBObject dbObject) {
        return null;
    }

    @Override
    protected BasicDBObject entryToDBObject(SMSLog entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("phonenum", entry.getPhone());
        dbObject.put("result", entry.getResult());
        dbObject.put("create_date", DateUtil.formatDateToString(entry.getCreateTime(), DateUtil.PATTERN_DATE));
        dbObject.put("third_code", entry.getThirdCode());
        dbObject.put("third_msg", entry.getThirdMessage());
        dbObject.put("create_time", entry.getCreateTime().getTime());
        return dbObject;
    }

}
