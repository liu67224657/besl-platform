package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppStatInstallLog;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-7-24 下午5:34
 * Description:
 */
public interface AppStatInstallLogAccessor {

    AppStatInstallLog insert(AppStatInstallLog appStatInstallLog, Connection conn) throws DbException;

    List<AppStatInstallLog> query(long seqId, QueryExpress queryExpress, Connection conn) throws DbException;

    int update(long seqId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    int delete(long seqId, QueryExpress queryExpress, Connection conn) throws DbException;

    AppStatInstallLog get(long seqId, QueryExpress queryExpress, Connection conn) throws DbException;


}
