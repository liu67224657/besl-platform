package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppPageViewEntry;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
public interface PageViewInfoAccessor {
    void batchInsert(AppPageViewInfo info, Map<String, List<AppPageViewEntry>> entryMap, Connection conn) throws DbException;
}
