/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.joymeapp.ClientItemDomain;
import com.enjoyf.platform.service.joymeapp.ClientItemType;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.Date;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ClientLineCommentBeanWebDataProcessor implements ClientLineWebDataProcessor {


    @Override
    public ClientLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {

        ClientLineItem clientLineItem = new ClientLineItem();
        String directid = lineItemKeyValues.get("directid");
        if (StringUtil.isEmpty(directid)) {
            errorMsgMap.put("paramError", "directid.is.null");
            return clientLineItem;
        }
        CommentBean commentBean = null;
        try {
            commentBean = CommentServiceSngl.get().getCommentBeanById(directid);
            if (commentBean == null) {
                errorMsgMap.put("commentbeanError", "commentbean.is.null");
                return clientLineItem;
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            errorMsgMap.put("systemError", "system.error");
            return null;
        }
        String lineId = lineItemKeyValues.get("lineid");
        String itemType = lineItemKeyValues.get("itemtype");
        String itemDomain = lineItemKeyValues.get("itemdomain");
        String picurl = lineItemKeyValues.get("picurl");
        String author = lineItemKeyValues.get("author");
        String itemname = lineItemKeyValues.get("itemname");

        clientLineItem.setDirectId(directid);
        clientLineItem.setLineId(Integer.parseInt(lineId));
        clientLineItem.setItemDomain(ClientItemDomain.getByCode(Integer.parseInt(itemDomain)));
        clientLineItem.setPicUrl(StringUtil.isEmpty(picurl) ? commentBean.getPic() : picurl);
        clientLineItem.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
        clientLineItem.setTitle(StringUtil.isEmpty(itemname) ? commentBean.getTitle() : itemname);
        clientLineItem.setValidStatus(ValidStatus.REMOVED);
        clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        clientLineItem.setItemCreateDate(new Date());
        clientLineItem.setAuthor(author);
//        clientLineItem.setRedirectType(AppRedirectType.getByCode(Integer.parseInt(redirecttype)));
//        clientLineItem.setAppDisplayType(AppDisplayType.getByCode(Integer.parseInt(displayType)));
        return clientLineItem;
    }

    @Override
    public ClientLineItemDTO buildViewLineItemDTOs(ClientLineItem item) throws ServiceException {
        CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(item.getDirectId());
        if (commentBean == null) {
            return null;
        }
        ClientLineItemDTO dto = new ClientLineItemDTO();
        dto.setTitle(item.getTitle() == null ? commentBean.getTitle() : item.getTitle());
        dto.setDesc(item.getDesc() == null ? commentBean.getDescription() : item.getDesc());
        dto.setPic(URLUtils.getJoymeDnUrl(item.getPicUrl() == null ? commentBean.getPic() : item.getPicUrl()));
        dto.setId(item.getItemId());
        dto.setLid(item.getLineId());
        dto.setCreateDate(item.getItemCreateDate());
        dto.setDomain(item.getItemDomain().getCode());
        dto.setDid(item.getDirectId());
        dto.setLink(item.getUrl());
        dto.setOrder(item.getDisplayOrder());
        dto.setrType(item.getRedirectType() == null ? 0 : item.getRedirectType().getCode());
        dto.setType(item.getItemType().getCode());
        dto.setStatus(item.getValidStatus().getCode());
        return dto;
    }
}
