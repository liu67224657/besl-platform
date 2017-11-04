/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileDetail;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface ProfileDetailAccessor {
    //
    public ProfileDetail insert(ProfileDetail profileDetail, Connection conn) throws DbException;

    //
    public boolean update(ProfileDetail profileDetail, Connection conn) throws DbException;

    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

    public boolean update(UpdateExpress updateExpress, String uno, Connection conn) throws DbException;

    //
    public ProfileDetail get(String uno, Connection conn) throws DbException;

    public List<ProfileDetail> queryList(String uno, QueryExpress queryExpress, Connection conn) throws DbException;
}
