package com.enjoyf.webapps.joyme.webpage.util;

import com.enjoyf.platform.service.usercenter.Token;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证是否是正常的访问
 * Created by zhimingli on 2015/9/8.
 */
public class CheckAuthUtil {
    /**
     * true按正常业务逻辑处理
     * false走异常提醒
     *
     * @return
     */
    public static boolean isNormalRequest(String tokenStr, HttpServletRequest request, boolean isYouku) {
        boolean flag = false;
        if (StringUtil.isEmpty(tokenStr)) {
            return flag;
        }

        try {
            //token不存在
            Token token = UserCenterServiceSngl.get().getToken(tokenStr);
            if (token == null) {
                return flag;
            }
            String requestParam = token.getRequest_parameter();
            if (StringUtil.isEmpty(requestParam)) {
                return flag;
            }
            JSONObject jsonObject = JSONObject.fromObject(requestParam);
            if (isYouku) {
                //idfa
                String idfa = HTTPUtil.getParam(request, "idfa");
                if (!idfa.equals(jsonObject.getString("idfa"))) {
                    return flag;
                }
                //source 设备来源：0-正常,未知 1-越狱或root | mock 模拟：0-正常,未知 1-模拟器 2-apk模拟器
                if (!"0".equals(jsonObject.getString("source")) || !"0".equals(jsonObject.getString("mock"))) {
                    return flag;
                }
                //channelid
                String channelid = HTTPUtil.getParam(request, "channelid");
                if (!channelid.equals(jsonObject.getString("channelid"))) {
                    return flag;
                }
            } else {
                if (!"0".equals(jsonObject.getString("mock"))) {
                    return flag;
                }
            }


            //clientid
//            String clientid = HTTPUtil.getParam(request, "clientid");
//            if (!clientid.equals(jsonObject.getString("clientid"))) {
//                return flag;
//            }
            //uid
            Long uid = token.getUid();
            String uidParam = HTTPUtil.getParam(request, "uid");
            if (uid != Long.parseLong(uidParam)) {
                return flag;
            }
        } catch (Exception e) {
            GAlerter.lab("YoukuAuthUtil occured ServiceException.e: ", e);
            return flag;
        }
        return true;
    }

    public static void main(String[] args) {
        //isYokuRequest("b2902cad-4a88-4127-96fc-81c80e7bf331", null);
        String requestParam = "{\"platform\":\"0\",\"mock\":\"1\",\"otherid\":\"E3834B23-9CAE-49A3-BA83-C6D450846B22\"," +
                "\"appkey\":\"08pkvrWvx5ArJNvhYf19kN\"," +
                "\"yktk\":\"1%7C1434426667%7C15%7CaWQ6Mzc5NDQ3NTQ1LG5uOmFudG9ueTUxNix2aXA6ZmFsc2UseXRpZDozNzk0NDc1NDUsdGlkOjA%3D%7C6511532db1b6a5d7e3768cb49afcff76%7Cc079d6717134a9480ae8934ab56d576efbf36320%7C1\"," +
                "\"version\":\"1\",\"channelid\":\"appstore\",\"clientid\":\"E3834B23-9CAE-49A3-BA83-C6D450846B22\"," +
                "\"time\":\"1441680330608\",\"entry\":\"0\",\"logindomain\":\"client\",\"idfa\":\"aaaa\"}";
        JSONObject jsonObject = JSONObject.fromObject(requestParam);
        System.out.println(jsonObject.getString("mock"));
    }
}
