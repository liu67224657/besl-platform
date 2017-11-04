package com.enjoyf.webapps.tools.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContentReply;
import com.enjoyf.platform.service.content.social.SocialContentReplyField;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-23
 * Time: 上午11:24
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/socialclient/reply")
public class SocialContentReplyController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "contentid", required = false) String contentid,
                             @RequestParam(value = "screenname", required = false) String screenname,
                             @RequestParam(value = "body", required = false) String body,
                             @RequestParam(value = "message", required = false) String message) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(status)) {
            mapMessage.put("status", status);
            queryExpress.add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, status));
        } else {
            mapMessage.put("status", "n");
            queryExpress.add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        }
        if (!StringUtil.isEmpty(contentid)) {
            mapMessage.put("contentid", contentid);
            queryExpress.add(QueryCriterions.eq(SocialContentReplyField.CONTENT_ID, Long.valueOf(contentid)));
        }
        queryExpress.add(QuerySort.add(SocialContentReplyField.REPLY_ID, QuerySortOrder.DESC));
         //
        try {

            if (!StringUtil.isEmpty(screenname)) {
                SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByScreenName(screenname);
                if (profile == null) {
                    return new ModelAndView("/joymeapp/socialclient/reply/replylist", mapMessage);
                }
                mapMessage.put("screenname", screenname);
                queryExpress.add(QueryCriterions.eq(SocialContentReplyField.REPLY_UNO, profile.getUno()));
            }

            if (!StringUtil.isEmpty(body)) {
                mapMessage.put("body", body);
                queryExpress.add(QueryCriterions.like(SocialContentReplyField.BODY, "%" + body + "%"));
            }


            PageRows<SocialContentReply> pageRows = ContentServiceSngl.get().querySocialContentReplyByPage(queryExpress, pagination);//QUERY_SOCIALCONTENT_REPLY_BYPAGE
            Map<String, SocialProfile> mapProfile = null;
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapProfile = new HashMap<String, SocialProfile>();
                List<SocialContentReply> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getReplyUno())) {
                        unos.add(list.get(i).getReplyUno());
                    }
					unos.add(list.get(i).getContentUno());
                }
                mapProfile = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unos);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                mapMessage.put("mapprofile", mapProfile);
            }


            //
            if (!StringUtil.isEmpty(message) && message.equals("1")) {
                mapMessage.put("errorMsg", "请选择正确的修改方式");
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/reply/replylist", mapMessage);
        }
        return new ModelAndView("/joymeapp/socialclient/reply/replylist", mapMessage);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "curpage", required = false) String curPage,
                               @RequestParam(value = "contentid", required = false) Long contentId,
                               @RequestParam(value = "replyid", required = false) Long replyId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int pager = 0;
        try {
            pager = (Integer.parseInt(curPage) - 1) * 40;
            boolean remoeReply = ContentServiceSngl.get().removeSocialContentReply(contentId, replyId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/reply/replylist", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/reply/list?pager.offset=" + pager);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String contentid = request.getParameter("contentid");
            if (StringUtil.isEmpty(contentid)) {
                String replyids = request.getParameter("replyids");
                String updatestatuscode = request.getParameter("updatestatuscode");//n恢复 y删除
                String hiddenstatus = request.getParameter("hiddenstatus");//现在的状态
                if (updatestatuscode.equals(hiddenstatus)) {
                    return new ModelAndView("redirect:/joymeapp/socialclient/reply/list?message=1&pager.offset=0", mapMessage);
                }

                String arrids[] = replyids.split("@");
                for (String arr : arrids) {
                    String arrs[] = arr.split("_");
                    if (updatestatuscode.equals("n")) {
                        ContentServiceSngl.get().recoverSocialContentReply(Long.valueOf(arrs[1]), Long.valueOf(arrs[0]));
                    } else {
                        ContentServiceSngl.get().removeSocialContentReply(Long.valueOf(arrs[1]), Long.valueOf(arrs[0]));
                    }
                }
            } else {
                String replyid = request.getParameter("replyid");
                ContentServiceSngl.get().recoverSocialContentReply(Long.valueOf(contentid), Long.valueOf(replyid));
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/joymeapp/socialclient/reply/replylist", mapMessage);
        }
        return new ModelAndView("redirect:/joymeapp/socialclient/reply/list?pager.offset=0");
    }

}
