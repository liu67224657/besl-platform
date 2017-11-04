package com.enjoyf.platform.service.ask.Test;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class TestMain {

    private static Set<String> profileList = new HashSet<String>();

    static {
        profileList.add("02d40db86df60181779d4d9fd9852ebf");//口袋
        profileList.add("0287f091ec14aa1ce7b8936ea5745b3c");//越长大越孤独
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
        profileList.add("0287f091ec14aa1ce7b8936ea5745b3c");
        profileList.add("028a499aa92f0d1addd30697951f94c3");
        profileList.add("02c7b907047f617d121fdab17200d5b5");
        profileList.add("02d40db86df60181779d4d9fd9852ebf");
        profileList.add("03735779647fddf20354d3c37829229c");
        profileList.add("03e7e71edf79c33016644577473b4d55");
        profileList.add("03ecfe5eadd6cf67dfb968dc4d222e8c");

        profileList.add("04778fbfd234fb3f1928e3e02007b010");
    }

    public static void main(String[] args) throws ServiceException {
//        System.out.println(profileList.size());

//        Map<String, Profile> map = UserCenterServiceSngl.get().queryProfiles(profileList);
//
//        int i = 0;
//        for (Profile profile : map.values()) {
//            WanbaProfile wanbaProfile = new WanbaProfile();
//            wanbaProfile.setProfileId(profile.getProfileId());
//            wanbaProfile.setVerifyType(Long.valueOf(VerifyType.FAMOUS.getCode()));
//            wanbaProfile.setDescription("达人的简介" + i);
//            wanbaProfile.setNick(profile.getNick());
//            System.out.println(AskServiceSngl.get().verifyWanbaProfile(wanbaProfile, 58));
//            i++;
//        }

        //    postTimeQuestion(0, "e0419fbf21c278974beedbd5b0afbb98");
        //   postOneOnOneQuestion();
        System.out.println(new Date().getTime());
//        for (int i = 1; i < 2; i++) {
//            long qid = postTimeQuestion(i, "e0419fbf21c278974beedbd5b0afbb98");
//            long aid = answer(qid, "028a499aa92f0d1addd30697951f94c3", "answerTitle" + i);
//            accept(qid, aid, "e0419fbf21c278974beedbd5b0afbb98");
//        }
        //       System.out.println("==================end===========");


    }


    private static long answer(long questionId, String answerProfile, String answerTitle) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://api.joyme.test/wanba/api/ask/answer/post", new HttpParameter[]{
                new HttpParameter("title", answerTitle),
                new HttpParameter("text", answerTitle),
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
        return httpClient.post("http://api.joyme.test/wanba/api/ask/answer/accept", new HttpParameter[]{
                new HttpParameter("pid", questionPid),
                new HttpParameter("qid", questionId),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
                new HttpParameter("aid", answerid)
        }, null).toString();
    }

    private static long postTimeQuestion(int i, String askpid) {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult httpResult = httpClient.post("http://api.joyme.test/wanba/api/ask/question/post/timelimit", new HttpParameter[]{
                new HttpParameter("title", "搜索的问题" + i),
                new HttpParameter("text", "text"),
                new HttpParameter("voice", "va.mp3"),
                new HttpParameter("vtime", "444"),
                new HttpParameter("pid", askpid),
                //new HttpParameter("tagid", 57),
                new HttpParameter("tagid", 73),
                new HttpParameter("timelimit", 5 * 60 * 1000),
                new HttpParameter("appkey", "17yfn24TFexGybOF0PqjdYI"),
        }, null);

        JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
        System.out.println(jsonObject);
        return jsonObject.getJSONObject("result").getLong("qid");
    }


    private void addItemByLineKey() {
        String ownProfileId = "-10000";
        String linekey = AskUtil.getAskLineKey(ownProfileId, WanbaItemDomain.RECOMMEND);

        WanbaItem item = new WanbaItem();
        item.setOwnProfileId(ownProfileId);
        item.setItemType(ItemType.QUESTION);
        item.setCreateTime(new Date());
        item.setItemDomain(WanbaItemDomain.RECOMMEND);
        item.setDestId(String.valueOf(5));
        //    item.setDestId(String.valueOf(27));
        item.setLineKey(linekey);
        item.setScore(System.currentTimeMillis());

        try {
            AskServiceSngl.get().addItemByLineKey(item);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
