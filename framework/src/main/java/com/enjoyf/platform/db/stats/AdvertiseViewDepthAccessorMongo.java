package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.stats.StatViewDepth;
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
public class AdvertiseViewDepthAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<StatViewDepth> {

    private String KEY_COLLECTION = "advertise_pvcount_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    public int uvCount(MongoQueryExpress queryExpress, Date date, DB db) throws DbException {
        return super.count(getCollection(date), queryExpress, db);
    }

    public List<StatViewDepth> queryAdvertiseCrawDepth(MongoQueryExpress queryExpress, Date statDate, Date endDate, DB db) throws DbException {
        List<StatViewDepth> returnList = new ArrayList<StatViewDepth>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatViewDepth> list = super.query(queryExpress, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }


    @Override
    public StatViewDepth insert(StatViewDepth advrtiseCrawDepth, DB db) throws DbException {
        return null;
    }

    @Override
    protected StatViewDepth dbObjToEntry(DBObject dbObject) {
        StatViewDepth advertiseCrawDepth = new StatViewDepth();
        advertiseCrawDepth.setCount((Integer) dbObject.get("count"));
        advertiseCrawDepth.setPublishId((String) dbObject.get("advertise_id"));
        advertiseCrawDepth.setSessionId((String) dbObject.get("sessionid"));
        advertiseCrawDepth.setStatDate((String) dbObject.get("stat_date"));
        advertiseCrawDepth.setCreateIp((String) dbObject.get("ip"));
        return advertiseCrawDepth;
    }

    @Override
    protected BasicDBObject entryToDBObject(StatViewDepth entry) {
        return null;
    }
}
