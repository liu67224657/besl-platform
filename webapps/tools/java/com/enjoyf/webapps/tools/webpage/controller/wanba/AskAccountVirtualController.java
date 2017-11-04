package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.AccountVirtualType;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.notice.AppNoticeSum;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.tools.weblogic.dto.wanba.ReplyMessageListDTO;
import com.enjoyf.webapps.tools.weblogic.wanba.AskAccountVirtualWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */
@Controller
@RequestMapping(value = "/wanba/virtual")
public class AskAccountVirtualController extends AbstractWanbaController {

    @Resource(name = "askAccountVirtualWebLogic")
    private AskAccountVirtualWebLogic askAccountVirtualWebLogic;

    private static String WANBA_ASK_APPKEY = "3iiv7VWfx84pmHgCUqRwun";

    private static Set<NoticeType> types = new HashSet<NoticeType>();

    static {
        types.add(NoticeType.ANSWER);
        types.add(NoticeType.REPLY);
    }


    private static final String PROFILE_KEY = "www";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "ext", required = false) String ext,
                             @RequestParam(value = "remove_status", required = false, defaultValue = "1") String remove_status,
                             HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("virtualTypeColl", AccountVirtualType.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        try {

            queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
            queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CREATE_TIME, QuerySortOrder.DESC));
            if (!StringUtil.isEmpty(ext)) {
                queryExpress.add(QueryCriterions.like(WanbaProfileClassifyField.EXT, "%" + ext + "%"));
            }
            if (!StringUtil.isEmpty(remove_status)) {
                queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.getByCode(Integer.valueOf(remove_status)).getCode()));
            }
            PageRows<WanbaProfileClassify> pageRows = AskServiceSngl.get().queryWanbaProfileClassify(queryExpress, pagination);

            Map<String, Map<String, AppNoticeSum>> appNoticeSumMap = new HashMap<String, Map<String, AppNoticeSum>>();
            Set<String> pids = new HashSet<String>();
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (WanbaProfileClassify wanbaProfileClassify : pageRows.getRows()) {
                    pids.add(wanbaProfileClassify.getProfileid());
                    Map<String, AppNoticeSum> appSumMap = NoticeServiceSngl.get().queryAppNoticeSum(wanbaProfileClassify.getProfileid(), "", types);
                    appNoticeSumMap.put(wanbaProfileClassify.getProfileid(), appSumMap);

                }
            }
            mapMessage.put("appNoticeSumMap", appNoticeSumMap);


            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pids)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pids));

            }
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("remove_status", remove_status);
            mapMessage.put("ext", ext);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {

        }
        return new ModelAndView("/wanba/virtual/virtuallist", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/wanba/virtual/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "ext", required = false) String ext,
                               @RequestParam(value = "nick", required = false) String nick,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "sex", required = false) String sex,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Icons icons = null;
            if (!StringUtil.isEmpty(pic)) {
                icons = new Icons();
                icons.add(new Icon(0, pic));
            }
            String uno = UUID.randomUUID().toString();
            Profile profile = new Profile();
            profile.setNick(nick);
            profile.setCheckNick(nick);
            profile.setDescription(desc);
            profile.setProfileId(UserCenterUtil.getProfileId(uno, PROFILE_KEY));
            profile.setUno(uno);
            profile.setProfileKey(PROFILE_KEY);
            profile.setCreateIp("127.0.0.1");
            profile.setCreateTime(new Date());
            profile.setSex(sex);

            profile.setFlag(new ProfileFlag().has(ProfileFlag.FLAG_EXPLORE));
            if (!StringUtil.isEmpty(pic)) {
                profile.setIcon(pic);
                profile.setIcons(icons);
            }
            profile = UserCenterServiceSngl.get().createProfile(profile);

            if (profile.getUid() > 0) {
                WanbaProfileClassify classify = new WanbaProfileClassify();
                classify.setProfileid(profile.getProfileId());
                classify.setExt(profile.getNick());
                classify.setRemoveStatus(IntValidStatus.VALID);
                classify.setClassify_type(WanbaProfileClassifyType.WANBA_ASK_VIRTUAL);
                classify.setCreate_time(new Date());
                AskServiceSngl.get().insertWanbaProfileClassify(classify);


                //默认给一千积分
                UserPoint userPoint = new UserPoint();
                userPoint.setUserNo(profile.getUno());
                userPoint.setProfileId(profile.getProfileId());
                userPoint.setConsumeAmount(0);
                userPoint.setConsumeExchange(0);
                userPoint.setUserPoint(1000);
                userPoint.setPointKey(PointKeyType.getByCode(WANBA_ASK_APPKEY).getValue());
                PointServiceSngl.get().addUserPoint(userPoint);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/virtual/list");
    }


    @RequestMapping(value = "/realcreate")
    public ModelAndView realcreate(
            @RequestParam(value = "realnick", required = false) String nick,
            HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (profile != null) {
                WanbaProfileClassify classify = new WanbaProfileClassify();
                classify.setProfileid(profile.getProfileId());
                classify.setExt(profile.getNick());
                classify.setRemoveStatus(IntValidStatus.VALID);
                classify.setClassify_type(WanbaProfileClassifyType.WANBA_ASK_VIRTUAL);
                classify.setCreate_time(new Date());
                AskServiceSngl.get().insertWanbaProfileClassify(classify);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/virtual/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "classifyid") String classifyid,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            WanbaProfileClassify wanbaProfileClassify = AskServiceSngl.get().getWanbaProfileClassify(classifyid);
            mapMessage.put("wanbaProfileClassify", wanbaProfileClassify);
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(wanbaProfileClassify.getProfileid());
            mapMessage.put("profile", profile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/virtual/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "ext", required = false) String ext,
                               @RequestParam(value = "nick", required = false) String nick,
                               @RequestParam(value = "pic", required = false) String pic,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "profileid", required = false) String profileid,
                               @RequestParam(value = "classifyid") String classifyid,
                               HttpServletRequest request) {
        UpdateExpress updateExpress = new UpdateExpress();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            updateExpress.set(ProfileField.NICK, nick);
            updateExpress.set(ProfileField.CHECKNICK, nick);
            updateExpress.set(ProfileField.ICON, pic);
            Icons icons = null;
            if (!StringUtil.isEmpty(pic)) {
                icons = new Icons();
                icons.add(new Icon(0, pic));
            }
            updateExpress.set(ProfileField.ICONS, icons.toJsonStr());
            updateExpress.set(ProfileField.DESCRIPTION, desc);
            updateExpress.set(ProfileField.SEX, sex);
            boolean bval = UserCenterServiceSngl.get().modifyProfile(updateExpress, profileid);
            if (bval) {
                AskServiceSngl.get().modifyWanbaProfileClassify(new UpdateExpress().set(WanbaProfileClassifyField.EXT, nick), classifyid);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/virtual/list", map);
    }


    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "classifyid") String classifyid,
                               @RequestParam(value = "remove_status") String remove_status,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress up = new UpdateExpress();
            up.set(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.getByCode(Integer.valueOf(remove_status)).getCode());
            AskServiceSngl.get().modifyWanbaProfileClassify(up, classifyid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/wanba/virtual/list", mapMessage);
    }


    @RequestMapping(value = "/questionlist")
    public ModelAndView questionlist(@RequestParam(value = "profilieid") String profilieid,
                                     @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                     @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                                     HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<UserNotice> pageRows = askAccountVirtualWebLogic.queryUserNotice(profilieid, "", NoticeType.ANSWER, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("profilieid", profilieid);
            if (!StringUtil.isEmpty(profilieid)) {
                try {
                    NoticeServiceSngl.get().readNotice(profilieid, "", NoticeType.ANSWER);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " read notice error.e:", e);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/virtual/questionlist", mapMessage);
    }


    @RequestMapping(value = "/messagelist")
    public ModelAndView messagelist(@RequestParam(value = "profilieid") String profilieid,
                                    @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                    @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<ReplyMessageListDTO> pageRows = askAccountVirtualWebLogic.queryWanbaSocialMessageList(profilieid, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("profilieid", profilieid);
            if (!StringUtil.isEmpty(profilieid)) {
                try {
                    NoticeServiceSngl.get().readNotice(profilieid, "", NoticeType.REPLY);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " read notice error.e:", e);
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/wanba/virtual/messagelist", mapMessage);
    }


    @RequestMapping(value = "/postreplypage")
    public ModelAndView postreplypage(HttpServletRequest request,
                                      @RequestParam(value = "answerid") String answerid,
                                      @RequestParam(value = "parentid", defaultValue = "0") Integer parentid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
        queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CLASSIFY_ID, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.VALID.getCode()));
        try {
            List<WanbaProfileClassify> list = AskServiceSngl.get().queryWanbaProfileClassifyList(queryExpress);
            Set<String> pid = new HashSet<String>();
            if (!CollectionUtil.isEmpty(list)) {
                for (WanbaProfileClassify classify : list) {
                    pid.add(classify.getProfileid());
                }
            }
            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pid)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pid));

            }
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("list", list);
            mapMessage.put("answerid", answerid);
            mapMessage.put("parentid", parentid);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/virtual/reply", mapMessage);
    }


    //1对多 限时提问
    @RequestMapping(value = "/postquestionpage")
    public ModelAndView postquestionpage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
        queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CLASSIFY_ID, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.VALID.getCode()));
        try {
            List<WanbaProfileClassify> list = AskServiceSngl.get().queryWanbaProfileClassifyList(queryExpress);
            Set<String> pid = new HashSet<String>();
            if (!CollectionUtil.isEmpty(list)) {
                for (WanbaProfileClassify classify : list) {
                    pid.add(classify.getProfileid());
                }
            }
            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pid)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pid));

            }

            //提问人
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("list", list);


            //时间与积分对应关系
            mapMessage.put("questionconfigtype", QuestionConfigType.getAll());


            //标签
            QueryExpress qu = new QueryExpress();
            qu.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            qu.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_ASK.getCode()));
            qu.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(qu);

            List<AnimeTag> returTagList = new ArrayList<AnimeTag>();
            if (!CollectionUtil.isEmpty(animeTagList)) {
                for (AnimeTag tag : animeTagList) {
                    if (tag.getPicjson() != null && tag.getPicjson().getType().equals("0") && tag.getTag_id() > 0) {
                        returTagList.add(tag);
                    }
                }
            }
            mapMessage.put("animeTagList", returTagList);




            mapMessage.put("askpid", CookieUtil.getCookieValue(request, TIME_VIRTUAL_ASKPID));
            mapMessage.put("tagid", CookieUtil.getCookieValue(request, TIME_VIRTUAL_TAGID));
            mapMessage.put("timelimit", CookieUtil.getCookieValue(request, TIME_VIRTUAL_TIMELIMIT));


        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/virtual/question_timelimit", mapMessage);
    }


    //1对1提问
    @RequestMapping(value = "/invitequestionpage")
    public ModelAndView invitequestionpage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
        queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CLASSIFY_ID, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.VALID.getCode()));
        try {
            List<WanbaProfileClassify> list = AskServiceSngl.get().queryWanbaProfileClassifyList(queryExpress);
            Set<String> pid = new HashSet<String>();
            if (!CollectionUtil.isEmpty(list)) {
                for (WanbaProfileClassify classify : list) {
                    pid.add(classify.getProfileid());
                }
            }
            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pid)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pid));

            }

            //提问人
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("list", list);


            //达人类型
            QueryExpress qu = new QueryExpress();
            qu.add(QuerySort.add(AnimeTagField.DISPLAY_ORDER, QuerySortOrder.DESC));
            qu.add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.WANBA_VERIFYPROFILE.getCode()));
            qu.add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(qu);
            List<AnimeTag> returTagList = new ArrayList<AnimeTag>();
            if (!CollectionUtil.isEmpty(animeTagList)) {
                for (AnimeTag tag : animeTagList) {
                    if (tag.getTag_id() > 0) {
                        returTagList.add(tag);
                    }
                }
            }
            mapMessage.put("animeTagList", returTagList);

            mapMessage.put("askpid", CookieUtil.getCookieValue(request, ONE_VIRTUAL_ASKPID));
            mapMessage.put("tagid", CookieUtil.getCookieValue(request, ONE_VIRTUAL_TAGID));
            String invitepid = CookieUtil.getCookieValue(request, ONE_VIRTUAL_INVITEPID);

            if (!StringUtil.isEmpty(invitepid)) {
                mapMessage.put("invitepid", UserCenterServiceSngl.get().getProfileByProfileId(invitepid));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/virtual/question_invite", mapMessage);
    }


    //虚拟用户回答页面
    @RequestMapping(value = "/answerpage")
    public ModelAndView answerpage(HttpServletRequest request,
                                   @RequestParam(value = "questionid", required = false) String questionid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.CLASSIFY_TYPE, WanbaProfileClassifyType.WANBA_ASK_VIRTUAL.getCode()));
        queryExpress.add(QuerySort.add(WanbaProfileClassifyField.CLASSIFY_ID, QuerySortOrder.DESC));
        queryExpress.add(QueryCriterions.eq(WanbaProfileClassifyField.REMOVE_STATUS, IntValidStatus.VALID.getCode()));
        try {
            List<WanbaProfileClassify> list = AskServiceSngl.get().queryWanbaProfileClassifyList(queryExpress);
            Set<String> pid = new HashSet<String>();
            if (!CollectionUtil.isEmpty(list)) {
                for (WanbaProfileClassify classify : list) {
                    pid.add(classify.getProfileid());
                }
            }
            Map<String, Profile> profileMap = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pid)) {
                profileMap.putAll(UserCenterServiceSngl.get().queryProfiles(pid));

            }

            //回答人
            mapMessage.put("profileMap", profileMap);
            mapMessage.put("list", list);
            mapMessage.put("questionid", questionid);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/virtual/answerpage", mapMessage);
    }

    // 采纳答案
    @RequestMapping(value = "/answeraccept")
    public ModelAndView answeraccept(
            @RequestParam(value = "questionid", required = false) String questionid,
            @RequestParam(value = "answerid", required = false) String answerid,
            HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Question question = null;
        try {
            question = AskServiceSngl.get().getQuestion(Long.valueOf(questionid));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        String returnStr = "";
        if (question != null) {
            returnStr = askAccountVirtualWebLogic.accept(questionid, answerid, question.getAskProfileId());
        }
        return new ModelAndView("redirect:/wanba/ask/answer/list?qid=" + questionid + "&rstatus=1", mapMessage);
    }


    //真是用户
    @RequestMapping(value = "/reallist")
    public ModelAndView reallist(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                 @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(value = "nick", required = false) String nick,
                                 HttpServletRequest request
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        try {

            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (profile != null) {
                WanbaProfileSum wanbaProfileSum = AskServiceSngl.get().getWanProfileSum(profile.getProfileId());
                mapMessage.put("wanbaProfileSum", wanbaProfileSum);

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(QuestionField.ASKPROFILEID, profile.getProfileId()));
                queryExpress.add(QueryCriterions.eq(QuestionField.REMOVESTATUS, IntValidStatus.UNVALID.getCode()));
                PageRows<Question> questionPageRows = AskServiceSngl.get().queryQuestion(queryExpress, pagination);
                int size = 0;
                if (questionPageRows != null || !CollectionUtil.isEmpty(questionPageRows.getRows())) {
                    size = questionPageRows.getPage().getTotalRows();
                }
                mapMessage.put("questionPageRows", size);

                VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profile.getProfileId());
                mapMessage.put("verifyProfile", verifyProfile);
            }

            mapMessage.put("profile", profile);
            mapMessage.put("nick", nick);
        } catch (Exception e) {

        }
        return new ModelAndView("/wanba/virtual/reallist", mapMessage);
    }


}
