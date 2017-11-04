package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.SocialNotice;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-6
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public interface SocialNoticeAccessor {

    public SocialNotice insert(SocialNotice socialNotice, Connection conn) throws DbException;

    public SocialNotice get(String ownUno, Connection conn) throws DbException;

    public int update(String ownUno, UpdateExpress updateExpress, Connection conn) throws DbException;
}
