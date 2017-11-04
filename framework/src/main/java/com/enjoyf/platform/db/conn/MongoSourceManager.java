package com.enjoyf.platform.db.conn;

import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


/**
 * @author ericliu
 */
public class MongoSourceManager {


    private static final Logger logger = LoggerFactory.getLogger(MongoSourceManager.class);

    private static final long HEALTH_CHECKER_INTERVAL_MSECS = 1000 * 60;

    //
    private static Map<String, Mongo> dataSourceMap = new HashMap<String, Mongo>();
    private static Map<String, MongoDataSourceProps> dataSourcePropsMap = new HashMap<String, MongoDataSourceProps>();

    //
    private static final MongoSourceManager instance = new MongoSourceManager();

    private MongoSourceManager() {
    }

    public static MongoSourceManager get() {
        return instance;
    }

    /**
     * @param dsn
     * @param props
     * @throws DbTypeException
     * @throws DataSourceException
     */
    public synchronized void append(String dsn, FiveProps props)throws DbException {
        if (dataSourceMap.containsKey(dsn)) {
            return;
        }

        MongoDataSourceProps dsProps = null;
        try {
            dsProps = new MongoDataSourceProps(dsn,props);
            dataSourcePropsMap.put(dsn, dsProps);

            createDataSource(dsn,dsProps);
        } catch (DbException e) {
            GAlerter.lab("Append the datasource configure failed.", e);
            throw e;
        }
    }

    public Mongo createDataSource(String dsn, MongoDataSourceProps dsProps) throws DbTypeException {
        Mongo returnDs = new Mongo(dsProps.getMongoAddr(), dsProps.getOptions());

        dataSourceMap.put(dsn, returnDs);

        return returnDs;
    }

    public Mongo getMongo(String dsName) throws DataSourceException {
        Mongo ds = null;

        if (dataSourceMap.containsKey(dsName)) {
            ds = dataSourceMap.get(dsName.toLowerCase());
            if (ds == null) {
                logger.warn("Creating the datasource, " + dsName + ", failed.");
                throw new DataSourceException(DataSourceException.DS_CREATE_ERROR,
                        "The data source " + dsName + " does not exist.");
            }
        } else {
            logger.warn("The datasourceProps, " + dsName + ", is not exist.");

            throw new DataSourceException(DataSourceException.DS_NOTFOUND,
                    "The datasourceProps " + dsName + " does not exist.");
        }

        return ds;
    }


}
