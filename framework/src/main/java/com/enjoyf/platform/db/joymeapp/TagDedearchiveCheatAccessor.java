package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhimingli
 * Date: 2015/01/08
 * Time: 9:59
 */
public interface TagDedearchiveCheatAccessor {
    public TagDedearchiveCheat insert(TagDedearchiveCheat tagDedearchiveCheat, Connection connection) throws DbException;

    public TagDedearchiveCheat get(QueryExpress queryExpress, Connection conn)throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

    public List<TagDedearchiveCheat> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TagDedearchiveCheat> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
