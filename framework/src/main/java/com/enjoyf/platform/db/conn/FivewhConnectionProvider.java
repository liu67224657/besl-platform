package com.enjoyf.platform.db.conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

public class FivewhConnectionProvider implements ConnectionProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(FivewhConnectionProvider.class);
    public static final String PREFIX_NAME = "DB_PREFIX";
    private String dsName;

    public void configure(Properties properties) throws HibernateException {
        FiveProps fiveProps = new FiveProps(properties);

        dsName = fiveProps.get(PREFIX_NAME, null);

        try {
            new DataSourceProps(fiveProps, dsName);
        } catch (DataSourceException e) {
            throw new HibernateException(e);
        } catch (DbTypeException e) {
            throw new HibernateException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;

        logger.debug("Get Connection for DataSource:" + dsName);

        try {
            DataSource ds = null;
            ds = DataSourceManager.get().getDataSource(dsName);

            conn = ds.getConnection();
        } catch (DataSourceException e) {
            GAlerter.lab("On getting Connection, DataSourceException occured: " + e);
            throw new SQLException("FivewhConnectionProvider get DataSource error.", e.getMessage());
        } catch (SQLException e) {
            GAlerter.lab("On getting Connection, SQLException occured: " + e);
            throw e;
        }

        logger.debug("Got the Connection successfully.");

        return conn;
    }

    public void closeConnection(Connection connection) throws SQLException {
        DataBaseUtil.closeConnection(connection);
    }

    public void close() throws HibernateException {
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }
}
