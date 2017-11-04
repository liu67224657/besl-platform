package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/3
 * Description: 给接口用的验证拦截器
 */
public class PassportApiAuthInterceptor extends AbstractPassportAuthInterceptor {

    private Logger logger = LoggerFactory.getLogger(PassportApiAuthInterceptor.class);

    protected boolean handle(HttpServletRequest request, HttpServletResponse response, ResultWap result) {
        if (result.getResult().getCode() == ResultCodeConstants.SUCCESS.getCode()) {
            return true;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(result.getResult().getCode()));
            jsonObject.put("msg", result.getResult().getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
        } catch (Exception e) {
            logger.error("write not PassportWebAuthInterceptor.", e);
        }

        return false;
    }


}
