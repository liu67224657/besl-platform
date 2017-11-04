/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;

import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:19
 * Description:
 */
public interface ViewLineWebDataProcessor {

    //web input to object
    public ViewLineAutoFillRule generateAutoFillRule(Map<String, String> autoFillRuleKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap);

    //object to edit input.
    public void autoFillRuleToInput(ViewLineAutoFillRule rule, Map<String, Object> msgMap);

    //add line item.
    public ViewLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap);

    //
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems);
}
