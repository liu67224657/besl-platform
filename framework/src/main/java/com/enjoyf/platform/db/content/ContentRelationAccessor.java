package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ContentRelation;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.hibernate.sql.Update;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public interface ContentRelationAccessor {
    public ContentRelation insert(ContentRelation contentRelation, Connection connection) throws DbException;

    public ContentRelation get(QueryExpress getExpress, Connection connection) throws DbException;

    public List<ContentRelation> query(QueryExpress queryExpress, Connection connection) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;
}
