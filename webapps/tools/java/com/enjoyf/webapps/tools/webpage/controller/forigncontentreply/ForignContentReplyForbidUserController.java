package com.enjoyf.webapps.tools.webpage.controller.forigncontentreply;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.service.comment.CommentForbidField;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallAppField;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tonydiao on 2014/12/15.
 */

@Controller
@RequestMapping(value = "/forign/content/forbid")
public class ForignContentReplyForbidUserController extends ToolsBaseController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "nickName", required = false) String nickName,
                             @RequestParam(value = "errorFK", required = false) String errorFK) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("errorFK", errorFK);
        try {


            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(nickName)) {
                mapMessage.put("nickName", nickName);
                Profile profile = UserCenterServiceSngl.get().getProfileByNick(nickName);
                if (profile != null) {
                    queryExpress.add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profile.getProfileId()));
                } else {

                    mapMessage.put("errorFK", i18nSource.getMessage("forign.content.forbid.status.user.nickname", null, Locale.CHINA));
                    return new ModelAndView("/forigncontent/forbiduserlist", mapMessage);
                }
            }


            queryExpress.add(new QuerySort(CommentForbidField.START_TIME, QuerySortOrder.DESC));

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<CommentForbid> pageRows = CommentServiceSngl.get().queryCommentForbidByPage(queryExpress, pagination);
            if (pageRows != null) {
                List<CommentForbid> list = pageRows.getRows();
                Calendar tempCalendar = Calendar.getInstance();
                for (int i = 0; i < list.size(); i++) {

                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(list.get(i).getProfileid());
                    if (profile != null) {
                        list.get(i).setNickName(profile.getNick());
                    }
                    list.get(i).setEndTime(new Date(list.get(i).getStartTime().getTime() + list.get(i).getLength() * 1000));


                }

                mapMessage.put("list", list);
                mapMessage.put("page", pageRows.getPage());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/forigncontent/forbiduserlist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "nickName", required = false) String nickName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("nickName", nickName);
        return new ModelAndView("/forigncontent/forbiduseradd", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "nickName", required = false) String nickName,
                               @RequestParam(value = "startTime", required = false) String startTime,
                               @RequestParam(value = "endTime", required = false) String endTime,
                               @RequestParam(value = "flag_forever", required = false) String flag_forever,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nickName.trim());
            if (profile == null) {
                //该用户不存在,无法禁言.请查证后重试!
                mapMessage.put("nickName", nickName);
                mapMessage.put("startTime", startTime);
                mapMessage.put("endTime", endTime);
                mapMessage.put("flag_forever", flag_forever);
                mapMessage.put("errorMsg", i18nSource.getMessage("forign.content.forbid.status.user", null, Locale.CHINA));
                return new ModelAndView("/forigncontent/forbiduseradd", mapMessage);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profile.getProfileId()));
            List<CommentForbid> list = CommentServiceSngl.get().queryCommentForbid(queryExpress);
            if (list.size() > 0) {    //更新
                UpdateExpress updateExpress = new UpdateExpress();
                if (flag_forever.equals("1")) {    //永久禁言
                    updateExpress.set(CommentForbidField.START_TIME, new Date());
                    updateExpress.set(CommentForbidField.LENGTH, 0L);
                } else {   //非永久禁言
                    updateExpress.set(CommentForbidField.START_TIME, sdf.parse(startTime));
                    updateExpress.set(CommentForbidField.LENGTH, (sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime()) / 1000);
                }
                CommentServiceSngl.get().modifyCommentForbid(updateExpress, queryExpress);
                return new ModelAndView("redirect:/forign/content/forbid/list");
            }

            CommentForbid commentForbid = new CommentForbid();
            commentForbid.setProfileid(profile.getProfileId());

            if (flag_forever.equals("1")) {         //永久禁言
                commentForbid.setStartTime(new Date());
                commentForbid.setLength(0L);
            } else {    //非永久禁言
                commentForbid.setStartTime(sdf.parse(startTime));
                long length = (sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime()) / 1000;
                commentForbid.setLength(length);
            }

            CommentServiceSngl.get().createCommentForbid(commentForbid);

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_FORBID_ADD);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("评论禁言create方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/forign/content/forbid/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "nickName", required = false) String nickName,
                                   @RequestParam(value = "startTime", required = false) String startTime,
                                   @RequestParam(value = "length", required = false) String length) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            mapMessage.put("nickName", nickName);
            mapMessage.put("startTime", startTime);


            String flag_forever;
            String endTime = "";
            if (length != null && length.equals("0")) {     //永久禁言
                flag_forever = "1";


            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date sTime = sdf.parse(startTime);
                Date eTime = new Date(sTime.getTime() + Long.parseLong(length) * 1000);

                endTime = sdf.format(eTime);
                flag_forever = "2";

            }

            mapMessage.put("endTime", endTime);
            mapMessage.put("flag_forever", flag_forever);

        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

        }


        return new ModelAndView("/forigncontent/forbiduseredit", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "nickName", required = false) String nickName,
                               @RequestParam(value = "startTime", required = false) String startTime,
                               @RequestParam(value = "endTime", required = false) String endTime,
                               @RequestParam(value = "flag_forever", required = false) String flag_forever,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {

            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nickName.trim());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profile.getProfileId()));
            UpdateExpress updateExpress = new UpdateExpress();
            if (flag_forever.equals("1")) {    //永久禁言
                updateExpress.set(CommentForbidField.START_TIME, new Date());
                updateExpress.set(CommentForbidField.LENGTH, 0L);
            } else {   //非永久禁言
                updateExpress.set(CommentForbidField.START_TIME, sdf.parse(startTime));
                updateExpress.set(CommentForbidField.LENGTH, (sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime()) / 1000);
            }

            CommentServiceSngl.get().modifyCommentForbid(updateExpress, queryExpress);
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_FORBID_MODIFY);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            Map<String, String[]> params = request.getParameterMap();
            String queryString = "  ";
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString += key + "=" + values[i] + "&";
                }
            }
            // 去掉最后一个空格
            queryString = queryString.substring(0, queryString.length() - 1);

            if (queryString.length() > 1950) {
                queryString = queryString.substring(0, 1950);
            }
            log.setOpAfter("评论禁言modify方法,queryString" + queryString); //描述 推荐用中文
            addLog(log);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/forign/content/forbid/list");
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "profileid", required = true) String profileid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            boolean result = CommentServiceSngl.get().deleteCommentForbid(profileid);
            if (!result) {
                mapMessage.put("errorFK", "删除失败!");
            }

            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.COMMENT_FORBID_DELETE);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            log.setOpAfter("评论禁言delete方法,profileid" + profileid); //描述 推荐用中文
            addLog(log);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("forward:/forign/content/forbid/list", mapMessage);
    }
}
