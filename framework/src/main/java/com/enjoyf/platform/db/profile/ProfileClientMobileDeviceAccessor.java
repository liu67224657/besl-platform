package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileClientMobileDevice;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-14
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileClientMobileDeviceAccessor {

    public ProfileClientMobileDevice insert(ProfileClientMobileDevice mobileDevice, Connection conn) throws DbException;

    public ProfileClientMobileDevice get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ProfileClientMobileDevice> query(QueryExpress queryExpress, Pagination pagination,Connection conn) throws DbException;

    public List<ProfileClientMobileDevice> query(QueryExpress queryExpress,Connection conn) throws DbException;

    public int modify(UpdateExpress updateExpress, QueryExpress queryExpress,Connection conn) throws DbException;
}
