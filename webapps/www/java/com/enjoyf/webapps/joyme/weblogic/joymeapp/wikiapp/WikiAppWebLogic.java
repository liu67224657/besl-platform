package com.enjoyf.webapps.joyme.weblogic.joymeapp.wikiapp;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.JoymeWiki;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiAppIndexItemDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhitaoshi on 2015/4/10.
 */
@Service(value = "wikiAppWebLogic")
public class WikiAppWebLogic {

    public List<WikiAppIndexItemDTO> queryWikiAppSubscribe(String profileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException {
        if(StringUtil.isEmpty(profileId)){
            return null;
        }
        List<WikiAppIndexItemDTO> MyWikiList = new ArrayList<WikiAppIndexItemDTO>();

        List<String> myWikiKeyList = SocialServiceSngl.get().queryObjectRelationObjectIdList(profileId, relationType, pagination);
        Set<String> wikiKeySet = new HashSet<String>();
        wikiKeySet.addAll(myWikiKeyList);
        Map<String, JoymeWiki> wikiMap = JoymeAppServiceSngl.get().queryJoymeWikiByWikiKeySet(wikiKeySet, JoymeWikiContextPath.MWIKI);
        for(String wikiKey:myWikiKeyList){
            if(wikiMap.containsKey(wikiKey)){
                if(wikiMap.get(wikiKey) != null){
                    WikiAppIndexItemDTO itemDTO = buildWikiAppIndexItemDTOByWiki(wikiMap.get(wikiKey));
                    if(itemDTO != null){
                        MyWikiList.add(itemDTO);
                    }
                }
            }
        }
        return MyWikiList;
    }

    private WikiAppIndexItemDTO buildWikiAppIndexItemDTOByWiki(JoymeWiki joymeWiki) {
        if(joymeWiki == null){
            return null;
        }
        WikiAppIndexItemDTO itemDTO = new WikiAppIndexItemDTO();
        itemDTO.setWikikey(joymeWiki.getWikiKey());
        itemDTO.setPicurl(joymeWiki.getParam().getIcon());
        itemDTO.setTitle(joymeWiki.getWikiName());
        itemDTO.setJt("");
        itemDTO.setJi(JoymeWikiUtil.getWikiUrl());
        return itemDTO;
    }

    public WikiAppIndexItemDTO buildWikiAppIndexItemDTO(ClientLineItem item) {
        if(item == null){
            return null;
        }
        WikiAppIndexItemDTO itemDTO = new WikiAppIndexItemDTO();
        itemDTO.setWikikey(item.getDirectId());
        itemDTO.setPicurl(item.getPicUrl());
        itemDTO.setTitle(item.getTitle());
        itemDTO.setJt(String.valueOf(item.getRedirectType().getCode()));
        itemDTO.setJi(item.getUrl());
        return itemDTO;
    }



}
