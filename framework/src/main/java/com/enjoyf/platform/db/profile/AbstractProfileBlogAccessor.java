package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:31
 * Desc:
 */
class AbstractProfileBlogAccessor extends AbstractSequenceBaseTableAccessor<ProfileBlog> implements ProfileBlogAccessor {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private static final String KEY_TABLE_NAME_PREFIX = "PROFILE_BLOG";
    private static final int TABLE_NUM = 100;

    private static final String KEY_SEQ_USER_ID = "SEQ_PROFILEBLOG_USERID";


    @Override
    public ProfileBlog insert(ProfileBlog profileBlog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(profileBlog.getUno()));

            pstmt.setString(1, profileBlog.getUno());
            pstmt.setString(2, profileBlog.getScreenName());
            pstmt.setString(3, profileBlog.getDomain());
            pstmt.setString(4, profileBlog.getDescription());
            pstmt.setString(5, profileBlog.getTemplateId());
            pstmt.setString(6, profileBlog.getSelfSetStatus().getCode());
            pstmt.setString(7, profileBlog.getSelfSetData() == null ? "" : profileBlog.getSelfSetData().toJsonStr());
            pstmt.setTimestamp(8, profileBlog.getCreateDate() != null ? new Timestamp(profileBlog.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, profileBlog.getHeadIconSet() != null ? profileBlog.getHeadIconSet().toJsonStr() : null);
            pstmt.setTimestamp(10, profileBlog.getUpdateDate() != null ? new Timestamp(profileBlog.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(11, profileBlog.getAuditStatus() != null ? profileBlog.getAuditStatus().getValue() : 0);
            pstmt.setString(12, profileBlog.getActiveStatus() != null ? profileBlog.getActiveStatus().getCode() : null);
            pstmt.setTimestamp(13, profileBlog.getInactiveTillDate() != null ? new Timestamp(profileBlog.getInactiveTillDate().getTime()) : null);
            pstmt.setTimestamp(14, profileBlog.getAuditDate() != null ? new Timestamp(profileBlog.getAuditDate().getTime()) : null);
            pstmt.setString(15, profileBlog.getAuditUserId());
            pstmt.setString(16, profileBlog.getProfileDomain() != null ? profileBlog.getProfileDomain().getCode() : null);
            pstmt.setInt(17, profileBlog.getTableNo());

//            pstmt.setLong(18, getSeqNo(KEY_SEQ_USER_ID, conn));
            pstmt.setLong(18, 0);
            pstmt.setString(19, profileBlog.getPlayingGames());
            pstmt.setString(20, profileBlog.getBackgroundPic());
            if (!StringUtil.isEmpty(profileBlog.getPhoneNum())) {
                pstmt.setString(21, profileBlog.getPhoneNum());
            } else {
                pstmt.setNull(21, Types.VARCHAR);
            }
            if (profileBlog.getPhoneVerifyStatus() != null) {
                pstmt.setString(22, profileBlog.getPhoneVerifyStatus().getCode());
            } else {
                pstmt.setNull(22, Types.VARCHAR);
            }
            if (profileBlog.getPhoneBindDate() != null) {
                pstmt.setTimestamp(23, new Timestamp(profileBlog.getPhoneBindDate().getTime()));
            } else {
                pstmt.setNull(23, Types.TIMESTAMP);

            }

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ProfileBlog, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return profileBlog;
    }

    @Override
    public boolean update(ProfileBlog profileBlog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(profileBlog.getUno())
                + " SET SCREENNAME = ?, DOMAINNAME = ?, BLOGDESCRIPTION = ?, TEMPLATEID = ?, SELFSETSTATUS = ?, SELFSETDATA = ? , UPDATEDATE = ?, ACTIVESTATUS = ? WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog update : " + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, profileBlog.getScreenName());
            pstmt.setString(2, profileBlog.getDomain());
            pstmt.setString(3, profileBlog.getDescription());

            pstmt.setString(4, profileBlog.getTemplateId());
            pstmt.setString(5, profileBlog.getSelfSetStatus().getCode());
            pstmt.setString(6, profileBlog.getSelfSetData().toJsonStr());

            pstmt.setTimestamp(7, profileBlog.getUpdateDate() != null ? new Timestamp(profileBlog.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(8, profileBlog.getActiveStatus().getCode());

            pstmt.setString(9, profileBlog.getUno());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update ProfileBlog, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getUpdateScript(uno, keyValues));

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyValues);
            pstmt.setString(index, uno);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public ProfileBlog getByUno(String uno, Connection conn) throws DbException {
        ProfileBlog returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(uno) + " WHERE UNO = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog getByUno : " + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, uno);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select a ProfileBlog, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public ProfileBlog getByScreenName(String screenName, Connection conn) throws DbException {
        ProfileBlog returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName("") + " WHERE SCREENNAME = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog getByScreenName : " + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, screenName);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getByScreenName, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName, Connection conn) throws DbException {
        Map<String, ProfileBlog> map = new HashMap<String, ProfileBlog>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog XXXX Script:" + "SELECT * FROM " + getTableName("") + " WHERE SCREENNAME LIKE ?");
        }
        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName("") + " WHERE SCREENNAME LIKE ?");
            pstmt.setString(1, "%" + screenName + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                map.put(rsToObject(rs).getUno(), rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryProfileBlogsByScreenName, a SQLException occurred:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return map;
    }

    @Override
    public Map<String, ProfileBlog> queryByScreenNameMaps(Set<String> screenNames, Connection conn) throws DbException {
        Map<String, ProfileBlog> map = new HashMap<String, ProfileBlog>();
        if (screenNames.size() < 1) {
            return map;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + getTableName("") + " WHERE " + getScreenNamesQueryWhereCause(screenNames.size());

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog queryByScreenNameMaps : " + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);
            Iterator iterator = screenNames.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                pstmt.setString(i, (String) iterator.next());
                i++;
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                map.put(rsToObject(rs).getUno(), rsToObject(rs));
            }

        } catch (SQLException e) {
            GAlerter.lab("On getByScreenName, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return map;
    }

    @Override
    public ProfileBlog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME_PREFIX, queryExpress, conn);
    }

    @Override
    public ProfileBlog getByDomain(String domain, Connection conn) throws DbException {
        ProfileBlog returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + getTableName("") + " WHERE DOMAINNAME = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog getByDomain :" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, domain);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getByDomain, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<ProfileBlog> search(String keyWord, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<ProfileBlog> queryBlogByDateStep(Date startDate, Date endDate, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME_PREFIX, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ProfileBlog> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    //////////////////////////////////////////
    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX;// + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected ProfileBlog rsToObject(ResultSet rs) throws SQLException {
        ProfileBlog profileBlog = new ProfileBlog();

        profileBlog.setUno(rs.getString("UNO"));
        profileBlog.setScreenName(rs.getString("SCREENNAME"));
        profileBlog.setDomain(rs.getString("DOMAINNAME"));

        profileBlog.setDescription(rs.getString("BLOGDESCRIPTION"));

        profileBlog.setTemplateId(rs.getString("TEMPLATEID"));
        profileBlog.setSelfSetStatus(ActStatus.getByCode(rs.getString("SELFSETSTATUS")));
        profileBlog.setSelfSetData(BlogSelfSetData.parse(rs.getString("SELFSETDATA")));
        ProfileBlogHeadIconSet profileBlogHeadIconSet = ProfileBlogHeadIconSet.parse(rs.getString("MOREHEADICONS"));
        profileBlog.setHeadIconSet(profileBlogHeadIconSet);
        profileBlog.setHeadIcon(getHeadIcon(profileBlogHeadIconSet));

        //new columns
        profileBlog.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        profileBlog.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        profileBlog.setAuditStatus(ProfileAuditStatus.getByValue(rs.getInt("AUDITSTATUS")));
        profileBlog.setAuditDate(rs.getTimestamp("AUDITDATE") != null ? new Date(rs.getTimestamp("AUDITDATE").getTime()) : null);
        profileBlog.setAuditUserId(rs.getString("AUDITUSERID"));
        profileBlog.setActiveStatus(ProfileActiveStatus.getByCode(rs.getString("ACTIVESTATUS")));
        profileBlog.setInactiveTillDate(rs.getTimestamp("INACTIVETILLDATE") != null ? new Date(rs.getTimestamp("INACTIVETILLDATE").getTime()) : null);
        profileBlog.setProfileDomain(ProfileDomain.getByCode(rs.getString("PROFILEDOMAIN")));

        profileBlog.setPhoneNum(rs.getString("PHONENUM"));
        profileBlog.setPhoneVerifyStatus(ActStatus.getByCode(rs.getString("PHONEVERIFYSTATUS")));
        profileBlog.setUserid(rs.getLong("USERID"));
        profileBlog.setPhoneBindDate(rs.getTimestamp("PHONEBINDDATE"));

        profileBlog.setBackgroundPic(rs.getString("BACKGROUNDPIC"));
        profileBlog.setPlayingGames(rs.getString("PLAYINGGAMES"));

        return profileBlog;
    }


    protected String getUpdateScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapSetClause(keyValues));
        }

        returnValue.append(" WHERE UNO = ?");

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog getUpdateScript : " + returnValue.toString());
        }

        return returnValue.toString();
    }


    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (UNO, SCREENNAME, DOMAINNAME, BLOGDESCRIPTION, " +
                "TEMPLATEID, SELFSETSTATUS, SELFSETDATA, CREATEDATE, " +
                "MOREHEADICONS, UPDATEDATE, AUDITSTATUS, ACTIVESTATUS, " +
                "INACTIVETILLDATE, AUDITDATE, AUDITUSERID, PROFILEDOMAIN,TABLENO,USERID,PLAYINGGAMES,BACKGROUNDPIC,PHONENUM,PHONEVERIFYSTATUS,PHONEBINDDATE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileBlog INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getScreenNamesQueryWhereCause(Integer screenNamesSize) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < screenNamesSize; i++) {
            stringBuilder.append(" SCREENNAME = ?");

            if (i < (screenNamesSize - 1)) {
                stringBuilder.append(" OR");
            }
        }

        return stringBuilder.toString();
    }

    private String getHeadIcon(ProfileBlogHeadIconSet profileBlogHeadIconSet) throws SQLException {
        Set<ProfileBlogHeadIcon> set = profileBlogHeadIconSet.getIconSet();
        Iterator<ProfileBlogHeadIcon> it = set.iterator();
        while (it.hasNext()) {
            ProfileBlogHeadIcon profileBlogHeadIcon = it.next();
            if (profileBlogHeadIcon.getValidStatus()) {
                return profileBlogHeadIcon.getHeadIcon();
            }
        }
        return null;
    }
}
