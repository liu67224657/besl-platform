package com.enjoyf.platform.db.conn;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Garrison
 */
public class DbConnFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(DbConnFactory.class);
	
    public static Connection factory(String dsName) throws DbException {
        Connection conn = null;
        DataSource ds = null;

        if (logger.isDebugEnabled()) {
        	logger.debug("Get Connection from DataSource:" + dsName);
        }
        try {
            ds = DataSourceManager.get().getDataSource(dsName);

            conn = ds.getConnection();
        } catch (DataSourceException e) {
            GAlerter.lab("On getting Connection, DataSourceException occured: ", e);
            throw e;
        } catch (SQLException e) {
        	GAlerter.lab("On getting Connection, SQLException occured: ", e);
            throw new DbException(e);
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Got the Connection successfully.");
        }
        return conn;
    }

    public static DB mongoDbFactory(String dsName,String dbName) throws DbException {
        DB db = null;
        Mongo mongo = null;


        if (logger.isDebugEnabled()) {
            logger.debug("Get Connection from DataSource:" + dsName);
        }
        try {
            mongo = MongoSourceManager.get().getMongo(dsName);

            db = mongo.getDB(dbName);
        } catch (DataSourceException e) {
            GAlerter.lab("On getting Connection, DataSourceException occured: ", e);
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Got the Connection successfully.");
        }
        return db;
    }

}
