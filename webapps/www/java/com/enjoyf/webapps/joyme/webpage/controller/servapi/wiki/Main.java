package com.enjoyf.webapps.joyme.webpage.controller.servapi.wiki;/**
 * Created by ericliu on 16/6/28.
 */

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.serv.usercenter.UserCenterCache;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import com.enjoyf.platform.util.memcached.MemCachedServer;
import com.qiniu.util.Json;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/28
 */
public class Main {

    private static final String HOST="http://servapi.joyme.alpha/";

    public static void main(String[] args) {
//        sendMobileCode("18511849086");
//        97146
/*
        new MemCachedManager(new MemCachedConfig(Props.instance().getServProps())).put("usercenterservice_mobile_code_18511849086", "1112",60);


        System.out.println(new MemCachedManager(new MemCachedConfig(Props.instance().getServProps())).get("usercenterservice_mobile_code_15652914865"));
*/

        verifyMobile("18511849086","29925");

//        System.out.println(UserCenterUtil.getUserLoginId("18511849086",LoginDomain.MOBILE));

//        register("18511849086","111111","1234_abc","85381");
        //{"token":"8929d79f-ad3a-4783-b2b6-f6d6a0ffd6a3","expires":"0"},"profile":{"uno":"0257fdb9-0ef4-434a-869a-1801c349dc35","profileid":"c54792acf5892d99186f5b33fe0bb4aa","icon":"","nick":"liuhaown","uid":"1000043","desc":"","logindomain":"mobile","flag":4096,"mobile":"","fresh":"true"}

//        login("18511849086","111111");//todo get user check bind mobile

//        logout();

//        bindMobile("18511849086","111111");

//        JSONObject jsonObject=auth();
//        System.out.println(jsonObject);

//        test bind mobile
//        JSONObject profile=(JSONObject)jsonObject.get("profile");
//        bindMobile("18310123005","123456","048f6a020baeaebd018ca37d930fa911");

        //test modifypassword profileid 9246cf063f1593b7e0d0d0c1f144f824
//        modifyPassowrd("111111","999999","9246cf063f1593b7e0d0d0c1f144f824");
//        System.out.println("after modify password currentpwd is 111111 so use 111111 login success");
//        login("18511849088","111111");
//        System.out.println("after modify password currentpwd is 111111 so use 999999 login error");
//        login("18511849088","999999");

        //test getprofile
//        getProfile("9246cf063f1593b7e0d0d0c1f144f824");

        //test modify mobile
//        modifyMobile("18511849089","18511849088","9246cf063f1593b7e0d0d0c1f144f824");
//        System.out.println("after modify password mobile is 18511849089 so use 18511849089 login success");
//        login("18511849089","111111");
//        System.out.println("after modify password mobile is 18511849089 so use 18511849088 login success");
//        login("18511849088","111111");

//        getProfile("9246cf063f1593b7e0d0d0c1f144f824");

//        getCity();
    }


    private static void sendMobileCode(String mobile){

        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/mobile/sendcode",new HttpParameter[]{
                new HttpParameter("mobile",mobile),
        },null));
    }

    private static void register(String mobile, String password, String nick, String mobilecode){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/register",new HttpParameter[]{
                new HttpParameter("loginkey",mobile),
                new HttpParameter("password",password),
                new HttpParameter("profilekey","www"),
                new HttpParameter("nick",nick),
                new HttpParameter("mobilecode",mobilecode),
                new HttpParameter("logindomain","mobile"),
        },null));
    }

    private static void login(String mobile,String password){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/login",new HttpParameter[]{
                new HttpParameter("loginkey",mobile),
                new HttpParameter("password",password),
                new HttpParameter("profilekey","www"),
                new HttpParameter("logindomain","mobile"),
        },null));
    }

    private static void logout(){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/logout",new HttpParameter[]{
        },null));
    }


    private static JSONObject auth(){
        HttpResult httpResult = new HttpClientManager().post(HOST + "/servapi/auth", new HttpParameter[]{
                new HttpParameter("otherid", MD5Util.Md5(UUID.randomUUID().toString())),
                new HttpParameter("logindomain", LoginDomain.CLIENT.getCode()),
                new HttpParameter("appkey", "default"),
        }, null);

       String object= httpResult.getResult();

        JSONObject jsonObject= JSONObject.fromObject(object);

        return jsonObject;
    }

    private static void bindMobile(String mobile, String password,String profileId){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth//bind/mobile",new HttpParameter[]{
                new HttpParameter("mobile",mobile),
                new HttpParameter("password",password),
                new HttpParameter("profilekey","www"),
                new HttpParameter("profileid",profileId),
        },null));
    }

    private static void modifyPassowrd(String password,String oldpwd,String profileId){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/modify/password",new HttpParameter[]{
                new HttpParameter("pwd",password),
                new HttpParameter("oldpwd",oldpwd),
                new HttpParameter("logindomain","mobile"),
                new HttpParameter("profileid",profileId),
        },null));
    }

    private static void modifyMobile(String mobile,String oldmobile,String profileId){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/modify/mobile",new HttpParameter[]{
                new HttpParameter("mobile",mobile),
                new HttpParameter("oldmobile",oldmobile),
                new HttpParameter("logindomain","mobile"),
                new HttpParameter("profileid",profileId),
        },null));
    }



    private static void verifyMobile(String mobile,String code){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/verify/mobile",new HttpParameter[]{
                new HttpParameter("mobile",mobile),
                new HttpParameter("mobilecode",code)
        },null));
    }



    private static void getProfile(String profileId){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/auth/getprofile",new HttpParameter[]{
                new HttpParameter("profileid",profileId),
        },null));
    }


    private static void getCity(){
        System.out.println(new HttpClientManager().post(HOST+"/servapi/config/cityinfo",new HttpParameter[]{
        },null));
    }

}
