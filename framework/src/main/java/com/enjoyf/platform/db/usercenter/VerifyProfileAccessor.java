package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface VerifyProfileAccessor {

    public VerifyProfile insert(VerifyProfile wanbaProfile, Connection conn) throws DbException;

    public VerifyProfile get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<VerifyProfile> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<VerifyProfile> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
