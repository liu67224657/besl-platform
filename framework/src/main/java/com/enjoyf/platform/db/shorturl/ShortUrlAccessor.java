/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.shorturl;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface ShortUrlAccessor extends SequenceAccessor {
    //insert
    public ShortUrl insert(ShortUrl url, Connection conn) throws DbException;

    public Map<String, ShortUrl> insert(List<ShortUrl> urls, Connection conn) throws DbException;

    //get by key
    public ShortUrl getByKey(String key, Connection conn) throws DbException;

    public ShortUrl getByUrl(String url, Connection conn) throws DbException;

    public Map<String, ShortUrl> queryByKeys(Set<String> keys, Connection conn) throws DbException;

    public Map<String, ShortUrl> queryByUrls(Set<String> urls, Connection conn) throws DbException;

    //update
    public boolean update(String key, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

    @Deprecated
    //todo
     List<ShortUrl> selectByTableNo(int tableno, Connection conn)throws DbException;
}
