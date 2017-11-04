package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-22
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public interface WeixinDataProcessor {
    public String processRequest(Map<String, String> requestMap);
}
