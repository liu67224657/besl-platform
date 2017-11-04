package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.service.gameres.GameResourceConstants;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBChannel;
import com.enjoyf.platform.service.gameres.gamedb.GameDBRelation;
import com.enjoyf.platform.service.gameres.privilege.PrivilegeRoleRelation;
import com.enjoyf.platform.service.gameres.privilege.Role;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-15
 * Time: 下午5:13
 * To change this template use File | Settings | File Templates.
 */
public class GroupPrivilegeCache {
    private static final long TIME_OUT_SEC = 60 * 60 * 6l;
    //
    private static final String KEY_CACHE_GROUP_ROLE = "_group_role_";
    private static final String KEY_CACHE_ROLE_PRIVILEGE = "_group_role_privilege_";
    //

    //gameCAtegoryCache
    private static final String KEY_CATEGORY_ID = "_categor_id_";
    private static final long GAMEDB_CHANNEL_CODE = 60 * 60 * 3;

    //gamedb mongo cache
    private static final String KEY_CACHE_GAMEDB_MONGO = "_gamedb_mongo_";

    private static final String KEY_CACHE_GAMEDB_LIST = "gamedb_list";


    //gameCAtegoryCache
    private static final String KEY_CACHE_GAMEDB_REALTION = "_gamedb_relation_";
    private static final long GAMEDB_REALTION_TIME_OUT_SEC = 60 * 60 * 6l;


    private static final String KEY_CACHE_GAMEDB_CHANNEL = "_gamedb_channel_";
    private static final long TIME_OUT_GAMEDB_CHANNEL = 60 * 60 * 3;

    private static final String KEY_CACHE_GAMEDB_ANOTHERNAME = "_gamedb_anothername_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    public GroupPrivilegeCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    /////////////////////////////////////////////////////
    private String getKeyCacheGroupRole(long groupId) {
        String key = GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GROUP_ROLE + groupId;
        return key;
    }

    public List<Role> getRoleByGroup(long groupId) {
        Object infoList = manager.get(getKeyCacheGroupRole(groupId));
        if (infoList == null) {
            return null;
        }
        return (List<Role>) infoList;
    }

    public void putRoleByGroup(long groupId, List<Role> list) {
        manager.put(getKeyCacheGroupRole(groupId), list, TIME_OUT_SEC);
    }

    public boolean removeRoleByGroup(long groupId) {
        return manager.remove(getKeyCacheGroupRole(groupId));
    }

    ///////////////////////////////////////////////////////////
    private String getKeyCacheRolePrivilege(long roleId) {
        String key = GameResourceConstants.SERVICE_SECTION + KEY_CACHE_ROLE_PRIVILEGE + roleId;
        return key;
    }

    public void putRelationByRole(long roleId, List<PrivilegeRoleRelation> list) {
        manager.put(getKeyCacheRolePrivilege(roleId), list, TIME_OUT_SEC);
    }

    public boolean removeRelationByRole(long roleId) {
        return manager.remove(getKeyCacheRolePrivilege(roleId));
    }

    public List<PrivilegeRoleRelation> getRelationByRole(long roleId) {
        Object infoList = manager.get(getKeyCacheRolePrivilege(roleId));
        if (infoList == null) {
            return null;
        }
        return (List<PrivilegeRoleRelation>) infoList;
    }


    public void putGameDBCache(long gameDbId, GameDB gameDb) {
        String key = GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_MONGO + gameDbId;
        manager.put(key, gameDb, TIME_OUT_SEC);
    }

    public boolean removeGameDBCache(long gameDbId) {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_MONGO + gameDbId);
    }

    public GameDB getGameDBCache(long gameDbId) {
        Object gamedb = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_MONGO + gameDbId);
        if (gamedb == null) {
            return null;
        }
        return (GameDB) gamedb;
    }

    //////////////CATEGORYCACHE/////////////////
    public void putGameCategoryCache(String categoryId, List<GameDB> gameDBList) {
        String key = GameResourceConstants.SERVICE_SECTION + KEY_CATEGORY_ID + categoryId;
        manager.put(key, gameDBList, GAMEDB_CHANNEL_CODE);
    }

    public List<GameDB> getGameCategoryList(String categoryId) {
        Object lists = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CATEGORY_ID + categoryId);
        return (List<GameDB>) lists;
    }

    public boolean removeGameCategoryList(String categoryId) {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CATEGORY_ID + categoryId);
    }

    ///////////gamedb realtions
    public void putRelations(long gamedbId, List<GameDBRelation> relations) {
        manager.put(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_REALTION + gamedbId, relations, GAMEDB_REALTION_TIME_OUT_SEC);
    }

    public List<GameDBRelation> getRelations(long gamedbId) {
        Object lists = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_REALTION + gamedbId);
        if (lists == null) {
            return null;
        }
        return (List<GameDBRelation>) lists;
    }

    public boolean removeRelations(long gamedbId) {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_REALTION + gamedbId);
    }

    ////////////////////////
    public void putGameDBList(List<GameDB> lists) {
        manager.put(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_LIST, lists, 60 * 60 * 2);
    }


    public List<GameDB> getGameDBList() {
        Object lists = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_LIST);
        if (lists == null) {
            return null;
        }
        return (List<GameDB>) lists;
    }

    public boolean removeGameDBListCache() {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_LIST);
    }

    //////////////////////
    public void putGameDBChannel(GameDBChannel channel) {
        String key = GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_CHANNEL + channel.getChannelCode();
        manager.put(key, channel, TIME_OUT_GAMEDB_CHANNEL);
    }

    public GameDBChannel getGameDBChannel(String code) {
        Object obj = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_CHANNEL + code);
        if (obj != null) {
            return (GameDBChannel) obj;
        }
        return null;
    }

    public boolean removeGameDBChannel(String code) {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_CHANNEL + code);
    }

    public GameDB getGameDBByAnotherName(String anotherName) {
        Object obj = manager.get(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_ANOTHERNAME + anotherName);
        if (obj == null) {
            return null;
        }
        return (GameDB) obj;
    }

    public void putGameDBByAnotherName(String anotherName, GameDB gameDB) {
        manager.put(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_ANOTHERNAME + anotherName, gameDB, 60l * 60l);
    }

    public boolean removeGameDBByAnotherName(Object anotherName) {
        return manager.remove(GameResourceConstants.SERVICE_SECTION + KEY_CACHE_GAMEDB_ANOTHERNAME + anotherName);
    }
}
