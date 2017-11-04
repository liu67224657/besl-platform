package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemCustomDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.google.gdata.util.common.base.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-5-25
 * Time: 下午6:27
 * To change this template use File | Settings | File Templates.
 */
public class ViewLineCustomWebDataProcessor implements ViewLineWebDataProcessor  {
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
        ViewLineItemDisplayInfo displayInfo = new ViewLineItemDisplayInfo();

        //1, the url, 2, the subject, 3, the icon, 4, the desciption
        String srcId1 = lineItemKeyValues.get("srcId1");
        String srcId2 = lineItemKeyValues.get("srcId2");
        String srcId3 = lineItemKeyValues.get("srcId3");
        String srcId4 = lineItemKeyValues.get("srcId4");

        if(!StringUtil.isEmpty(srcId1)){
            displayInfo.setLinkUrl(srcId1);
        }
        if(!StringUtil.isEmpty(srcId2)){
            displayInfo.setSubject(srcId2);
        }
        if(!StringUtil.isEmpty(srcId3)){
            displayInfo.setIconUrl(srcId3);
        }
        if(!StringUtil.isEmpty(srcId4)){
            displayInfo.setDesc(srcId4);
        }

        returnValue.setDisplayInfo(displayInfo);
        returnValue.setItemCreateDate(new Date());
        returnValue.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

        return returnValue;
    }

    @Override
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems) {
        List<ViewLineItemDTO> returnValue = new ArrayList<ViewLineItemDTO>();

        for (ViewLineItem item : viewLineItems) {
            ViewLineItemCustomDTO itemDTO = new ViewLineItemCustomDTO();

            itemDTO.setLineItemId(item.getItemId());
            itemDTO.setLineId(item.getLineId());
            itemDTO.setItemDesc(item.getItemDesc());

            itemDTO.setCreateDate(item.getCreateDate());
            itemDTO.setCreateUno(item.getCreateUno());

            itemDTO.setDisplayOrder(item.getDisplayOrder());
            itemDTO.setValidStatus(item.getValidStatus());

            itemDTO.setViewLineItem(item);

            returnValue.add(itemDTO);
        }

        return returnValue;
    }
}
