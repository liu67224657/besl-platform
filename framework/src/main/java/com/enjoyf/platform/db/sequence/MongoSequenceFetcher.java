/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.sequence;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.props.EnvConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */

/**
 * this class is used by jdbc and hibernate. used to get a new sequence id.
 */
public class MongoSequenceFetcher {
    private static Map<String, SequenceValue> sequenceMap = new HashMap<String, SequenceValue>();

    // the table name which store all the sequence of the database.
    private static final String SEQUENCE_TABLE_NAME = "SEQUENCES_TABLE";

    private static MongoSequenceFetcher instance = new MongoSequenceFetcher();

    private MongoSequenceFetcher() {
    }

    public static synchronized MongoSequenceFetcher get() {
        return instance;
    }

    public long generateOne(String sequeceName, DB db) throws TableSequenceException {
        synchronized (sequeceName) {
            try {
                return fetchNextValue(sequeceName, 1, db) + 1;
            } catch (SQLException e) {
                throw new TableSequenceException("GenerateOne sequence value failed, the sequence name is " + sequeceName + " " + e);
            }
        }
    }

    public long generate(String sequenceName, DB db) throws TableSequenceException {
        SequenceValue sequenceValue = null;
        long returnValue = 0;

        // get the sequence from map or initialize it.
        synchronized (sequenceMap) {
            sequenceValue = sequenceMap.get(sequenceName);

            if (sequenceValue == null) {
                sequenceValue = new SequenceValue(sequenceName);

                sequenceMap.put(sequenceValue.getSequenceName(), sequenceValue);
            }
        }

        // get the next value from the cache.
        synchronized (sequenceValue) {
            // if the cache has next value, just get one from the cache.
            if (sequenceValue.hasNext()) {
                returnValue = sequenceValue.getNextValue();
            } else {
                // if the cache hasn't next value, get more from db.
                long dbValue = 0;
                try {
                    dbValue = fetchNextValue(sequenceName, EnvConfig.get().getSequenceFetchStep(), db);
                } catch (SQLException e) {
                    throw new TableSequenceException("Generate sequence value failed, the sequence name is " + sequenceName + " " + e);
                }

                // set the db value to cache.
                sequenceValue.setCurValue(dbValue);
                sequenceValue.setMaxValue(dbValue + EnvConfig.get().getSequenceFetchStep() - 1);

                returnValue = sequenceValue.getNextValue();
            }
        }

        return returnValue;
    }

    private long fetchNextValue(String sequenceName, int step, DB db) throws SQLException {
        long curValue = -1;

        boolean success = false;
        int tryTimes = 0;

        DBCollection collection = db.getCollection(SEQUENCE_TABLE_NAME);

        while (!success && (tryTimes < EnvConfig.get().getSequenceFetchTryTimes())) {

            DBObject queryCurValue = new BasicDBObject().append("SEQUENCENAME", sequenceName);
            DBObject tempDBObject = collection.findOne(queryCurValue);

            if (tempDBObject == null) {
                BasicDBObject dbObject = new BasicDBObject();
                dbObject.put("SEQUENCENAME", sequenceName);
                dbObject.put("CURVALUE", 1000);
                collection.save(dbObject);
                return 1;
            }
            Object tempObject = tempDBObject.get("CURVALUE");
            curValue = Double.valueOf(tempObject.toString()).longValue();

            DBObject query = new BasicDBObject();
            query.put("SEQUENCENAME", sequenceName);
            query.put("CURVALUE", curValue);
            DBObject newDocument = new BasicDBObject().append("$inc", new BasicDBObject().append("CURVALUE", step));
            DBObject ret = collection.findAndModify(query, newDocument);

            if (ret != null) {
                success = true;
            } else {
                tryTimes++;
            }
        }

        if (curValue < 0) {
            throw new SQLException("Fetch sequence value from table failed, curValue < 0,the sequence name is " + sequenceName);
        }

        return curValue;
    }
}
