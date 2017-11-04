package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.enjoyf.webapps.joyme.webpage.controller.weixin.WeixinUtil.EXPIRE;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-22
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class WeixinImageDataProcessor implements WeixinDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WeixinImageDataProcessor.class);
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

//        //tools记录
//        try {
//            WeixinTextThread thread = new WeixinTextThread(fromUserName, content, APPID, SECRET, redisManager);
//            thread.start();
//        } catch (Exception e) {
//            GAlerter.lab("WeixinTextThread occur Exception.e", e);
//        }

        // // 回复文本消息
        RespTextMessage textMessage = new RespTextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);


        String token = WeixinUtil.saveToken(fromUserName);
        StringBuffer buffer = new StringBuffer();
        buffer.append("恭喜亲，你的游戏咖指数小编自叹不如，敬请期待中奖名单！");
        respContent = buffer.toString();
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
