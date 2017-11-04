package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialConetntAppImages;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialContentAppAudios;
import com.enjoyf.platform.service.content.social.SocialContentPlatformDomain;
import com.enjoyf.platform.service.joymeapp.AccountVirtual;
import com.enjoyf.platform.service.joymeapp.AccountVirtualField;
import com.enjoyf.platform.service.joymeapp.AccountVirtualType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlogField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppSocialVirtualTimer;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppSocialVirtualTimerConstant;
import com.enjoyf.webapps.tools.webpage.controller.Constant;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-20
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/virtual")
public class SocialVirtualUserControll extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "screenname", required = false) String screenname,
                             @RequestParam(value = "vtype", required = false) Integer virtualType,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("virtualTypeColl", AccountVirtualType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.DEFAULT.getCode()));
            queryExpress.add(QuerySort.add(AccountVirtualField.VIRTUAL_ID, QuerySortOrder.DESC));
            if (!StringUtil.isEmpty(screenname)) {
                queryExpress.add(QueryCriterions.like(AccountVirtualField.SCREENNAME, "%" + screenname + "%"));
            }
            if (virtualType != null && virtualType > 0) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, virtualType));
            }

            //TODO 删除
            PageRows<AccountVirtual> pageRows = null;//JoymeAppServiceSngl.get().queryAccountVirtualByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<AccountVirtual> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getUno())) {
                        unos.add(list.get(i).getUno());
                    }
                }
                Map<String, SocialProfile> mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
                for (Map.Entry<String, SocialProfile> entry : mapProfile.entrySet()) {
                    String key = entry.getKey();
                    SocialProfile profile = entry.getValue();
                    ProfileSum profileSum = ProfileServiceSngl.get().getProfileSum(key);
                    profile.setSum(profileSum);
                    mapMessage.put(key, profile);
                }

                //TODO 删除
                List<AccountVirtual> accountVirtualList = null;// JoymeAppServiceSngl.get().queryAccountVirtual(new QueryExpress().add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
                int formalNum = 0;
                int defaultNum = 0;
                for (AccountVirtual accountVirtual : accountVirtualList) {
                    if (accountVirtual.getAccountVirtualType().getCode() == 0) {
                        defaultNum++;
                    } else {
                        formalNum++;
                    }
                }
                mapMessage.put("formalNum", formalNum);
                mapMessage.put("defaultNum", defaultNum);

                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("mapprofile", mapProfile);
            }
        } catch (Exception e) {

        }
        return new ModelAndView("/joymeapp/socialclient/virtual/virtuallist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("sex", 1);
        mapMessage.put("virtualTypes", AccountVirtualType.getAll());
        return new ModelAndView("/joymeapp/socialclient/virtual/createpage", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "virtualId") Long virtualId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("virtualTypes", AccountVirtualType.getAll());
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId));
        try {
            //TODO 删除
            AccountVirtual accountVirtual = null;//JoymeAppServiceSngl.get().getAccountVirtual(queryExpress);
            mapMessage.put("accountVirtual", accountVirtual);
            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(accountVirtual.getUno());
            String picurl1 = null;
            try {
                picurl1 = ImageURLTag.parseFirstFacesInclude(profile.getBlog().getHeadIconSet(), profile.getDetail().getSex(), "m", true);
            } catch (Exception e) {
            }

            if (picurl1.startsWith("/r001")) {
                picurl1 = "http://r001." + Constant.R_DOMAIN + picurl1;
            } else if (picurl1.startsWith("/r002")) {
                picurl1 = "http://r002." + Constant.R_DOMAIN + picurl1;
            }
            mapMessage.put("profile", profile);
            mapMessage.put("picurl1", picurl1);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/joymeapp/socialclient/virtual/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "virtualId", required = false) Long virtualId,
                               @RequestParam(value = "screenName", required = false) String screenName,
                               @RequestParam(value = "picurl1", required = false) String picurl1,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "birthday", required = false) String birthday) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (picurl1.startsWith("http://r001")) {
            picurl1 = picurl1.replaceAll("http://r001." + Constant.R_DOMAIN, "").trim();
        } else if (picurl1.startsWith("http://r002")) {
            picurl1 = picurl1.replaceAll("http://r002." + Constant.R_DOMAIN, "").trim();
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId));
        try {
            //TODO 删除
            AccountVirtual accountVirtual = null;//JoymeAppServiceSngl.get().getAccountVirtual(queryExpress);

            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(accountVirtual.getUno());
            if (!screenName.equals(profile.getBlog().getScreenName())) {
                SocialProfile socialProfileByScreenName = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
                if (socialProfileByScreenName != null && socialProfileByScreenName.getBlog() != null) {
                    mapMessage.put("profile", profile);
                    mapMessage.put("picurl1", picurl1);
                    mapMessage.put("accountVirtual", accountVirtual);
                    mapMessage.put("errorMsg", "昵称已经存在，请换一个昵称");
                    return new ModelAndView("forward:/joymeapp/socialclient/virtual/modifypage", mapMessage);
                }
            }
            String oldPicurl1 = null;
            try {
                oldPicurl1 = ImageURLTag.parseFirstFacesInclude(profile.getBlog().getHeadIconSet(), profile.getDetail().getSex(), "m", true);
            } catch (Exception e) {
            }
            boolean isUpdateProfileBlog = false;
            if (!screenName.equals(profile.getBlog().getScreenName())) {
                profile.getBlog().setScreenName(screenName);
                //TODO 删除
//				JoymeAppServiceSngl.get().modifyAccountVirtual(new UpdateExpress().set(AccountVirtualField.SCREENNAME, screenName)
//						.set(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.DEFAULT.getCode()),
//						new QueryExpress().add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId)));
                isUpdateProfileBlog = true;
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(SocialProfileBlogField.SCREENNAME, screenName);
            ProfileBlogHeadIconSet set = new ProfileBlogHeadIconSet();
            if (!picurl1.equals(oldPicurl1)) {
                ProfileBlogHeadIcon headIcon = new ProfileBlogHeadIcon();
                headIcon.setHeadIcon(picurl1);
                headIcon.setId(0);
                headIcon.setValidStatus(true);
                set.add(headIcon);
                profile.getBlog().setHeadIconSet(set);
                updateExpress.set(SocialProfileBlogField.MOREHEADICONS, set.toJsonStr());

                isUpdateProfileBlog = true;
            }
            if (isUpdateProfileBlog) {
                ProfileServiceSngl.get().modifySocialProfileBlogByUno(updateExpress, profile.getBlog().getUno());
            }

            // String uno, Map<ObjectField, Object> keyValues
            Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();
            if (!sex.equals(profile.getDetail().getSex()) || !birthday.equals(profile.getDetail().getBirthday())) {
                keyValues.put(ProfileDetailField.BIRTHDAY, birthday);
                keyValues.put(ProfileDetailField.SEX, sex);
                ProfileServiceSngl.get().updateProfileDetail(profile.getBlog().getUno(), keyValues);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "screenName", required = false) String screenName,
                               @RequestParam(value = "picurl1", required = false) String picurl1,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "birthday", required = false) String birthday) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        SocialProfile profile = null;
        try {
            //昵称存在
            profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
            if (profile != null && profile.getBlog() != null) {
                mapMessage.put("screenName", screenName);
                mapMessage.put("picurl1", picurl1);
                mapMessage.put("birthday", birthday);
                mapMessage.put("sex", sex);
                mapMessage.put("errorMsg", "昵称已经存在，请换一个昵称");
                return new ModelAndView("/joymeapp/socialclient/virtual/createpage", mapMessage);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        if (picurl1.startsWith("http://r001")) {
            picurl1 = picurl1.replaceAll("http://r001." + Constant.R_DOMAIN, "").trim();
        } else if (picurl1.startsWith("http://r002")) {
            picurl1 = picurl1.replaceAll("http://r002." + Constant.R_DOMAIN, "").trim();
        }

        ProfileBlogHeadIconSet set = new ProfileBlogHeadIconSet();
        ProfileBlogHeadIcon headIcon = new ProfileBlogHeadIcon();
        headIcon.setHeadIcon(picurl1);
        headIcon.setId(0);
        headIcon.setValidStatus(true);
        set.add(headIcon);

        try {
            SocialProfileBlog socialProfileBlog = new SocialProfileBlog();
            String profileUno = UUID.randomUUID().toString();
            socialProfileBlog.setUno(profileUno);
            socialProfileBlog.setScreenName(screenName);
            socialProfileBlog.setHeadIconSet(set);
            socialProfileBlog.setCreateDate(new Date());
            socialProfileBlog.setTemplateId("0");
            profile = ProfileServiceSngl.get().createSocialProfile(socialProfileBlog);

            Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();
            keyValues.put(ProfileDetailField.BIRTHDAY, birthday);
            keyValues.put(ProfileDetailField.SEX, sex);
            ProfileServiceSngl.get().updateProfileDetail(profile.getBlog().getUno(), keyValues);

            AccountVirtual accountVirtual = new AccountVirtual();
            accountVirtual.setUno(profile.getBlog().getUno());
            accountVirtual.setCreate_time(new Date());
            accountVirtual.setAccountVirtualType(AccountVirtualType.DEFAULT);
            accountVirtual.setCreate_user(this.getCurrentUser().getUserid());
            //TODO 删除
            //JoymeAppServiceSngl.get().createAccountVirtual(accountVirtual);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
    }

    @RequestMapping(value = "/agreepage")
    public ModelAndView create() {
        return new ModelAndView("/joymeapp/socialclient/virtual/agreepage");
    }

    @RequestMapping(value = "/replypage")
    public ModelAndView replypage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        List<AccountVirtual> list = null;
        try {
            //TODO 删除
            //	list = JoymeAppServiceSngl.get().queryAccountVirtual(new QueryExpress().add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
            int formalNum = 0;
            int defaultNum = 0;
            for (AccountVirtual accountVirtual : list) {
                if (accountVirtual.getAccountVirtualType().getCode() == 0) {
                    defaultNum++;
                } else {
                    formalNum++;
                }
            }
            mapMessage.put("formalNum", formalNum);
            mapMessage.put("defaultNum", defaultNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("/joymeapp/socialclient/virtual/replypage", mapMessage);
    }

    @RequestMapping(value = "/agree")
    public ModelAndView agree(@RequestParam(value = "contentId", required = false) long contentId,
                              @RequestParam(value = "agreeNum", required = false) int agreeNum,
                              @RequestParam(value = "accountType", required = false) long accountType,
                              @RequestParam(value = "timerType", required = false) int timerType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            SocialContent socialContent = ContentServiceSngl.get().getSocialContent(contentId);
            if (socialContent == null) {
                mapMessage.put("errorMsg", "文章不存在或被删除");
                mapMessage.put("contentId", contentId);
                mapMessage.put("agreeNum", agreeNum);
                return new ModelAndView("redirect:/joymeapp/socialclient/content/list");
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            if (accountType == 0) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.DEFAULT.getCode()));
            } else if (accountType == 1) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.FORMAL.getCode()));
            }
            //TODO 删除
            List<AccountVirtual> virtualList = null;//JoymeAppServiceSngl.get().queryAccountVirtual(queryExpress);

            //random
            List<AccountVirtual> randomByList = RandomUtil.getRandomByList(virtualList, agreeNum);
            List<String> listUnos = new ArrayList<String>();
            for (AccountVirtual virtual : randomByList) {
                listUnos.add(virtual.getUno());
            }

            long interval = 0L;
            if (timerType == 1) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_5;
            } else if (timerType == 2) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_10;
            } else if (timerType == 3) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_15;
            } else if (timerType == 4) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_30;
            } else {
                interval = JoymeAppSocialVirtualTimerConstant.DEFAULT;
            }

            JoymeAppSocialVirtualTimer joymeAppSocialVirtualTimer = new JoymeAppSocialVirtualTimer("agree", socialContent, listUnos, interval);
            joymeAppSocialVirtualTimer.start();


        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/joymeapp/socialclient/content/list");
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/content/list");
    }

    @RequestMapping(value = "/reply")
    public ModelAndView reply(HttpServletRequest request,
                              @RequestParam(value = "contentId", required = false) long contentId,
                              @RequestParam(value = "accountType", required = false) long accountType,
                              @RequestParam(value = "timerType", required = false) int timerType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            SocialContent socialContent = ContentServiceSngl.get().getSocialContent(contentId);
            if (socialContent == null) {
                mapMessage.put("errorMsg", "文章不存在或被删除");
                return new ModelAndView("/joymeapp/socialclient/virtual/replypage", mapMessage);
            }
            long interval = 0L;
            if (timerType == 1) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_5;
            } else if (timerType == 2) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_10;
            } else if (timerType == 3) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_15;
            } else if (timerType == 4) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_30;
            } else {
                interval = JoymeAppSocialVirtualTimerConstant.DEFAULT;
            }

            //body
            String[] bodys = request.getParameterValues("body");
            List<String> bodyList = new ArrayList<String>();
            for (String body : bodys) {
                if (!StringUtil.isEmpty(body)) {
                    bodyList.add(body);
                }
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            if (accountType == 0) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.DEFAULT.getCode()));
            } else if (accountType == 1) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.FORMAL.getCode()));
            }
            //TODO 删除
            List<AccountVirtual> virtualList = null;//JoymeAppServiceSngl.get().queryAccountVirtual(queryExpress);
            //random
            List<AccountVirtual> randomByList = RandomUtil.getRandomByList(virtualList, bodyList.size());
            List<String> listUnos = new ArrayList<String>();
            for (AccountVirtual virtual : randomByList) {
                listUnos.add(virtual.getUno());
            }

            List<String> listBody = new ArrayList<String>();
            for (int i = 0; i < listUnos.size(); i++) {
                if (!StringUtil.isEmpty(bodyList.get(i))) {
                    listBody.add(bodyList.get(i));
                }
            }


            JoymeAppSocialVirtualTimer joymeAppSocialVirtualTimer = new JoymeAppSocialVirtualTimer("reply", socialContent, listUnos, listBody, interval);
            joymeAppSocialVirtualTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
    }

    @RequestMapping(value = "/postpage")
    public ModelAndView postPage(@RequestParam(value = "uno", required = false) String uno,
                                 @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("uno", uno);
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("platformColl", SocialContentPlatformDomain.getAll());

        return new ModelAndView("/joymeapp/socialclient/virtual/postpage", mapMessage);
    }

    @RequestMapping(value = "/postcontent")
    public ModelAndView postContent(@RequestParam(value = "uno", required = false) String uno,
                                    @RequestParam(value = "platform", required = false) Integer platform,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "body", required = false) String body,
                                    @RequestParam(value = "pic", required = false) String pic,
                                    @RequestParam(value = "pics", required = false) String pics,
                                    @RequestParam(value = "amr", required = false) String amr,
                                    @RequestParam(value = "mp3", required = false) String mp3,
                                    @RequestParam(value = "audiotime", required = false, defaultValue = "0") Long audiotime,
                                    @RequestParam(value = "lon", required = false, defaultValue = "0") Float lon,
                                    @RequestParam(value = "lat", required = false, defaultValue = "0") Float lat,
                                    @RequestParam(value = "activityid", required = false, defaultValue = "0") Long activityid,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("uno", uno);
        mapMessage.put("platform", platform);
        mapMessage.put("title", title);
        mapMessage.put("body", body);
        mapMessage.put("pic", pic);
        mapMessage.put("pics", pics);
        mapMessage.put("amr", amr);
        mapMessage.put("mp3", mp3);
        mapMessage.put("audiotime", audiotime);
        mapMessage.put("lon", lon);
        mapMessage.put("lat", lat);
        mapMessage.put("activityid", activityid);

        SocialContent socialContent = new SocialContent();
        socialContent.setUno(uno);
        socialContent.setTitle(title);
        socialContent.setBody(body);
        SocialConetntAppImages images = new SocialConetntAppImages();
        images.setPic(pic);
        images.setPic_s(pics);
        socialContent.setPic(images);
        SocialContentAppAudios audio = new SocialContentAppAudios();
        audio.setAmr(amr);
        audio.setMp3(mp3);
        socialContent.setAudio(audio);
        if (audiotime != null && audiotime > 0l) {
            socialContent.setAudioLen(audiotime);
        }
        socialContent.setLon(lon);
        socialContent.setLat(lat);
        socialContent.setCreateTime(new Date());
        socialContent.setActivityId(activityid == null ? 0l : activityid);
        socialContent.setSocialContentPlatformDomain(SocialContentPlatformDomain.getByCode(platform));

        try {
            ContentServiceSngl.get().postSocialContent(socialContent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/virtual/postpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list?pager.offset=" + pageStartIndex);
    }

    @RequestMapping(value = "/focus")
    public ModelAndView focus(HttpServletRequest request,
                              @RequestParam(value = "srcScreenname", required = false) String srcScreenname,
                              @RequestParam(value = "accountTypeFocus", required = false) int accountTypeFocus,
                              @RequestParam(value = "timerType", required = false) int timerType,
                              @RequestParam(value = "action", required = false) int action,
                              @RequestParam(value = "focusNum", required = false) int focusNum) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            SocialProfile srcProfile = ProfileServiceSngl.get().getSocialProfileByScreenName(srcScreenname.trim());
            if (srcProfile == null) {
                mapMessage.put("focusNum", focusNum);
                mapMessage.put("srcScreenname", srcScreenname);
                mapMessage.put("errorMsg", "用户不存在");
                return new ModelAndView("forward:/joymeapp/socialclient/virtual/list", mapMessage);
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            if (accountTypeFocus == 0) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.DEFAULT.getCode()));
            } else if (accountTypeFocus == 1) {
                queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.FORMAL.getCode()));
            }
            //TODO 删除
            List<AccountVirtual> virtualList = null;//JoymeAppServiceSngl.get().queryAccountVirtual(queryExpress);
            //random
            List<AccountVirtual> randomByList = RandomUtil.getRandomByList(virtualList, focusNum);

            long interval = 0L;
            if (timerType == 1) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_5;
            } else if (timerType == 2) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_10;
            } else if (timerType == 3) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_15;
            } else if (timerType == 4) {
                interval = JoymeAppSocialVirtualTimerConstant.MINUTES_30;
            } else {
                interval = JoymeAppSocialVirtualTimerConstant.DEFAULT;
            }
            List<String> unos = new ArrayList<String>();
            for (AccountVirtual accountVirtual : randomByList) {
                if (srcProfile.getBlog().getUno().equals(accountVirtual.getUno())) {
                    continue;
                }
                unos.add(accountVirtual.getUno());
            }

            JoymeAppSocialVirtualTimer joymeAppSocialVirtualTimer = new JoymeAppSocialVirtualTimer("focus", unos, srcProfile.getBlog().getUno(), action, interval);
            joymeAppSocialVirtualTimer.start();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/joymeapp/socialclient/virtual/postpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/virtual/list");
    }

}
