package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
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
public class WeixinSubscribeDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory
            .getLogger(WeixinSubscribeDataProcessor.class);

    /**
     * 关注回复
     *
     * @param requestMap
     * @return
     */
    @Override
    public String processRequest(Map<String, String> requestMap) {
        String respMessage = "";

        String respContent = ""; //默认回复

        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");

        // // 回复文本消息
        RespTextMessage textMessage = new RespTextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);

        StringBuffer buffer = new StringBuffer();

        buffer.append("只有着迷，才有乐趣\r\n");
        buffer.append("Hi，很高兴你已经关注着迷哦!（鲜花表情），着迷是中国领先的泛游戏兴趣聚合平台，这里每天推送精华资讯，还有免费游戏礼包等你来拿！\r\n");
        buffer.append("回复“着迷玩霸”下载着迷玩霸APP；回复“礼包”免费领取最新独家礼包。");
        textMessage.setContent(buffer.toString());
        respMessage = MessageUtil.textMessageToXml(textMessage);
//        String token = Md5Utils.md5(UUID.randomUUID().toString());
//        try {
//            UserCenterServiceSngl.get().saveMobileCode(token, fromUserName);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
//        }
//
//        List<Article> articleList = null;
//        RespNewsMessage resNewsMessage = new RespNewsMessage();
//        resNewsMessage.setFromUserName(toUserName);
//        resNewsMessage.setToUserName(fromUserName);
//        resNewsMessage.setCreateTime(new Date().getTime());
//        resNewsMessage.setFuncFlag(0);
//        resNewsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//
//        articleList = new ArrayList<Article>();
//
//
//        Article article = new Article();
//        article.setTitle("着迷，才有乐趣");
//        article.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/joymelog.jpg");
//        article.setUrl("http://www.joyme.com/");
//        articleList.add(article);
//
//        Article article1 = new Article();
//        article1.setTitle("【活动】着迷玩霸一周年活动");
//        article1.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/newsicon.png");
//        article1.setUrl("http://t.cn/RAWeRA6");
//        articleList.add(article1);
//
//        Article article4 = new Article();
//        article4.setTitle("【活动】滴滴打车新人红包");
//        article4.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/didicar.png");
//        article4.setUrl("http://pay.diditaxi.com.cn/activity/hongbao/b_business/router/?codekey=6d12853b882759e23c56f77dc1bfe7ab");
//
//        articleList.add(article4);
//
//        Article article3 = new Article();
//        article3.setTitle("【WIKI】 权威手游资料库");
//        article3.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/wikiicon.png");
//        article3.setUrl("http://www.joyme.com/wechat/strategy/index.html");
//        articleList.add(article3);
//
//
//        Article article2 = new Article();
//        article2.setTitle("【双旦】着迷惊喜，礼包福利");
//        article2.setPicUrl("http://joymepic.joyme.com/qiniu/original/2015/12/74/eb5052ea092ca049d70ac3204dcd7b0da06d.jpg");
//        article2.setUrl("http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
//        articleList.add(article2);
//
//
//        resNewsMessage.setArticleCount(articleList.size());
//        resNewsMessage.setArticles(articleList);
//        // respMessage是返回值， 这里面的是多图文回复
//        respMessage = MessageUtil
//                .newsMessageToXml(resNewsMessage);
        logger.info(this.getClass().getName() + " Return respMessage:" + respMessage);

        return respMessage;
    }
}
