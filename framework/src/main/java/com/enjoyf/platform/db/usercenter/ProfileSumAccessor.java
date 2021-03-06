package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/11
 * Description:
 */
public interface ProfileSumAccessor {

    ProfileSum insert(ProfileSum sum, Connection conn) throws DbException;

    ProfileSum get(QueryExpress queryExpress, Connection conn) throws DbException;

    List<ProfileSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    List<ProfileSum> query(QueryExpress queryExpress, Connection conn) throws DbException;


    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
