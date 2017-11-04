package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.service.profile.ProfileMobileDevice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileMobileDeviceAccessor {
    //insert
    public ProfileMobileDevice insert(ProfileMobileDevice entry, Connection conn) throws DbException;

    //get by id
    public ProfileMobileDevice get(QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
