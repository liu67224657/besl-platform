package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.UserAccount;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/28
 * Description:
 */
public interface UserAccountAccessor {
    public UserAccount insert(UserAccount account, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;


    public UserAccount get(QueryExpress queryExpess, Connection conn) throws DbException;


}
