package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class UserEventAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<UserEventEntry> {

    private String KEY_COLLECTION = "user_event_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";


    public UserEventEntry insert(UserEventEntry entry, DB db) throws DbException {

        DBCollection installLogCollection = db.getCollection(getCollection(entry.getEventDate()));

        installLogCollection.insert(entryToDBObject(entry));

        return entry;
    }

    @Override
    protected UserEventEntry dbObjToEntry(DBObject dbObject) {
        return null;
    }

    @Override
    protected BasicDBObject entryToDBObject(UserEventEntry entry) {
        BasicDBObject dbObject = new BasicDBObject();

        dbObject.put("src_uno", Strings.nullToEmpty(entry.getSrcUno()));
        dbObject.put("dest_uno", Strings.nullToEmpty(entry.getDestUno()));
        dbObject.put("event_type", entry.getEventType().getCode());
        dbObject.put("event_count", entry.getCount());
        dbObject.put("event_desc", entry.getDescription());
        dbObject.put("event_meta", entry.getMeta() != null ? entry.getMeta().deconstitute() : null);
        dbObject.put("event_date", new Timestamp(entry.getEventDate().getTime()));
        dbObject.put("event_ip", entry.getEventIp());

        return dbObject;
    }


    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

}
