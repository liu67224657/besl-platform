package com.enjoyf.platform.db.conn;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.util.FiveProps;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataSourceProps {
    private static Map<String, DataSourceProps> dataSourceMap = new HashMap<String, DataSourceProps>();

    //the keys of the configure attributes
    public static String SUB_KEY_DATABASE_TYPE = ".databaseType";
    public static String SUB_KEY_CONN_POOL_TYPE = ".connPoolType";

    public static String SUB_KEY_USER_NAME = ".userName";
    public static String SUB_KEY_PASSWORD = ".password";
    public static String SUB_KEY_PASSWORD_DESKEY = ".password.deskey";

    public static String SUB_KEY_DRIVER_CLASS_NAME = ".driverClassName";
    public static String SUB_KEY_URL = ".url";
    public static String SUB_KEY_VALIDATE_SQL = ".validatesql";

    public static String SUB_KEY_IDLE_CONNECTION_TEST_PERIOD = ".idleConnectionTestPeriod";

    public static String SUB_KEY_MAX_ACTIVE = ".maxActive";
    public static String SUB_KEY_MAX_IDLE = ".maxIdle";
    public static String SUB_KEY_MAX_IDLE_MILLISECONDS = ".maxIdleMilliseconds";
    public static String SUB_KEY_MAX_INITIAL_SIZE = ".initialSize";
    public static String SUB_KEY_MAX_WAIT = ".maxWait";
    public static String SUB_KEY_MAX_STATEMENTS = ".maxStatements";
    public static String SUB_KEY_POOL_PARTITION = ".poolPartitions";

    //thie proxool props
    public static String SUB_KEY_MAX_CONNECTION_COUNT = ".max.contentcion.count";
    public static String SUB_KEY_MAX_ACTIVE_TIME = ".max.active.time";
    public static String SUB_KEY_MAX_CONNECTION_LIFETIME = ".max.connection.lifetime";
    public static String SUB_KEY_HOUSE_KEEPING_SLEEPTIME = ".house.keeping.sleeptime";
    public static String SUB_KEY_PROTOTYPE_COUNT = ".prototype.count";
    public static String SUB_KEY_SIMULTANEOUS_BUILD_THROTTLE = ".simultaneous.build.throttle";
    public static String SUB_KEY_STATISTICS = ".statistics";


    //the datasource props members
    private String dataSourceName;
    private DataBaseType dataBaseType;
    private ConnPoolType connPoolType;
    private String dataSourceURL;

    //The fully qualified Java class name of the JDBC driver to be used.
    private String dataSourceDriverClassName;
    private String dataSourceUserName;
    private String dataSourcePassword;
    private String dataSourcePasswordDesKey;

    //
    private String dataSourceValidateSQL;

    private int idleConnectionTestPeriodSecs = 120;

    //The initial number of connections that are created when the pool is started.
    private int dataSourceInitialSize = 3;
    //The maximum number of active connections that
    //can be allocated from this pool at the same time, or non-positive for no limit.
    private int dataSourceMaxActive = 15;
    //The maximum number of milliseconds that the pool will wait (when there are no available connections)
    //for a connection to be returned before throwing an exception, or -1 to wait indefinitely.
    private long dataSourceMaxWait = 10000;     //checkoutTimeout
    //The maximum number of connections that can remain idle in the pool,
    //without extra ones being released, or negative for no limit.
    private int dataSourceMaxIdle = 5;
    //The maximum number of open statements that can be allocated from the statement pool
    //at the same time, or non-positive for no limit.
    private int dataSourceMaxStatements = 0;
    // For partitioned data sources like BoneCP
    private int dataSourcePoolPartitions = 1;
    // over 5 minutes, the idle connections will be returned to shrink the pool
    private int dataSourceIdleMaxMilliseconds = 1000 * 60 * 5;


    //proxxool
    private int maxConnectionCount = 20;
    private int simultaneousBuildThrottle = 80;
    private int maximumActiveTime = 180000;
    private int maximumConnectionLifetime = 180000;
    private int houseKeepingSleepTime = 180000;
    private int prototypeCount = 20;
    private String statistics = "10s,1m,1d";

    /**
     * constructor by the props and name
     *
     * @param extPros
     * @param dsName
     * @throws DataSourceException
     */
    public DataSourceProps(FiveProps extPros, String dsName) throws DataSourceException, DbTypeException {
        dataSourceName = dsName;

        dataSourceURL = extPros.get(dataSourceName + SUB_KEY_URL);
        dataSourceDriverClassName = extPros.get(dataSourceName + SUB_KEY_DRIVER_CLASS_NAME);
        dataSourceUserName = extPros.get(dataSourceName + SUB_KEY_USER_NAME);
        dataSourcePassword = extPros.get(dataSourceName + SUB_KEY_PASSWORD);
        dataSourcePasswordDesKey = extPros.get(dataSourceName + SUB_KEY_PASSWORD_DESKEY);
        dataSourceValidateSQL = extPros.get(dataSourceName + SUB_KEY_VALIDATE_SQL);

        idleConnectionTestPeriodSecs = extPros.getInt(dataSourceName + SUB_KEY_IDLE_CONNECTION_TEST_PERIOD, idleConnectionTestPeriodSecs);

        dataBaseType = DataBaseType.getDbType(extPros.get(dataSourceName + SUB_KEY_DATABASE_TYPE));
        connPoolType = ConnPoolType.getConnPoolType(extPros.get(dataSourceName + SUB_KEY_CONN_POOL_TYPE));

        //the pool parameter
        dataSourceInitialSize = extPros.getInt(dataSourceName + SUB_KEY_MAX_INITIAL_SIZE, dataSourceInitialSize);
        dataSourceMaxActive = extPros.getInt(dataSourceName + SUB_KEY_MAX_ACTIVE, dataSourceMaxActive);
        dataSourceMaxWait = extPros.getLong(dataSourceName + SUB_KEY_MAX_WAIT, dataSourceMaxWait);
        dataSourceMaxIdle = extPros.getInt(dataSourceName + SUB_KEY_MAX_IDLE, dataSourceMaxIdle);
        dataSourceMaxStatements = extPros.getInt(dataSourceName + SUB_KEY_MAX_STATEMENTS, dataSourceMaxStatements);
        dataSourcePoolPartitions = extPros.getInt(dataSourceName + SUB_KEY_POOL_PARTITION, dataSourcePoolPartitions);
        dataSourceIdleMaxMilliseconds = extPros.getInt(dataSourceName + SUB_KEY_MAX_IDLE_MILLISECONDS, dataSourceIdleMaxMilliseconds);

        //the proxool parametor
        maxConnectionCount = extPros.getInt(dataSourceName + SUB_KEY_MAX_CONNECTION_COUNT, maxConnectionCount);
        simultaneousBuildThrottle = extPros.getInt(dataSourceName + SUB_KEY_SIMULTANEOUS_BUILD_THROTTLE, simultaneousBuildThrottle);
        maximumActiveTime = extPros.getInt(dataSourceName + SUB_KEY_MAX_ACTIVE_TIME, maximumActiveTime);
        maximumConnectionLifetime = extPros.getInt(dataSourceName + SUB_KEY_MAX_CONNECTION_LIFETIME, maximumConnectionLifetime);
        houseKeepingSleepTime = extPros.getInt(dataSourceName + SUB_KEY_HOUSE_KEEPING_SLEEPTIME, houseKeepingSleepTime);
        prototypeCount = extPros.getInt(dataSourceName + SUB_KEY_PROTOTYPE_COUNT, prototypeCount);
        statistics = extPros.get(dataSourceName + SUB_KEY_STATISTICS, statistics);



        if (Strings.isNullOrEmpty(dataSourceURL) ||
                Strings.isNullOrEmpty(dataSourceDriverClassName) ||
                Strings.isNullOrEmpty(dataSourceUserName) ||
                Strings.isNullOrEmpty(dataSourcePassword)) {
            throw new DataSourceException(DataSourceException.DS_MISS_CONFIGURE, "The configure of datasource missed some values.");
        }

        dataSourceMap.put(dataSourceName, this);
    }


    public String getName() {
        return dataSourceName;
    }

    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    public ConnPoolType getConnPoolType() {
        return connPoolType;
    }

    public String getUrl() {
        return dataSourceURL;
    }

    public String getDriverClassName() {
        return dataSourceDriverClassName;
    }

    public String getUserName() {
        return dataSourceUserName;
    }

    public String getPassword() {
        return dataSourcePassword;
    }

    public String getPasswordDesKey() {
        return dataSourcePasswordDesKey;
    }

    public String getValidateSQL() {
        return dataSourceValidateSQL;
    }

    public int getIdleConnectionTestPeriodSecs() {
        return idleConnectionTestPeriodSecs;
    }

    public int getMaxActive() {
        return dataSourceMaxActive;
    }

    public int getInitialSize() {
        return dataSourceInitialSize;
    }

    public long getMaxWait() {
        return dataSourceMaxWait;
    }

    public int getMaxIdle() {
        return dataSourceMaxIdle;
    }

    public int getMaxStatements() {
        return dataSourceMaxStatements;
    }

    public int getPoolPartitions() {
        return dataSourcePoolPartitions;
    }

    public int getIdleMaxMilliseconds() {
        return dataSourceIdleMaxMilliseconds;
    }

    public String toString() {
        return "DataSourceProps {" + "Name:" + dataSourceName +
                "; Conn poll type:" + connPoolType +
                "; DriverClassName: " + dataSourceDriverClassName +
                "; URL:" + dataSourceURL +
                "; UserName:" + dataSourceUserName +
                "; Password:" + dataSourcePassword +
                "; InitialSize:" + dataSourceInitialSize +
                "; MaxActive:" + dataSourceMaxActive +
                "; MaxIdle:" + dataSourceMaxIdle +
                "; MaxWait:" + dataSourceMaxWait +
                "; MaxStatements:" + dataSourceMaxStatements +
                "}";
    }

    public static Iterator<DataSourceProps> getDataSourcePropsItr() {
        return dataSourceMap.values().iterator();
    }

    public static DataSourceProps getDataSourceProps(String dsName) {
        return dataSourceMap.get(dsName.toLowerCase());
    }

    public static boolean hasDataSourceProps(String name) {
        return dataSourceMap.containsKey(name.toLowerCase());
    }

    public int getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public int getSimultaneousBuildThrottle() {
        return simultaneousBuildThrottle;
    }

    public int getMaximumActiveTime() {
        return maximumActiveTime;
    }

    public int getMaximumConnectionLifetime() {
        return maximumConnectionLifetime;
    }

    public int getHouseKeepingSleepTime() {
        return houseKeepingSleepTime;
    }

    public int getPrototypeCount() {
        return prototypeCount;
    }

    public String getStatistics() {
        return statistics;
    }
}
