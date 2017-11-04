package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.ProfileRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public interface ProfileRelationAccessor {

    ProfileRelation insert(ProfileRelation relation, Connection conn) throws DbException;

    ProfileRelation get(QueryExpress queryExpress, Connection conn) throws DbException;

    List<ProfileRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
