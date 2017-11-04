package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileSetting;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:15
 * Desc:
 */
interface ProfileSettingAccessor {

    public ProfileSetting insert(ProfileSetting profileSetting, Connection conn) throws DbException;

    public boolean update(ProfileSetting profileSetting, Connection conn) throws DbException;

    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

    public ProfileSetting getByUno(String uno, Connection conn) throws DbException;
}
