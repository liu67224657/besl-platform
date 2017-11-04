package com.enjoyf.platform.tools.ask;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.serv.ask.AskConfig;
import com.enjoyf.platform.serv.ask.AskLogic;
import com.enjoyf.platform.serv.usercenter.UserCenterCache;
import com.enjoyf.platform.serv.usercenter.UsercenterRedis;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2016/10/20
 */
public class ImportAskByExcel {

    //private static String dir = "C:\\Users\\pengxu\\Desktop\\asktemplate";
    private static String dir = "/home/ops/services/asktemplate/asktemplate";

    private static String xlsxPath = dir + "/template.xlsx";

    public static String IMAGE_DIR = dir + "/image/";

    public static String HEAD_DIR = dir + "/head/";

    public static String VOICE_DIR = dir + "/voice/";


    private static Map<String, Long> gameName2TagId = new HashMap<String, Long>();

    private static Map<String, Long> gameName2PTagId = new HashMap<String, Long>();
    private static Map<String, Long> verifyMap = new HashMap<String, Long>();


    private static UserCenterHandler userCenterHandler = null;
    private static AskLogic askLogic = null;
    private static UsercenterRedis userCenterRedis = null;
    private static UserCenterCache userCenterCache = null;
    private static final String PROFILE_KEY = "www";

    /**
     * 5分钟-100分
     * 10分钟-60分
     * 20分钟-40分
     * 30分钟-20分
     */
    private static Map<String, QuestionConfig> limit2QuestionConfig = new HashMap<String, QuestionConfig>();


    static {
        gameName2TagId.put("克鲁赛德战记", 110l);
        gameName2TagId.put("勇者斗恶龙X", 111l);
        gameName2TagId.put("王者荣耀", 112l);
        gameName2TagId.put("奇迹暖暖", 113l);
        gameName2TagId.put("阴阳师", 114l);

        gameName2PTagId.put("阴阳师", 100l);
        gameName2PTagId.put("克鲁赛德战记", 101l);
        gameName2PTagId.put("奇迹暖暖", 102l);

        limit2QuestionConfig.put("5分钟", QuestionConfigType.FIV_MIN);
        limit2QuestionConfig.put("10分钟", QuestionConfigType.TEN_MIN);
        limit2QuestionConfig.put("20分钟", QuestionConfigType.TWENTY_MIN);
        limit2QuestionConfig.put("30分钟", QuestionConfigType.THIRTY_MIN);

        verifyMap.put("核心玩家", 1l);
        verifyMap.put("游戏大神", 2l);
        verifyMap.put("氪金高手", 3l);
    }


    public static void main(String[] args) throws Exception {
        //上传图片
        Map<String, String> imageMap = loadExcelUtil.uploadImage(IMAGE_DIR);
        System.out.println(imageMap);
        //  上传语音
        Map<String, String> voiceMap = loadExcelUtil.uploadImage(VOICE_DIR);
        System.out.println(voiceMap);

        // 上传头像
        Map<String, String> headMap = loadExcelUtil.uploadImage(HEAD_DIR);
        System.out.println(headMap);

        Workbook workbook = loadExcelUtil.loadExcel(xlsxPath);
//
        FiveProps fiveProps = Props.instance().getServProps();
        try {
            userCenterHandler = new UserCenterHandler("usercenter", fiveProps);
            userCenterRedis = new UsercenterRedis(fiveProps);
            userCenterCache = new UserCenterCache(new MemCachedConfig(fiveProps));
            askLogic = new AskLogic(new AskConfig(fiveProps));

            //joymeapp handler tag
        } catch (DbException e) {
            e.printStackTrace();
            Utility.sleep(5000);
            System.exit(0);
        }
//
        // 生成用户
        Map<String, String> profileMap = importWanbaProfile(workbook, headMap);
        System.out.println("================profileMap start=================");
        System.out.println(profileMap);
        System.out.println("================profileMap end=================");


        //  生成问题
        System.out.println("================question start=================");

        Map<String, Question> questionMap = importQuestion(workbook, profileMap, imageMap, voiceMap);
        System.out.println(questionMap);
        System.out.println("================question end=================");

        //  生成答案
        System.out.println("================answer start=================");

        Map<String, Long> answerMap = importAnswer(workbook, questionMap, profileMap, imageMap, voiceMap);

        System.out.println(answerMap);
        System.out.println("================answer end=================");

    }

    private static Map<String, String> importWanbaProfile(Workbook workbook, Map<String, String> headIconMaps) throws ServiceException {
        Sheet profileSheet = workbook.getSheet("用户");

        //隔壁老王|是|阴阳师游戏达人|男
        List<String> list = loadExcelUtil.exportListFromExcel(profileSheet, workbook);
        Map<String, String> profileMap = new HashMap<String, String>();
        int i = 0;
        for (String profileStr : list) {
            if (i != 0) {
                System.out.println(profileStr);
                Map<String, String> map = strToProfile(profileStr, headIconMaps);
                if (map != null) {
                    profileMap.putAll(map);
                }
            }
            i++;
        }
        return profileMap;
    }

    private static Map<String, String> strToProfile(String questionStr, Map<String, String> headIconMaps) throws ServiceException {
        String[] array = questionStr.split("\\|");
        if (array == null) {
            return null;
        }
//隔壁老王	是	阴阳师游戏达人	男
        String nick = array[0];
        String descrption = array[2];
        long type = array[1].equals("是") ? 1 : 0;
        String sex = array[3].equals("男") ? "1" : "0";
        String icon = headIconMaps.get(nick);

        //todo 娄超加上
        int askPoint = 0;
        try {
            if (!StringUtil.isEmpty(array[6]) && !"null".equals(array[6])) {
                askPoint = (int) Double.parseDouble(array[6]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //todo 娄超加上
        long tagId = 0;
        if (!StringUtil.isEmpty(array[5]) && gameName2PTagId.containsKey(array[5])) {
            tagId = gameName2PTagId.get(array[5]);
        }

        Map<String, String> map = new HashMap<String, String>();
        Profile profile = userCenterHandler.getProfile(new QueryExpress().add(QueryCriterions.eq(ProfileField.CHECKNICK, nick.toLowerCase())));

        Icons icons = null;
        if (!StringUtil.isEmpty(icon)) {
            icons = new Icons();
            icons.add(new Icon(0, icon));
        }

        if (profile == null) {
            String uno = UUID.randomUUID().toString();
            profile = new Profile();
            profile.setNick(nick);
            profile.setDescription(array[2]);
            profile.setProfileId(UserCenterUtil.getProfileId(uno, PROFILE_KEY));
            profile.setUno(uno);
            profile.setProfileKey(PROFILE_KEY);
            profile.setCreateIp("127.0.0.1");
            profile.setCreateTime(new Date());
            profile.setSex(sex);
            profile.setFlag(new ProfileFlag().has(ProfileFlag.FLAG_EXPLORE));
            if (!StringUtil.isEmpty(icon)) {
                profile.setIcon(icon);
                profile.setIcons(icons);
            }
            profile = userCenterHandler.createProfile(profile);
        }
        boolean b = userCenterHandler.modifyProfile(new UpdateExpress().set(ProfileField.SEX, sex).set(ProfileField.ICON, icon).set(ProfileField.ICONS, icons.toJsonStr()), profile.getProfileId());
        if (b) {
            userCenterCache.deleteProfile(profile);
        }

        if (profile != null && type == 1 && tagId > 0) {
            VerifyProfile wanbaProfile = userCenterHandler.getVerifyProfileByProfileId(profile.getProfileId());
            if (wanbaProfile == null) {
                wanbaProfile = new VerifyProfile();
                wanbaProfile.setAskPoint(askPoint);
                wanbaProfile.setDescription(descrption);
                wanbaProfile.setNick(nick);
                wanbaProfile.setProfileId(profile.getProfileId());
                wanbaProfile.setVerifyType(StringUtil.isEmpty(array[4]) ? 0L : verifyMap.get(array[4]));
                userCenterHandler.insertVerifyProfile(wanbaProfile);
                if (tagId > 0) {
                    if (!userCenterRedis.existsPrifileIdByTagId(tagId, profile.getProfileId())) {
                        userCenterRedis.addProfileIdByTagId(tagId, profile.getProfileId(), System.currentTimeMillis());
                    }
                    if (!userCenterRedis.existsPrifileIdByTagId(-1l, profile.getProfileId())) {
                        userCenterRedis.addProfileIdByTagId(-1l, profile.getProfileId(), System.currentTimeMillis());
                    }
                }
            }
        }
        map.put(nick, profile.getProfileId());
        return map;
    }


    private static Map<String, Question> importQuestion(Workbook workbook, Map<String, String> pidMap, Map<String, String> imageMap, Map<String, String> voiceMap) throws ParseException, ServiceException {
        Sheet questionSheet = workbook.getSheet("问题");
        List<String> list = loadExcelUtil.exportListFromExcel(questionSheet, workbook);
        Map<String, Question> returnMap = new HashMap<String, Question>();
        int i = 0;
        for (String questionStr : list) {
            if (i != 0) {
                System.out.println(questionStr);
                Map<String, Question> map = strToQuestion(questionStr, pidMap, imageMap, voiceMap);
                if (map != null) {
                    returnMap.putAll(map);
                }

            }
            i++;
        }
        return returnMap;
    }

    //问题ID	问题类型（一对一/抢答）	问题分类（游戏名）	提问者昵称	提问内容	提问时间 todo 抢答时间/邀请人昵称 声音/声音时长 娄超提供
    private static Map<String, Question> strToQuestion(String questionStr, Map<String, String> pidMap, Map<String, String> imageMap, Map<String, String> voiceMap) throws ParseException, ServiceException {
        String[] array = questionStr.split("\\|");
        if (array == null) {
            return null;
        }
        Map<String, Question> map = new HashMap<String, Question>();
        long qidLong = Math.round(Double.parseDouble(array[0]));
        String sheetQid = String.valueOf(qidLong);
        QuestionType questionType = array[1].equalsIgnoreCase("一对一") ? QuestionType.ONEONONE : QuestionType.TIMELIMIT;
        long tagId = -1l;
        if (questionType.equals(QuestionType.TIMELIMIT)) {
            tagId = gameName2TagId.get(array[2]);
        }
        String nick = array[3];//todo
        if (StringUtil.isEmpty(nick) || !pidMap.containsKey(nick)) {
            return map;
        }
        String pid = pidMap.get(nick);

        String title = array[4];
        String content = array[5];//todo
        String voice = (!StringUtil.isEmpty(array[8]) && !"null".equalsIgnoreCase(array[8]) && voiceMap.containsKey(array[8])) ? voiceMap.get(array[8]) : "";
        long voiceTime = 0;
        if (!StringUtil.isEmpty(voice) && !StringUtil.isEmpty(array[9])) {
            voiceTime = (long) (Double.parseDouble(array[9]) * 1000l);
        }

        long dateLong = Math.round(Double.parseDouble(array[6]));
        Date date = DateUtil.formatStringToDate(String.valueOf(dateLong), "yyyyMMddHHmm");
        QuestionConfig questionConfig = null;
        String inviteNick = "";
        if (questionType.equals(QuestionType.TIMELIMIT)) {
            questionConfig = limit2QuestionConfig.get(array[7]);
        } else {
            inviteNick = array[7];
        }

        Question question = new Question();
        question.setAskProfileId(pid);
        question.setType(questionType);
        question.setTitle(title);

        Map<String, AskBody> bodyMap = convertStringAskBody(content, imageMap);
        question.setRichText(bodyMap.keySet().iterator().next());
        question.setBody(bodyMap.values().iterator().next());
        question.setAskVoice(new AskVoice(voice, voiceTime));
        question.setCreateTime(date);
        if (questionType.equals(QuestionType.ONEONONE)) {
            Profile profile = userCenterHandler.getProfile(new QueryExpress().add(QueryCriterions.eq(ProfileField.NICK, inviteNick)));
            question.setInviteProfileId(profile.getProfileId());
        }
        if (questionType.equals(QuestionType.TIMELIMIT)) {
            question.setQuestionPoint(questionConfig.getQuestionPoint());
            question.setGameId(Long.valueOf(tagId));
            question.setTimeLimit(date.getTime() + questionConfig.getTimeLimit());
        }

        question = askLogic.postQuestion(question);
        map.put(sheetQid, question);
        return map;
    }


    //关联问题ID	是否最佳答案	是否抢答分类	回答者昵称	回答内容	回答时间
    private static Map<String, Long> importAnswer(Workbook workbook, Map<String, Question> qidMap, Map<String, String> pidMap, Map<String, String> imageMap, Map<String, String> voiceMap) throws ParseException, ServiceException {
        Sheet questionSheet = workbook.getSheet("回答");
        List<String> list = loadExcelUtil.exportListFromExcel(questionSheet, workbook);
        Map<String, Long> returnMap = new HashMap<String, Long>();
        int i = 0;
        for (String answerStr : list) {
            if (i != 0) {
                System.out.println(answerStr);
                Map<String, Long> map = strToAnswer(answerStr, qidMap, pidMap, imageMap, voiceMap);
                if (map != null) {
                    returnMap.putAll(map);
                }
            }
            i++;
        }
        return returnMap;
    }

    //关联问题ID	是否最佳答案	是否抢答分类	回答者昵称	回答内容	回答时间
    private static Map<String, Long> strToAnswer(String answerStr, Map<String, Question> qidMap, Map<String, String> pidMap, Map<String, String> imageMap, Map<String, String> voiceMap) throws ParseException, ServiceException {
        String[] array = answerStr.split("\\|");
        if (array == null) {
            return null;
        }
        Map<String, Long> map = new HashMap<String, Long>();

        long longQid = Math.round(Double.parseDouble(array[0]));
        String sheetQid = String.valueOf(longQid);
        if (sheetQid == null && qidMap.containsKey(sheetQid)) {
            return map;
        }

        boolean isAccept = "true".equalsIgnoreCase(array[1]) ? true : false;
        String nick = array[3];
        if (StringUtil.isEmpty(nick) || !pidMap.containsKey(nick)) {
            return map;
        }
        String pid = pidMap.get(nick);
        String content = array[4];//todo
        AskVoice askVoice = new AskVoice();
        String voice = (!StringUtil.isEmpty(array[6]) && !"null".equals(array[6]) && voiceMap.containsKey(array[6])) ? voiceMap.get(array[6]) : "";
        if (!StringUtil.isEmpty(voice)) {
            long voiceTime = (long) (Double.parseDouble(array[7]) * 1000l);
            askVoice.setTime(voiceTime);
            askVoice.setUrl(voice);
        }
        long dateLong = Math.round(Double.parseDouble(array[5]));
        Date date = DateUtil.formatStringToDate(String.valueOf(dateLong), "yyyyMMddHHmm");

        Question q = qidMap.get(sheetQid);

        Answer answer = new Answer();
        answer.setAnswerProfileId(pid);
        answer.setAnswerTime(date);
        Map<String, AskBody> bodyMap = convertStringAskBody(content, imageMap);
        answer.setRichText(bodyMap.keySet().iterator().next());
        answer.setBody(bodyMap.values().iterator().next());
        answer.setAskVoice(askVoice);
        answer.setQuestionId(q.getQuestionId());
        answer = askLogic.answer(answer);
//
        if (isAccept) {
            if (q.getType().equals(QuestionType.TIMELIMIT)) {
                askLogic.acceptAnswer(answer.getAnswerId(), q.getQuestionId());
                askLogic.modifyAnswer(new UpdateExpress().set(AnswerField.IS_ACCEPT, isAccept), answer.getAnswerId());
            }
        }

        map.put(sheetQid, answer.getAnswerId());

        return map;
    }


    private static Map<String, AskBody> convertStringAskBody(String body, Map<String, String> questionMap) {
        List<Map<String, String>> localImageMap = RegexUtil.fetch(body, Pattern.compile("\\[image:([^.]+.jpg)\\]"), -1);
        for (Map<String, String> regImage : localImageMap) {
            String imageName = regImage.get("1");
            String imageTag = regImage.get("0");
            if (questionMap != null) {
                String imageUrl = questionMap.get(imageName);
                if (imageUrl != null) {
                    body.replaceAll(imageTag, "<jmwbimg:" + imageUrl + ">");
                }
            }
        }
        Map<String, AskBody> map = new HashMap<String, AskBody>();
        map.put(body.replace("\\n", "\n"), AskUtil.getQuestionBody(body.replace("\\n", "\n")));
        return map;
    }

}
