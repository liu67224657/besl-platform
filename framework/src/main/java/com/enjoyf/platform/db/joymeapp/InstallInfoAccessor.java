package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppInstallInfo;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public interface InstallInfoAccessor {
    AppInstallInfo insert(AppInstallInfo installInfo, Connection conn)throws DbException;
}
