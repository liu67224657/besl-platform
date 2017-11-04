package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.IngoreApp;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactIngoreAppAccessor extends AbstractBaseTableAccessor<IngoreApp> implements IngoreAppAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstactIngoreAppAccessor.class);


    protected static final String KEY_TABLE_NAME = "app_ingore";

    @Override
    public IngoreApp insert(IngoreApp ingoreApp, Connection conn) throws DbException {
        //todo not use
        return null;
    }

    @Override
    public List<IngoreApp> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected IngoreApp rsToObject(ResultSet rs) throws SQLException {
        IngoreApp returnObj = new IngoreApp();

        returnObj.setIngore_app_id(rs.getInt("ingore_app_id"));
        returnObj.setApp_name(rs.getString("app_name"));
        returnObj.setPkg_name(rs.getString("package_name"));
        returnObj.setPlatform(rs.getInt("platform"));

        return returnObj;
    }
}
