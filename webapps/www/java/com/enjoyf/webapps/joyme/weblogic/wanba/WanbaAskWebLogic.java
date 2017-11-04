package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenu;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenuField;
import com.enjoyf.platform.service.joymeapp.AppTopMenuCategory;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.joyme.dto.Wanba.ActivityTopMenuDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.QuestionDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaRecommendDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
@Service
public class WanbaAskWebLogic extends AbstractWanbaWebLogic {

    private static final int QUESTION_PAGESIZE = 10;

    private static String RECOMMEND_TAGID = "-10000";//推荐

    public ScoreRangeRows<QuestionDTO> queryLineKeyQuestionByTimeLimit(String lineKey, ScoreRange scoreRange, String profileId) throws ServiceException {
        ScoreRangeRows<Question> list = AskServiceSngl.get().queryQuestionByLineKeyRange(lineKey, scoreRange);
        List<Question> queryQuestionList = new ArrayList<Question>();
        for (Question question : list.getRows()) {
            queryQuestionList.add(question);
        }
        List<QuestionDTO> dtoList = queryQuetionDtoListByQuestionList(queryQuestionList, profileId, true);

        ScoreRangeRows<QuestionDTO> returnRows = new ScoreRangeRows<QuestionDTO>();
        ScoreRange returnPage = list.getRange();
        returnRows.setRange(returnPage);
        returnRows.setRows(dtoList);

        return returnRows;
    }

    public ScoreRangeRows<QuestionDTO> queryFollowQuestionList(String profileId, ScoreRange range) throws ServiceException {
        ScoreRangeRows<Question> quesionPageRows = AskServiceSngl.get().queryFollowQuestionByProfileId(profileId, range);
        List<QuestionDTO> dtoList = queryQuetionDtoListByQuestionList(quesionPageRows.getRows(), profileId, false);
        ScoreRangeRows<QuestionDTO> returnRows = new ScoreRangeRows<QuestionDTO>();
        ScoreRange returnPage = quesionPageRows.getRange();
        returnRows.setRange(returnPage);
        returnRows.setRows(dtoList);

        return returnRows;

    }


    public PageRows<QuestionDTO> queryQuestionByLineKey(String lineKey, Pagination page, boolean isDesc, String profileId, boolean isIgnoreAnswer) throws ServiceException {
        PageRows<Question> myQuestionRows = AskServiceSngl.get().queryQuestionByLineKey(lineKey, page, isDesc);

        List<QuestionDTO> returnList = queryQuetionDtoListByQuestionList(myQuestionRows.getRows(), profileId, isIgnoreAnswer);

        PageRows<QuestionDTO> returnRows = new PageRows<QuestionDTO>();
        returnRows.setPage(myQuestionRows.getPage());
        returnRows.setRows(returnList);

        return returnRows;

    }

    public PageRows<QuestionDTO> queryMyAnswerByLineKey(String answerProfileId, Pagination page, boolean isDesc, String queryProfileId) throws ServiceException {
        String linekey = AskUtil.getAskLineKey(answerProfileId, WanbaItemDomain.MYANSER);
        PageRows<Answer> answerPageRows = AskServiceSngl.get().queryAnswerByLineKeyRange(linekey, page, isDesc);
        PageRows<QuestionDTO> returnRows = new PageRows<QuestionDTO>();
        if (CollectionUtil.isEmpty(answerPageRows.getRows())) {
            returnRows.setPage(answerPageRows.getPage());
            return returnRows;
        }
        List<QuestionDTO> returnList = queryQuetionDtoListByAnswerList(answerPageRows.getRows(), queryProfileId);

        returnRows.setPage(answerPageRows.getPage());
        returnRows.setRows(returnList);

        return returnRows;

    }

    //推荐查询
    public PageRows<WanbaRecommendDTO> queryQuestionRecommendByLineKey(HttpServletRequest request, String lineKey, int pageNo,
                                                                       boolean isDesc, String profileId, boolean isIgnoreAnswer) throws ServiceException {
        Pagination page = new Pagination(QUESTION_PAGESIZE * pageNo, pageNo, QUESTION_PAGESIZE);


        //如果是推荐才查-10000
        String tagid = request.getParameter("tagid");
        Map<Long, ActivityTopMenu> adMap = new HashMap<Long, ActivityTopMenu>();
        if (RECOMMEND_TAGID.equals(tagid)) {
            //推荐的广告
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, AppUtil.getAppKey(HTTPUtil.getParam(request, "appkey"))));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, Integer.valueOf(HTTPUtil.getParam(request, "platform"))));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.WANBA_ASK_RECOMMEND_INSERT.getCode()));
            List<ActivityTopMenu> list = JoymeAppConfigServiceSngl.get().queryActivityTopMenu(queryExpress);
            for (ActivityTopMenu menu : list) {
                adMap.put(menu.getChannelId(), menu);
            }
        }

        //TODO
//        Map<String, ItemType> typeMap = AskServiceSngl.get().queryItemByLineKey(lineKey, page, true);

        PageRows<Question> myQuestionRows = AskServiceSngl.get().queryQuestionByLineKey(lineKey, page, true);


        //return
        List<WanbaRecommendDTO> returnList = new ArrayList<WanbaRecommendDTO>();
        if (!CollectionUtil.isEmpty(myQuestionRows.getRows())) {
            List<QuestionDTO> questionDTOList = queryQuetionDtoListByQuestionList(myQuestionRows.getRows(), profileId, isIgnoreAnswer);
            int advIndex = (pageNo - 1) * QUESTION_PAGESIZE;
            for (int i = 0; i < questionDTOList.size(); i++) {
                ///普通的问题
                WanbaRecommendDTO dto = new WanbaRecommendDTO();
                dto.setType(1);
                dto.setQuestion(questionDTOList.get(i));
                dto.setAdv(new ActivityTopMenuDTO());
                dto.setDaren(new ArrayList<WanbaProfileDTO>());
                returnList.add(dto);


                //插入广告
                ActivityTopMenu menu = adMap.get(Long.valueOf(advIndex + i + 1));
                if (menu != null) {
                    WanbaRecommendDTO admenu = new WanbaRecommendDTO();
                    admenu.setType(2);
                    admenu.setAdv(buildActivityTopMenu(menu));
                    admenu.setDaren(new ArrayList<WanbaProfileDTO>());
                    admenu.setQuestion(new QuestionDTO());
                    returnList.add(admenu);
                }
            }


            //推荐查询需要返回推荐人的列表
            if (RECOMMEND_TAGID.equals(tagid) && pageNo == 1) {
                WanbaRecommendDTO daren = getWanbaRecommendDTO();
                if (daren != null) {
                    returnList.add(daren);
                }
            }
        }

        PageRows<WanbaRecommendDTO> returnRows = new PageRows<WanbaRecommendDTO>();
        returnRows.setPage(myQuestionRows.getPage());
        returnRows.setRows(returnList);
        return returnRows;
    }

    public List<ActivityTopMenuDTO> queryActivityTopMenu(HttpServletRequest request, Integer channelid) throws ServiceException {
        List<ActivityTopMenuDTO> returnList = new ArrayList<ActivityTopMenuDTO>();

        String appkey = HTTPUtil.getParam(request, "appkey");
        String platform = HTTPUtil.getParam(request, "platform");
        if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(platform)) {
            return returnList;
        }
        List<ActivityTopMenu> list = JoymeAppConfigServiceSngl.get().queryActivityTopMenuList(AppUtil.getAppKey(appkey), Long.valueOf(channelid), Integer.valueOf(platform));
        if (!CollectionUtil.isEmpty(list)) {
            for (ActivityTopMenu topMenu : list) {
                returnList.add(buildActivityTopMenu(topMenu));
            }
        }
        return returnList;
    }


    //活动提问的时候，需要将问题加到活动的列表
    //对某个游戏标签提问的时候，需要将问题加到游戏问题列表
    public void addItemByLineKey(String tagid, WanbaItemDomain wanbaItemDomain, Question question) throws ServiceException {
        String ownProfileId = tagid;
        String linekey = AskUtil.getAskLineKey(ownProfileId, wanbaItemDomain);

        WanbaItem item = new WanbaItem();
        item.setOwnProfileId(ownProfileId);
        item.setItemType(ItemType.QUESTION);
        item.setCreateTime(new Date());
        item.setItemDomain(wanbaItemDomain);
        item.setDestId(String.valueOf(question.getQuestionId()));
        item.setLineKey(linekey);
        item.setScore(System.currentTimeMillis());//todo 这里用invite sum排序
        AskServiceSngl.get().addItemByLineKey(item);
    }


    private ActivityTopMenuDTO buildActivityTopMenu(ActivityTopMenu topMenu) {
        ActivityTopMenuDTO dto = new ActivityTopMenuDTO();
        dto.setDesc(topMenu.getMenuDesc());
        dto.setName(topMenu.getMenuName());
        dto.setPicurl(topMenu.getPicUrl());
        dto.setJt(String.valueOf(topMenu.getMenuType()));
        dto.setJi(topMenu.getLinkUrl());
        dto.setPictype(String.valueOf(topMenu.getRedirectType()));
        return dto;
    }


    //首页的推荐达人配置
    private WanbaRecommendDTO getWanbaRecommendDTO() throws ServiceException {
        WanbaRecommendDTO daren = null;

        List<String> darenLsit = AskServiceSngl.get().getWanbaRecommentProfiles();
        if (!CollectionUtil.isEmpty(darenLsit)) {
            Set<String> pidSet = new HashSet<String>();
            for (String pid : darenLsit) {
                if (StringUtil.isEmpty(pid)) {
                    continue;
                }
                pidSet.add(pid);
            }
            if (!CollectionUtil.isEmpty(pidSet)) {
                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pidSet);
                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(pidSet);
                List<WanbaProfileDTO> returnDarenList = new ArrayList<WanbaProfileDTO>();
                for (String pid : darenLsit) {
                    if (StringUtil.isEmpty(pid)) {
                        continue;
                    }
                    Profile profile = profileMap.get(pid);
                    if (profile != null) {
                        returnDarenList.add(wanbaProfileDTO(profile, wanbaProfileMap.get(pid)));
                    }
                }
                if (!CollectionUtil.isEmpty(returnDarenList)) {
                    daren = new WanbaRecommendDTO();
                    daren.setType(3);
                    daren.setQuestion(new QuestionDTO());
                    daren.setAdv(new ActivityTopMenuDTO());
                    daren.setDaren(returnDarenList);
                    // returnList.add(daren);
                }
            }
        }

        return daren;
    }


}
