package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.SocialInviteInfo;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-20
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public interface SocialInviteInfoAccessor {

    public SocialInviteInfo insert(SocialInviteInfo socialInviteInfo, Connection conn) throws DbException;

    public SocialInviteInfo get(QueryExpress queryExpress, Connection conn) throws DbException;
}
