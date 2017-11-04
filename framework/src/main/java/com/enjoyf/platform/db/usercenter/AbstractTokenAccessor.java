package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public abstract class AbstractTokenAccessor extends AbstractBaseTableAccessor<Token> implements TokenAccessor {

    private static final String KEY_TABLE_NAME = "token";

    private static Logger logger = LoggerFactory.getLogger(AbstractTokenAccessor.class);

    @Override
    public Token insert(Token token, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            //token,token_type,appkey,token_expires,token_uno,token_uid,profileid,createtime,createip
            pstmt = conn.prepareStatement(getInsertSql());
            token.setToken(UUID.randomUUID().toString());
            pstmt.setString(1, token.getToken());
            pstmt.setInt(2, token.getTokenType().getCode());
            pstmt.setString(3, token.getProfileKey());
            pstmt.setInt(4, token.getTokenExpires());
            pstmt.setString(5, token.getUno());
            pstmt.setLong(6, token.getUid());
            pstmt.setString(7, token.getProfileId());
            pstmt.setTimestamp(8, new Timestamp(token.getCreateTime() == null ? System.currentTimeMillis() : token.getCreateTime().getTime()));
            pstmt.setString(9, token.getCreateIp());
            pstmt.setString(10, token.getRequest_parameter());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert token, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return token;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(token,token_type,profilekey,token_expires,token_uno,token_uid,profileid,createtime,createip,request_parameter) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("token insert sql" + sql);
        }
        return sql;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public Token get(QueryExpress queryExpess, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpess, conn);
    }

    @Override
    public int delete(QueryExpress queryExpess, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpess, conn);
    }

    @Override
    protected Token rsToObject(ResultSet rs) throws SQLException {

        Token token = new Token();
        token.setToken(rs.getString("token"));
        token.setTokenType(TokenType.getByCode(rs.getInt("token_type")));
        token.setProfileKey(rs.getString("profilekey"));
        token.setTokenExpires(rs.getInt("token_expires"));
        token.setUno(rs.getString("token_uno"));
        token.setUid(rs.getLong("token_uid"));
        token.setProfileId(rs.getString("profileid"));
        token.setCreateTime(rs.getTimestamp("createtime"));
        token.setCreateIp(rs.getString("createip"));
        token.setRequest_parameter(rs.getString("request_parameter"));
        return token;
    }
}
