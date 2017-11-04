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
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
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
 * User: zhitaoshi
 * Date: 14-7-25
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/operate")
public class SocialOperateControll extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "screenname", required = false) String screenname
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("virtualTypeColl", AccountVirtualType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QuerySort.add(AccountVirtualField.VIRTUAL_ID, QuerySortOrder.DESC));
            if (!StringUtil.isEmpty(screenname)) {
                queryExpress.add(QueryCriterions.like(AccountVirtualField.SCREENNAME, "%" + screenname + "%"));
            }
            queryExpress.add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_TYPE, AccountVirtualType.FORMAL.getCode()));

            PageRows<AccountVirtual> pageRows = null;//JoymeAppServiceSngl.get().queryAccountVirtualByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < pageRows.getRows().size(); i++) {
                    if (!StringUtil.isEmpty(pageRows.getRows().get(i).getUno())) {
                        unos.add(pageRows.getRows().get(i).getUno());
                    }
                }
                Map<String, SocialProfile> mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
                mapMessage.put("mapprofile", mapProfile);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("/joymeapp/socialclient/operate/operatelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/joymeapp/socialclient/operate/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "screenName", required = false) String screenName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        SocialProfile profile = null;
        try {
            //昵称存在
            profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
            if (profile == null || profile.getBlog() == null) {
                mapMessage.put("screenName", screenName);
                mapMessage.put("errorMsg", "用户不存在");
                return new ModelAndView("/joymeapp/socialclient/operate/createpage", mapMessage);
            }

            AccountVirtual accountVirtual = new AccountVirtual();
            accountVirtual.setUno(profile.getUno());
            accountVirtual.setScreenname(profile.getBlog().getScreenName());
            accountVirtual.setCreate_time(new Date());
            accountVirtual.setAccountVirtualType(AccountVirtualType.FORMAL);
            accountVirtual.setCreate_user(this.getCurrentUser().getUserid());
            //TODO 删除
            //JoymeAppServiceSngl.get().createAccountVirtual(accountVirtual);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/operate/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "vid", required = false) Long virtualId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(AccountVirtualField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            //TODO 删除
            //JoymeAppServiceSngl.get().modifyAccountVirtual(updateExpress, new QueryExpress().add(QueryCriterions.eq(AccountVirtualField.VIRTUAL_ID, virtualId)));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            mapMessage.put("errorMsg", "system.error");
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/operate/list");
    }

    @RequestMapping(value = "/postpage")
    public ModelAndView postPage(@RequestParam(value = "uno", required = false) String uno,
                                 @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("uno", uno);
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("platformColl", SocialContentPlatformDomain.getAll());

        return new ModelAndView("/joymeapp/socialclient/operate/postpage", mapMessage);
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
            return new ModelAndView("forward:/joymeapp/socialclient/operate/postpage", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/operate/list?pager.offset=" + pageStartIndex);
    }

}
