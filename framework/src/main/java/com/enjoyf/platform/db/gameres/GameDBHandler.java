package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

import java.sql.Connection;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:48
 * Desc:
 */
public class GameDBHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private GameDBAccessorMongo gameDBAccessorMongo;
    private GameDBChannelAccessorMongo gameDBChannelAccessorMongo;
    private GameOrderedAccessorMongo gameOrderedAccessorMongo;
    private static final String DB_GAME_DB = "game";


    private GameDBRelationAccessor gameDBRelationAccessor;

    public GameDBHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        MongoSourceManager.get().append(dsn, props);
        gameDBAccessorMongo = new GameDBAccessorMongo();
        gameDBChannelAccessorMongo = new GameDBChannelAccessorMongo();
        gameOrderedAccessorMongo = new GameOrderedAccessorMongo();

        dataSourceName = dsn.toLowerCase();
        DataSourceManager.get().append(dataSourceName, props);
//
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();
        gameDBRelationAccessor = TableAccessorFactory.get().factoryAccessor(GameDBRelationAccessor.class, dataBaseType);
    }

    /**
     * GameDB CURD method begin
     */

    public GameDB insertGameDB(GameDB gameDb) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBAccessorMongo.insert(gameDb, db);
    }

    public int countGameDB(MongoQueryExpress express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBAccessorMongo.countGameDB(express, db);
    }

    public List<GameDB> query(MongoQueryExpress express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBAccessorMongo.query(express, db);
    }

    public PageRows<DBObject> findAll(BasicDBObject queryDBObject, Pagination pagination) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        List<DBObject> list = gameDBAccessorMongo.findAll(queryDBObject, pagination, db);
        PageRows<DBObject> pageRows = new PageRows<DBObject>();
        pageRows.setPage(pagination);
        pageRows.setRows(list);
        return pageRows;
    }

    public GameDB get(BasicDBObject express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBAccessorMongo.get(express, db);
    }

    public boolean update(BasicDBObject queryObject, BasicDBObject updateObject) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBAccessorMongo.update(queryObject, updateObject, db);
    }

    public PageRows<GameDB> queryGameDbByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws DbException {
        PageRows<GameDB> pageRows = null;
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        List<GameDB> list = gameDBAccessorMongo.query(mongoQueryExpress, pagination, db);
        pageRows = new PageRows<GameDB>();
        pageRows.setPage(pagination);
        pageRows.setRows(list);

        return pageRows;
    }

    /**
     * Gamedb curd method  end
     */

    public GameDBChannel insertGameDbChannelEntry(GameDBChannel gameDbChannel) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBChannelAccessorMongo.insert(gameDbChannel, db);
    }

    public List<GameDBChannel> queryGameDbChannel(MongoQueryExpress express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBChannelAccessorMongo.query(express, db);
    }


    public GameDBChannel getGameDbChannel(MongoQueryExpress express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameDBChannelAccessorMongo.get(express, db);
    }


    public GameDBRelation insertGameDbRelation(GameDBRelation gameDBRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameDBRelationAccessor.insert(gameDBRelation, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameDBRelation getGameDbRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameDBRelationAccessor.get(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<GameDBRelation> queryGameDBRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameDBRelationAccessor.query(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGameDBRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameDBRelationAccessor.update(updateExpress, queryExpress, conn) > 0;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleateGameDBRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameDBRelationAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameOrdered getGameOrdered(BasicDBObject basicDBObject) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameOrderedAccessorMongo.get(basicDBObject, db);
    }

    public GameOrdered insertGameOrdered(GameOrdered gameOrdered) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return gameOrderedAccessorMongo.insert(gameOrdered, db);
    }
}
