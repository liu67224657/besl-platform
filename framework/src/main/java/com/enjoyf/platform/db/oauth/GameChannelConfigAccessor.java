package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.GameChannelConfig;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by ericliu on 15/12/29.
 */
public interface GameChannelConfigAccessor {

    public GameChannelConfig insert(GameChannelConfig config, Connection conn) throws DbException;

    public GameChannelConfig get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GameChannelConfig> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
