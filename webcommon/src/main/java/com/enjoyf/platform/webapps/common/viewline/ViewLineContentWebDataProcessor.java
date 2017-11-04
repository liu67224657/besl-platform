/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.autofillrule.ViewLineAutoFillContentRule;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemContentDTO;
import com.enjoyf.platform.webapps.common.dto.ViewLineItemDTO;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.google.common.base.Strings;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ViewLineContentWebDataProcessor implements ViewLineWebDataProcessor {
    //
    private static final int MAX_TIME = 99999999;

    //
    @Override
    public ViewLineAutoFillRule generateAutoFillRule(Map<String, String> autoFillRuleKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        //
        ViewLineAutoFillContentRule rule = new ViewLineAutoFillContentRule();

        //
        String afr01Value = autoFillRuleKeyValues.get("afrkey01");
        if (!Strings.isNullOrEmpty(afr01Value)) {
            String[] strarray = afr01Value.split(",");

            for (String str : strarray) {
                try {
                    ProfileBlog pb = ProfileServiceSngl.get().getProfileBlogByDomain(str);

                    if (pb != null) {
                        rule.getIncludeUnos().add(pb.getUno());
                    }
                } catch (ServiceException e) {
                    GAlerter.lab("ViewLineWebLogic generateAutoFillRule error.", e);
                }
            }
        }

        //
        String afr02Value = autoFillRuleKeyValues.get("afrkey02");
        if (!Strings.isNullOrEmpty(afr02Value)) {
            String[] strarray = afr02Value.split(",");
            for (String str : strarray) {
                rule.getIncludeKeywords().add(str);
            }
        }

        //
        String afr03Value = autoFillRuleKeyValues.get("afrkey03");
        if (!Strings.isNullOrEmpty(afr03Value)) {
            String[] strarray = afr03Value.split(",");
            for (String str : strarray) {
                rule.getContentTypeValues().add(Integer.valueOf(str));
            }

            Map<String, String> afrkey03s = new HashMap<String, String>();
            for (String str : afr03Value.split(",")) {
                afrkey03s.put(str, str);
            }

            msgMap.put("afrkey03s", afrkey03s);
        }

        //
        String afr04Value = autoFillRuleKeyValues.get("afrkey04");
        if (!Strings.isNullOrEmpty(afr04Value)) {
            String[] strarray = afr04Value.split(",");
            for (String str : strarray) {
                rule.getPublishTypeCodes().add(str);
            }

            Map<String, String> afrkey04s = new HashMap<String, String>();
            for (String str : afr04Value.split(",")) {
                afrkey04s.put(str, str);
            }

            msgMap.put("afrkey04s", afrkey04s);
        }

        //
        return rule;
    }

    @Override
    public void autoFillRuleToInput(ViewLineAutoFillRule rule, Map<String, Object> msgMap) {
        if (rule != null) {
            ViewLineAutoFillContentRule autoFillRule = (ViewLineAutoFillContentRule) rule;

            //
            String domains = "";
            try {
                //
                List<Profile> profiles = ProfileServiceSngl.get().queryProfilesByUnos(autoFillRule.getIncludeUnos());

                //
                for (Profile p : profiles) {
                    domains += p.getBlog().getDomain() + ",";
                }
                msgMap.put("afrkey01", domains);
            } catch (ServiceException e) {
                //
                GAlerter.lab("ViewLineWebLogic autoFillRuleToInput error.", e);
            }

            //
            String keyWords = "";
            for (String keyword : autoFillRule.getIncludeKeywords()) {
                keyWords += keyword + ",";
            }
            msgMap.put("afrkey02", keyWords);

            //
            Map<String, String> afrkey03sMap = new HashMap<String, String>();
            for (int contentType : autoFillRule.getContentTypeValues()) {
                afrkey03sMap.put(String.valueOf(contentType), String.valueOf(contentType));
            }
            msgMap.put("afrkey03s", afrkey03sMap);

            //
            Map<String, String> afrkey04sMap = new HashMap<String, String>();
            for (String publishType : autoFillRule.getPublishTypeCodes()) {
                afrkey04sMap.put(publishType, publishType);
            }
            msgMap.put("afrkey04s", afrkey04sMap);
        }

    }

    @Override
    public ViewLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
        ViewLineItem returnValue = new ViewLineItem();

        //
        String srcId1 = lineItemKeyValues.get("srcId1");
        if (!Strings.isNullOrEmpty(srcId1)) {
            //http://www.joyme.com/note/xxxx/xxxxxxx
//            Map<String, String> datas = splitUrl(srcId1);
            String contentId = getContentIdByUrl(srcId1);
            if (!StringUtil.isEmpty(contentId)) {
                try {
                    Content content = ContentServiceSngl.get().getContentById(contentId);

                    if (content != null) {
                        returnValue.setDirectId(content.getContentId());
                        returnValue.setDirectUno(content.getUno());

                        returnValue.setParentId(content.getParentContentId());
                        returnValue.setParentUno(content.getParentContentUno());

                        returnValue.setRelationId(content.getRootContentId());
                        returnValue.setRelationUno(content.getRootContentUno());

                        returnValue.setItemCreateDate(content.getPublishDate());
                        returnValue.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

                        ViewLineItemDisplayInfo viewLineItemDisplayInfo = new ViewLineItemDisplayInfo();
                        viewLineItemDisplayInfo.setLinkUrl(srcId1);


//                            if(content.getImages() != null){
//                                Set<ImageContent> imageContentSet = content.getImages().getImages();
//                                if(content.getImages().getValidStatus() && imageContentSet.size() > 0){
//                                    Iterator<ImageContent> it = imageContentSet.iterator();
//                                    while(it.hasNext()){
//                                        ImageContent imageContent = it.next();
//                                        if(imageContent.getValidStatus()){
//                                            viewLineItemDisplayInfo.setIconUrl(imageContent.getUrl());
//                                            break;
//                                        }
//                                    }
//                                }
//                            }

                        viewLineItemDisplayInfo.setSubject(content.getSubject());
                        ResolveContent resolveContent = new ResolveContent();
                        resolveContent.setContent(content.getContent());
                        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_SYNC_CONTENT);
                        resolveContent = textProcessor.process(resolveContent);
                        viewLineItemDisplayInfo.setDesc(content.getContent() != null ?
                                (resolveContent.getContent().length() > 100 ? resolveContent.getContent().substring(0, 100) : resolveContent.getContent()) : "");

                        returnValue.setDisplayInfo(viewLineItemDisplayInfo);
                    } else {
                        errorMsgMap.put("srcId1", "error.viewline.item.input.wrong.data");
                    }

                } catch (ServiceException e) {
                    //
                    errorMsgMap.put("srcId1", "error.viewline.item.input.wrong.data");
                }
            } else {
                errorMsgMap.put("srcId1", "error.viewline.item.input.wrong.fmt");
            }
        } else {
            errorMsgMap.put("errorMsgMap", "error.viewline.item.input.blank");
        }

        return returnValue;
    }

    @Override
    public List<ViewLineItemDTO> buildViewLineItemDTOs(List<ViewLineItem> viewLineItems) {
        List<ViewLineItemDTO> returnValue = new ArrayList<ViewLineItemDTO>();

        for (ViewLineItem item : viewLineItems) {
            ViewLineItemContentDTO itemDTO = new ViewLineItemContentDTO();

            itemDTO.setLineItemId(item.getItemId());
            itemDTO.setLineId(item.getLineId());
            itemDTO.setItemDesc(item.getItemDesc());

            itemDTO.setCreateDate(item.getCreateDate());
            itemDTO.setCreateUno(item.getCreateUno());

            itemDTO.setDisplayOrder(item.getDisplayOrder());
            itemDTO.setValidStatus(item.getValidStatus());
            itemDTO.setDisplayType(item.getDisplayType());

            itemDTO.setViewLineItem(item);

            try {
                Content content = ContentServiceSngl.get().getContentById(item.getDirectId());

                ResolveContent resolveContent = new ResolveContent();
                resolveContent.setContent(content.getContent());
                TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_PRIVIEW_TEXT);
                resolveContent = textProcessor.process(resolveContent);

                content.setContent(resolveContent.getContent());
                itemDTO.setContent(content);
                itemDTO.setProfile(ProfileServiceSngl.get().getProfileBlogByUno(item.getDirectUno()));
            } catch (ServiceException e) {
                //
                break;
            }

            returnValue.add(itemDTO);
        }

        return returnValue;
    }

    //////////////////////////////////////////
    private String getContentIdByUrl(String url) {
        Pattern contentIdPattern = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getContentUrlRegex();

        List<Map<String, String>> result = RegexUtil.fetch(url, contentIdPattern, 1);
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }

        return result.get(0).get("1");
    }
}
