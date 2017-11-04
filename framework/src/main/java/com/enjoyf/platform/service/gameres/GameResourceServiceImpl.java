package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.ClientLineType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午12:52
 * Desc:
 */
class GameResourceServiceImpl implements GameResourceService {
	
    private ReqProcessor reqProcessor = null;

    public GameResourceServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("GameResourceServiceImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public GameResource createGameResource(GameResource gameResource) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(gameResource);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (GameResource) rPacket.readSerializable();
    }

    @Override
    public GameResource getGameResource(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(getExpress);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (GameResource) rPacket.readSerializable();
    }

    @Override
    public List<GameResource> queryGameResources(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<GameResource>) rPacket.readSerializable();
    }

    @Override
    public PageRows<GameResource> queryGameResourceByPage(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(page);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_QUERY_BY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<GameResource>) rPacket.readSerializable();
    }

    @Override
    public List<GameResource> queryGameResourcesBySynonyms(String synonyms) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(synonyms);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_QUERY_BY_SYNONYMS, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<GameResource>) rPacket.readSerializable();
    }

    @Override
    public Map<String, List<GameResource>> queryGameResourceMapBySynonymses(Set<String> synonymses) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable((Serializable) synonymses);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_QUERY_MAP_BY_SYNONYMSES, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Map<String, List<GameResource>>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyGameResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(GameResourceConstants.GAME_RESOURCE_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean deleteGameResource(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_RESOURCE_DELETE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(event);

        Request req = new Request(GameResourceConstants.RECIEVE_EVENT, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public GameRelation createRelation(GameRelation gameRelation) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(gameRelation);

        Request req = new Request(GameResourceConstants.GAME_RELATION_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (GameRelation) rPacket.readSerializable();
    }

    @Override
    public boolean modifyGameRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_RELATION_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public List<GameResource> queryGameResourceByRelationValue(String relationValue, GameRelationType gameRelationType) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(relationValue);
        wPacket.writeSerializable(gameRelationType);

        Request request = new Request(GameResourceConstants.GAME_RESOURCE_QUERY_BY_RELATIONVALUE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<GameResource>) rPacket.readSerializable();
    }

    @Override
    public GameRelation getGameRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_RELATION_GET, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GameRelation) rPacket.readSerializable();
    }

    @Override
    public List<GameRelation> queryGameRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_RELATION_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<GameRelation>) rPacket.readSerializable();
    }

    @Override
    public List<GameResource> queryGameResourceByRelationQueryExpress(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_RELATION_QUERY_GAMERESOURCE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<GameResource>) rPacket.readSerializable();
    }

    @Override
    public Map<Long, GameResource> searchGameResourceByWord(String s, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(s);
        wPacket.writeSerializable(pagination);

        Request request = new Request(GameResourceConstants.GAME_RESOURCE_SEARCHE_WORD, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (Map<Long, GameResource>) rPacket.readSerializable();
    }

    @Override
    public List<GameProperty> batchCreateGameProperts(List<GameProperty> gamePropertyList) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable((Serializable) gamePropertyList);

        Request request = new Request(GameResourceConstants.GAME_PROPERTY_BATCHICREATE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<GameProperty>) rPacket.readSerializable();
    }

    @Override
    public GameProperty createGameProperty(GameProperty gameProperty) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(gameProperty);

        Request request = new Request(GameResourceConstants.GAME_PROPERTY_BATCHICREATE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GameProperty) rPacket.readSerializable();
    }

    @Override
    public boolean modifyGameProperty(List<GameProperty> gamePropertyList, long resourceId, GamePropertyDomain gamePropertyDomain) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable((Serializable) gamePropertyList);
        wPacket.writeLongNx(resourceId);
        wPacket.writeSerializable(gamePropertyDomain);

        Request request = new Request(GameResourceConstants.GAME_PROPERTY_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public GameLayout createGameLayout(GameLayout gameLayout) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(gameLayout);

        Request request = new Request(GameResourceConstants.GAME_LAYOUT_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GameLayout) rPacket.readSerializable();
    }

    @Override
    public boolean modifyGameLayout(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_LAYOUT_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public GameLayout getGameLayout(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_LAYOUT_GET, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GameLayout) rPacket.readSerializable();
    }


    @Override
    public GamePrivacy createGamePrivacy(GamePrivacy privacy) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(privacy);

        Request request = new Request(GameResourceConstants.GAME_PRIVACT_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GamePrivacy) rPacket.readSerializable();
    }

    @Override
    public GamePrivacy getGamePrivacy(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(getExpress);

        Request request = new Request(GameResourceConstants.GAME_PRIVACT_GET, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (GamePrivacy) rPacket.readSerializable();
    }

    @Override
    public List<GamePrivacy> queryGamePrivacies(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.GAME_PRIVACT_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<GamePrivacy>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyGamePrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GAME_PRIVACT_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean deleteGamePrivacy(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GAME_PRIVACT_DELETE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public PageRows<WikiResource> queryWikiResourceByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_QUERY_BY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (PageRows<WikiResource>) rPacket.readSerializable();
    }

    @Override
    public List<WikiResource> queryWikiResource(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (List<WikiResource>) rPacket.readSerializable();
    }

    @Override
    public WikiResource getWikiResource(QueryExpress getExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(getExpress);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_GET, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (WikiResource) rPacket.readSerializable();
    }

    @Override
    public boolean modifyWikiResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public WikiResource createWikiResource(WikiResource wikiResource) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(wikiResource);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return (WikiResource) rPacket.readSerializable();
    }

    @Override
    public boolean statWiki(long resourceId) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeLongNx(resourceId);
        Request request = new Request(GameResourceConstants.WIKI_RESOURCE_STAT, wPacket);
        RPacket rPacket = reqProcessor.process(request);

        return rPacket.readBooleanNx();
    }

    @Override
    public NewRelease createNewGameInfo(NewRelease newRelease, List<Long> newGameTagIdList, long cityId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(newRelease);
        wp.writeSerializable((Serializable) newGameTagIdList);
        wp.writeLongNx(cityId);

        Request request = new Request(GameResourceConstants.CREATE_NEW_GAME_INFO, wp);
        RPacket rp = reqProcessor.process(request);

        return (NewRelease) rp.readSerializable();
    }

    @Override
    public boolean modifyNewGameInfo(long newGameInfoId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(newGameInfoId);
        wp.writeSerializable(updateExpress);

        Request request = new Request(GameResourceConstants.MODIFY_NEW_GAME_INFO, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();
    }

    @Override
    public NewRelease getNewGameInfo(long newGameInfoId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(newGameInfoId);

        Request request = new Request(GameResourceConstants.GET_NEW_GAME_INFO, wp);
        RPacket rp = reqProcessor.process(request);

        return (NewRelease) rp.readSerializable();
    }

    @Override
    public List<NewRelease> queryNewGameInfo(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.QUERY_NEW_GAME_INFO, wp);
        RPacket rp = reqProcessor.process(request);

        return (List<NewRelease>) rp.readSerializable();
    }

    @Override
    public PageRows<NewRelease> queryNewGameInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request request = new Request(GameResourceConstants.QUERY_NEW_GAME_INFO_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);

        return (PageRows<NewRelease>) rp.readSerializable();
    }

    @Override
    public NewReleaseTag createNewGameTag(NewReleaseTag newGameTag) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(newGameTag);

        Request request = new Request(GameResourceConstants.CREATE_NEW_GAME_TAG, wp);
        RPacket rp = reqProcessor.process(request);

        return (NewReleaseTag) rp.readSerializable();
    }

    @Override
    public boolean modifyNewGameTag(long newGameTagId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(newGameTagId);
        wp.writeSerializable(updateExpress);

        Request request = new Request(GameResourceConstants.MODIFY_NEW_GAME_TAG, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();
    }

    @Override
    public NewReleaseTag getNewGameTag(long newGameTagId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(newGameTagId);

        Request request = new Request(GameResourceConstants.GET_NEW_GAME_TAG, wp);
        RPacket rp = reqProcessor.process(request);

        return (NewReleaseTag) rp.readSerializable();
    }

    @Override
    public List<NewReleaseTag> queryNewGameTag(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.QUERY_NEW_GAME_TAG, wp);
        RPacket rp = reqProcessor.process(request);

        return (List<NewReleaseTag>) rp.readSerializable();
    }

    @Override
    public PageRows<NewReleaseTag> queryNewGameTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);

        Request request = new Request(GameResourceConstants.QUERY_NEW_GAME_TAG_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);

        return (PageRows<NewReleaseTag>) rp.readSerializable();
    }

    @Override
    public List<NewReleaseTagRelation> queryNewTagRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.QUERY_NEW_TEG_RELATION, wp);
        RPacket rp = reqProcessor.process(request);

        return (List<NewReleaseTagRelation>) rp.readSerializable();
    }

    @Override
    public City createCity(City city) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(city);

        Request request = new Request(GameResourceConstants.CREATE_CITY, wp);
        RPacket rp = reqProcessor.process(request);

        return (City) rp.readSerializable();
    }

    @Override
    public boolean modifyCity(long cityId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(cityId);
        wp.writeSerializable(updateExpress);

        Request request = new Request(GameResourceConstants.MODIFY_CITY, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();
    }

    @Override
    public List<City> queryCity(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.QUERY_CITY, wp);
        RPacket rp = reqProcessor.process(request);

        return (List<City>) rp.readSerializable();
    }

    @Override
    public List<CityRelation> queryCityRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);

        Request request = new Request(GameResourceConstants.QUERY_CITY_RELATION, wp);
        RPacket rp = reqProcessor.process(request);

        return (List<CityRelation>) rp.readSerializable();
    }

    @Override
    public boolean modifyCityRelation(long cityRelationId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(cityRelationId);
        wp.writeSerializable(updateExpress);

        Request request = new Request(GameResourceConstants.MODIFY_CITY_RELATION, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();
    }

    @Override
    public NewReleaseTagRelation createTagRelation(NewReleaseTagRelation tagRelation) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(tagRelation);

        Request request = new Request(GameResourceConstants.CREATE_TAG_RELATION, wp);
        RPacket rp = reqProcessor.process(request);

        return (NewReleaseTagRelation) rp.readSerializable();
    }

    @Override
    public boolean modifyTagRelation(long newTagRelationId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(newTagRelationId);
        wp.writeSerializable(updateExpress);

        Request request = new Request(GameResourceConstants.MODIFY_TAG_RELATION, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();
    }


    @Override
    public GroupUser createGroupUser(GroupUser groupUser) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(groupUser);

        Request request = new Request(GameResourceConstants.CREATE_GROUPUSER, wp);
        RPacket rp = reqProcessor.process(request);

        return (GroupUser) rp.readSerializable();
    }

    @Override
    public GroupUser getGroupUser(String uno, long groupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeLongNx(groupId);
        Request request = new Request(GameResourceConstants.GET_GROUPUSER_UNO_GROUPID, wp);
        RPacket rp = reqProcessor.process(request);

        return (GroupUser) rp.readSerializable();
    }

    @Override
    public PageRows<GroupUser> queryGroupUser(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_GROUPUSER_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);

        return (PageRows<GroupUser>) rp.readSerializable();
    }

    @Override
    public boolean modifyGroupUser(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.MODIFY_GROUPUSER, wp);
        RPacket rp = reqProcessor.process(request);

        return rp.readBooleanNx();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Role createRole(Role role) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(role);
        Request request = new Request(GameResourceConstants.CREATE_ROLE, wp);
        RPacket rp = reqProcessor.process(request);
        return (Role) rp.readSerializable();
    }

    @Override
    public Role getRole(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GET_ROLE, wp);
        RPacket rp = reqProcessor.process(request);
        return (Role) rp.readSerializable();
    }

    @Override
    public List<Role> queryRole(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.QUERY_ROLE, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<Role>) rp.readSerializable();
    }

    @Override
    public PageRows<Role> queryRoleByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_ROLE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<Role>) rp.readSerializable();
    }

    @Override
    public boolean modifyRole(long roleId, long groupId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(roleId);
        wp.writeLongNx(groupId);
        wp.writeSerializable(updateExpress);
        Request request = new Request(GameResourceConstants.MODIFY_ROLE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public PrivilegeRoleRelation createPrivilegeRoleRelation(PrivilegeRoleRelation relation) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(relation);
        Request request = new Request(GameResourceConstants.CREATE_PRIVILEGE_ROLE_RELATION, wp);
        RPacket rp = reqProcessor.process(request);
        return (PrivilegeRoleRelation) rp.readSerializable();
    }

    @Override
    public PrivilegeRoleRelation getPrivilegeRoleRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GET_PRIVILEGE_ROLE_RELATION, wp);
        RPacket rp = reqProcessor.process(request);
        return (PrivilegeRoleRelation) rp.readSerializable();
    }

    @Override
    public List<PrivilegeRoleRelation> queryPrivilegeRoleRelation(long roleId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(roleId);
        Request request = new Request(GameResourceConstants.QUERY_PRIVILEGE_ROLE_RELATION, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<PrivilegeRoleRelation>) rp.readSerializable();
    }

    @Override
    public PageRows<PrivilegeRoleRelation> queryPrivilegeRoleRelationByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_PRIVILEGE_ROLE_RELATION_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<PrivilegeRoleRelation>) rp.readSerializable();
    }

    @Override
    public boolean modifyPrivilegeRoleRelation(long relationId, long roleId, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(relationId);
        wp.writeLongNx(roleId);
        wp.writeSerializable(updateExpress);
        Request request = new Request(GameResourceConstants.MODIFY_PRIVILEGE_ROLE_RELATION, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GroupPrivilege createGroupPrivilege(GroupPrivilege groupPrivilege) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(groupPrivilege);
        Request request = new Request(GameResourceConstants.CREATE_GROUP_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupPrivilege) rp.readSerializable();
    }

    @Override
    public GroupPrivilege getGroupPrivilege(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GET_GROUP_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupPrivilege) rp.readSerializable();
    }

    @Override
    public List<GroupPrivilege> queryGroupPrivilege(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GroupPrivilege>) rp.readSerializable();
    }

    @Override
    public PageRows<GroupPrivilege> queryGroupPrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PRIVILEGE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GroupPrivilege>) rp.readSerializable();
    }

    @Override
    public boolean modifyGroupPrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(updateExpress);
        Request request = new Request(GameResourceConstants.MODIFY_GROUP_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GroupProfile createGroupProfile(GroupProfile groupProfile) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(groupProfile);
        Request request = new Request(GameResourceConstants.CREATE_GROUP_PROFILE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupProfile) rp.readSerializable();
    }

    @Override
    public GroupProfile getGroupProfile(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GET_GROUP_PROFILE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupProfile) rp.readSerializable();
    }

    @Override
    public List<GroupProfile> queryGroupProfile(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PROFILE, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GroupProfile>) rp.readSerializable();
    }

    @Override
    public PageRows<GroupProfile> queryGroupProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PROFILE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GroupProfile>) rp.readSerializable();
    }

    @Override
    public boolean modifyGroupProfile(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(updateExpress);
        Request request = new Request(GameResourceConstants.MODIFY_GROUP_PROFILE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GroupProfilePrivilege createGroupProfilePrivilege(GroupProfilePrivilege groupProfilePrivilege) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(groupProfilePrivilege);
        Request request = new Request(GameResourceConstants.CREATE_GROUP_PROFILE_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupProfilePrivilege) rp.readSerializable();
    }

    @Override
    public GroupProfilePrivilege getGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GET_GROUP_PROFILE_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GroupProfilePrivilege) rp.readSerializable();
    }

    @Override
    public List<GroupProfilePrivilege> queryGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PROFILE_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GroupProfilePrivilege>) rp.readSerializable();
    }

    @Override
    public PageRows<GroupProfilePrivilege> queryGroupProfilePrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_GROUP_PROFILE_PRIVILEGE_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GroupProfilePrivilege>) rp.readSerializable();
    }

    @Override
    public boolean modifyGroupProfilePrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(updateExpress);
        Request request = new Request(GameResourceConstants.MODIFY_GROUP_PROFILE_PRIVILEGE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GameDB createGameDb(GameDB gameDb) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(gameDb);
        Request request = new Request(GameResourceConstants.GAME_DB_CREATE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDB) rp.readSerializable();
    }

    @Override
    public int countGameDB(MongoQueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(GameResourceConstants.GAME_DB_COUNT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();

    }


    @Override
    public List<GameDB> queryGameDB(MongoQueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GAME_DB_QUERY, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDB>) rp.readSerializable();
    }

    @Override
    public List<GameDB> queryGameDBByCategory(String categoryId, MongoQueryExpress mongoQueryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(categoryId);
        wp.writeSerializable(mongoQueryExpress);
        Request request = new Request(GameResourceConstants.GAME_DB_CATEGORY, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDB>) rp.readSerializable();
    }

    @Override
    public PageRows<GameDB> queryGameDbByPage(MongoQueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.GAME_DB_QUERY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GameDB>) rp.readSerializable();
    }

    @Override
    public GameDB getGameDB(BasicDBObject basicDBObject, boolean b) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(basicDBObject);
        wp.writeBooleanNx(b);
        Request request = new Request(GameResourceConstants.GAME_DB_GET, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDB) rp.readSerializable();
    }

    @Override
    public boolean updateGameDB(BasicDBObject query, BasicDBObject update) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(query);
        wp.writeSerializable(update);
        Request request = new Request(GameResourceConstants.GAME_DB_UPDATE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public Map<Long, GameDB> queryGameDBSet(Set<Long> setLong) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) setLong);
        Request request = new Request(GameResourceConstants.QUERY_GAMEDB_BY_SET_RETURN_MAP, wp);
        RPacket rp = reqProcessor.process(request);
        return (Map<Long, GameDB>) rp.readSerializable();
    }

    @Override
    public List<GameDBChannel> queryGameDbChannel() throws ServiceException {
        WPacket wp = new WPacket();
        Request request = new Request(GameResourceConstants.GAME_DB_CHANNEL_QUERY, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDBChannel>) rp.readSerializable();
    }

    @Override
    public GameDBChannel getGameDbChannel(String code) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(code);
        Request request = new Request(GameResourceConstants.GAME_DBCHANNEL_BY_CODE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDBChannel) rp.readSerializable();
    }


    @Override
    public List<GamePropertyInfo> createGamePropertyInfo(List<GamePropertyInfo> gamePropertyInfoList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) gamePropertyInfoList);
        Request request = new Request(GameResourceConstants.CREATE_GAME_PROPERTY_INFO, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GamePropertyInfo>) rp.readSerializable();
    }

    @Override
    public List<GameDB> queryGamePropertyInfo(List<GamePropertyInfo> paramList) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) paramList);
        Request request = new Request(GameResourceConstants.QUERY_GAME_PROPERTY_INFO, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDB>) rp.readSerializable();
    }

    @Override
    public PageRows<GameDB> queryGamePropertyInfoByPage(List<GamePropertyInfo> paramList, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) paramList);
        wp.writeSerializable((Serializable) pagination);
        Request request = new Request(GameResourceConstants.QUERY_GAME_PROPERTY_INFO_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GameDB>) rp.readSerializable();
    }

    @Override
    public GameDBRelation createGameDbRelation(GameDBRelation gameDBRelation) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(gameDBRelation);
        Request request = new Request(GameResourceConstants.GAME_DB_RELATION_CREATE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDBRelation) rp.readSerializable();
    }

    @Override
    public List<GameDBRelation> queryGameDBRelationbyGameDbId(long gamedbId, boolean fromCache) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(gamedbId);
        wp.writeBooleanNx(fromCache);
        Request request = new Request(GameResourceConstants.GAME_DB_RELATION_QUERY, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDBRelation>) rp.readSerializable();
    }

    @Override
    public List<GameDB> queryGameDBByCache() throws ServiceException {
        WPacket wp = new WPacket();
        Request request = new Request(GameResourceConstants.GAME_DB_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameDB>) rp.readSerializable();
    }

    @Override
    public boolean updateGameDbRelation(UpdateExpress updateExpress, long realitonId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeLongNx(realitonId);
        Request request = new Request(GameResourceConstants.GAME_DB_RELATION_UPDATE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GameDBRelation getGameDbRelation(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request request = new Request(GameResourceConstants.GAME_DB_RELATION_GET, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDBRelation) rp.readSerializable();
    }

    @Override
    public GameBrand createGameBrand(GameBrand gameBrand) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(gameBrand);
        Request request = new Request(GameResourceConstants.GAME_BRAND_CREATE, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameBrand) rp.readSerializable();
    }

    @Override
    public List<GameBrand> queryGameBrand() throws ServiceException {
        WPacket wp = new WPacket();
        Request request = new Request(GameResourceConstants.GAME_BRAND_QUERY, wp);
        RPacket rp = reqProcessor.process(request);
        return (List<GameBrand>) rp.readSerializable();
    }

    @Override
    public void modifyGameBrand(Integer brandId, GameBrand gameBrand) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(brandId);
        wp.writeSerializable(gameBrand);
        Request request = new Request(GameResourceConstants.GAME_BRAND_MODIFY, wp);
        reqProcessor.process(request);
        return;
    }

    @Override
    public void putGameCollectionListCache(String lineCode, int displayOrder, GameDB gameDB) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(lineCode);
        wp.writeIntNx(displayOrder);
        wp.writeSerializable(gameDB);
        Request request = new Request(GameResourceConstants.PUT_GAME_COLLECTION_LIST_CACHE, wp);
        reqProcessor.process(request);
        return;
    }

    @Override
    public void incrGameCollectionListCache(String lineCode, int incScore, GameDB gameDB) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(lineCode);
        wp.writeIntNx(incScore);
        wp.writeSerializable(gameDB);
        Request request = new Request(GameResourceConstants.INCR_GAME_COLLECTION_LIST_CACHE, wp);
        reqProcessor.process(request);
        return;
    }

    @Override
    public boolean removeGameCollectionListCache(String lineCode) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(lineCode);
        Request request = new Request(GameResourceConstants.REMOVE_GAME_COLLECTION_LIST_CACHE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public  Map<String, List<GameCollectionDTO>> getGameCollectionListCache(ClientLineType clientLineType, AppPlatform platform, Set<String> lineCodeSet, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(clientLineType);
        wp.writeSerializable(platform);
        wp.writeSerializable((Serializable) lineCodeSet);
        wp.writeSerializable(page);
        Request request = new Request(GameResourceConstants.GET_GAME_COLLECTION_LIST_CACHE, wp);
        RPacket rp = reqProcessor.process(request);
        return (Map<String, List<GameCollectionDTO>>) rp.readSerializable();
    }

    @Override
    public GameDB getGameDBByAnotherName(String anotherName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(anotherName);
        Request request = new Request(GameResourceConstants.GET_GAME_DB_BY_ANOTHER_NAME, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameDB) rp.readSerializable();
    }

    @Override
    public PageRows<GameArchivesDTO> queryGameArchivesByCache(long gameDbId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(gameDbId);
        wp.writeSerializable(archiveRelationType);
        wp.writeSerializable(archiveContentType);
        wp.writeSerializable(pagination);
        Request request = new Request(GameResourceConstants.QUERY_GAME_ARCHIVES_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(request);
        return (PageRows<GameArchivesDTO>) rp.readSerializable();
    }

    @Override
    public void putGameArchivesByCache(Long gameId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, TagDedearchives tagDedearchives) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(gameId);
        wp.writeSerializable(archiveRelationType);
        wp.writeSerializable(archiveContentType);
        wp.writeSerializable(tagDedearchives);
        Request request = new Request(GameResourceConstants.PUT_GAME_ARCHIVES_BY_CACHE, wp);
        reqProcessor.process(request);
    }

    @Override
    public boolean removeGameArchivesByCache(Long gameId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, int archiveId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(gameId);
        wp.writeSerializable(archiveRelationType);
        wp.writeSerializable(archiveContentType);
        wp.writeIntNx(archiveId);
        Request request = new Request(GameResourceConstants.REMOVE_GAME_ARCHIVES_BY_CACHE, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public int getUserLikeGame(long uid, long gameId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(uid);
        wp.writeLongNx(gameId);
        Request request = new Request(GameResourceConstants.GET_USER_LIKE_GAME, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readIntNx();
    }

    @Override
    public boolean incUserLikeGame(long uid, long gameId, String anotherName, String column) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeLongNx(uid);
        wp.writeLongNx(gameId);
        wp.writeStringUTF(anotherName);
        wp.writeStringUTF(column);
        Request request = new Request(GameResourceConstants.INC_USER_LIKE_GAME, wp);
        RPacket rp = reqProcessor.process(request);
        return rp.readBooleanNx();
    }

    @Override
    public GameOrdered getGameOrdered(BasicDBObject basicDBObject) throws ServiceException {
        WPacket wp = new WPacket();
        wp. writeSerializable(basicDBObject);
        Request request = new Request(GameResourceConstants.GET_GAME_ORDERED, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameOrdered) rp.readSerializable();
    }

    @Override
    public GameOrdered createGameOrdered(GameOrdered gameOrdered) throws ServiceException {
        WPacket wp = new WPacket();
        wp. writeSerializable(gameOrdered);
        Request request = new Request(GameResourceConstants.CREATE_GAME_ORDERED, wp);
        RPacket rp = reqProcessor.process(request);
        return (GameOrdered) rp.readSerializable();
    }

    @Override
    public Map<String, String> getGameFilterGroup(List<String> keys) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) keys);;
        Request request = new Request(GameResourceConstants.GET_GAME_FILTER_GROUP, wp);
        RPacket rp = reqProcessor.process(request);
        return (Map<String, String>) rp.readSerializable();
    }

	@Override
	public void addGameFilterGroup(List<GameDB> games) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) games);
        Request request = new Request(GameResourceConstants.UPDATE_GAME_FILTER_GROUP, wp);
        reqProcessor.process(request);
	}


}
