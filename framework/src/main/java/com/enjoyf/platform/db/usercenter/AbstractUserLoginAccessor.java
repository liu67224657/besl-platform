package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public abstract class AbstractUserLoginAccessor extends AbstractBaseTableAccessor<UserLogin> implements UserLoginAccessor {

    private static final String KEY_TABLE_NAME = "user_login";

    private static Logger logger = LoggerFactory.getLogger(AbstractUserLoginAccessor.class);

    @Override
    public UserLogin insert(UserLogin userLogin, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            long createTimestamp = userLogin.getCreateTime() == null ? System.currentTimeMillis() : userLogin.getCreateTime().getTime();
            //login_id,login_key,login_password,login_name,login_domain,uno,createtime,createip
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, userLogin.getLoginId());
            pstmt.setString(2, userLogin.getLoginKey());
            pstmt.setString(3, userLogin.getLoginPassword());
            pstmt.setString(4, userLogin.getLoginName());
            pstmt.setString(5, userLogin.getLoginDomain().getCode());
            pstmt.setString(6, userLogin.getUno());
            pstmt.setTimestamp(7, new Timestamp(createTimestamp));
            pstmt.setString(8, userLogin.getCreateIp());
            pstmt.setString(9, userLogin.getPasswdTime());

            pstmt.setString(10, userLogin.getTokenInfo() == null ? "" : userLogin.getTokenInfo().toJsonStr());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert userlogin, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return userLogin;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(login_id,login_key,login_password,login_name,login_domain,uno,createtime,createip,passwdtime,token_info) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("userLogin insert sql" + sql);
        }
        return sql;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public UserLogin get(QueryExpress queryExpess, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpess, conn);
    }

    @Override
    public List<UserLogin> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserLogin> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected UserLogin rsToObject(ResultSet rs) throws SQLException {

        UserLogin userLogin = new UserLogin();

        userLogin.setUno(rs.getString("uno"));
        userLogin.setLoginKey(rs.getString("login_key"));
        userLogin.setLoginId(rs.getString("login_id"));
        userLogin.setLoginDomain(LoginDomain.getByCode(rs.getString("login_domain")));
        userLogin.setCreateIp(rs.getString("createip"));
        userLogin.setCreateTime(rs.getTimestamp("createtime"));
        userLogin.setLoginId(rs.getString("login_id"));
        userLogin.setLoginName(rs.getString("login_name"));
        userLogin.setLoginPassword(rs.getString("login_password"));
        userLogin.setPasswdTime(rs.getString("passwdtime"));
        userLogin.setTokenInfo(TokenInfo.getByJsonStr(rs.getString("token_info")));
        userLogin.setAuthCode(rs.getString("auth_code"));
        userLogin.setAuthTime(rs.getTimestamp("auth_time"));

        return userLogin;
    }

}
