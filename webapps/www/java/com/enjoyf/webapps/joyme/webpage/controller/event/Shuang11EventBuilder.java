package com.enjoyf.webapps.joyme.webpage.controller.event;

import com.enjoyf.platform.props.hotdeploy.ActivityHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-4 上午10:53
 * Description:
 */
class Shuang11EventBuilder implements EventWebModelBuilder {
    private static final String Shuang11Activity = "shuang11";
    private GiftMarketWebLogic giftMarketWebLogic = new GiftMarketWebLogic();

    private ActivityHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ActivityHotdeployConfig.class);

    public ModelAndView buildModelAndView() throws Exception {
        ActivityConfig activityConfig = config.getActivityConfig(Shuang11Activity);

//        Date now = new Date();
//        if (activityConfig != null && activityConfig.getStartTime() != null && now.before(activityConfig.getStartTime())) {
//            //todo retrun prestart.jsp
//            return new ModelAndView("/views/jsp/event/" + Shuang11Activity + "/prestart");
//        }
//        if (activityConfig != null && activityConfig.getEndTime() != null && now.after(activityConfig.getEndTime())) {
//            return new ModelAndView("/views/jsp/event/" + Shuang11Activity + "/end");
//        }
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        List<ActivityDTO> listElevenGift = queryElevenGift();
//        List<ActivityDTO> listPointPage = querySalePoint();
//        int count = 0;
//        for (ActivityDTO activityDTO : listElevenGift) {
//            count += activityDTO.getRn();
//        }
//        for (ActivityDTO activityDTO : listPointPage) {
//            count += (activityDTO.getCn() - activityDTO.getSn());
//        }
//        count += 3488;
//        char[] countArray = StringUtil.appendZore(count, 5).toCharArray();
//        List charList = new LinkedList();
//        for (int i = 0; i < countArray.length; i++) {
//            charList.add(countArray[i]);
//        }
        mapMessage.put("giftPage", listElevenGift);
//        mapMessage.put("pointPage", listPointPage);
//        mapMessage.put("count", charList);
        return new ModelAndView("/views/jsp/event/" + Shuang11Activity + "/2014/page", mapMessage);
    }

    private List<ActivityDTO> queryElevenGift() throws ServiceException {
        ViewLine viewLine = ViewLineServiceSngl.get().getViewLine(new QueryExpress()
                .add(QueryCriterions.eq(ViewLineField.CATEGORYASPECT, ViewCategoryAspect.CUSTOM_GIFTINDEX.getCode()))
                .add(QueryCriterions.eq(ViewLineField.LOCATIONCODE, "elevengift")));

        List<ViewLineItem> giftList = ViewLineServiceSngl.get().queryLineItems(new QueryExpress().
                add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()))
                .add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC)));
        Set<Long> giftSet = new LinkedHashSet<Long>();
        for (ViewLineItem item : giftList) {
            giftSet.add(Long.valueOf(item.getDirectId()));
        }
        List<ActivityDTO> listActivityDTO = new LinkedList<ActivityDTO>();
        if (!CollectionUtil.isEmpty(giftSet)) {
            Map<Long, Activity> activityMap = ContentServiceSngl.get().queryActivityByActivityId(giftSet);
            Collection<Activity> collectionActivity = activityMap.values();
            int i = 0;
            for (Activity activity : collectionActivity) {
                if (i < 12 && activity != null) {
                    if (activity.getActivityType().equals(ActivityType.EXCHANGE_GOODS)){
//                        listActivityDTO.add(giftMarketWebLogic.buildExchangeActivityDTO(activity));
                    }
                }
                i++;
            }
        }
        return listActivityDTO;
    }

    private List<ActivityDTO> querySalePoint() throws ServiceException {
        ViewLine viewLine = ViewLineServiceSngl.get().getViewLine(new QueryExpress()
                .add(QueryCriterions.eq(ViewLineField.CATEGORYASPECT, ViewCategoryAspect.CUSTOM_GIFTINDEX.getCode()))
                .add(QueryCriterions.eq(ViewLineField.LOCATIONCODE, "salepoint")));

        List<ViewLineItem> salePointList = ViewLineServiceSngl.get().queryLineItems(new QueryExpress().
                add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()))
                .add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC)));
        Set<Long> activityIdSet = new LinkedHashSet<Long>();
        for (ViewLineItem item : salePointList) {
            activityIdSet.add(Long.valueOf(item.getDirectId()));
        }
        List<ActivityDTO> listActivityDTO = new LinkedList<ActivityDTO>();
        if (!CollectionUtil.isEmpty(activityIdSet)) {
            Map<Long, Activity> activityMap = ContentServiceSngl.get().queryActivityByActivityId(activityIdSet);
            Collection<Activity> collectionActivity = activityMap.values();
            List<ActivityDTO> tempList = new LinkedList<ActivityDTO>();
            if (!CollectionUtil.isEmpty(activityMap)) {
                int i = 0;
                for (Activity activity : collectionActivity) {
                    if (i < 8 && activity != null) {
                        if (activity.getActivityType().getCode() == ActivityType.GOODS.getCode());
//                            tempList.add(giftMarketWebLogic.buildGoodsActivityDTO(activity));
                    }
                    i++;
                }
            }

            for (ActivityDTO activityDTO : tempList) {
                for (ViewLineItem item : salePointList) {
                    if (activityDTO.getGid() == Long.parseLong(item.getDirectId())) {
                        if (!StringUtil.isEmpty(item.getDisplayInfo().getExtraField1())) {
                            activityDTO.setShuang11Point(Long.parseLong(item.getDisplayInfo().getExtraField1()));

                        } else {
                            activityDTO.setShuang11Point(Long.valueOf(activityDTO.getPoint()));
                        }
                        if (!StringUtil.isEmpty(item.getDisplayInfo().getIconUrl())) {
                            activityDTO.setShuang11Pic(item.getDisplayInfo().getIconUrl());
                        }
                        listActivityDTO.add(activityDTO);
                        break;
                    }
                }
            }
        }
        return listActivityDTO;
    }
}
