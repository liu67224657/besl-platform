package com.enjoyf.webapps.joyme.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.wiki.*;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentUtil;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeDTO;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.apiwiki.ApiwikiTagDTO;
import com.enjoyf.webapps.joyme.dto.apiwiki.ContentDTO;
import com.enjoyf.webapps.joyme.weblogic.apiwiki.ApiwikiWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
@RequestMapping("/api/wiki/content")
@Controller
public class ApiwikiContentController {

    @Resource(name = "apiwikiWebLogic")
    private ApiwikiWebLogic apiwikiWebLogic;

    @RequestMapping("/wikipost")
    @ResponseBody
    public String wikipost(HttpServletRequest request,
                           @RequestParam(value = "wikikey", required = false) String wikikey,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "wikiname", required = false) String wikiname,
                           @RequestParam(value = "weburl", required = false) String webUrl,
                           @RequestParam(value = "publishtime", required = false) String publishtime) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(title) || StringUtil.isEmpty(wikikey) || StringUtil.isEmpty(wikiname) || StringUtil.isEmpty(webUrl) || StringUtil.isEmpty(publishtime)) {
                return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            Content content = new Content();
            content.setCommentId(CommentUtil.genCommentId(wikikey + "|" + title, CommentDomain.UGCWIKI_COMMENT));
            content.setSource(ContentSource.WIKI);
            content.setTitle(title);
            content.setDescription(wikiname);
            content.setPublishTime(new Date(Long.valueOf(publishtime)));
            content.setWebUrl(webUrl);
            content.setCreateDate(new Date());
            AskServiceSngl.get().postContentWiki(content);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/post")
    @ResponseBody
    public String post(HttpServletRequest request,
                       @RequestParam(value = "archiveid", required = false) String archiveid,
                       @RequestParam(value = "title", required = false) String title,
                       @RequestParam(value = "describe", required = false) String describe,
                       @RequestParam(value = "pic", required = false) String pic,
                       @RequestParam(value = "author", required = false) String author,
                       @RequestParam(value = "gameid", required = false) String gameId,
                       @RequestParam(value = "weburl", required = false) String webUrl,
                       @RequestParam(value = "publishtime", required = false) String publishtime) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(archiveid) || StringUtil.isEmpty(title) || StringUtil.isEmpty(describe)
                    || StringUtil.isEmpty(pic) || StringUtil.isEmpty(author) || StringUtil.isEmpty(gameId)
                    || StringUtil.isEmpty(webUrl) || StringUtil.isEmpty(publishtime)) {
                return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            Content content = new Content();
            content.setCommentId(CommentUtil.genCommentId(archiveid, CommentDomain.CMS_COMMENT));
            content.setTitle(title);
            content.setDescription(describe);
            content.setPic(pic);
            content.setAuthor(author);
            content.setGameId(gameId);
            content.setPublishTime(new Date(Long.valueOf(publishtime)));
            content.setWebUrl(webUrl);
            content.setCreateDate(new Date());
            AskServiceSngl.get().postContent(content);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    //修改状态，删除或者恢复
    @RequestMapping("/updatestatus")
    @ResponseBody
    public String updatestatus(HttpServletRequest request,
                               @RequestParam(value = "archiveid", required = false) String archiveid,
                               @RequestParam(value = "status", required = false, defaultValue = "removed") String status) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(archiveid)) {
                return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            ValidStatus validStatus = ValidStatus.getByCode(status);

            AskServiceSngl.get().updateContentStatus(CommentUtil.genCommentId(archiveid, CommentDomain.CMS_COMMENT), validStatus);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping("/taglist")
    @ResponseBody
    public String taglist(HttpServletRequest request) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            List<ApiwikiTagDTO> list = apiwikiWebLogic.queryContentTagList(ContentTagLine.RECOMMEND);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", list);
            jsonObject.put("result", map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping("/list")
    @ResponseBody
    public String list(HttpServletRequest request,
                       @RequestParam(value = "pcount", required = false, defaultValue = "20") String pcount,
                       @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer pnum,
                       @RequestParam(value = "tagid", required = false, defaultValue = "") String tagid) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

        String pid = HTTPUtil.getParam(request, "pid");
        if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(tagid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String queryFlag = HTTPUtil.getParam(request, "queryflag");//score
        //添加个默认值
        queryFlag = StringUtil.isEmpty(queryFlag) ? "-1" : queryFlag;

        double timeLimit = -1D;
        if (Double.valueOf(queryFlag) < 0) {//第一页
            timeLimit = System.currentTimeMillis();
        } else {
            timeLimit = new BigDecimal(queryFlag).doubleValue();
        }

        String appkey = HTTPUtil.getParam(request, "appkey");
        String platform = HTTPUtil.getParam(request, "platform");


        try {
            ScoreRange range = new ScoreRange(-1, timeLimit, Integer.parseInt(pcount), true);
            range.setIsFirstPage(Double.valueOf(queryFlag) < 0);


            ScoreRangeRows<ContentDTO> list = apiwikiWebLogic.queryContentByScoreRangeRows(tagid, range, pnum, appkey, platform, pid);

            //标签轮播图
            List<ContentDTO> headitems = apiwikiWebLogic.headitems(appkey, platform, pnum);


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("range", new ScoreRangeDTO(list.getRange()));
            map.put("rows", list.getRows());
            map.put("pnum", ++pnum);
            map.put("headitems", headitems);
            jsonObject.put("result", map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping("/laud")
    @ResponseBody
    public String laud(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String archiveid = request.getParameter("archiveid");

        JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
        //check param
        if (StringUtil.isEmpty(archiveid) || StringUtil.isEmpty(profileId)) {
            return "laud([" + WanbaResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
        }

        try {
            Content content = AskServiceSngl.get().getContent(CommentUtil.genCommentId(archiveid, CommentDomain.CMS_COMMENT));
            if (content == null) {
                return "laud([" + WanbaResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }

            boolean bval = AskServiceSngl.get().addContentSum(content.getId(), ContentSumField.AGREE_NUM, profileId);
            if (!bval) {
                return "laud([" + WanbaResultCodeConstants.WANBA_ASK_ANSWER_AGREE_FAILED.getJsonString() + "])";
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return "laud([" + WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
        return "laud([" + jsonObject.toString() + "])";
    }


    @RequestMapping("/checklaud")
    @ResponseBody
    public String checklaud(HttpServletRequest request) {
        String profileId = HTTPUtil.getParam(request, "pid");
        String archiveid = request.getParameter("archiveid");

        JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
        //check param
        if (StringUtil.isEmpty(archiveid) || StringUtil.isEmpty(profileId)) {
            return "checklaud([" + WanbaResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
        }
        try {
            Content content = AskServiceSngl.get().getContent(CommentUtil.genCommentId(archiveid, CommentDomain.CMS_COMMENT));
            if (content == null) {
                return "checklaud([" + WanbaResultCodeConstants.PARAM_EMPTY.getJsonString() + "])";
            }
            boolean bval = AskServiceSngl.get().checkContentSum(content.getId(), ContentSumField.AGREE_NUM, profileId);
            if (bval) {
                return "checklaud([" + WanbaResultCodeConstants.WANBA_ASK_ANSWER_AGREE_FAILED.getJsonString() + "])";
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return "checklaud([" + WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString() + "])";
        }
        return "checklaud([" + jsonObject.toString() + "])";
    }

    @RequestMapping("/slideshow")
    @ResponseBody
    public String slideShow(HttpServletRequest request,
                            @RequestParam(value = "appkey", required = false, defaultValue = "2ojbX21Pd7WqJJRWmIniM0") String appkey,
                            @RequestParam(value = "platform", required = false, defaultValue = "0") String platform,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer pnum) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            List<ContentDTO> headitems = apiwikiWebLogic.headitems(appkey, platform, pnum);
            jsonObject.put("result", headitems);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

}
