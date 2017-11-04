package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppInfo;
import com.enjoyf.platform.service.joymeapp.MobileGameArticle;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-24
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public interface MobileGameArticleAccessor {

    public MobileGameArticle insert(MobileGameArticle appInfo, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<MobileGameArticle> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<MobileGameArticle> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public MobileGameArticle get(QueryExpress queryExpress, Connection conn) throws DbException;
}
