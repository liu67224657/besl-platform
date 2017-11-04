package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.webapps.common.util.YoukuCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

/**
 * Created by ericliu on 2015/6/2.
 */

public abstract class AbstractYoukuController extends BaseRestSpringController {


    protected static final String APPKEY = YoukuCookieUtil.YOUKU_APPKEY;
    protected static final String PREFIX_YOUKU_SIGN_ = "youku.sign";

    protected String getIdBytaskVerifyId(int taskVerifyId, String profileId, String cleintId) {
        return taskVerifyId == 0 ? profileId : cleintId;
    }

}
