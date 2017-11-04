package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.point.GoodsSeckillDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.giftmarket.AbstractGiftMarketBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/youku/json/seckill")
public class YoukuJsonGiftmarketController extends AbstractGiftMarketBaseController {

    private static final long CODE_INTERVAL_TIME = 15;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;
//
//    @RequestMapping(value = "/testpage")
//    public ModelAndView page() {
//        return new ModelAndView("/views/jsp/youku/testpage");
//    }

//    @ResponseBody
//    @RequestMapping(value = "/list")
//    public String list(HttpServletRequest request, HttpServletResponse response) {
//        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
//        try {
//            QueryExpress queryExpress = new QueryExpress()
//                    .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, 1))
//                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
//                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
//                    .add(QuerySort.add(ActivityGoodsField.START_TIME, QuerySortOrder.ASC))
//                    .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()))
//                    .add(QueryCriterions.geq(ActivityGoodsField.END_TIME, new Date()));
//
//            List<ActivityGoods> list = PointServiceSngl.get().listActivityGoods(queryExpress);
//            Map<String, List<ActivityDTO>> map = giftMarketWebLogic.buildYoukuGiftmarket(list);
//            if (map != null) {
//                List<ActivityDTO> seckillList = map.get("seckillList");
//                if (CollectionUtil.isEmpty(seckillList)) {
//                    resultMsg.setRs(ResultObjectMsg.CODE_E);
//                    resultMsg.setMsg("seckill.is.null");
//                    return "list([" + jsonBinder.toJson(resultMsg) + "])";
//                }
//
//                ActivityDTO activityDTO = returnSeckill(seckillList);
//                if (activityDTO == null) {
//                    resultMsg.setRs(ResultObjectMsg.CODE_E);
//                    resultMsg.setMsg("seckill.is.null");
//                    return "list([" + jsonBinder.toJson(resultMsg) + "])";
//                }
//
//                Map resultMap = new HashMap();
//                resultMap.put("nowDate", System.currentTimeMillis());
//                resultMap.put("rows", activityDTO);
//                resultMsg.setResult(resultMap);
//                resultMsg.setMsg("success");
//                return "list([" + jsonBinder.toJson(resultMsg) + "])";
//            } else {
//                resultMsg.setRs(ResultObjectMsg.CODE_E);
//                resultMsg.setMsg("seckill.is.null");
//                return "list([" + jsonBinder.toJson(resultMsg) + "])";
//            }
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
//            resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
//            return "list([" + jsonBinder.toJson(resultMsg) + "])";
//        }
//    }

    @ResponseBody
    @RequestMapping(value = "/getseckill")
    public ModelAndView getSeckill(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Date nowDate = new Date();
//            QueryExpress seckillExpress = new QueryExpress();
//            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()));
//            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.SECKILL_SUM, 0));
//            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.END_TIME, nowDate));
//            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode()));
//            seckillExpress.add(QuerySort.add(GoodsSeckillField.START_TIME, QuerySortOrder.ASC));
//            List<GoodsSeckill> goodsSeckillList = PointServiceSngl.get().queryGoodsSeckill(seckillExpress);
            List<GoodsSeckill> goodsSeckillList = PointServiceSngl.get().queryGoodsSeckillByCache(GoodsActionType.YKSC, 0l, null);
            //没秒杀  取第一个兑换
            if (CollectionUtil.isEmpty(goodsSeckillList)) {
                QueryExpress goodsExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, ChooseType.NO.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()))
                        .add(QueryCriterions.gt(ActivityGoodsField.END_TIME, new Date()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
                List<ActivityGoods> goodsList = PointServiceSngl.get().listActivityGoods(goodsExpress);
                if (CollectionUtil.isEmpty(goodsList)) {
                    return new ModelAndView("/views/jsp/youku/ajax-seckill", map);
                }
                map.put("activityGoods", giftMarketWebLogic.buildGoodsActivityDTO(goodsList.get(0)));
            } else {
                //按照开始时间排下序
                Collections.sort(goodsSeckillList, new Comparator<GoodsSeckill>() {
                    @Override
                    public int compare(GoodsSeckill o1, GoodsSeckill o2) {
                        return o1.getStartTime().getTime() > o2.getStartTime().getTime() ? 1 : (o1.getStartTime().getTime() == o2.getStartTime().getTime() ? 0 : -1);
                    }
                });

                List<GoodsSeckill> list = new ArrayList<GoodsSeckill>();
                //正在抢 即将开抢
                List<GoodsSeckill> ingList = new ArrayList<GoodsSeckill>();
                List<GoodsSeckill> toList = new ArrayList<GoodsSeckill>();
                for (GoodsSeckill goodsSeckill : goodsSeckillList) {
                    if (goodsSeckill != null) {
                        //未抢光
                        if (goodsSeckill.getSeckillSum() > 0) {
                            if (goodsSeckill.getStartTime().getTime() <= nowDate.getTime()) {
                                ingList.add(goodsSeckill);
                            } else {
                                toList.add(goodsSeckill);
                            }
                        }
                    }
                }
                //优先取 正在抢 且 最近开始的
                if (!CollectionUtil.isEmpty(ingList)) {
                    int j = ingList.size();
                    for (int i = j; i >= 1; i--) {
                        list.add(ingList.get(i-1));
                    }
                }
                //没有正在抢  取即将开抢的
                if (CollectionUtil.isEmpty(list)) {
                    if (!CollectionUtil.isEmpty(toList)) {
                        int j = (toList.size() > 1) ? 1 : toList.size();
                        for (int i = 0; i < j; i++) {
                            list.add(toList.get(i));
                        }
                    }
                }
                if (!CollectionUtil.isEmpty(list)) {
                    GoodsSeckillDTO goodsSeckillDTO = giftMarketWebLogic.buildGoodsSeckillDTO(list.get(0), null);
                    map.put("goodsSeckill", goodsSeckillDTO);
                    map.put("currentTime", System.currentTimeMillis());
                }else {
                    //全部抢光 取兑换
                    QueryExpress goodsExpress = new QueryExpress()
                            .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, ChooseType.NO.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.YKSC.getCode()))
                            .add(QueryCriterions.gt(ActivityGoodsField.END_TIME, new Date()))
                            .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
                    List<ActivityGoods> goodsList = PointServiceSngl.get().listActivityGoods(goodsExpress);
                    if (CollectionUtil.isEmpty(goodsList)) {
                        return new ModelAndView("/views/jsp/youku/ajax-seckill", map);
                    }
                    map.put("activityGoods", giftMarketWebLogic.buildGoodsActivityDTO(goodsList.get(0)));
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/views/jsp/youku/ajax-seckill", map);
    }

    @ResponseBody
    @RequestMapping(value = "/userislogin")
    public String userisLogin(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        resultMsg.setResult(getUserCenterSeesion(request) == null ? false : true);
        resultMsg.setMsg("success");
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    private ActivityDTO returnSeckill(List<ActivityDTO> seckillList) {
        long nowDate = System.currentTimeMillis();
        List<ActivityDTO> lists = new ArrayList<ActivityDTO>();
        List<ActivityDTO> lists2 = new ArrayList<ActivityDTO>();
        for (ActivityDTO activityDTO : seckillList) {
            //获得当前时间，已经开始的和尚未开始的分开
            if (nowDate > activityDTO.getStartDate().getTime()) {
                //如果剩余数量为0 则需要显示下一个
                if (activityDTO.getSn() < 1) {
                    continue;
                }
                lists.add(activityDTO);
            } else {
                lists2.add(activityDTO);
            }
        }
        if (CollectionUtil.isEmpty(lists) && CollectionUtil.isEmpty(lists2)) {
            return null;
        }
        //如果有多个正在进行中的秒杀 按开始时间降序排列后取第一个
        if (!CollectionUtil.isEmpty(lists)) {
            Collections.sort(lists, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((ActivityDTO) o1).getStartDate().getTime() < ((ActivityDTO) o2).getStartDate().getTime()) {
                        return 1;
                    }
                    return -1;
                }
            });
            return lists.get(0);
        } else {
            //正在进行中的秒杀为空时 取第一个尚未开始的秒杀
            return lists2.get(0);
        }
    }


}