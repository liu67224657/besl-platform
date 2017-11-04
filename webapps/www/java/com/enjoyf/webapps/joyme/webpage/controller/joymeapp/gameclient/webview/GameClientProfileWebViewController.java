package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.ProfileRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.usercenter.ProfileDTO;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/18
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/webview/profile")
public class GameClientProfileWebViewController extends AbstractGameClientBaseController {

    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;

    /**
     * TA喜欢的人
     */
    @RequestMapping("/likelist")
    public ModelAndView likeList(HttpServletRequest request) {
        Map map = new HashMap();
        String uidParam = HTTPUtil.getParam(request, "uid");
        String desUid = HTTPUtil.getParam(request, "desuid");
        map.put("uid", uidParam);
        map.put("desuid", desUid);
        String queryUid = ((StringUtil.isEmpty(desUid) || desUid == "null") ? uidParam : desUid);
        if (StringUtil.isEmpty(queryUid)) {
            //todo error
            return null;
        }

        long uid = -1;
        try {
            uid = Long.parseLong(queryUid);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            //todo error
            return null;
        }

        try {
            Pagination pagination = this.getPaginationbyRequest(request);

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                //todo error
                return null;
            }
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
            map.put("likesum", "");
            map.put("likedsum", "");
            List<ProfileDTO> likeProfileList = new ArrayList<ProfileDTO>();
            //喜欢的人
            PageRows<ProfileRelation> LikeRelationList = SocialServiceSngl.get().querySrcRelation(profile.getProfileId(), ObjectRelationType.PROFILE, pagination);
            if (LikeRelationList == null) {
                map.put("priflelist", likeProfileList);
                map.put("page", pagination);
                return new ModelAndView("/views/jsp/gameclient/webview/profilelike-list", map);
            }
            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : LikeRelationList.getRows()) {
                prifileIdSet.add(relation.getDestProfileId());
            }

            //组装dto
            Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);
            for (ProfileRelation relation : LikeRelationList.getRows()) {
                if (profileDTOMap.containsKey(relation.getDestProfileId())) {
                    likeProfileList.add(profileDTOMap.get(relation.getDestProfileId()));
                }
            }

            map.put("priflelist", likeProfileList);
            map.put("page", LikeRelationList.getPage());
            return new ModelAndView("/views/jsp/gameclient/webview/profilelike-list", map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            //todo
            return null;
        }

    }

    /**
     * 喜欢TA的人
     */
    @RequestMapping("/likedlist")
    public ModelAndView likedList(HttpServletRequest request) {
        Map map = new HashMap();
        String uidParam = HTTPUtil.getParam(request, "uid");
        String desUid = HTTPUtil.getParam(request, "desuid");
        map.put("uid", uidParam);
        map.put("desuid", desUid);
        String queryUid = ((StringUtil.isEmpty(desUid) || desUid == "null") ? uidParam : desUid);
        if (StringUtil.isEmpty(queryUid)) {
            //todo error
            return null;
        }

        long uid = -1;
        try {
            uid = Long.parseLong(queryUid);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            //todo error
            return null;
        }

        try {
            Pagination pagination = this.getPaginationbyRequest(request);

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                //todo error
                return null;
            }
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
            map.put("likesum", "");
            map.put("likedsum", "");
            List<ProfileDTO> likedProfileList = new ArrayList<ProfileDTO>();
            //喜欢的人
            PageRows<ProfileRelation> likedRelationList = SocialServiceSngl.get().queryDestRelation(profile.getProfileId(), ObjectRelationType.PROFILE, pagination);
            if (likedRelationList == null) {
                map.put("priflelist", likedRelationList);
                map.put("page", pagination);
                return new ModelAndView("/views/jsp/gameclient/webview/profileliked-list", map);
            }
            Set<String> prifileIdSet = new HashSet<String>();
            for (ProfileRelation relation : likedRelationList.getRows()) {
                prifileIdSet.add(relation.getSrcProfileId());
            }

            //组装dto
            Map<String, ProfileDTO> profileDTOMap = userCenterWebLogic.buildProfileDTOByProfileIDs(prifileIdSet);
            for (ProfileRelation relation : likedRelationList.getRows()) {
                if (profileDTOMap.containsKey(relation.getSrcProfileId())) {
                    likedProfileList.add(profileDTOMap.get(relation.getSrcProfileId()));
                }
            }

            map.put("priflelist", likedProfileList);
            map.put("page", likedRelationList.getPage());
            return new ModelAndView("/views/jsp/gameclient/webview/profileliked-list", map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            //todo
            return null;
        }

    }
}
