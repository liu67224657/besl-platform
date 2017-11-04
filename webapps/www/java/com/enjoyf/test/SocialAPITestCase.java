//package com.enjoyf.test;
//
//import com.enjoyf.platform.util.http.HttpClientManager;
//import com.enjoyf.platform.util.http.HttpParameter;
//import com.enjoyf.platform.util.http.HttpResult;
//import com.enjoyf.platform.util.http.URLUtils;
//import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
//import com.enjoyf.platform.util.security.DESUtil;
//import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
//import com.enjoyf.util.MD5Util;
//import org.junit.Test;
//
///**
// * Created by IntelliJ IDEA.
// * User: zhitaoshi
// * Date: 14-4-26
// * Time: 下午2:52
// * To change this template use File | Settings | File Templates.
// */
//public class SocialAPITestCase extends BaseTestCase {
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
//    }
//
//    @Override
//    protected void tearDown() throws Exception {
//        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
//    }
//
//    @Test
//    public void testPostMessage() throws Exception {
//
//        HttpClientManager clientManager = new HttpClientManager();
//        HttpResult result = clientManager.post("http://www.joyme.dev/joymeapp/register/postmessage", new HttpParameter[]{
//                new HttpParameter("phone", "18801069719")
//        }, null);
//
//    }
//
//
//    public void testP() throws Exception {
//        String url = "http://www.joyme.com/socialapp/content/post";
//        String uno = "0234b84f-db42-4699-9ecc-992a4a5a1b2e";
//        String time = System.currentTimeMillis() + "";
//        String ak = "0VsYSLLsN8CrbBSMUOlLNx";
//        String encryptParam = null;
//        try {
//            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        HttpClientManager clientManager = new HttpClientManager();
//        HttpResult result = clientManager.post(url, new HttpParameter[]{
//                new HttpParameter("uno", uno),
//                new HttpParameter("platform", "0"),
//                new HttpParameter("title", "咔哒一下"),
//                new HttpParameter("body", "咔哒咔哒咔哒，根本停不下来~"),
//                new HttpParameter("pic", "18801069719.jpg"),
//                new HttpParameter("pics", "18801069719.jpg"),
//                new HttpParameter("amr", "18801069719.amr"),
//                new HttpParameter("mp3", "18801069719.mp3"),
//                new HttpParameter("audiotime", "3600"),
//                new HttpParameter("lon", "0"),
//                new HttpParameter("lat", "0"),
//                new HttpParameter("activityid", "10002"),
//                new HttpParameter("time", time),
//                new HttpParameter("appkey", ak),
//                new HttpParameter("secret", encryptParam)
//        }, null);
//
//        System.out.println(result);
//
//    }
//
//    //
////    public static void main(String[] args) {
////        String url = "http://www.joyme.alpha/socialapp/content/post";
//////        String url = "http://www.joyme.dev/socialapp/content/post";
//////        String uno = "bbca4a5f-1224-458b-8094-b67959ce7190";
//////        String uno = "d5617f03-64fa-4ae8-903b-58684596a8e2";
////        String uno = "ff4c768c-8d9d-4db8-8821-d409178c294f";
//////        String uno = "8b99d982-ba87-4afa-b919-870090166c8c";
////        int platform = 0;
////        String title = "全民coser";
////        String body = "全民coser";
////        String pic = "http://r001.joyme.test/r001/original/2014/05/76/8b821b530c5b804df20a7a6004054d1dac58.gif";
////        String pics = "http://r001.joyme.test/r001/original/2014/05/76/8b821b530c5b804df20a7a6004054d1dac58.gif";
////        String bgaudioid = "10008";
////        String watermarkid = "10004";
////        String activityid = "10000";
////        String time = System.currentTimeMillis() + "";
////        String ak = "0VsYSLLsN8CrbBSMUOlLNx";
////        String encryptParam = null;
////        try {
////            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        String content = null;
////        SocialCount socialCount = new SocialCount();
////        socialCount.setActivityid(10001l);
////        socialCount.setWatermarkid(10001l);
////        socialCount.setBgaudioid(10008l);
////        socialCount.setCid(1468l);
////        Set<SocialCount> set = new HashSet<SocialCount>();
////        set.add(socialCount);
////        SocialCountJsonSet jsonSet = new SocialCountJsonSet();
////        jsonSet.setSocialCountSet(set);
////
////        try {
////            content = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
//////                            new PostParameter("pnum", "1"),
//////                            new PostParameter("desuno", ""),
//////                            new PostParameter("startid", "0"),
//////                            new PostParameter("isnext", "true"),
//////                            new PostParameter("jsonarray", "[{\"cid\":11153,\"platform\":0,\"channel\":\"sinaweibo\"},{\"cid\":11152,\"platform\":1,\"channel\":\"qq\"}]"),
////                            new PostParameter("uno", uno),
//////                            new PostParameter("q", "wo"),
//////                            new PostParameter("desuno", "fe7d1178-4bc4-4b50-816a-6e808da9a459"),
//////                            new PostParameter("action", "0"),
////                            new PostParameter("cid", "10039"),
////                            new PostParameter("cuno", "0f1786bd-2ce5-49b8-84ff-6d88b907b0a3"),
////                            new PostParameter("pid", "0"),
////                            new PostParameter("puno", ""),
////                            new PostParameter("rid", "4172"),
////                            new PostParameter("runo", ""),
////                            new PostParameter("ip", "127.0.0.1"),
//////                            new PostParameter("action", "1"),
//////                            new PostParameter("name", "小TT丶"),
////                            new PostParameter("platform", platform),
//////                            new PostParameter("adtype", 0),
////                            new PostParameter("title", title),
////                            new PostParameter("body", "(T_T)"),
////                            new PostParameter("pic", pic),
////                            new PostParameter("pics", pics),
//////                            new PostParameter("bgaudioid", bgaudioid),
//////                            new PostParameter("watermarkid", watermarkid),
////                            new PostParameter("activityid", activityid),
////                            new PostParameter("lon", "0"),
////                            new PostParameter("lat", "0"),
////                            new PostParameter("time", time),
////                            new PostParameter("appkey", ak),
////                            new PostParameter("secret", encryptParam)
////                    }, "47a433ab-3258-4265-b86e-8eb49af94230");
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////        System.out.println(content);
////        System.out.println(TableUtil.getTableNumSuffix("0f1786bd-2ce5-49b8-84ff-6d88b907b0a3".hashCode(), 10));
////    }
//
//    public static void main(String[] args){
////        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
////        map.put(ProfileSumField.SOCIALAGREEMSGSUM, 4);
////        try {
////            ProfileServiceSngl.get().updateProfileSum("bff07ef6-ef74-4966-95c6-6f3d356a9ac2", map);
////        } catch (ServiceException e) {
////            e.printStackTrace();
////        }
//
////        System.out.println(Md5Utils.md5("17yfn24TFexGybOF0PqjdY|0|2.0.0|appstore"));
////        System.out.println(MD5Util.Md5("17yfn24TFexGybOF0PqjdY|0|2.0.1|appstore"));
////        System.out.println(MD5Util.Md5("17yfn24TFexGybOF0PqjdY|0|2.0.2|appstore"));
////        System.out.println(MD5Util.Md5("0G30ZtEkZ4vFBhAfN7Bx4v|0|1.1.3|appstore"));
//        System.out.println(URLUtils.getQiniuUrl(ImageURLTag.parseUserCenterHeadIcon("", "", "m", true)));
////        String url = "http://www.joyme.com/socialapp/message/messagelist";
////        String uno = "157ab749-ee23-4871-92da-c18005ae0cbe";
////        String time = System.currentTimeMillis() + "";
////        String appkey = "0VsYSLLsN8CrbBSMUOlLNx";
////        String encryptParam = null;
////        try {
////            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("uno", uno),
////                            new PostParameter("msgtype", "3"),
////                            new PostParameter("pnum", "1"),
////                            new PostParameter("time", time),
////                            new PostParameter("appkey", appkey),
////                            new PostParameter("secret", encryptParam)}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(jsonResult);
////            System.out.println(TableUtil.getTableNumSuffix(uno.hashCode(), 10));
////            System.out.println(TableUtil.getTableNumSuffix(uno.hashCode(), 100));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//    }
//
//
////    public static void main(String[] args) {
////        String url = "http://www.joyme.test/socialapp/profile/push";
////        String uno = "0234b84f-db42-4699-9ecc-992a4a5a1b2e";
////        long time = System.currentTimeMillis();
////        String appkey = "0VsYSLLsN8CrbBSMUOlLNx";
////        String encryptParam = null;
////        try {
////            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("uno", uno),
////                            new PostParameter("agree", "a"),
////                            new PostParameter("reply", "a"),
////                            new PostParameter("focus", "a"),
////                            new PostParameter("sound", "a"),
////                            new PostParameter("time", System.currentTimeMillis()),
////                            new PostParameter("appkey", appkey),
////                            new PostParameter("secret", encryptParam)}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(jsonResult);
////            System.out.println(TableUtil.getTableNumSuffix("fe7d1178-4bc4-4b50-816a-6e808da9a459".hashCode(), 10));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public static void main(String[] args) {
////        String url = "http://www.joyme.test/socialapp/profile/getpush";
////        String uno = "0234b84f-db42-4699-9ecc-992a4a5a1b2e";
////        long time = System.currentTimeMillis();
////        String appkey = "0VsYSLLsN8CrbBSMUOlLNx";
////        String encryptParam = null;
////        try {
////            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("uno", uno),
//////                            new PostParameter("agree", "n"),
//////                            new PostParameter("reply", "a"),
//////                            new PostParameter("focus", "n"),
//////                            new PostParameter("sound", "n"),
////                            new PostParameter("time", System.currentTimeMillis()),
////                            new PostParameter("appkey", appkey),
////                            new PostParameter("secret", encryptParam)}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(jsonResult);
////            System.out.println(TableUtil.getTableNumSuffix("fe7d1178-4bc4-4b50-816a-6e808da9a459".hashCode(), 10));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public static void main(String[] args){
////            String url = "http://www.joyme.test/socialapp/content/hotlist";
////            String uno = "27c470b0-8835-4899-ac5a-9c507990d129";
////            String time = System.currentTimeMillis() + "";
////            String appkey = "0VsYSLLsN8CrbBSMUOlLNx";
////            String encryptParam = null;
////            try {
////                encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            try {
////                String jsonResult = new HttpUpload().postMultipart(url,
////                        new PostParameter[]{
////                                new PostParameter("startid", "0"),
////                                new PostParameter("isnext", "true"),
////                                new PostParameter("uno", uno),
////                                new PostParameter("platform", "0"),
//////                                new PostParameter("msgid", "10033"),
//////                                new PostParameter("msgtype", "2"),
//////                                new PostParameter("pnum", "1"),
////                                new PostParameter("time", time),
////                                new PostParameter("appkey", appkey),
////                                new PostParameter("secret", encryptParam)}, "47a433ab-3258-4265-b86e-8eb49af94230");
////                System.out.println(jsonResult);
////                System.out.println(TableUtil.getTableNumSuffix(uno.hashCode(), 10));
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            }
////        }
//
////    public static void main(String[] args) {
////        String url = "http://www.joyme.test/socialapp/activity/index";
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("appkey", "0VsYSLLsN8CrbBSMUOlLNx"),
////                            new PostParameter("platform", "0"),
////                            new PostParameter("startid", "15"),
////                            new PostParameter("isnext", "true")}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(jsonResult);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public static void main(String[] args) {
////        String url = "http://www.joyme.dev/socialapp/activity/list";
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("activityid", "21"),
////                            new PostParameter("platform", "0"),
////                            new PostParameter("uno", "fb434664-5393-4e4f-92fe-b6d560ba30bb"),
////                            new PostParameter("startid", "0"),
////                            new PostParameter("isnext", "true")}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(jsonResult);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public static void main(String[] args) {
////
////        String url = "http://www.joyme.com/joymeapp/message/getlastmsg";
////        String jsonResult = null;
////        try {
////            jsonResult = new HttpUpload().post(url, new PostParameter[]{new PostParameter("appkey", "17yfn24TFexGybOF0PqjdY"),
////                    new PostParameter("client_id", "3f69fff707e3c03c600968d0918c187107cc"),
////                    new PostParameter("client_token", "9ba09fa507ad103886094a80217772aa4e15"),
////                    new PostParameter("msgid", 0)}, null);
////        } catch (Exception e) {
////            GAlerter.lab("HttpUpload post " + e);
////        }
////        System.out.println(jsonResult);
////
////    }
//
//
////    public static void main(String[] args) throws Exception {
////        String delUrl = "http://www.joyme.test/joymeapp/auth/sinaweibo/getprofile";
////        String vid = new HttpUpload().post(delUrl,
////                new PostParameter[]{
////                        new PostParameter("access_token", "2.00E_26TCeeIcUEf33762723eRp3wUB"),
////                        new PostParameter("expire", 7831705),
////                        new PostParameter("user_name", "xiaoxiaoxiao"),
////                        // new PostParameter("icon", "http://tp3.sinaimg.cn/2266819514/50/5699238167/1"),
////                        new PostParameter("icon", "http://tp2.sinaimg.cn/5203512469/180/5699349081/1"),
////                        new PostParameter("forign_id", "11111111111111111"),
////                        new PostParameter("appkey", "0VsYSLLsN8CrbBSMUOlLNx"),
////                        new PostParameter("clientid", "123456789"),
////                        new PostParameter("clienttoken", "123456789"),
////                        new PostParameter("sex", "0")
////                }, "aaaaaaaaaaaaaaa");
////        System.out.println(vid);
////    }
//
////    public static void main(String[] args) throws Exception {
////        String delUrl = "http://www.joyme.test/joymeapp/login/device";
////        String vid = new HttpUpload().post(delUrl,
////                new PostParameter[]{
////                        new PostParameter("uno", "111111"),
////                        new PostParameter("clientid", "123456789999991122"),
////                        new PostParameter("clienttoken", "123456789999991122"),
////                        new PostParameter("platform", "0"),
////                        new PostParameter("appkey", "0VsYSLLsN8CrbBSMUOlLNxI"),
////                        new PostParameter("pushchannel", "0"),
////                        new PostParameter("version", "1.3.0"),
////                        new PostParameter("channel", ""),
////                        new PostParameter("tags", ""),
////                        new PostParameter("adv_id", "")
////                }, "aaaaaaaaaaaaaaa");
////        System.out.println(vid);
//
////        System.out.println(TableUtil.getTableNumSuffix("80b42791-cb67-4182-9aeb-24a6955d871b".hashCode(), 10));
////    }
//
//
////    public static void main(String[] args) throws Exception {
////        String url = "http://www.joyme.beta/socialapp/profile/setting";
////        String uno = "8b99d982-ba87-4afa-b919-870090166c8c";
////        String time = System.currentTimeMillis() + "";
////        String appkey = "0VsYSLLsN8CrbBSMUOlLNx";
////        String encryptParam = null;
////        try {
////            encryptParam = DESUtil.desEncrypt("cc0003850f100b0007585ec81e00c006", uno + time);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        try {
////            String jsonResult = new HttpUpload().postMultipart(url,
////                    new PostParameter[]{
////                            new PostParameter("uno", uno),
////                            new PostParameter("headicon", "http://r001.joyme.beta/r001/headicon/2014/07/49/a22b7a59023cd044ea08b6a060c4803bc396.jpg"),
////                            new PostParameter("time", time),
////                            new PostParameter("appkey", appkey),
////                            new PostParameter("secret", encryptParam)}, "47a433ab-3258-4265-b86e-8eb49af94230");
////            System.out.println(time);
////            System.out.println(appkey);
////            System.out.println(encryptParam);
////            System.out.println(jsonResult);
////            System.out.println(TableUtil.getTableNumSuffix(uno.hashCode(), 10));
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public static void main(String[] args) {
////        JedisPoolConfig config = new JedisPoolConfig();
////        config.setMaxActive(20);
////        config.setMaxIdle(5);
////        config.setMaxWait(1000l);
////        config.setTestOnBorrow(false);
////
////        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
////
////        Jedis jedis = jedisPool.getResource();
////
////        jedis.set("111", "111");
////        jedis.set("222", "222");
////        System.out.println(jedis.get("111"));
////        System.out.println(jedis.keys("*"));
////
////        jedis.del("111");
////        System.out.println(jedis.keys("*"));
////
////        jedis.flushDB();
////        System.out.println(jedis.keys("*"));
////
////    }
//
////    public static void main(String[] args) {
////        try {
////
////
////            Element root = new Element("list");
////
////            // 根节点添加到文档中；
////
////            Document Doc = new Document(root);
////
////            // 此处 for 循环可替换成 遍历 数据库表的结果集操作;
////            Pagination page = new Pagination(200, 1, 200);
////            int i = 1;
////            do {
////                PageRows<SocialProfile> pageRows = ProfileServiceSngl.get().queryNewestSocialProfile(new QueryExpress(), page);
////                for (SocialProfile profile:pageRows.getRows()) {
////
////                    // 创建节点 user;
////
////                    Element elements = new Element("user");
////
////                    // 给 user 节点添加属性 id;
////
////                    elements.setAttribute("user" + i, profile.getUno());
////                    i = i+1;
////
////                    // 给 user 节点添加子节点并赋值；
////
////                    // 赋值；
////
////                    elements.addContent(new Element("name").setText(profile.getBlog().getScreenName()));
////
////                    // 给父节点list添加user子节点;
////
////                    root.addContent(elements);
////
////                }
////            }while (!page.isLastPage());
////
////            XMLOutputter XMLOut = new XMLOutputter();
////
////            // 输出 XMLOutput.xml 文件到项目根目录；
////
////            XMLOut.output(Doc, new FileOutputStream("C:\\Users\\zhitaoshi\\Desktop\\aaaa.xml"));
////
////            System.out.println();
////        } catch (Exception e) {
////
////        }
////    }
//
////    public static void main(String[] args) {
////        String url = "http://web001.dev:38080/search/query.do";
////        String query = "(searchtext:" + "影子" + ")(appkey:" + "0VsYSLLsN8CrbBSMUOlLNx" + ")";
////        String responseMessage = "";
////        StringBuffer resposne = new StringBuffer();
////        HttpURLConnection httpConnection = null;
////        DataOutputStream out = null;
////        BufferedReader reader = null;
////        try {
////            URL urlPost = new URL(url);
////            httpConnection = (HttpURLConnection) urlPost.openConnection();
////            httpConnection.setDoOutput(true);
////            httpConnection.setDoInput(true);
////            // 参数长度太大，不能用get方式
////            httpConnection.setRequestMethod("POST");
////            // 不使用缓存
////            httpConnection.setUseCaches(false);
////            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
////            httpConnection.setInstanceFollowRedirects(true);
////            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
////            // 意思是正文是urlencoded编码过的form参数
////            httpConnection.setRequestProperty("Content-Type",
////                    "application/x-www-form-urlencoded");
////            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
////            // 要注意的是connection.getOutputStream会隐含的进行connect。
////            // 实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
////            httpConnection.connect();
////            out = new DataOutputStream(httpConnection.getOutputStream());
////            // The URL-encoded contend
////            // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
////
////            JSONObject jsonObject = new JSONObject();
////            jsonObject.put("c", SolrCore.USERS.getCore());
////            jsonObject.put("q", query);
////            jsonObject.put("p", 1);
////            jsonObject.put("ps", 20);
////            //写入参数,DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
////            out.writeBytes(jsonObject.toJSONString());
////            // flush and close
////            out.flush();
////            reader = new BufferedReader(new InputStreamReader(
////                    httpConnection.getInputStream()));
////            while ((responseMessage = reader.readLine()) != null) {
////                resposne.append(responseMessage);
////            }
////
////            if (!"failure".equals(resposne.toString())) {
////            } else {
////            }
////            // 将该url的配置信息缓存起来
////            System.out.println(resposne.toString());
////        } catch (IOException e) {
////            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
////        } finally {
////            try {
////                if (null != out) {
////                    out.close();
////                }
////                if (null != reader) {
////                    reader.close();
////                }
////                if (null != httpConnection) {
////                    httpConnection.disconnect();
////                }
////            } catch (Exception e2) {
////                e2.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
////            }
////        }
////    }
//
//}
