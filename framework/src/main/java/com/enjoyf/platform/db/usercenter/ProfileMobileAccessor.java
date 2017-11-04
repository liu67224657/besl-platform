package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.ProfileMobile;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/23
 * Description:
 */
public interface ProfileMobileAccessor {

    ProfileMobile insert(ProfileMobile mobile, Connection conn) throws DbException;

    ProfileMobile get(QueryExpress queryExpress, Connection conn) throws DbException;

    List<ProfileMobile> query(QueryExpress queryExpress, Connection conn) throws DbException;

    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
