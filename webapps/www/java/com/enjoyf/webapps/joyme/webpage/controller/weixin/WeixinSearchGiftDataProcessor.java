package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.util.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-22
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class WeixinSearchGiftDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory
            .getLogger(WeixinSearchGiftDataProcessor.class);

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

        List<Article> articleList = new ArrayList<Article>();
        Article article = new Article();
        article.setTitle("搜礼包");
        article.setDescription("点击进入搜索页面“淘”礼包");
        article.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/search.jpg");
        article.setUrl("http://www.joyme.com/gift/wap/searchpage?openid=" + fromUserName);
        articleList.add(article);


        resNewsMessage.setArticleCount(articleList.size());
        resNewsMessage.setArticles(articleList);
        // respMessage是返回值， 这里面的是单图文回复
        respMessage = MessageUtil
                .newsMessageToXml(resNewsMessage);
        logger.info(this.getClass().getName() + " Return respMessage:" + respMessage);

        return respMessage;
    }
}
