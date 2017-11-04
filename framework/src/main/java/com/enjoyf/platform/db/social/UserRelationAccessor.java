package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface UserRelationAccessor {

    public UserRelation insert(UserRelation userRelation, Connection conn) throws DbException;

    public UserRelation get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserRelation> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public UserRelation get(String srcUno, String destUno, ObjectRelationType type, Connection conn) throws DbException;

    List<UserRelation> queryByDestProfileId(String srcUno, ObjectRelationType type, Pagination page, Connection conn) throws DbException;

    List<UserRelation> queryBySrcProfileId(String srcUno, ObjectRelationType type, Pagination page, Connection conn) throws DbException;
}
