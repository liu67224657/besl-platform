/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.WikiResource;
import com.enjoyf.platform.service.gameres.WikiResourceField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemActivityDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemGameDTO;
import com.google.common.base.Strings;
import org.apache.xerces.impl.xpath.regex.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ViewLineActivityWebDataProcessor implements ViewLineWebDataProcessor {

    private static final Pattern GIFT_URL_PATTERN = Pattern.compile("/gift/(\\d+)");

    @Override
    public ViewLineAutoFillRule generateAutoFillRule(Map<String, String> autoFillRuleKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void autoFillRuleToInput(ViewLineAutoFillRule rule, Map<String, Object> msgMap) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ViewLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        ViewLineItem returnValue = new ViewLineItem();

        String activityId = lineItemKeyValues.get("srcId1");
        String activityUrl = lineItemKeyValues.get("srcId2");

        Activity activity = null;
        if (StringUtil.isEmpty(activityId)) {
            activityId = getGiftId(activityUrl);
        }

        if (StringUtil.isEmpty(activityId)) {
            errorMsgMap.put("srcId1", "error.viewline.item.wiki.input.wrong.data");
        }

        try {
            if (!StringUtil.isEmpty(activityId)) {
                activity = ContentServiceSngl.get().getActivityById(Long.valueOf(activityId));
            } else {
                errorMsgMap.put("errorMsgMap", "error.viewline.item.input.blank");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "generateAddLineItem error.", e);
        }
//
        ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();
        returnValue.setDisplayInfo(viewLineItemDisplayInfo);

        if (activity != null) {
            returnValue.setDirectId(String.valueOf(activity.getActivityId()));
            returnValue.setDirectUno(String.valueOf(activity.getActivityId()));
            returnValue.setItemCreateDate(activity.getCreateTime());
            returnValue.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

            viewLineItemDisplayInfo.setSubject(activity.getActivitySubject());
        }

        return returnValue;
    }

    private String getGiftId(String url) {
        Matcher m = GIFT_URL_PATTERN.matcher(url);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    @Override
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems) {
        List<ViewLineItemDTO> returnValue = new ArrayList<ViewLineItemDTO>();

        for (ViewLineItem item : viewLineItems) {
            ViewLineItemActivityDTO itemDTO = new ViewLineItemActivityDTO();

            itemDTO.setLineItemId(item.getItemId());
            itemDTO.setLineId(item.getLineId());
            itemDTO.setItemDesc(item.getItemDesc());

            itemDTO.setCreateDate(item.getCreateDate());
            itemDTO.setCreateUno(item.getCreateUno());

            itemDTO.setDisplayOrder(item.getDisplayOrder());
            itemDTO.setValidStatus(item.getValidStatus());

            itemDTO.setViewLineItem(item);

//            try {
//                itemDTO.setGameResource(GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, Long.parseLong(item.getDirectId())))));
//            } catch (ServiceException e) {
//                //
//                break;
//            }

            returnValue.add(itemDTO);
        }

        return returnValue;

    }

    public static void main(String[] args) {
        Matcher m = GIFT_URL_PATTERN.matcher("http://www.joyme.com/gift/101");
        if (m.find()) {
            System.out.println(m.group(1));
        }

    }
}
