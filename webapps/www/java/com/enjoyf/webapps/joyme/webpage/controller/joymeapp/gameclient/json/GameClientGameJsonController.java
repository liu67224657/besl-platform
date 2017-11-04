package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.ActivityGoodsField;
import com.enjoyf.platform.service.point.GiftReserve;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationField;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSumField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.gamedb.GameClientGameNewsDTO;
import com.enjoyf.webapps.joyme.weblogic.gamedb.GameDbWebLogic;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/14
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/json/game/")
public class GameClientGameJsonController extends AbstractGameClientBaseController {

    @Resource(name = "gameDbWebLogic")
    private GameDbWebLogic gameDbWebLogic;

    @Resource
    private GiftMarketWebLogic giftMarketWebLogic;

    //正在玩api
    @ResponseBody
    @RequestMapping(value = "/batchlike")
    public String batchLikeGame(HttpServletRequest request) {

        String gameParam = request.getParameter("gids");
        if (gameParam == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String[] gameStringArray = gameParam.split(",");

        String platFormString = HTTPUtil.getParam(request, "platform");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String appKey = HTTPUtil.getParam(request, "appkey");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(appKey) || StringUtil.isEmpty(platFormString)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }

        if (appPlatform == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            Set<Long> idSet = new HashSet<Long>();
            for (String gameId : gameStringArray) {
                if (!StringUtil.isEmpty(gameId)) {
                    idSet.add(Long.valueOf(gameId));
                }
            }
            int sum = idSet.size();
            //已经喜欢的
            QueryExpress queryExpressForGameIds = new QueryExpress();
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.PROFILEID, profile.getProfileId()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, ObjectRelationType.GAME.getCode()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.PROFILEKEY, profile.getProfileKey()));
            queryExpressForGameIds.add(QuerySort.add(ObjectRelationField.MODIFYTIME, QuerySortOrder.ASC));
            List<ObjectRelation> likedList = SocialServiceSngl.get().queryObjectRelations(queryExpressForGameIds);
            //包含以喜欢，不再喜欢，不包含的话，取消
            for (ObjectRelation objectRelation : likedList) {
                if (idSet.contains(Long.valueOf(objectRelation.getObjectId()))) {
                    idSet.remove(Long.valueOf(objectRelation.getObjectId()));
                } else {
                    SocialServiceSngl.get().removeObjectRelation(profile.getProfileId(), objectRelation.getObjectId(), ObjectRelationType.GAME, profile.getProfileKey());
                }
            }
            //
            if (idSet.size() > 0) {         //大于0才操作
                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(idSet);
                Date date = new Date();
                String gameName = "";
                for (GameDB gameDB : gameDBMap.values()) {
                    try {
                        ObjectRelation objectRelation = new ObjectRelation();
                        objectRelation.setProfileKey(profile.getProfileKey());
                        objectRelation.setType(ObjectRelationType.GAME);
                        objectRelation.setModifyIp(getIp(request));
                        objectRelation.setModifyTime(new Date());
                        objectRelation.setObjectId(String.valueOf(gameDB.getGameDbId()));
                        objectRelation.setStatus(IntValidStatus.VALID);
                        objectRelation.setProfileId(profile.getProfileId());
                        objectRelation.setProfileKey(profile.getProfileKey());
                        SocialServiceSngl.get().saveObjectRelation(objectRelation);
                        gameName = gameDB.getGameName();

                        sendOutTask(profile, appPlatform, getIp(request), appKey, date, String.valueOf(gameDB.getGameDbId()), clientId);
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                    }
                }
//                UserCenterServiceSngl.get().modifyProfileSum(new UpdateExpress().set(ProfileSumField.LIKEGAME, sum).set(ProfileSumField.PLAYINGGAME, gameName), profile.getProfileId());
            }
            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    //正在玩api
    @ResponseBody
    @RequestMapping(value = "/like")
    public String likeGame(HttpServletRequest request) {
        String gameParam = request.getParameter("gid");
        String platFormString = HTTPUtil.getParam(request, "platform");
        String uidParam = HTTPUtil.getParam(request, "uid");
        String appKey = HTTPUtil.getParam(request, "appkey");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统

        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(appKey) || StringUtil.isEmpty(platFormString)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long gameid = Long.parseLong(gameParam);
        if (gameid < 0) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }

        if (appPlatform == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Set<Long> idSet = new HashSet<Long>();
            idSet.add(gameid);
            //已经喜欢的
            QueryExpress queryExpressForGameIds = new QueryExpress();
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.PROFILEID, profile.getProfileId()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, ObjectRelationType.GAME.getCode()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode()));
            queryExpressForGameIds.add(QueryCriterions.eq(ObjectRelationField.PROFILEKEY, profile.getProfileKey()));
            queryExpressForGameIds.add(QuerySort.add(ObjectRelationField.MODIFYTIME, QuerySortOrder.ASC));
            List<ObjectRelation> likedList = SocialServiceSngl.get().queryObjectRelations(queryExpressForGameIds);
            for (ObjectRelation objectRelation : likedList) {
                idSet.add(Long.valueOf(objectRelation.getObjectId()));
            }
            //较早的取消
            if (idSet.size() > MAX_LIKE_GAME) {
                int j = (idSet.size() - MAX_LIKE_GAME) < likedList.size() ? (idSet.size() - MAX_LIKE_GAME) : likedList.size();
                for (int i = 0; i < j; i++) {
                    ObjectRelation removeRelation = likedList.get(i);
                    if (removeRelation != null) {
                        SocialServiceSngl.get().removeObjectRelation(profile.getProfileId(), removeRelation.getObjectId(), ObjectRelationType.GAME, profile.getProfileKey());
                        idSet.remove(Long.valueOf(removeRelation.getObjectId()));
                    }
                }
            }
            //
            if (idSet.size() > 0) {         //大于0才操作
                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(idSet);
                Date date = new Date();
                String gameName = "";
                int likeGameSum = 0;
                for (GameDB gameDB : gameDBMap.values()) {
                    try {
                        ObjectRelation objectRelation = new ObjectRelation();
                        objectRelation.setProfileKey(profile.getProfileKey());
                        objectRelation.setType(ObjectRelationType.GAME);
                        objectRelation.setModifyIp(getIp(request));
                        objectRelation.setModifyTime(new Date());
                        objectRelation.setObjectId(String.valueOf(gameDB.getGameDbId()));
                        objectRelation.setStatus(IntValidStatus.VALID);
                        objectRelation.setProfileId(profile.getProfileId());
                        objectRelation.setProfileKey(profile.getProfileKey());
                        SocialServiceSngl.get().saveObjectRelation(objectRelation);
                        likeGameSum += 1;
                        gameName = gameDB.getGameName();

                        sendOutTask(profile, appPlatform, getIp(request), appKey, date, String.valueOf(gameDB.getGameDbId()), clientId);
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                    }
                }
//                UserCenterServiceSngl.get().modifyProfileSum(new UpdateExpress().set(ProfileSumField.LIKEGAME, likeGameSum).set(ProfileSumField.PLAYINGGAME, gameName), profile.getProfileId());
            }
            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/tab/news/list")
    public String tabNewsList(HttpServletRequest request,
                              @RequestParam(value = "gameid", required = false) String gameId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

        try {
            if (StringUtil.isEmpty(gameId)) {
                return jsonObject.toString();
            }

            Pagination pagination = getPaginationbyRequest(request);
            PageRows<GameClientGameNewsDTO> clientLineItems = gameDbWebLogic.buildGameNewsDTO(gameId, pagination);
            Map<String, List<GameClientGameNewsDTO>> dataMap = new HashMap<String, List<GameClientGameNewsDTO>>();
            for (GameClientGameNewsDTO dto : clientLineItems.getRows()) {
                String date = String.valueOf(DateUtil.ignoreTime(dto.getCreatetime()).getTime());
                if (!dataMap.containsKey(date)) {
                    dataMap.put(date, new ArrayList<GameClientGameNewsDTO>());
                }
                dataMap.get(date).add(dto);
            }

            Map map = new HashMap();
            map.put("page", clientLineItems.getPage());
            map.put("rows", dataMap);

            jsonObject.put("result", map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            //todo
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/tab/giftlist")
    public String giftList(HttpServletRequest request,
                           @RequestParam(value = "gameid", required = false) String gameId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

        long gameDbID = -1l;
        try {
            gameDbID = Long.parseLong(gameId);
        } catch (Exception e) {
        }
        if (gameDbID < 0l) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        Pagination pagination = getPaginationbyRequest(request);
        try {
            String uidParam = HTTPUtil.getParam(request, "uid");
            long uid = -1l;
            try {
                uid = Long.parseLong(uidParam);
            } catch (Exception e) {
            }

            Profile profile = null;
            if (uid > 0l) {

                try {
                    profile = UserCenterServiceSngl.get().getProfileByUid(uid);
                } catch (ServiceException e) {
                }
            }
            String appkey = HTTPUtil.getParam(request, "uid");

            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.GAME_DB_ID, gameDbID))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);

            //build status 处理异常不影响外部访问
            try {
                if (!StringUtil.isEmpty(appkey) && profile != null
                        && goodsActivityPageRows != null && !CollectionUtils.isEmpty(goodsActivityPageRows.getRows())) {
                    Set<Long> aidS = new HashSet<Long>();
                    for (ActivityDTO dto : goodsActivityPageRows.getRows()) {
                        aidS.add(dto.getGid());
                    }

                    Map<Long, GiftReserve> reserveMap = PointServiceSngl.get().checkGiftReserve(profile.getProfileId(), appkey, aidS);
                    for (ActivityDTO dto : goodsActivityPageRows.getRows()) {
                        if (reserveMap.containsKey(dto.getGid())) {
                            dto.setReserveType(2);
                        }
                    }
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            }

            Map map = new HashMap();
            map.put("rows", goodsActivityPageRows.getRows());
            map.put("page", goodsActivityPageRows.getPage());

            jsonObject.put("result", map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            //todo
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


}
