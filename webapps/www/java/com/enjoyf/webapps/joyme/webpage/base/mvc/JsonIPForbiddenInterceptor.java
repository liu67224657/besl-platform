/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.EncodeUtils;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Author: zhaoxin
 * Date: 12-4-13
 * Time: 下午4:04
 * Desc:
 */
public class JsonIPForbiddenInterceptor extends AbstractIPForbiddenInterceptor{

    private Logger logger = LoggerFactory.getLogger(JsonIPForbiddenInterceptor.class);

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @Override
    protected void handleForbidPost(HttpServletRequest request, HttpServletResponse response) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void handleForbidLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("json forbidlogin....");

        List<String> resultList = new ArrayList<String>();

        String reurl = EncodeUtils.urlDecode(HTTPUtil.getRedr(request)).trim();

        if(!StringUtil.isEmpty(reurl) && ("hot".endsWith(reurl) ||
                "talent".endsWith(reurl)||
                "discovery".endsWith(reurl))) {

            resultList.add(HTTPUtil.getRedr(request));
        } else{
            resultList.add("/index");
        }

        //user.ipforbidden.forbidlogin
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_FORBIP, i18nSource.getMessage("user.ipforbidden.forbidlogin", new Object[]{}, Locale.CHINA), resultList);
        try {
            HTTPUtil.writeJson(response, JsonBinder.buildNonNullBinder().toJson(resultMsg));
        } catch (IOException e) {
            logger.error("write not handleForbidLogin.", e);
        }
    }
}
