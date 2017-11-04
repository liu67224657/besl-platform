package com.enjoyf.webapps.joyme.webpage.controller.giftmarket;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.gameres.gamedb.GameCategoryType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopMenu;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.MobileVerifyWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-9-21
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gift")
public class GiftController extends AbstractGiftMarketBaseController {
    TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "mobileVerifyWebLogic")
    private MobileVerifyWebLogic mobileVerifyWebLogic;

    private static final String GET = "get";
    private final String APPKEY = "default";

    private static Map<String, String> letterMap = new LinkedHashMap<String, String>();

    static {
        letterMap.put("a", "ABC");
        letterMap.put("b", "ABC");
        letterMap.put("c", "ABC");

        letterMap.put("d", "DEF");
        letterMap.put("e", "DEF");
        letterMap.put("f", "DEF");

        letterMap.put("g", "GHI");
        letterMap.put("h", "GHI");
        letterMap.put("i", "GHI");

        letterMap.put("j", "JKL");
        letterMap.put("k", "JKL");
        letterMap.put("l", "JKL");

        letterMap.put("m", "MNO");
        letterMap.put("n", "MNO");
        letterMap.put("o", "MNO");


        letterMap.put("p", "PQR");
        letterMap.put("q", "PQR");
        letterMap.put("r", "PQR");

        letterMap.put("s", "STU");
        letterMap.put("t", "STU");
        letterMap.put("u", "STU");

        letterMap.put("v", "VW");
        letterMap.put("w", "VW");

        letterMap.put("x", "XYZ");
        letterMap.put("y", "XYZ");
        letterMap.put("z", "XYZ");
    }

    @RequestMapping
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "p", required = false) String platform) {
        try {
            String reqUrl = request.getRequestURL().toString();
            URL url = new URL(reqUrl);
            if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
                //手机访问电脑版
                return pcGift(request, response, platform);
            } else if (url.getHost().contains("m.joyme.")) {
                //m域名
                return wapGift(request, response, platform);
            } else {
                return pcGift(request, response, platform);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
    }

    private ModelAndView wapGift(HttpServletRequest request, HttpServletResponse response, String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //轮播图
            List<JoymeAppTopMenu> joymeAppMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByAppKey(APPKEY);
            List<JoymeAppTopMenu> returnAppMenuList = new ArrayList<JoymeAppTopMenu>();
            for (JoymeAppTopMenu joymeAppTopMenu : joymeAppMenuList) {
                joymeAppTopMenu.setPicUrl1(URLUtils.getJoymeDnUrl(joymeAppTopMenu.getPicUrl1()));
                returnAppMenuList.add(joymeAppTopMenu);
            }
            mapMessage.put("menupic", returnAppMenuList);
            //独家礼包
            PageRows<ClientLineItem> exclusivePageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(PC_EXCLUSIVE, "", new Pagination(hotActivityPageSize, pageNo, hotActivityPageSize));
            if (exclusivePageRows != null && !CollectionUtil.isEmpty(exclusivePageRows.getRows())) {
                Set<Long> exclusiveLong = new LinkedHashSet<Long>();
                for (ClientLineItem clientLineItem : exclusivePageRows.getRows()) {
                    exclusiveLong.add(Long.parseLong(clientLineItem.getDirectId()));
                }

                QueryExpress exclusiveQueryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, exclusiveLong.toArray()));
                List<ActivityGoods> exclusiveActivityList = PointServiceSngl.get().listActivityGoods(exclusiveQueryExpress);
                List returnExclusiveList = new ArrayList<ActivityGoods>();
                for (Long id : exclusiveLong) {
                    for (ActivityGoods activityGoods : exclusiveActivityList) {
                        if (id == activityGoods.getActivityGoodsId()) {
                            activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
                            returnExclusiveList.add(activityGoods);
                        }
                    }
                }
                mapMessage.put("exclusiveList", returnExclusiveList);
            }
            Pagination page = new Pagination(WAP_PAGE_SIZE, 1, WAP_PAGE_SIZE);
            PageRows<ActivityGoods> pageRows = PointServiceSngl.get().queryActivityGoodsAllListByCache(ActivityType.EXCHANGE_GOODS, page);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<ActivityGoods> list = new ArrayList<ActivityGoods>();
                for (ActivityGoods gift : pageRows.getRows()) {
                    gift.setActivityPicUrl(URLUtils.getJoymeDnUrl(gift.getActivityPicUrl()));
                    list.add(gift);
                }
                mapMessage.put("list", list);
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/m/gift-m", mapMessage);
    }

    private ModelAndView pcGift(HttpServletRequest request, HttpServletResponse response, String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            mapMessage.put("userSession", userSession);
            //轮播图
            List<JoymeAppTopMenu> joymeAppMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByAppKey(APPKEY);
            List<JoymeAppTopMenu> returnAppMenuList = new ArrayList<JoymeAppTopMenu>();
            for (JoymeAppTopMenu joymeAppTopMenu : joymeAppMenuList) {
                joymeAppTopMenu.setPicUrl1(URLUtils.getJoymeDnUrl(joymeAppTopMenu.getPicUrl1()));
                returnAppMenuList.add(joymeAppTopMenu);
            }
            mapMessage.put("menupic", returnAppMenuList);

            //独家礼包
            PageRows<ClientLineItem> exclusivePageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(PC_EXCLUSIVE, "", new Pagination(hotActivityPageSize, pageNo, hotActivityPageSize));
            if (exclusivePageRows != null && !CollectionUtil.isEmpty(exclusivePageRows.getRows())) {
                Set<Long> exclusiveLong = new LinkedHashSet<Long>();
                for (ClientLineItem clientLineItem : exclusivePageRows.getRows()) {
                    exclusiveLong.add(Long.parseLong(clientLineItem.getDirectId()));
                }

                //todo 是否有性能更高的方法？缓存？用goodsid做缓存每次用goodsid从换从中取？
                QueryExpress exclusiveQueryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, exclusiveLong.toArray()));
                List<ActivityGoods> exclusiveActivityList = PointServiceSngl.get().listActivityGoods(exclusiveQueryExpress);
                List returnExclusiveList = new ArrayList<ActivityGoods>();
                for (Long id : exclusiveLong) {
                    for (ActivityGoods activityGoods : exclusiveActivityList) {
                        if (id == activityGoods.getActivityGoodsId()) {
                            activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
                            returnExclusiveList.add(activityGoods);
                        }
                    }
                }
                mapMessage.put("exclusiveList", returnExclusiveList);
            }

            //热门礼包
            //todo 同上 是否在上架一个礼包的时候为缓存里存多份呢？ IOS-hot--->list anrdoird--->list
            QueryExpress hotGiftQueryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, ChooseType.YES.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (!StringUtil.isEmpty(platform)) {
                hotGiftQueryExpress.add(QueryCriterions.in(ActivityGoodsField.PLATFORM, new Object[]{Integer.parseInt(platform), AppPlatform.ALL.getCode()}));
                mapMessage.put("platform", platform);

            }
            PageRows<ActivityDTO> hotActivityPageRows = giftMarketWebLogic.queryActivityDTOs(hotGiftQueryExpress,
                    new Pagination(hotActivityPageSize, pageNo, hotActivityPageSize));
            if (hotActivityPageRows != null && !CollectionUtil.isEmpty(hotActivityPageRows.getRows())) {
                mapMessage.put("hotRows", hotActivityPageRows.getRows());
            }
            //最新礼包
            QueryExpress newGiftQueryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, ChooseType.NO.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ActivityDTO> newActivityPageRows = giftMarketWebLogic.queryActivityDTOs(newGiftQueryExpress,
                    new Pagination(newActivityPageSize, pageNo, newActivityPageSize));

            if (newActivityPageRows != null && !CollectionUtil.isEmpty(newActivityPageRows.getRows())) {
                mapMessage.put("newRows", newActivityPageRows.getRows());
            }
            //礼包中心和快来淘号模块//缓存
            Map<String, List<ActivityGoods>> activityGoodsMap = PointServiceSngl.get().queryGiftmarketIndexList();

            //todo 缓存？
            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));

            List<ActivityHotRanks> returnRanksList = new ArrayList<ActivityHotRanks>();
            for (ActivityHotRanks activityHotRanks : ranksList) {
                activityHotRanks.setPic(URLUtils.getJoymeDnUrl(activityHotRanks.getPic()));
                returnRanksList.add(activityHotRanks);
            }

            mapMessage.put("giftlist", returnRanksList);
            mapMessage.put("taolist", activityGoodsMap.get("taoindexlist"));

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarket", mapMessage);
    }


    //更多礼包列表  最多查询20行
    @RequestMapping(value = "/more")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "firstletter", required = false) String firstLetter,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "pattern", required = false) String pattern,
                             @RequestParam(value = "exclusive", required = false) String exclusive,
                             @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo) {
        try {
            String reqUrl = request.getRequestURL().toString();
            URL url = new URL(reqUrl);
            if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
                //手机访问电脑版
                return pcGiftMore(request, response, firstLetter, platform, pattern, exclusive, pageNo);
            } else if (url.getHost().contains("m.joyme.")) {
                //m域名
                return wapGiftMore(request, response, firstLetter, platform, pattern, exclusive, pageNo);
            } else {
                return pcGiftMore(request, response, firstLetter, platform, pattern, exclusive, pageNo);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
    }

    private ModelAndView wapGiftMore(HttpServletRequest request, HttpServletResponse response, String firstLetter, String platform, String pattern, String exclusive, Integer pageNo) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        pageNo = pageNo == null ? 1 : pageNo;
        Pagination pagination = new Pagination(pageNo * WAP_MORE_PAGE_SIZE, pageNo, WAP_MORE_PAGE_SIZE);
        try {
            String refer = request.getHeader("referer");
            String url = request.getRequestURL().toString();
            if (StringUtil.isEmpty(refer) || !refer.contains(request.getServerName()) || refer.equals(url)) {
                //为空 或者 被钓鱼，返回到礼包首页
                refer = WebappConfig.get().getUrlM() + "/gift";
            }
            mapMessage.put("refer", refer);
            Set<String> letterSet = new LinkedHashSet<String>();
            letterSet.addAll(letterMap.values());
            mapMessage.put("letterGroupList", letterSet);
            firstLetter = (StringUtil.isEmpty(firstLetter) ? "ABC" : firstLetter);
            mapMessage.put("firstLetter", firstLetter);
            PageRows<ActivityGoods> activityGoodsList = PointServiceSngl.get().queryActivityGoodsByLetter(ActivityType.EXCHANGE_GOODS, firstLetter, null);
            if (activityGoodsList != null && !CollectionUtil.isEmpty(activityGoodsList.getRows())) {
                List<ActivityGoods> list = new ArrayList<ActivityGoods>();
                for (ActivityGoods gift : activityGoodsList.getRows()) {
                    gift.setActivityPicUrl(URLUtils.getJoymeDnUrl(gift.getActivityPicUrl()));
                    list.add(gift);
                }
                mapMessage.put("list", list);
                mapMessage.put("page", activityGoodsList.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/m/gift-more-m", mapMessage);
    }

    private ModelAndView pcGiftMore(HttpServletRequest request, HttpServletResponse response, String firstLetter, String platform, String pattern, String exclusive, Integer pageNo) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            mapMessage.put("userSession", userSession);
            //右侧快来淘号
            Map<String, List<ActivityGoods>> activityGoodsMap = PointServiceSngl.get().queryGiftmarketIndexList();

            //todo 礼包总数 礼包总数也存到一个key。每次count*
            int giftSum = PointServiceSngl.get().queryActivityGoodsCount(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
            //兑换排行榜 todo 是否可以用缓存serv端来计算排行榜。前段直接从缓存中取
            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));

            List<ActivityHotRanks> returnRanksList = new ArrayList<ActivityHotRanks>();
            for (ActivityHotRanks activityHotRanks : ranksList) {
                activityHotRanks.setPic(URLUtils.getJoymeDnUrl(activityHotRanks.getPic()));
                returnRanksList.add(activityHotRanks);
            }
            mapMessage.put("giftlist", returnRanksList);
            mapMessage.put("taolist", activityGoodsMap.get("taoindexlist"));
            mapMessage.put("firstLetter", firstLetter);
            mapMessage.put("platform", platform);
            mapMessage.put("pattern", pattern);
            mapMessage.put("exclusive", exclusive);
            mapMessage.put("letter", letterMap.keySet());
            mapMessage.put("giftSum", giftSum);
            Map<Integer, String> categoryMap = new HashMap<Integer, String>();
            for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                categoryMap.put(categoryType.getCode(), categoryType.getValue());
            }
            mapMessage.put("categoryMap", categoryMap);

            List<ActivityGoods> returnLetterList = new ArrayList<ActivityGoods>();//返回的LIST
            //按首字母查询，走缓存
            if (!StringUtil.isEmpty(firstLetter)) {
                PageRows<ActivityGoods> pageRows = PointServiceSngl.get().queryActivityGoodsByLetter(ActivityType.EXCHANGE_GOODS, firstLetter, null);
                List<ActivityGoods> activityGoodsList = (pageRows == null ? null : pageRows.getRows());
                List<ActivityGoods> tempList = new ArrayList<ActivityGoods>(); //临时LIST 做数据临时存储 转换用

                if (!CollectionUtil.isEmpty(activityGoodsList)) {
                    returnLetterList.addAll(activityGoodsList);
                    if (!StringUtil.isEmpty(platform)) {
                        returnLetterList.clear(); //todo???
                        for (ActivityGoods activityGoods : activityGoodsList) {
                            if (activityGoods.getPlatform().equals(AppPlatform.ALL) || activityGoods.getPlatform().equals(AppPlatform.getByCode(Integer.parseInt(platform)))) {
                                activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
                                returnLetterList.add(activityGoods);
                            }
                        }
                    }
                    if (!StringUtil.isEmpty(pattern)) {
                        for (ActivityGoods activityGoods : returnLetterList) {
                            activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));

                            if (pattern.equals(GET)) {
                                if (activityGoods.getGoodsResetAmount() > 0) {
                                    tempList.add(activityGoods);
                                }
                            } else {
                                if (activityGoods.getGoodsResetAmount() < 1) {
                                    tempList.add(activityGoods);
                                }
                            }
                        }
                        returnLetterList.clear();
                        returnLetterList.addAll(tempList);
                    }
                    if (!StringUtil.isEmpty(exclusive)) {
                        tempList.clear();
                        if (exclusive.equals("1")) {
                            List<ClientLineItem> exclusiveList = JoymeAppServiceSngl.get().queryClientLineItemListForHotPage(PC_EXCLUSIVE);
                            for (ActivityGoods activityGoods : returnLetterList) {
                                for (ClientLineItem clientLineItem : exclusiveList) {
                                    if (activityGoods.getActivityGoodsId() == Long.parseLong(clientLineItem.getDirectId())) {
                                        tempList.add(activityGoods);
                                    }
                                }
                            }
                            returnLetterList.clear();
                            returnLetterList.addAll(tempList);
                        }
                    }
                }
            } else {
                Pagination pagination = new Pagination(pageNo * giftLine, pageNo, giftLine);
                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
                if (!StringUtil.isEmpty(exclusive)) {
                    if (exclusive.equals("1")) {
                        Set<Long> setLongs = new HashSet<Long>();
                        List<ClientLineItem> exclusiveList = JoymeAppServiceSngl.get().queryClientLineItemListForHotPage(PC_EXCLUSIVE);
                        for (ClientLineItem clientLineItem : exclusiveList) {
                            setLongs.add(Long.parseLong(clientLineItem.getDirectId()));
                        }
                        if (!CollectionUtil.isEmpty(setLongs)) {
                            queryExpress.add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, setLongs.toArray()));
                        } else {
                            return new ModelAndView("views/jsp/giftmarket/giftindex", mapMessage);
                        }
                    }
                }
                if (!StringUtil.isEmpty(platform)) {
                    queryExpress.add(QueryCriterions.in(ActivityGoodsField.PLATFORM, new Object[]{Integer.parseInt(platform), AppPlatform.ALL.getCode()}));
                }
                if (!StringUtil.isEmpty(pattern)) {
                    if (pattern.equals(GET)) {
                        queryExpress.add(QueryCriterions.gt(ActivityGoodsField.GOODS_RESETAMOUNT, 0));
                    } else {
                        queryExpress.add(QueryCriterions.leq(ActivityGoodsField.GOODS_RESETAMOUNT, 0));
                    }
                }
                PageRows<ActivityGoods> pageRows = PointServiceSngl.get().queryActivityGoodsByPage(queryExpress, pagination);
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    returnLetterList.addAll(pageRows.getRows());
                    mapMessage.put("page", pageRows.getPage());
                }
            }
            mapMessage.put("letterList", returnLetterList);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("views/jsp/giftmarket/giftindex", mapMessage);
    }

    //礼包的详情页信息
    @RequestMapping(value = "/{activityId}")
    public ModelAndView gitfDetail(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable(value = "activityId") String aid,
                                   @RequestParam(value = "reurl", required = false) String reurl,
                                   @RequestParam(value = "logid", required = false) String logId) {
        try {
            String reqUrl = request.getRequestURL().toString();
            URL url = new URL(reqUrl);
            if (request.getQueryString() != null && request.getQueryString().contains("tab_device=wap_pc")) {
                //手机访问电脑版
                return pcGiftDetail(request, response, aid);
            } else if (url.getHost().contains("m.joyme.")) {
                //m域名
                return wapGiftDetail(request, response, aid, logId, reurl);
            } else if (url.getHost().contains("www.joyme.") && AppUtil.checkMobile(request)) {
                //手机 访问 www 跳 m
                String redirectUrl = request.getScheme() + "://" + url.getHost().replace("www.joyme", "m.joyme") + url.getPath() + (StringUtil.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString());
                response.sendRedirect(redirectUrl);
            } else {
                return pcGiftDetail(request, response, aid);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return null;
    }

    private ModelAndView wapGiftDetail(HttpServletRequest request, HttpServletResponse response, String aid, String logId, String reurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            //返回
            mapMessage.put("reurl", reurl);

            UserCenterSession userSession = getUserCenterSeesion(request);
            mapMessage.put("userSession", userSession);
            if (StringUtil.isEmpty(aid)) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("activity.gift.is.null"));
            }
            Long id = null;
            try {
                id = Long.parseLong(aid);
            } catch (NumberFormatException e) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("activity.gift.is.null"));
            }

            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(id);
            if (activityDetailDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("activity.gift.is.null"));
            }
            mapMessage.put("detailDTO", activityDetailDTO);
            if (!StringUtil.isEmpty(logId) && userSession != null) {
                try {
                    long lid = Long.parseLong(logId);
                    UserExchangeLog exchangeLog = PointServiceSngl.get().getUserExchangeLog(userSession.getProfileId(), activityDetailDTO.getAid(), lid);
                    if (exchangeLog != null) {
                        mapMessage.put("exchangeLog", exchangeLog);
                    }
                } catch (NumberFormatException e) {
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/m/gift-detail-m", mapMessage);
    }

    private ModelAndView pcGiftDetail(HttpServletRequest request, HttpServletResponse response, String aid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            mapMessage.put("userSession", userSession);
            if (StringUtil.isEmpty(aid)) {
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }

            Long id = null;
            try {
                id = Long.parseLong(aid);
            } catch (NumberFormatException e) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("activity.gift.is.null"));
            }

            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(id);
            if (activityDetailDTO == null) {
                return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("activity.gift.is.null"));
            }

            activityDetailDTO.setGameIcon(URLUtils.getJoymeDnUrl(activityDetailDTO.getGameIcon()));
            mapMessage.put("detailDTO", activityDetailDTO);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(newActivityPageSize, pageNo, newActivityPageSize));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
            }
            Map<String, List<ActivityGoods>> activityGoodsMap = PointServiceSngl.get().queryGiftmarketIndexList();
            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));
            List<ActivityHotRanks> returnRanksList = new ArrayList<ActivityHotRanks>();
            for (ActivityHotRanks activityHotRanks : ranksList) {
                activityHotRanks.setPic(URLUtils.getJoymeDnUrl(activityHotRanks.getPic()));
                returnRanksList.add(activityHotRanks);
            }
            mapMessage.put("giftlist", returnRanksList);
            mapMessage.put("taolist", activityGoodsMap.get("taoindexlist"));

            List<ExchangeGoodsItem> userExchangeLogs = PointServiceSngl.get().taoExchangeGoodsItemByGoodsId("", "", "", Long.parseLong(aid), new Date(), "");
            if (!CollectionUtil.isEmpty(userExchangeLogs) && userExchangeLogs.size() >= 10) {
                mapMessage.put("taobtn", "1");
            } else {
                mapMessage.put("taobtn", "0");
            }
            boolean bool = false;
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityDetailDTO.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                bool = true;
            }

            mapMessage.put("isBool", bool);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("views/jsp/giftmarket/giftdetail", mapMessage);
    }

    @RequestMapping(value = "/bindphonepage")
    public ModelAndView bindPhonePage(@RequestParam(value = "aid", required = false) String aid,
                                      @RequestParam(value = "reurl", required = false) String reurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("aid", aid);
        mapMessage.put("reurl", reurl);
        return new ModelAndView("/views/jsp/giftmarket/m/bind_phone_page", mapMessage);
    }

    /**
     * 给手机发送验证码
     *
     * @param request
     * @param response
     * @param phone
     * @param aid
     * @return
     */
    @RequestMapping(value = "/bindphone")
    public ModelAndView bindPhone(HttpServletRequest request,
                                  HttpServletResponse response, @RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "aid", required = false) String aid,
                                  @RequestParam(value = "reurl", required = false) String reurl) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("phone", phone);
            mapMessage.put("aid", aid);
            mapMessage.put("reurl", reurl);
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/loginpage?reurl=" + (StringUtil.isEmpty(reurl) ? "" : reurl));
            }
            String lastObj = getTimeInCookie(request);
            //String vTemplate = templateConfig.getVerifyMobileSmsTemplate();
            if (!StringUtil.isEmpty(lastObj)) {
                long now = System.currentTimeMillis();
                long last = Long.parseLong(lastObj);

                long intravel = 30 - ((now - last) / 1000);
                if (intravel >= 0) {
                    mapMessage.put("intravel", intravel);
                } else {
                    deleteTimeInCookie(request, response);
                    //todo 微服务改版
                    //MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
                    boolean resutl = UserCenterServiceSngl.get().sendMobileNo(phone);
                    //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
                    if(resutl){
                        saveTimeInCookie(request, response);
                        mapMessage.put("intravel", "30");
                        //把生成code存入session
//                        request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                        UserCenterServiceSngl.get().saveMobileCode(userSession.getUno(), dto.getCode());
                    }
                }
                return new ModelAndView("/views/jsp/giftmarket/m/bind_phone_verify", mapMessage);
            }
            //MobileCodeDTO dto = mobileVerifyWebLogic.generatorCode(userSession.getUno(), phone, vTemplate, MobileVerifyWebLogic.VERIFYSMS_TIMES);
            boolean resutl = UserCenterServiceSngl.get().sendMobileNo(phone);
            //if (dto.getRs() == MobileCodeDTO.RS_SUCCESS) {
            if(resutl){
                saveTimeInCookie(request, response);
                mapMessage.put("intravel", "30");
                //把生成code存入session
//                request.getSession().setAttribute(Constant.SESSION_MOBILE_CODE, dto.getCode());
//                UserCenterServiceSngl.get().saveMobileCode(userSession.getUno(), dto.getCode());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/m/bind_phone_verify", mapMessage);
    }

    /**
     * 目前只有 m站用到
     *
     * @param request
     * @param response
     * @param activityGoodsId
     * @return
     */
    @RequestMapping(value = "/getcode")
    public ModelAndView getCode(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "aid", required = false) String activityGoodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(activityGoodsId)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            Long aid = null;
            try {
                aid = Long.parseLong(activityGoodsId);
            } catch (NumberFormatException e) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            //返回
            String refer = request.getHeader("referer");
            String url = request.getRequestURL().toString();
            if (StringUtil.isEmpty(refer) || !refer.contains(request.getServerName()) || refer.equals(url)) {
                //为空 或者 被钓鱼，返回到礼包首页
                refer = WebappConfig.get().getUrlM() + "/gift";
            }
            mapMessage.put("refer", refer);

            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/loginpage?reurl=" + refer);
            }
            if (StringUtil.isEmpty(userSession.getMobile())) {
                return new ModelAndView("redirect:/gift/bindphonepage?reurl=" + refer);
            }

            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(aid);
            if (activityGoods == null) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            mapMessage.put("gift", activityGoods);

            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowGetCode(activityGoods, userSession.getProfileId());
            if (allowExchangeStatus.equals(AllowExchangeStatus.NO_ALLOW)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("message.user.syserror"));
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.getcode.one"));
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_DAY)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.getcode.by.day"));
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.getcode.by.intaval"));
            }
            Long newDate = System.currentTimeMillis();
            Long lastDate = userSession.getGetCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= CODE_INTERVAL_TIME) {
                    return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.get.code.interval"));
                }
            }

            if (MobileExclusive.WEIXIN.equals(activityGoods.getChannelType())) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("is.weixin.exclusive"));
            } else if (MobileExclusive.NEWSCLIENT.equals(activityGoods.getChannelType())) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("is.newsclient.exclusive"));
            }
//
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("time.is.out"));
            }

            UserExchangeLog userExchangeLog = PointServiceSngl.get().exchangeGoodsItem(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), aid, new Date(), getIp(request), "pc", true);
            if (userExchangeLog == null) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.getcode.null"));
            }
            mapMessage.put("exchangeLog", userExchangeLog);
            userSession.setGetCodeTime(userExchangeLog.getExhangeTime().getTime());
            sendOutEvent(aid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("system.error"));
        }
        return new ModelAndView("/views/jsp/giftmarket/m/gift-getcode-m", mapMessage);
    }

    @RequestMapping(value = "/taocode")
    public ModelAndView taoCode(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "aid", required = false) String activityGoodsId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(activityGoodsId)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            Long aid = null;
            try {
                aid = Long.parseLong(activityGoodsId);
            } catch (NumberFormatException e) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            //返回
            String refer = request.getHeader("referer");
            String url = request.getRequestURL().toString();
            if (StringUtil.isEmpty(refer) || !refer.contains(request.getServerName()) || refer.equals(url)) {
                //为空 或者 被钓鱼，返回到礼包首页
                refer = WebappConfig.get().getUrlM() + "/gift";
            }
            mapMessage.put("refer", refer);

            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/loginpage?reurl=" + refer);
            }
            if (StringUtil.isEmpty(userSession.getMobile())) {
                return new ModelAndView("redirect:/gift/bindphonepage?reurl=" + refer);
            }

            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(aid);
            if (activityGoods == null) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("activity.gift.is.null"));
            }
            mapMessage.put("gift", activityGoods);

            Long newDate = System.currentTimeMillis();
            Long lastDate = userSession.getTaoCodeTime();
            if (lastDate != null) {
                long interval = (newDate - lastDate) / 1000;
                if (interval <= CODE_INTERVAL_TIME) {
                    return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.get.code.interval"));
                }
            }

            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("time.is.out"));
            }
            List<ExchangeGoodsItem> listExchangeGoodsItem = PointServiceSngl.get().taoExchangeGoodsItemByGoodsId(userSession.getUno(), userSession.getProfileId(), userSession.getAppkey(), aid, new Date(), getIp(request));
            if (listExchangeGoodsItem.size() < 10) {
                return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("user.gift.tao.num.limit.10"));
            }
            mapMessage.put("itemList", listExchangeGoodsItem);
            userSession.setTaoCodeTime(System.currentTimeMillis());
            sendOutEvent(aid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/giftmarket/m/result_error_m", putErrorMessage("system.error"));
        }
        return new ModelAndView("/views/jsp/giftmarket/m/gift-taocode-m", mapMessage);
    }

    @RequestMapping(value = "/searchpage")
    public ModelAndView searchPage(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "searchtext", required = false) String searchText,
                                   @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "size", required = false, defaultValue = "25") Integer size
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String reqUrl = request.getRequestURL().toString();
            URL url = new URL(reqUrl);
            if (url.getHost().contains("m.joyme.")) {
                size = 10;
            }

            Pagination paginatin = new Pagination(pageNo * size, pageNo, size);
            PageRows<ActivityDTO> activityDTOPageRows = giftMarketWebLogic.searchActivity(searchText, paginatin);
            mapMessage.put("searchtext", searchText);
            if (activityDTOPageRows != null) {
                mapMessage.put("list", activityDTOPageRows.getRows());
                mapMessage.put("page", activityDTOPageRows.getPage());
            } else {
                mapMessage.put("list", "");
                mapMessage.put("page", "");
                //独家礼包
                PageRows<ClientLineItem> exclusivePageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(PC_EXCLUSIVE, "", new Pagination(hotActivityPageSize, pageNo, hotActivityPageSize));
                if (exclusivePageRows != null && !CollectionUtil.isEmpty(exclusivePageRows.getRows())) {
                    Set<Long> exclusiveLong = new LinkedHashSet<Long>();
                    for (ClientLineItem clientLineItem : exclusivePageRows.getRows()) {
                        exclusiveLong.add(Long.parseLong(clientLineItem.getDirectId()));
                    }

                    QueryExpress exclusiveQueryExpress = new QueryExpress()
                            .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                            .add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, exclusiveLong.toArray()))
                            .add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
                    List<ActivityGoods> exclusiveActivityList = PointServiceSngl.get().listActivityGoods(exclusiveQueryExpress);
//                    List returnExclusiveList = new ArrayList<ActivityGoods>();
//                    for (Long id : exclusiveLong) {
//                        for (ActivityGoods activityGoods : exclusiveActivityList) {
//                            if (id == activityGoods.getActivityGoodsId()) {
//                                activityGoods.setActivityPicUrl(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
//                                returnExclusiveList.add(activityGoods);
//                            }
//                        }
//                    }
                    mapMessage.put("exclusiveList", exclusiveActivityList);
                }
            }
            //M端的搜索页面
            if (url.getHost().contains("m.joyme.")) {
                //m域名
                return new ModelAndView("/views/jsp/giftmarket/m/search-result-m", mapMessage);
            }

            //礼包中心和快来淘号模块//缓存
            Map<String, List<ActivityGoods>> activityGoodsMap = PointServiceSngl.get().queryGiftmarketIndexList();

            List<ActivityHotRanks> ranksList = PointServiceSngl.get().queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));

            List<ActivityHotRanks> returnRanksList = new ArrayList<ActivityHotRanks>();
            for (ActivityHotRanks activityHotRanks : ranksList) {
                activityHotRanks.setPic(URLUtils.getJoymeDnUrl(activityHotRanks.getPic()));
                returnRanksList.add(activityHotRanks);
            }

            mapMessage.put("giftlist", returnRanksList);
            mapMessage.put("taolist", activityGoodsMap.get("taoindexlist"));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("system.error"));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("system.error"));
        }

        return new ModelAndView("/views/jsp/giftmarket/searchpage", mapMessage);
    }

}
