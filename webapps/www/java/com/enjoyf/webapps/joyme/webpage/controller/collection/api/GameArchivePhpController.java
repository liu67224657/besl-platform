package com.enjoyf.webapps.joyme.webpage.controller.collection.api;

import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.point.ActivityGoods;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.collection.PhpArchiveGameDTO;
import com.enjoyf.webapps.joyme.dto.collection.PhpSearchGameDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.collection.AbstractGameCollectionController;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhitaoshi on 2015/6/15.
 */
@Controller
@RequestMapping(value = "/collection/api/gamearchive")
public class GameArchivePhpController extends AbstractGameCollectionController {

    private static final int GIFT_PAGE_SIZE = 4;

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @ResponseBody
    @RequestMapping(value = "/bind")
    public String bind(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "archiveid", required = false) String archiveIdStr,
                       @RequestParam(value = "gameids", required = false) String gameIds,        //多个之间","隔开
                       @RequestParam(value = "contenttype", required = false) String type
    ) {
        String callback = request.getParameter("callback");
        if (StringUtil.isEmpty(archiveIdStr) || StringUtil.isEmpty(gameIds) || StringUtil.isEmpty(type)) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        ArchiveContentType contentType = ArchiveContentType.getByCode(Integer.valueOf(type));
        if (contentType == null) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        String[] gameIdArr = gameIds.split(",");
        if (CollectionUtil.isEmpty(gameIdArr)) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }
        int archiveId = 0;
        try {
            archiveId = Integer.parseInt(archiveIdStr);
        } catch (Exception e) {
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
        }

        try {
            Archive archive = JoymeAppServiceSngl.get().getArchiveById(archiveId);
            if (archive != null) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveIdStr));
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));
                List<TagDedearchives> tagDedearchivesList = JoymeAppServiceSngl.get().queryTagDedearchives(queryExpress);
                Map<Long, TagDedearchives> existMap = new HashMap<Long, TagDedearchives>();
                if (!CollectionUtil.isEmpty(tagDedearchivesList)) {
                    for (TagDedearchives tagDedearchives : tagDedearchivesList) {
                        existMap.put(tagDedearchives.getTagid(), tagDedearchives);
                    }
                }
                for (String gameIdStr : gameIdArr) {
                    Long gameId = 0l;
                    try {
                        gameId = Long.parseLong(gameIdStr);
                        if (gameId <= 0l) {
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    if (existMap.containsKey(gameId)) {
                        QueryExpress q = new QueryExpress();
                        q.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveIdStr));
                        q.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId));
                        q.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));

                        UpdateExpress u = new UpdateExpress();
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, archive.getTitle());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_DESCRIPTION, archive.getDesc());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_LITPIC, archive.getIcon());
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, archive.getCreateTime().getTime());

                        DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                        String dedearchiv_url = "";
                        if (arctype != null) {
                            dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), contentType);
                        }
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_URL, dedearchiv_url);
                        u.set(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, contentType.getCode());
                        u.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode());
                        boolean bool = JoymeAppServiceSngl.get().modifyTagDedearchives(gameId, String.valueOf(archive.getArchiveId()), q, u, ArchiveRelationType.GAME_RELATION);
                        if (bool) {
                            TagDedearchives existArchive = existMap.get(gameId);
                            if (!contentType.equals(existArchive.getArchiveContentType())) {
                                GameResourceServiceSngl.get().removeGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, existArchive.getArchiveContentType(), archive.getArchiveId());
                            }
                            existArchive.setDede_archives_title(archive.getTitle());
                            existArchive.setDede_archives_description(archive.getDesc());
                            existArchive.setDede_archives_litpic(archive.getIcon());
                            existArchive.setDede_archives_pubdate(archive.getCreateTime().getTime());
                            existArchive.setDede_archives_url(dedearchiv_url);
                            existArchive.setArchiveContentType(contentType);
                            GameResourceServiceSngl.get().putGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, contentType, existArchive);
                        }
                        existMap.remove(gameId);
                    } else {
                        TagDedearchives inserTag = new TagDedearchives();
                        inserTag.setId(MD5Util.Md5(gameIdStr + archive.getArchiveId()));
                        inserTag.setDede_archives_title(archive.getTitle());
                        inserTag.setDede_archives_description(archive.getDesc());
                        inserTag.setDede_archives_litpic(archive.getIcon());
                        inserTag.setDisplay_order(System.currentTimeMillis());
                        inserTag.setDede_archives_id(String.valueOf(archive.getArchiveId()));
                        inserTag.setTagid(gameId);
                        inserTag.setDede_archives_pubdate(archive.getCreateTime().getTime());
                        inserTag.setRemove_status(ValidStatus.VALID);

                        inserTag.setArchiveRelationType(ArchiveRelationType.GAME_RELATION);
                        inserTag.setArchiveContentType(contentType);

                        DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                        if (arctype != null) {
                            String dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), contentType);
                            inserTag.setDede_archives_url(dedearchiv_url);
                        }

                        JoymeAppServiceSngl.get().createTagDedearchives(inserTag);
                        GameResourceServiceSngl.get().putGameArchivesByCache(gameId, ArchiveRelationType.GAME_RELATION, contentType, inserTag);
                    }
                }
                for (Long gid : existMap.keySet()) {
                    QueryExpress q = new QueryExpress();
                    q.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveIdStr));
                    q.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gid));
                    q.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));

                    UpdateExpress u = new UpdateExpress();
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_TITLE, archive.getTitle());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_DESCRIPTION, archive.getDesc());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_LITPIC, archive.getIcon());
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, archive.getCreateTime().getTime());
                    //跳转类型
                    DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                    if (arctype != null) {
                        String dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId(), contentType);
                        u.set(TagDedearchivesFiled.DEDE_ARCHIVES_URL, dedearchiv_url);
                    }
                    u.set(TagDedearchivesFiled.ARCHIVE_CONTENT_TYPE, contentType.getCode());
                    u.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.REMOVED.getCode());
                    boolean bool = JoymeAppServiceSngl.get().modifyTagDedearchives(gid, String.valueOf(archive.getArchiveId()), q, u, ArchiveRelationType.GAME_RELATION);
                    if (bool) {
                        GameResourceServiceSngl.get().removeGameArchivesByCache(gid, ArchiveRelationType.GAME_RELATION, contentType, archive.getArchiveId());
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }

        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            httpClientManager.get("http://webcache." + WebappConfig.get().getDomain() + "/json/pagestat/contenttype.do?articleid=" + archiveIdStr + "&contenttype=" + contentType.getCode(), new HttpParameter[]{}, "UTF-8");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }

        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    /**
     * 依赖于视频文章的  vip/v/的处理规则，规则不能变
     *
     * @param arctype
     * @param time
     * @param archiveId
     * @param contentType
     * @return
     */
    private String getArticleUrl(DedeArctype arctype, long time, int archiveId, ArchiveContentType contentType) {
        String host = "www." + WebappConfig.get().getDomain();
        String typedir = arctype.getTypedir();//\{cmspath\}/vip/v/zzmj
        typedir = typedir.replaceAll("\\{cmspath\\}", "");
        if (typedir.startsWith("/vip/")) {
            String key = typedir.replaceAll("/vip/", "");
            if (key.indexOf("/") > 0) {
                key = key.substring(0, key.indexOf("/"));
            }
            host = key + "." + WebappConfig.get().getDomain();
            typedir = typedir.replaceAll("/vip/" + key, "");
        }
        String namerule = arctype.getNamerule();
        // {typedir}/{Y}{M}{D}/{aid}.html
        namerule = namerule.replaceAll("\\{typedir\\}", typedir);
        String date = DateUtil.convert2String(time, DateUtil.YYYYMMDD_FORMAT);
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        namerule = namerule.replaceAll("\\{Y\\}", year);
        namerule = namerule.replaceAll("\\{M\\}", month);
        namerule = namerule.replaceAll("\\{D\\}", day);
        namerule = namerule.replaceAll("\\{aid\\}", String.valueOf(archiveId));

        int position = namerule.lastIndexOf("/");
        String[] paths = new String[2];
        if (position > 0) {
            paths[0] = namerule.substring(0, position);
            paths[1] = namerule.substring(position + 1, namerule.length());
        }
        String returnURL = "http://" + host + paths[0] + "/" + paths[1];
        return returnURL;
    }

    @ResponseBody
    @RequestMapping(value = "/getgames")
    public String getGames(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "archiveid", required = false) String archiveIdStr
    ) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(archiveIdStr)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            jsonObject.put("result", new ArrayList<TagDedearchives>());
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }

        try {
            QueryExpress queryExpress = new QueryExpress();
            if (archiveIdStr.indexOf(",") > 0) {
                queryExpress.add(QueryCriterions.in(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveIdStr.split(",")));
            } else {
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveIdStr));
            }

            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<TagDedearchives> tagDedearchivesList = JoymeAppServiceSngl.get().queryTagDedearchives(queryExpress);
            if (!CollectionUtil.isEmpty(tagDedearchivesList)) {
                Set<Long> gameIds = new HashSet<Long>();
                for (TagDedearchives tagDedearchives : tagDedearchivesList) {
                    gameIds.add(tagDedearchives.getTagid());
                }
                if (!CollectionUtil.isEmpty(gameIds)) {
                    Map<Long, GameDB> map = GameResourceServiceSngl.get().queryGameDBSet(gameIds);
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                    List<PhpArchiveGameDTO> list = new ArrayList<PhpArchiveGameDTO>();
                    for (TagDedearchives tagDedearchives : tagDedearchivesList) {
                        PhpArchiveGameDTO dto = buildGameArchiveDTO(map.get(tagDedearchives.getTagid()), tagDedearchives);
                        list.add(dto);
                    }
                    jsonObject.put("result", list);

                    if (StringUtil.isEmpty(callback)) {
                        return jsonObject.toString();
                    } else {
                        return callback + "([" + jsonObject.toString() + "])";
                    }
                }
            }
        } catch (Exception e) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new ArrayList<TagDedearchives>());
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }

        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new ArrayList<TagDedearchives>());
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "([" + jsonObject.toString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getarchives")
    public String getArchives(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "gamename", required = false) String gameName,
                              @RequestParam(value = "currentpage", required = false, defaultValue = "1") Integer cp,
                              @RequestParam(value = "pagesize", required = false, defaultValue = "30") Integer pSize) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(gameName)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            jsonObject.put("result", new ArrayList<TagDedearchives>());
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }

        Pagination page = new Pagination(pSize * cp, cp, pSize);

        try {
            Set<Long> gameIdSet = new HashSet<Long>();
            Map<Long, GameDB> map = new HashMap<Long, GameDB>();

            MongoQueryExpress queryGames = new MongoQueryExpress();
            queryGames.add(MongoQueryCriterions.like(GameDBField.GAMENAME, gameName));
//            queryGames.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            List<GameDB> gameDBList = GameResourceServiceSngl.get().queryGameDB(queryGames);
            if (gameDBList != null && !CollectionUtil.isEmpty(gameDBList)) {
                for (GameDB gameDB : gameDBList) {
                    gameIdSet.add(gameDB.getGameDbId());
                    map.put(gameDB.getGameDbId(), gameDB);
                }
            }
            if (CollectionUtil.isEmpty(gameIdSet)) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.in(TagDedearchivesFiled.TAGID, gameIdSet.toArray()));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            PageRows<Long> tagDedearchivesList = JoymeAppServiceSngl.get().queryTagDedeArchivesByDistinct(queryExpress, page);
            if (tagDedearchivesList != null && !CollectionUtil.isEmpty(tagDedearchivesList.getRows())) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("rows", tagDedearchivesList.getRows());
                result.put("page", tagDedearchivesList.getPage());
                jsonObject.put("result", result);

                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }

            }
        } catch (Exception e) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new ArrayList<TagDedearchives>());
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }

        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new ArrayList<TagDedearchives>());
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "([" + jsonObject.toString() + "])";
        }
    }

    private PhpArchiveGameDTO buildGameArchiveDTO(GameDB gameDB, TagDedearchives tagDedearchives) {
        if (gameDB == null || tagDedearchives == null) {
            return null;
        }
        PhpArchiveGameDTO phpArchiveGameDTO = new PhpArchiveGameDTO();
        phpArchiveGameDTO.setArchiveId(Long.valueOf(tagDedearchives.getDede_archives_id()));
        phpArchiveGameDTO.setArchiveTitle(tagDedearchives.getDede_archives_title());
        phpArchiveGameDTO.setGameId(gameDB.getGameDbId());
        phpArchiveGameDTO.setGameName(gameDB.getGameName());
        return phpArchiveGameDTO;
    }

    @ResponseBody
    @RequestMapping(value = "/searchgame")
    public String searchGame(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "searchtext", required = false) String searchText
    ) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(searchText)) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            jsonObject.put("result", new ArrayList<GameDB>());
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
        try {
            MongoQueryExpress queryExpress = new MongoQueryExpress();
            queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME, searchText));
            queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            List<GameDB> gameDBList = GameResourceServiceSngl.get().queryGameDB(queryExpress);
            if (gameDBList != null && !CollectionUtil.isEmpty(gameDBList)) {
                List<PhpSearchGameDTO> gameDTOList = new ArrayList<PhpSearchGameDTO>();
                for (GameDB gameDB : gameDBList) {
                    PhpSearchGameDTO gameDTO = buildGameDTO(gameDB);
                    if (gameDTO != null) {
                        gameDTOList.add(gameDTO);
                    }
                }

                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                if (gameDTOList != null && !CollectionUtil.isEmpty(gameDTOList)) {
                    jsonObject.put("result", gameDTOList);
                }
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (Exception e) {
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new ArrayList<GameDB>());
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }

        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new ArrayList<GameDB>());
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "([" + jsonObject.toString() + "])";
        }
    }

    private PhpSearchGameDTO buildGameDTO(GameDB gameDB) {
        PhpSearchGameDTO gameDTO = null;
        if (gameDB != null) {
            gameDTO = new PhpSearchGameDTO();
            gameDTO.setGameId(gameDB.getGameDbId());
            gameDTO.setGameName(gameDB.getGameName());
            gameDTO.setWikikey(StringUtil.isEmpty(gameDB.getWikiKey()) ? "" : gameDB.getWikiKey());
        }
        return gameDTO;
    }

    public static void main(String[] args) {
        String host = "www." + WebappConfig.get().getDomain();
        String typedir = "\\{cmspath\\}/vip/v/zzmj";//\{cmspath\}/vip/v/zzmj
        typedir = typedir.replace("\\{cmspath\\}", "");
        if (typedir.startsWith("/vip/")) {

            String key = typedir.replace("/vip/", "");
            if (key.indexOf("/") > 0) {
                key = key.substring(0, key.indexOf("/"));
            }
            host = key + "." + WebappConfig.get().getDomain();
            typedir = typedir.replace("/vip/" + key, "");
        }
        System.out.println("http://" + host + typedir);
    }

    @ResponseBody
    @RequestMapping(value = "/getgameplatform")
    public String getGamePlatform(HttpServletRequest request, HttpServletResponse response) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        Map<String, Set<GamePlatform>> map = new HashMap<String, Set<GamePlatform>>();
        map.put(String.valueOf(GamePlatformType.MOBILE.getCode()), new HashSet<GamePlatform>());
        map.get(String.valueOf(GamePlatformType.MOBILE.getCode())).addAll(MobilePlatform.getAll());
        map.put(String.valueOf(GamePlatformType.PC.getCode()), new HashSet<GamePlatform>());
        map.get(String.valueOf(GamePlatformType.PC.getCode())).addAll(PCPlatform.getAll());
        map.put(String.valueOf(GamePlatformType.PSP.getCode()), new HashSet<GamePlatform>());
        map.get(String.valueOf(GamePlatformType.PSP.getCode())).addAll(PSPPlatform.getAll());
        map.put(String.valueOf(GamePlatformType.TV.getCode()), new HashSet<GamePlatform>());
        map.get(String.valueOf(GamePlatformType.TV.getCode())).addAll(TVPlatform.getAll());

        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", map);
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "([" + jsonObject.toString() + "])";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/creategame")
    public String createGame(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "gamename", required = false) String gameName,
                             @RequestParam(value = "mplatform", required = false) String mPlatformStr,
                             @RequestParam(value = "pcplatform", required = false) String pcPlatformStr,
                             @RequestParam(value = "pspplatform", required = false) String pspPlatformStr,
                             @RequestParam(value = "tvplatform", required = false) String tvPlatformStr) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtil.isEmpty(gameName)) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }

            BasicDBObject query = new BasicDBObject();
            query.put(GameDBField.GAMENAME.getColumn(), gameName);
            GameDB existGame = GameResourceServiceSngl.get().getGameDB(query, false);
            if (existGame != null) {
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.GAME_COLLECTION_HAS_EXIST.getCode()));
                jsonObject.put("msg", ResultCodeConstants.GAME_COLLECTION_HAS_EXIST.getMsg());
                jsonObject.put("result", buildGameDTO(existGame));
                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }

            GameDB gameDB = new GameDB();
            gameDB.setGameName(gameName);

            Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
            if (!StringUtil.isEmpty(mPlatformStr)) {
                Set<GamePlatform> mobilePlatformSet = new HashSet<GamePlatform>();
                if (mPlatformStr.indexOf(",") > 0) {
                    String[] mPlatformArr = mPlatformStr.split(",");
                    for (String platform : mPlatformArr) {
                        MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(platform));
                        if (mobilePlatform != null) {
                            mobilePlatformSet.add(mobilePlatform);
                        }
                    }
                } else {
                    MobilePlatform mobilePlatform = MobilePlatform.getByCode(Integer.parseInt(mPlatformStr));
                    if (mobilePlatform != null) {
                        mobilePlatformSet.add(mobilePlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.MOBILE.getCode()), mobilePlatformSet);
            }

            if (!StringUtil.isEmpty(pcPlatformStr)) {
                Set<GamePlatform> pcPlatformSet = new HashSet<GamePlatform>();
                if (pcPlatformStr.indexOf(",") > 0) {
                    String[] pcPlatformArr = pcPlatformStr.split(",");
                    for (String platform : pcPlatformArr) {
                        PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(platform));
                        if (pcPlatform != null) {
                            pcPlatformSet.add(pcPlatform);
                        }
                    }
                } else {
                    PCPlatform pcPlatform = PCPlatform.getByCode(Integer.parseInt(pcPlatformStr));
                    if (pcPlatform != null) {
                        pcPlatformSet.add(pcPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PC.getCode()), pcPlatformSet);
            }

            if (!StringUtil.isEmpty(pspPlatformStr)) {
                Set<GamePlatform> pspPlatformSet = new HashSet<GamePlatform>();
                if (pspPlatformStr.indexOf(",") > 0) {
                    String[] pspPlatformArr = pspPlatformStr.split(",");
                    for (String platform : pspPlatformArr) {
                        PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(platform));
                        if (pspPlatform != null) {
                            pspPlatformSet.add(pspPlatform);
                        }
                    }
                } else {
                    PSPPlatform pspPlatform = PSPPlatform.getByCode(Integer.parseInt(pspPlatformStr));
                    if (pspPlatform != null) {
                        pspPlatformSet.add(pspPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), pspPlatformSet);
            }

            if (!StringUtil.isEmpty(tvPlatformStr)) {
                Set<GamePlatform> tvPlatformSet = new HashSet<GamePlatform>();
                if (tvPlatformStr.indexOf(",") > 0) {
                    String[] tvPlatformArr = tvPlatformStr.split(",");
                    for (String platform : tvPlatformArr) {
                        TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(platform));
                        if (tvPlatform != null) {
                            tvPlatformSet.add(tvPlatform);
                        }
                    }
                } else {
                    TVPlatform tvPlatform = TVPlatform.getByCode(Integer.parseInt(tvPlatformStr));
                    if (tvPlatform != null) {
                        tvPlatformSet.add(tvPlatform);
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), tvPlatformSet);
            }
            gameDB.setPlatformMap(platformMap);

            gameDB = GameResourceServiceSngl.get().createGameDb(gameDB);
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", buildGameDTO(gameDB));
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "([" + jsonObject.toString() + "])";
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/gameinfo")
    public String gameInfo(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "archiveid", required = false) String archiveId
    ) {
        String callback = request.getParameter("callback");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(archiveId)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
        }

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveId));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, ArchiveRelationType.GAME_RELATION.getCode()));
            queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.DESC));

            TagDedearchives tagDedearchives = JoymeAppServiceSngl.get().getTagDedearchives(queryExpress);
            if (tagDedearchives != null) {
                long gameId = tagDedearchives.getTagid();
                if (gameId > 0l) {
                    BasicDBObject basicDBObject = new BasicDBObject();
                    basicDBObject.put(GameDBField.ID.getColumn(), gameId);
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(basicDBObject, false);
                    if (gameDB != null) {
                        JSONObject result = new JSONObject();
                        GameCollectionDTO gameCollectionDTO = GameCollectionDTO.buildDTOFromGameDB(gameDB);
                        if (gameCollectionDTO != null) {
                            result.put("gameinfo", gameCollectionDTO);
                            //礼包
                            List<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByGameId(gameDB.getGameDbId());
                            if (!CollectionUtil.isEmpty(activityGoodsList)) {
                                List<ActivityDTO> giftList = new ArrayList<ActivityDTO>();
                                int loop = GIFT_PAGE_SIZE < activityGoodsList.size() ? GIFT_PAGE_SIZE : activityGoodsList.size();
                                for (int i = 0; i < loop; i++) {
                                    giftList.add(giftMarketWebLogic.buildExchangeActivityDTO(activityGoodsList.get(i)));
                                }
                                result.put("giftlist", giftList);
                            }
                            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                            jsonObject.put("result", result);
                            return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
                        }
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return ResultCodeConstants.ERROR.getJsonString(callback);
        }
        return ResultCodeConstants.SUCCESS.getJsonString(callback);
    }

}
