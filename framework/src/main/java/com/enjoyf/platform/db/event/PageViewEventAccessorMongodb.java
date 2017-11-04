package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.mongodb.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class PageViewEventAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<PageViewEvent> {

    private String KEY_COLLECTION_ERROR_LOG = "pv_event_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";


    public PageViewEvent insert(PageViewEvent pageViewEvent, DB db) throws DbException {

        DBCollection installLogCollection = db.getCollection(getCollection(pageViewEvent.getEventDate()));

        installLogCollection.insert(entryToDBObject(pageViewEvent));

        return pageViewEvent;
    }

    @Override
    protected PageViewEvent dbObjToEntry(DBObject dbObject) {
        return null;
    }

    @Override
    protected BasicDBObject entryToDBObject(PageViewEvent entry) {
        BasicDBObject dbObject = new BasicDBObject();

        dbObject.put("location_url", entry.getLocationUrl());
        dbObject.put("location_id", entry.getLocationId());
        dbObject.put("session_id", entry.getSessionId());
        dbObject.put("global_id", entry.getGlobalId());
        dbObject.put("uno", entry.getUno());
        dbObject.put("refer", entry.getRefer());
        dbObject.put("refer_id", entry.getReferId());
        dbObject.put("os", entry.getOs());
        dbObject.put("screen", entry.getScreen());

        dbObject.put("explorer_type", entry.getExplorerType());
        dbObject.put("explorer_version", entry.getExplorerVersion());
        dbObject.put("deconstitute", entry.getMeta().deconstitute());

        dbObject.put("deconstitute", entry.getMeta().deconstitute());
        dbObject.put("event_ip", entry.getEventIp());
        dbObject.put("event_date", entry.getEventDate().getTime());
        dbObject.put("stat_date", DateUtil.formatDateToString(entry.getEventDate(), DateUtil.PATTERN_DATE_DAY));

        return dbObject;
    }


    private String getCollection(Date date) {
        return KEY_COLLECTION_ERROR_LOG + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

}
