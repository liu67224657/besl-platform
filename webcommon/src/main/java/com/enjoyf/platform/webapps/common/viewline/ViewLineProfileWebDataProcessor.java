/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.autofillrule.ViewLineAutoFillProfileRule;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemProfileDTO;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ViewLineProfileWebDataProcessor implements ViewLineWebDataProcessor {

    @Override
    public ViewLineAutoFillRule generateAutoFillRule(Map<String, String> autoFillRuleKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        return new ViewLineAutoFillProfileRule();
    }

    @Override
    public void autoFillRuleToInput(ViewLineAutoFillRule rule, Map<String, Object> msgMap) {
        //
    }

    @Override
    public ViewLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        ViewLineItem returnValue = new ViewLineItem();

        //1, the domain, 2, the nickname
        String srcId1 = lineItemKeyValues.get("srcId1");
        String srcId2 = lineItemKeyValues.get("srcId2");

        ProfileBlog profile = null;

        try {
            if (!Strings.isNullOrEmpty(srcId1)) {
                profile = ProfileServiceSngl.get().getProfileBlogByDomain(srcId1);
            } else if (!Strings.isNullOrEmpty(srcId2)) {
                profile = ProfileServiceSngl.get().getProfileBlogByScreenName(srcId2);
            }
        } catch (ServiceException e) {
            //
            GAlerter.lab("ViewLineProfileWebDataProcessor generateAddLineItem getProfileBlogByDomain error.", e);
        }

        if (profile != null) {
            returnValue.setDirectId(profile.getUno());
            returnValue.setDirectUno(profile.getUno());
            returnValue.setItemCreateDate(profile.getCreateDate());
            returnValue.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

            ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();
            viewLineItemDisplayInfo.setSubject(profile.getScreenName());
//            viewLineItemDisplayInfo.setDesc(profile.getDescription());
//            viewLineItemDisplayInfo.setIconUrl(profile.getHeadIcon());

            returnValue.setDisplayInfo(viewLineItemDisplayInfo);


        } else {
            errorMsgMap.put("system", "error.viewline.item.input.wrong.data");
        }

        return returnValue;
    }

    @Override
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems) {
        List<ViewLineItemDTO> returnValue = new ArrayList<ViewLineItemDTO>();

        for (ViewLineItem item : viewLineItems) {
            ViewLineItemProfileDTO itemDTO = new ViewLineItemProfileDTO();

            itemDTO.setLineItemId(item.getItemId());
            itemDTO.setLineId(item.getLineId());
            itemDTO.setItemDesc(item.getItemDesc());

            itemDTO.setCreateDate(item.getCreateDate());
            itemDTO.setCreateUno(item.getCreateUno());

            itemDTO.setDisplayOrder(item.getDisplayOrder());
            itemDTO.setValidStatus(item.getValidStatus());

            itemDTO.setViewLineItem(item);

            try {
                itemDTO.setProfile(ProfileServiceSngl.get().getProfileByUno(item.getDirectUno()));
            } catch (ServiceException e) {
                //
                break;
            }

            returnValue.add(itemDTO);
        }

        return returnValue;
    }
}
