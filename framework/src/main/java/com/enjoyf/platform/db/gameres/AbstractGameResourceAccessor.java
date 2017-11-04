package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.gameres.GameCategorySet;
import com.enjoyf.platform.service.gameres.GameDeviceSet;
import com.enjoyf.platform.service.gameres.GameMediaScoreSet;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceStatus;
import com.enjoyf.platform.service.gameres.GameStyleSet;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.gameres.ResourceImageSet;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
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
import java.util.List;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午4:53
 * Desc:
 */
//todo insert remove category
abstract class AbstractGameResourceAccessor extends AbstractSequenceBaseTableAccessor<GameResource> implements GameResourceAccessor {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private static final String KEY_TABLE_NAME = "GAME_RESOURCE";
    private static final String KEY_SEQUENCE_NAME = "SEQ_GAME_RESOURCE_ID";

    @Override
    public GameResource insert(GameResource gameResource, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            gameResource.setResourceId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            //RESOURCEID, CATEGORYID,RESOURCENAME,GAMECODE, RESOURCEDESC,ICON,THUMBIMG,SCREENSHOT,RESOURCEURL,
            //DEVELOP,PUBLISHCOMPANY,RESOURCECATEGORY,DEVICE,OPERATIONSUTATUS,PUBLISHDATE,SYNONYMS,PLAYERNUMBER,PLAYTIME,
            //REFERID,PRICE,RESOURCEVERSION,FILESIZE,LANGUAGE,CURRENTRATING,CURRENTRATINGCOUNT,TOTALRATING,
            //TOTALRATINGCOUNT,CREATEUSERID, CREATEDATE, LASTMODIFYUSERID, LASTMODIFYDATE,
            // RESOURCEDOMAIN,SHOWVALUE,PLAYINGSUM,PLAYEDSUM,REMOVESTATUS,LOGOSIZE,
            // SEOKEYWORDS, SEODESCRIPTION, EXTSTR01, SCORE

            pstmt.setLong(1, gameResource.getResourceId());
//            pstmt.setInt(2, gameResource.getCategoryId());
            pstmt.setString(2, gameResource.getResourceName());
            pstmt.setString(3, gameResource.getGameCode() == null ? gameResource.getResourceId() + "" : gameResource.getGameCode());
            pstmt.setString(4, gameResource.getResourceDesc());
            pstmt.setString(5, gameResource.getIcon() == null ? null : gameResource.getIcon().toJsonStr());
            pstmt.setString(6, gameResource.getResourceThumbimg() == null ? null : gameResource.getResourceThumbimg().toJsonStr());
            pstmt.setString(7, gameResource.getScreenShot() == null ? null : gameResource.getScreenShot().toJsonStr());
            pstmt.setString(8, gameResource.getResourceUrl());

            pstmt.setString(9, gameResource.getDevelop());
            pstmt.setString(10, gameResource.getPublishCompany());
            pstmt.setString(11, gameResource.getCategorySet() == null ? null : gameResource.getCategorySet().toJsonStr());
            pstmt.setString(12, gameResource.getDeviceSet() == null ? null : gameResource.getDeviceSet().toJsonStr());
            pstmt.setString(13, gameResource.getOperationstatus());
            pstmt.setString(14, gameResource.getPublishDate());
            pstmt.setString(15, gameResource.getSynonyms());
            pstmt.setString(16, gameResource.getPlayerNumber());
            pstmt.setString(17, gameResource.getPlayTime());

            pstmt.setString(18, StringUtil.isEmpty(gameResource.getReferId()) ? String.valueOf(gameResource.getResourceId()) : gameResource.getReferId());
            pstmt.setString(19, gameResource.getPrice());
            pstmt.setString(20, gameResource.getResourceVersion());
            pstmt.setString(21, gameResource.getFileSize());
            pstmt.setString(22, gameResource.getLanguage());
            pstmt.setFloat(23, gameResource.getCurrentRating() == null ? 0f : gameResource.getCurrentRating());
            pstmt.setInt(24, gameResource.getCurrentRatingCount() == null ? 0 : gameResource.getCurrentRatingCount());
            pstmt.setFloat(25, gameResource.getTotalRating() == null ? 0f : gameResource.getTotalRating());
            pstmt.setInt(26, gameResource.getTotalRatingCount() == null ? 0 : gameResource.getTotalRatingCount());

            //
            pstmt.setString(27, gameResource.getCreateUserid());
            pstmt.setTimestamp(28, new Timestamp((gameResource.getCreateDate() == null ? new Date() : gameResource.getCreateDate()).getTime()));

            pstmt.setString(29, gameResource.getModifyUserid());
            pstmt.setTimestamp(30, gameResource.getModifyDate() == null ? null : new Timestamp(gameResource.getCreateDate().getTime()));

            pstmt.setString(31, gameResource.getResourceDomain() == null ? null : gameResource.getResourceDomain().getCode());
            pstmt.setInt(32, gameResource.getResourceStatus() == null ? 0 : gameResource.getResourceStatus().getValue());
            pstmt.setInt(33, gameResource.getPlayingSum() == null ? 0 : gameResource.getPlayingSum());
            pstmt.setInt(34, gameResource.getPlayedSum() == null ? 0 : gameResource.getPlayedSum());
            pstmt.setString(35, gameResource.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : gameResource.getRemoveStatus().getCode());
            pstmt.setString(36, gameResource.getLogoSize());
            pstmt.setString(37, gameResource.getStyleSet() == null ? null : gameResource.getStyleSet().toJsonStr());

            pstmt.setString(38, gameResource.getSeoKeyWords());
            pstmt.setString(39, gameResource.getSeoDescription());
            pstmt.setString(40, StringUtil.isEmpty(gameResource.getEventDesc()) ? null : gameResource.getEventDesc());
            pstmt.setString(41, gameResource.getGameMediaScoreSet() == null ? null : gameResource.getGameMediaScoreSet().toJsonStr());

            pstmt.setString(42, StringUtil.isEmpty(gameResource.getLastUpdateDate()) ? null : gameResource.getLastUpdateDate());
            pstmt.setString(43, gameResource.getBuyLink() == null ? null : gameResource.getBuyLink());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return gameResource;
    }

    @Override
    public GameResource get(QueryExpress getExpress, Connection conn) throws DbException {
        //
        return get(getTableName(), getExpress, conn);
    }

    @Override
    public List<GameResource> query(QueryExpress queryExpress, Connection conn) throws DbException {
        //
        return query(getTableName(), queryExpress, conn);
    }

    @Override
    public List<GameResource> queryByPage(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    protected String getTableName() throws DbException {
        return KEY_TABLE_NAME;
    }

    private String getInsertSql() throws DbException {
        String sql = "INSERT INTO " + getTableName() +
                " (RESOURCEID, RESOURCENAME, GAMECODE, RESOURCEDESC, ICON, THUMBIMG, SCREENSHOT, RESOURCEURL," +
                " DEVELOP, PUBLISHCOMPANY, RESOURCECATEGORY, DEVICE, OPERATIONSUTATUS, PUBLISHDATE, SYNONYMS, PLAYERNUMBER, PLAYTIME," +
                " REFERID, PRICE, RESOURCEVERSION, FILESIZE, LANGUAGE, CURRENTRATING, CURRENTRATINGCOUNT, TOTALRATING, TOTALRATINGCOUNT," +
                " CREATEUSERID, CREATEDATE, LASTMODIFYUSERID, LASTMODIFYDATE, RESOURCEDOMAIN, SHOWVALUE, PLAYINGSUM, PLAYEDSUM, REMOVESTATUS," +
                " LOGOSIZE, RESOURCESTYLE, SEOKEYWORDS, SEODESCRIPTION, EXTSTR01, SCORE, LASTUPDATEDATE, BUYLINK)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" insert sql is :" + sql);
        }

        return sql;
    }

    protected GameResource rsToObject(ResultSet rs) throws SQLException {
        GameResource entry = new GameResource();

        //" (RESOURCEID, CATEGORYID, RESOURCENAME, GAMECODE, RESOURCEDESC, ICON, THUMBIMG, SCREENSHOT, RESOURCEURL," +
        //" DEVELOP, PUBLISHCOMPANY, RESOURCECATEGORY, DEVICE, OPERATIONSUTATUS, PUBLISHDATE, SYNONYMS, PLAYERNUMBER, PLAYTIME," +
        //" REFERID, PRICE, RESOURCEVERSION, FILESIZE, LANGUAGE, CURRENTRATING, CURRENTRATINGCOUNT, TOTALRATING, TOTALRATINGCOUNT," +
        //" CREATEUSERID, CREATEDATE, LASTMODIFYUSERID, LASTMODIFYDATE, RESOURCEDOMAIN, SHOWVALUE, PLAYINGSUM, PLAYEDSUM, REMOVESTATUS," +
        //" LOGOSIZE, RESOURCESTYLE, SEOKEYWORDS, SEODESCRIPTION, EXTSTR01)" +

        entry.setResourceId(rs.getLong("RESOURCEID"));
//        entry.setCategoryId(rs.getInt("CATEGORYID"));
        entry.setResourceName(rs.getString("RESOURCENAME"));
        entry.setGameCode(rs.getString("GAMECODE"));
        entry.setResourceDesc(rs.getString("RESOURCEDESC"));
        entry.setIcon(ResourceImageSet.parse(rs.getString("ICON")));
        entry.setResourceThumbimg(ImageContentSet.parse(rs.getString("THUMBIMG")));
        entry.setScreenShot(ImageContentSet.parse(rs.getString("SCREENSHOT")));
        entry.setResourceUrl(rs.getString("RESOURCEURL"));
        entry.setDevelop(rs.getString("DEVELOP"));
        entry.setPublishCompany(rs.getString("PUBLISHCOMPANY"));
        entry.setCategorySet(GameCategorySet.parse(rs.getString("RESOURCECATEGORY")));
        entry.setDeviceSet(GameDeviceSet.parse(rs.getString("DEVICE")));
        entry.setOperationstatus(rs.getString("OPERATIONSUTATUS"));
        entry.setPublishDate(rs.getString("PUBLISHDATE"));
        entry.setSynonyms(rs.getString("SYNONYMS"));
        entry.setPlayerNumber(rs.getString("PLAYERNUMBER"));
        entry.setPlayTime(rs.getString("PLAYTIME"));
        entry.setReferId(rs.getString("REFERID"));
        entry.setPrice(rs.getString("PRICE"));
        entry.setResourceVersion(rs.getString("RESOURCEVERSION"));
        entry.setFileSize(rs.getString("FILESIZE"));
        entry.setLanguage(rs.getString("LANGUAGE"));
        entry.setCurrentRating(rs.getFloat("CURRENTRATING"));
        entry.setCurrentRatingCount(rs.getInt("CURRENTRATINGCOUNT"));
        entry.setTotalRating(rs.getFloat("TOTALRATING"));
        entry.setTotalRatingCount(rs.getInt("TOTALRATINGCOUNT"));

        entry.setCreateUserid(rs.getString("CREATEUSERID"));
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") == null ? null : new Date(rs.getTimestamp("CREATEDATE").getTime()));

        entry.setModifyUserid(rs.getString("LASTMODIFYUSERID"));
        entry.setModifyDate(rs.getTimestamp("LASTMODIFYDATE") == null ? null : new Date(rs.getTimestamp("LASTMODIFYDATE").getTime()));

        entry.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        entry.setResourceDomain(ResourceDomain.getByCode(rs.getString("RESOURCEDOMAIN")));

        entry.setPlayedSum(rs.getInt("PLAYEDSUM"));
        entry.setPlayingSum(rs.getInt("PLAYINGSUM"));

        entry.setResourceStatus(new GameResourceStatus(rs.getInt("SHOWVALUE")));
        entry.setStyleSet(GameStyleSet.parse(rs.getString("RESOURCESTYLE")));
        entry.setLogoSize(rs.getString("LOGOSIZE"));

        entry.setSeoKeyWords(rs.getString("SEOKEYWORDS"));
        entry.setSeoDescription(rs.getString("SEODESCRIPTION"));

        entry.setEventDesc(rs.getString("EXTSTR01"));

        entry.setGameMediaScoreSet(rs.getString("SCORE") == null ? null : new GameMediaScoreSet(rs.getString("SCORE")));

        entry.setLastUpdateDate(rs.getString("LASTUPDATEDATE"));
        entry.setBuyLink(rs.getString("BUYLINK"));

        return entry;
    }

    @Override
    public List<GameResource> queryBySynonyms(String synonyms, Connection conn) throws DbException {
        //
        List<GameResource> returnValue = new ArrayList<GameResource>();

        //
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        //
        String sql = "SELECT * FROM " + getTableName() + " WHERE FIND_IN_SET(?, SYNONYMS) AND REMOVESTATUS = ? AND RESOURCEDOMAIN = 'game'";
        if (logger.isDebugEnabled()) {
            logger.debug(" getBySynonyms script:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, synonyms);
            pstmt.setString(2, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            //
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryBySynonyms, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        //
        return update(getTableName(), updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(getTableName(), queryExpress, conn);
    }
}
