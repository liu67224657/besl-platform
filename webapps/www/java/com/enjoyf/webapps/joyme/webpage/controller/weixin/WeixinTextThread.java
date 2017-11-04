package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.weixin.WeixinUtil;
import net.sf.json.JSONObject;

/**
 * Created by zhitaoshi on 2016/2/1.
 */
public class WeixinTextThread extends Thread {

    private String openid;
    private String content;
    private String appid;
    private String secret;
    private RedisManager redisManager;

    public WeixinTextThread(String openid, String content) {
        this.openid = openid;
        this.content = content;
    }

    public WeixinTextThread(String fromUserName, String content, String appid, String secret, RedisManager redisManager) {
        this.openid = fromUserName;
        this.content = content;
        this.appid = appid;
        this.secret = secret;
        this.redisManager = redisManager;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void run() {
        try {
            String accessToken = MiscServiceSngl.get().getAccessTokenCache(appid, secret);
            if(accessToken == null){
                return;
            }
            //获取用户信息
            String GETUSERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
            GETUSERINFO = GETUSERINFO.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
            JSONObject jsonObject = WeixinUtil.httpRequest(GETUSERINFO, "GET", null);
            if (!jsonObject.containsKey("errcode")) {
                JSONObject userObj = new JSONObject();
                userObj.put("nickname", jsonObject.get("nickname"));
                userObj.put("city", jsonObject.get("city"));
                userObj.put("country", jsonObject.get("country"));
                userObj.put("openid", jsonObject.get("openid"));
                userObj.put("province", jsonObject.get("province"));
                userObj.put("content", content);
                userObj.put("timemillis", System.currentTimeMillis());
                redisManager.zadd("weixin_user_messages", System.currentTimeMillis()/1000, userObj.toString(), -1);
            }
        } catch (Exception e) {
        }
    }
}
