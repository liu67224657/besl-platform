package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.SocialInviteDetail;
import com.enjoyf.platform.service.social.SocialInviteInfo;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-20
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public interface SocialInviteDetailAccessor {

    public SocialInviteDetail insert(SocialInviteDetail inviteDetail, Connection conn) throws DbException;

    public SocialInviteDetail get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialInviteDetail> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<SocialInviteDetail> query(QueryExpress queryExpress, Connection conn) throws DbException;

}
