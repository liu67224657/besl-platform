package com.enjoyf.platform.service.ask.Test;

import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.util.MD5Util;
import net.sf.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/18
 */
public class ApiMain {
    static String ASK_PROFILEID = "02d40db86df60181779d4d9fd9852ebf"; //口袋
    static String ANSWER_PROFILEID = "0287f091ec14aa1ce7b8936ea5745b3c";//越长大越孤独

    static String ALPHA_ASK_PROFILEID = "ca1b0521bbd32229ae9277c6f2e19658"; //徐
    static String ALPHA_ANSWER_PROFILEID = "e3cbd3e17b1b94bb8287400c58e7e0d2";//徐-答

    static String s = "这是第一张图<jmwbimg:pic1.jpg>,\n这是第二张图<jmwbimg:pic2.jpg>,\n音频来吧<jmwbvoice:v.mp3:jmwbvt:4600>";
    static String HOST="api.joyme.alpha";

    private static Set<String> profileList = new HashSet<String>();

    static {
        profileList.add("001275b72d74e9e952a85bfaec2f1c59");//joyme_s9msngtu
        profileList.add("0028bd96acd495c5f680801e77da9d5a");//sumakira00_300797
        profileList.add("0086e37e8a8a64b3f7f4be409393f9de");
        profileList.add("00922a6940fe3916aea0c2bdc8bd7076");
        profileList.add("00d0a81b5025baa5663f4cd5d03edb82");
        profileList.add("00db556e5c955ebca3b813e9c5b0a45a");
        profileList.add("01439bf0aca4a81528aa3544efb56d80");
        profileList.add("0153a122db1042e3a97b39855fe304b6");
        profileList.add("01652bc6c5b0aae1b0cb9395deb82d16");
        profileList.add("01b0835ffbc40126b30bc64d4b92a7ee");
        profileList.add("01bf9ad7b8817c0c0d83c5a3f9fb2e36");
        profileList.add("0213b4bf49ee0d7fa4004f1938c3b46f");
        profileList.add("023dc1a3a3c5190959a59a2e9ae0ec4b");
        profileList.add("0287f091ec14aa1ce7b8936ea5745b3c");//越长大越孤独
        profileList.add("028a499aa92f0d1addd30697951f94c3");
        profileList.add("02c7b907047f617d121fdab17200d5b5");
        profileList.add("03735779647fddf20354d3c37829229c");
        profileList.add("03e7e71edf79c33016644577473b4d55");
        profileList.add("03ecfe5eadd6cf67dfb968dc4d222e8c");

        profileList.add("04778fbfd234fb3f1928e3e02007b010");
    }

    public static void main(String[] args) {
        System.out.println("======timelimit 20==");
//        for (int qtime = 0; qtime < 10; qtime++) {
//            long qid = postTimeQuestion(61 * 60 * 1000, ALPHA_ASK_PROFILEID);
//            System.out.println("qiuestionid is=====" + qid);
//            int i = 0;
//            for (String profile : profileList) {
//                System.out.println("follow=" + followQuestion(qid, profile));
//            }
//            long aid = answer(qid, ALPHA_ANSWER_PROFILEID, "title alpha " + qid + i);
//            if (i == 0) {
//                System.out.println("accept profile=" + ALPHA_ANSWER_PROFILEID);
//                System.out.println("accept=" + accept(qid, aid, ALPHA_ASK_PROFILEID));
//            }

//            for (String profile : profileList) {
//                long aid = answer(qid, profile, "title " + qid + i);
//                if (i == 0) {
//                    System.out.println("accept profile=" + profile);
//                    System.out.println("accept=" + accept(qid, aid, "ca1b0521bbd32229ae9277c6f2e19658"));
//                }
//                i++;
//            }

//            System.out.println(inviateQuestion(qid, "0028bd96acd495c5f680801e77da9d5a", "03ecfe5eadd6cf67dfb968dc4d222e8c"));

//        }

//        System.out.println(queryMyAnswerList(ALPHA_ANSWER_PROFILEID,ALPHA_ANSWER_PROFILEID,new Pagination(5,1,1)));

//        //被邀请人呢通知
//            System.out.println(queryQuestionNotice("00d0a81b5025baa5663f4cd5d03edb82"));
//
//        //邀请人关注
//        System.out.println(followQuestionList("0028bd96acd495c5f680801e77da9d5a"));

//            System.out.println(queryProfile(-1,"1",qid));


        //问题关注人
//        System.out.println(followQuestionProfileList(1006));


//            int i = 0;
//
//            for (String profile : profileList) {
//                System.out.println("follow=" + followQuestion(qid, profile));
//            }
//            for (String profile : profileList) {
//                long aid = answer(qid, profile, "title " + qid + i);
//                if (i == 0) {
//                    System.out.println("accept profile=" + profile);
//                    System.out.println("accept=" + accept(qid, aid, ASK_PROFILEID));
//                }
//                i++;
//            }
//        }
//        System.out.println("======one on one 20==");
//        for (int qtime = 0; qtime < 20; qtime++) {
//            long qid = postOneOnOneQuestion();
//            System.out.println("qiuestionid is=====" + qid);
//            long aid = answer(qid, ANSWER_PROFILEID, "title " + qid);
//        }
//        System.out.println("start====" + qid);
//        System.out.println(queryList("mylist", ASK_PROFILEID, ANSWER_PROFILEID, new Pagination(10, 1, 10)));
//        System.out.println("follow====" + qid);
//        System.out.println(followQuestion(qid, ANSWER_PROFILEID));
//        System.out.println(queryList("mylist", ASK_PROFILEID, ANSWER_PROFILEID, new Pagination(10, 1, 10)));
//        System.out.println("unfollow====" + qid);
//        System.out.println(unfollowQuestion(qid, ANSWER_PROFILEID));
//        System.out.println(queryList("mylist", ASK_PROFILEID, ANSWER_PROFILEID, new Pagination(10, 1, 10)));

//        System.out.println(queryList("timelist/accept", ASK_PROFILEID, ANSWER_PROFILEID, new Pagination(10, 1, 10)));
//        System.out.println(queryList("timelist/expire", ASK_PROFILEID, ANSWER_PROFILEID, new Pagination(10, 1, 10)));


//        System.out.println("answerid is=====" + qid);
//        accept(qid, aid, ASK_PROFILEID);
//        System.out.println("accept =====" + qid);
//
//        System.out.println("======one on one==");
//        qid = postOneOnOneQuestion();
//        System.out.println("qiuestionid is=====" + qid);
//        answer(qid, ANSWER_PROFILEID);
//        System.out.println("answerid is=====" + qid);
//        accept(qid, aid, ASK_PROFILEID);
//
//        System.out.println("哥哥");
//        System.out.println(searchQuestion("测试"));

//        System.out.println("======query notice sum==");
//        System.out.println(querySum());
//
//        System.out.println("======query question notice==");
//        System.out.println(queryQuestionNotice("01652bc6c5b0aae1b0cb9395deb82d16"));
//        System.out.println(queryQuestionNotice("00db556e5c955ebca3b813e9c5b0a45a"));

//        System.out.println("======follow question list==");
//        System.out.println(followQuestionList("028a499aa92f0d1addd30697951f94c3"));

//        System.out.println("======follow profile list==");
//        System.out.println(followQuestionProfileList(958));
//
//        System.out.println("======after read user notice sum==");
//        System.out.println(querySum());
////
//        System.out.println("======query system notice==");
//        System.out.println(querySysNotice());
//
//        System.out.println("======after read sys notice sum==");
//        System.out.println(querySum());
//
//        System.out.println("======delte user notice==");
//        System.out.println(delUserNotice());

        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://localhost:7501/refresh",null,null);
        System.out.println(httpResult);
    }


    private static long answer(long questionId, String answerProfile, String answerTitle) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/answer/post", new HttpParameter[]{
                new HttpParameter("title", answerTitle),
                new HttpParameter("text", answerTitle+"\n"+s),
                new HttpParameter("voice", "va.mp3"),
                new HttpParameter("vtime", "444"),
                new HttpParameter("pid", answerProfile),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
                new HttpParameter("qid", questionId)
        }, null);



        try {
            JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
            return jsonObject.getJSONObject("result").getLong("aid");
        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
        }
    }


    private static String accept(long questionId, long answerid, String questionPid) {
        HttpClientManager httpClient = new HttpClientManager();
        return httpClient.post("http://"+HOST+"/wanba/api/ask/answer/accept", new HttpParameter[]{
                new HttpParameter("pid", questionPid),
                new HttpParameter("qid", questionId),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
                new HttpParameter("aid", answerid)
        }, null).toString();
    }

    private static long postTimeQuestion(long timelimit, String askpid) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/post/timelimit", new HttpParameter[]{
                new HttpParameter("title", "刘浩抢答 系统通知"),
                new HttpParameter("text", s),
                new HttpParameter("voice", "va.mp3"),
                new HttpParameter("vtime", "444"),
                new HttpParameter("pid", askpid),
                new HttpParameter("tagid", 1),
                new HttpParameter("timelimit", timelimit),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
        }, null);

        JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
        return jsonObject.getJSONObject("result").getLong("qid");
    }

    private static long postOneOnOneQuestion() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/post/invite", new HttpParameter[]{
                new HttpParameter("title", "刘浩邀请系统通知"),
                new HttpParameter("text", s),
                new HttpParameter("voice", "va.mp3"),
                new HttpParameter("vtime", "444"),
                new HttpParameter("point", "100"),
                new HttpParameter("pid", ASK_PROFILEID),
                new HttpParameter("invite", ANSWER_PROFILEID),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI")

        }, null);

        JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());

        return jsonObject.getJSONObject("result").getLong("qid");
    }


    private static String searchQuestion(String text) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/search/question", new HttpParameter[]{
                new HttpParameter("text", text),
                new HttpParameter("pnum", 1),
                new HttpParameter("pcount", 30)
        }, null);
        return httpResult.getResult();
    }

    private static String querySum() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/notice/query/sum", new HttpParameter[]{
                new HttpParameter("pid", ASK_PROFILEID),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
        }, null);
        return httpResult.getResult();
    }

    private static String queryQuestionNotice(String pid) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/notice/question/list", new HttpParameter[]{
                new HttpParameter("pid", pid),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
        }, null);
        return httpResult.getResult();
    }

    private static String querySysNotice() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/notice/system/list", new HttpParameter[]{
                new HttpParameter("pid", ASK_PROFILEID),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
        }, null);
        return httpResult.getResult();
    }


    private static String delUserNotice() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/notice/user/delete", new HttpParameter[]{
                new HttpParameter("pid", ASK_PROFILEID),
                new HttpParameter("nid", 10041l)
        }, null);
        return httpResult.getResult();
    }

    private static String queryList(String queryList, String querypid, String pid, Pagination pagination) {

        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/" + queryList, new HttpParameter[]{
                new HttpParameter("pid", pid),
                new HttpParameter("querypid", querypid),
                new HttpParameter("queryflag", -1l)
        }, null);
        return httpResult.getResult();
    }

    private static String queryMyAnswerList( String querypid, String pid, Pagination pagination) {

        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/myanswerlist" , new HttpParameter[]{
                new HttpParameter("pid", pid),
                new HttpParameter("querypid", querypid),
                new HttpParameter("queryflag", -1l)
        }, null);
        return httpResult.getResult();
    }

    private static String followQuestion(long questionId, String profileId) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/follow", new HttpParameter[]{
                new HttpParameter("pid", profileId),
                new HttpParameter("qid", questionId)
        }, null);
        return httpResult.getResult();
    }

    private static String unfollowQuestion(long questionId, String profileId) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/unfollow", new HttpParameter[]{
                new HttpParameter("pid", profileId),
                new HttpParameter("qid", questionId)
        }, null);
        return httpResult.getResult();
    }

    private static String followQuestionList(String profileId) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/follow/questionlist", new HttpParameter[]{
                new HttpParameter("pid", profileId),
                new HttpParameter("queryflag", -1),
                new HttpParameter("pcount", 10)
        }, null);
        return httpResult.getResult();
    }

    private static String followQuestionProfileList(long questionId) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/follow/profilelist", new HttpParameter[]{
                new HttpParameter("qid", questionId),
                new HttpParameter("pid", "pid"),
                new HttpParameter("queryflag", -1),
                new HttpParameter("pcount", 10)
        }, null);
        return httpResult.getResult();
    }


    private static String inviateQuestion(long questionId, String profileId, String invitedpid) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/ask/question/invite/profile", new HttpParameter[]{
                new HttpParameter("pid", profileId),
                new HttpParameter("invitedpid", invitedpid),
                new HttpParameter("qid", questionId)
        }, null);
        return httpResult.getResult();
    }


    private static String queryProfile(long tagid, String checkis, long iqid) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://"+HOST+"/wanba/api/profile/listbytag", new HttpParameter[]{
                new HttpParameter("tagid", tagid),
                new HttpParameter("checkis", checkis),
                new HttpParameter("iqid", iqid)

        }, null);
        return httpResult.getResult();
    }


}
