/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.service.gameres.GameResourceConstants;
import com.enjoyf.platform.service.gameres.gamedb.GameBrand;
import com.enjoyf.platform.service.gameres.gamedb.GameCategoryType;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameLanguageType;
import com.enjoyf.platform.service.gameres.gamedb.GameNetType;
import com.enjoyf.platform.service.gameres.gamedb.GamePlatform;
import com.enjoyf.platform.service.gameres.gamedb.GamePlatformType;
import com.enjoyf.platform.service.gameres.gamedb.GameThemeType;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class GameResourceRedis {
    private static final int TIME_OUT_SEC = 24 * 60 * 60;

    private static final String KEY_PREFIX = GameResourceConstants.SERVICE_SECTION;

    private static final String KEY_GAME_BRAND_LIST = "_game_brand_ids";
    private static final String KEY_GAME_BRAND = "_game_brand_";

    private static final String KEY_GAME_COLLECTION_GAMES = "_game_collection_games_ids_";
    private static final String KEY_GAME_COLLECTION_GAME = "_game_collection_game_";
    private static final String KEY_GAME_COLLECTION_ARCHIVES = "_game_collection_archives_ids_";
    private static final String KEY_GAME_COLLECTION_ARCHIVE = "_game_collection_archive_";
    private static final String KEY_GAME_COLLECTION_USER_LIKE_GAME = "_game_collection_user_like_game_";
   
    private static final String KEY_GAME_FILTER="_game_filter_";
    
    private static final String KEY_GAME_SHARE="_game_share_";


    private RedisManager manager;

    public GameResourceRedis(FiveProps p) {
        manager = new RedisManager(p);
    }


    public GameBrand putGameBrandList(GameBrand gameBrand) {
        String gameBrandIdsKey = KEY_PREFIX + KEY_GAME_BRAND_LIST;
        manager.lpush(gameBrandIdsKey, String.valueOf(gameBrand.getId()));
        setGameBrand(gameBrand.getId(), gameBrand);
        return gameBrand;
    }

    public List<GameBrand> getGameBrandList() {
        List<GameBrand> gameBrandList = null;
        List<String> idList = manager.lrange(KEY_PREFIX + KEY_GAME_BRAND_LIST, 0, -1);
        if (!CollectionUtil.isEmpty(idList)) {
            gameBrandList = new ArrayList<GameBrand>();
            for (String id : idList) {
                String obj = manager.get(KEY_PREFIX + KEY_GAME_BRAND + id);
                if (obj != null) {
                    GameBrand gameBrand = GameBrand.parse(obj);
                    if (gameBrand != null) {
                        gameBrandList.add(gameBrand);
                    }
                }
            }
        }
        return gameBrandList;
    }

    public void setGameBrand(Integer brandId, GameBrand gameBrand) {
        manager.set(KEY_PREFIX + KEY_GAME_BRAND + brandId, gameBrand.toJson());
    }

    public void incrGameCollectionListCache(String lineCode, int score, GameDB gameDB) {
        manager.zincrby(KEY_PREFIX + KEY_GAME_COLLECTION_GAMES + lineCode, score, String.valueOf(gameDB.getGameDbId()));
    }

    public void putGameCollectionListCache(String lineCode, int displayOrder, GameDB gameDB) {
        manager.zadd(KEY_PREFIX + KEY_GAME_COLLECTION_GAMES + lineCode, displayOrder, String.valueOf(gameDB.getGameDbId()), 0);
    }

    public void setGameCollectionDTO(GameCollectionDTO gameCollectionDTO) {
        manager.set(KEY_PREFIX + KEY_GAME_COLLECTION_GAME + gameCollectionDTO.getGameDbId(), gameCollectionDTO.toJson());
    }


    public GameCollectionDTO getGameCollectionDTO(long gameId) {
        GameCollectionDTO dto = null;
        String obj = manager.get(KEY_PREFIX + KEY_GAME_COLLECTION_GAME + gameId);
        if (obj != null) {
            dto = GameCollectionDTO.parse(obj);
        }
        return dto;
    }

    public boolean removeGameCollectionDTO(long gameId) {
        return manager.remove(KEY_PREFIX + KEY_GAME_COLLECTION_GAME + gameId) > 0l;
    }

    public List<GameCollectionDTO> getGameCollectionListCache(String lineCode, GameResourceLogic gameResourceLogic, Pagination page) throws ServiceException {
        List<GameCollectionDTO> gameCollectionDTOList = null;
        Set<String> idList = manager.zrange(KEY_PREFIX + KEY_GAME_COLLECTION_GAMES + lineCode, page == null ? 0 : page.getStartRowIdx(), page == null ? -1 : page.getEndRowIdx(), lineCode.equals("collection_game_hot_major") ? RedisManager.RANGE_ORDERBY_DESC : RedisManager.RANGE_ORDERBY_ASC);
        if (!CollectionUtil.isEmpty(idList)) {
            gameCollectionDTOList = new ArrayList<GameCollectionDTO>();
            for (String id : idList) {
                String obj = manager.get(KEY_PREFIX + KEY_GAME_COLLECTION_GAME + id);
                if (obj != null) {
                    GameCollectionDTO gameCollectionDTO = GameCollectionDTO.parse(obj);
                    if (gameCollectionDTO != null) {
                        gameCollectionDTOList.add(gameCollectionDTO);
                    }
                } else {
                    GameDB gameDB = gameResourceLogic.getGameDB(new BasicDBObject("_id", Long.valueOf(id)), false);
                    if (gameDB != null) {
                        GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
                        if (gameCollectionDTO != null) {
                            gameCollectionDTOList.add(gameCollectionDTO);
                            setGameCollectionDTO(gameCollectionDTO);
                        }
                    }
                }
            }
        }
        return gameCollectionDTOList;
    }

    public boolean removeGameCollectionListCache(String lineCode) {
        return manager.remove(KEY_PREFIX + KEY_GAME_COLLECTION_GAMES + lineCode) > 0l;
    }

    public void putGameArchivesByCache(long gameDbId, int relation, int content, TagDedearchives tagDedearchives) {
        String key = KEY_PREFIX + KEY_GAME_COLLECTION_ARCHIVES + gameDbId + "_" + relation + "_" + content;
        manager.zadd(key, tagDedearchives.getDede_archives_pubdate(), String.valueOf(tagDedearchives.getDede_archives_id()), 0);
        GameArchivesDTO gameArchivesDTO = GameArchivesDTO.buildDTOFromTagDedeArchives(tagDedearchives);
        manager.set(KEY_PREFIX + KEY_GAME_COLLECTION_ARCHIVE + tagDedearchives.getDede_archives_id(), gameArchivesDTO.toJson());
    }

    public PageRows<GameArchivesDTO> getGameArchivesByCache(long gameDbId, int relation, int content, Pagination pagination) {
        String key = KEY_PREFIX + KEY_GAME_COLLECTION_ARCHIVES + gameDbId + "_" + relation + "_" + content;
        List<GameArchivesDTO> gameArchivesDTOList = null;
        Set<String> idList = manager.zrange(key, pagination.getStartRowIdx(), pagination.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        if (!CollectionUtil.isEmpty(idList)) {
            gameArchivesDTOList = new ArrayList<GameArchivesDTO>();
            for (String id : idList) {
                String obj = manager.get(KEY_PREFIX + KEY_GAME_COLLECTION_ARCHIVE + id);
                if (obj != null) {
                    GameArchivesDTO gameArchivesDTO = GameArchivesDTO.parse(obj);
                    if (gameArchivesDTO != null) {
                        gameArchivesDTOList.add(gameArchivesDTO);
                    }
                }
            }
        }

        pagination.setTotalRows((int) manager.zcard(key));

        PageRows<GameArchivesDTO> pageRows = new PageRows<GameArchivesDTO>();
        pageRows.setRows(gameArchivesDTOList);
        pageRows.setPage(pagination);
        return pageRows;
    }

    public boolean removeGameArchivesByCache(long gameDbId, int relation, int content, int archiveId) {
        String key = KEY_PREFIX + KEY_GAME_COLLECTION_ARCHIVES + gameDbId + "_" + relation + "_" + content;
        return manager.zrem(key, String.valueOf(archiveId)) > 0l;
    }

    public int getUserLikeGame(long uid, long gameId) {
        String key = KEY_PREFIX + KEY_GAME_COLLECTION_USER_LIKE_GAME + uid + "_" + gameId;
        int sum = 0;
        String obj = manager.get(key);
        if (!StringUtil.isEmpty(obj)) {
            sum = Integer.valueOf(obj);
        }
        return sum;
    }

    public boolean incUserLikeGame(long uid, long gameId) {
        String key = KEY_PREFIX + KEY_GAME_COLLECTION_USER_LIKE_GAME + uid + "_" + gameId;
        return manager.incr(key, 1, 0) > 0l;
    }

    public Map<String, String> getGameFilterGroup(String key){
    	return manager.hgetAll(KEY_PREFIX+KEY_GAME_FILTER+key);
    }
    
    public String addGameFilterGroup(String key,Map<String, String> value){
    	return manager.hmset(KEY_PREFIX+KEY_GAME_FILTER+key, value);
    }
    
    public long delGameFilterGroup(String key,String... fields){
    	return manager.delHash(KEY_PREFIX+KEY_GAME_FILTER+key, fields);
    }
    
    public long updateGameFilterGroup(String key, String field,String value){
    	return manager.hset(KEY_PREFIX+KEY_GAME_FILTER+key, field, value);
    }
    
}
