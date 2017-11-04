package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.PrivacyType;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-13
 * Description:
 */
public class ImportContentSSImage {

    public static void main(String[] args) throws DataSourceException, DbTypeException {
        FiveProps props = Props.instance().getServProps();

        DataSourceManager.get().append("writeable", props);

        int total = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(2011, 9, 1, 0, 0, 0);
        Date to = cal.getTime();


        Connection conn = null;
        try {
            conn = DbConnFactory.factory("writeable");
            total = getTotalRows(conn, to);

            System.out.println(total);
        } catch (DbException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        int result=0;
        Connection contentConn = null;
        Pagination pagination = new Pagination(total, 1, 1000);
        try {
            do {
                contentConn = DbConnFactory.factory("writeable");
                List<Content> contentList = queryContent(contentConn, to, pagination);

                for (Content content : contentList) {
                    Map<String, Map<ObjectField, Object>> map = covert(content);
                    if (!CollectionUtil.isEmpty(map)) {
                        result+=updateContent(map, contentConn);
                    }
                }

                pagination.setCurPage(pagination.getCurPage() + 1);
            } while (pagination.hasNextPage());
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            DataBaseUtil.closeConnection(contentConn);
        }

        System.out.println("result------------->"+result);
    }


    private static int getTotalRows(Connection conn, Date to) throws SQLException {
        int size = 0;

        String sql = "SELECT COUNT(*) FROM CONTENT WHERE PUBLISHDATE<=? AND PUBLISHTYPE = ? AND CONTENTTYPE&? > 0 AND  REMOVESTATUS = ?";

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, new Timestamp(to.getTime()));
            pstmt.setString(2, ContentPublishType.ORIGINAL.getCode());
            pstmt.setInt(3, ContentType.IMAGE);
            pstmt.setString(4, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }


        return size;
    }

    private static List<Content> queryContent(Connection conn, Date to, Pagination pageination) throws SQLException {
        List<Content> contentList = new ArrayList<Content>();

        String sql = "SELECT * FROM CONTENT WHERE PUBLISHDATE<=? AND PUBLISHTYPE = ? AND CONTENTTYPE&? > 0 AND  REMOVESTATUS = ? LIMIT ?, ?";

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, new Timestamp(to.getTime()));
            pstmt.setString(2, ContentPublishType.ORIGINAL.getCode());
            pstmt.setInt(3, ContentType.IMAGE);
            pstmt.setString(4, ActStatus.UNACT.getCode());
            pstmt.setInt(5, pageination.getStartRowIdx());
            pstmt.setInt(6, pageination.getEndRowIdx());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                contentList.add(rsToObject(rs));
            }
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return contentList;
    }

    private static int updateContent(Map<String, Map<ObjectField, Object>> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;


        try {

            for (Map.Entry<String, Map<ObjectField, Object>> entry : map.entrySet()) {
                String sql = "UPDATE CONTENT SET " + ObjectFieldUtil.generateMapSetClause(entry.getValue()) + " WHERE CONTENTID = ?";
                pstmt = conn.prepareStatement(sql);

                int index = ObjectFieldUtil.setStmtParams(pstmt, 1, entry.getValue());
                pstmt.setString(index, entry.getKey());

                pstmt.addBatch();
            }


            return pstmt.executeBatch().length;
        } catch (SQLException e) {
            GAlerter.lab("On updateContent a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    private static Map<String, Map<ObjectField, Object>> covert(Content ct) {
        Map<String, Map<ObjectField, Object>> returnObj = new HashMap<String, Map<ObjectField, Object>>();

        Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();

        boolean needUpdate = false;
        Set<ImageContent> imageContentSet = new HashSet<ImageContent>();
        for (ImageContent contentImage : ct.getImages().getImages()) {
            String ss = contentImage.getSs();
            if (StringUtil.isEmpty(ss)) {
                needUpdate = true;
                String orgUrl = contentImage.getUrl();
                if(!orgUrl.endsWith(".jpg")||orgUrl.endsWith(".png")){
                    continue;
                }
                String exname = orgUrl.substring(orgUrl.lastIndexOf("."));
                String sNametou = orgUrl.substring(0, orgUrl.lastIndexOf("."));
                ss = sNametou + "_SS" + exname;
                contentImage.setSs(ss);
                System.out.println("cid------> " + ct.getContentId() + " ss-----> " + ss);
            }
            imageContentSet.add(contentImage);
        }

        if (needUpdate) {
            keyValues.put(ContentField.IMAGES, ct.getImages().toJsonStr());
            returnObj.put(ct.getContentId(), keyValues);
        }

        return returnObj;
    }

    private static Content rsToObject(ResultSet rs) throws SQLException {
        Content entity = new Content();

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

        entity.setLastReplyId(rs.getString("EXTSTR01"));
//        entity.setResourceId(rs.getString("EXTSTR02"));

        entity.setUpdateDate(rs.getTimestamp("UPDATEDATE"));

        entity.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        entity.setAuditStatus(ContentAuditStatus.getByValue(rs.getInt("AUDITSTATUS")));

        return entity;
    }
}
