package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialContentField;
import com.enjoyf.platform.service.content.social.SocialHotContent;
import com.enjoyf.platform.service.joymeapp.AccountVirtual;
import com.enjoyf.platform.service.joymeapp.AccountVirtualField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/content")
public class SocialContentController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "screenname", required = false) String screenName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(SocialContentField.CONTENTID, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(status)) {
            mapMessage.put("status", status);
            queryExpress.add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, status));
        } else {
            queryExpress.add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        }
        try {
            if (!StringUtil.isEmpty(screenName)) {
                SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenName);
                if (profile == null) {
                    return new ModelAndView("/joymeapp/socialclient/content/contentlist", mapMessage);
                }
                mapMessage.put("screenname", screenName);
                queryExpress.add(QueryCriterions.eq(SocialContentField.UNO, profile.getUno()));
            }

            PageRows<SocialContent> pageRows = ContentServiceSngl.get().querySocialContentByPage(queryExpress, pagination);
            Map<String, SocialProfile> mapProfile = null;
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<SocialContent> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getUno())) {
                        unos.add(list.get(i).getUno());
                    }
                }
                mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("mapprofile", mapProfile);
            }
            //TODO 删除
            List<AccountVirtual> list = null;// JoymeAppServiceSngl.get().queryAccountVirtual(new QueryExpress().add(QueryCriterions.eq(AccountVirtualField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
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


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/content/contentlist", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/content/contentlist", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView modify(@RequestParam(value = "cid", required = false) String contentId,
                               @RequestParam(value = "uno", required = false) String uno,
                               @RequestParam(value = "curpage", required = false) String curPage) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int pager = 0;
        try {

            pager = (Integer.parseInt(curPage) - 1) * 40;
            ContentServiceSngl.get().removeSocialContent(uno, Long.parseLong(contentId));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/report/reportlist", mapMessage);
        }


        return new ModelAndView("redirect:/joymeapp/socialclient/content/list?pager.offset=" + pager);
    }

    @RequestMapping(value = "/getbyid")
    public ModelAndView getById(@RequestParam(value = "cid", required = false) String contentId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int pager = 0;
        try {
            SocialContent socialContent = ContentServiceSngl.get().getSocialContent(Long.parseLong(contentId));
            mapMessage.put("socialContent", socialContent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/report/reportlist", mapMessage);
        }


        return new ModelAndView("/joymeapp/socialclient/content/contentinfo", mapMessage);
    }

    @RequestMapping(value = "/createhot")
    public ModelAndView createHot(@RequestParam(value = "offset", required = false, defaultValue = "0") int pageStartIndex,
                                  @RequestParam(value = "cid", required = false) Long contentId,
                                  @RequestParam(value = "uno", required = false) String contentUno
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            SocialHotContent socialHotContent = new SocialHotContent();
            socialHotContent.setContentId(contentId);
            socialHotContent.setUno(contentUno);
            socialHotContent.setRemoveStatus(ActStatus.UNACT);
            socialHotContent.setCreateDate(new Date());
            socialHotContent.setCreateIp(getIp());
            ContentServiceSngl.get().insertSocialHotContent(socialHotContent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/content/list", mapMessage);
        }

        return new ModelAndView("redirect:/joymeapp/socialclient/content/list?pager.offset=" + pageStartIndex, mapMessage);
    }


}
