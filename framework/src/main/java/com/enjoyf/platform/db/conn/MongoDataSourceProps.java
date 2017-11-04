package com.enjoyf.platform.db.conn;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;

/**
 * @author ericliu
 */
public class MongoDataSourceProps {

    private String dsName;
    private String host;
    private int port;
    private MongoOptions options;

    private ServerAddress mongoAddr;

    public MongoDataSourceProps(String dsName, FiveProps props) throws DbException {
        this.dsName = dsName;

        options = new MongoOptions();

        options.setThreadsAllowedToBlockForConnectionMultiplier(props.getInt(dsName + ".threads.allowed.toblockforconnection.multiplier", 5));
        options.setConnectionsPerHost(props.getInt(dsName + ".connection.per.host", 10));
        options.setMaxWaitTime(props.getInt(dsName + ".maxwaittime", 0));
        options.setConnectTimeout(props.getInt(dsName + ".connection.timeout", 0));

        host = props.get(dsName + ".host");
        port = props.getInt(dsName + ".port");
        try {
            mongoAddr = new ServerAddress(host, port);
        } catch (UnknownHostException e) {
            GAlerter.lab(this.getClass().getName() + " occured UnknownHostException", e);
            throw new DbException(DbException.BASE_DB_MONGODB_UNKONWHOST, "unknown host: " + host + ":" + port);
        }
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public MongoOptions getOptions() {
        return options;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ServerAddress getMongoAddr() {
        return mongoAddr;
    }

    public String getDsName() {
        return dsName;
    }
}
