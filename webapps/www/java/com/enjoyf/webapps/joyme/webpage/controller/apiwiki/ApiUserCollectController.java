package com.enjoyf.webapps.joyme.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.ask.wiki.Content;
import com.enjoyf.platform.service.ask.wiki.ContentField;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentUtil;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.joymewiki.CollectDTO;
import com.enjoyf.webapps.joyme.weblogic.apiwiki.ApiwikiWebLogic;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by pengxu on 2017/3/27.
 */

@Controller
@RequestMapping(value = "/api/wiki/collect")
public class ApiUserCollectController {

    @Resource(name = "apiwikiWebLogic")
    private ApiwikiWebLogic apiwikiWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @ResponseBody
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        String profileId = HTTPUtil.getParam(request, "pid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String ctype = HTTPUtil.getParam(request, "ctype");
        String contentid = HTTPUtil.getParam(request, "contentid");
        String title = HTTPUtil.getParam(request, "title");
        CollectType collectType = CollectType.getByCode(Integer.parseInt(ctype));
        if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(ctype) || collectType == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        if ((collectType.equals(CollectType.CMS) && StringUtil.isEmpty(contentid)) || (collectType.equals(CollectType.WIKI) && (StringUtil.isEmpty(title) || StringUtil.isEmpty(contentid)))) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            String commentId = CommentUtil.genCommentId(contentid, CommentDomain.CMS_COMMENT);
            if (collectType.equals(CollectType.WIKI)) {
                commentId = CommentUtil.genCommentId(contentid + "|" + title, CommentDomain.UGCWIKI_COMMENT);
            }
            Content content = AskServiceSngl.get().getContent(commentId);
            if (content == null) {
                return ResultCodeConstants.CONTENT_NOT_EXIST.getJsonString();
            }
            UserCollect userCollect = new UserCollect();
            userCollect.setProfileId(profileId);
            userCollect.setAppkey(appkey);
            userCollect.setCreateDate(new Date());
            userCollect.setCollectType(CollectType.getByCode(Integer.parseInt(ctype)));
            userCollect.setContentId(content.getId());
            userCollect = AskServiceSngl.get().insertUserCollect(userCollect);
            if (userCollect.getId() <= 0) {
                return ResultCodeConstants.FAILED.getJsonString();
            }
            Map<String, Long> returnMap = new HashMap<String, Long>();
            returnMap.put("collect_id", userCollect.getId());
            jsonObject.put("result", returnMap);
            return jsonObject.toString();
        } catch (ServiceException e) {
            if (e.equals(AskServiceException.USER_COLLECT_EXIST)) {
                return ResultCodeConstants.USER_COLLECT_EXIST.getJsonString();
            }
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/querystatus")
    public String queryStatus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        String callback = HTTPUtil.getParam(request, "callback");
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            String ctype = HTTPUtil.getParam(request, "ctype");
            String cotentid = HTTPUtil.getParam(request, "contentid");
            String title = HTTPUtil.getParam(request, "title");
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(ctype)) {
                return StringUtil.isEmpty(callback) ? ResultCodeConstants.PARAM_EMPTY.getJsonString() : ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }
            CollectType collectType = CollectType.getByCode(Integer.parseInt(ctype));
            if ((collectType.equals(CollectType.CMS) && StringUtil.isEmpty(cotentid)) || (collectType.equals(CollectType.WIKI) && (StringUtil.isEmpty(title) || StringUtil.isEmpty(cotentid)))) {
                return StringUtil.isEmpty(callback) ? ResultCodeConstants.PARAM_EMPTY.getJsonString() : ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            String commentId = CommentUtil.genCommentId(cotentid, CommentDomain.CMS_COMMENT);
            if (collectType.equals(CollectType.WIKI)) {
                commentId = CommentUtil.genCommentId(cotentid + "|" + title, CommentDomain.UGCWIKI_COMMENT);
            }
            Content content = AskServiceSngl.get().getContent(commentId);
            if (content == null) {
                return StringUtil.isEmpty(callback) ? ResultCodeConstants.CONTENT_NOT_EXIST.getJsonString() : ResultCodeConstants.CONTENT_NOT_EXIST.getJsonString(callback);
            }

            UserCollect userCollect = AskServiceSngl.get().getCollect(new QueryExpress().add(QueryCriterions.eq(UserCollectField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(UserCollectField.CONTENT_ID, content.getId())));
            if (userCollect != null) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("collect_id", userCollect.getId());
                jsonObject.put("result", map);
                return StringUtil.isEmpty(callback) ? jsonObject.toString() : callback + "([" + jsonObject.toString() + "])";
            } else {
                return StringUtil.isEmpty(callback) ? ResultCodeConstants.NOT_COLLECT.getJsonString() : ResultCodeConstants.NOT_COLLECT.getJsonString(callback);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return StringUtil.isEmpty(callback) ? ResultCodeConstants.ERROR.getJsonString() : ResultCodeConstants.ERROR.getJsonString(callback);

        }
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public String followList(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "pcount", required = false, defaultValue = "10") Integer count) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            Pagination pagination = new Pagination(count * page, page, count);
            if (StringUtil.isEmpty(profileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            PageRows<UserCollect> pageRows = AskServiceSngl.get().queryCollectByCache(profileId, pagination);
            Map<String, Object> returnMap = new HashMap<String, Object>();
            if (pageRows == null) {
                returnMap.put("rows", new ArrayList<CollectDTO>());
                returnMap.put("page", pagination);
                jsonObject.put("result", returnMap);
                return jsonObject.toString();
            }
            List<CollectDTO> list = apiwikiWebLogic.buildCollect(pageRows.getRows());
            returnMap.put("rows", CollectionUtil.isEmpty(list) ? new ArrayList<CollectDTO>() : list);
            returnMap.put("page", pageRows.getPage());
            jsonObject.put("result", returnMap);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }


    @RequestMapping(value = "/remove")
    @ResponseBody
    public String unfollow(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String profileId = HTTPUtil.getParam(request, "pid");
            String ids = HTTPUtil.getParam(request, "ids");
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(ids)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            String[] idArray = new String[0];
            try {
                idArray = ids.split("\\,");
            } catch (Exception e) {
                return ResultCodeConstants.FORMAT_ERROR.getJsonString();
            }
            Set<Long> idSet = new HashSet<Long>();
            for (int i = 0; i < idArray.length; i++) {
                idSet.add(Long.parseLong(idArray[i]));
            }
            boolean bool = AskServiceSngl.get().deleteUserCollect(idSet, profileId);
            if (bool) {
                return jsonObject.toString();
            }
            return ResultCodeConstants.FAILED.getJsonString();
        } catch (ServiceException e) {
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }
}
