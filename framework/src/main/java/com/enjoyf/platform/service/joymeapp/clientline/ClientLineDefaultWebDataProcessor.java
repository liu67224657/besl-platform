/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ClientLineDefaultWebDataProcessor implements ClientLineWebDataProcessor {

    @Override
    public ClientLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        ClientLineItem clientLineItem = new ClientLineItem();
        String lineId = lineItemKeyValues.get("lineid");
        String itemType = lineItemKeyValues.get("itemtype");
        String itemDomain = lineItemKeyValues.get("itemdomain");
        String picurl = lineItemKeyValues.get("picurl");
        String directid = lineItemKeyValues.get("directid");
        String url = lineItemKeyValues.get("linkaddress");
        String itemname = lineItemKeyValues.get("itemname");
        String desc = lineItemKeyValues.get("desc");
        String redirecttype = lineItemKeyValues.get("redirecttype");
        String displayType = lineItemKeyValues.get("displaytype");
        String author = lineItemKeyValues.get("author");
        String status = lineItemKeyValues.get("status");
        String paramtextjson = lineItemKeyValues.get("paramtextjson");
        if (!StringUtil.isEmpty(paramtextjson)) {
            clientLineItem.setParam(ParamTextJson.fromJson(paramtextjson));
        }

        ValidStatus validStatus = null;
        if (!StringUtil.isEmpty(status)) {
            validStatus = ValidStatus.getByCode(status);
        }

        AppRedirectType redirectType = AppRedirectType.getByCode(Integer.parseInt(redirecttype));
        if (AppRedirectType.CMSARTICLE.equals(redirectType)) {
            url = url.replace("marticle/", "json/");
        } else if (AppRedirectType.TAGLIST.equals(redirectType)) {
            //判断tagid是否合法
            Integer tagId = null;
            try {
                tagId = Integer.parseInt(directid);
            } catch (NumberFormatException e) {
            }

            if (tagId == null) {
                tagId = getTagIdByTagListUrl(url);
            }

            if (tagId == null) {
                //todo 错误提示下个版本修复
                errorMsgMap.put("urlerror", "article.link.invalid");
            }

            url = "http://marticle.joyme.com/json/tags/" + tagId + "_1.html";
            directid = tagId.toString();
        }

        clientLineItem.setDesc(desc);
        clientLineItem.setDirectId(directid);
        clientLineItem.setLineId(Integer.parseInt(lineId));
        clientLineItem.setItemDomain(ClientItemDomain.getByCode(Integer.parseInt(itemDomain)));
        clientLineItem.setPicUrl(picurl);
        clientLineItem.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
        clientLineItem.setTitle(itemname);
        clientLineItem.setUrl(url);
        clientLineItem.setRedirectType(AppRedirectType.getByCode(Integer.parseInt(redirecttype)));
        if (validStatus == null) {
            clientLineItem.setValidStatus(ValidStatus.VALID);
        } else {
            clientLineItem.setValidStatus(validStatus);
        }
        clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

        String category = lineItemKeyValues.get("category");
        String categpryColor = lineItemKeyValues.get("categorycolor");

        String itemcreatedate = lineItemKeyValues.get("itemcreatedate");
        if (StringUtil.isEmpty(itemcreatedate)) {
            clientLineItem.setItemCreateDate(new Date());
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                clientLineItem.setItemCreateDate(sdf.parse(itemcreatedate));
            } catch (ParseException pe) {

                errorMsgMap.put("format error", "parse itemcreatedate failure!");
            }
        }
        if (!StringUtil.isEmpty(displayType)) {
            clientLineItem.setAppDisplayType(AppDisplayType.getByCode(Integer.parseInt(displayType)));
        }
        clientLineItem.setCategory(category);
        clientLineItem.setCategoryColor(categpryColor);
        clientLineItem.setAuthor(author);
        return clientLineItem;
    }

    @Override
    public ClientLineItemDTO buildViewLineItemDTOs(ClientLineItem item) {
        ClientLineItemDTO dto = new ClientLineItemDTO();
        dto.setId(item.getItemId());
        dto.setLid(item.getLineId());
        dto.setCreateDate(item.getItemCreateDate());
        dto.setDesc(item.getDesc());
        dto.setDomain(item.getItemDomain().getCode());
        dto.setDid(item.getDirectId());
        dto.setLink(item.getUrl());
        dto.setOrder(item.getDisplayOrder());
        dto.setPic(item.getPicUrl());
        dto.setrType(item.getRedirectType().getCode());
        dto.setTitle(item.getTitle());
        dto.setType(item.getItemType().getCode());
        dto.setStatus(item.getValidStatus().getCode());
        dto.setCategory(item.getCategory());
        dto.setCategoryColor(item.getCategoryColor());
        return dto;
    }

    private Integer getTagIdByTagListUrl(String tagUrl) {
        Integer tagId = null;

        int startIndex = tagUrl.lastIndexOf("/");
        int endIndex = tagUrl.lastIndexOf(".html");

        String tagIdString = tagUrl.substring(startIndex + 1, endIndex);

        if (tagIdString.indexOf("_") > 0) {
            tagIdString = tagIdString.split("_")[0];
        }

        try {
            tagId = Integer.parseInt(tagIdString);
        } catch (NumberFormatException e) {
            GAlerter.lab(this.getClass().getName() + " get tagId error.Url:" + tagUrl);
            return null;
        }

        return tagId;
    }
}
