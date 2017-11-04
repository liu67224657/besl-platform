package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.gameres.GamedbSendPhpChannel;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-7-26
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping("/json/gameresource")
public class JsonGameResourceController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping("/relationsort")
    public String gameRelationSort(@RequestParam(value = "relationid", required = false) String relationId,
                                   @RequestParam(value = "sorttype", required = false) String sortType) {
        if (logger.isDebugEnabled()) {
            logger.debug("parameter from jsp : relationid=" + relationId + ", sorttype= " + sortType);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        try {
            if (!StringUtil.isEmpty(relationId) && !StringUtil.isEmpty(sortType)) {


                GameRelation gameRelation = GameResourceServiceSngl.get().getGameRelation(new QueryExpress().add(QueryCriterions.eq(GameRelationField.RELATIONID, Long.parseLong(relationId))));

                if (gameRelation != null) {

                    GameResource gameResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameRelation.getResourceId())));

                    List<GameRelation> list = gameResource.getGameRelationSet().getGameRelationList();

                    int index = list.indexOf(gameRelation);

                    if (index != 0 && index != list.size() - 1) {

                        GameRelation nextGameRelation = null;

                        if (sortType.equals("prev")) {
                            nextGameRelation = list.get(index - 1);
                        } else if (sortType.equals("next")) {
                            nextGameRelation = list.get(index + 1);
                        }


                        int currentGameRelationSortNum = gameRelation.getSortNum();
                        int nextGameRelationSortNum = nextGameRelation.getSortNum();
                        long currentGameRelationId = gameRelation.getRelationId();
                        long nextGameRelationId = nextGameRelation.getRelationId();


                        boolean nextToCurrent = GameResourceServiceSngl.get().modifyGameRelation(new UpdateExpress().set(GameRelationField.SORTNUM, currentGameRelationSortNum),
                                new QueryExpress().add(QueryCriterions.eq(GameRelationField.RELATIONID, nextGameRelationId)));

                        boolean currentToNext = GameResourceServiceSngl.get().modifyGameRelation(new UpdateExpress().set(GameRelationField.SORTNUM, nextGameRelationSortNum),
                                new QueryExpress().add(QueryCriterions.eq(GameRelationField.RELATIONID, currentGameRelationId)));

                        if (nextToCurrent && currentToNext) {
                            //todo 准备log对象
                            ToolsLog log = new ToolsLog();

                            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                        }

                    }

                }


            }
        } catch (ServiceException e) {
            GAlerter.lab("while sorting occurred an exception:", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/relation")
    public String relation(@RequestParam(value = "wikikey", required = false) String wikiKey,
                           @RequestParam(value = "gameid", required = false) Long gameId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        BasicDBObject queryDBObject = new BasicDBObject();
        try {
            queryDBObject.put(GameDBField.WIKIKEY.getColumn(), wikiKey);
            //查询是否有游戏已经关注该wikikey
            GameDB gameDb = GameResourceServiceSngl.get().getGameDB(queryDBObject, true);
            if (gameDb != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", -1);
                jsonObject.put("gameName", gameDb.getGameName());
                return jsonObject.toString();
            }

            boolean bool = GameResourceServiceSngl.get().updateGameDB(new BasicDBObject(GameDBField.ID.getColumn(), gameId), new BasicDBObject(GameDBField.WIKIKEY.getColumn(), wikiKey));
            if (bool) {
                WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.GAMEID, gameId)));
                if (wikiGameres != null && wikiGameres.getValidStatus().equals(ValidStatus.VALID)) {
                    //修改wikikey以后 需要禁用游戏列表里的游戏
                    AskServiceSngl.get().modifyRecommend(wikiGameres.getId(), false, new UpdateExpress().set(WikiGameresField.VALIDSTATUS, ValidStatus.INVALID.getCode()));
                }

                BasicDBObject query = new BasicDBObject();
                query.put(GameDBField.ID.getColumn(), gameId);
                GameDB gameDB = GameResourceServiceSngl.get().getGameDB(query, false);
                //send php
                gameDB.setWikiKey(wikiKey);
                GamedbSendPhpChannel channel = new GamedbSendPhpChannel();
                channel.sendPhpGamedb(gameDB);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "system.error");
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}


