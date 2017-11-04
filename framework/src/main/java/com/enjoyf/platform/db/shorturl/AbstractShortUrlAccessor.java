/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.shorturl;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlFileType;
import com.enjoyf.platform.service.shorturl.ShortUrlProtocolType;
import com.enjoyf.platform.service.shorturl.ShortUrlStatus;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.shorturl.ShortUrlUtil;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractShortUrlAccessor implements ShortUrlAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractShortUrlAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "SHORTURL_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_SHORTURL_ID";
    private static final int TABLE_NUM = 100;
    private static final int URL_KEY_LEN = 5;


    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    ///private methods.
    private String getInsertSql(String key) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(key)
                + " (URLID, URLKEY, URL, PROTOCOLTYPE, FILETYPE, URLSTATUS, INITDATE, INITUNO, CLICKTIMES)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ShortUrl INSERT Script:" + insertSql);
        }

        return insertSql;
    }


    private String getKeysSelectSqlCausse(int size) {
        StringBuffer returnValue = new StringBuffer();

        for (int i = 0; i < size; i++) {
            returnValue.append("URLKEY = ? ");

            if (i < (size - 1)) {
                returnValue.append("OR ");
            }
        }

        return returnValue.toString();
    }

    protected String getTableName(String key) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(key.hashCode(), TABLE_NUM);
    }

    protected String getTableName(Integer idx) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(idx, TABLE_NUM);
    }

    protected ShortUrl rsToObject(ResultSet rs) throws SQLException {
        ShortUrl entry = new ShortUrl();

        entry.setUrlId(rs.getLong("URLID"));
        entry.setUrlKey(rs.getString("URLKEY"));
        entry.setUrl(rs.getString("URL"));

        entry.setFileType(ShortUrlFileType.getByCode(rs.getString("FILETYPE")));
        entry.setProtocolType(ShortUrlProtocolType.getByCode(rs.getString("PROTOCOLTYPE")));

        entry.setUrlStatus(ShortUrlStatus.getByCode(rs.getString("URLSTATUS")));

        entry.setInitDate(rs.getTimestamp("INITDATE") != null ? new Date(rs.getTimestamp("INITDATE").getTime()) : null);
        entry.setInitUno(rs.getString("INITUNO"));

        entry.setClickTimes(rs.getLong("CLICKTIMES"));

        return entry;
    }

    @Override
    public ShortUrl insert(ShortUrl entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setUrlId(getSeqNo(conn));
            entry.setUrlKey(ShortUrlUtil.generateShortUrl(entry.getUrlId(), URL_KEY_LEN));

            pstmt = conn.prepareStatement(getInsertSql(entry.getUrlKey()));

            //
            pstmt.setLong(1, entry.getUrlId());
            pstmt.setString(2, entry.getUrlKey());
            pstmt.setString(3, entry.getUrl());

            pstmt.setString(4, entry.getProtocolType().getCode());
            pstmt.setString(5, entry.getFileType().getCode());

            pstmt.setString(6, entry.getUrlStatus().getCode());

            pstmt.setTimestamp(7, entry.getInitDate() != null ? new Timestamp(entry.getInitDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(8, entry.getInitUno());

            pstmt.setLong(9, entry.getClickTimes());

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ShortUrl, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public Map<String, ShortUrl> insert(List<ShortUrl> urls, Connection conn) throws DbException {
        Map<String, ShortUrl> returnValue = new HashMap<String, ShortUrl>();

        //
        for (ShortUrl url : urls) {
            url = insert(url, conn);

            //
            returnValue.put(url.getUrlKey(), url);
        }

        return returnValue;
    }

    @Override
    public ShortUrl getByKey(String key, Connection conn) throws DbException {
        ShortUrl returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(key) + " WHERE URLKEY = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("ShortUrl getByKey Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, key);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select a ShortUrl, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public ShortUrl getByUrl(String url, Connection conn) throws DbException {
        return null;
    }

    @Override
    public Map<String, ShortUrl> queryByKeys(Set<String> keys, Connection conn) throws DbException {
        Map<String, ShortUrl> returnValue = new HashMap<String, ShortUrl>();

        Map<Integer, Set<String>> mapedSelectRequest = mapSelectRequest(keys);
        for (Map.Entry<Integer, Set<String>> entry : mapedSelectRequest.entrySet()) {
            returnValue.putAll(queryByKeys(entry.getKey(), entry.getValue(), conn));
        }

        return returnValue;
    }

    private Map<Integer, Set<String>> mapSelectRequest(Set<String> keys) {
        Map<Integer, Set<String>> returnValue = new HashMap<Integer, Set<String>>();

        for (String key : keys) {
            int idx = Math.abs(key.hashCode()) % TABLE_NUM;

            //
            Set<String> l = returnValue.get(idx);
            if (l == null) {
                l = new HashSet<String>();
                returnValue.put(idx, l);
            }

            //
            l.add(key);
        }

        return returnValue;
    }

    public Map<String, ShortUrl> queryByKeys(Integer idx, Set<String> keys, Connection conn) throws DbException {
        Map<String, ShortUrl> returnValue = new HashMap<String, ShortUrl>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(idx) + " WHERE " + getKeysSelectSqlCausse(keys.size());
        if (logger.isDebugEnabled()) {
            logger.debug("ShortUrl queryByKeys Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int p = 1;
            for (String key : keys) {
                pstmt.setString(p++, key);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                ShortUrl url = rsToObject(rs);

                returnValue.put(url.getUrlKey(), url);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryByKeys, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public Map<String, ShortUrl> queryByUrls(Set<String> urls, Connection conn) throws DbException {
        return null;
    }

    @Override
    public boolean update(String key, Map<ObjectField, Object> keyValues, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        String sql = "UPDATE " + getTableName(key) + " SET " + ObjectFieldUtil.generateMapSetClause(keyValues) + " WHERE URLKEY = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyValues);
            pstmt.setString(index, key);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Deprecated
    @Override
    public List<ShortUrl> selectByTableNo(int tableno, Connection conn) throws DbException {
        List<ShortUrl> returnValue = new ArrayList<ShortUrl>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(tableno, TABLE_NUM);

        try {
            pstmt = conn.prepareStatement(sql);


            rs = pstmt.executeQuery();
            while (rs.next()) {
                ShortUrl url = rsToObject(rs);

                returnValue.add(url);
            }
        } catch (SQLException e) {
            GAlerter.lab("On update a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }
}
