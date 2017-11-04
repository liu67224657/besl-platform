package com.enjoyf.platform.util.weixin;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.weixin.WeixinMenu;
import com.enjoyf.platform.service.weixin.resp.Article;
import com.enjoyf.platform.service.weixin.resp.RespNewsMessage;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.rmi.ConnectException;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: pengxu Date: 14-5-13 Time: 上午11:52 To change
 * this template use File | Settings | File Templates.
 */
public class WeixinUtil {
    private static final Logger logger = LoggerFactory
            .getLogger(WeixinUtil.class);


    // 菜单创建URL （post）
    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 发起HTTPS请求并获得结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（get/post）
     * @param outputStr     提交的数据
     * @return
     */
    public static JSONObject httpRequest(String requestUrl,
                                         String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            // 设置请求方式
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

            return jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            logger.error("Weixin server connection timed out.");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return null;
    }

    /**
     * 创建自定义菜单
     *
     * @param menu        菜单列表
     * @param accessToken accessToken凭证 从getAccessToken()方法获取
     * @return
     */
    public static int createMenu(WeixinMenu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonMenu) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                logger.error("创建菜单失败 errcode:{} errmsg:{}",
                        jsonObject.getInt("errcode"),
                        jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    /**
     * 处理微信发来的请求
     *
     * @param requestMap
     * @return
     */
    public static String processRequest(Map<String, String> requestMap) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            String respContent = "";

            // xml请求解析


            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // // 回复文本消息
            RespTextMessage textMessage = new RespTextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            //
            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                respContent = "亲，我还在成长中 对于您的话我会稍后回复 " + "\n" + " 如果您想领礼包 请点击下方菜单。";
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // // 图片消息
            // else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            // respContent = "您发送的是图片消息！";
            // textMessage.setContent(respContent);
            // respMessage = MessageUtil.textMessageToXml(textMessage);
            // }
            // // 地理位置消息
            // else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
            // respContent = "您发送的是地理位置消息！";
            // textMessage.setContent(respContent);
            // respMessage = MessageUtil.textMessageToXml(textMessage);
            // }
            // // 链接消息
            // else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
            // respContent = "您发送的是链接消息！";
            // textMessage.setContent(respContent);
            // respMessage = MessageUtil.textMessageToXml(textMessage);
            // }
            // // 音频消息
            // else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
            // respContent = "您发送的是音频消息！";
            // textMessage.setContent(respContent);
            // respMessage = MessageUtil.textMessageToXml(textMessage);
            //
            // }
            // 事件推送
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "欢迎您使用着迷礼包神器                                       超多新鲜礼包  随便挑/可爱热门独家礼包  嗨翻天/鼓掌                                      1秒领号、还能预定哟！   领号请点击下方菜单                                           建议反馈请直接编辑       建议+您想说的话 发送  我们十分重视您的想法";
                    textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                }
                // 取消订阅
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    List<Article> articleList = null;
                    RespNewsMessage resNewsMessage = new RespNewsMessage();
                    resNewsMessage.setFromUserName(toUserName);
                    resNewsMessage.setToUserName(fromUserName);
                    resNewsMessage.setCreateTime(new Date().getTime());
                    resNewsMessage.setFuncFlag(0);
                    resNewsMessage
                            .setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                    String key = requestMap.get("EventKey");
                    logger.info("Return key" + key);
                    if ("feedback".equals(key)) { //意见反馈
                        respContent = "亲爱的用户，感谢您对着迷礼包的关注和支持。您有任何意见或建议可以直接回复发送给我们，我们将尽力为您解答。";
                        textMessage.setContent(respContent);
                        respMessage = MessageUtil.textMessageToXml(textMessage);
//                        articleList = new ArrayList<Article>();
//                        Article article = new Article();
//                        article.setTitle("着迷最新攻略测试");
//                        article.setPicUrl("http://r001.joyme.alpha/r001/original/2014/03/94/1d0a7e910d4d2041c30a4ee017890830472b.jpg");
//                        article.setUrl("http://www.joyme.alpha/giftmarket/wap?openid="
//                                + fromUserName);
//                        article.setDescription("这是图文消息测试1");
//                        articleList.add(article);
//
//                        resNewsMessage.setArticleCount(articleList.size());
//                        resNewsMessage.setArticles(articleList);
//                        // respMessage是返回值， 这里面的是单图文回复
//                        respMessage = MessageUtil
//                                .newsMessageToXml(resNewsMessage);
                    } else if ("exclusive".equals(key)) {  //独家礼包
                        articleList = new ArrayList<Article>();

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
//                        GiftMarketWebLogic giftMarketWebLogic = new GiftMarketWebLogic();
//                        PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
//
//                        if (pageRows != null) {
//                            for (ActivityDTO activityDTO : pageRows.getRows()) {
//                                Article article2 = new Article();
//                                article2.setTitle(activityDTO.getTitle());
//                                article2.setPicUrl(activityDTO.getGipic());
//                                article2.setUrl("http://www.joyme.com/giftmarket/wap/giftdetail?aid=" + activityDTO.getGid() + "&openid=" + fromUserName);
//                                articleList.add(article2);
//                            }
//                        }
//                        Article article2 = new Article();
//                        article2.setTitle("着迷多图文测试2");
//                        article2.setPicUrl("http://article.joyme.com//article/uploads/allimg/140107/21-14010G63F20-L.jpg");
//                        article2.setUrl("http://www.joyme.com");
//                        article2.setDescription("这是多图文消息测试2");
//                        articleList.add(article2);
//
//                        Article article3 = new Article();
//                        article3.setTitle("着迷最新攻略测试3");
//                        article3.setPicUrl("http://article.joyme.com//article/uploads/131231/17-13123110333LP.jpg");
//                        article3.setUrl("http://www.joyme.com");
//                        article3.setDescription("这是多图文消息测试3");
//                        articleList.add(article3);

                        resNewsMessage.setArticleCount(articleList.size() + 1);
                        resNewsMessage.setArticles(articleList);
                        // respMessage是返回值， 这里面的是单图文回复
                        respMessage = MessageUtil
                                .newsMessageToXml(resNewsMessage);
                    }

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_VIEW)) {
                    String key = requestMap.get("EventKey");
                    respMessage = fromUserName;
                }
            }
            logger.info("Return respMessage:" + respMessage);

//			logger.info("Return requestMap:" + requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respMessage;
    }


    //获取微信加密信息
    public static String getSignature(String jsapi_ticket, String timestamp,
                                      String nonce, String jsurl) throws IOException {
        /****
         * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
         * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
         * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
         * URL 转义。即 signature=sha1(string1)。
         * **如果没有按照生成的key1=value&key2=value拼接的话会报错
         */
        String[] paramArr = new String[]{"jsapi_ticket=" + jsapi_ticket,
                "timestamp=" + timestamp, "noncestr=" + nonce, "url=" + jsurl};
        Arrays.sort(paramArr);
        // 将排序后的结果拼接成一个字符串
        String content = paramArr[0].concat("&" + paramArr[1]).concat("&" + paramArr[2]).concat("&" + paramArr[3]);
        // System.out.println("拼接之后的content为:" + content);

        String sha1 = SHA1Util.SHA1(content);
        //  System.out.println("SHA1的SHA1为:" + sha1);
        return sha1;
    }

    // url（当前网页的URL，不包含#及其后面部分）
    public static String getFullUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        return url.toString();
    }


}
