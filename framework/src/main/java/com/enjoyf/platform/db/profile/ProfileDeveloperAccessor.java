package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileDeveloper;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:13
 * Desc:
 */
interface ProfileDeveloperAccessor {

    public ProfileDeveloper insert(ProfileDeveloper profileDeveloper, Connection conn) throws DbException;

    public ProfileDeveloper get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ProfileDeveloper> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ProfileDeveloper> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
