package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.stats.StatViewTime;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-21
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */
public class AdvertiseViewTimeAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<StatViewTime> {

    private String KEY_COLLECTION = "advertise_intime_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    public List<StatViewTime> queryAdvertiseViewTime(MongoQueryExpress queryExpress, Date statDate, Date endDate, DB db) throws DbException {
        List<StatViewTime> returnList = new ArrayList<StatViewTime>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatViewTime> list = super.query(queryExpress, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }


    @Override
    public StatViewTime insert(StatViewTime advertiseReferReadTime, DB db) throws DbException {
        return null;
    }

    @Override
    protected StatViewTime dbObjToEntry(DBObject dbObject) {
        StatViewTime advertiseReferReadTime = new StatViewTime();
        advertiseReferReadTime.setReadTimes((Long) dbObject.get("read_times"));
        advertiseReferReadTime.setUrl((String) dbObject.get("url"));
        advertiseReferReadTime.setPublishId((String) dbObject.get("advertise_id"));
        advertiseReferReadTime.setGlobalId((String) dbObject.get("globalid"));
        advertiseReferReadTime.setSessionId((String) dbObject.get("sessionid"));
        advertiseReferReadTime.setStatDate((String) dbObject.get("stat_date"));
        advertiseReferReadTime.setCreateIp((String) dbObject.get("ip"));
        return advertiseReferReadTime;
    }

    @Override
    protected BasicDBObject entryToDBObject(StatViewTime entry) {
        return null;
    }

}
