package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.GameChannelInfo;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by ericliu on 15/12/29.
 */
public interface GameChannelInfoAccessor {

    public GameChannelInfo insert(GameChannelInfo channelInfo, Connection conn) throws DbException;

    public GameChannelInfo get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GameChannelInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
