package com.enjoyf.platform.db.conn;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author Daniel
 */
public class CassandraDataSourceProps {

    private String hosts;
    private int maxActiveConns;
    private int maxIdleConns;
    private int thriftSocketTimeout;
    private String cluster;
    private String keyspace;

    public CassandraDataSourceProps(FiveProps props) {
        String dsName = props.get("datasource.name");
        this.hosts = props.get("cassandra.hosts");
        this.maxActiveConns = props.getInt("cassandra.conn.max");
        this.maxIdleConns = props.getInt("cassandra.conn.max.idle");
        this.thriftSocketTimeout = props.getInt("cassandra.conn.socket.timeout");
        this.cluster = props.get("cassandra.cluster");
        this.keyspace = props.get("cassandra.keyspace." + dsName + ".property");
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public int getMaxActiveConns() {
        return maxActiveConns;
    }

    public void setMaxActiveConns(int maxActiveConns) {
        this.maxActiveConns = maxActiveConns;
    }

    public int getMaxIdleConns() {
        return maxIdleConns;
    }

    public void setMaxIdleConns(int maxIdleConns) {
        this.maxIdleConns = maxIdleConns;
    }

    public int getThriftSocketTimeout() {
        return thriftSocketTimeout;
    }

    public void setThriftSocketTimeout(int thriftSocketTimeout) {
        this.thriftSocketTimeout = thriftSocketTimeout;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }
}
