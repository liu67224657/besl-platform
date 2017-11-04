package com.enjoyf.webapps.joyme.weblogic.joymeapp;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.joymeapp.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Service(value = "joymeAppWebLogic")
public class JoymeAppWebLogic {

    private static final int PLATFORM_IOS = 0;

    private static final int PLATFORM_ANDORID = 1;


    public List<JoymeAppTopMenuDTO> queryTopMenuListByAppKey(String appkey, String channelCode) throws ServiceException {

        List<JoymeAppTopMenuDTO> returnList = new ArrayList<JoymeAppTopMenuDTO>();

        int platform = getPlatformByAppKey(appkey);
        appkey = getAppKey(appkey);

        List<JoymeAppTopMenu> topMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByAppKey(appkey);


        for (JoymeAppTopMenu top : topMenuList) {
            String name = top.getMenuName();
            String desc = top.getMenuDesc() == null ? "" : top.getMenuDesc();
            String url = top.getUrl();
            String picUrl = "";
            if (platform == PLATFORM_ANDORID && !StringUtil.isEmpty(top.getPicUrl2())) {
                picUrl = top.getPicUrl2();
            } else {
                picUrl = top.getPicUrl1();
            }

            //如果需相关渠道不为空
            if (!StringUtil.isEmpty(channelCode)) {
                ChannelTopMenu channelTopMenu = getChannelTopMenu(top, channelCode, platform);
                if (channelTopMenu != null) {
                    name = channelTopMenu.getName();
                    desc = channelTopMenu.getDesc() == null ? "" : channelTopMenu.getDesc();
                    url = channelTopMenu.getUrl();
                    picUrl = channelTopMenu.getPicUrl();
                }
            }

            JoymeAppTopMenuDTO dto = new JoymeAppTopMenuDTO();
            dto.setMenuId(top.getMenuId());
            dto.setMenuName(name);
            dto.setMenuDesc(desc);
            dto.setUrl(url);
            dto.setDisplay_order(top.getDisplay_order());
            dto.setHot(top.isHot());
            dto.setNew(top.isNew());
            dto.setMenuId(top.getMenuId());
            dto.setMenuType(top.getMenuType());
            dto.setPicUrl(picUrl);
            returnList.add(dto);
        }
        return returnList;
    }

    private ChannelTopMenu getChannelTopMenu(JoymeAppTopMenu topMenu, String channelCode, int platform) {
        ChannelTopMenu returnObj = null;

        ChannelTopMenuSet channelTopMenuSet = topMenu.getChannelTopMenuSet();
        if (channelTopMenuSet == null || CollectionUtil.isEmpty(channelTopMenuSet.getChannelTopMenuSet())) {
            return returnObj;
        }

        Map<String, ChannelTopMenu> channelMap = new HashMap<String, ChannelTopMenu>();
        for (ChannelTopMenu channelTopMenu : channelTopMenuSet.getChannelTopMenuSet()) {
            channelMap.put(channelTopMenu.getChannelCode() + channelTopMenu.getPlatform(), channelTopMenu);
        }

        returnObj = channelMap.get(channelCode + platform);

        return returnObj;
    }

    public List<JoymeAppBottomMenuDTO> queryBottomMenuListByAppKey(String appkey, long pid) throws ServiceException {

        List<JoymeAppBottomMenuDTO> returnList = new ArrayList<JoymeAppBottomMenuDTO>();

        appkey = getAppKey(appkey);
        List<JoymeAppMenu> joymeAppMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appkey, pid, JoymeAppMenuModuleType.DEFAULT);

        for (JoymeAppMenu menu : joymeAppMenuList) {
            JoymeAppBottomMenuDTO dto = new JoymeAppBottomMenuDTO();

            dto.setMenuName(menu.getMenuName());
            dto.setMenuDesc(menu.getMenuDesc() == null ? "" : menu.getMenuDesc());
            dto.setUrl(menu.getUrl());
            dto.setDisplay_order(menu.getDisplay_order());
            dto.setHot(menu.isHot());
            dto.setNew(menu.isNew());
            dto.setMenuId(menu.getMenuId());
            dto.setMenuType(menu.getMenuType());
            dto.setPicUrl(menu.getPicUrl());
            dto.setMenuId(menu.getMenuId());
            dto.setParentId(menu.getParentId());
            dto.setDisplay_type(menu.getDisplayType().getCode());
            dto.setTagid(menu.getTagId());
            dto.setStatusDesc(menu.getStatusDesc() == null ? "" : menu.getStatusDesc());
            dto.setStar(menu.getRecommendStar());
            dto.setContenttype(menu.getContentType() == null ? 0 : menu.getContentType().getCode());
            dto.setUpdatetime(menu.getLastModifyDate() == null ? 0 : menu.getLastModifyDate().getTime());
            returnList.add(dto);
        }
        return returnList;
    }

    public List<JoymeAppBottomMenuDTO> queryBottomMenuListByTagid(long tid) throws ServiceException {

        List<JoymeAppBottomMenuDTO> returnList = new ArrayList<JoymeAppBottomMenuDTO>();

        List<JoymeAppMenu> joymeAppMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByTagId(tid);

        for (JoymeAppMenu menu : joymeAppMenuList) {
            JoymeAppBottomMenuDTO dto = new JoymeAppBottomMenuDTO();

            dto.setMenuName(menu.getMenuName());
            dto.setMenuDesc(menu.getMenuDesc() == null ? "" : menu.getMenuDesc());
            dto.setUrl(menu.getUrl());
            dto.setDisplay_order(menu.getDisplay_order());
            dto.setHot(menu.isHot());
            dto.setNew(menu.isNew());
            dto.setMenuId(menu.getMenuId());
            dto.setMenuType(menu.getMenuType());
            dto.setPicUrl(menu.getPicUrl());
            dto.setMenuId(menu.getMenuId());
            dto.setParentId(menu.getParentId());
            dto.setDisplay_type(menu.getDisplayType().getCode());

            returnList.add(dto);
        }
        return returnList;
    }


    public List<JoymeAppMenuTagDTO> queryMenuTags(long pid) throws ServiceException {
        List<JoymeAppMenuTagDTO> returnList = new ArrayList<JoymeAppMenuTagDTO>();

        List<JoymeAppMenuTag> tagList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuTagByMenuId(pid);

        for (JoymeAppMenuTag tag : tagList) {
            JoymeAppMenuTagDTO dto = new JoymeAppMenuTagDTO();

            dto.setMenuid(pid);
            dto.setTagid(tag.getTagId());
            dto.setTagname(tag.getTagName());

            returnList.add(dto);
        }
        return returnList;
    }

    ////////////////////////////////////
    private static String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

    private int getPlatformByAppKey(String appKey) {
        if (appKey.length() < 23) {
            return -1;
        } else if (appKey.endsWith("I")) {
            return 0;
        } else if (appKey.endsWith("A")) {
            return 1;
        }

        return -1;
    }

    public List<JoymeAppTopMenuDTO> queryActivityTopMenuList(String appKey, String channelCode, int platform, String flag) throws ServiceException {
        List<ActivityTopMenu> topMenuList = JoymeAppConfigServiceSngl.get().queryActivityTopMenuCache(platform, appKey, flag);
        Map<String, List<JoymeAppTopMenuDTO>> map = new LinkedHashMap<String, List<JoymeAppTopMenuDTO>>();
        for (ActivityTopMenu top : topMenuList) {
            JoymeAppTopMenuDTO dto = new JoymeAppTopMenuDTO();
            dto.setMenuId(top.getActivityTopMenuId());
            dto.setMenuName(top.getMenuName());
            dto.setMenuDesc(top.getMenuDesc());
            dto.setPicUrl(top.getPicUrl());
            dto.setUrl(top.getLinkUrl());
            dto.setDisplay_order(top.getDisplayOrder());
            dto.setHot(top.getIsHot());
            dto.setNew(top.getIsNew());
            dto.setMenuType(top.getMenuType());
            if (!map.containsKey(top.getAppKey() + top.getChannelId())) {
                map.put(top.getAppKey() + top.getChannelId(), new ArrayList<JoymeAppTopMenuDTO>());
            }
            map.get(top.getAppKey() + top.getChannelId()).add(dto);
        }
        return map.get(getAppKey(appKey) + getChannelId(channelCode));
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

    public List<JoymeAppTopMenuDTO> queryActivityHeadImageList(String appKey, String channelCode, String platform, String flag) throws ServiceException {
        List<ActivityTopMenu> topMenuList = JoymeAppConfigServiceSngl.get().queryActivityTopMenuCache(Integer.parseInt(platform), appKey, flag);
        Map<String, List<JoymeAppTopMenuDTO>> map = new LinkedHashMap<String, List<JoymeAppTopMenuDTO>>();
        for (ActivityTopMenu top : topMenuList) {
            JoymeAppTopMenuDTO dto = new JoymeAppTopMenuDTO();
            dto.setMenuId(top.getActivityTopMenuId());
            dto.setMenuName(top.getMenuName());
            dto.setMenuDesc(top.getMenuDesc());
            dto.setPicUrl(top.getPicUrl());
            dto.setUrl(top.getLinkUrl());
            dto.setDisplay_order(top.getDisplayOrder());
            dto.setHot(top.getIsHot());
            dto.setNew(top.getIsNew());
            dto.setMenuType(top.getRedirectType());
            dto.setAuthor(top.getParam() == null ? null : top.getParam().getAuthor());
            dto.setColor(top.getParam() == null ? null : top.getParam().getPicColor());

            try {
                if (top.getParam() != null && !StringUtil.isEmpty(top.getParam().getPublishDate())) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date publishDate = df.parse(top.getParam().getPublishDate());
                    dto.setPublishTime(publishDate.getTime());
                }
            } catch (ParseException e) {
                GAlerter.lab(this.getClass().getName() + " ParseException");
            }

            if (!map.containsKey(top.getAppKey() + top.getChannelId())) {
                map.put(top.getAppKey() + top.getChannelId(), new ArrayList<JoymeAppTopMenuDTO>());
            }
            map.get(top.getAppKey() + top.getChannelId()).add(dto);
        }
        return map.get(getAppKey(appKey) + getChannelId(channelCode));
    }

    public List<AppAdvertiseDTO> queryAppAdvertiseList(String appId, AppAdvertisePublishType publishType, int platform, String channel) throws ServiceException {
        List<AppAdvertiseDTO> returnList = new ArrayList<AppAdvertiseDTO>();
        List<AppAdvertisePublish> publishList = AdvertiseServiceSngl.get().queryAppAdvertisePublishByCache(appId, publishType, channel);


        Set<Long> advertiseIdSet = new HashSet<Long>();
        Map<Long, AppAdvertisePublish> appAdvertisePublishMap = new HashMap<Long, AppAdvertisePublish>();
        for (AppAdvertisePublish publish : publishList) {
            advertiseIdSet.add(publish.getAdvertiseId());
            appAdvertisePublishMap.put(publish.getAdvertiseId(), publish);
        }


        Map<Long, AppAdvertise> advertiseMap = AdvertiseServiceSngl.get().queryAppAdvertiseByIdSet(advertiseIdSet, platform);

        for (AppAdvertisePublish publish : publishList) {
            AppAdvertise advertise = advertiseMap.get(publish.getAdvertiseId());
            if (advertise == null) {
                continue;
            }
            AppAdvertiseDTO appAdvertiseDTO = buildAppAdvertiseDTO(advertise, publish, appAdvertisePublishMap);
            returnList.add(appAdvertiseDTO);
        }
        return returnList;
    }

    private AppAdvertiseDTO buildAppAdvertiseDTO(AppAdvertise advertise, AppAdvertisePublish publish, Map<Long, AppAdvertisePublish> appAdvertisePublishMap) {
        AppAdvertiseDTO appAdvertiseDTO = new AppAdvertiseDTO();
        appAdvertiseDTO.setAdvertiseid(advertise.getAdvertiseId());
        appAdvertiseDTO.setName(advertise.getAdvertiseName());
        appAdvertiseDTO.setDescription(advertise.getAdvertiseDesc());
        appAdvertiseDTO.setIos4pic(advertise.getPicUrl1());
        appAdvertiseDTO.setIos5pic(advertise.getPicUrl2());
        if ("0VsYSLLsN8CrbBSMUOlLNx".equals(publish.getAppkey())) {
            if (StringUtil.isEmpty(advertise.getUrl().trim())) {
                appAdvertiseDTO.setRurl("");
            } else {
                appAdvertiseDTO.setRurl(WebappConfig.get().getUrlWww() + "/joymeapp/app/advertise/click/" + publish.getPublishId() + "/" + advertise.getAdvertiseId());
            }
        } else {
            appAdvertiseDTO.setRurl(advertise.getUrl());
        }
        appAdvertiseDTO.setIndex(publish.getPublishParam().getNumberParam());
        appAdvertiseDTO.setPublishId(publish.getPublishId());
        appAdvertiseDTO.setRedirecttype(advertise.getAppAdvertiseRedirectType());


        //joymewiki app
        if (advertise.getAppAdvertiseRedirectType() == WanbaJt.WAP_WIKI.getCode()) {
            appAdvertiseDTO.setRedirecttype(-2);
        }

        appAdvertiseDTO.setViewtime(publish.getPublishParam().getLongtime());
        AppAdvertisePublish advertisePublish = appAdvertisePublishMap.get(advertise.getAdvertiseId());
        if (advertisePublish != null) {
            appAdvertiseDTO.setStarttime(String.valueOf(advertisePublish.getStartTime().getTime()));
            appAdvertiseDTO.setEndtime(String.valueOf(advertisePublish.getEndTime().getTime()));
        }
        return appAdvertiseDTO;
    }

    public AppTipsDTO getLastAppTips(String appKey, int platform, String clientId, String clientToken) throws ServiceException {
        AppTips tips = JoymeAppConfigServiceSngl.get().getAppTipsByCache(appKey, platform);
        if (tips == null) {
            return null;
        }
        AppTipsDTO dto = buildAppTipsDTO(tips);
        return dto;
    }

    private AppTipsDTO buildAppTipsDTO(AppTips tips) {
        AppTipsDTO dto = new AppTipsDTO();
        dto.setDescription(tips.getTipsDescription());
        dto.setTipsid(tips.getTipsId());
        dto.setUpdatetime(tips.getUpdateTime() == null ? 0l : tips.getUpdateTime().getTime());
        return dto;
    }

    public List<JoymeAppBottomMenuDTO> queryBottomMenuByModule(String appkey, JoymeAppMenuModuleType module) throws ServiceException {
        List<JoymeAppBottomMenuDTO> returnList = null;
        appkey = getAppKey(appkey);
        List<JoymeAppMenu> menuList = JoymeAppConfigServiceSngl.get().queryJoymeAppMenuByCache(appkey, 0l, module);
        if (!CollectionUtil.isEmpty(menuList)) {
            returnList = new ArrayList<JoymeAppBottomMenuDTO>();
            for (JoymeAppMenu menu : menuList) {
                JoymeAppBottomMenuDTO dto = new JoymeAppBottomMenuDTO();
                dto.setMenuName(menu.getMenuName());
                dto.setMenuDesc(menu.getMenuDesc() == null ? "" : menu.getMenuDesc());
                dto.setUrl(menu.getUrl());
                dto.setDisplay_order(menu.getDisplay_order());
                dto.setHot(menu.isHot());
                dto.setNew(menu.isNew());
                dto.setMenuId(menu.getMenuId());
                dto.setMenuType(menu.getMenuType());
                dto.setPicUrl(menu.getPicUrl());
                dto.setMenuId(menu.getMenuId());
                dto.setParentId(menu.getParentId());
                dto.setDisplay_type(menu.getDisplayType().getCode());
                dto.setTagid(menu.getTagId());
                dto.setStatusDesc(menu.getStatusDesc() == null ? "" : menu.getStatusDesc());
                dto.setStar(menu.getRecommendStar());
                dto.setContenttype(menu.getContentType() == null ? 0 : menu.getContentType().getCode());

                returnList.add(dto);
            }
        }
        return returnList;
    }

    public PageRows<NotificationDTO> queryJoymeAppPushMessage(String appKey, int platform, Pagination pagination, long timestamp, PushListType pushListType) throws ServiceException {
        PageRows<NotificationDTO> pageRows = new PageRows<NotificationDTO>();
        pageRows.setPage(pagination);
        List<NotificationDTO> returnList = new ArrayList<NotificationDTO>();
        List<PushMessage> pushMessageList = MessageServiceSngl.get().queryPushMessageByCache(appKey, platform, "", timestamp, pushListType);
        if (CollectionUtil.isEmpty(pushMessageList)) {
            pagination.setTotalRows(0);
            return pageRows;
        }
        //分页
        int start = ((pagination.getCurPage() - 1) * pagination.getPageSize());
        int end = (pagination.getCurPage() * pagination.getPageSize()) - 1;
        for (int i = start; i <= end; i++) {
            if (i > pushMessageList.size() - 1) {
                break;
            }
            PushMessage pushMessage = pushMessageList.get(i);
            NotificationDTO dto = new NotificationDTO();
            dto.setMsgid(pushMessage.getPushMsgId() + "");
            dto.setTitle(pushMessage.getMsgSubject());
            dto.setDesc(pushMessage.getShortMessage());
            //dto.setIcon(pushMessage.getMsgIcon());
            if (pushMessage.getOptions() != null && !CollectionUtil.isEmpty(pushMessage.getOptions().getList())) {
                dto.setJt(pushMessage.getOptions().getList().get(0).getType() + "");
                dto.setJi(pushMessage.getOptions().getList().get(0).getInfo());
            }
            dto.setTime(pushMessage.getSendDate() == null ? 0 + "" : pushMessage.getSendDate().getTime() + "");
            returnList.add(dto);
        }
        pageRows.setPage(new Pagination(pushMessageList.size(), pagination.getCurPage(), pagination.getPageSize()));
        pageRows.setRows(returnList);
        return pageRows;
    }
}
