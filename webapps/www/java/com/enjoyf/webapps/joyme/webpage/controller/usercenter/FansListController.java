package com.enjoyf.webapps.joyme.webpage.controller.usercenter;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.usercenter.ProfileSumField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserRelationWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:43
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/usercenter/fans")
public class FansListController extends BaseRestSpringController {
    private static final Logger logger = LoggerFactory.getLogger(FansListController.class);

    @Resource(name = "userRelationWebLogic")
    private UserRelationWebLogic userRelationWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request) throws ServiceException {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String profileId = request.getParameter("profileid");
        String qflag = request.getParameter("qflag");
        String pno = request.getParameter("p");

        int pnum = 1;
        try {
            pnum = Integer.parseInt(pno);
        } catch (NumberFormatException e) {
        }
        if (!StringUtil.isEmpty(profileId)) {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            mapMessage.put("profileId", profileId);
            String sessionPid = userCenterSession != null ? userCenterSession.getProfileId() : "";
            if (!StringUtil.isEmpty(sessionPid) && profileId.equals(sessionPid)) {
                return new ModelAndView("redirect:/usercenter/fans/mylist");
            }

            Pagination page = new Pagination(pnum * 10, pnum, 10);
            PageRows<UserinfoDTO> scoreRangeRows = userRelationWebLogic.queryFansUserinfoProfileid(profileId, ObjectRelationType.WIKI_PROFILE, page, sessionPid,true);
            mapMessage.put("rows", scoreRangeRows.getRows());
            mapMessage.put("page", scoreRangeRows.getPage());

//            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profileId);
//            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserinfoDTO currentUser = userRelationWebLogic.getUser(profileId);
            if(currentUser.getFans()!=scoreRangeRows.getPage().getTotalRows()) {
                int increNum = scoreRangeRows.getPage().getTotalRows()-currentUser.getFans();
                currentUser.setFans(scoreRangeRows.getPage().getTotalRows());
                UserCenterServiceSngl.get().increaseProfileSum(profileId, ProfileSumField.FANSSUM,increNum);
            }
            mapMessage.put("profile", currentUser);
            mapMessage.put("followCount", currentUser.getFollows());
            mapMessage.put("fansCount", currentUser.getFans());
        }
        return new ModelAndView("/views/jsp/usercenter/fans-list", mapMessage);
    }

    @RequestMapping(value = "/mylist")
    public ModelAndView mylist(HttpServletRequest request) throws ServiceException {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String pno = request.getParameter("p");
        int pnum = 1;
        try {
            pnum = Integer.parseInt(pno);
        } catch (NumberFormatException e) {
        }

        UserCenterSession userCenterSession = getUserCenterSeesion(request);

        String sessionPid = userCenterSession != null ? userCenterSession.getProfileId() : "";
        mapMessage.put("profileId", sessionPid);

        Pagination page = new Pagination(pnum * 10, pnum, 10);
        PageRows<UserinfoDTO> scoreRangeRows = userRelationWebLogic.queryFansUserinfoProfileid(sessionPid, ObjectRelationType.WIKI_PROFILE, page, sessionPid,true);
        mapMessage.put("rows", scoreRangeRows.getRows());
        mapMessage.put("page", scoreRangeRows.getPage());

        //ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(sessionPid);
        //Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(sessionPid);
        UserinfoDTO currentUser = userRelationWebLogic.getUser(sessionPid);
        // 如果数据不一致，进行修复，以列表数据为准
        if(currentUser.getFans()!=scoreRangeRows.getPage().getTotalRows()) {
            int increNum = scoreRangeRows.getPage().getTotalRows()-currentUser.getFans();
            currentUser.setFans(scoreRangeRows.getPage().getTotalRows());
            UserCenterServiceSngl.get().increaseProfileSum(sessionPid, ProfileSumField.FANSSUM,increNum);
        }
        mapMessage.put("profile", currentUser);
        mapMessage.put("followCount", currentUser.getFollows());
        mapMessage.put("fansCount", currentUser.getFans());
        return new ModelAndView("/views/jsp/usercenter/myfans-list", mapMessage);
    }

}
