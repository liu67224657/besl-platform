package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialActivity;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialContentAction;
import com.enjoyf.platform.service.content.social.SocialContentActionType;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-12
 * Time: 下午8:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/social/sharewap")
public class SocialShareWapController extends AbstractSocialAppBaseController {

    @RequestMapping("/content")
    public ModelAndView socialShareWap(HttpServletRequest request, @RequestParam(value = "cid", required = false) String cid,
                                       @RequestParam(value = "uno", required = false) String uno) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Long lcid = Long.valueOf(cid);
        try {

            if (AppUtil.checkIsIOS(request)) {
                mapMessage.put("platform", "0");
            } else {
                mapMessage.put("platform", "1");
            }
            SocialContent socialContent = ContentServiceSngl.get().getSocialContentByContentId(lcid);
            if (socialContent == null) {
                return new ModelAndView("/views/jsp/share/social-share", mapMessage);
            }
            if (socialContent.getAudioLen() != 0) {
                int audiolen = (int) socialContent.getAudioLen();
                int fen = audiolen / 1000 / 60;
                int miao = audiolen / 1000 % 60;
                mapMessage.put("fen", fen);
                mapMessage.put("miao", miao);
            }
            if (socialContent.getActivityId() != 0) {
                SocialActivity socialActivity = ContentServiceSngl.get().getSocialActivity(socialContent.getActivityId());
                mapMessage.put("socialActivity", socialActivity);
            }
            mapMessage.put("socialContent", socialContent);
//            Profile profile = ProfileServiceSngl.get().getProfileByUno(socialContent.getUno());
            SocialProfile socialProfile = ProfileServiceSngl.get().getSocialProfileByUno(socialContent.getUno());
            mapMessage.put("profile", socialProfile);
            NextPagination np = new NextPagination(0, 5, true);
            NextPageRows<SocialContentAction> sa = ContentServiceSngl.get().querySocialContentAction(lcid, SocialContentActionType.AGREE, np);
            if (sa.getRows() != null) {
                Set<String> unoSet = new HashSet<String>();
                for (SocialContentAction socialContentAction : sa.getRows()) {
                    unoSet.add(socialContentAction.getUno());
                }
                List<SocialProfile> profileList = ProfileServiceSngl.get().querySocialProfilesByUnos(unoSet);
                mapMessage.put("profileList", profileList);
            } else {
                mapMessage.put("profileList", "");
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/share/social-share", mapMessage);
    }
}
