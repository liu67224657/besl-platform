package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.stats.StatViewBounce;
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
 * Date: 14-3-24
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class AdvertiseBounceRateAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<StatViewBounce> {

    private String KEY_COLLECTION = "advertise_bouncerate_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

    public List<StatViewBounce> queryBounceRate(MongoQueryExpress queryExpress, Date statDate, Date endDate, DB db) throws DbException {
        List<StatViewBounce> returnList = new ArrayList<StatViewBounce>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatViewBounce> list = super.query(queryExpress, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    @Override
    public StatViewBounce insert(StatViewBounce advertiseBounceRate, DB db) throws DbException {
        return null;
    }

    @Override
    protected StatViewBounce dbObjToEntry(DBObject dbObject) {
        StatViewBounce advertiseBounceRate = new StatViewBounce();
        advertiseBounceRate.setUrl((String) dbObject.get("url"));
        advertiseBounceRate.setPublishId((String) dbObject.get("advertise_id"));
        advertiseBounceRate.setGlobalId((String) dbObject.get("globalid"));
        advertiseBounceRate.setSessionId((String) dbObject.get("sessionid"));
        advertiseBounceRate.setStatDate((String) dbObject.get("stat_date"));
        advertiseBounceRate.setCreateIp((String) dbObject.get("ip"));
        return advertiseBounceRate;
    }

    @Override
    protected BasicDBObject entryToDBObject(StatViewBounce entry) {
        return null;
    }
}
