package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.JoymeAppMenuTag;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-3
 * Time: 下午10:50
 * To change this template use File | Settings | File Templates.
 */
public interface JoymeAppMenuTagAccessor {

    public JoymeAppMenuTag insert(JoymeAppMenuTag tag, Connection conn) throws DbException;

    public JoymeAppMenuTag get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<JoymeAppMenuTag> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
