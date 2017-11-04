package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:24
 * Desc:
 */
class AbstractProfileSettingAccessor implements ProfileSettingAccessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private static final String KEY_TABLE_NAME_PREFIX = "PROFILE_SETTING_";
    private static final int TABLE_NUM = 100;

    @Override
    public ProfileSetting insert(ProfileSetting profileSetting, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(profileSetting.getUno()));

            pstmt.setString(1, profileSetting.getUno());
            pstmt.setString(2, profileSetting.getAllowContentReplyType().getCode());
            pstmt.setString(3, profileSetting.getAllowLetterType().getCode());
            pstmt.setString(4, profileSetting.getLetterhead());
            pstmt.setString(5, profileSetting.getAllowSubmit().getCode());
            pstmt.setInt(6, profileSetting.getAllowsubmittype().getValue());
            pstmt.setString(7, profileSetting.getAllowtag());
            pstmt.setString(8, profileSetting.getHintmyfans().getCode());
            pstmt.setString(9, profileSetting.getHintmyfeedback().getCode());
            pstmt.setString(10, profileSetting.getHintmyletter().getCode());
            pstmt.setString(11, profileSetting.getHintmynotice().getCode());
            pstmt.setString(12, profileSetting.getHintatme().getCode());

//           pstmt.setString(13, profileSetting.getSyncSet() == null ? "" : profileSetting.getSyncSet().toJsonStr());
            pstmt.setString(13, profileSetting.getAtSet() == null ? "" : profileSetting.getAtSet().toJson());


            pstmt.setInt(14, profileSetting.getPagetitlenum() != null ? profileSetting.getPagetitlenum() : 0);

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert profileSetting, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return profileSetting;
    }

    @Override
    public boolean update(ProfileSetting profileSetting, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(profileSetting.getUno()) + " SET " +
                    "ALLOWCONTENTREPLYTYPE=?, ALLOWLETTERTYPE=?,LETTERHEAD=? ,ALLOWSUBMIT=?,ALLOWSUBMITTYPE=?,ALLOWTAG=?,HINTMYFANS=?,HINTMYFEEDBACK=?,HINTMYLETTER=?,HINTMYNOTICE=?,HINTATME=?,EXTSTR02=?,PAGETITLENUM=?, WHERE UNO = ?");


            pstmt.setString(1, profileSetting.getAllowContentReplyType().getCode());
            pstmt.setString(2, profileSetting.getAllowLetterType().getCode());
            pstmt.setString(3, profileSetting.getLetterhead());
            pstmt.setString(4, profileSetting.getAllowSubmit().getCode());
            pstmt.setInt(5, profileSetting.getAllowsubmittype().getValue());
            pstmt.setString(6, profileSetting.getAllowtag());
            pstmt.setString(7, profileSetting.getHintmyfans().getCode());
            pstmt.setString(8, profileSetting.getHintmyfeedback().getCode());
            pstmt.setString(9, profileSetting.getHintmyletter().getCode());
            pstmt.setString(10, profileSetting.getHintmynotice().getCode());
            pstmt.setString(11, profileSetting.getHintatme().getCode());

//            pstmt.setString(12, profileSetting.getSyncSet() == null ? "" : profileSetting.getSyncSet().toJsonStr());
            pstmt.setString(12, profileSetting.getAtSet() == null ? "" : profileSetting.getAtSet().toJson());

            pstmt.setInt(13, profileSetting.getPagetitlenum());
            pstmt.setString(14, profileSetting.getUno());
            //
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update profileSetting, a SQLException occured:", e);

            throw new DbException(e);
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
    public ProfileSetting getByUno(String uno, Connection conn) throws DbException {
        ProfileSetting returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(uno) + " WHERE UNO = ?");

            pstmt.setString(1, uno);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select an ProfileSetting, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected ProfileSetting rsToObject(ResultSet rs) throws SQLException {
        ProfileSetting profileSetting = new ProfileSetting();

        profileSetting.setAllowContentReplyType(AllowType.getByCode(rs.getString("ALLOWCONTENTREPLYTYPE")));
        profileSetting.setAllowLetterType(AllowType.getByCode(rs.getString("ALLOWLETTERTYPE")));
        profileSetting.setAllowSubmit(AllowType.getByCode(rs.getString("ALLOWSUBMIT")));
        profileSetting.setAllowsubmittype(ContentType.getByValue(rs.getInt("ALLOWSUBMITTYPE")));
        profileSetting.setAllowtag(rs.getString("ALLOWTAG"));
        profileSetting.setHintatme(ActStatus.getByCode(rs.getString("HINTATME")));
        profileSetting.setHintmyfans(ActStatus.getByCode(rs.getString("HINTMYFANS")));
        profileSetting.setHintmyfeedback(ActStatus.getByCode(rs.getString("HINTMYFEEDBACK")));
        profileSetting.setHintmyletter(ActStatus.getByCode(rs.getString("HINTMYLETTER")));
        profileSetting.setHintmynotice(ActStatus.getByCode(rs.getString("HINTMYNOTICE")));
        profileSetting.setLetterhead(rs.getString("LETTERHEAD"));

//        profileSetting.setSyncSet(ProfileSettingSyncSet.parse(rs.getString("EXTSTR01")));
        profileSetting.setAtSet(ProfileSettingAt.parse(rs.getString("EXTSTR02"))); // at 设置
        profileSetting.setAllowExpSchool(rs.getString("EXTSTR03") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR03"))); //experience--school
        profileSetting.setAllowExpComp(rs.getString("EXTSTR04") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR04"))); //experience--company

        profileSetting.setAllowAgreeSocial(rs.getString("EXTSTR05") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR05")));//社交端--赞
        profileSetting.setAllowReplySocial(rs.getString("EXTSTR06") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR06")));//社交端--评论
        profileSetting.setAllowFocusSocial(rs.getString("EXTSTR07") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR07")));//社交端--关注
        profileSetting.setAllowSoundSocial(rs.getString("EXTSTR08") == null ? AllowType.A_ALLOW : AllowType.getByCode(rs.getString("EXTSTR08"))); //社交端--提示音
        profileSetting.setPagetitlenum(rs.getInt("PAGETITLENUM"));
        profileSetting.setUno(rs.getString("UNO"));

        return profileSetting;
    }

    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (UNO,ALLOWCONTENTREPLYTYPE,ALLOWLETTERTYPE,LETTERHEAD,ALLOWSUBMIT,ALLOWSUBMITTYPE,ALLOWTAG,HINTMYFANS,HINTMYFEEDBACK,HINTMYLETTER,HINTMYNOTICE,HINTATME,EXTSTR02,PAGETITLENUM)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileSetting INSERT Script:" + insertSql);
        }

        return insertSql;
    }


    protected String getUpdateScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapSetClause(keyValues));
        }

        returnValue.append(" WHERE UNO = ?");

        return returnValue.toString();
    }
}

