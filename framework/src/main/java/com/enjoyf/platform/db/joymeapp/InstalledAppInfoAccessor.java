package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.InstalledAppInfo;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public interface InstalledAppInfoAccessor {

    public InstalledAppInfo insert(InstalledAppInfo entry, Connection conn) throws DbException;


    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
