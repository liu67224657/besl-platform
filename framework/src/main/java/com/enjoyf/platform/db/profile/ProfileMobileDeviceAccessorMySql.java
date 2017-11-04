package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileMobileDevice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class ProfileMobileDeviceAccessorMySql extends AbstractProfileMobileDeviceAccessor {
    @Override
    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, range, conn);
    }
}
