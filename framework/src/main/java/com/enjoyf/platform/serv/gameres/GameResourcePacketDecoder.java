package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.gamedb.GameBrand;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBRelation;
import com.enjoyf.platform.service.gameres.gamedb.GameOrdered;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.ClientLineType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:35
 * Desc:
 */
class GameResourcePacketDecoder extends PacketDecoder {

    private GameResourceLogic processLogic = null;

    public GameResourcePacketDecoder(GameResourceLogic logic) {
        processLogic = logic;

        //
        this.setTransContainer(GameResourceConstants.getTransContainer());
    }

    /**
     * This function should be implemented to call the server's logic.
     */
    @Override
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            //get
            case GameResourceConstants.GAME_RESOURCE_GET:
                wp.writeSerializable(processLogic.getGameResource((QueryExpress) rPacket.readSerializable()));
                break;
            //create
            case GameResourceConstants.GAME_RESOURCE_CREATE:
                wp.writeSerializable(processLogic.createGameResource((GameResource) rPacket.readSerializable()));
                break;
            //modify
            case GameResourceConstants.GAME_RESOURCE_MODIFY:
                wp.writeSerializable(processLogic.modifyGameResource((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            //query
            case GameResourceConstants.GAME_RESOURCE_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameResources((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RESOURCE_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryGameResourceByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RESOURCE_QUERY_BY_SYNONYMS:
                wp.writeSerializable((Serializable) processLogic.queryGameResourcesBySynonyms(rPacket.readStringUTF()));
                break;
            case GameResourceConstants.GAME_RESOURCE_QUERY_MAP_BY_SYNONYMSES:
                wp.writeSerializable((Serializable) processLogic.queryGameResourceMapBySynonymses((Set<String>) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RESOURCE_DELETE:
                wp.writeSerializable(processLogic.deleteGameResource((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RELATION_CREATE:
                wp.writeSerializable(processLogic.createRelation((GameRelation) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RELATION_MODIFY:
                wp.writeSerializable(processLogic.modifyGameRelation((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RESOURCE_QUERY_BY_RELATIONVALUE:
                wp.writeSerializable((Serializable) processLogic.queryGameResourceByRelationValue(rPacket.readStringUTF(), (GameRelationType) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RELATION_GET:
                wp.writeSerializable(processLogic.getGameRelation((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_RELATION_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameRelation((QueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_RELATION_QUERY_GAMERESOURCE:
                wp.writeSerializable((Serializable) processLogic.queryGameResourceByRelationQueryExpress((QueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_RESOURCE_SEARCHE_WORD:
                wp.writeSerializable((Serializable) processLogic.searchGameResourceByWord(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;


            case GameResourceConstants.GAME_PROPERTY_CREATE:
                wp.writeSerializable(processLogic.createGameProperty((GameProperty) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_PROPERTY_BATCHICREATE:
                wp.writeSerializable((Serializable) processLogic.batchCreateGameProperts((List<GameProperty>) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_PROPERTY_MODIFY:
                wp.writeSerializable(processLogic.modifyGameProperty((List<GameProperty>) rPacket.readSerializable(), rPacket.readLongNx(), (GamePropertyDomain) rPacket.readSerializable()));
                break;
            //
            case GameResourceConstants.GAME_LAYOUT_CREATE:
                wp.writeSerializable(processLogic.createGameLayout((GameLayout) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_LAYOUT_MODIFY:
                wp.writeSerializable(processLogic.modifyGameLayout((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_LAYOUT_GET:
                wp.writeSerializable(processLogic.getGameLayout((QueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_PRIVACT_CREATE:
                wp.writeSerializable(processLogic.createGamePrivacy((GamePrivacy) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_PRIVACT_GET:
                wp.writeSerializable(processLogic.getGamePrivacy((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_PRIVACT_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGamePrivacies((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_PRIVACT_MODIFY:
                wp.writeBooleanNx(processLogic.modifyGamePrivacy((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_PRIVACT_DELETE:
                wp.writeBooleanNx(processLogic.deleteGamePrivacy((QueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.WIKI_RESOURCE_CREATE:
                wp.writeSerializable(processLogic.createWikiResource((WikiResource) rPacket.readSerializable()));
                break;
            case GameResourceConstants.WIKI_RESOURCE_MODIFY:
                wp.writeBooleanNx(processLogic.modifyWikiResource((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.WIKI_RESOURCE_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryWikiResourceByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.WIKI_RESOURCE_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryWikiResource((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.WIKI_RESOURCE_GET:
                wp.writeSerializable(processLogic.getWikiResource((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.WIKI_RESOURCE_STAT:
                wp.writeSerializable(processLogic.statWiki(rPacket.readLongNx()));
                break;

            //review event
            case GameResourceConstants.RECIEVE_EVENT:
                wp.writeSerializable(processLogic.receiveEvent((Event) rPacket.readSerializable()));
                break;
            //new game preview
            case GameResourceConstants.CREATE_NEW_GAME_INFO:
                wp.writeSerializable(processLogic.createNewGameInfo((NewRelease) rPacket.readSerializable(), (List<Long>) rPacket.readSerializable(), rPacket.readLongNx()));
                break;
            case GameResourceConstants.MODIFY_NEW_GAME_INFO:
                wp.writeBooleanNx(processLogic.modifyNewGameInfo(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_NEW_GAME_INFO:
                wp.writeSerializable(processLogic.getNewGameInfo(rPacket.readLongNx()));
                break;
            case GameResourceConstants.QUERY_NEW_GAME_INFO:
                wp.writeSerializable((Serializable) processLogic.queryNewGameInfo((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_NEW_GAME_INFO_BY_PAGE:
                wp.writeSerializable(processLogic.queryNewGameInfoByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.CREATE_NEW_GAME_TAG:
                wp.writeSerializable(processLogic.createNewGameTag((NewReleaseTag) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_NEW_GAME_TAG:
                wp.writeBooleanNx(processLogic.modifyNewGameTag(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_NEW_GAME_TAG:
                wp.writeSerializable(processLogic.getNewGameTag(rPacket.readLongNx()));
                break;
            case GameResourceConstants.QUERY_NEW_GAME_TAG:
                wp.writeSerializable((Serializable) processLogic.queryNewGameTag((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_NEW_GAME_TAG_BY_PAGE:
                wp.writeSerializable(processLogic.queryNewGameTagByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_NEW_TEG_RELATION:
                wp.writeSerializable((Serializable) processLogic.queryNewTagRelation((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.CREATE_CITY:
                wp.writeSerializable(processLogic.createCity((City) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_CITY:
                wp.writeBooleanNx(processLogic.modifyCity(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_CITY:
                wp.writeSerializable((Serializable) processLogic.queryCity((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_CITY_RELATION:
                wp.writeSerializable((Serializable) processLogic.queryCityRelation((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_CITY_RELATION:
                wp.writeBooleanNx(processLogic.modifyCityRelation(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.CREATE_TAG_RELATION:
                wp.writeSerializable(processLogic.createTagRelation((NewReleaseTagRelation) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_TAG_RELATION:
                wp.writeBooleanNx(processLogic.modifyTagRelation(rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;

            ///////////////////////////////
            case GameResourceConstants.CREATE_GROUPUSER:
                wp.writeSerializable(processLogic.createGroupUser((GroupUser) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_GROUPUSER_UNO_GROUPID:
                wp.writeSerializable(processLogic.getGroupUser(rPacket.readStringUTF(), rPacket.readLongNx()));
                break;
            case GameResourceConstants.QUERY_GROUPUSER_BY_PAGE:
                wp.writeSerializable(processLogic.queryGroupUser((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_GROUPUSER:
                wp.writeSerializable(processLogic.modifyGroupUser((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            //////////////////////////////////
            case GameResourceConstants.CREATE_ROLE:
                wp.writeSerializable(processLogic.createRole((Role) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_ROLE:
                wp.writeSerializable(processLogic.getRole((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_ROLE:
                wp.writeSerializable((Serializable) processLogic.queryRole((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_ROLE_BY_PAGE:
                wp.writeSerializable(processLogic.queryRoleByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_ROLE:
                wp.writeBooleanNx(processLogic.modifyRole(rPacket.readLongNx(), rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            ///////////////////////////
            case GameResourceConstants.CREATE_GROUP_PRIVILEGE:
                wp.writeSerializable(processLogic.createGroupPrivilege((GroupPrivilege) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_GROUP_PRIVILEGE:
                wp.writeSerializable(processLogic.getGroupPrivilege((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PRIVILEGE:
                wp.writeSerializable((Serializable) processLogic.queryGroupPrivilege((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PRIVILEGE_BY_PAGE:
                wp.writeSerializable(processLogic.queryGroupPrivilegeByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_GROUP_PRIVILEGE:
                wp.writeBooleanNx(processLogic.modifyGroupPrivilege((QueryExpress) rPacket.readSerializable(), (UpdateExpress) rPacket.readSerializable()));
                break;
            ///////////////////////////////////////////
            case GameResourceConstants.CREATE_PRIVILEGE_ROLE_RELATION:
                wp.writeSerializable(processLogic.createPrivilegeRoleRelation((PrivilegeRoleRelation) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_PRIVILEGE_ROLE_RELATION:
                wp.writeSerializable(processLogic.getPrivilegeRoleRelation((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_PRIVILEGE_ROLE_RELATION:
                wp.writeSerializable((Serializable) processLogic.queryPrivilegeRoleRelation(rPacket.readLongNx()));
                break;
            case GameResourceConstants.QUERY_PRIVILEGE_ROLE_RELATION_BY_PAGE:
                wp.writeSerializable(processLogic.queryPrivilegeRoleRelationByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_PRIVILEGE_ROLE_RELATION:
                wp.writeBooleanNx(processLogic.modifyPrivilegeRoleRelation(rPacket.readLongNx(), rPacket.readLongNx(), (UpdateExpress) rPacket.readSerializable()));
                break;
            /////////////////////////////////
            case GameResourceConstants.CREATE_GROUP_PROFILE:
                wp.writeSerializable(processLogic.createGroupProfile((GroupProfile) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_GROUP_PROFILE:
                wp.writeSerializable(processLogic.getGroupProfile((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PROFILE:
                wp.writeSerializable((Serializable) processLogic.queryGroupProfile((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PROFILE_BY_PAGE:
                wp.writeSerializable(processLogic.queryGroupProfileByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_GROUP_PROFILE:
                wp.writeBooleanNx(processLogic.modifyGroupProfile((QueryExpress) rPacket.readSerializable(), (UpdateExpress) rPacket.readSerializable()));
                break;
            ////////////////////////////////////
            case GameResourceConstants.CREATE_GROUP_PROFILE_PRIVILEGE:
                wp.writeSerializable(processLogic.createGroupProfilePrivilege((GroupProfilePrivilege) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_GROUP_PROFILE_PRIVILEGE:
                wp.writeSerializable(processLogic.getGroupProfilePrivilege((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PROFILE_PRIVILEGE:
                wp.writeSerializable((Serializable) processLogic.queryGroupProfilePrivilege((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GROUP_PROFILE_PRIVILEGE_BY_PAGE:
                wp.writeSerializable(processLogic.queryGroupProfilePrivilegeByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.MODIFY_GROUP_PROFILE_PRIVILEGE:
                wp.writeBooleanNx(processLogic.modifyGroupProfilePrivilege((QueryExpress) rPacket.readSerializable(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_CREATE:
                wp.writeSerializable(processLogic.createGameDb((GameDB) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameDB((MongoQueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_GET:
                wp.writeSerializable(processLogic.getGameDB((BasicDBObject) rPacket.readSerializable(), rPacket.readBooleanNx()));
                break;
            case GameResourceConstants.GAME_DB_UPDATE:
                wp.writeSerializable(processLogic.updateGameDB((BasicDBObject) rPacket.readSerializable(), (BasicDBObject) rPacket.readSerializable()));
                break;
            case GameResourceConstants.CREATE_GAME_PROPERTY_INFO:
                wp.writeSerializable((Serializable) processLogic.createGamePropertyInfo((List<GamePropertyInfo>) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GAME_PROPERTY_INFO:
                wp.writeSerializable((Serializable) processLogic.queryGamePropertyInfo((List<GamePropertyInfo>) rPacket.readSerializable()));
                break;
            case GameResourceConstants.QUERY_GAME_PROPERTY_INFO_BY_PAGE:
                wp.writeSerializable((Serializable) processLogic.queryGamePropertyInfoByPage((List<GamePropertyInfo>) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_CHANNEL_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameDbChannel());
                break;
            case GameResourceConstants.GAME_DB_QUERY_PAGE:
                wp.writeSerializable(processLogic.queryGameDbByPage((MongoQueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DBCHANNEL_BY_CODE:
                wp.writeSerializable(processLogic.getGameDbChannel(rPacket.readStringUTF()));
                break;
            case GameResourceConstants.QUERY_GAMEDB_BY_SET_RETURN_MAP:
                wp.writeSerializable((Serializable) processLogic.queryGameDBSet((Set<Long>) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_BY_CACHE:
                wp.writeSerializable((Serializable) processLogic.queryGameDBByCache());
                break;

            case GameResourceConstants.GAME_DB_CATEGORY:
                wp.writeSerializable((Serializable) processLogic.queryGameDBByCategory((String) rPacket.readStringUTF(), (MongoQueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_DB_COUNT:
                wp.writeIntNx(processLogic.countGameDB((MongoQueryExpress) rPacket.readSerializable()));
                break;

            case GameResourceConstants.GAME_DB_RELATION_CREATE:
                wp.writeSerializable(processLogic.createGameDbRelation((GameDBRelation) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_RELATION_UPDATE:
                wp.writeSerializable(processLogic.updateGameDbRelation((UpdateExpress) rPacket.readSerializable(), rPacket.readLongNx()));
                break;

            case GameResourceConstants.GAME_DB_RELATION_GET:
                wp.writeSerializable(processLogic.getGameDbRelation((QueryExpress) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_DB_RELATION_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameDBRelationbyGameDbId(rPacket.readLongNx(), rPacket.readBooleanNx()));
                break;

            case GameResourceConstants.GAME_BRAND_CREATE:
                wp.writeSerializable(processLogic.createGameBrand((GameBrand) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GAME_BRAND_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryGameBrand());
                break;
            case GameResourceConstants.GAME_BRAND_MODIFY:
                processLogic.modifyGameBrand(rPacket.readIntNx(), (GameBrand) rPacket.readSerializable());
                break;
            case GameResourceConstants.PUT_GAME_COLLECTION_LIST_CACHE:
                processLogic.putGameCollectionListCache(rPacket.readStringUTF(), rPacket.readIntNx(), (GameDB) rPacket.readSerializable());
                break;
            case GameResourceConstants.INCR_GAME_COLLECTION_LIST_CACHE:
                processLogic.incrGameCollectionListCache(rPacket.readStringUTF(), rPacket.readIntNx(), (GameDB) rPacket.readSerializable());
                break;
            case GameResourceConstants.GET_GAME_COLLECTION_LIST_CACHE:
                wp.writeSerializable((Serializable) processLogic.getGameCollectionListCache((ClientLineType)rPacket.readSerializable(), (AppPlatform)rPacket.readSerializable(), (Set<String>) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.REMOVE_GAME_COLLECTION_LIST_CACHE:
                wp.writeBooleanNx(processLogic.removeGameCollectionListCache(rPacket.readStringUTF()));
                break;
            case GameResourceConstants.GET_GAME_DB_BY_ANOTHER_NAME:
                wp.writeSerializable(processLogic.getGameDBByAnotherName(rPacket.readStringUTF()));
                break;
            case GameResourceConstants.QUERY_GAME_ARCHIVES_BY_CACHE:
                wp.writeSerializable(processLogic.queryGameArchivesByCache(rPacket.readLongNx(), (ArchiveRelationType) rPacket.readSerializable(), (ArchiveContentType) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case GameResourceConstants.PUT_GAME_ARCHIVES_BY_CACHE:
                processLogic.putGameArchivesByCache(rPacket.readLongNx(), (ArchiveRelationType) rPacket.readSerializable(), (ArchiveContentType) rPacket.readSerializable(), (TagDedearchives) rPacket.readSerializable());
                break;
            case GameResourceConstants.REMOVE_GAME_ARCHIVES_BY_CACHE:
                wp.writeBooleanNx(processLogic.removeGameArchivesByCache(rPacket.readLongNx(), (ArchiveRelationType) rPacket.readSerializable(), (ArchiveContentType) rPacket.readSerializable(), rPacket.readIntNx()));
                break;
            case GameResourceConstants.INC_USER_LIKE_GAME:
                wp.writeBooleanNx(processLogic.incUserLikeGame(rPacket.readLongNx(), rPacket.readLongNx(), rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case GameResourceConstants.GET_USER_LIKE_GAME:
                wp.writeIntNx(processLogic.getUserLikeGame(rPacket.readLongNx(), rPacket.readLongNx()));
                break;
            case GameResourceConstants.GET_GAME_ORDERED:
                wp.writeSerializable(processLogic.getGameOrdered((BasicDBObject) rPacket.readSerializable()));
                break;
            case GameResourceConstants.CREATE_GAME_ORDERED:
                wp.writeSerializable(processLogic.createGameOrdered((GameOrdered) rPacket.readSerializable()));
                break;
            case GameResourceConstants.GET_GAME_FILTER_GROUP:
            	wp.writeSerializable((Serializable) processLogic.getGameFilterGroup((List<String>) rPacket.readSerializable()));
            	break;
            default:
                GAlerter.lab("GameResourcePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}