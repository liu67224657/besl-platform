package com.enjoyf.platform.db.conn;

import com.enjoyf.platform.crypto.PasswdUtil;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import com.jolbox.bonecp.BoneCPDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.connection.ProxoolConnectionProvider;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Garrison
 *         NOTE: this class is NOT thread-safe, you must make sure append(..) is called in an initializer
 *         before any call to getDataSource(..)
 */
public class DataSourceManager {
    //
    private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    private static final long HEALTH_CHECKER_INTERVAL_MSECS = 1000 * 60;

    //
    private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
    private static Map<String, DataSourceProps> dataSourcePropsMap = new HashMap<String, DataSourceProps>();

    //
    private static final DataSourceManager instance = new DataSourceManager();

    private DataSourceManager() {
    }

    public static DataSourceManager get() {
        return instance;
    }

    /**
     * @param dsn
     * @param props
     * @throws DbTypeException
     * @throws DataSourceException
     */
    public synchronized void append(String dsn, FiveProps props) throws DbTypeException, DataSourceException {
        if (dataSourceMap.containsKey(dsn)) {
            return;
        }

        DataSourceProps dsProps = null;
        String dataSourceName = null;

        dataSourceName = dsn.toLowerCase();
        try {
            dsProps = new DataSourceProps(props, dataSourceName);
            dataSourcePropsMap.put(dsn, dsProps);

            createDataSource(dsProps);
        } catch (DataSourceException e) {
            GAlerter.lab("Append the datasource configure failed.", e);
            throw e;
        } catch (DbTypeException e) {
            GAlerter.lab("Append the datasource configure failed.", e);
            throw e;
        }

        //start the timer to validate the sql.
        Timer dataSourceHealthCheckTimer = new Timer();
        dataSourceHealthCheckTimer.scheduleAtFixedRate(new DataSourceHealthChecker(), HEALTH_CHECKER_INTERVAL_MSECS, HEALTH_CHECKER_INTERVAL_MSECS);
    }

    public DataSource createDataSource(DataSourceProps dsProps) throws DbTypeException {
        DataSource returnDs = null;
        if (dsProps.getConnPoolType().equals(ConnPoolType.CONN_POOL_TYPE_BONECP)) {
            returnDs = createDataSourceBoneCP(dsProps);
        } else if (dsProps.getConnPoolType().equals(ConnPoolType.CONN_POOL_TYPE_C3P0)) {
            returnDs = createDataSourceC3P0(dsProps);
        } else if (dsProps.getConnPoolType().equals(ConnPoolType.CONN_POOL_TYPE_PROXOOL)) {
            returnDs = createDataSourcePROXOOL(dsProps);
        } else {
            throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT, "Not supported:" + dsProps.getConnPoolType());
        }

        dataSourceMap.put(dsProps.getName(), returnDs);

        return returnDs;
    }

    private DataSource createDataSourcePROXOOL(DataSourceProps dsProps) {
        ProxoolDataSource proxoolDataSource = new ProxoolDataSource();

        proxoolDataSource.setAlias(dsProps.getName());
        proxoolDataSource.setUser(dsProps.getUserName());
         if (!Strings.isNullOrEmpty(dsProps.getPasswordDesKey())) {
            proxoolDataSource.setPassword(PasswdUtil.decrypt(dsProps.getPassword(), dsProps.getPasswordDesKey()));
        } else {
            proxoolDataSource.setPassword(dsProps.getPassword());
        }
        proxoolDataSource.setDriverUrl(dsProps.getUrl());
        proxoolDataSource.setDriver(dsProps.getDriverClassName());

        proxoolDataSource.setMaximumConnectionCount(dsProps.getMaxConnectionCount());
        proxoolDataSource.setMaximumActiveTime(dsProps.getMaximumActiveTime());
        proxoolDataSource.setMaximumConnectionLifetime(dsProps.getMaximumConnectionLifetime());
        proxoolDataSource.setHouseKeepingSleepTime(dsProps.getHouseKeepingSleepTime());
        proxoolDataSource.setPrototypeCount(dsProps.getPrototypeCount());
        proxoolDataSource.setSimultaneousBuildThrottle(dsProps.getSimultaneousBuildThrottle());
        proxoolDataSource.setStatistics(dsProps.getStatistics());

        if (!Strings.isNullOrEmpty(dsProps.getValidateSQL())) {
            proxoolDataSource.setHouseKeepingTestSql(dsProps.getValidateSQL());
        }


        return proxoolDataSource;
    }

    private DataSource createDataSourceBoneCP(DataSourceProps dsProps) {
        BoneCPDataSource boneCPDs = new BoneCPDataSource();

        boneCPDs.setPoolName(dsProps.getName());
        boneCPDs.setDriverClass(dsProps.getDriverClassName());
        boneCPDs.setJdbcUrl(dsProps.getUrl());

        //set the password and username of the db account
        boneCPDs.setUsername(dsProps.getUserName());
        if (!Strings.isNullOrEmpty(dsProps.getValidateSQL())) {
            boneCPDs.setInitSQL(dsProps.getValidateSQL());
        }

        if (!Strings.isNullOrEmpty(dsProps.getPasswordDesKey())) {
            boneCPDs.setPassword(PasswdUtil.decrypt(dsProps.getPassword(), dsProps.getPasswordDesKey()));
        } else {
            boneCPDs.setPassword(dsProps.getPassword());
        }

        boneCPDs.setMaxConnectionsPerPartition(dsProps.getMaxActive());
        boneCPDs.setMinConnectionsPerPartition(dsProps.getInitialSize());
        boneCPDs.setPartitionCount(dsProps.getPoolPartitions());
        boneCPDs.setIdleMaxAge(dsProps.getIdleMaxMilliseconds(), TimeUnit.MILLISECONDS);
        boneCPDs.setConnectionTimeout(dsProps.getMaxWait(), TimeUnit.MILLISECONDS);

        boneCPDs.setIdleConnectionTestPeriod(dsProps.getIdleConnectionTestPeriodSecs());

        return boneCPDs;
    }

    private DataSource createDataSourceC3P0(DataSourceProps dsProps) {
        ComboPooledDataSource c3p0Ds = new ComboPooledDataSource();

        c3p0Ds.setDataSourceName(dsProps.getName());
        try {
            c3p0Ds.setDriverClass(dsProps.getDriverClassName());
        } catch (PropertyVetoException e) {
            GAlerter.lab("c3p0 load driver class error.", e);
        }

        c3p0Ds.setJdbcUrl(dsProps.getUrl());

        //set the password and username of the db account
        c3p0Ds.setUser(dsProps.getUserName());
        if (!Strings.isNullOrEmpty(dsProps.getPasswordDesKey())) {
            c3p0Ds.setPassword(PasswdUtil.decrypt(dsProps.getPassword(), dsProps.getPasswordDesKey()));
        } else {
            c3p0Ds.setPassword(dsProps.getPassword());
        }

        if (!Strings.isNullOrEmpty(dsProps.getValidateSQL())) {
            c3p0Ds.setPreferredTestQuery(dsProps.getValidateSQL());
        }

        c3p0Ds.setMinPoolSize(dsProps.getInitialSize());
        c3p0Ds.setMaxPoolSize(dsProps.getMaxActive());
        c3p0Ds.setCheckoutTimeout((int) (dsProps.getMaxWait()));
        c3p0Ds.setMaxStatementsPerConnection(dsProps.getMaxStatements());

        c3p0Ds.setIdleConnectionTestPeriod(dsProps.getIdleConnectionTestPeriodSecs());

        return c3p0Ds;
    }


    public DataSource getDataSource(String dsName) throws DataSourceException {
        DataSource ds = null;

        if (DataSourceProps.hasDataSourceProps(dsName)) {
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

    class DataSourceHealthChecker extends TimerTask {

        public void run() {
            logger.debug("Start Datasource Health Checking.");

            for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
                Connection conn = null;

                try {
                    //
                    conn = entry.getValue().getConnection();
                    DataSourceProps dsProps = dataSourcePropsMap.get(entry.getKey());

                    //
                    if (conn != null && dsProps != null) {
                        //
                        if (!Strings.isNullOrEmpty(dsProps.getValidateSQL())) {
                            logger.info("The datasource " + entry.getKey() + "'s current time is " + processValidateSql(conn, dsProps.getValidateSQL()));
                        } else {
                            logger.info("DataSourceHealthChecker run: the datasource " + entry.getKey() + " hasn't validate sql.");
                        }
                    } else {
                        logger.info("DataSourceHealthChecker run: the datasource " + entry.getKey() + "'s connection is null.");
                    }
                } catch (Exception e) {
                    logger.error("Some errors occured when checking the datasource " + entry.getKey() + "'s Health.", e);
                } finally {
                    DataBaseUtil.closeConnection(conn);
                }
            }

            logger.debug("Datasource Health Checking is Completed.");
        }

        private Timestamp processValidateSql(Connection conn, String sql) throws SQLException {
            Timestamp returnValue = null;

            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                pstmt = conn.prepareStatement(sql);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    returnValue = rs.getTimestamp(1);
                }
            } finally {
                DataBaseUtil.closeResultSet(rs);
                DataBaseUtil.closeStatment(pstmt);
            }

            return returnValue;
        }
    }
}
