package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.IngoreApp;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public interface IngoreAppAccessor {

    public List<IngoreApp> query(QueryExpress queryExpress,Connection conn) throws DbException;

}
