/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth.test;

import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.sql.UUIDUtil;

import java.util.Date;
import java.util.UUID;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 下午5:27
 * Description:
 */
public class Main {

    //
    public static void main(String[] args) {

//        AuthApp app = new AuthApp();
//
//        app.setAppId(getShortUUID());
//        app.setAppType(AuthAppType.INTERNAL_CLIENT);
//        app.setAppKey(getUUID());
//        app.setAuditStatus(ActStatus.ACTED);
//        app.setValidStatus(ValidStatus.VALID);
//        app.setAppName("我叫MTonline-IOS");

        //
        try {
         //  OAuthServiceSngl.get().appplyAuthApp(app);
          //  System.out.println("aaaaaaaaaaaaaaa");
//            for(int i =1;i < 2; i++){
//                 OAuthServiceSngl.get().generaterOauthInfo(getShortUUID(),"appKey"+i);
//            }

//             OAuthInfo oAuthInfo = new OAuthInfo();
//
//                oAuthInfo.setAccess_token("");
//                oAuthInfo.setApp_key("appKey");


//            String accessKey = "e762ff0a0d75a04d76089010dad9dcfd8fba";
//            String appKey ="appKey1";
//            OAuthInfo oAuthInfo = OAuthServiceSngl.get().getOAuthInfoByAccessToken(accessKey, appKey);
//            System.out.println(oAuthInfo.getUno());

//            String refreshKey = "12d0d71e0875b046990b2e7002791cbc53ec";
//            String appKey2 ="appKey52";
//             OAuthInfo oAuthInfo2 = OAuthServiceSngl.get().getOAuthInfoByRereshToken(refreshKey, appKey2);
//            System.out.println(oAuthInfo2.getUno());

            
            
//            OAuthServiceSngl.get().getApp(app.getAppId());
//
//            token = OAuthServiceSngl.get().applyToken(token);
//
//            OAuthServiceSngl.get().getToken(token.getToken(), token.getTokenType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getShortUUID() {
        return UUIDUtil.getShortUUID().toString().replaceAll("-","0").replaceAll("_","0");
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","0").replaceAll("_","0");
    }
}
