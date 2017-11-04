package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.serv.joymeapp.LastMsgEntry;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;


public interface PushMessageAccessor {
    public PushMessage insert(PushMessage pushMessage, Connection conn) throws DbException;

    public PushMessage get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<PushMessage> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

     public List<PushMessage> query(QueryExpress queryExpress,Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Long> queryLastastMsgIdByPlatform(AppPlatform appPlatform, Connection conn)throws DbException;

    public Long getLastestPushMessageByAppKeyPlatform(String appKey, AppPlatform appPlatform, String version, Connection conn)throws DbException;

    public boolean remove(Long msgId, Connection conn) throws DbException;
}
