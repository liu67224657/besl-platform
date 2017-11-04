package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.stats.StatUserView;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.Pagination;
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
public class AdvertiseUserViewAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<StatUserView> {

    private String KEY_COLLECTION = "advertise_uv_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private String SEQ_NAME = "SEQ_GAME_DB_ID";

    public int uvCount(MongoQueryExpress queryExpress, Date date, DB db) throws DbException {
        return super.count(getCollection(date), queryExpress, db);
    }

    public List<StatUserView> queryAdvertiseUVPage(MongoQueryExpress queryExpress, Pagination pagination, Date statDate, Date endDate, DB db) throws DbException {
        List<StatUserView> returnList = new ArrayList<StatUserView>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatUserView> list = super.query(queryExpress, pagination, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    public List<StatUserView> queryAdvertiseUV(MongoQueryExpress queryExpress, Date statDate, Date endDate, DB db) throws DbException {
        List<StatUserView> returnList = new ArrayList<StatUserView>();
        for (Date date : DateUtil.getMonthList(statDate, endDate)) {
            List<StatUserView> list = super.query(queryExpress, getCollection(date), db);
            returnList.addAll(list);
        }
        return returnList;
    }

    private String getCollection(Date date) {
        return KEY_COLLECTION + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }


    @Override
    public StatUserView insert(StatUserView advertiseUV, DB db) throws DbException {
        return null;
    }

    @Override
    protected StatUserView dbObjToEntry(DBObject dbObject) {
        StatUserView advertiseUV = new StatUserView();
        advertiseUV.setUrl((String) dbObject.get("url"));
        advertiseUV.setRefer((String) dbObject.get("refer"));
        advertiseUV.setPublishId((String) dbObject.get("advertise_id"));
        advertiseUV.setGlobalId((String) dbObject.get("globalid"));
        advertiseUV.setSessionId((String) dbObject.get("sessionid"));
        advertiseUV.setStatDate((String) dbObject.get("stat_date"));
        advertiseUV.setCreateIp((String) dbObject.get("ip"));
        return advertiseUV;
    }

    @Override
    protected BasicDBObject entryToDBObject(StatUserView entry) {
        return null;
    }

    public StatUserView getAdvertiseUV(MongoQueryExpress query, Date date2, DB db) throws DbException {
        StatUserView uv = null;
        List<StatUserView> list = super.query(query, getCollection(date2), db);
        if (list != null && !CollectionUtil.isEmpty(list)) {
            uv = list.get(0);
        }
        return uv;
    }

}
