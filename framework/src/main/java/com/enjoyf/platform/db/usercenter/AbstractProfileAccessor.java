package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.Icon;
import com.enjoyf.platform.service.usercenter.Icons;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.profile.ProfileDomainGenerator;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public abstract class AbstractProfileAccessor extends AbstractBaseTableAccessor<Profile> implements ProfileAccessor {

    private static final String KEY_TABLE_NAME = "profile";

    private static Logger logger = LoggerFactory.getLogger(AbstractProfileAccessor.class);

    @Override
    public Profile insert(Profile profile, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            if (StringUtil.isEmpty(profile.getDomain())) {
                profile.setDomain(profile.getProfileId());
            }

            if (StringUtil.isEmpty(profile.getNick())) {
                profile.setNick("joyme" + ProfileDomainGenerator.generateProfileDomain(System.currentTimeMillis(), ProfileDomainGenerator.KEY_DOMAIN_LEN));
            }

            //profile_id,uid,nick,description,icon,createtime,createip,uno,appkey,flag,signature,level,experience,backpic,hobby
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, profile.getProfileId());
            pstmt.setString(2, profile.getNick());
            pstmt.setString(3, profile.getDescription());
            pstmt.setString(4, profile.getIcon());
            pstmt.setTimestamp(5, new Timestamp(profile.getCreateTime() == null ? System.currentTimeMillis() : profile.getCreateTime().getTime()));
            pstmt.setString(6, profile.getCreateIp());
            pstmt.setString(7, profile.getUno());
            pstmt.setString(8, profile.getProfileKey());
            pstmt.setInt(9, profile.getFlag().getValue());
            pstmt.setString(10, profile.getDomain());
            pstmt.setString(11, profile.getIcons() != null ? profile.getIcons().toJsonStr() : null);
            pstmt.setString(12, profile.getNick().toLowerCase());
            pstmt.setString(13, profile.getSignature());
            pstmt.setInt(14, profile.getLevel());
            pstmt.setInt(15, profile.getExperience());
            pstmt.setString(16, profile.getBackPic());
            pstmt.setString(17, profile.getHobby());
            pstmt.setString(18, profile.getAppkey());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                profile.setUid(rs.getLong(1));
            }

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return profile;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,nick,description,icon,createtime," +
                "createip,uno,profilekey,flag,domain,icons,checknick," +
                "signature,level,experience,backpic,hobby,appkey) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("profile insert sql" + sql);
        }
        return sql;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public Profile get(QueryExpress queryExpess, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpess, conn);
    }

    @Override
    public List<Profile> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Profile> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected Profile rsToObject(ResultSet rs) throws SQLException {

        Profile profile = new Profile();

        profile.setProfileId(rs.getString("profile_id"));
        profile.setUid(rs.getLong("uid"));
        profile.setNick(rs.getString("nick"));
        profile.setDescription(rs.getString("description"));
        profile.setIcon(URLUtils.getJoymeDnUrl(rs.getString("icon")));
        profile.setCreateTime(rs.getTimestamp("createtime"));
        profile.setCreateIp(rs.getString("createip"));
        profile.setProfileKey(rs.getString("profilekey"));
        profile.setFlag(new ProfileFlag(rs.getInt("flag")));
        profile.setUno(rs.getString("uno"));
        profile.setDomain(rs.getString("domain"));
        profile.setCheckNick(rs.getString("checknick"));

        profile.setDescription(rs.getString("description"));
        profile.setSex(rs.getString("sex"));
        profile.setProvinceId(rs.getInt("provinceid"));
        profile.setCityId(rs.getInt("cityid"));
        profile.setMobile(rs.getString("mobile"));

        Icons icons = Icons.parse(rs.getString("icons"));
        List<Icon> list = icons.getIconList();
        for (int i = 0; i < list.size(); i++) {
            Icon icon = list.get(i);
            icon.setIcon(URLUtils.getJoymeDnUrl(icon.getIcon()));
            list.set(i, icon);
        }
        profile.setIcons(icons);
        //signature,level,experience,backpic,hobby
        profile.setSignature(rs.getString("signature"));
        profile.setLevel(rs.getInt("level"));
        profile.setExperience(rs.getInt("experience"));
        profile.setBackPic(rs.getString("backpic"));
        profile.setHobby(rs.getString("hobby"));
        profile.setBirthday(rs.getString("birthday"));
        profile.setRealName(rs.getString("realname"));
        profile.setAppkey(rs.getString("appkey"));
        return profile;
    }

}
