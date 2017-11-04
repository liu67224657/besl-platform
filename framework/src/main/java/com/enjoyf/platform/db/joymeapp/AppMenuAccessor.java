package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public interface AppMenuAccessor {
    /**
     * 插入接口
     *
     * @param joymeAppMenu
     * @return
     * @throws DbException
     */
    public JoymeAppMenu insert(JoymeAppMenu joymeAppMenu, Connection conn) throws DbException;

    public JoymeAppMenu get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<JoymeAppMenu> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;


    public List<JoymeAppMenu> query(QueryExpress queryExpress,Pagination pagination,Connection conn)throws DbException;
}
