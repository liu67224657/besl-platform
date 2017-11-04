/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.ClientItemDomain;
import com.enjoyf.platform.service.joymeapp.ClientItemType;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.mongodb.BasicDBObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ClientLineGameWebDataProcessor implements ClientLineWebDataProcessor {

    private static final Pattern PATTERN_REG_GAMECODE = Pattern.compile("http://www\\.joyme\\.[^/]+/(?:game|board)/([a-zA-Z0-9]+)");

    @Override
    public ClientLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {

        ClientLineItem clientLineItem = new ClientLineItem();
        String directid = lineItemKeyValues.get("directid");
        if (StringUtil.isEmpty(directid)) {
            errorMsgMap.put("paramError", "directid.is.null");
            return clientLineItem;
        }
        GameDB gameDB = null;
        try {
            gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(directid)), false);
            if (gameDB == null) {
                errorMsgMap.put("gameDbError", "gamedb.is.null");
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

        String url = lineItemKeyValues.get("linkaddress");
        String itemname = lineItemKeyValues.get("itemname");

        String desc = lineItemKeyValues.get("desc");
        String redirecttype = lineItemKeyValues.get("redirecttype");
        String rate = lineItemKeyValues.get("rate");


        clientLineItem.setRate(StringUtil.isEmpty(rate) ? String.valueOf(gameDB.getGameRate()) : rate);
        clientLineItem.setDesc(StringUtil.isEmpty(desc) || desc == "" ? gameDB.getGameProfile() : desc);
        clientLineItem.setDirectId(directid);
        clientLineItem.setLineId(Integer.parseInt(lineId));
        clientLineItem.setItemDomain(ClientItemDomain.getByCode(Integer.parseInt(itemDomain)));
        clientLineItem.setPicUrl(StringUtil.isEmpty(picurl) || picurl == "" ? gameDB.getGameIcon() : picurl);
        clientLineItem.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
        clientLineItem.setTitle(StringUtil.isEmpty(itemname) || itemname == "" ? gameDB.getGameName() : itemname);
        String linkUrl = "";
        if (!StringUtil.isEmpty(gameDB.getWikiUrl())) {
            linkUrl = gameDB.getWikiUrl();
        } else {
            linkUrl = gameDB.getCmsUrl();
        }

        clientLineItem.setUrl(StringUtil.isEmpty(url) || url == "" ? linkUrl : url);
        clientLineItem.setValidStatus(ValidStatus.VALID);
        clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

        if (lineItemKeyValues.containsKey("displayType")) {
            try {
                clientLineItem.setDisplayType(Integer.parseInt(lineItemKeyValues.get("displayType")));
            } catch (NumberFormatException e) {
                errorMsgMap.put("format error", "parse displayType failure!");
            }
        }

        if (lineItemKeyValues.containsKey("category")) {
            try {
                clientLineItem.setCategory(lineItemKeyValues.get("category"));
            } catch (NumberFormatException e) {
            }
        }

        if (lineItemKeyValues.containsKey("categoryColor")) {
            try {
                clientLineItem.setCategoryColor(lineItemKeyValues.get("categoryColor"));
            } catch (NumberFormatException e) {
            }
        }


        if (lineItemKeyValues.containsKey("startdate")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                clientLineItem.setStateDate(sdf.parse(lineItemKeyValues.get("startdate")));
            } catch (ParseException pe) {

                errorMsgMap.put("format error", "parse startdate failure!");
            }
        }

        if (lineItemKeyValues.containsKey("contentid")) {
            try {
                clientLineItem.setContentid(Long.parseLong(lineItemKeyValues.get("contentid")));
            } catch (NumberFormatException e) {
                errorMsgMap.put("format error", "parse contentid failure!");
            }
        }


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
        if (!StringUtil.isEmpty(redirecttype)) {
            clientLineItem.setRedirectType(AppRedirectType.getByCode(Integer.parseInt(redirecttype)));
        }
        return clientLineItem;
    }

    @Override
    public ClientLineItemDTO buildViewLineItemDTOs(ClientLineItem item) throws ServiceException {
        GameDB game = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", Long.parseLong(item.getDirectId())), false);
        if (game == null) {
            return null;
        }
        ClientLineItemDTO dto = new ClientLineItemDTO();
        dto.setTitle(item.getTitle() == null ? game.getGameName() : item.getTitle());
        dto.setDesc(item.getDesc() == null ? game.getGameProfile() : item.getDesc());
        dto.setPic(item.getPicUrl() == null ? game.getGameIcon() : item.getPicUrl());
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
        dto.setDisplayType(item.getDisplayType());
        return dto;
    }
}
