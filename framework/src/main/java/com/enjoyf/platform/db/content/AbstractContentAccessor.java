/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;


import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.PrivacyType;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class AbstractContentAccessor extends AbstractBaseTableAccessor<Content> implements ContentAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractContentAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "CONTENT";
    private static final int TABLE_NUM = 100;

    @Override
    public Content insert(Content entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //  init entry data
            if (Strings.isNullOrEmpty(entry.getContentId())) {
                entry.setContentId(getContentId()); //id
            }

            if (entry.getAuditStatus() == null) {
                entry.setAuditStatus(new ContentAuditStatus(0));
            }

            pstmt = conn.prepareStatement(getInsertSql());

            //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,SEARCHTAG,CONTENTBODY,IMAGES,AUDIOS,VIDEOS,APPS,GAMES,VOTESUBJECTID,THUMBIMGLINK,
            //PUBLISHTYPE,CONTENTTYPE,CONTENTPRIVACY,PUBLISHDATE,PUBLISHIP,
            // ROOTCONTENTID,ROOTCONTENTUNO, PARENTCONTENTID, PARENTCONTENTUNO,
            //REPLYTIMES,FORWARDTIMES,FAVORTIMES,EXTNUM01,EXTNUM02,EXTNUM03,EXTNUM04, EXTSTR01,EXTSTR02,
            // UPDATEDATE,REMOVESTATUS

            pstmt.setString(1, entry.getContentId());
            pstmt.setString(2, entry.getUno());

            pstmt.setString(3, entry.getSubject());
            pstmt.setString(4, entry.getContentTag() != null ? entry.getContentTag().stringValue() : null);
            pstmt.setString(5, entry.getSearchTag() != null ? entry.getSearchTag().stringValue() : null);
            pstmt.setString(6, entry.getContent());

            pstmt.setString(7, entry.getImages() != null ? entry.getImages().toJsonStr() : null);
            pstmt.setString(8, entry.getAudios() != null ? entry.getAudios().toJsonStr() : null);
            pstmt.setString(9, entry.getVideos() != null ? entry.getVideos().toJsonStr() : null);
            pstmt.setString(10, entry.getApps() != null ? entry.getApps().toJsonStr() : null);
            pstmt.setString(11, entry.getGames() != null ? entry.getGames().toJsonStr() : null);

            pstmt.setString(12, entry.getVoteSubjectId());

            pstmt.setString(13, entry.getThumbImgLink());
            pstmt.setString(14, entry.getPublishType().getCode());
            pstmt.setInt(15, entry.getContentType().getValue());
            pstmt.setString(16, entry.getPrivacy().getCode());

            pstmt.setTimestamp(17, entry.getPublishDate() != null ? new Timestamp(entry.getPublishDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(18, entry.getPublishIp());

            pstmt.setString(19, entry.getRootContentId());
            pstmt.setString(20, entry.getRootContentUno());

            pstmt.setString(21, entry.getParentContentId());
            pstmt.setString(22, entry.getParentContentUno());

            pstmt.setInt(23, entry.getReplyTimes());
            pstmt.setInt(24, entry.getForwardTimes());
            pstmt.setInt(25, entry.getFavorTimes());

            pstmt.setInt(26, entry.getDingTimes());
            pstmt.setInt(27, entry.getCaiTimes());
            pstmt.setInt(28, entry.getViewTimes());
            pstmt.setInt(29, entry.getFloorTimes());

            pstmt.setString(30, entry.getLastReplyId());

            pstmt.setString(31, null);

            pstmt.setTimestamp(32, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

            pstmt.setString(33, entry.getRemoveStatus().getCode());
            pstmt.setInt(34, entry.getAuditStatus() != null ? entry.getAuditStatus().getValue() : 0);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    //更新内容中的数，收藏数、转发数、回复数等。
    public boolean updateContentNum(String contentId, ObjectField field, Integer value, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getUpdateContentSumSql(field));

            pstmt.setInt(1, value);
            pstmt.setString(2, contentId);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On updateContentNum a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    //更新博文内容
    public boolean updateContent(String contentId, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName() + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE CONTENTID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("Content update script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setString(index, contentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On updateContent a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

    }

    @Override
    public int updateContnet(String uno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(getTableName(), updateExpress, queryExpress, conn);
    }

    //查询单条记录，根据ID，UNO
    public Content getByCidUno(String contentId, Connection conn) throws DbException {
        Content returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName() + " WHERE CONTENTID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("Content getByCidUno script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, contentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  Content findById, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }


    // 删除--- 更新removed 字段
    @Override
    public boolean remove(String contentId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName() + " SET REMOVESTATUS = ? WHERE CONTENTID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("Content remove script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ActStatus.ACTED.getCode());
            pstmt.setString(2, contentId);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update content removed, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

    }

    public List<Content> queryContents(Set<String> contentIds, Connection conn) throws DbException {
        List<Content> returnValue = new ArrayList<Content>();

        if (contentIds.size() < 1) {
            return returnValue;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName() + " WHERE " + getCidsQueryWhereCause(contentIds.size()));

            int i = 1;
            for (String contentId : contentIds) {
                pstmt.setString(i, contentId);
                i++;
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On  Content queryContents, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<Content> queryContents(String uno, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Content> queryContentsByTimeStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Content> queryLastestContents(ContentPublishType publishType, ContentType contentType, Integer size, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Content> queryLastestContents(String uno, Integer size, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Content> queryContentByParam(ContentQueryParam param, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }


    @Override
    public List<Content> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    //
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + getTableName()
                + " (CONTENTID, UNO, CONTENTSUBJECT, CONTENTTAG,SEARCHTAG, CONTENTBODY, IMAGES, AUDIOS, VIDEOS, APPS,GAMES,VOTESUBJECTID, THUMBIMGLINK, "
                + "PUBLISHTYPE, CONTENTTYPE, CONTENTPRIVACY, PUBLISHDATE, PUBLISHIP, ROOTCONTENTID, ROOTCONTENTUNO, "
                + "PARENTCONTENTID, PARENTCONTENTUNO, REPLYTIMES, FORWARDTIMES, FAVORTIMES, EXTNUM01, EXTNUM02, EXTNUM03,EXTNUM04,EXTSTR01,EXTSTR02, UPDATEDATE, REMOVESTATUS, AUDITSTATUS) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Content INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getCidsQueryWhereCause(Integer cidsSize) {
        StringBuilder stringBuilder = new StringBuilder();

        if (cidsSize > 1) {
            stringBuilder.append("(");
        }

        for (int i = 0; i < cidsSize; i++) {
            stringBuilder.append(" CONTENTID = ?");

            if (i < (cidsSize - 1)) {
                stringBuilder.append(" OR");
            }
        }

        if (cidsSize > 1) {
            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }

    protected String getTableName() throws DbException {
        return KEY_TABLE_NAME_PREFIX;
    }

    protected String getTableNameByViewContent() throws DbException {
        return "CONTENT";
    }

    @Override
    protected Content rsToObject(ResultSet rs) throws SQLException {
        Content entity = new Content();

        //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,SEARCHTAG,CONTENTBODY,IMAGES,AUDIOS,VIDEOS,APPS,VOTESUBJECTID,THUMBIMGLINK,
        //PUBLISHTYPE,CONTENTTYPE,CONTENTPRIVACY,PUBLISHDATE,PUBLISHIP,ROOTCONTENTID,ROOTCONTENTUNO,
        //PARENTCONTENTID,PARENTCONTENTUNO,REPLYTIMES,FORWARDTIMES,FAVORTIMES,
        // EXTNUM01,EXTNUM02,EXTNUM03,EXTSTR01,EXTSTR02,UPDATEDATE,REMOVESTATUS

        entity.setContentId(rs.getString("CONTENTID"));

        entity.setUno(rs.getString("UNO"));

        entity.setSubject(rs.getString("CONTENTSUBJECT"));
        entity.setContentTag(ContentTag.parse(rs.getString("CONTENTTAG")));
        entity.setSearchTag(ContentTag.parse(rs.getString("SEARCHTAG")));
        entity.setContent(rs.getString("CONTENTBODY"));

        entity.setImages(ImageContentSet.parse(rs.getString("IMAGES")));
        entity.setAudios(AudioContentSet.parse(rs.getString("AUDIOS")));
        entity.setVideos(VideoContentSet.parse(rs.getString("VIDEOS")));
        entity.setApps(AppsContentSet.parse(rs.getString("APPS")));
        entity.setGames(GameContentSet.parse(rs.getString("GAMES")));

        entity.setVoteSubjectId(rs.getString("VOTESUBJECTID"));

        entity.setThumbImgLink(rs.getString("THUMBIMGLINK"));

        entity.setPublishType(ContentPublishType.getByCode(rs.getString("PUBLISHTYPE")));
        entity.setContentType(ContentType.getByValue(rs.getInt("CONTENTTYPE")));
        entity.setPrivacy(PrivacyType.getByCode(rs.getString("CONTENTPRIVACY")));

        entity.setPublishDate(rs.getTimestamp("PUBLISHDATE"));
        entity.setPublishIp(rs.getString("PUBLISHIP"));

        entity.setRootContentId(rs.getString("ROOTCONTENTID"));
        entity.setRootContentUno(rs.getString("ROOTCONTENTUNO"));

        entity.setParentContentId(rs.getString("PARENTCONTENTID"));
        entity.setParentContentUno(rs.getString("PARENTCONTENTUNO"));

        entity.setReplyTimes(rs.getInt("REPLYTIMES"));
        entity.setForwardTimes(rs.getInt("FORWARDTIMES"));
        entity.setFavorTimes(rs.getInt("FAVORTIMES"));

        entity.setDingTimes(rs.getInt("EXTNUM01"));
        entity.setCaiTimes(rs.getInt("EXTNUM02"));
        entity.setViewTimes(rs.getInt("EXTNUM03"));
        entity.setFloorTimes(rs.getInt("EXTNUM04"));

        entity.setLastReplyId(rs.getString("EXTSTR01"));
//        entity.setResourceId(rs.getString("EXTSTR02"));

        entity.setUpdateDate(rs.getTimestamp("UPDATEDATE"));

        entity.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        entity.setAuditStatus(ContentAuditStatus.getByValue(rs.getInt("AUDITSTATUS")));

        return entity;
    }


    protected String getUpdateContentSumSql(ObjectField field) throws DbException {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("UPDATE ").append(getTableName()).append(" SET ").append(field.getColumn()).append(" = ").append(field.getColumn()).
                append(" + ? WHERE CONTENTID = ?").append(" AND ").append(field.getColumn()).append(" >= 0");

        if (logger.isDebugEnabled()) {
            logger.debug("Content updateSum sql:" + stringBuffer.toString());
        }

        return stringBuffer.toString();
    }

    protected String getSearchTextWhereCause(Set<String> keySet) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> keySetIterator = keySet.iterator();
        while (keySetIterator.hasNext()) {
            sb.append("(CONTENTSUBJECT LIKE ? OR CONTENTBODY LIKE ? OR CONTENTTAG LIKE ?)");
            keySetIterator.next();
            if (keySetIterator.hasNext()) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

    //initNo=5 pstmt.set(5,key)
    protected int setSearchTextSqlParam(Set<String> keySet, PreparedStatement pstmt, int initNo) throws SQLException {
        for (String key : keySet) {
            pstmt.setString(initNo, "%" + key + "%");
            initNo++;
            pstmt.setString(initNo, "%" + key + "%");
            initNo++;
            pstmt.setString(initNo, "%" + key + "%");
            initNo++;
        }
        return initNo;
    }

    protected String getSearchTagWhereCause(ContentTag contentTag) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> tagSetIterator = contentTag.getTags().iterator();
        while (tagSetIterator.hasNext()) {
            sb.append("(CONTENTTAG LIKE ?) ");
            tagSetIterator.next();
            if (tagSetIterator.hasNext()) {
                sb.append("AND ");
            }
        }
        return sb.toString();
    }

    protected int setSearchTagSqlParam(ContentTag contentTag, PreparedStatement pstmt, int initNo) throws SQLException {
        for (String tag : contentTag.getTags()) {
            pstmt.setString(initNo, "%" + tag + "%");
            initNo++;
        }
        return initNo;
    }


    private String getContentId() {
        return UUIDUtil.getShortUUID();

    }
}
