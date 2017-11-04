package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-22
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class WeixinGiftmarketDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory
            .getLogger(WeixinGiftmarketDataProcessor.class);

    @Override
    public String processRequest(Map<String, String> requestMap) {
        String respMessage = "";

        String respContent = ""; //默认回复

        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");

        RespNewsMessage resNewsMessage = new RespNewsMessage();
        resNewsMessage.setFromUserName(toUserName);
        resNewsMessage.setToUserName(fromUserName);
        resNewsMessage.setCreateTime(new Date().getTime());
        resNewsMessage.setFuncFlag(0);
        resNewsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

        //生成token，每次生成的token会不一样
        String token = WeixinUtil.saveToken(fromUserName);

        //将token和openid作为参数用get方式传递到链接中
        List<Article> articleList = new ArrayList<Article>();

        Article article = new Article();

        article.setTitle("新礼包刚刚上架 — 点击领取");
        article.setDescription("海量礼包等你领，免登陆哟！");
        article.setPicUrl("http://joymepic.joyme.com/o_1aj0v0itu1ed1j15k671od110n79.jpeg");
        article.setUrl("http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
        articleList.add(article);

        Article article2 = new Article();
        article2.setTitle("【精选】热门手游礼包");
        article2.setPicUrl("http://joymepic.joyme.com/qiniu/original/2016/04/91/825cc4c201b2704fed0a5f309f120199921f.png");
        article2.setUrl("http://www.joyme.com/giftmarket/wap/hotactivity?openid=" + fromUserName + "&token=" + token);
        articleList.add(article2);

        Article article3 = new Article();
        article3.setTitle("下载APP 天天活动拿福利");
        article3.setPicUrl("http://joymepic.joyme.com/qiniu/original/2016/11/48/87deeb670310d044f20ac940ed99158030f6.jpg");
        article3.setUrl("http://wanba.joyme.com/");
        articleList.add(article3);
        resNewsMessage.setArticleCount(articleList.size());
        resNewsMessage.setArticles(articleList);
        // respMessage是返回值， 这里面的是单图文回复
        respMessage = MessageUtil
                .newsMessageToXml(resNewsMessage);
        logger.info(this.getClass().getName() + " Return respMessage:" + respMessage);

        return respMessage;
    }
}
