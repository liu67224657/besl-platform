/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.db.sequence.MongoSequenceFetcher;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoUtil;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-30 下午1:04
 * Description:
 */
public abstract class AbstractSequenceBaseMongoDbAccessor<T> extends AbstractBaseMongoAccessor<T> {
    //
    public long getSeqNo(String sequenceName, DB db) throws DbException {
        try {
            return MongoSequenceFetcher.get().generateOne(sequenceName, db);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + sequenceName);
        }
    }


    protected List<T> query(MongoQueryExpress queryExpress, String collectionName, DB db) throws DbException {
        List<T> returnList = new ArrayList<T>();

        DBObject object = MongoUtil.generatorQueryCondition(queryExpress);

        DBCursor dbCursor = null;
//        if (queryExpress.getMongoSort() == null) {
//            dbCursor = db.getCollection(collectionName).find(object);
//        } else {
//            dbCursor = db.getCollection(collectionName).find(object).sort(MongoUtil.generatorSort(queryExpress.getMongoSort()));
//        }

        if (queryExpress.getMongoSortArray() == null) {
            dbCursor = db.getCollection(collectionName).find(object);
        } else {
        	DBObject dbObject=MongoUtil.generatorSort(queryExpress.getMongoSortArray());
        	if (null == dbObject) {
        		dbCursor = db.getCollection(collectionName).find(object);
			}else {
				dbCursor = db.getCollection(collectionName).find(object).sort(MongoUtil.generatorSort(queryExpress.getMongoSortArray()));
			}
            
        }
        
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            returnList.add(dbObjToEntry(dbObject));
        }
        return returnList;

    }

    protected int count(String collectionName, MongoQueryExpress queryExpress, DB db) throws DbException {
        DBObject object = MongoUtil.generatorQueryCondition(queryExpress);

        return db.getCollection(collectionName).find(object).count();
    }

    protected int distinct(String collectionName, String key, MongoQueryExpress queryExpress, DB db) throws DbException {
        DBObject object = MongoUtil.generatorQueryCondition(queryExpress);

        return db.getCollection(collectionName).distinct(key, object).size();
    }

    protected List<T> query(MongoQueryExpress queryExpress, Pagination p, String collectionName, DB db) throws DbException {
        List<T> returnList = new ArrayList<T>();

        DBObject object = MongoUtil.generatorQueryCondition(queryExpress);

        p.setTotalRows(db.getCollection(collectionName).find(object).count());

        DBCursor dbCursor = null;
//        if (queryExpress.getMongoSort() == null) {
//            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
//        } else {
//            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize()).sort(MongoUtil.generatorSort(queryExpress.getMongoSortArray()));
//        }

        if (queryExpress.getMongoSortArray() == null) {
            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
        } else {
        	DBObject dbObject=MongoUtil.generatorSort(queryExpress.getMongoSortArray());
        	if (null == dbObject) {
        		dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
			}else {
				dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize()).sort(dbObject);
			}
        }
        
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            returnList.add(dbObjToEntry(dbObject));
        }

        return returnList;

    }

    public T get(MongoQueryExpress mongoQueryExpress, String collectionName, DB db) {
        DBObject object = MongoUtil.generatorQueryCondition(mongoQueryExpress);

        DBObject dbObject = null;
        dbObject = db.getCollection(collectionName).findOne(object);
        if (dbObject != null) {
            return dbObjToEntry(dbObject);
        } else {
            return null;
        }
    }




    public boolean update(BasicDBObject query, BasicDBObject update, String collectionName, DB db) {
        boolean isExists = db.getCollection(collectionName).findOne(query) != null;

        if (isExists) {
            db.getCollection(collectionName).update(query, update, true, false);
        }
        return true;
    }

    @Deprecated
    protected List<T> aggregate(MongoQueryExpress queryExpress, Pagination p, String collectionName, DB db) throws DbException {
        List<T> returnList = new ArrayList<T>();


//        p.setTotalRows(db.getCollection(collectionName).find(object).count());

//        DBCursor dbCursor = null;
//        if (queryExpress.getMongoSort() == null) {
//            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
//        } else {
//            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize()).sort(MongoUtil.generatorSort(queryExpress.getMongoSortArray()));
//        }

        List<DBObject> pipeline = new ArrayList<DBObject>();
        DBObject object = MongoUtil.generatorQueryCondition(queryExpress);
        BasicDBObject matchObject = new BasicDBObject();
        matchObject.put("$match", object);

        BasicDBObject limitObject = new BasicDBObject();
        limitObject.put("$limit", p.getPageSize());

        BasicDBObject skipObject = new BasicDBObject();
        skipObject.put("$skip", p.getStartRowIdx());

        DBObject sortExpObject = MongoUtil.generatorSort(queryExpress.getMongoSortArray());
        BasicDBObject sortObject = new BasicDBObject();
        sortObject.put("$sort", sortExpObject);

        pipeline.add(matchObject);
        pipeline.add(limitObject);
        pipeline.add(skipObject);
        pipeline.add(sortObject);

//        BasicDBObject opObject=new BasicDBObject("allowDiskUse", true);

//        AggregationOutput output=db.getCollection(collectionName).aggregate(limitObject, skipObject, sortObject, opObject);

        AggregationOptions.Builder builder = AggregationOptions.builder();
        builder.allowDiskUse(true);
        AggregationOptions aggregationOptions = builder.build();

        Cursor dbCursor = db.getCollection(collectionName).aggregate(pipeline, aggregationOptions);


        while (dbCursor.hasNext()) {
            DBObject resultObject = dbCursor.next();
            returnList.add(dbObjToEntry(resultObject));
        }

//
//        System.out.println(output.getCommand());
//        Iterable<com.mongodb.DBObject> iterable=output.results();
//        while (iterable.iterator().hasNext()){
//            returnList.add(dbObjToEntry(iterable.iterator().next()));
//        }


//        if (queryExpress.getMongoSortArray() == null) {
//            dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
//        } else {
//        	DBObject dbObject=MongoUtil.generatorSort(queryExpress.getMongoSortArray());
//        	if (null == dbObject) {
//        		dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize());
//			}else {
//				dbCursor = db.getCollection(collectionName).find(object).skip(p.getStartRowIdx()).limit(p.getPageSize()).sort(dbObject);
//			}
//        }

//        while (dbCursor.hasNext()) {
//            DBObject dbObject = dbCursor.next();
//            returnList.add(dbObjToEntry(dbObject));
//        }

        return returnList;

    }

}
