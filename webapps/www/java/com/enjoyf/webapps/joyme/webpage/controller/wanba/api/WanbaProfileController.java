package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.MyDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientTagDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaProfileWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaTagWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/20
 */
@Controller
@RequestMapping("/wanba/api/profile")
public class WanbaProfileController extends BaseRestSpringController {
    private static final int PROFILE_SIZE = 10;


    private static final long ALL_TAG_ID = -20000;

    @Resource
    private WanbaProfileWebLogic wanbaProfileWebLogic;


    @Resource
    private WanbaTagWebLogic wanbaTagWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    /**
     * 达人标签列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listtag")
    public String listtag(HttpServletRequest request) {

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        List<GameClientTagDTO> returnRows = wanbaTagWebLogic.getTagList(request, AnimeTagAppType.WANBA_VERIFYPROFILE);

        Map map = new HashMap();
        map.put("rows", returnRows);

        jsonObject.put("result", map);
        return jsonObject.toString();
    }


    /**
     * 达人列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listbytag")
    public String listbytag(HttpServletRequest request,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                            @RequestParam(value = "pcount", required = false, defaultValue = "20") String pcount) {

        int pageNo = 1;
        int pageSize = 1;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.valueOf(pcount);
        } catch (NumberFormatException ignored) {
        }
        String tagIdStr = request.getParameter("tagid");
        long tagId = -1l;
        try {
            tagId = Long.parseLong(tagIdStr);

            //全部在line里面是-1
            if (tagId == ALL_TAG_ID) {
                tagId = -1l;
            }
        } catch (NumberFormatException ignored) {
        }

        try {
            PageRows<WanbaProfileDTO> pageRows = wanbaProfileWebLogic.queryProfileByTagId(tagId, new Pagination(pageSize * pageNo, pageNo, pageSize));

            //checkis 1 iqid 不为空 check invit status
            String checkis = request.getParameter("checkis");
            String iqid = request.getParameter("iqid");
            if (!StringUtil.isEmpty(checkis) && checkis.equals("1") && !StringUtil.isEmpty(iqid)
                    && pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                long inviteQuestionId = -1l;
                try {
                    inviteQuestionId = Long.parseLong(iqid);
                } catch (NumberFormatException ignored) {
                }


                if (inviteQuestionId > 0l) {
                    try {
                        wanbaProfileWebLogic.setInviteStatusByQuestion(inviteQuestionId, pageRows.getRows());
                    } catch (ServiceException e) {
                        GAlerter.lab(this.getClass().getName() + " check profile invite status occured error.e:", e);
                    }
                }
            }

            JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }


    @RequestMapping(value = "/my")
    @ResponseBody
    public String my(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String appkey = HTTPUtil.getParam(request, "appkey");
            String pid = HTTPUtil.getParam(request, "pid");
            String querypid = HTTPUtil.getParam(request, "querypid");

            if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(pid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            //查询别人的时候用querypid
            if (!StringUtil.isEmpty(querypid)) {
                pid = querypid;
            }


            UserPoint userPoint = pointWebLogic.getUserPoint(AppUtil.getAppKey(appkey), pid);
            MyDTO returnMyDTO = wanbaProfileWebLogic.getMyDTO(userPoint, pid);

            Map map = new HashMap();
            map.put("profile", returnMyDTO.getProfile());
            map.put("profilesum", returnMyDTO.getProfilesum());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/checkforbid")
    public String checkforbid(HttpServletRequest request) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String pid = HTTPUtil.getParam(request, "pid");
            if (StringUtil.isEmpty(pid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            //禁言
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(pid);
            if (forbid != null) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }
}
