package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.point.GoodsSeckillDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/youku/ajax/userdata")
public class YoukuAjaxUserController extends AbstractYoukuController {


    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @ResponseBody
    @RequestMapping
    public ModelAndView userinfo(HttpServletRequest request, HttpServletResponse response) {

        Map mapMessage = new HashMap();

        String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);
        //=======
        Profile profile = null;

        try {
            mapMessage.put("nowDate", System.currentTimeMillis());
            if (!StringUtil.isEmpty(profileId)) {
                profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                UserPoint userPoint = giftMarketWebLogic.getUserPoint(APPKEY, profile);
                mapMessage.put("userPoint", userPoint);
                mapMessage.put("profile", profile);
            }
            //=======
            Date nowDate = new Date();
//            QueryExpress seckillExpress = new QueryExpress();
//            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()));
////            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.SECKILL_SUM, 0));
//            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.END_TIME, nowDate));
//            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode()));
//            seckillExpress.add(QuerySort.add(GoodsSeckillField.START_TIME, QuerySortOrder.ASC));
//            List<GoodsSeckill> seckillRows = PointServiceSngl.get().queryGoodsSeckill(seckillExpress);
            List<GoodsSeckill> seckillRows = PointServiceSngl.get().queryGoodsSeckillByCache(GoodsActionType.YKSC, 0l, null);
            if (!CollectionUtil.isEmpty(seckillRows)) {
                //按照开始时间排下序
                Collections.sort(seckillRows, new Comparator<GoodsSeckill>() {
                    @Override
                    public int compare(GoodsSeckill o1, GoodsSeckill o2) {
                        return o1.getStartTime().getTime() > o2.getStartTime().getTime() ? 1 : (o1.getStartTime().getTime() == o2.getStartTime().getTime() ? 0 : -1);
                    }
                });

                List<GoodsSeckillDTO> seckillDTOs = new ArrayList<GoodsSeckillDTO>();
                List<GoodsSeckill> list = new ArrayList<GoodsSeckill>();
                Set<Long> goodsIdSet = new HashSet<Long>();
                //正在强的
                List<GoodsSeckill> ingList = new ArrayList<GoodsSeckill>();
                List<GoodsSeckill> toList = new ArrayList<GoodsSeckill>();
                List<GoodsSeckill> overList = new ArrayList<GoodsSeckill>();
                for (GoodsSeckill goodsSeckill : seckillRows) {
                    if (goodsSeckill != null) {
                        if (goodsSeckill.getSeckillSum() > 0) {
                            if (goodsSeckill.getStartTime().getTime() <= nowDate.getTime()) {
                                ingList.add(goodsSeckill);
                            } else {
                                toList.add(goodsSeckill);
                            }
                        } else {
                            overList.add(goodsSeckill);
                        }
                    }
                }
                //正在抢
                if (!CollectionUtil.isEmpty(ingList)) {
                    int j = ingList.size();
                    for (int i = j; i >= 1; i--) {
                        list.add(ingList.get(i - 1));
                        goodsIdSet.add(Long.valueOf(ingList.get(i - 1).getGoodsId()));
                    }
                }
                //即将抢
                if (!CollectionUtil.isEmpty(toList)) {
                    int j = (toList.size() > 2) ? 2 : toList.size();
                    for (int i = 0; i < j; i++) {
                        list.add(toList.get(i));
                        goodsIdSet.add(Long.valueOf(toList.get(i).getGoodsId()));
                    }
                }
                //抢光的
                if (list.size() > 0) {
                    if (!CollectionUtil.isEmpty(overList)) {
                        int j = overList.size();
                        for (int i = 0; i < j; i++) {
                            list.add(overList.get(i));
                            goodsIdSet.add(Long.valueOf(overList.get(i).getGoodsId()));
                        }
                    }
                }
                if (!CollectionUtil.isEmpty(list)) {
                    if (!CollectionUtil.isEmpty(goodsIdSet)) {
                        Map<Long, ActivityGoods> map = PointServiceSngl.get().queryActivityGoodsIdSet(goodsIdSet);
                        if (goodsIdSet.size() == 1) {
                            GoodsSeckill goodsSeckill = list.get(0);
                            GoodsSeckillDTO goodsSeckillDTO = giftMarketWebLogic.buildGoodsSeckillDTO(goodsSeckill, map.get(Long.valueOf(goodsSeckill.getGoodsId())));
                            if (goodsSeckillDTO != null) {
                                seckillDTOs.add(goodsSeckillDTO);
                            }
                        } else {
                            for (GoodsSeckill goodsSeckill : list) {
                                if (goodsSeckill != null) {
                                    if (!goodsIdSet.contains(Long.valueOf(goodsSeckill.getGoodsId()))) {
                                        continue;
                                    }
                                    GoodsSeckillDTO goodsSeckillDTO = giftMarketWebLogic.buildGoodsSeckillDTO(goodsSeckill, map.get(Long.valueOf(goodsSeckill.getGoodsId())));
                                    if (goodsSeckillDTO != null) {
                                        seckillDTOs.add(goodsSeckillDTO);
                                        goodsIdSet.remove(Long.valueOf(goodsSeckill.getGoodsId()));
                                        if (seckillDTOs.size() >= 2) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                mapMessage.put("seckillDTOs", seckillDTOs);
            }

            //=======
            QueryExpress goodsExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, ChooseType.NO.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()))
                    .add(QueryCriterions.gt(ActivityGoodsField.END_TIME, new Date()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            List<ActivityGoods> list = PointServiceSngl.get().listActivityGoods(goodsExpress);
            if (!CollectionUtil.isEmpty(list)) {
                List<ActivityDTO> activityDTOs = giftMarketWebLogic.buildYoukuGiftmarket(list);
                mapMessage.put("activityDTOs", activityDTOs);
            }
            //=======
            String clientId = HTTPUtil.getParam(request, "clientid"); //用于任务系统
            if (profile != null) {
                String groupId = TaskUtil.getTaskGroupId(PREFIX_YOUKU_SIGN_, AppPlatform.IOS);
                List<Task> tasks = EventServiceSngl.get().getTaskGroupList(groupId);
                if (CollectionUtil.isEmpty(tasks)) {
                    mapMessage.put("signcomplete", false);
                    mapMessage.put("signnum", 0);        //总签到次数
                    return new ModelAndView("/views/jsp/youku/ajax-userinfo", mapMessage);
                }
                Task FirstTask = tasks.get(0);
                int taskVerifyId = FirstTask.getTaskVerifyId();
                String userId = getIdBytaskVerifyId(taskVerifyId, profile.getProfileId(), clientId);
                if (StringUtil.isEmpty(userId)) {
                    return new ModelAndView("/views/jsp/youku/ajax-userinfo", putErrorMessage("param.empty"));
                }
                TaskLog taskLog = EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), userId, new Date());
                int signNum = EventServiceSngl.get().querySignSumByProfileIdGroupId(userId, FirstTask.getTaskGroupId());
                mapMessage.put("signcomplete", taskLog != null);
                mapMessage.put("signnum", signNum);        //总签到次数
            }
            //=======
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }

        return new ModelAndView("/views/jsp/youku/ajax-userinfo", mapMessage);
    }


    private List<ActivityDTO> returnSeckill
            (List<ActivityDTO> seckillList) {
        //已经开始的秒杀活动集合
        List<ActivityDTO> startList = new ArrayList<ActivityDTO>();
        //未开始的秒杀活动集合
        List<ActivityDTO> notStartList = new ArrayList<ActivityDTO>();

        //返回
        List<ActivityDTO> returnList = new ArrayList<ActivityDTO>();

        if (CollectionUtil.isEmpty(seckillList)) {
            return null;
        }
        sortActivityDTOListByAsc(seckillList);
        int i = 0;
        long nowDate = System.currentTimeMillis();
        for (ActivityDTO activityDTO : seckillList) {
            if (activityDTO.getStartDate().getTime() < nowDate) {
                if (activityDTO.getSn() < 1) {
                    //已经没有物品的秒杀活动
                    i++;
                }
                startList.add(activityDTO);
            } else {
                notStartList.add(activityDTO);
            }
        }

        if (!CollectionUtil.isEmpty(startList)) {
            //正在秒杀的活动全部被领光的情况  返回的是未开始秒杀的第一个和已经开始秒杀的最后一个
            if (startList.size() == i) {
                if (CollectionUtil.isEmpty(notStartList)) {
                    //如果没有未开始秒杀的商品 已经领光的不用显示
                    return null;
                }

                //未开始的秒杀大于1个的时候返回两个未开始的秒杀
                if (notStartList.size() > 1) {
                    returnList.add(notStartList.get(0));
                    returnList.add(notStartList.get(1));
                } else {
                    ActivityDTO activityDTO = startList.get(startList.size() - 1);
                    returnList.add(notStartList.get(0));
                    returnList.add(activityDTO);
                }
                return returnList;
            } else if ((startList.size() - 1) == i) { // 正在秒杀的活动只有一个没有被领光的情况
                if (!CollectionUtil.isEmpty(notStartList)) {   //未开始的秒杀活动不为空时，返回没被领光的和未开始的第一个秒杀活动
                    for (int j = startList.size() - 1; j >= 0; j--) {
                        if (startList.get(j).getSn() > 0) {
                            returnList.add(startList.get(j));
                            break;
                        }
                    }
                    returnList.add(notStartList.get(0));
                } else { //没有未开始的秒杀活动
                    if (startList.size() == 1) { //只有一个返回值
                        returnList.add(startList.get(startList.size() - 1));
                    } else {//返回最近两个
                        for (int j = startList.size() - 1; j >= 0; j--) {
                            if (startList.get(j).getSn() > 0) {
                                returnList.add(startList.get(j));
                                if (startList.size() == j + 1) {
                                    returnList.add(startList.get(startList.size() - 2));
                                } else {
                                    returnList.add(startList.get(startList.size() - 1));
                                }
                            }
                        }
                        return returnList;
                    }
                }
            } else {//多个活动没被领光 返回最后两个有剩余数量的秒杀活动
                i = 0;
                for (int j = startList.size() - 1; j >= 0; j--) {
                    if (startList.get(j).getSn() > 0) {
                        returnList.add(startList.get(j));
                        i++;
                        if (i >= 2) {
                            break;
                        }
                    }
                }
                return returnList;
            }
        } else {
            if (notStartList.size() == 1) {
                returnList.add(notStartList.get(0));
            } else {
                returnList.add(notStartList.get(0));
                returnList.add(notStartList.get(1));
            }
        }
//        sortActivityDTOListByAsc(returnList);
        return returnList;
    }

    private void sortActivityDTOListByAsc(List<ActivityDTO> seckillList) {
        if (CollectionUtil.isEmpty(seckillList)) {
            return;
        }
        Collections.sort(seckillList, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((ActivityDTO) o1).getStartDate().getTime() > ((ActivityDTO) o2).getStartDate().getTime()) {
                    return 1;
                }
                return -1;
            }
        });
    }


}