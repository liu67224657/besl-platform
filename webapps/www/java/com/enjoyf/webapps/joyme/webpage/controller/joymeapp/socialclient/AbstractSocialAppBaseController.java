package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.socialclient;

import com.enjoyf.platform.service.profile.ProfileActiveStatus;
import com.enjoyf.platform.service.profile.ProfileBlogField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-12
 * Time: 上午9:39
 * To change this template use File | Settings | File Templates.
 */
public class AbstractSocialAppBaseController extends BaseRestSpringController {

    protected ResultObjectMsg checkSocialProfile(SocialProfile profile, ResultObjectMsg msg) throws ServiceException {
        if (profile == null || profile.getBlog() == null) {
            msg.setRs(ResultCodeConstants.SOCIAL_PROFILE_IS_NULL.getCode());
            msg.setMsg(ResultCodeConstants.SOCIAL_PROFILE_IS_NULL.getMsg());
            return msg;
        }
        //限制登陆 or 封号
        ProfileActiveStatus activeStatus = checkProfileStatus(profile.getBlog());
        if (activeStatus.equals(ProfileActiveStatus.FORBID_LOGIN)) {
            msg.setRs(ResultCodeConstants.SOCIAL_PROFILE_HAS_FORBID.getCode());
            msg.setMsg(ResultCodeConstants.SOCIAL_PROFILE_HAS_FORBID.getMsg());
            return msg;
        } else if (activeStatus.equals(ProfileActiveStatus.BAN)) {
            msg.setRs(ResultCodeConstants.SOCIAL_PROFILE_HAS_BAN.getCode());
            msg.setMsg(ResultCodeConstants.SOCIAL_PROFILE_HAS_BAN.getMsg());
            return msg;
        }
        msg.setRs(ResultObjectMsg.CODE_S);
        return msg;
    }

    private ProfileActiveStatus checkProfileStatus(SocialProfileBlog profileBlog) {
        ProfileActiveStatus activeStatus = profileBlog.getActiveStatus();
        Date inactiveTillDate = profileBlog.getInactiveTillDate();
        Date now = new Date();
        if ((activeStatus.equals(ProfileActiveStatus.FORBID_LOGIN) || activeStatus.equals(ProfileActiveStatus.FORBID_POST)) && now.after(inactiveTillDate)) {
            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileBlogField.INACTIVETILLDATE, null)
                    .set(ProfileBlogField.ACTIVESTATUS, ProfileActiveStatus.INIT);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ProfileBlogField.UNO, profileBlog.getUno()));
            try {
                ProfileServiceSngl.get().modifyBlog(profileBlog.getUno(), updateExpress, queryExpress, true);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " checkProfileStatus.modifyBlog activestatus.occured Exception.", e);
            }
            return ProfileActiveStatus.INIT;
        }

        return activeStatus;
    }

    protected String getAppKey(String appKey) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }
}
