package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceValue;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.gameres.GameCategorySet;
import com.enjoyf.platform.service.gameres.GameDeviceSet;
import com.enjoyf.platform.service.gameres.GameRelation;
import com.enjoyf.platform.service.gameres.GameRelationType;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceLink;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceStatus;
import com.enjoyf.platform.service.gameres.GameStyleSet;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.gameres.ResourceImageSet;
import com.enjoyf.platform.service.viewline.LocationCode;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-10-22
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
public class GameDataProcessor {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.30.205:3311/GAMETAG?useUnicode=true&characterEncoding=UTF-8", "xiuyujia", "xiuyujia");
            con.setAutoCommit(false);
            pstmt = con.prepareStatement("SELECT * FROM GAME_RESOURCE where RESOURCEDOMAIN ='temp'");

            rs=pstmt.executeQuery();

            while (rs.next()){
                GameResource gameResource = GameDataProcessor.rsToObject(rs);
                gameResource.setResourceDomain(ResourceDomain.GAME);
                long id = getSeqNo("SEQ_GAME_RESOURCE_ID", con);
                gameResource.setResourceId(id);
                gameResource = insert(gameResource, con);


                ViewCategory boardCategory = new ViewCategory();
                ViewLine boardViewLine = new ViewLine();
                ViewCategory downloadCategory = new ViewCategory();
                ViewLine downloadLine = new ViewLine();
                ViewCategory contentLinkCategory = new ViewCategory();
                ViewLine contentLinkLine = new ViewLine();

                boardCategory.setCategoryName(gameResource.getResourceName());
                boardCategory.setLocationCode(LocationCode.DEFAULT.getCode());
                //加个game是为了区别小组的categoryCode和角度的唯一约束
                boardCategory.setCategoryCode("game" + gameResource.getGameCode());   //
                boardCategory.setCreateDate(new Date());
                boardCategory.setCreateUserid(gameResource.getCreateUserid());
                boardCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                boardCategory.setParentCategoryId(0);
                boardCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                boardCategory.setValidStatus(ValidStatus.VALID);
                boardCategory.setPublishStatus(ActStatus.ACTED);
                boardCategory = insertViewCategory(boardCategory);

                boardViewLine.setCategoryId(boardCategory.getCategoryId());
                boardViewLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                boardViewLine.setLineName(boardCategory.getCategoryName());
                boardViewLine.setItemType(ViewItemType.CUSTOM);
                boardViewLine.setLocationCode(LocationCode.GAME_LINK.getCode());
                boardViewLine.setCreateDate(new Date());
                boardViewLine.setCreateUserid(gameResource.getCreateUserid());
                boardViewLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                boardViewLine.setValidStatus(ValidStatus.VALID);
                boardViewLine = insertViewLine(boardViewLine);


//        if (gameResourceLinkSet != null) {
//            Set<GameResourceLink> links = gameResourceLinkSet.getLinks();
//            Iterator<GameResourceLink> it = links.iterator();
//            while (it.hasNext()) {
//                GameResourceLink gameResourceLink = it.next();
//
//                ViewLineItem lineItem = new ViewLineItem();
//                lineItem.setCategoryAspect(boardViewLine.getCategoryAspect());
//                lineItem.setCategoryId(boardViewLine.getCategoryId());
//                lineItem.setCreateDate(new Date());
//                lineItem.setCreateUno(gameResource.getCreateUserid());
//                lineItem.setItemCreateDate(gameResource.getCreateDate());
//                lineItem.setLineId(boardViewLine.getLineId());
//                lineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
//
//                ViewLineItemDisplayInfo itemDisplayInfo = new ViewLineItemDisplayInfo();
//                itemDisplayInfo.setSubject(gameResourceLink.getLinkName());
//                itemDisplayInfo.setLinkUrl(gameResourceLink.getLinkUrl());
//                lineItem.setDisplayInfo(itemDisplayInfo);
//                lineItem.setValidStatus(ValidStatus.VALID);
//                ViewLineServiceSngl.get().addLineItem(lineItem);
//            }
//        }

                downloadCategory.setCategoryName("相关下载");
                downloadCategory.setLocationCode(LocationCode.DOWNLOAD_LINK.getCode());
                downloadCategory.setCategoryCode(boardCategory.getCategoryCode() + LocationCode.DOWNLOAD_LINK.getCode());
                downloadCategory.setParentCategoryId(boardCategory.getCategoryId()); //father
                downloadCategory.setCreateDate(new Date());
                downloadCategory.setCreateUserid(gameResource.getCreateUserid());
                downloadCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                downloadCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                downloadCategory.setValidStatus(ValidStatus.VALID);
                downloadCategory.setPublishStatus(ActStatus.ACTED);
                downloadCategory = insertViewCategory(downloadCategory);

                downloadLine.setCategoryId(downloadCategory.getCategoryId());
                downloadLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                downloadLine.setLineName(downloadCategory.getCategoryName());
                downloadLine.setItemType(ViewItemType.CONTENT);
                downloadLine.setLocationCode(LocationCode.DEFAULT.getCode());
                downloadLine.setCreateDate(new Date());
                downloadLine.setCreateUserid(gameResource.getCreateUserid());
                downloadLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                downloadLine.setValidStatus(ValidStatus.VALID);
                insertViewLine(downloadLine);

                contentLinkCategory.setCategoryName("相关小组文章");
                contentLinkCategory.setLocationCode(LocationCode.GROUP_CONTENT_LINK.getCode());
                contentLinkCategory.setCategoryCode(boardCategory.getCategoryCode() + LocationCode.GROUP_CONTENT_LINK.getCode());
                contentLinkCategory.setParentCategoryId(boardCategory.getCategoryId()); //father
                contentLinkCategory.setCreateDate(new Date());
                contentLinkCategory.setCreateUserid(gameResource.getCreateUserid());
                contentLinkCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                contentLinkCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                contentLinkCategory.setValidStatus(ValidStatus.VALID);
                contentLinkCategory.setPublishStatus(ActStatus.ACTED);
                contentLinkCategory = insertViewCategory(contentLinkCategory);

                contentLinkLine.setCategoryId(contentLinkCategory.getCategoryId());
                contentLinkLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
                contentLinkLine.setLineName(contentLinkCategory.getCategoryName());
                contentLinkLine.setItemType(ViewItemType.CONTENT);
                contentLinkLine.setLocationCode(LocationCode.DEFAULT.getCode());
                contentLinkLine.setCreateDate(new Date());
                contentLinkLine.setCreateUserid(gameResource.getCreateUserid());
                contentLinkLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                contentLinkLine.setValidStatus(ValidStatus.VALID);
                insertViewLine(contentLinkLine);

                //
                GameRelation gameRelation = new GameRelation();
                gameRelation.setResourceId(gameResource.getResourceId());
                gameRelation.setSortNum(1);
                gameRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_BOARD);
                gameRelation.setRelationValue(String.valueOf(boardCategory.getCategoryId()));
                gameRelation = insertRelation(gameRelation, con);

                con.commit();
            }
            System.out.println("---------------------导游戏数据结束-------------------");
//            pstmt.
//
//           con.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            rs.close();
            pstmt.close();
            con.close();
        }


    }

    protected static GameResource rsToObject(ResultSet rs) throws SQLException {
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

        return entry;
    }

    public static long getSeqNo(String sequenceName, Connection conn) throws DbException {
        try {
            MyTableSequenceFetcher fetcher = new MyTableSequenceFetcher();
            return fetcher.generate(sequenceName, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + sequenceName);
        }
    }

    public static GameResource insert(GameResource gameResource, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());



            //RESOURCEID, CATEGORYID,RESOURCENAME,GAMECODE, RESOURCEDESC,ICON,THUMBIMG,SCREENSHOT,RESOURCEURL,
            //DEVELOP,PUBLISHCOMPANY,RESOURCECATEGORY,DEVICE,OPERATIONSUTATUS,PUBLISHDATE,SYNONYMS,PLAYERNUMBER,PLAYTIME,
            //REFERID,PRICE,RESOURCEVERSION,FILESIZE,LANGUAGE,CURRENTRATING,CURRENTRATINGCOUNT,TOTALRATING,
            //TOTALRATINGCOUNT,CREATEUSERID, CREATEDATE, LASTMODIFYUSERID, LASTMODIFYDATE,
            // RESOURCEDOMAIN,SHOWVALUE,PLAYINGSUM,PLAYEDSUM,REMOVESTATUS,LOGOSIZE,
            // SEOKEYWORDS, SEODESCRIPTION, EXTSTR01

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

            pstmt.executeUpdate();
        } catch (SQLException e) {

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return gameResource;
    }

    private static String getInsertSql() throws DbException {
        String sql = "INSERT INTO GAME_RESOURCE" +
                " (RESOURCEID, RESOURCENAME, GAMECODE, RESOURCEDESC, ICON, THUMBIMG, SCREENSHOT, RESOURCEURL," +
                " DEVELOP, PUBLISHCOMPANY, RESOURCECATEGORY, DEVICE, OPERATIONSUTATUS, PUBLISHDATE, SYNONYMS, PLAYERNUMBER, PLAYTIME," +
                " REFERID, PRICE, RESOURCEVERSION, FILESIZE, LANGUAGE, CURRENTRATING, CURRENTRATINGCOUNT, TOTALRATING, TOTALRATINGCOUNT," +
                " CREATEUSERID, CREATEDATE, LASTMODIFYUSERID, LASTMODIFYDATE, RESOURCEDOMAIN, SHOWVALUE, PLAYINGSUM, PLAYEDSUM, REMOVESTATUS," +
                " LOGOSIZE, RESOURCESTYLE, SEOKEYWORDS, SEODESCRIPTION, EXTSTR01)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        return sql;
    }


public static class MyTableSequenceFetcher {
    private Map<String, SequenceValue> sequenceMap = new HashMap<String, SequenceValue>();

    // the table name which store all the sequence of the database.
    private static final String SEQUENCE_TABLE_NAME = "SEQUENCES_TABLE";

    public MyTableSequenceFetcher() {
    }

    public long generate(String sequenceName, Connection conn) throws TableSequenceException {
        SequenceValue sequenceValue = null;
        long returnValue = 0;

        // get the sequence from map or initialize it.
        synchronized (sequenceMap) {
            sequenceValue = sequenceMap.get(sequenceName);

            if (sequenceValue == null) {
                sequenceValue = new SequenceValue(sequenceName);

                sequenceMap.put(sequenceValue.getSequenceName(), sequenceValue);
            }
        }

        // get the next value from the cache.
        synchronized (sequenceValue) {
            // if the cache has next value, just get one from the cache.
            if (sequenceValue.hasNext()) {
                returnValue = sequenceValue.getNextValue();
            } else {
                // if the cache hasn't next value, get more from db.
                long dbValue = 0;
                try {
                    dbValue = fetchNextValue(sequenceName, 10, conn);
                }
                catch (SQLException e) {
                    throw new TableSequenceException("Generate sequence value failed, the sequence name is " + sequenceName + " " + e);
                }

                // set the db value to cache.
                sequenceValue.setCurValue(dbValue);
                sequenceValue.setMaxValue(dbValue + 10 - 1);

                returnValue = sequenceValue.getNextValue();
            }
        }

        return returnValue;
    }

    private long fetchNextValue(String sequenceName, int step, Connection conn) throws SQLException {
        String selectSql = "SELECT CURVALUE FROM " + SEQUENCE_TABLE_NAME + " WHERE SEQUENCENAME = ?";
        String updateSql = "UPDATE " + SEQUENCE_TABLE_NAME + " SET CURVALUE = CURVALUE + ? WHERE SEQUENCENAME = ? AND CURVALUE = ?";

        long curValue = -1;

        boolean success = false;
        int tryTimes = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            while (!success && (tryTimes < 3)) {
                // select the current value from table.
                pstmt = conn.prepareStatement(selectSql);
                pstmt.setString(1, sequenceName.trim());

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    curValue = rs.getLong(1);
                } else {
                    throw new SQLException("The sequence is not exist in sequence table, sequence:" + sequenceName);
                }

                // update the sequence value.
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setLong(1, step);
                pstmt.setString(2, sequenceName);
                pstmt.setLong(3, curValue);

                if (pstmt.executeUpdate() > 0) {
                    success = true;
                } else {
                    curValue = -1;
                }

                // try times increase.
                tryTimes++;
            }
        }
        finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        if (curValue < 0) {
            throw new SQLException("Fetch sequence value from table failed, curValue < 0,the sequence name is " + sequenceName);
        }

        return curValue;
    }

}



    public static ViewCategory insertViewCategory(ViewCategory entry) throws DbException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

                String insertSql = "INSERT INTO VIEWLINE_CATEGORY (CATEGORYID, CATEGORYDOMAIN, CATEGORYASPECT, CATEGORYCODE, LOCATIONCODE, CATEGORYNAME, CATEGORYDESC, SEOKEYWORD, SEODESC, " +
                "PARENTCATEGORYID, DISPLAYSETTING, DISPLAYORDER, VALIDSTATUS, PUBLISHSTATUS, " +
                "CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.30.205:3311/VIEWLINE?useUnicode=true&characterEncoding=UTF-8", "xiuyujia", "xiuyujia");
//            con.setAutoCommit(false);
            pstmt = con.prepareStatement(insertSql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            pstmt = con.prepareStatement(insertSql);

            entry.setCategoryId((int) getSeqNo("SEQ_VIEWLINE_CATEGORY_ID", con));

            if (Strings.isNullOrEmpty(entry.getCategoryCode())) {
                entry.setCategoryCode(String.valueOf(entry.getCategoryId()));
            }

            // CATEGORYID, CATEGORYDOMAIN, CATEGORYASPECT, CATEGORYCODE, LOCATIONCODE, CATEGORYNAME, CATEGORYDESC, SEOKEYWORD, SEODESC,
            // PARENTCATEGORYID, DISPLAYSETTING, DISPLAYORDER, VALIDSTATUS, PUBLISHSTATUS,
            // CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID

            pstmt.setInt(1, entry.getCategoryId());

            pstmt.setString(2, entry.getCategoryAspect().getItemType().getCode());
            pstmt.setString(3, entry.getCategoryAspect().getCode());

            pstmt.setString(4, !StringUtil.isEmpty(entry.getCategoryCode())?entry.getCategoryCode():String.valueOf(entry.getCategoryId()));
            pstmt.setString(5, entry.getLocationCode());

            pstmt.setString(6, entry.getCategoryName());
            pstmt.setString(7, entry.getCategoryDesc());

            pstmt.setString(8, entry.getSeoKeyWord());
            pstmt.setString(9, entry.getSeoDesc());

            pstmt.setInt(10, entry.getParentCategoryId());

            pstmt.setString(11, entry.getDisplaySetting() != null ? entry.getDisplaySetting().toJson() : null);
            pstmt.setInt(12, entry.getDisplayOrder());

            pstmt.setString(13, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);
            pstmt.setString(14, entry.getPublishStatus() != null ? entry.getPublishStatus().getCode() : null);

            pstmt.setTimestamp(15, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : null);
            pstmt.setString(16, entry.getCreateUserid());

            pstmt.setTimestamp(17, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(18, entry.getUpdateUserid());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("On insert ViewCategory, a SQLException occured.");
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    public static ViewLine insertViewLine(ViewLine entry) throws DbException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        String insertSql = "INSERT INTO VIEWLINE_LINE (LINEID, LINENAME, LINEDESC, CATEGORYID, CATEGORYASPECT, LOCATIONCODE, ITEMTYPE, ITEMMINCOUNT, DISPLAYORDER, " +
                "AUTOFILLTYPE, AUTOFILLRULE, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID, VALIDSTATUS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.30.205:3311/VIEWLINE?useUnicode=true&characterEncoding=UTF-8", "xiuyujia", "xiuyujia");
//            con.setAutoCommit(false);
            pstmt = con.prepareStatement(insertSql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            pstmt = con.prepareStatement(insertSql);

            entry.setLineId((int) getSeqNo("SEQ_VIEWLINE_LINE_ID", con));

            // LINEID, LINENAME, LINEDESC, CATEGORYID, CATEGORYASPECT, LOCATIONCODE, ITEMTYPE, ITEMMINCOUNT, DISPLAYORDER,
            // AUTOFILLTYPE, AUTOFILLRULE, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID, VALIDSTATUS
            pstmt.setInt(1, entry.getLineId());

            pstmt.setString(2, entry.getLineName());
            pstmt.setString(3, entry.getLineDesc());

            pstmt.setInt(4, entry.getCategoryId());
            pstmt.setString(5, entry.getCategoryAspect() != null ? entry.getCategoryAspect().getCode() : null);

            pstmt.setString(6, entry.getLocationCode());
            pstmt.setString(7, entry.getItemType().getCode());
            pstmt.setInt(8, entry.getItemMinCount());

            pstmt.setInt(9, entry.getDisplayOrder());

            pstmt.setString(10, entry.getAutoFillType() != null ? entry.getAutoFillType().getCode() : null);
            pstmt.setString(11, entry.getAutoFillRule() != null ? entry.getAutoFillRule().toJson() : null);

            pstmt.setTimestamp(12, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(13, entry.getCreateUserid());

            pstmt.setTimestamp(14, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(15, entry.getUpdateUserid());

            pstmt.setString(16, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }
    public static GameRelation insertRelation(GameRelation gameRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSqlRelation());

            gameRelation.setRelationId(getSeqNo("SEQ_GAME_RELATION_ID", conn));

            //RELATIONID RESOURCEID,RELATIONTYPE,SORTNUM,RELATIONVALUE,VALIDSTATUS
            pstmt.setLong(1, gameRelation.getRelationId());
            pstmt.setLong(2, gameRelation.getResourceId());
            pstmt.setString(3, gameRelation.getGameRelationType().getCode());
            pstmt.setInt(4, gameRelation.getSortNum());
            pstmt.setString(5, gameRelation.getRelationValue());
            pstmt.setString(6, gameRelation.getValidStatus().getCode());
            pstmt.setString(7, gameRelation.getRelationName());

            return pstmt.execute() ? gameRelation : null;

        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    private static String getInsertSqlRelation() {
        String sqlScript = "INSERT INTO GAME_RESOURCE_RELATION (RELATIONID,RESOURCEID,RELATIONTYPE,SORTNUM,RELATIONVALUE,VALIDSTATUS,RELATIONNAME) VALUES (? ,? ,? ,? ,? ,? ,?)";

        return sqlScript;
    }
}
