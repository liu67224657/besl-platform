package com.enjoyf.webapps.joyme.weblogic.joymeapp.client;

import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.gameres.gamedb.GamePlatformType;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.search.SearchGameCriteria;
import com.enjoyf.platform.service.search.SearchGameDbResultEntry;
import com.enjoyf.platform.service.search.SearchServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.webapps.joyme.dto.joymeapp.client.*;
import com.enjoyf.webapps.joyme.dto.joymeclient.*;
import com.enjoyf.webapps.joyme.dto.platinum.PlatinumDTO;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-12
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "clientWebLogic")
public class ClientWebLogic {

    private static final int HOME_RECOM_SIZE = 4;
    private static final int PAGE_NO = 1;

    private static final long IPHONE = 1;
    private static final long IPAD = 2;
    private static final long ANDROID = 3;

    private static Map<Long, Long> platformMap = new HashMap<Long, Long>();

    static {
        platformMap.put(0l, IPHONE);
        platformMap.put(1l, ANDROID);
        platformMap.put(2l, IPAD);
    }

    /**
     * @param appKey
     * @param category    类型 0-活动轮播图  1-大端轮播图  2-新闻端轮播图
     * @param channelCode 渠道
     * @param platform    平台
     * @param flag
     * @return
     * @throws ServiceException
     */
    public List<ClientTopMenuDTO> queryClientTopMenuList(String appKey, Integer category, String channelCode, Integer platform, String flag) throws ServiceException {
        List<ActivityTopMenu> topMenuList = JoymeAppServiceSngl.get().queryClientTopMenuCache(platform, category, flag, getAppKey(appKey));
        if (CollectionUtil.isEmpty(topMenuList)) {
            return null;
        }
        Map<String, List<ClientTopMenuDTO>> map = new LinkedHashMap<String, List<ClientTopMenuDTO>>();
        for (ActivityTopMenu top : topMenuList) {
            ClientTopMenuDTO dto = new ClientTopMenuDTO();
            dto.setMenuId(top.getActivityTopMenuId());
            dto.setTitle(top.getMenuName());
            dto.setPicurl(top.getPicUrl());
            dto.setValue(top.getLinkUrl());
            dto.setType(top.getRedirectType());
            if (!map.containsKey(top.getAppKey() + top.getChannelId())) {
                map.put(top.getAppKey() + top.getChannelId(), new ArrayList<ClientTopMenuDTO>());
            }
            map.get(top.getAppKey() + top.getChannelId()).add(dto);
        }
        List<ClientTopMenuDTO> returnList = new LinkedList<ClientTopMenuDTO>();
        List<ClientTopMenuDTO> list = map.get(getAppKey(appKey) + getChannelId(channelCode));
        if (CollectionUtil.isEmpty(list)) {
            list = map.get(getAppKey(appKey) + 0);
        }
        if (!CollectionUtil.isEmpty(list)) {
            for (int i = 0; i < 3; i++) {
                if (i < list.size() && list.get(i) != null) {
                    returnList.add(list.get(i));
                }
            }
        }
        return returnList;

    }

    private static String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

    private Long getChannelId(String channelCode) throws ServiceException {
        if (StringUtil.isEmpty(channelCode)) {
            return 0L;
        }
        List<AppChannel> appChannelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
        if (CollectionUtil.isEmpty(appChannelList)) {
            return 0L;
        }
        for (AppChannel channel : appChannelList) {
            if (channel.getChannelCode().equals(channelCode)) {
                return channel.getChannelId();
            }
        }
        return 0L;
    }


    public PageRows<ArticleDTO> queryArticleListByItems(String lineCode, String flag, Pagination pagination) throws ServiceException {

        PageRows<ClientLineItem> lineItemRows = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, flag, pagination);

        if (lineItemRows == null || CollectionUtil.isEmpty(lineItemRows.getRows())) {
            return null;
        }

        Set<Integer> archiveIds = new HashSet<Integer>();
        for (ClientLineItem items : lineItemRows.getRows()) {
            if (items.getItemDomain().equals(ClientItemDomain.CMSARTICLE)) {
                try {
                    archiveIds.add(Integer.valueOf(items.getDirectId()));
                } catch (NumberFormatException e) {
                }
            }
        }

        Map<Integer, Archive> archiveMap = new HashMap<Integer, Archive>();
        if (!CollectionUtil.isEmpty(archiveIds)) {
            archiveMap = JoymeAppServiceSngl.get().queryArchiveMapByIds(archiveIds);
        }

        List<ArticleDTO> returnList = new ArrayList<ArticleDTO>();
        for (ClientLineItem item : lineItemRows.getRows()) {
            ArticleDTO articleDTO = buildByArticleDTO(item, archiveMap);
            if (articleDTO != null) {
                returnList.add(articleDTO);
            }
        }

        PageRows<ArticleDTO> returnObj = new PageRows<ArticleDTO>();
        returnObj.setRows(returnList);
        returnObj.setPage(lineItemRows.getPage());

        return returnObj;
    }

    //
    private ArticleDTO buildByArticleDTO(ClientLineItem item, Map<Integer, Archive> map) {
        ArticleDTO articleDTO = null;
        if (item.getItemDomain().equals(ClientItemDomain.CMSARTICLE) && !StringUtil.isEmpty(item.getDirectId())) {
            try {
                Archive archive = map.get(Integer.parseInt(item.getDirectId()));
                if (archive != null) {
                    articleDTO = new ArticleDTO();
                    articleDTO.setDate(item.getItemCreateDate().getTime());
                    articleDTO.setDesc(item.getDesc());
                    articleDTO.setMenuId(item.getItemId());
                    articleDTO.setPicurl(item.getPicUrl());
                    articleDTO.setTitle(item.getTitle());
                    articleDTO.setType(item.getRedirectType().getCode());
                    articleDTO.setValue(item.getUrl());
                    articleDTO.setTags(new ArrayList<TagDTO>());
                    for (ArchiveTag archiveTag : archive.getArchiveTagList()) {
                        articleDTO.getTags().add(new TagDTO(archiveTag.getTagId(), archiveTag.getTag()));
                    }
                    return articleDTO;
                } else {
                    GAlerter.lab(this.getClass().getName() + " archive is empty.itemId:" + item.getItemId());
                }
            } catch (NumberFormatException e) {
                GAlerter.lab(this.getClass().getName() + " trans clientitem to Integer error.itemId:" + item.getItemId());
            }
        } else {
            articleDTO = new ArticleDTO();
            articleDTO.setDate(item.getItemCreateDate().getTime());
            articleDTO.setDesc(item.getDesc());
            articleDTO.setMenuId(item.getItemId());
            articleDTO.setPicurl(item.getPicUrl());
            articleDTO.setTitle(item.getTitle());
            articleDTO.setType(item.getRedirectType().getCode());
            articleDTO.setValue(item.getUrl());
        }

        return articleDTO;
    }

    //攻略库推荐位
    public PageRows<GameDTO> queryGameDTO(String lineCode, String flag, Pagination pagination) throws ServiceException {
        PageRows<GameDTO> pageRows = null;
        List<GameDTO> list = null;
        PageRows<ClientLineItem> page = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, flag, pagination);
        if (page == null || CollectionUtil.isEmpty(page.getRows())) {
            return pageRows;
        }
        pageRows = new PageRows<GameDTO>();
        list = new ArrayList<GameDTO>();
        for (ClientLineItem clientLineItem : page.getRows()) {
            GameDTO gameDTO = new GameDTO();
            gameDTO.setDesc(clientLineItem.getDesc());
            gameDTO.setGid(Long.parseLong(clientLineItem.getDirectId()));
            gameDTO.setIcon(clientLineItem.getPicUrl());
            gameDTO.setTitle(clientLineItem.getTitle());
            gameDTO.setRate(clientLineItem.getRate());
            gameDTO.setLink(clientLineItem.getUrl());
            gameDTO.setType(clientLineItem.getRedirectType().getCode());
            list.add(gameDTO);
        }
        pageRows = new PageRows<GameDTO>();
        pageRows.setPage(page.getPage());
        pageRows.setRows(list);
        return pageRows;
    }

    public List<ClientRecomMenuDTO> queryClientRecomMenuList(String lineCode, String flag) throws ServiceException {
        PageRows<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, flag, new Pagination(PAGE_NO * HOME_RECOM_SIZE, PAGE_NO, HOME_RECOM_SIZE));
        if (itemList == null || CollectionUtil.isEmpty(itemList.getRows())) {
            return null;
        }
        List<ClientRecomMenuDTO> dtoList = new LinkedList<ClientRecomMenuDTO>();
        for (ClientLineItem item : itemList.getRows()) {
            ClientRecomMenuDTO dto = buildClientRecomDTO(item);
            dtoList.add(dto);
        }
        return dtoList;
    }

    private ClientRecomMenuDTO buildClientRecomDTO(ClientLineItem item) {
        ClientRecomMenuDTO dto = new ClientRecomMenuDTO();
        dto.setMenuId(item.getItemId());
        dto.setTitle(item.getTitle());
        dto.setType(item.getRedirectType().getCode());
        dto.setValue(item.getUrl());
        return dto;
    }

    public PageRows<ArticleDTO> queryHomeNewsMenuList(String lineCode, String flag, Pagination pagination) throws Exception {
        PageRows<ClientLineItem> itemPageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, flag, pagination);
        if (itemPageRows == null || CollectionUtil.isEmpty(itemPageRows.getRows())) {
            return null;
        }
        List<ArticleDTO> dtoList = new LinkedList<ArticleDTO>();
        for (ClientLineItem item : itemPageRows.getRows()) {
            ArticleDTO dto = buildHomeNewsDTO(item);
            if (dto != null) {
                dtoList.add(dto);
            }
        }
        PageRows<ArticleDTO> pageRows = new PageRows<ArticleDTO>();
        pageRows.setPage(itemPageRows.getPage());
        pageRows.setRows(dtoList);
        return pageRows;
    }

    private ArticleDTO buildHomeNewsDTO(ClientLineItem item) throws Exception {
        Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.parseInt(item.getDirectId()));
        if (archive == null) {
            return null;
        }
        ArticleDTO dto = new ArticleDTO();
        dto.setDate(item.getItemCreateDate().getTime());
        dto.setTitle(item.getTitle() == null ? archive.getTitle() : item.getTitle());
        dto.setDesc(item.getDesc() == null ? archive.getDesc() : item.getDesc());
        List<TagDTO> tagDTOs = new LinkedList<TagDTO>();
        for (ArchiveTag tag : archive.getArchiveTagList()) {
            TagDTO tagDTO = new TagDTO(tag.getTagId(), tag.getTag());
            tagDTOs.add(tagDTO);
        }
        dto.setTags(tagDTOs);
        dto.setType(item.getRedirectType().getCode());
        dto.setMenuId(item.getItemId());
        dto.setPicurl(item.getPicUrl());
        dto.setValue(item.getUrl());
        dto.setTypeName(StringUtil.isEmpty(archive.getTypeName()) ? "" : archive.getTypeName());
        dto.setTypeColor(StringUtil.isEmpty(archive.getTypeColor()) ? "" : archive.getTypeColor());
        return dto;
    }

    public PageRows<ClientSpecialDTO> querySpecial(String linceCode, String flag, Pagination pagination) throws ServiceException {
        PageRows<ClientSpecialDTO> pageRows = null;
//        PageRows<ClientLineItem> clientLineItemPageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(new QueryExpress()
//                .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
//                .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
//                .add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC)), pagination);

        PageRows<ClientLineItem> clientLineItemPageRows = JoymeAppServiceSngl.get().queryItemsByLineCode(linceCode, flag, pagination);


        if (CollectionUtil.isEmpty(clientLineItemPageRows.getRows())) {
            return pageRows;
        }
        pageRows = new PageRows<ClientSpecialDTO>();
        List<ClientSpecialDTO> clientLineItems = new ArrayList<ClientSpecialDTO>();
        for (ClientLineItem clientLineItem : clientLineItemPageRows.getRows()) {
            ClientSpecialDTO clientSpecialDTO = new ClientSpecialDTO();
            clientSpecialDTO.setMenuId(clientLineItem.getItemId());
            clientSpecialDTO.setPicurl(clientLineItem.getPicUrl());
            clientSpecialDTO.setTitle(clientLineItem.getTitle());
            clientSpecialDTO.setType(clientLineItem.getRedirectType().getCode());
            clientSpecialDTO.setValue(clientLineItem.getUrl());
            clientSpecialDTO.setDesc(clientLineItem.getDesc());
            clientLineItems.add(clientSpecialDTO);
        }
        pageRows.setRows(clientLineItems);
        pageRows.setPage(clientLineItemPageRows.getPage());
        return pageRows;
    }

    public PageRows<ClientMoreAppDTO> queryClientMoreAppList(String clientMoreAppCode, String flag, Pagination pagination) throws ServiceException {
        PageRows<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryItemsByLineCode(clientMoreAppCode, flag, pagination);
        if (itemList == null || CollectionUtil.isEmpty(itemList.getRows())) {
            return null;
        }
        List<ClientMoreAppDTO> dtoList = new LinkedList<ClientMoreAppDTO>();
        for (ClientLineItem item : itemList.getRows()) {
            ClientMoreAppDTO dto = buildMoreAppDTO(item);
            dtoList.add(dto);
        }
        PageRows<ClientMoreAppDTO> pageRows = new PageRows<ClientMoreAppDTO>();
        pageRows.setPage(itemList.getPage());
        pageRows.setRows(dtoList);
        return pageRows;
    }

    private ClientMoreAppDTO buildMoreAppDTO(ClientLineItem item) {
        ClientMoreAppDTO dto = new ClientMoreAppDTO();
        dto.setMenuId(item.getItemId());
        dto.setDesc(item.getDesc());
        dto.setTitle(item.getTitle());
        dto.setType(item.getRedirectType().getCode());
        dto.setPicurl(item.getPicUrl());
        dto.setValue(item.getUrl());
        dto.setCategoryname(StringUtil.isEmpty(item.getTypeName()) ? "" : item.getTypeName());
        dto.setCategorycolor(StringUtil.isEmpty(item.getTypeColor()) ? "" : item.getTypeColor());
        return dto;
    }

    public PageRows<NewsItemsDTO> queryNewsItems(String lineCode, String flag, Pagination pagination, JoymeAppClientConstant joymeAppClientConstant) throws ServiceException {
        PageRows<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, flag, pagination);
        if (itemList == null || CollectionUtil.isEmpty(itemList.getRows())) {
            return null;
        }
        List<NewsItemsDTO> dtoList = new LinkedList<NewsItemsDTO>();
        for (ClientLineItem item : itemList.getRows()) {
            //老版本不显示
            if (item.getRedirectType().getCode() == AppRedirectType.WEBVIEW.getCode() && joymeAppClientConstant == null) {
                continue;
            }
            NewsItemsDTO dto = buildMoreNewsItem(item);
            dtoList.add(dto);
        }
        PageRows<NewsItemsDTO> pageRows = new PageRows<NewsItemsDTO>();
        pageRows.setPage(itemList.getPage());
        pageRows.setRows(dtoList);
        return pageRows;
    }

    private NewsItemsDTO buildMoreNewsItem(ClientLineItem clientLineItem) {
        NewsItemsDTO dto = new NewsItemsDTO();
        dto.setTitle(clientLineItem.getTitle());
        dto.setPic(clientLineItem.getPicUrl());
        dto.setAuthor(clientLineItem.getAuthor());
        dto.setUrl(clientLineItem.getUrl());
        dto.setDesc(clientLineItem.getDesc());
        if (clientLineItem.getRedirectType() != null) {
            dto.setMenu_type(clientLineItem.getRedirectType().getCode());
        }
        dto.setOrder(clientLineItem.getDisplayOrder());
        dto.setDate(clientLineItem.getItemCreateDate().getTime());
        if (clientLineItem.getAppDisplayType() != null) {
            dto.setD_type(clientLineItem.getAppDisplayType().getCode());
        }
        if (!StringUtil.isEmpty(clientLineItem.getDirectId())) {
            try {
                Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.parseInt(clientLineItem.getDirectId()));
                if (archive != null) {
                    dto.setCategory(StringUtil.isEmpty(archive.getTypeName()) ? "" : archive.getTypeName());
                    dto.setCategory_color(StringUtil.isEmpty(archive.getTypeColor()) ? "" : archive.getTypeColor());
                }

                //如果有webview地址则替换
                if (!StringUtil.isEmpty(archive.getWebviewUrl()) && dto.getMenu_type() == AppRedirectType.WEBVIEW.getCode()) {
                    dto.setUrl(archive.getWebviewUrl());
                }
            } catch (Exception e) {
                GAlerter.lab("buildMoreNewsItem error.", e);
            }

        }


        if (clientLineItem.getParam() != null) {
            dto.setParam(clientLineItem.getParam());
        }
        return dto;
    }

    public List<PlatinumDTO> queryPlatinumApps(String lineCode) throws ServiceException, ParseException {
        List<PlatinumDTO> returnList = new ArrayList<PlatinumDTO>();
        List<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryClientLineItemList(lineCode);
        if (CollectionUtil.isEmpty(itemList)) {
            return returnList;
        }
        for (ClientLineItem item : itemList) {
            PlatinumDTO dto = buildPlatinumDTO(item);
            returnList.add(dto);
        }
        return returnList;
    }

    private PlatinumDTO buildPlatinumDTO(ClientLineItem item) throws ParseException {
        PlatinumDTO dto = new PlatinumDTO();
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDesc());
        dto.setPic(item.getPicUrl());
        dto.setItemId(item.getItemId());
        dto.setUrl(item.getUrl());
        dto.setSoftwareSize(item.getParam() == null ? "" : item.getParam().getSize());

        if (item.getParam() != null && !StringUtil.isEmpty(item.getParam().getStartDate()) && !StringUtil.isEmpty(item.getParam().getEndDate())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(item.getParam().getStartDate());
            Date end = format.parse(item.getParam().getEndDate());
            dto.setStartDate(start);
            dto.setEndDate(end);
        }

//		dto.setHot(item.getDisplayIcon());
        return dto;
    }


    public Map<String, OtherInfoModuleDateDTO> buildOtherModuleDataDTOMap(JoymeAppMenu menuData) {
        if (menuData == null) {
            return null;
        }
        Map<String, OtherInfoModuleDateDTO> map = new HashMap<String, OtherInfoModuleDateDTO>();
        OtherInfoModuleDateDTO moduleDateDTO = new OtherInfoModuleDateDTO();
        moduleDateDTO.setDesc(menuData.getMenuDesc());
        moduleDateDTO.setJt(String.valueOf(menuData.getMenuType()));
        moduleDateDTO.setJi(menuData.getUrl());
        if (!map.containsKey(menuData.getMenuName())) {
            map.put(menuData.getMenuName(), moduleDateDTO);
        }
        return map;
    }

    public NewsModuleDTO buildNewsModuleDTO(JoymeAppMenu menu, List<JoymeAppMenu> newsList) {
        if (menu == null || CollectionUtil.isEmpty(newsList)) {
            return null;
        }
        NewsModuleDTO moduleDTO = new NewsModuleDTO();
        moduleDTO.setName(menu.getMenuName());
        List<NewsModuleDataDTO> list = new ArrayList<NewsModuleDataDTO>();
        for (JoymeAppMenu menuData : newsList) {
            if (menuData != null) {
                NewsModuleDataDTO moduleDataDTO = buildNewsModuleDataDTO(menuData);
                if (moduleDataDTO != null) {
                    list.add(moduleDataDTO);
                }
            }
        }
        moduleDTO.setList(list);
        return moduleDTO;
    }

    private NewsModuleDataDTO buildNewsModuleDataDTO(JoymeAppMenu menuData) {
        if (menuData == null) {
            return null;
        }
        NewsModuleDataDTO moduleDataDTO = new NewsModuleDataDTO();
        moduleDataDTO.setTitle(menuData.getMenuName());
        moduleDataDTO.setJt(String.valueOf(menuData.getMenuType()));
        moduleDataDTO.setJi(menuData.getUrl());
        return moduleDataDTO;
    }

    public ClientModuleDTO buildClientModuleDTO(JoymeAppMenu menu, List<JoymeAppMenu> headList, int platform) {
        if (menu == null || CollectionUtil.isEmpty(headList)) {
            return null;
        }
        ClientModuleDTO moduleDTO = new ClientModuleDTO();
        moduleDTO.setName(menu.getMenuName());
        List<ClientModuleDataDTO> list = new ArrayList<ClientModuleDataDTO>();
        for (JoymeAppMenu menuData : headList) {
            if (menuData != null) {
                ClientModuleDataDTO moduleDataDTO = buildClientModuleDataDTO(menuData, platform);
                if (moduleDataDTO != null) {
                    list.add(moduleDataDTO);
                }
            }
        }
        moduleDTO.setList(list);
        return moduleDTO;
    }

    public ClientModuleDataDTO buildClientModuleDataDTO(JoymeAppMenu menuData, int platform) {
        if (menuData == null) {
            return null;
        }
        ClientModuleDataDTO moduleDataDTO = new ClientModuleDataDTO();
        moduleDataDTO.setMenuid(menuData.getMenuId());
        moduleDataDTO.setTitle(menuData.getMenuName());
        moduleDataDTO.setDesc(menuData.getMenuDesc());
        if (AppPlatform.IOS.equals(AppPlatform.getByCode(platform))) {
            moduleDataDTO.setPicurl(menuData.getPic().getIosPic());
        } else if (AppPlatform.ANDROID.equals(AppPlatform.getByCode(platform))) {
            moduleDataDTO.setPicurl(menuData.getPic().getAndroidPic());
        }
        moduleDataDTO.setContenttype(String.valueOf(menuData.getContentType().getCode()));
        if (menuData.getExpField() != null) {
            moduleDataDTO.setExpdesc(menuData.getExpField().getExpDesc());
            moduleDataDTO.setStar(menuData.getExpField().getStar());
            moduleDataDTO.setAuthor(menuData.getExpField().getAuthor());
            if (!StringUtil.isEmpty(menuData.getExpField().getPublishDate())) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date publishDate = df.parse(menuData.getExpField().getPublishDate());
                    if (publishDate != null) {
                        moduleDataDTO.setPublishdate(DateUtil.parseDate(publishDate));
                    }
                } catch (ParseException e) {
                }
            }
        }
        moduleDataDTO.setIshot(String.valueOf(menuData.isHot()));
        moduleDataDTO.setIsnew(String.valueOf(menuData.isNew()));
        moduleDataDTO.setDisplaytype(String.valueOf(menuData.getDisplayType().getCode()));
        moduleDataDTO.setJi(String.valueOf(menuData.getMenuType()));
        moduleDataDTO.setJi(menuData.getUrl());
        moduleDataDTO.setTimestamp(menuData.getLastModifyDate() == null ? menuData.getCreateDate().getTime() : menuData.getLastModifyDate().getTime());
        return moduleDataDTO;
    }
}
