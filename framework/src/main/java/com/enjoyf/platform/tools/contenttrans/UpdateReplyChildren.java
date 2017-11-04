package com.enjoyf.platform.tools.contenttrans;


import com.enjoyf.platform.db.*;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateReplyChildren {

     private static final Logger logger = LoggerFactory.getLogger(UpdateReplyChildren.class);

    public static void main(String[] args) {
        long start=System.currentTimeMillis();

        try {
            initConnection("writeable");

            for (int i = 0; i <= 99; i++) {
                logger.debug("start cal contentids table.no: " + i);

                //得到每张表的content，关链接
                Set<String> contentIds = null;
                Connection conn = null;
                try {
                    conn = DbConnFactory.factory("writeable");
                    contentIds = getContentIdByTableNum(conn, i);
                } finally {
                    DataBaseUtil.closeConnection(conn);
                }


               logger.debug("start update floorNum.table.no: " + i);
                //找到该表中每篇文章的回复
                for (String contentId : contentIds) {
                    System.out.println(contentId);

                    List<ContentInteraction> contentInteractionList = new ArrayList<ContentInteraction>();

                    //得到该文章下的所有评论
                    Connection queryInteractionConn = null;
                    try {
                        queryInteractionConn = DbConnFactory.factory("writeable");
                        contentInteractionList = queryReplyByContentId(queryInteractionConn, contentId);
                    } finally {
                        DataBaseUtil.closeConnection(queryInteractionConn);
                    }

                    //计算楼号
                    int floorNum = 1;
                    for (ContentInteraction connInteraction : contentInteractionList) {
                        connInteraction.setFloorNo(floorNum);
                        floorNum = floorNum + 1;
                    }

                    Connection batchUpdateConn = null;
                    try {
                        batchUpdateConn = DbConnFactory.factory("writeable");

                        batchUpdateConn.setAutoCommit(false);

                        batchUpdateContentInteraction(batchUpdateConn, contentInteractionList, contentId);

                        batchUpdateConn.commit();
                    } catch (SQLException e) {
                          logger.debug("e:",e);
                    } finally {
                        DataBaseUtil.closeConnection(batchUpdateConn);
                    }
                }
                 logger.debug("start update finish.table.no:" +i);
            }

            logger.debug("all finished spend: "+((System.currentTimeMillis()-start)/1000)+" sec.");
        } catch (DbException e) {
           logger.debug("e:",e);
        }

    }

    private static void initConnection(String dsName) throws DataSourceException, DbTypeException {
        FiveProps servProps = Props.instance().getServProps();
        DataSourceManager.get().append(dsName, servProps);

        logger.debug("init connection finish.....");
    }

    private static Set<String> getContentIdByTableNum(Connection connection, int tableNum) throws DbException {
        Set<String> set = new HashSet<String>();

        String sql = "SELECT DISTINCT(CONTENTID) FROM CONTENT_INTERACTION_" + Strings.repeat("0", 2 - String.valueOf(tableNum).length()) + tableNum + " WHERE REMOVESTATUS=?";

        System.out.println(sql);

        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                set.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("get content by tableno error. tableNum:" + tableNum + " ,e");
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return set;
    }

    private static List<ContentInteraction> queryReplyByContentId(Connection connection, String contentId) throws DbException {
        List<ContentInteraction> returnList = new ArrayList<ContentInteraction>();

        String sql = "SELECT * FROM CONTENT_INTERACTION_" + TableUtil.getTableNumSuffix(contentId.hashCode(), 100) + " WHERE CONTENTID=? AND REMOVESTATUS=? AND INTERACTIONTYPE=? ORDER BY CREATEDATE ASC";

        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, contentId);
            pstmt.setString(2, ActStatus.UNACT.getCode());
            pstmt.setString(3, InteractionType.REPLY.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnList.add(rsToObject(rs));
            }

        } catch (SQLException e) {
            System.out.println("queryReplyByContentId error. tableNum:" + contentId + " ,e");
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnList;
    }

    private static int[] batchUpdateContentInteraction(Connection connection, List<ContentInteraction> interactionList, String contentId) throws DbException {

        String sql = "UPDATE CONTENT_INTERACTION_" + TableUtil.getTableNumSuffix(contentId.hashCode(), 100) + " SET FLOORNUM=? WHERE INTERACTIONID=? ";

        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql);
            for (ContentInteraction interaction : interactionList) {
                pstmt.setLong(1, interaction.getFloorNo());
                pstmt.setString(2, interaction.getInteractionId());
                pstmt.addBatch();
            }

            return pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println("queryReplyByContentId error. tableNum:" + contentId + " ,e");
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

    }

    private static ContentInteraction rsToObject(ResultSet rs) throws SQLException {
        ContentInteraction entity = new ContentInteraction();

        //INTERACTIONID,CONTENTID,CONTENTUNO,PARENTID, PARENTUNO,
        //INTERACTIONCONTENT, INTERACTIONUNO, REMOVESTATUS, CREATEDATE  CREATEIP
        entity.setInteractionId(rs.getString("INTERACTIONID"));

        entity.setContentId(rs.getString("CONTENTID"));
        entity.setContentUno(rs.getString("CONTENTUNO"));

        entity.setParentId(rs.getString("PARENTID"));
        entity.setParentUno(rs.getString("PARENTUNO"));

        entity.setRootId(rs.getString("ROOTID"));
        entity.setRootUno(rs.getString("ROOTUNO"));

        entity.setReplyTimes(rs.getInt("REPLYTIMES"));
        entity.setFloorNo(rs.getInt("FLOORNUM"));

        entity.setInteractionContent(rs.getString("INTERACTIONCONTENT"));
        entity.setInteractionContentType(InteractionContentType.getByValue(rs.getInt("INTERACTIONCONTENTTYPE")));
        entity.setInteractionImages(ImageContentSet.parse(rs.getString("INTERACTIONIMAGES")));

        entity.setInteractionUno(rs.getString("INTERACTIONUNO"));

        entity.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));

        entity.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entity.setCreateIp(rs.getString("CREATEIP"));

        entity.setAuditStatus(ContentReplyAuditStatus.getByValue(rs.getInt("AUDITSTATUS")));

        entity.setInteractionType(InteractionType.getByCode(rs.getString("INTERACTIONTYPE")));
        entity.setIntetractionDisplayType(IntetractionDisplayType.getByValue(rs.getInt("DISPLAYTYPE")));
        return entity;
    }
}
