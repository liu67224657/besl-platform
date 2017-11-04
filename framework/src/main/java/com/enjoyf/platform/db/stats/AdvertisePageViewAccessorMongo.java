package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.stats.StatPageView;
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
public class AdvertisePageViewAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<StatPageView> {

    private String KEY_COLLECTION = "advertise_pv_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

    public List<StatPageView> queryAdvertisePV(MongoQueryExpress queryExpress, Date statDate, Date endDate, DB db) throws DbException {
        List<StatPageView> returnList = new ArrayList<StatPageView>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatPageView> list = super.query(queryExpress, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    @Override
    public StatPageView insert(StatPageView advertisePV, DB db) throws DbException {
        return null;
    }

    @Override
    protected StatPageView dbObjToEntry(DBObject dbObject) {
        StatPageView advertisePV = new StatPageView();
        advertisePV.setUrl((String) dbObject.get("url"));
        advertisePV.setRefer((String) dbObject.get("refer"));
        advertisePV.setPublishId((String) dbObject.get("advertise_id"));
        advertisePV.setGlobalId((String) dbObject.get("globalid"));
        advertisePV.setSessionId((String) dbObject.get("sessionid"));
        advertisePV.setStatDate((String) dbObject.get("stat_date"));
        advertisePV.setCreateIp((String) dbObject.get("ip"));

        return advertisePV;
    }

    @Override
    protected BasicDBObject entryToDBObject(StatPageView entry) {
        return null;
    }
}
