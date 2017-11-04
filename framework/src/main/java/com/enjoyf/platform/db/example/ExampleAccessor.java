/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.example;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ExampleAccessor {
    //insert
    public Example insert(Example entry, Connection conn) throws DbException;

    //get by id
    public Example get(QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<Example> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<Example> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
