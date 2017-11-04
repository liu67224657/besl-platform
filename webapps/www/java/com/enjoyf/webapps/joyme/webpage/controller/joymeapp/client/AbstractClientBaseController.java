package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client;

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
 * User: zhitaoshi
 * Date: 15-3-2
 * Time: 下午15:17
 * To change this template use File | Settings | File Templates.
 */
public class AbstractClientBaseController extends BaseRestSpringController {

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
