package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlClickInfo;
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
 * Date: 14-7-31
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class AppUrlStatMongoAccessor extends AbstractSequenceBaseMongoDbAccessor<AdvertiseAppUrlClickInfo> {


    private static final String KEY_COLLECTION_APPURL_LOG = "advertise_appurl_log_";
    private static final String KEY_TABLE_SUFFIX_FMT = "yyyyMM";


    public AdvertiseAppUrlClickInfo insert(AdvertiseAppUrlClickInfo info, DB db) throws DbException {
        DBCollection pvLogCollection = db.getCollection(KEY_COLLECTION_APPURL_LOG + TableUtil.getTableDateSuffix(info.getReportDate(), KEY_TABLE_SUFFIX_FMT));
        pvLogCollection.insert(entryToDBObject(info));

        return info;
    }

    @Override
    protected AdvertiseAppUrlClickInfo dbObjToEntry(DBObject dbObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BasicDBObject entryToDBObject(AdvertiseAppUrlClickInfo entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("code", entry.getCode());
        dbObject.put("platform", entry.getPlatform().getCode());
        dbObject.put("redirect_url", entry.getRedirectUrl());
        dbObject.put("report_time", entry.getReportDate().getTime());
        dbObject.put("stat_date", DateUtil.formatDateToString(entry.getReportDate(),DateUtil.DATE_FORMAT));
        dbObject.put("sid", entry.getSid());

        return dbObject;
    }

}
