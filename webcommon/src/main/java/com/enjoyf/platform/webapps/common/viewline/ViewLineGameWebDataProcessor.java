/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemGameDTO;
import com.google.common.base.Strings;

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
public class ViewLineGameWebDataProcessor implements ViewLineWebDataProcessor {

    private static final Pattern PATTERN_REG_GAMECODE = Pattern.compile("http://www\\.joyme\\.[^/]+/(?:game|board)/([a-zA-Z0-9]+)");

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

        String srcId1 = lineItemKeyValues.get("srcId1");
        String srcId2 = lineItemKeyValues.get("srcId2");
        String locationCode = "";
        Matcher m = PATTERN_REG_GAMECODE.matcher(srcId2);
        if (m.find()) {
            locationCode = m.group(1);
        }

        GameResource gameResource = null;
        try {
            if (!Strings.isNullOrEmpty(srcId1)) {
                gameResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCENAME, srcId1))
                        .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode())));
                if (gameResource == null) {
                    List<GameResource> gameResources = GameResourceServiceSngl.get().queryGameResourcesBySynonyms(srcId1);

                    if (gameResources.size() == 0) {
                        errorMsgMap.put("srcId1", "error.viewline.item.game.input.wrong.data");
                    } else {
                        gameResource = gameResources.get(0);
                    }
                }
            } else if (!Strings.isNullOrEmpty(srcId2)) {
                gameResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, locationCode)));
            } else {
                errorMsgMap.put("errorMsgMap", "error.viewline.item.input.blank");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "generateAddLineItem error.", e);
        }

        ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();
        returnValue.setDisplayInfo(viewLineItemDisplayInfo);

        if (gameResource != null) {
            returnValue.setDirectId(gameResource.getResourceId() + "");
            returnValue.setDirectUno(gameResource.getResourceId() + "");
            returnValue.setItemCreateDate(gameResource.getCreateDate());
            returnValue.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));


            viewLineItemDisplayInfo.setSubject(gameResource.getResourceName());
        }

        return returnValue;
    }

    @Override
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems) {
        List<ViewLineItemDTO> returnValue = new ArrayList<ViewLineItemDTO>();

        for (ViewLineItem item : viewLineItems) {
            ViewLineItemGameDTO itemDTO = new ViewLineItemGameDTO();

            itemDTO.setLineItemId(item.getItemId());
            itemDTO.setLineId(item.getLineId());
            itemDTO.setItemDesc(item.getItemDesc());

            itemDTO.setCreateDate(item.getCreateDate());
            itemDTO.setCreateUno(item.getCreateUno());

            itemDTO.setDisplayOrder(item.getDisplayOrder());
            itemDTO.setValidStatus(item.getValidStatus());

            itemDTO.setViewLineItem(item);

            try {
                itemDTO.setGameResource(GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, Long.parseLong(item.getDirectId())))));
            } catch (ServiceException e) {
                //
                break;
            }

            returnValue.add(itemDTO);
        }

        return returnValue;

    }
}
