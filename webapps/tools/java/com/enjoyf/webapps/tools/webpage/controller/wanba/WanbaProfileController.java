package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.anime.GameClientTagType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/26
 */
@Controller
@RequestMapping("/wanba/profile")
public class WanbaProfileController {
    static List<String> appkeyList = new ArrayList<String>();

    static {
        appkeyList.add("default");
        appkeyList.add("3iiv7VWfx84pmHgCUqRwun");
    }

    @RequestMapping("/verifylist")
    public ModelAndView list(@RequestParam(value = "text", required = false) String text,
                             @RequestParam(value = "verifyid", required = false) String verifyid,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            //QueryExpress queryExpress = new QueryExpress();
            Long levelId = null;
            if (!StringUtil.isEmpty(text)) {
               // queryExpress.add(QueryCriterions.like(VerifyProfileField.NICK, "%" + text + "%"));
                mapMessage.put("text", text);
            }
            if (!StringUtil.isEmpty(verifyid)) {
                //queryExpress.add(QueryCriterions.eq(VerifyProfileField.VERIFYTYPE, Long.parseLong(verifyid)));
                levelId = Long.parseLong(verifyid);
                mapMessage.put("verifyid", verifyid);
            }
            if (!StringUtil.isEmpty(appkey)) {
               // queryExpress.add(QueryCriterions.eq(VerifyProfileField.APPKEY, appkey));
                mapMessage.put("appkey", appkey);
            }
            //todo 微服务改造
            //PageRows<VerifyProfile> wanbaProfilePageRows = UserCenterServiceSngl.get().queryVerifyProfile(queryExpress, page);
            PageRows<VerifyProfile> wanbaProfilePageRows = UserCenterServiceSngl.get().queryPlayers(text,appkey,levelId,page);
            mapMessage.put("list", wanbaProfilePageRows.getRows());
            mapMessage.put("page", wanbaProfilePageRows.getPage());

            List<Verify> wanbaVerifyList = UserCenterServiceSngl.get().queryVerify(new QueryExpress().add(QueryCriterions.eq(VerifyField.VALID_STATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("wanbaVerifyList", wanbaVerifyList);

            mapMessage.put("appkeyList", appkeyList);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/profile/verifylist", mapMessage);
    }


    //////////认证用户//////////////
    @RequestMapping("/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<Verify> wanbaVerifyList = UserCenterServiceSngl.get().queryVerify(new QueryExpress().add(QueryCriterions.eq(VerifyField.VALID_STATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("wanbaVerifyList", wanbaVerifyList);
            mapMessage.put("appkeyList", appkeyList);


            mapMessage.put("animeTagList", getAnimeTagList());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/profile/createpage", mapMessage);
    }

    @RequestMapping("/create")
    public ModelAndView verifyProfile(@RequestParam(value = "nick", required = false) String nick,
                                      @RequestParam(value = "verifytype", required = false) String verifytype,
                                      @RequestParam(value = "point", required = false) String point,
                                      @RequestParam(value = "verifydesc", required = false) String verifydesc,
                                      @RequestParam(value = "appkey", required = false) String appkey,
                                      @RequestParam(value = "tagid", required = false) String tagid) {

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
//todo
//        AskServiceSngl.get().verifyWanbaProfile();

            VerifyProfile wanbaProfile = new VerifyProfile();
            wanbaProfile.setAskPoint(StringUtil.isEmpty(point) ? 0 : Integer.parseInt(point));
            wanbaProfile.setDescription(verifydesc);
            wanbaProfile.setVerifyType(Long.parseLong(verifytype));
            wanbaProfile.setNick(nick);
            wanbaProfile.setProfileId(profile.getProfileId());
            wanbaProfile.setAppkey(appkey);
            boolean bool = UserCenterServiceSngl.get().verifyProfile(wanbaProfile, 0l);
            if (bool) {
                WanbaProfileSum sum = AskServiceSngl.get().getWanProfileSum(wanbaProfile.getProfileId());
                if (sum == null) {
                    sum = new WanbaProfileSum();
                    sum.setProfileId(wanbaProfile.getProfileId());
                    sum.setAnswerSum(0);
                    sum.setAwardPoint(0);
                    sum.setFavoriteSum(0);
                    sum.setQuestionFollowSum(0);
                    AskServiceSngl.get().insertWanbaProfileSum(sum);
                }

                //绑定游戏
                if (!StringUtil.isEmpty(tagid)) {
                    String tagarr[] = tagid.split(",");
                    for (String tag : tagarr) {
                        //add
                        if (!StringUtil.isEmpty(tag)) {
                            UserCenterServiceSngl.get().addVerifyProfileTagsByProfileId(Long.valueOf(tag), wanbaProfile.getProfileId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/wanba/profile/verifylist");
    }


    @RequestMapping("/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "profileid", required = false) String profileId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<Verify> wanbaVerifyList = UserCenterServiceSngl.get().queryVerify(new QueryExpress().add(QueryCriterions.eq(VerifyField.VALID_STATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("wanbaVerifyList", wanbaVerifyList);

            VerifyProfile profile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);
            mapMessage.put("wanbaprofile", profile);
            mapMessage.put("appkeyList", appkeyList);

            mapMessage.put("animeTagList", getAnimeTagList());


            Set<String> pidSet = UserCenterServiceSngl.get().getVerifyProfileTagsByProfileId(profileId);
            mapMessage.put("pidSet", pidSet);
            mapMessage.put("oldtagidSet", CollectionUtil.isEmpty(pidSet) ? "" : StringUtils.join(pidSet.toArray(), ","));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/profile/modifypage", mapMessage);
    }

    @RequestMapping("/modify")
    public ModelAndView modify(@RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "verifytype", required = false) String verifytype,
                               @RequestParam(value = "point", required = false) String point,
                               @RequestParam(value = "verifydesc", required = false) String verifydesc,
                               @RequestParam(value = "tagid", required = false) String tagid,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "oldtagidSet", required = false) String oldtagidSet) {

        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(VerifyProfileField.DESCRIPTION, verifydesc);
            updateExpress.set(VerifyProfileField.VERIFYTYPE, Long.parseLong(verifytype));
            updateExpress.set(VerifyProfileField.POINT, StringUtil.isEmpty(point) ? 0 : Integer.parseInt(point));
            updateExpress.set(VerifyProfileField.APPKEY, appkey);
            UserCenterServiceSngl.get().modifyVerifyProfile(profileId, updateExpress);
            //绑定游戏
            if (!StringUtil.isEmpty(oldtagidSet)) {
                Set<String> logtagidSet = new HashSet<String>();
                String loldtagarr[] = oldtagidSet.split(",");
                for (String tag : loldtagarr) {
                    if (!StringUtil.isEmpty(tag)) {
                        logtagidSet.add(tag);
                    }
                }

                //delete
                for (String tagId : logtagidSet) {
                    UserCenterServiceSngl.get().removeVerifyProfileTagsByProfileId(Long.valueOf(tagId), profileId);
                }
            }

            if (!StringUtil.isEmpty(tagid)) {
                Set<String> tagidSet = new HashSet<String>();
                String tagarr[] = tagid.split(",");
                for (String tag : tagarr) {
                    if (!StringUtil.isEmpty(tag)) {
                        tagidSet.add(tag);
                    }
                }
                VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);
                //add
                for (String tagId : tagidSet) {
                    //UserCenterServiceSngl.get().verifyProfile(wanbaProfile, Long.valueOf(tagId));
                    UserCenterServiceSngl.get().addVerifyProfileTagsByProfileId(Long.valueOf(tagId), wanbaProfile.getProfileId());
                }
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }


        return new ModelAndView("redirect:/wanba/profile/verifylist");
    }

    ///////////////达人列表//////////////////////


    ///////////////认证类型//////////////////


    @RequestMapping(value = "/checknick")
    @ResponseBody
    public String checkNick(@RequestParam(value = "nick", required = false) String nick) {
        JSONObject jsonObject = new JSONObject();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (profile == null) {
                jsonObject.put("rs", "-1");
                return jsonObject.toString();
            }

            VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            if (wanbaProfile != null) {
                jsonObject.put("rs", "-2");
                return jsonObject.toString();
            }


            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            e.printStackTrace();
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/virtualchecknick")
    @ResponseBody
    public String virtualchecknick(@RequestParam(value = "nick", required = false) String nick) {
        JSONObject jsonObject = new JSONObject();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (profile == null) {
                jsonObject.put("rs", "-1");
                return jsonObject.toString();
            }
            WanbaProfileClassify wanbaProfileClassify = AskServiceSngl.get().getWanbaProfileClassify(AskUtil.getWanbaProfileClassifyId(profile.getProfileId(),
                    WanbaProfileClassifyType.WANBA_ASK_VIRTUAL));
            if (wanbaProfileClassify != null) {
                jsonObject.put("rs", "-3");
                return jsonObject.toString();
            }


            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (ServiceException e) {
            e.printStackTrace();
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    private List<AnimeTag> getAnimeTagList() {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
        queryExpress.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
        //queryExpress.add(QueryCriterions.gt(AnimeTagField.TAG_ID, 0L));
        List<AnimeTag> returnList = new ArrayList<AnimeTag>();
        try {
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(queryExpress);
            for (AnimeTag tag : animeTagList) {
                if (tag.getTag_id() < 0) {
                    continue;
                }

                if (tag.getPicjson() != null && tag.getPicjson().getType().equals(GameClientTagType.DEFAULT.getCode())) {
                    returnList.add(tag);
                }

            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return returnList;
    }
}
