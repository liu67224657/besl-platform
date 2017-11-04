package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.joymeapp.JoymeAppCommonParameterUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class AbstractAnimeBaseController extends BaseRestSpringController {

    private String flag = "";


    private JoymeAppClientConstant joymeAppClientConstant = null;

    protected JoymeAppClientConstant getJoymeAppClientConstant(HttpServletRequest request) {
        joymeAppClientConstant = JoymeAppCommonParameterUtil.geAppCommonParameter(request);

        if (joymeAppClientConstant == null) {
            return null;
        }
        //标志是否为沙盒环境,true为沙盒 非true为正式环境
        this.setFlag(StringUtil.isEmpty(request.getParameter("flag")) ? "" : request.getParameter("flag"));

        //截取appkey
        joymeAppClientConstant.setAppkey(getAppKey(joymeAppClientConstant.getAppkey()));

        return joymeAppClientConstant;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
