package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午12:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class JoymeAppBaseController extends BaseRestSpringController {

    protected static final int DEFAULT_PAGE_SIZE = 20;

    protected Pagination getPaginationbyRequest(HttpServletRequest request) {
        String pageNo = request.getParameter("pnum");
        String pageCount = request.getParameter("pcount");

        int pagNum = 1;
        try {
            pagNum = Integer.parseInt(pageNo);
        } catch (NumberFormatException e) {
        }
        int pageSize = 24;
        try {
            pageSize = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
        }

        return new Pagination(pagNum * pageSize, pagNum, pageSize);
    }


    protected static String getAppKey(String appKey) {
        if (appKey.length() < 23) {
            return appKey;
        }

        return appKey.substring(0, appKey.length() - 1);
    }

    protected int getPlatformByAppKey(String appKey) {
        if (appKey.length() < 23) {
            return -1;
        } else if (appKey.endsWith("I")) {
            return 0;
        } else if (appKey.endsWith("A")) {
            return 1;
        }

        return -1;
    }

    protected AppPlatform getPlatformByPlatformStr(String platform) {
        AppPlatform appPlatform = null;
        if (!StringUtil.isEmpty(platform)) {
            try {
                appPlatform = AppPlatform.getByCode(Integer.parseInt(platform));
            } catch (NumberFormatException e) {
            }
        }

        return appPlatform;
    }
}
