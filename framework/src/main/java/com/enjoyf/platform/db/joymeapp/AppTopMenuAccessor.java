package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.JoymeAppMenu;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopMenu;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public interface AppTopMenuAccessor {
    /**
     * 插入接口
     *
     * @paramJoymeAppTopMenu
     * @return
     * @throws com.enjoyf.platform.db.DbException
     */
    public JoymeAppTopMenu insert(JoymeAppTopMenu joymeAppTopMenu, Connection conn) throws DbException;

    public JoymeAppTopMenu get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<JoymeAppTopMenu> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress,UpdateExpress updateExpress, Connection conn) throws DbException;


    public List<JoymeAppTopMenu> query(QueryExpress queryExpress, Pagination pagination, Connection conn)throws DbException;
}
