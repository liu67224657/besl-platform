package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ActivityCalEventTimeEvent;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.giftmarket.ConsumeRankDTO;
import com.enjoyf.webapps.joyme.dto.giftmarket.CreditDetailDTO;
import com.enjoyf.webapps.joyme.dto.giftmarket.PointRankDTO;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//import com.enjoyf.platform.service.profile.Profile;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGiftMarketBaseController extends BaseRestSpringController {

    public static final int hotActivityPageSize = 20;
    public static final int newActivityPageSize = 10;
    public final String PC_EXCLUSIVE = "pc_exclusive";


    //积分兑换区列表  每页记录条数
    public static final int coinListPageSize = 15;
    //我的礼包 每页 条数
    public static final int myGiftPageSize = 20;
    public static final int myMGiftPageSize = 10;
    //礼包中心 商品数
    public static final int giftMarketGoodsSize = 9;
    //礼包中心 激活码 数
    public static final int giftMarketExchangeSize = 8;

    public static final int giftMarketPointEnoughExchangeSize = 8;
    // 根据查出的礼包数量设置  你可能感兴趣的礼包查询数量
    public static final int giftMarketYouFavoriteSize1 = 8;

    public static final int giftMarketYouFavoriteSize2 = 4;

    //礼包列表页显示多少条数据
    public static int giftLine = 25;
    //详情页面 感兴趣礼包次数
    public static final int giftInterest = 4;
    //固定页数
    public static final int pageNo = 1;
    //积分兑换详情页  你可能喜欢的其他礼包 个数
    public static final int recommendGoodsSize = 3;
    //积分排行榜 前5
    private static final int pointRankPageSize = 5;
    //积分消费榜 前5
    private static final int consumeRankPageSize = 5;
    //当前第一页
    private static final int currentPage = 1;
    //积分明细 每页 20 条记录
    private static final int creditDetailPageSize = 20;

    public static final int recommendActivitySize = 5;


    public static final int WAP_PAGE_SIZE = 5;
    public static final int WAP_MORE_PAGE_SIZE = 12;

    public static final long CODE_INTERVAL_TIME = 15l;

    /**
     * 今日已赚积分
     *
     * @param userCenterSession
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public int getUserDayPoint(UserCenterSession userCenterSession) throws ServiceException {
        //todo 逻辑有问题为什么要有一个DISCUZ_SYNC?为什么没有point_key？直接查DB可以考虑用memcached
        if (userCenterSession != null) {
            //todo 1profileid 2pointkey(老数据在www) 3actiontype 3memcached
            List<UserDayPoint> dayPointList = PointServiceSngl.get().queryUserDayPoint(new QueryExpress()
                    .add(QueryCriterions.eq(UserDayPointField.USERNO, userCenterSession.getUno()))
                    .add(QueryCriterions.eq(UserDayPointField.POINTACTIONTYPE, PointActionType.DISCUZ_SYNC.getCode()))
                    .add(QueryCriterions.eq(UserDayPointField.POINTDATE, new Date()))
                    .add(QueryCriterions.gt(UserDayPointField.POINTVALUE, 0)), new Date());
            int dayPointAmount = 0;
            if (!CollectionUtil.isEmpty(dayPointList)) {
                for (UserDayPoint userDayPoint : dayPointList) {
                    dayPointAmount += userDayPoint.getPointValue();
                }
            }
            return dayPointAmount;
        }
        return 0;
    }

    /**
     * 事件
     *
     * @param activityId
     */
    public void sendOutEvent(long activityId) {
        ActivityCalEventTimeEvent cetEvent = new ActivityCalEventTimeEvent();
        cetEvent.setActivityId(activityId);
        try {
            EventDispatchServiceSngl.get().dispatch(cetEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
    }

    /**
     * 积分消费排行榜 todo 无用的方法尽量删掉
     *
     * @return
     * @throws ServiceException
     */
    @Deprecated
    public List<ConsumeRankDTO> queryConsumeRank() throws ServiceException {
        List<ConsumeRankDTO> dtoList = new LinkedList<ConsumeRankDTO>();
//        PageRows<UserPoint> userPointPageRows = PointServiceSngl.get().queryUserPointByPage(new QueryExpress()
//                        .add(QuerySort.add(UserPointField.CONSUME_AMOUNT, QuerySortOrder.DESC)),
//                new Pagination(consumeRankPageSize, currentPage, consumeRankPageSize));
//
//        Set<String> profileIds = new LinkedHashSet<String>();
//        if (userPointPageRows != null) {
//            for (UserPoint userPoint : userPointPageRows.getRows()) {
//                profileIds.add(userPoint.getProfileId());
//            }
//
//            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
//            for (UserPoint userPoint : userPointPageRows.getRows()) {
//                Profile profile = profileMap.get(userPoint.getProfileId());
//                if (profile == null) {
//                    continue;
//                }
//
//                ConsumeRankDTO dto = new ConsumeRankDTO();
//                dto.setIcon(profile.getIcon());
//                dto.setDomain(profile.getDomain());
//                dto.setScreenName(profile.getNick());
//                dto.setPoint(userPoint.getConsumeAmount());
//                dto.setSex(profile.getSex());
//                dtoList.add(dto);
//            }
//        }
        return dtoList;
    }

    /**
     * 大家正在领
     *
     * @return
     * @throws ServiceException
     */
    public List<UserRecentLogEntry> queryUserRecentLog() throws ServiceException {
        List<UserRecentLogEntry> returnList = new LinkedList<UserRecentLogEntry>();
        List<UserRecentLogEntry> list = PointServiceSngl.get().queryUserRecentLog();
        if (!CollectionUtil.isEmpty(list)) {
            for (UserRecentLogEntry entry : list) {
                String userName = entry.getScreenName().substring(0, 1) + "****";
                entry.setScreenName(userName);
                returnList.add(entry);
            }
        }
        return returnList;
    }

    /**
     * 土豪积分榜
     *
     * @return
     * @throws ServiceException
     */
    public List<PointRankDTO> queryUserPointRank() throws ServiceException {
        List<PointRankDTO> dtoList = new LinkedList<PointRankDTO>();
        PageRows<UserPoint> userPointPageRows = PointServiceSngl.get().queryUserPointByPage(new QueryExpress()
                        .add(QuerySort.add(UserPointField.USERPOINT, QuerySortOrder.DESC)),
                new Pagination(pointRankPageSize, currentPage, pointRankPageSize));


        if (userPointPageRows != null && !CollectionUtil.isEmpty(userPointPageRows.getRows())) {
            Set<String> profileIds = new LinkedHashSet<String>();
            for (UserPoint userPoint : userPointPageRows.getRows()) {
                profileIds.add(userPoint.getProfileId());
            }

            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
            for (UserPoint userPoint : userPointPageRows.getRows()) {
                Profile profile = profileMap.get(userPoint.getUserNo());
                PointRankDTO dto = new PointRankDTO();
                dto.setScreenName(profile.getNick());
                dto.setPoint(userPoint.getUserPoint());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    /**
     * 用户积分历史明细
     *
     * @param userCenterSession
     * @param page
     * @return
     * @throws ServiceException
     */
    public PageRows<CreditDetailDTO> queryCreditDetail(UserCenterSession userCenterSession, Integer page) throws ServiceException {
        page = page == null ? 1 : page;
        Pagination pagination = new Pagination(page * creditDetailPageSize, page, creditDetailPageSize);

        PageRows<PointActionHistory> pointActionHistoryPageRows = PointServiceSngl.get().queryPointActionHistoryByPage(new QueryExpress()
                .add(QueryCriterions.eq(PointActionHistoryField.PROFILEID, userCenterSession.getProfileId()))
                .add(QueryCriterions.eq(PointActionHistoryField.ACTIONTYPE, PointActionType.DISCUZ_SYNC.getCode()))
                .add(QuerySort.add(PointActionHistoryField.CREATEDATE, QuerySortOrder.DESC)), pagination);
        if (pointActionHistoryPageRows == null || CollectionUtil.isEmpty(pointActionHistoryPageRows.getRows())) {
            return null;
        }
        List<CreditDetailDTO> dtoList = new LinkedList<CreditDetailDTO>();
        for (PointActionHistory history : pointActionHistoryPageRows.getRows()) {
            CreditDetailDTO dto = new CreditDetailDTO();
            dto.setScreenName(userCenterSession.getNick());
            dto.setDescription(history.getActionDescription());
            dto.setDate(history.getActionDate());
            dto.setPoint(history.getPointValue());
            dtoList.add(dto);
        }
        PageRows<CreditDetailDTO> returnPageRows = new PageRows<CreditDetailDTO>();
        returnPageRows.setPage(pointActionHistoryPageRows.getPage());
        returnPageRows.setRows(dtoList);
        return returnPageRows;
    }


    protected int getUserPointAmount(UserCenterSession userCenterSession) throws ServiceException {

        // todo 里面的pointkey不要写死
        if (userCenterSession != null) {
//            if(userCenterSession.getPointAmount()<0){
            UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, userCenterSession.getProfileId()))
                    .add(QueryCriterions.eq(UserPointField.POINTKEY, "www")));
            if (userPoint != null) {
                userCenterSession.setPointAmount(userPoint.getUserPoint());
            } else {
                userCenterSession.setPointAmount(0);
            }

//            }
            return userCenterSession.getPointAmount();
        }

        return 0;
    }

    public String getTimeInCookie(HttpServletRequest request) {
        if (CookieUtil.getCookie(request, Constant.SESSION_SEND_INTRAV) == null) {
            return null;
        }

        return CookieUtil.getCookie(request, Constant.SESSION_SEND_INTRAV).getValue();
    }

    public void saveTimeInCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, Constant.SESSION_SEND_INTRAV, String.valueOf(System.currentTimeMillis()), 60 * 1000);
    }

    public void deleteTimeInCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, Constant.SESSION_SEND_INTRAV, String.valueOf(System.currentTimeMillis()), 0);
    }


}
