package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.gamesubscribe.GameSubscribe;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:48
 * Desc:
 */
public class GameResourceHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private GameResourceAccessor gameResourceAccessor = null;
    private GameRelationAccessor gameRelationAccessor = null;
    private GamePropertyAccessor gamePropertyAccessor = null;
    private GameLayoutAccessor gameLayoutAccessor = null;
    private GamePrivacyAccessor gamePrivacyAccessor = null;

    private WikiResourceAccessor wikiResourceAccessor = null;

    //new game preview
    private NewReleaseAccessor newReleaseAccessor;
    private NewReleaseTagAccessor newReleaseTagAccessor;
    private NewReleaseTagRelationAccessor newReleaseTagRelationAccessor;
    private CityAccessor cityAccessor;
    private CityRelationAccessor cityRelationAccessor;

    private GroupUserAccessor groupUserAccessor;

    private RoleAccessor roleAccessor;

    private PrivilegeRoleRelationAccessor privilegeRoleRelationAccessor;
    private GroupProfileAccessor groupProfileAccessor;
    private GroupPrivilegeAccessor groupPrivilegeAccessor;
    private GroupProfilePrivilegeAccessor groupProfilePrivilegeAccessor;

    private GamePropertyInfoAccessor gamePropertyInfoAccessor;
//    private GameSubscribeAccessor gameSubscribeAccessor;

    public GameResourceHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();
        gameResourceAccessor = TableAccessorFactory.get().factoryAccessor(GameResourceAccessor.class, dataBaseType);
        gameRelationAccessor = TableAccessorFactory.get().factoryAccessor(GameRelationAccessor.class, dataBaseType);
        gamePropertyAccessor = TableAccessorFactory.get().factoryAccessor(GamePropertyAccessor.class, dataBaseType);

        gameLayoutAccessor = TableAccessorFactory.get().factoryAccessor(GameLayoutAccessor.class, dataBaseType);

        gamePrivacyAccessor = TableAccessorFactory.get().factoryAccessor(GamePrivacyAccessor.class, dataBaseType);

        wikiResourceAccessor = TableAccessorFactory.get().factoryAccessor(WikiResourceAccessor.class, dataBaseType);

        newReleaseAccessor = TableAccessorFactory.get().factoryAccessor(NewReleaseAccessor.class, dataBaseType);
        newReleaseTagAccessor = TableAccessorFactory.get().factoryAccessor(NewReleaseTagAccessor.class, dataBaseType);
        newReleaseTagRelationAccessor = TableAccessorFactory.get().factoryAccessor(NewReleaseTagRelationAccessor.class, dataBaseType);
        cityAccessor = TableAccessorFactory.get().factoryAccessor(CityAccessor.class, dataBaseType);
        cityRelationAccessor = TableAccessorFactory.get().factoryAccessor(CityRelationAccessor.class, dataBaseType);

        groupUserAccessor = TableAccessorFactory.get().factoryAccessor(GroupUserAccessor.class, dataBaseType);

        roleAccessor = TableAccessorFactory.get().factoryAccessor(RoleAccessor.class, dataBaseType);

        privilegeRoleRelationAccessor = TableAccessorFactory.get().factoryAccessor(PrivilegeRoleRelationAccessor.class, dataBaseType);

        groupPrivilegeAccessor = TableAccessorFactory.get().factoryAccessor(GroupPrivilegeAccessor.class, dataBaseType);
        groupProfileAccessor = TableAccessorFactory.get().factoryAccessor(GroupProfileAccessor.class, dataBaseType);

        groupProfilePrivilegeAccessor = TableAccessorFactory.get().factoryAccessor(GroupProfilePrivilegeAccessor.class, dataBaseType);

        gamePropertyInfoAccessor = TableAccessorFactory.get().factoryAccessor(GamePropertyInfoAccessor.class, dataBaseType);

//        gameSubscribeAccessor = TableAccessorFactory.get().factoryAccessor(GameSubscribeAccessor.class, dataBaseType);

    }

    public GameResource insertBameResource(GameResource entity) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameResource getGameResource(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GameResource> queryGameResources(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GameResource> queryGameResourcesByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<GameResource> returnValue = new PageRows<GameResource>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(gameResourceAccessor.queryByPage(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public int updateGameResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int deleteGameResource(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GameResource> queryBySynonyms(String synonyms) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameResourceAccessor.queryBySynonyms(synonyms, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    public GameRelation insertGameRelation(GameRelation gameRelation) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameRelationAccessor.insert(gameRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameRelation getGameRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameRelationAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<Long, List<GameRelation>> queryGameRelationByMap(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        Map<Long, List<GameRelation>> returnMap = new HashMap<Long, List<GameRelation>>();

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<GameRelation> gameRelations = gameRelationAccessor.query(queryExpress, conn);

            for (GameRelation gameRelation : gameRelations) {
                if (!returnMap.containsKey(gameRelation.getResourceId())) {
                    returnMap.put(gameRelation.getResourceId(), new ArrayList<GameRelation>());
                }

                returnMap.get(gameRelation.getResourceId()).add(gameRelation);
            }

            return returnMap;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GameRelation> queryGameRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        List<GameRelation> returnMap = new ArrayList<GameRelation>();

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGameRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public GameProperty insertGameProperty(GameProperty gameProperty) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePropertyAccessor.insert(gameProperty, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GameProperty> insertGameProperties(List<GameProperty> gamePropertyList) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);
            List<GameProperty> returnList = gamePropertyAccessor.insert(gamePropertyList, conn);
            conn.commit();
            return returnList;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On batch insertGameProperties , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGameProperties(List<GameProperty> gamePropertyList, long resourceId, GamePropertyDomain gamePropertyDomain) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            gamePropertyAccessor.delete(new QueryExpress()
                    .add(QueryCriterions.eq(GamePropertyField.RESOURCEID, resourceId))
                    .add(QueryCriterions.eq(GamePropertyField.PROPERTYDOMAIN, gamePropertyDomain.getCode()))
                    , conn);
            gamePropertyAccessor.insert(gamePropertyList, conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On batch insertGameProperties , a SQLException occurred.", e);
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<Long, List<GameProperty>> queryGameProperties(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        Map<Long, List<GameProperty>> returnMap = new HashMap<Long, List<GameProperty>>();

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<GameProperty> propertyList = gamePropertyAccessor.query(queryExpress, conn);

            for (GameProperty porperty : propertyList) {
                if (!returnMap.containsKey(porperty.getResourceId())) {
                    returnMap.put(porperty.getResourceId(), new ArrayList<GameProperty>());
                }

                returnMap.get(porperty.getResourceId()).add(porperty);
            }

            return returnMap;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameLayout insertGameLayout(GameLayout gameLayout) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            gameLayoutAccessor.insert(gameLayout, conn);
            return gameLayout;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGameLayout(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameLayoutAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameLayout getGameLayout(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameLayoutAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GamePrivacy insertGamePrivacy(GamePrivacy gamePrivcacy) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePrivacyAccessor.insert(gamePrivcacy, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGamePrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePrivacyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GamePrivacy getGamePrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePrivacyAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GamePrivacy> queryGamePrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePrivacyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteGamePrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePrivacyAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    public PageRows<WikiResource> queryWikiResource(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<WikiResource> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<WikiResource> resourceList = wikiResourceAccessor.query(queryExpress, pagination, conn);

            returnObj = new PageRows<WikiResource>();
            returnObj.setRows(resourceList);
            returnObj.setPage(pagination);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public WikiResource getWikiResource(QueryExpress getExpress) throws DbException {
        Connection conn = null;
        WikiResource returnObj = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            returnObj = wikiResourceAccessor.get(getExpress, conn);
            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<WikiResource> queryWikiResource(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiResourceAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public WikiResource insertWikiResource(WikiResource wikiResource) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiResourceAccessor.insert(wikiResource, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateWikiResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return wikiResourceAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    public NewRelease insertNewGameInfo(NewRelease newRelease) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseAccessor.insert(newRelease, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateNewGameInfo(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public NewRelease getNewGameInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<NewRelease> queryNewGameInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<NewRelease> queryNewGameInfoByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<NewRelease> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<NewRelease> list = newReleaseAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<NewRelease>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NewReleaseTag insertNewGameTag(NewReleaseTag newGameTag) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagAccessor.insert(newGameTag, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public boolean updateNewGameTag(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NewReleaseTag getNewGameTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<NewReleaseTag> queryNewGameTag(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<NewReleaseTag> queryNewGameTagByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<NewReleaseTag> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<NewReleaseTag> list = newReleaseTagAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public NewReleaseTagRelation insertNewTagRelation(NewReleaseTagRelation newTagRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagRelationAccessor.insert(newTagRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<NewReleaseTagRelation> queryNewTagRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public City createCity(City city) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityAccessor.insert(city, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateCity(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<City> queryCity(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<CityRelation> queryCityRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public CityRelation insertCityRelation(CityRelation cityRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityRelationAccessor.insert(cityRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyCityRelation(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return cityRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NewReleaseTagRelation createTagRelation(NewReleaseTagRelation tagRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagRelationAccessor.insert(tagRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyTagRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return newReleaseTagRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //group user
    public GroupUser insertGroupUser(GroupUser groupUser) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupUserAccessor.insert(groupUser, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGroupUser(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupUserAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupUser getGroupUserByGroupIdUno(String uno, long groupId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupUserAccessor.getByGroupIdUno(uno, groupId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GroupUser> queryGroupUserList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupUserAccessor.query(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<GroupUser> queryGroupUser(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<GroupUser> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GroupUser> list = groupUserAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<GroupUser>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Role insertRole(Role role) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return roleAccessor.insert(role, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Role getRole(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return roleAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Role> queryRole(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return roleAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Role> queryRoleByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<Role> pageRows = new PageRows<Role>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<Role> list = roleAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateRole(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return roleAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupPrivilege insertGroupPrivilege(GroupPrivilege groupPrivilege) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupPrivilegeAccessor.insert(groupPrivilege, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupPrivilege getGroupPrivilege(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupPrivilegeAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GroupPrivilege> queryGroupPrivilege(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupPrivilegeAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GroupPrivilege> queryGroupPrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<GroupPrivilege> pageRows = new PageRows<GroupPrivilege>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GroupPrivilege> list = groupPrivilegeAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGroupPrivilege(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupPrivilegeAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeRoleRelation insertPrivilegeRoleRelation(PrivilegeRoleRelation relation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleRelationAccessor.insert(relation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PrivilegeRoleRelation getPrivilegeRoleRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleRelationAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PrivilegeRoleRelation> queryPrivilegeRoleRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<PrivilegeRoleRelation> queryPrivilegeRoleRelationByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<PrivilegeRoleRelation> pageRows = new PageRows<PrivilegeRoleRelation>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<PrivilegeRoleRelation> list = privilegeRoleRelationAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updatePrivilegeRoleRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return privilegeRoleRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupProfile insertGroupProfile(GroupProfile groupProfile) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfileAccessor.insert(groupProfile, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public GroupProfile getGroupProfile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfileAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GroupProfile> queryGroupProfile(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfileAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GroupProfile> queryGroupProfileByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<GroupProfile> pageRows = new PageRows<GroupProfile>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GroupProfile> groupProfile = groupProfileAccessor.query(queryExpress, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(groupProfile);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyGroupProfile(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfileAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupProfilePrivilege insertGroupProfilePrivilege(GroupProfilePrivilege groupProfilePrivilege) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfilePrivilegeAccessor.insert(groupProfilePrivilege, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GroupProfilePrivilege getGroupProfilePrivilege(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfilePrivilegeAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GroupProfilePrivilege> queryGroupProfilePrivilege(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfilePrivilegeAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GroupProfilePrivilege> queryGroupProfilePrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<GroupProfilePrivilege> pageRows = new PageRows<GroupProfilePrivilege>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GroupProfilePrivilege> list = groupProfilePrivilegeAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateGroupProfilePrivilege(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return groupProfilePrivilegeAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GamePropertyInfo> insertGamePropertyInfo(List<GamePropertyInfo> gamePropertyInfoList) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePropertyInfoAccessor.batchInsert(gamePropertyInfoList, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GamePropertyInfo> queryGamePropertyInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gamePropertyInfoAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GamePropertyInfo> queryGamePropertyInfoByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<GamePropertyInfo> pageRows = new PageRows<GamePropertyInfo>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<GamePropertyInfo> list = gamePropertyInfoAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }


//    public List<GameSubscribe> insertGameSubscribe(List<GameSubscribe> gameSubscribeList) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return gameSubscribeAccessor.batchInsert(gameSubscribeList, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public List<GameSubscribe> queryGameSubscribe(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return gameSubscribeAccessor.query(queryExpress, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public GameSubscribe getGameSubscribe(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return gameSubscribeAccessor.get(queryExpress, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//
//    }
//
//    public boolean modifyGameSubscribe(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return gameSubscribeAccessor.update(updateExpress, queryExpress, conn) > 0;
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
}
