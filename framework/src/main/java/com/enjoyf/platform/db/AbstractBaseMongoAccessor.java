/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-30 下午12:04
 * Description:
 */
public abstract class AbstractBaseMongoAccessor<T> {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseMongoAccessor.class);

    //todo object
    public abstract T insert(T t, DB db) throws DbException;

    //fullfill the object use the result set.
    protected abstract T dbObjToEntry(DBObject dbObject);

    protected abstract BasicDBObject entryToDBObject(T entry);

}
