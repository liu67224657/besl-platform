package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.weixin.MessageUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
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
public class WeixinExclusiveDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory
            .getLogger(WeixinExclusiveDataProcessor.class);

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
        article.setTitle("热门礼包 着迷独家奉送");
        article.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/giftmarket/giftmarket20140721.png");
        article.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx758f0b2d30620771&redirect_uri=http://www.joyme.com/giftmarket/wap&response_type=code&scope=snsapi_base#wechat_redirect");
        articleList.add(article);

        Pagination pagination = new Pagination(1 * 5, 1, 5);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                .add(QueryCriterions.eq(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                .add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        GiftMarketWebLogic giftMarketWebLogic = new GiftMarketWebLogic();
        PageRows<ActivityDTO> pageRows = null;
        try {
            pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return null;
        }
        if (pageRows != null) {
            for (ActivityDTO activityDTO : pageRows.getRows()) {
                Article article2 = new Article();
                article2.setTitle(activityDTO.getTitle());
                article2.setPicUrl(activityDTO.getGipic());
                article2.setUrl("http://www.joyme.com/giftmarket/wap/giftdetail?aid=" + activityDTO.getGid() + "&openid=" + fromUserName);
                articleList.add(article2);
            }
        }
        resNewsMessage.setArticleCount(articleList.size() + 1);
        resNewsMessage.setArticles(articleList);
        // respMessage是返回值， 这里面的是单图文回复
        respMessage = MessageUtil
                .newsMessageToXml(resNewsMessage);
        logger.info(this.getClass().getName() + " Return respMessage:" + respMessage);

        return respMessage;
    }
}
