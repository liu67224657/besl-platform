package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class WeixinTextDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WeixinTextDataProcessor.class);
    private static RedisManager redisManager = new RedisManager(WebappConfig.get().getProps());
    private static final String APPID = "wxedaaf0b0315d44e7";   //订阅号
    private static final String SECRET = "afde3c1cd927e508f663b92e6a084b6b ";   //订阅号

    @Override
    public String processRequest(Map<String, String> requestMap) {
        String respMessage = "";

        String respContent = ""; //默认回复

        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");

        String content = requestMap.get("Content"); //文本消息内容

        //tools记录
        try {
            WeixinTextThread thread = new WeixinTextThread(fromUserName, content, APPID, SECRET, redisManager);
            thread.start();
        } catch (Exception e) {
            GAlerter.lab("WeixinTextThread occur Exception.e", e);
        }

        // // 回复文本消息
        RespTextMessage textMessage = new RespTextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);

        //回复图文消息
        RespNewsMessage resNewsMessage = new RespNewsMessage();
        resNewsMessage.setFromUserName(toUserName);
        resNewsMessage.setToUserName(fromUserName);
        resNewsMessage.setCreateTime(new Date().getTime());
        resNewsMessage.setFuncFlag(0);
        resNewsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

        String token = WeixinUtil.saveToken(fromUserName);
        if ("玩霸".equals(content) || "着迷玩霸".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("下载玩霸——参与问答免费拿福利！");
            article.setPicUrl("http://joymepic.joyme.com/qiniu/original/2016/11/89/6614c9310f7bf04e870950007f6551705aa2.jpg");
            article.setUrl("http://wanba.joyme.com/");
            article.setDescription("着迷玩霸 让攻略主动来找你");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("着迷专区专区游戏专区攻略".contains(content)) {
            respContent = "着迷网权威手游攻略: http://t.cn/RhABrjE";
        } else if ("y".equalsIgnoreCase(content)) {
            respContent = "着迷2016元宵节灯谜会，快来试试你的游戏咖指数，更有超实用礼品等你拿！<a href='http://m.joyme.com/news/dm2016.html'>戳此链接</a>去逛灯谜会。";
            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
            return respMessage;
        } else if ("东".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是东方不动侠！");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/wechat/dong.jpg");
            article.setUrl("http://t.cn/RANREvR");
            article.setDescription("潜在能量已被激发……");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("西".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是西方傲娇怪！");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/wechat/xi.jpg");
            article.setUrl("http://t.cn/RANR1H2");
            article.setDescription("潜在能量已被激发……");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("南".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是南方南不女！");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/wechat/nan.jpg");
            article.setUrl("http://t.cn/RANRmeZ");
            article.setDescription("潜在能量已被激发……");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("北".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是北方神龙使！");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/wechat/bei.jpg");
            article.setUrl("http://t.cn/RANR35a");
            article.setDescription("潜在能量已被激发……");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("白羊".equals(content) || "白羊座".equals(content) || "牡羊座".equals(content) || "牧羊".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是白羊座，我的《梦幻西游》手游之路从狮驼岭开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/baiyang.jpg");
            article.setUrl("http://t.cn/RwmZlD5");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("处女".equals(content) || "处女座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是处女座，我的《梦幻西游》手游之路从大唐官府开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/chunv.jpg");
            article.setUrl("http://t.cn/RwmZH58");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("金牛".equals(content) || "金牛座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是金牛座，我的《梦幻西游》手游之路从龙宫开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/jinniu.jpg");
            article.setUrl("http://t.cn/RwmZn2H");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("巨蟹".equals(content) || "巨蟹座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是巨蟹座，我的《梦幻西游》手游之路从普陀山开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/juxie.jpg");
            article.setUrl("http://t.cn/RwmZu6S");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("摩羯".equals(content) || "摩羯座".equals(content) || "山羊".equals(content) || "山羊座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是摩羯座，我的《梦幻西游》手游之路从方寸山开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/mojie.jpg");
            article.setUrl("http://t.cn/RwmZgzi");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("射手".equals(content) || "射手座".equals(content) || "人马".equals(content) || "人马座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是射手座，我的《梦幻西游》手游之路从龙宫开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/sheshou.jpg");
            article.setUrl("http://t.cn/RwmZDWq");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("狮子".equals(content) || "狮子座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是狮子座，我的《梦幻西游》手游之路从阴曹地府开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/shizi.jpg");
            article.setUrl("http://t.cn/RwmZszA");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("双鱼".equals(content) || "双鱼座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是双鱼座，我的《梦幻西游》手游之路从普陀山开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/shuangyu.jpg");
            article.setUrl("http://t.cn/RwmwP5m");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("双子".equals(content) || "双子座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是双子座，我的《梦幻西游》手游之路从方寸山开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/shuangzi.jpg");
            article.setUrl("http://t.cn/Rwmw7GD");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("水瓶".equals(content) || "水瓶座".equals(content) || "宝瓶".equals(content) || "宝瓶座".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是水瓶座，我的《梦幻西游》手游之路从狮驼岭开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/shuiping.jpg");
            article.setUrl("http://t.cn/RwmwAX7");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("天平".equals(content) || "天平座".equals(content) || "天秤".equals(content) || "天秤座".equals(content)) {
            respContent = content + "：";
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是天平座，我的《梦幻西游》手游之路从大唐官府开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/tiancheng.jpg");
            article.setUrl("http://t.cn/Rwmwyf7");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("天蝎".equals(content) || "天蝎座".equals(content)) {
            respContent = content + "：";
            respContent = content + "：";
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("我是天蝎座，我的《梦幻西游》手游之路从阴曹地府开始");
            article.setPicUrl("http://lib.joyme.com/static/theme/default/images/mhxy/tianxie.jpg");
            article.setUrl("http://t.cn/Rwmw4W6");
            article.setDescription("梦幻西游之路 着迷WIKI同行");
            articleList.add(article);
            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;
        } else if ("礼包".equals(content) || "手游礼包".equals(content)) {
            List<Article> articleList = new ArrayList<Article>();
            Article article = new Article();
            article.setTitle("手游礼包");
            article.setDescription("天天好礼包 就在着迷网，请猛戳");
            article.setPicUrl("http://lib.joyme.com/hotdeploy/static/img/weixin/giftmarket.jpg");
            article.setUrl("http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            articleList.add(article);

            resNewsMessage.setArticleCount(articleList.size());
            resNewsMessage.setArticles(articleList);
            // respMessage是返回值， 这里面的是单图文回复
            respMessage = MessageUtil
                    .newsMessageToXml(resNewsMessage);
            return respMessage;

        } else if ("wiki".equalsIgnoreCase(content)) {
            respContent = "着迷网权威手游攻略: http://t.cn/RhABrjE";
        } else if ("着迷joyme着迷网".contains(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("官网： http://www.joyme.com/").append("\n");
            buffer.append("攻略专区：http://t.cn/RhABrjE");
            buffer.append("手游礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("手游画报".equals(content)) {
            respContent = "下载手游画报 重温游戏之美 http://t.cn/RhAr2y2";
        } else if ("我叫MT我叫mtMTmt".contains(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略 http://t.cn/RhArfQy");
            buffer.append("领礼包：http://www.joyme.com/gift/wap/search?searchtext=mt&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("神之刃".equals(content)) {
            respContent = "看攻略：http://t.cn/RhiFs1v";
        } else if ("怪物x联盟".equalsIgnoreCase(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("怪物x联盟 看wiki：http://t.cn/RhAdFC7");
            buffer.append("领礼包：www.joyme.com/gift/wap/search?searchtext=%E6%80%AA%E7%89%A9x%E8%81%94%E7%9B%9F&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("黑暗光年".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAeLOh");
            buffer.append("领礼包：www.joyme.com/gift/wap/search?searchtext=%E9%BB%91%E6%9A%97%E5%85%89%E5%B9%B4&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("赤壁乱舞".equals(content)) {
            respContent = "看攻略：cblw.joyme.com/";
        } else if ("巨人进击的巨人进击".contains(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAect3");
            buffer.append("领礼包：http://www.joyme.com/gift/wap/search?searchtext=%E8%BF%9B%E5%87%BB&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("炉石传说".contains(content)) {
            respContent = "看攻略：http://t.cn/RhAeXJh";
        } else if ("helloheroHELLOHERO你好英雄".contains(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://hh.joyme.com/");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("大侠OLol".contains(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAeNwR");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("大掌门".equals(content)) {
            respContent = "看攻略：http://t.cn/RhAejD1";
        } else if ("大召唤师".equals(content) || "召个女神玩".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAeT3z");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("刀塔传奇".equals(content) || "dota".equalsIgnoreCase(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAeR3m");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("放开那三国".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhAe1nY");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("疯狂猜图".equals(content) || "疯狂猜图2".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADhZ7");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("海贼大乱斗".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：hzdld.joyme.com");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("幻想英雄".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADUoY ");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("巨龙之怒".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADG22");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("猎神OL".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADc4a");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("飞机".equals(content) || "飞机大战".equals(content) || "全民飞机大战".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://qmfjdz.joyme.com/");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("魔力宝贝".equals(content) || "宝贝".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://ml.joyme.com/");
            buffer.append("领礼包：http://www.joyme.com/gift/wap/search?searchtext=%E9%AD%94%E5%8A%9B%E5%AE%9D%E8%B4%9D&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("啪啪三国".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://ppsg.joyme.com/");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("隐寺禅棋".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("目前《隐寺禅棋》已经上架安卓与IOS双平台，苹果商店查看地址：https://itunes.apple.com/cn/app/id726093693");
            buffer.append("谷歌商店查看地址：https://play.google.com/store/apps/details?id=com.ProperGames.TileTempleTactic");
            respContent = buffer.toString();
        } else if ("暖暖环游世界".equals(content) || "暖暖".equals(content) || "环游".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("领礼包：http://www.joyme.com/gift/wap/search?searchtext=%E6%9A%96%E6%9A%96&openid=" + fromUserName + "&token=" + token);
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("神鬼幻想".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADoC7");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("神魔之塔".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/RhADC1c");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("四国战记".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2wNVI");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("天天爱西游".equals(content) || "西游".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2wlL0");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("王者之剑".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2ReKg");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("我是火影".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://wshy.joyme.com/");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("仙魔九界".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2nvse");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("扩散性百万亚瑟王国服".indexOf(content) > -1) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2nf0E");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("天天炫斗".equals(content) || "炫斗".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2nSM4");
            buffer.append("领礼包：http://www.joyme.com/gift/wap/search?searchtext=%E5%A4%A9%E5%A4%A9%E7%82%AB%E6%96%97&openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("全民打怪兽".equals(content)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("看攻略：http://t.cn/Rh2nSM4");
            buffer.append("找礼包：http://www.joyme.com/giftmarket/wap?openid=" + fromUserName + "&token=" + token);
            respContent = buffer.toString();
        } else if ("神魔大陆".equals(content)) {
            respContent = "领礼包：http://www.joyme.com/gift/wap/search?searchtext=%E7%A5%9E%E9%AD%94&openid=" + fromUserName + "&token=" + token;
        } else {
            try {
                respContent = "查找关于【" + content + "】的礼包：http://www.joyme.com/gift/wap/search?searchtext=" + URLEncoder.encode(content, "utf-8") + "&openid=" + fromUserName + "&token=" + token;
            } catch (UnsupportedEncodingException e) {
                GAlerter.lab(this.getClass().getName() + " contentError", e);
                return null;
            }
        }


        textMessage.setContent(respContent);
        respMessage = MessageUtil.textMessageToXml(textMessage);
        logger.info(this.getClass().getName() + " Return respMessage:" + respMessage);

        return respMessage;
    }

    public static void main(String[] args) {
        try {
            WeixinTextThread thread = new WeixinTextThread("oiGijjl4LZpK_e29seanhLDkR1J4", "大话西游", APPID, SECRET, redisManager);
            thread.start();
        } catch (Exception e) {
            GAlerter.lab("WeixinTextThread occur Exception.e", e);
        }

    }
}
