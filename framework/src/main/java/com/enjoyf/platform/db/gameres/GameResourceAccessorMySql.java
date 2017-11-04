package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午4:53
 * Desc:
 */
public class GameResourceAccessorMySql extends AbstractGameResourceAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(GameResourceAccessorMySql.class);

    //
    @Override
    public List<GameResource> queryByPage(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(), queryExpress, page, conn);
    }
}
