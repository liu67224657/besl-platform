package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.SocialMessageType;
import com.enjoyf.platform.service.message.WanbaMessageType;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.ProfileRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.gamedb.GameDBSimpleDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.api.MessageListDTO;
import com.enjoyf.webapps.joyme.dto.joymeclient.GameMessageDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.ProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.gamedb.GameDbWebLogic;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.socialclient.SocialClientWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2015/3/31.
 */

@Controller
@RequestMapping("/joymeapp/gameclient/api")
public class GameClientApiMessageController extends AbstractGameClientBaseController {

    @Resource(name = "gameDbWebLogic")
    private GameDbWebLogic gameDbWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    @Resource(name = "socialClientWebLogic")
    private SocialClientWebLogic socialClientWebLogic;

    //小红点
    @ResponseBody
    @RequestMapping(value = "/message")
    public String message(HttpServletRequest request) {
        String uidParam = HTTPUtil.getParam(request, "uid");
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(platform)) {
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
        JSONObject returnObj = new JSONObject();
        JSONObject result = new JSONObject();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            //liked
            GameMessageDTO liked = new GameMessageDTO();
            liked.setSid(profile.getUid() + "");
            liked.setType("2");
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
//            if (profileSum != null && profileSum.getModifyTimeJson() != null) {
//                liked.setMessagetime(profileSum.getModifyTimeJson().getLikedprofileModifyTime() + "");
//                liked.setText(profileSum.getLikedSum() + "");
//            }
            result.put("liked", liked);

            //我的帖子
            GameMessageDTO mycard = new GameMessageDTO();
            mycard.setSid(profile.getUid() + "");
            mycard.setType("1");
//            if (profileSum != null && profileSum.getModifyTimeJson() != null) {
//                mycard.setMessagetime(profileSum.getModifyTimeJson().getMiyouModifyTime() + "");
//                mycard.setText("");
//            }
            result.put("mycard", mycard);

            GameMessageDTO miyoucard = new GameMessageDTO();
            miyoucard.setSid(profile.getUid() + "");
            miyoucard.setType("1");
//            if (profileSum != null && profileSum.getModifyTimeJson() != null) {
//                miyoucard.setMessagetime(profileSum.getModifyTimeJson().getMiyouModifyTime() + "");
//                miyoucard.setText("");
//            }
            result.put("miyouquan", miyoucard);


            Set<Long> longSet = new HashSet<Long>();
            PageRows<ObjectRelation> gameRelationList = SocialServiceSngl.get().queryObjectRelation(profile.getProfileId(), ObjectRelationType.GAME, new Pagination(1 * 6, 1, 6));
            for (ObjectRelation objectRelation : gameRelationList.getRows()) {
                longSet.add(Long.valueOf(objectRelation.getObjectId()));
            }
            Map<String, GameDBSimpleDTO> gameSimpleDTOMap = gameDbWebLogic.queryGameDTOByGameDBIds(longSet, null, "");


            if (!CollectionUtil.isEmpty(gameSimpleDTOMap)) {

                GameMessageDTO mydto = new GameMessageDTO();
                mydto.setSid("");
                mydto.setType("0");
                mydto.setMessagetime("0");
                mydto.setText("");

                //game
                List<GameMessageDTO> gamelist = new ArrayList<GameMessageDTO>();
                GameDBSimpleDTO simpleDTO = null;
                for (GameDBSimpleDTO gameDBSimpleDTO : gameSimpleDTOMap.values()) {
                    GameMessageDTO dto = new GameMessageDTO();
                    dto.setSid(gameDBSimpleDTO.getGameid());
                    simpleDTO = gameSimpleDTOMap.get(gameDBSimpleDTO.getGameid());
                    if (simpleDTO != null) {
                        dto.setType(simpleDTO.getRedMessageType());
                        dto.setText(simpleDTO.getRedMessageText());
                    }

                    //排序     0_无< 1_小红点 <2_数字 <3_自定义汉字< 4_攻 <5_新 <6_活 <7_礼
//                    if (Integer.valueOf(mydto.getType()) <= Integer.valueOf(simpleDTO.getRedMessageType())) {
//                        mydto.setType(simpleDTO.getRedMessageType());
//                        mydto.setText(simpleDTO.getRedMessageText());
//                        mydto.setMessagetime(gameDBSimpleDTO.getModifytime() + "");
//                    }

                    if (Long.valueOf(mydto.getMessagetime()) < simpleDTO.getRedMessageTime()) {
                        mydto.setType(simpleDTO.getRedMessageType());
                        mydto.setText(simpleDTO.getRedMessageText());
                        mydto.setMessagetime(gameDBSimpleDTO.getRedMessageTime() + "");
                    }


                    dto.setMessagetime(gameDBSimpleDTO.getRedMessageTime() + "");
                    gamelist.add(dto);
                }
                result.put(WanbaMessageType.MY.getName(), mydto);
                result.put("game", gamelist);
            }

            PageRows<ProfileRelation> likeRelaationList = SocialServiceSngl.get().querySrcRelation(profile.getProfileId(), ObjectRelationType.PROFILE, new Pagination(1 * 6, 1, 6));
            PageRows<ProfileRelation> likedReltionList = SocialServiceSngl.get().queryDestRelation(profile.getProfileId(), ObjectRelationType.PROFILE, new Pagination(1 * 6, 1, 6));
            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : likeRelaationList.getRows()) {
                prifileIdSet.add(relation.getDestProfileId());
            }
            for (ProfileRelation relation : likedReltionList.getRows()) {
                prifileIdSet.add(relation.getSrcProfileId());
            }

            Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);

            if (!CollectionUtil.isEmpty(likeRelaationList.getRows())) {
                ProfileDTO profileDTO = null;
                List<GameMessageDTO> likeRelation = new ArrayList<GameMessageDTO>();
                for (ProfileRelation relation : likeRelaationList.getRows()) {
                    GameMessageDTO dto = new GameMessageDTO();
                    profileDTO = profileDTOMap.get(relation.getDestProfileId());
                    if (profileDTO == null) {
                        continue;
                    }
                    dto.setSid(profileDTO.getUid() + "");
                    dto.setMessagetime(relation.getModifyTime().getTime() + "");
                    dto.setType("");
                    dto.setText((new Random().nextInt(10) + 1) + "");
                    likeRelation.add(dto);
                }
                result.put("likerelation", likeRelation);
            }

            if (!CollectionUtil.isEmpty(likedReltionList.getRows())) {
                ProfileDTO profileDTO = null;
                List<GameMessageDTO> likedRelation = new ArrayList<GameMessageDTO>();
                for (ProfileRelation relation : likedReltionList.getRows()) {
                    GameMessageDTO dto = new GameMessageDTO();
                    profileDTO = profileDTOMap.get(relation.getSrcProfileId());
                    if (profileDTO == null) {
                        continue;
                    }
                    dto.setSid(profileDTO.getUid() + "");
                    dto.setMessagetime(relation.getModifyTime().getTime() + "");
                    dto.setType("");
                    dto.setText((new Random().nextInt(10) + 1) + "");
                    likedRelation.add(dto);
                }
                result.put("likedrelation", likedRelation);
            }


            //other
            String str = MessageServiceSngl.get().getRedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform);
            str = updateTaskTime(str, platform);
            result.put("other", str);
            returnObj.put("result", result);
            returnObj.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            returnObj.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return returnObj.toString();
    }


    private String updateTaskTime(String str, String platform) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(str);
        if (jsonObject != null) {
            return str;
        }
        Object obj = jsonObject.get(WanbaMessageType.TASK.getName());
        if (obj != null) {
            Map<String, GameMessageDTO> map = GameMessageDTO.topMap(str);
            JSONObject taskobj = JSONObject.fromObject(obj.toString());
            if (!DateUtil.isToday(new Date(Long.valueOf(taskobj.get("messagetime").toString())))) {
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(taskobj.get("sid") + "");
                dto.setType(taskobj.get("type") + "");
                dto.setText(taskobj.get("text") + "");
                dto.setMessagetime(System.currentTimeMillis() + "");
                map.put(WanbaMessageType.TASK.getName(), dto);
                str = JSONSerializer.toJSON(map).toString();
                MessageServiceSngl.get().setTORedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform, str);
            }
        }
        return str;
    }


    //玩霸2.2消息列表
    @ResponseBody
    @RequestMapping(value = "/messagelist")
    public String messagelist(HttpServletRequest request,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                              @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount) {
        JSONObject returnObj = new JSONObject();
        String uidParam = HTTPUtil.getParam(request, "uid");
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(platform)) {
            returnObj.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            returnObj.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            returnObj.put("result", new Object());
            return returnObj.toString();
        }
        try {
            int pNum = Integer.valueOf(pnum);
            int pageSize = Integer.valueOf(pcount);
            Pagination pagination = new Pagination(pNum * pageSize, pNum, pageSize);
            Map resultMap = new HashMap();


            //查询玩霸消息列表
            PageRows<MessageListDTO> returnPage = new PageRows<MessageListDTO>();
            returnPage = socialClientWebLogic.queryWanbaSocialMessageList(uidParam, SocialMessageType.WANBA_MESSAGE_LIST, pagination);


            List<MessageListDTO> list = new ArrayList<MessageListDTO>();
            if (!CollectionUtil.isEmpty(returnPage.getRows())) {
                for (MessageListDTO dto : returnPage.getRows()) {
                    list.add(dto);
                }
            }

            resultMap.put("rows", list);
            resultMap.put("page", returnPage.getPage());
            returnObj.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            returnObj.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            returnObj.put("result", resultMap);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            returnObj.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
            returnObj.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            returnObj.put("result", new Object());
            return returnObj.toString();
        }
        return returnObj.toString();
    }


}
