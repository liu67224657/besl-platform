/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.joymeapp.ClientLineItem;

import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:19
 * Description:
 */
public interface ClientLineWebDataProcessor {

    //add line item.
    public ClientLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap);

    //
    public ClientLineItemDTO buildViewLineItemDTOs(ClientLineItem item) throws Exception;
}
