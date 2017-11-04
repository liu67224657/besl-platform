package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/28
 * Description:
 */
public abstract class AbstractUserAccountAccessor extends AbstractBaseTableAccessor<UserAccount> implements UserAccountAccessor   {

    private static final String KEY_TABLE_NAME="user_account";

    private static Logger logger= LoggerFactory.getLogger(AbstractUserAccountAccessor.class);


    public UserAccount insert(UserAccount account, Connection conn) throws DbException{
        PreparedStatement pstmt = null;
        try {

            //uno,address,createtime,createip
            pstmt=conn.prepareStatement(getInsertSql());
            pstmt.setString(1, account.getUno());
            pstmt.setString(2, account.getAddress() == null ? "" : account.getAddress().toJsonStr());
            pstmt.setTimestamp(3, new Timestamp(account.getCreateTime() == null ? System.currentTimeMillis() : account.getCreateTime().getTime()));
            pstmt.setString(4, account.getCreateIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert useraccount, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return account;
    }

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException{
       return super.update(KEY_TABLE_NAME,updateExpress,queryExpress,conn);
    }


    public UserAccount get(QueryExpress queryExpess, Connection conn) throws DbException{
        return super.get(KEY_TABLE_NAME, queryExpess, conn);
    }



    private String getInsertSql() {
        String sql = "INSERT INTO "+KEY_TABLE_NAME+"(uno,address,createtime,createip) VALUES(?,?,?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("userAccount insert sql"+sql);
        }
        return sql;
    }


    @Override
    protected UserAccount rsToObject(ResultSet rs) throws SQLException {
        UserAccount userAccount=new UserAccount();
        userAccount.setUno(rs.getString("uno"));
        userAccount.setAddress(Address.parse(rs.getString("address")));
        userAccount.setCreateTime(rs.getTimestamp("createtime"));
        userAccount.setCreateIp(rs.getString("createip"));
//        userAccount.setMobile(rs.getString("mobile"));
        userAccount.setAccountFlag(new AccountFlag(rs.getInt("flag")));
        return userAccount;
    }
}
