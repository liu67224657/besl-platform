/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.shorturl;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class ShortUrlHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ShortUrlAccessor shortUrlAccessor;

    public ShortUrlHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();


        shortUrlAccessor = ShortUrlAccessorFactory.factoryShortUrlAccessor(dataBaseType);
    }

    public ShortUrl generateUrl(ShortUrl url) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.insert(url, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, ShortUrl> generateUrls(List<ShortUrl> urls) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.insert(urls, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ShortUrl getUrl(String key) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.getByKey(key, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, ShortUrl> queryUrls(Set<String> keys) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.queryByKeys(keys, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateShortUrl(String key, Map<ObjectField, Object> keyValues) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.update(key, keyValues, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    @Deprecated
    //todo 临时方法用完后删除
    public  List<ShortUrl> queryShortUrlByTableNo(int tableno)throws DbException{
          Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shortUrlAccessor.selectByTableNo(tableno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
