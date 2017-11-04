package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.PointHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentRelation;
import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.dto.point.ActionHistoryDTO;
import com.enjoyf.webapps.joyme.dto.point.RankDTO;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-12
 * Time: 下午1:41
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "pointWebLogic")
public class PointWebLogic {
    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    public PointResultMsg awardPoint(PointActionHistory history) throws ServiceException {
        PointAwardStragy stragy = PointAwardFactory.getStragyByPointActionType(history.getActionType());

        if (stragy == null) {
            throw new ServiceException(PointServiceException.NOT_SUPPORT_STRAGY, "point actionType has not suppport stragy.actiontype: " + history.getActionType());
        }

        return stragy.arwardPoint(history);
    }

    //给文章调整积分的webLogic
    public PointResultMsg adjustContentPoint(Content content, ProfileBlog authorBlog, ProfileBlog adminBlog, int point, GameResource groupResource, String pointReason) throws ServiceException {

        Date now = new Date();
        PointActionHistory increasePointActionHistory = new PointActionHistory();
        increasePointActionHistory.setUserNo(adminBlog.getUno());
        increasePointActionHistory.setCreateDate(now);
        increasePointActionHistory.setActionDate(now);
        increasePointActionHistory.setActionDescription(pointReason);
        increasePointActionHistory.setActionType(PointActionType.CONTENT_ADMIN_ADJUST_POINT);
        increasePointActionHistory.setPointValue(-point);
        increasePointActionHistory.setDestId(content.getContentId());
        increasePointActionHistory.setDestUno(content.getUno());

        PointResultMsg result = awardPoint(increasePointActionHistory);

        if (result.equals(PointResultMsg.SUCCESS)) {
            ContentRelation contentRelation = new ContentRelation();
            contentRelation.setContentId(content.getContentId());
            contentRelation.setRelationId(String.valueOf(groupResource.getResourceId()));
            contentRelation.setRelationType(ContentRelationType.ADMIN_ADJUST_POINT);
            contentRelation.setRemoveStatus(ActStatus.UNACT);
            contentRelation.setRelationValue(String.valueOf(point));
            contentRelation.setCreateDate(new Date());
            contentRelation = ContentServiceSngl.get().createContentRelation(content.getContentId(), contentRelation);

            //send system notice
            sendAdjustContentNotice(content, authorBlog, adminBlog, groupResource, point);
        }

        return result;
    }

    //有给文章加积分的权限
    public boolean hasAdjustContentPointPrivacy(String uno, Content content, Map<Long, GameResource> ownGroupResource) {
        boolean bVal = content.getRelationSet() != null && content.getRelationSet().getGroupRelation() != null;
        if (!bVal) {
            return bVal;
        }

        List<String> superUnos = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getGameSuperPrivacyUnoList();
        if (superUnos.contains(uno)) {
            return bVal;
        }

        try {
            if (ownGroupResource.containsKey(Long.parseLong(content.getRelationSet().getGroupRelation().getRelationId()))) {
                return bVal;
            }
        } catch (NumberFormatException e) {
        }

        bVal = false;
        return bVal;
    }

    //得到该用户该日 该类型的动作剩余多少分
    public PointReduceResult getPointByActionType(String uno, Date date, PointActionType pointActionType) throws ServiceException {
        PointReduceResult returnObj = null;

        PointProps pointProps = HotdeployConfigFactory.get().getConfig(PointHotdeployConfig.class).getPropsMap().get(PointActionType.CONTENT_ADMIN_ADJUST_POINT);
        int limit = pointProps.getLimit();
        if (pointProps == null || limit <= 0) {
            returnObj = new PointReduceResult();
            returnObj.setType(PointReduceType.NOT_LIMIT);
            return returnObj;
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserDayPointField.USERNO, uno));
        queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTDATE, date));
        queryExpress.add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, pointActionType.getCode()));
        UserDayPoint userDayPoint = PointServiceSngl.get().getUserDayPoint(queryExpress, date);

        //分享类型的day limit超过
        if (userDayPoint == null) {
            returnObj = new PointReduceResult();
            returnObj.setType(PointReduceType.HAS_LIMIT);
            returnObj.setReduceValue(limit);
            return returnObj;
        }

        returnObj = new PointReduceResult();
        returnObj.setType(PointReduceType.HAS_LIMIT);
        returnObj.setReduceValue(limit + userDayPoint.getPointValue());

        return returnObj;
    }


    private void sendAdjustContentNotice(Content content, ProfileBlog author, ProfileBlog adminBlog, GameResource group, int point) {

        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("URL_WWW", WebappConfig.get().getUrlWww());
        paramMap.put("contentId", content.getContentId());
        paramMap.put("groupname", group.getResourceName());
        paramMap.put("adminname", adminBlog.getScreenName());
        paramMap.put("subject", content.getSubject());
        paramMap.put("point", String.valueOf(point));

        try {
            String messageBody = NamedTemplate.parse(templateConfig.getAdminAdjustPointTemplate()).format(paramMap);

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(author.getUno());
            message.setRecieverUno(author.getUno());
            message.setSendDate(new Date());
            MessageServiceSngl.get().postMessage(author.getUno(), message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcetpion.e", e);
        }

    }


    //Client领号

    public UserExchangeLog getCode(String uno, String profileId, String appKey, Long gid, String ip, boolean isFree) throws ServiceException {
        UserExchangeLog userExchangeLog = PointServiceSngl.get().exchangeGoodsItem(uno, profileId, appKey, gid, new Date(), ip, "client", isFree);
        return userExchangeLog;
    }

    public List<ExchangeGoodsItem> taoCode(String uno, String profileId, String appkey, Long gid, String ip) {
        try {
            return PointServiceSngl.get().taoExchangeGoodsItemByGoodsId(uno, profileId, appkey, gid, new Date(), ip);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcetpion.e", e);
        }

        return null;
    }


    public PageRows<ActivityMygiftDTO> queryUserExchangeLogByUno(String uno, Pagination pagination) {
        PageRows<ActivityMygiftDTO> returnObject = new PageRows<ActivityMygiftDTO>();

        try {
            PageRows<UserExchangeLog> page = PointServiceSngl.get().queryUserExchangeByUno(uno, pagination);
            if (CollectionUtil.isEmpty(page.getRows())) {
                returnObject.setPage(page.getPage());
                return returnObject;
            }
            long tomorrow = 60l * 60l * 24l * 1000l - 1000l;
            List<ActivityMygiftDTO> list = new ArrayList<ActivityMygiftDTO>();
            for (UserExchangeLog log : page.getRows()) {
                ActivityMygiftDTO activityMygiftDTO = null;
                try {
                    activityMygiftDTO = new ActivityMygiftDTO();
                    activityMygiftDTO.setDesc(log.getGoodsDesc());
                    activityMygiftDTO.setGid(log.getGoodsId());
                    //todo in idSet
                    ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(log.getGoodsId());
                    activityMygiftDTO.setClientEndTime(activityGoods.getEndTime().getTime() + tomorrow);
                    activityMygiftDTO.setAid(activityGoods.getActivityGoodsId());
                    activityMygiftDTO.setGipic(activityGoods.getActivityPicUrl());
                    activityMygiftDTO.setTitle(activityGoods.getActivitySubject());
                } catch (Exception e) {
                    //todo 临时处理
                    continue;
                }
                list.add(activityMygiftDTO);
            }
            returnObject.setRows(list);
            returnObject.setPage(page.getPage());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcetpion.e", e);
        }
        return returnObject;
    }


    //玩霸3.0加积分

    /**
     * 增加成功则返回增加的积分数  不成功或者超出次数 返回0
     *
     * @param pointActionType
     * @param profileId
     * @param appkey
     * @param
     * @return
     */

    public int modifyUserPoint(PointActionType pointActionType, String profileId, String appkey, int point, LoginDomain loginDomain) {
        try {
            if (pointActionType == null || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey)) {
                return 0;
            }
            appkey = AppUtil.getAppKey(appkey);
            if (appkey.equals("default")) {
                point = getPointRandomNum(-0.1, 0.1, point); //wiki操作上下浮动10%
            }
            PointAwardStragy pointAwardStragy = PointAwardFactory.getStragyByPointActionType(pointActionType);
            if (pointAwardStragy == null) {
                return 0;
            }

            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setProfileId(profileId);
            pointActionHistory.setActionType(pointActionType);
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            pointActionHistory.setPointValue(point);
            pointActionHistory.setAppkey(appkey);
            pointActionHistory.setLogindomain(loginDomain == null ? "" : loginDomain.getCode());
            PointResultMsg pointResultMsg = pointAwardStragy.arwardPoint(pointActionHistory);
            if (pointResultMsg.equals(PointResultMsg.SUCCESS)) {
                return point;
            } else if (pointResultMsg.equals(PointResultMsg.OUT_OF_POINT_LIMIT)) {
                return PointResultMsg.OUT_OF_POINT_LIMIT.getCode();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "modifyUserPoint occured ServiceExcetpion.e", e);
            return 0;
        }
        return 0;
    }


    //获取用户积分
    public UserPoint getUserPoint(String appkey, String profileId) throws ServiceException {

        appkey = AppUtil.getAppKey(appkey);

        PointKeyType pointKeyType = PointServiceSngl.get().getPointKeyType(appkey);
        String pointKey = "";
        if (pointKeyType == null) {
            pointKey = "default";
        } else {
            pointKey = pointKeyType.getValue();
        }

        UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profileId))
                .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKey)));
        if (userPoint == null) {
            userPoint = new UserPoint();
            userPoint.setProfileId(profileId);
            userPoint.setConsumeAmount(0);
            userPoint.setConsumeExchange(0);
            userPoint.setUserPoint(0); //积分
            userPoint.setPrestige(0);   //声望
            userPoint.setPointKey(pointKey);
            userPoint.setWorshipNum(0);
        }
        return userPoint;
    }

    public int reduceUserPoint(PointActionType pointActionType, String profileId, String appKey, int point) {
        try {
            if (pointActionType == null || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appKey)) {
                return 0;
            }
            PointAwardStragy pointAwardStragy = PointAwardFactory.getStragyByPointActionType(pointActionType);
            if (pointAwardStragy == null) {
                return 0;
            }

            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setProfileId(profileId);
            pointActionHistory.setActionType(pointActionType);
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            pointActionHistory.setPointValue(point);
            pointActionHistory.setAppkey(appKey);
            PointResultMsg pointResultMsg = pointAwardStragy.arwardPoint(pointActionHistory);
            if (pointResultMsg.equals(PointResultMsg.SUCCESS)) {
                return point;
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "modifyUserPoint occured ServiceExcetpion.e", e);
            return 0;
        }
        return 0;
    }

    private final String APPKEY = "default";

    /**
     * 膜拜操作
     *
     * @param pointActionType
     * @param currentProfileId //膜拜人
     * @param reportProfileId  //被膜拜人
     * @param num              //膜拜次数
     * @return
     */
    @Deprecated//todo 用户激励体系删除
    public String worshipReport(PointActionType pointActionType, String currentProfileId, String reportProfileId, String num) {
        try {
            int prestige = PointAwardFactory.getPrestigeNum(pointActionType);  //膜拜增加的声望
            int point = PointAwardFactory.getPointAwardNum(pointActionType); //膜拜需要消耗积分
            int intNum = 1;
            try {
                intNum = Integer.parseInt(num); //次数
            } catch (NumberFormatException e) {
                return ResultCodeConstants.FAILED.getJsonString();
            }
            if (intNum < 0) {
                return ResultCodeConstants.FAILED.getJsonString();
            }
            Set<String> pids = new HashSet<String>();
            pids.add(currentProfileId);
            pids.add(reportProfileId);
            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pids);
            if (profileMap == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            if (profileMap.get(currentProfileId) == null || profileMap.get(reportProfileId) == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            UserPoint userPoint = getUserPoint(APPKEY, currentProfileId);
            if (userPoint.getUserPoint() < point * intNum) {
                return ResultCodeConstants.POINT_NOT_ENOUGH.getJsonString();
            }

            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setProfileId(currentProfileId);
            pointActionHistory.setActionType(pointActionType);
            pointActionHistory.setActionDescription("向【" + profileMap.get(reportProfileId).getNick() + "】膜拜" + intNum + "次，积分-" + point * intNum);
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            pointActionHistory.setDestId(reportProfileId);
            pointActionHistory.setPointValue(-(point * intNum));
            pointActionHistory.setAppkey(APPKEY);
            pointActionHistory.setPointkey(PointKeyType.getByCode(APPKEY).getValue());
            pointActionHistory.setHistoryActionType(HistoryActionType.POINT);

            boolean bool = PointServiceSngl.get().increasePointActionHistory(pointActionHistory, PointKeyType.getByCode(APPKEY));
            if (bool) {
                pointActionHistory = new PointActionHistory();
                pointActionHistory.setProfileId(reportProfileId);
                pointActionHistory.setActionType(pointActionType);
                pointActionHistory.setActionDate(new Date());
                pointActionHistory.setCreateDate(new Date());
                pointActionHistory.setDestId(currentProfileId);
                pointActionHistory.setActionDescription("被【" + profileMap.get(currentProfileId).getNick() + "】膜拜" + intNum + "次，声望+" + prestige);
                pointActionHistory.setPointValue(0);
                pointActionHistory.setPrestige(prestige * intNum);
                pointActionHistory.setAppkey(APPKEY);
                pointActionHistory.setWorshipNum(intNum);
                pointActionHistory.setHistoryActionType(HistoryActionType.PRESTIGE);
                pointActionHistory.setPointkey(PointKeyType.getByCode(APPKEY).getValue());

                bool = PointServiceSngl.get().increasePointActionHistory(pointActionHistory, PointKeyType.getByCode(APPKEY));
            }
            if (!bool) {
                ResultCodeConstants.FAILED.toString();
            }
        } catch (ServiceException e) {
            if (e.equals(PointServiceException.USER_POINT_NOT_ENOUGH)) {
                return ResultCodeConstants.POINT_NOT_ENOUGH.getJsonString();
            }
            GAlerter.lab(this.getClass().getName() + " worshipReport ServiceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " worshipReport Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    /**
     * 感谢操作
     *
     * @param pointActionType
     * @param currentProfileId
     * @param reportProfileId
     * @return
     */
    public String thanksReport(PointActionType pointActionType, String currentProfileId, String reportProfileId) {
        try {
            int prestige = PointAwardFactory.getPrestigeNum(pointActionType);  //感谢增加的声望
            Set<String> pids = new HashSet<String>();
            pids.add(currentProfileId);
            pids.add(reportProfileId);
            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pids);
            if (profileMap == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            if (profileMap.get(currentProfileId) == null || profileMap.get(reportProfileId) == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setProfileId(reportProfileId);
            pointActionHistory.setActionType(pointActionType);
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            pointActionHistory.setDestId(currentProfileId);
            pointActionHistory.setActionDescription("被【" + profileMap.get(currentProfileId).getNick() + "】感谢，声望+" + prestige);
            pointActionHistory.setPointValue(0);
            pointActionHistory.setPrestige(prestige);
            pointActionHistory.setAppkey(APPKEY);
            pointActionHistory.setHistoryActionType(HistoryActionType.PRESTIGE);
            pointActionHistory.setPointkey(PointKeyType.getByCode(APPKEY).getValue());
            boolean bool = PointServiceSngl.get().increasePointActionHistory(pointActionHistory, PointKeyType.getByCode(APPKEY));
            if (bool) {
                wikiReport(PointActionType.WIKI_THANKS_AUTHOR, currentProfileId);//感谢加积分
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " worshipReport ServiceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " worshipReport Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    /**
     * wiki积分声望上报
     *
     * @param pointActionType //事件
     * @param reportProfileId //获得积分、声望的PID
     * @return
     */
    public String wikiReport(PointActionType pointActionType, String reportProfileId) {
        try {
            int prestige = 0;  //增加的声望
            int point = 0; //增加的声望
            if (pointActionType.getValue() == HistoryActionType.POINT.getCode()) {  //判断是积分操作还是声望操作
                point = PointAwardFactory.getPointAwardNum(pointActionType);
                point = getPointRandomNum(-0.1, 0.1, point); //wiki操作上下浮动10%
            } else {
                prestige = PointAwardFactory.getPrestigeNum(pointActionType);
            }

            PointAwardStragy pointAwardStragy = PointAwardFactory.getStragyByPointActionType(pointActionType);
            if (pointAwardStragy == null) {
                return ResultCodeConstants.FAILED.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(reportProfileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }


            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setProfileId(reportProfileId);
            pointActionHistory.setActionType(pointActionType);
            pointActionHistory.setActionDate(new Date());
            pointActionHistory.setCreateDate(new Date());
            pointActionHistory.setPointValue(point);
            pointActionHistory.setPrestige(prestige);
            pointActionHistory.setAppkey(APPKEY);
            pointActionHistory.setHistoryActionType(HistoryActionType.getByCode(pointActionType.getValue()));

            PointResultMsg pointResultMsg = pointAwardStragy.arwardPoint(pointActionHistory);
            if (pointResultMsg.equals(PointResultMsg.SUCCESS)) {
                return ResultCodeConstants.SUCCESS.getJsonString();
            } else if (pointResultMsg.equals(PointResultMsg.OUT_OF_POINT_LIMIT)) {
                return ResultCodeConstants.EXCEEDS_LIMIT.getJsonString();//超过限制次数
            } else {
                return ResultCodeConstants.FAILED.getJsonString();//失败
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " wikiReport ServiceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " wikiReport Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    public Map<String, List<RankDTO>> queryRankList(String type, Pagination pagination) throws ServiceException {
        String weekKey = DateUtil.getLastMonthRankLinKey(type, Calendar.DAY_OF_WEEK);
        String monthKey = DateUtil.getLastMonthRankLinKey(type, Calendar.DAY_OF_MONTH);
        String allKey = DateUtil.getRankLinKey(type, -1);

        Map<String, List<RankDTO>> returnMap = new HashMap<String, List<RankDTO>>();

        Map<String, Integer> weekMap = PointServiceSngl.get().queryRankListByType(weekKey, pagination);

        if (weekMap != null) {
            returnMap.put("week", buildRankDTO(weekMap));
        } else {
            returnMap.put("week", new ArrayList<RankDTO>());
        }
        Map<String, Integer> monthMap = PointServiceSngl.get().queryRankListByType(monthKey, pagination);

        if (monthMap != null) {
            returnMap.put("month", buildRankDTO(monthMap));
        } else {
            returnMap.put("month", new ArrayList<RankDTO>());
        }

        Map<String, Integer> allMap = PointServiceSngl.get().queryRankListByType(allKey, pagination);
        if (allMap != null) {
            returnMap.put("all", buildRankDTO(allMap));
        } else {
            returnMap.put("all", new ArrayList<RankDTO>());
        }

        return returnMap;
    }

    public List<RankDTO> buildRankDTO(Map<String, Integer> map) throws ServiceException {
        List<RankDTO> returnRankList = new ArrayList<RankDTO>();
        Set<String> profileSet = new HashSet<String>();
        profileSet.addAll(map.keySet());

        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileSet);

        for (String pid : map.keySet()) {
            if (profileMap.get(pid) == null) {
                continue;
            }
            RankDTO rankDTO = new RankDTO();
            rankDTO.setNick(profileMap.get(pid).getNick());
            rankDTO.setPic(ImageURLTag.parseUserCenterHeadIcon(profileMap.get(pid).getIcon(), profileMap.get(pid).getSex(), "m", true));
            rankDTO.setPid(pid);
            rankDTO.setValue(map.get(pid));
            returnRankList.add(rankDTO);
        }

        return returnRankList;
    }


    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, List<ActionHistoryDTO>> buildActionHistoryDTO(List<PointActionHistory> list) throws ServiceException {
        Date date = DateUtil.getMonthByFirstOne(new Date());
        List<ActionHistoryDTO> monthActionHistoryList = new ArrayList<ActionHistoryDTO>();
        List<ActionHistoryDTO> historyList = new ArrayList<ActionHistoryDTO>();

        Set<String> appkeys = new HashSet<String>();
        for (PointActionHistory pointActionHistory : list) {
            ActionHistoryDTO actionHistoryDTO = new ActionHistoryDTO();
            String appkeyMsg = null;
            try {
                appkeyMsg = StringUtil.isEmpty(pointActionHistory.getAppkey()) ? "" : i18nSource.getMessage("appkey." + pointActionHistory.getAppkey(), new Object[]{}, Locale.CHINA);
            } catch (NoSuchMessageException e) {
                appkeyMsg = "";
                e.printStackTrace();
            }
            actionHistoryDTO.setAppkey(appkeyMsg);
            actionHistoryDTO.setActionDescription(pointActionHistory.getActionDescription());
            actionHistoryDTO.setActionType(pointActionHistory.getActionType().getName());
            actionHistoryDTO.setCreateTime(sdf.format(pointActionHistory.getCreateDate()));
            actionHistoryDTO.setPointValue(pointActionHistory.getPointValue());
            if (pointActionHistory.getCreateDate().getTime() >= date.getTime()) {
                monthActionHistoryList.add(actionHistoryDTO);
            } else {
                historyList.add(actionHistoryDTO);
            }
            if (!StringUtil.isEmpty(pointActionHistory.getAppkey())) {
                appkeys.add(pointActionHistory.getAppkey());
            }
        }

        Map<String, List<ActionHistoryDTO>> returnMap = new HashMap<String, List<ActionHistoryDTO>>();
        returnMap.put("monthHistoryList", monthActionHistoryList);
        returnMap.put("allHistoryList", historyList);

        return returnMap;
    }


    /**
     * WIKI获取浮动积分（产品要求 暂时只在wiki）
     * 例如上下浮动为10%  下浮系数为-0.1 上浮系数为0.1 原始积分为100的情况下  返回的积分则在90-110之间
     *
     * @param smallNum 下浮系数
     * @param bigNum   上浮系数
     * @param point    原始积分
     * @return 获得的积分
     */
    public static int getPointRandomNum(double smallNum, double bigNum, int point) {
        BigDecimal b = new BigDecimal(Math.random() * (bigNum - smallNum) + smallNum);
        double num = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double pointDouble = (num * point) + point;
        Long a = Math.round(pointDouble);
        return a.intValue();
    }

}
