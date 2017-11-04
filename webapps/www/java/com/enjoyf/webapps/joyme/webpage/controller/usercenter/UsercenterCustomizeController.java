package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.point.GiftLottery;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserLotteryLog;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.CustomizeWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2017-1-9 0009.
 */
@Controller
@RequestMapping(value = "/usercenter/customize")
public class UsercenterCustomizeController extends BaseRestSpringController {

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    @Resource(name = "customizeWebLogic")
    private CustomizeWebLogic customizeWebLogic;

    //个性换装
    @RequestMapping(value = "/skin")
    public ModelAndView skin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/skin", mapMessage);
            }

            String profileId = userSession.getProfileId();
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                GAlerter.lab(this.getClass().getName() + " skin profile is null profileId=" + profileId);
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profileId);
            int giftBoxNum = PointServiceSngl.get().getGiftBoxNum(profileId);
            Map<Long, UserLotteryLog> userLotteryLogMap = PointServiceSngl.get().queryUserLotteryLogByCache(profileId);

            //todo 用户激励体系删除
            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profileId);
            mapMessage.put("type", "cloth");
            mapMessage.put("profile", profile);
            mapMessage.put("userPoint", userPoint);
            mapMessage.put("giftBoxNum", giftBoxNum);
            mapMessage.put("userLotteryLogMap", userLotteryLogMap);
            mapMessage.put("chooseMap", chooseMap);


            List<GiftLottery> giftLotteries = PointServiceSngl.get().queryGiftLotteryByCache();
            mapMessage.put("giftLotterys", giftLotteries);

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            //该用户获得的道具
            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/usercenter/skin", mapMessage);

    }


    //隐私
    @RequestMapping(value = "/privacy")
    public ModelAndView privacy(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profileid = userSession.getProfileId();
            UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(profileid);
            if (userPrivacy != null) {
                mapMessage.put("privacyAlarm", userPrivacy.getAlarmSetting());
                mapMessage.put("privacyFunction", userPrivacy.getFunctionSetting());
            } else {
                UserPrivacyPrivacyAlarm privacyAlarmDTO = new UserPrivacyPrivacyAlarm();
                privacyAlarmDTO.setUserat("1");
                privacyAlarmDTO.setSysteminfo("1");
                privacyAlarmDTO.setFollow("1");
                privacyAlarmDTO.setComment("1");
                privacyAlarmDTO.setAgreement("1");

                UserPrivacyFunction privacyFunctionDTO = new UserPrivacyFunction();
                privacyFunctionDTO.setAcceptFollow("1");
                privacyFunctionDTO.setChat("1");
                mapMessage.put("privacyAlarm", privacyAlarmDTO);
                mapMessage.put("privacyFunction", privacyFunctionDTO);
            }
            mapMessage.put("type", "secret");
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
            mapMessage.put("privacy", userPrivacy);
            mapMessage.put("profile", profile);


            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/views/jsp/usercenter/privacy", mapMessage);
    }


    //用户隐私保存接口
    @RequestMapping(value = "/privacysave")
    public ModelAndView privacysave(@RequestParam(value = "profileid", required = false, defaultValue = "0") String profileid,
                                    @RequestParam(value = "userat", required = false, defaultValue = "0") String userat,
                                    @RequestParam(value = "comment", required = false, defaultValue = "0") String comment,
                                    @RequestParam(value = "agreement", required = false, defaultValue = "0") String agreement,
                                    @RequestParam(value = "follow", required = false, defaultValue = "0") String follow,
                                    @RequestParam(value = "systeminfo", required = false, defaultValue = "0") String systeminfo,
                                    @RequestParam(value = "acceptfollow", required = false, defaultValue = "0") String acceptfollow,
                                    @RequestParam(value = "chat", required = false, defaultValue = "0") String chat, HttpServletRequest request) {
        try {
            if (!StringUtil.isEmpty(profileid)) {

                String updateIp = request.getRemoteHost();

                UserPrivacyPrivacyAlarm privacyAlarm = new UserPrivacyPrivacyAlarm();
                privacyAlarm.setAgreement(agreement);
                privacyAlarm.setComment(comment);
                privacyAlarm.setFollow(follow);
                privacyAlarm.setSysteminfo(systeminfo);
                privacyAlarm.setUserat(userat);

                UserPrivacyFunction privacyFunction = new UserPrivacyFunction();
                privacyFunction.setAcceptFollow(acceptfollow);
                privacyFunction.setChat(chat);
//todo 微服务新增token
                UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(profileid);

                if (userPrivacy == null) {
                    userPrivacy = new UserPrivacy();
                    userPrivacy.setFunctionSetting(privacyFunction);
                    userPrivacy.setAlarmSetting(privacyAlarm);
                    userPrivacy.setCreateip(updateIp);
                    userPrivacy.setCreatetime(new Timestamp(new Date().getTime()));
                    userPrivacy.setProfileId(profileid);
                    userPrivacy.setUpdateip(updateIp);
                    userPrivacy.setUpdatetime(new Timestamp(new Date().getTime()));
                    UserCenterServiceSngl.get().addUserPrivacy(userPrivacy);
                } else {
                    UpdateExpress updateExpress = new UpdateExpress().set(UserPrivacyField.ALARMSETTING, privacyAlarm.toJson())
                            .set(UserPrivacyField.FUNCTIONSETTING, privacyFunction.toJson())
                            .set(UserPrivacyField.UPDATEIP, updateIp)
                            .set(UserPrivacyField.UPDATETIME, new Timestamp((new Date()).getTime()));
                    UserCenterServiceSngl.get().modifyUserPrivacy(profileid, updateExpress);
                }

                //调用php隐私接口设置,
                customizeWebLogic.privacysave(profileid, chat, 2, "");
            }

        } catch (Exception e) {
            e.printStackTrace();//todo
        }
        return new ModelAndView("redirect:/usercenter/customize/privacy");
    }


    //修改头像
    @RequestMapping(value = "/modifyhead")
    public ModelAndView upload(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profileid = userSession.getProfileId();
            //String profileid = "91da4e84629fba26dd4b9bc66d25ba50";
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);

            mapMessage.put("type", "portrait");
            mapMessage.put("profile", profile);

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/usercenter/modifyhead", mapMessage);
    }


    //我的信息
    @RequestMapping(value = "/personinfo")
    public ModelAndView index(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
                mapMessage.put("isnotlogin", "true");
                return new ModelAndView("/views/jsp/usercenter/person-center", mapMessage);
            }
            String profiileId = userSession.getProfileId();
            //String profiileId = "91da4e84629fba26dd4b9bc66d25ba50";
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profiileId);
            if (profile == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            if (profile.getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                mapMessage.put("modifynick", "true");//是否修改过昵称
            }

            Map<Integer, Region> regionMap = SysCommCache.get().getRegionMap();
            List<Region> regionList = Lists.newArrayList();
            for (Region region : regionMap.values()) {
                if (region.getRegionLevel() == 1) {
                    regionList.add(region);
                }
            }
            //todo 微服务改造新增 加入token
            String token = UserCenterCookieUtil.getToken(request);
            UserCenterServiceSngl.get().setOauthToken(token);

            UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(profile.getUno());

            mapMessage.put("userAccount", userAccount);
            mapMessage.put("regionList", regionList);
            mapMessage.put("profile", profile);
            mapMessage.put("type", "message");

            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
            //该用户获得的道具 用户激励体系
//            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(profile.getProfileId());
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());

            UserinfoDTO userinfoDTO = userCenterWebLogic.buildUserInfoDTO(profile, verifyProfile,profileSum,true);
            mapMessage.put("userinfoDTO", userinfoDTO);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/usercenter/personinfo", mapMessage);
    }

    @RequestMapping(value = "/person/save")
    public ModelAndView save(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {


            String profileId = request.getParameter("profileId");

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            String sex = request.getParameter("sex");
            String nick = request.getParameter("nick");
            String description = request.getParameter("description");
            String birthday = request.getParameter("birthday");
            String hobby = request.getParameter("hobby");
            String provinceId = request.getParameter("provinceId");
            String address = request.getParameter("address");
            String realName = request.getParameter("realName");
            String mobile = request.getParameter("mobile");


            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileField.BIRTHDAY, birthday)
                    .set(ProfileField.SEX, sex)
                    .set(ProfileField.DESCRIPTION, description)
                    .set(ProfileField.HOBBY, hobby)
                    .set(ProfileField.PROVINCEID, Integer.parseInt(provinceId))
                            //.set(ProfileField.MOBILE, mobile)
                    .set(ProfileField.REALNAME, realName);
            if (!StringUtil.isEmpty(nick)) {
                updateExpress.set(ProfileField.NICK, nick);
            }
            //todo 微服务改造新增 加入token
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));

            boolean bval = UserCenterServiceSngl.get().modifyProfile(updateExpress, profileId);
            if (bval) {
                Address userAddress = new Address();
                userAddress.setAddress(address);
                String province = SysCommCache.get().findRegionById(Integer.parseInt(provinceId));
                if (!StringUtil.isEmpty(province)) {
                    userAddress.setProvince(province);
                }
                if (!StringUtil.isEmpty(mobile)) {
                    userAddress.setPhone(mobile);
                }
                if (profile != null) {
                    UserCenterServiceSngl.get().modifyUserAccount(new UpdateExpress().set(UserAccountField.ADDRESS, userAddress.toJsonStr()), profile.getUno());
                }
            }
            return new ModelAndView("redirect:/usercenter/customize/personinfo");
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
    }
}
