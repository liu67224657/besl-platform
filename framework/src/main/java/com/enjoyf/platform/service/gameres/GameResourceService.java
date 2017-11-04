package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.ClientLineType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午12:49
 * Desc:  游戏标签接口
 */
public interface GameResourceService extends EventReceiver {
	
    //insert
    public GameResource createGameResource(GameResource gameResource) throws ServiceException;

    //get
    public GameResource getGameResource(QueryExpress getExpress) throws ServiceException;

    //query
    public List<GameResource> queryGameResources(QueryExpress queryExpress) throws ServiceException;

    public PageRows<GameResource> queryGameResourceByPage(QueryExpress queryExpress, Pagination page) throws ServiceException;

    public List<GameResource> queryGameResourcesBySynonyms(String synonyms) throws ServiceException;

    public Map<String, List<GameResource>> queryGameResourceMapBySynonymses(Set<String> synonymses) throws ServiceException;

    //query resources by
    //public Map<Integer, GameResource> queryGameResourceByIds(Set<Integer> gameIds) throws ServiceException;

    //modify
    public boolean modifyGameResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    //delete
    public boolean deleteGameResource(QueryExpress queryExpress) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public GameRelation createRelation(GameRelation gameRelation) throws ServiceException;

    public boolean modifyGameRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public List<GameResource> queryGameResourceByRelationValue(String relationValue, GameRelationType gameRelationType) throws ServiceException;

    public GameRelation getGameRelation(QueryExpress queryExpress) throws ServiceException;

    public List<GameRelation> queryGameRelation(QueryExpress queryExpress) throws ServiceException;

    public List<GameResource> queryGameResourceByRelationQueryExpress(QueryExpress queryExpress) throws ServiceException;

    public Map<Long, GameResource> searchGameResourceByWord(String s, Pagination pagination) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////////////////
    public GameProperty createGameProperty(GameProperty gameProperty) throws ServiceException;

    public List<GameProperty> batchCreateGameProperts(List<GameProperty> gamePropertyList) throws ServiceException;


    public boolean modifyGameProperty(List<GameProperty> gamePropertyList, long resourceId, GamePropertyDomain gamePropertyDomain) throws ServiceException;

    ///////////////////////////////////////////////////////////////////////////////////////////
    public GameLayout createGameLayout(GameLayout gameLayout) throws ServiceException;

    public boolean modifyGameLayout(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public GameLayout getGameLayout(QueryExpress queryExpress) throws ServiceException;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //the game privacy apis
    public GamePrivacy createGamePrivacy(GamePrivacy privacy) throws ServiceException;

    public GamePrivacy getGamePrivacy(QueryExpress getExpress) throws ServiceException;

    public List<GamePrivacy> queryGamePrivacies(QueryExpress queryExpress) throws ServiceException;

    public boolean modifyGamePrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public boolean deleteGamePrivacy(QueryExpress queryExpress) throws ServiceException;

    //////////////////////////////////////////////////////////////////////////////////////////////
    //wiki resource
    public PageRows<WikiResource> queryWikiResourceByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public List<WikiResource> queryWikiResource(QueryExpress queryExpress) throws ServiceException;

    public WikiResource getWikiResource(QueryExpress getExpress) throws ServiceException;

    public boolean modifyWikiResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public WikiResource createWikiResource(WikiResource wikiResource) throws ServiceException;

    public boolean statWiki(long resourceId) throws ServiceException;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //new game info
    public NewRelease createNewGameInfo(NewRelease newRelease, List<Long> newGameTagIdList, long cityId) throws ServiceException;

    public boolean modifyNewGameInfo(long newGameInfoId, UpdateExpress updateExpress) throws ServiceException;

    public NewRelease getNewGameInfo(long newGameInfoId) throws ServiceException;

    public List<NewRelease> queryNewGameInfo(QueryExpress queryExpress) throws ServiceException;

    public PageRows<NewRelease> queryNewGameInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    //new game tag
    public NewReleaseTag createNewGameTag(NewReleaseTag newGameTag) throws ServiceException;

    public boolean modifyNewGameTag(long newGameTagId, UpdateExpress updateExpress) throws ServiceException;

    public NewReleaseTag getNewGameTag(long newGameTagId) throws ServiceException;

    public List<NewReleaseTag> queryNewGameTag(QueryExpress queryExpress) throws ServiceException;

    public PageRows<NewReleaseTag> queryNewGameTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    //new tag relation
    public List<NewReleaseTagRelation> queryNewTagRelation(QueryExpress queryExpress) throws ServiceException;

    //city
    public City createCity(City city) throws ServiceException;

    public boolean modifyCity(long cityId, UpdateExpress updateExpress) throws ServiceException;

    public List<City> queryCity(QueryExpress queryExpress) throws ServiceException;

    //city relation
    public List<CityRelation> queryCityRelation(QueryExpress queryExpress) throws ServiceException;

    public boolean modifyCityRelation(long cityRelationId, UpdateExpress updateExpress) throws ServiceException;

    public NewReleaseTagRelation createTagRelation(NewReleaseTagRelation tagRelation) throws ServiceException;

    public boolean modifyTagRelation(long newTagRelationId, UpdateExpress updateExpress) throws ServiceException;

    public GroupUser createGroupUser(GroupUser groupUser) throws ServiceException;

    public GroupUser getGroupUser(String uno, long groupId) throws ServiceException;

    public PageRows<GroupUser> queryGroupUser(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyGroupUser(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 权限管理
     */
    public Role createRole(Role role) throws ServiceException;

    public Role getRole(QueryExpress queryExpress) throws ServiceException;

    public List<Role> queryRole(QueryExpress queryExpress) throws ServiceException;

    public PageRows<Role> queryRoleByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyRole(long roleId, long groupId, UpdateExpress updateExpress) throws ServiceException;

    public PrivilegeRoleRelation createPrivilegeRoleRelation(PrivilegeRoleRelation relation) throws ServiceException;

    public PrivilegeRoleRelation getPrivilegeRoleRelation(QueryExpress queryExpress) throws ServiceException;

    public List<PrivilegeRoleRelation> queryPrivilegeRoleRelation(long roleId) throws ServiceException;

    public PageRows<PrivilegeRoleRelation> queryPrivilegeRoleRelationByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyPrivilegeRoleRelation(long relationId, long roleId, UpdateExpress updateExpress) throws ServiceException;

    public GroupPrivilege createGroupPrivilege(GroupPrivilege groupPrivilege) throws ServiceException;

    public GroupPrivilege getGroupPrivilege(QueryExpress queryExpress) throws ServiceException;

    public List<GroupPrivilege> queryGroupPrivilege(QueryExpress queryExpress) throws ServiceException;

    public PageRows<GroupPrivilege> queryGroupPrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyGroupPrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    public GroupProfile createGroupProfile(GroupProfile groupProfile) throws ServiceException;

    public GroupProfile getGroupProfile(QueryExpress queryExpress) throws ServiceException;

    public List<GroupProfile> queryGroupProfile(QueryExpress queryExpress) throws ServiceException;

    public PageRows<GroupProfile> queryGroupProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyGroupProfile(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    public GroupProfilePrivilege createGroupProfilePrivilege(GroupProfilePrivilege groupProfilePrivilege) throws ServiceException;

    public GroupProfilePrivilege getGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException;

    public List<GroupProfilePrivilege> queryGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException;

    public PageRows<GroupProfilePrivilege> queryGroupProfilePrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyGroupProfilePrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    ////////////////////////////////////////////////////////game db/////////////////////////////////////////////////////////////////////////

    public GameDB createGameDb(GameDB gameDb) throws ServiceException;

    public int countGameDB(MongoQueryExpress queryExpress) throws ServiceException;

    public List<GameDB> queryGameDB(MongoQueryExpress queryExpress) throws ServiceException;

    public List<GameDB> queryGameDBByCategory(String categoryId, MongoQueryExpress mongoQueryExpress) throws ServiceException;

    public PageRows<GameDB> queryGameDbByPage(MongoQueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public GameDB getGameDB(BasicDBObject basicDBObject, boolean b) throws ServiceException;

    public boolean updateGameDB(BasicDBObject query, BasicDBObject update) throws ServiceException;

    public Map<Long, GameDB> queryGameDBSet(Set<Long> setLong) throws ServiceException;

    /////////////////////////////////////////////////game db channel/////////////////////////////////////////////////////////////////////////////////////////

    public List<GameDBChannel> queryGameDbChannel() throws ServiceException;

    public GameDBChannel getGameDbChannel(String channelCode) throws ServiceException;

    public List<GamePropertyInfo> createGamePropertyInfo(List<GamePropertyInfo> gamePropertyInfoList) throws ServiceException;

    public List<GameDB> queryGamePropertyInfo(List<GamePropertyInfo> paramList) throws ServiceException;

    public PageRows<GameDB> queryGamePropertyInfoByPage(List<GamePropertyInfo> paramList, Pagination pagination) throws ServiceException;

    public List<GameDB> queryGameDBByCache() throws ServiceException;

    /////////////////////////////////game db relation/////////////////////////////////////////////////////////////////////////////////////
    public GameDBRelation createGameDbRelation(GameDBRelation gameDBRelation) throws ServiceException;

    /**
     * fromcache
     *
     * @param gamedbId
     * @param fromCache
     * @return
     * @throws ServiceException
     */
    public List<GameDBRelation> queryGameDBRelationbyGameDbId(long gamedbId, boolean fromCache) throws ServiceException;


    public boolean updateGameDbRelation(UpdateExpress updateExpress, long realitonId) throws ServiceException;

    public GameDBRelation getGameDbRelation(QueryExpress queryExpress) throws ServiceException;

    //GameBrand   redis
    public GameBrand createGameBrand(GameBrand gameBrand) throws ServiceException;

    public List<GameBrand> queryGameBrand() throws ServiceException;

    public void modifyGameBrand(Integer brandId, GameBrand gameBrand) throws ServiceException;

    public void putGameCollectionListCache(String lineCode, int displayOrder, GameDB gameDB) throws ServiceException;

    public void incrGameCollectionListCache(String lineCode, int incScore, GameDB gameDB) throws ServiceException;

    public boolean removeGameCollectionListCache(String lineCode) throws ServiceException;

    public Map<String, List<GameCollectionDTO>> getGameCollectionListCache(ClientLineType clientLineType, AppPlatform platform, Set<String> lineCodeSet, Pagination page) throws ServiceException;

    public GameDB getGameDBByAnotherName(String anotherName) throws ServiceException;

    public PageRows<GameArchivesDTO> queryGameArchivesByCache(long gameDbId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, Pagination pagination) throws ServiceException;

    public void putGameArchivesByCache(Long gameId, ArchiveRelationType gameRelation, ArchiveContentType contentType, TagDedearchives tagDedearchives) throws ServiceException;

    public boolean removeGameArchivesByCache(Long gameId, ArchiveRelationType gameRelation, ArchiveContentType contentType, int archiveId) throws ServiceException;

    public int getUserLikeGame(long uid, long gameId) throws ServiceException;

    public boolean incUserLikeGame(long uid, long gameId, String anotherName, String column) throws ServiceException;

    public GameOrdered getGameOrdered(BasicDBObject basicDBObject) throws ServiceException;

    public GameOrdered createGameOrdered(GameOrdered gameOrdered) throws ServiceException;
    
    public Map<String, String> getGameFilterGroup(List<String> keys) throws ServiceException;
    
    public void addGameFilterGroup(List<GameDB> games) throws ServiceException;
    
}
