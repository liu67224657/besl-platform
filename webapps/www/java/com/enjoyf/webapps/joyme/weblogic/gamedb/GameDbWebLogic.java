package com.enjoyf.webapps.joyme.weblogic.gamedb;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.gamedb.GameClientGameNewsDTO;
import com.enjoyf.webapps.joyme.dto.gamedb.GameClientRelationDTO;
import com.enjoyf.webapps.joyme.dto.gamedb.GameDBSimpleDTO;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
@Service(value = "gameDbWebLogic")
public class GameDbWebLogic {

    public Map<String, GameDBSimpleDTO> queryGameDTOByGameDBIds(Set<Long> gameDbIds, AppPlatform appPlatform, String channel) throws ServiceException {
        Map<String, GameDBSimpleDTO> returnObj = new HashMap<String, GameDBSimpleDTO>();

        //todo cache 性能优化
        GameDBChannel gameDBChannel = null;
        if (appPlatform != null && StringUtil.isEmpty(channel)) {
            gameDBChannel = GameResourceServiceSngl.get().getGameDbChannel(channel);

        }

        Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameDbIds);
        for (GameDB gameDb : gameDBMap.values()) {
            com.enjoyf.webapps.joyme.dto.gamedb.GameDBSimpleDTO gameDTO = buildGameDTO(gameDb, appPlatform, gameDBChannel, null);
            if (gameDTO != null) {
                returnObj.put(gameDTO.getGameid(), gameDTO);
            }
        }

        return returnObj;
    }

    public GameDBSimpleDTO buildGameDTO(GameDB gameDb, AppPlatform appPlatform, GameDBChannel gameDBChannel, Profile profile) throws ServiceException {
        GameDBSimpleDTO gameDTO = new GameDBSimpleDTO();

        gameDTO.setGameid(String.valueOf(gameDb.getGameDbId()));
        gameDTO.setIconurl(StringUtil.isEmpty(gameDb.getGameIcon()) ? "" : gameDb.getGameIcon());
        gameDTO.setName(StringUtil.isEmpty(gameDb.getGameName()) ? "" : gameDb.getGameName());
        String desc = "";
        if (gameDb.getGameDBCover() != null) {
            desc = StringUtil.isEmpty(gameDb.getGameDBCover().getCoverComment()) ? "" : gameDb.getGameDBCover().getCoverComment();
        }
        gameDTO.setDesc(desc);
        gameDTO.setReason((StringUtil.isEmpty(gameDb.getDownloadRecommend()) ? "" : gameDb.getDownloadRecommend()));

        if (gameDBChannel != null && appPlatform != null) {
            //gameDTO.setDownlink(GameDBUtils.getGameDownload(gameDb, appPlatform, channelId));
            gameDTO.setDownlink(getDownLink(appPlatform.getCode() + "", gameDb, gameDBChannel));
        }

        long modfigyTime = 0;
        if (gameDb.getModifyTime() != null) {
            modfigyTime = gameDb.getModifyTime().getGiftlastModifyTime() > gameDb.getModifyTime().getLastModifyTime() ? gameDb.getModifyTime().getGiftlastModifyTime() : gameDb.getModifyTime().getLastModifyTime();
            gameDTO.setRedMessageType(gameDb.getModifyTime().getRedMessageType());
            gameDTO.setRedMessageText(gameDb.getModifyTime().getRedMessageText());
            gameDTO.setRedMessageTime(gameDb.getModifyTime().getRedMessageTime());
        }
        gameDTO.setModifytime(modfigyTime);
        gameDTO.setPublishtime(0L);
        if (!CollectionUtil.isEmpty(gameDb.getCategoryTypeSet())) {
            List<String> tagList = new ArrayList<String>();
            for (GameCategoryType categoryType : gameDb.getCategoryTypeSet()) {
                tagList.add(categoryType.getValue());
            }
            gameDTO.setTags(tagList);
        }


        String rate = average(gameDb);
        if (StringUtil.isEmpty(rate)) {
            rate = String.valueOf(gameDb.getGameRate());
        }
        gameDTO.setRate(rate);
        gameDTO.setDottype(String.valueOf(gameDb.getDisplayIcon()));


        Set<String> directIdSet = new HashSet<String>();
        directIdSet.add(String.valueOf(gameDb.getGameDbId()));
        if (profile != null) {
            Map<String, ObjectRelation> gameRelationMap = SocialServiceSngl.get().checkObjectRelation(profile.getProfileId(), ObjectRelationType.GAME, directIdSet);
            ObjectRelation obj = gameRelationMap.get(String.valueOf(gameDb.getGameDbId()));
            if (obj != null && obj.getStatus().equals(IntValidStatus.VALID)) {
                gameDTO.setFollow("true");
            } else {
                gameDTO.setFollow("false");
            }
        }

        gameDTO.setJt(AppRedirectType.REDIRECT_WEBVIEW.getCode() + "");
        gameDTO.setJi("http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/cover?gameid=" + gameDb.getGameDbId());

        return gameDTO;
    }


    public List<GameClientRelationDTO> queryRelations(long gameDBId) throws ServiceException {
        List<GameDBRelation> relationList = GameResourceServiceSngl.get().queryGameDBRelationbyGameDbId(gameDBId, true);


//        Collections.sort(relationList, new Comparator<GameDBRelation>() {
//            @Override
//            public int compare(GameDBRelation o1, GameDBRelation o2) {
//                int jt1 = Integer.valueOf(o1.getDisplayOrder());
//                int jt2 = Integer.valueOf(o2.getDisplayOrder());
//                return jt1 > jt2 ? 1 : (o1 == o2 ? 0 : -1);
//            }
//        });

        List<GameClientRelationDTO> dtoList = new ArrayList<GameClientRelationDTO>();
        for (GameDBRelation relation : relationList) {
            GameClientRelationDTO dto = buildGameClientDBRelation(relation);
            if (dto != null) {
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    public PageRows<GameClientGameNewsDTO> buildGameNewsDTO(String gameId, Pagination pagination) {
        String lineCode = "gamedb_news_" + gameId;
        PageRows<GameClientGameNewsDTO> rows = new PageRows<GameClientGameNewsDTO>();
        try {
            PageRows<ClientLineItem> clientLineItems = JoymeAppServiceSngl.get().queryItemsByLineCode(lineCode, "", pagination);

            List<GameClientGameNewsDTO> list = new ArrayList<GameClientGameNewsDTO>();
            for (ClientLineItem item : clientLineItems.getRows()) {
                list.add(buildGameNewsDTO(item));
            }
            rows.setRows(list);
            rows.setPage(clientLineItems.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }

        return rows;
    }

    private GameClientGameNewsDTO buildGameNewsDTO(ClientLineItem item) {
        GameClientGameNewsDTO dto = new GameClientGameNewsDTO();

        dto.setCategory(item.getCategory());
        dto.setCategoryColor(item.getCategoryColor());
        dto.setCreatetime(item.getItemCreateDate());
        dto.setDesc(item.getDesc());
        dto.setJt(item.getRedirectType().getCode());
        dto.setTitle(item.getTitle());
        dto.setJi(item.getUrl());

        return dto;
    }


    /**
     * 创建gameclient的relation dto
     *
     * @param gameDBRelation
     * @return
     * @throws ServiceException
     */
    public GameClientRelationDTO buildGameClientDBRelation(GameDBRelation gameDBRelation) throws ServiceException {

        GameClientRelationDTO gameClientRelationDTO = new GameClientRelationDTO();
        gameClientRelationDTO.setTitle(gameDBRelation.getTitle());
        if (GameDBRelationType.DETAIL.equals(gameDBRelation.getType())) {
            gameClientRelationDTO.setJi("http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/tab/detail?gameid=" + gameDBRelation.getGamedbId());
            gameClientRelationDTO.setJt(AppRedirectType.REDIRECT_GAMEDETAIL.getCode() + "");
            gameClientRelationDTO.setRelationid(gameDBRelation.getRelationId() + "");
        } else if (GameDBRelationType.GIFT.equals(gameDBRelation.getType())) {
            gameClientRelationDTO.setJi("http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/tab/giftlist?gameid=" + gameDBRelation.getGamedbId());
            gameClientRelationDTO.setJt(AppRedirectType.REDIRECT_GIFTMARKET.getCode() + "");
            gameClientRelationDTO.setRelationid(gameDBRelation.getRelationId() + "");
        } else if (GameDBRelationType.NEWS.equals(gameDBRelation.getType())) {
            gameClientRelationDTO.setJi("http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/tab/gamenews?gameid=" + gameDBRelation.getGamedbId());
            gameClientRelationDTO.setJt(AppRedirectType.DEFAULT_WEBVIEW.getCode() + "");
            gameClientRelationDTO.setRelationid(gameDBRelation.getRelationId() + "");
        } else if (GameDBRelationType.LINK.equals(gameDBRelation.getType())) {
            gameClientRelationDTO.setJi(gameDBRelation.getUri());
            gameClientRelationDTO.setJt(AppRedirectType.CLICK_LIKEGAME.getCode() + "");
            gameClientRelationDTO.setRelationid(gameDBRelation.getRelationId() + "");
        } else {
            GAlerter.lab(getClass().getName() + " buildGameCliengDBRelation type error " + gameDBRelation.getRelationId());
        }

        return gameClientRelationDTO;
    }


    public String average(GameDB gameDB) {
        String returnObj = "";
        GameDBCoverFieldJson fieldJson = gameDB.getGameDBCoverFieldJson();
        if (fieldJson != null) {
            try {
                double sum = Double.valueOf(fieldJson.getValue1()) + Double.valueOf(fieldJson.getValue2()) + Double.valueOf(fieldJson.getValue3()) + Double.valueOf(fieldJson.getValue4()) + Double.valueOf(fieldJson.getValue5());
                DecimalFormat df = new DecimalFormat("0.0");
                returnObj = df.format(sum / 5.0);
            } catch (Exception e) {
                GAlerter.lab(getClass().getName() + " average type error ", e);
            }
        }
        return returnObj;
    }

    //        =<5 的显示： 渣渣
//        5< 且=<8的：一般
//        8> 且 <9的：好玩
//        9=<的显示 ：精品
    public String averageValue(String average) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(average)) {
            return "";
        }
        double returnObj = Double.valueOf(average);
        if (returnObj <= 5) {
            return "渣渣";
        } else if (returnObj > 5 && returnObj <= 8) {
            return "一般";
        } else if (returnObj > 8 && returnObj < 9) {
            return "好玩";
        } else if (returnObj >= 9) {
            return "精品";
        }
        return "";
    }

    public String getDownLink(String platform, GameDB gameDB, GameDBChannel gameDBChannel) {
        String downLink = "";
        boolean isExistChannel = false;
        if (!StringUtil.isEmpty(platform)) {
            AppPlatform appPlatform = AppPlatform.getByCode(Integer.valueOf(platform));
            if ((appPlatform.equals(AppPlatform.ANDROID) && "true".equals(gameDB.getGameDBCover().getCoverDownloadAndroid())) || (appPlatform.equals(AppPlatform.IOS) && "true".equals(gameDB.getGameDBCover().getCoverDownload()))) {
                Set<GameDBChannelInfo> set = gameDB.getChannelInfoSet();
                for (GameDBChannelInfo gameDBChannelInfo : set) {
                    if (GameDBUtils.getGameDbDeviceByPlatform(appPlatform).equals(gameDBChannelInfo.getDevice()) && gameDBChannel != null && gameDBChannel.getChannelId() == Long.valueOf(gameDBChannelInfo.getChannel_id()) && !StringUtil.isEmpty(gameDBChannelInfo.getChannel_id())) {
                        downLink = gameDBChannelInfo.getChannelDownloadInfo().getDownload();
                        if (StringUtil.isEmpty(downLink)) {
                            isExistChannel = true;
                        }
                        break;
                    }
                }
            }
        }

        //渠道包下载地址维护 ios默认是appstroe android是joyme，如果配置了渠道，但是链接地址是空，则不显示
        if (StringUtil.isEmpty(downLink) && !isExistChannel) {
            AppPlatform appPlatform = AppPlatform.getByCode(Integer.valueOf(platform));
            try {
                if (appPlatform.equals(AppPlatform.IOS)) {
                    gameDBChannel = GameResourceServiceSngl.get().getGameDbChannel(AppChannelType.APPSTORE.getCode());
                } else {
                    gameDBChannel = GameResourceServiceSngl.get().getGameDbChannel(AppChannelType.JOYME.getCode());
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            if ((appPlatform.equals(AppPlatform.ANDROID) && "true".equals(gameDB.getGameDBCover().getCoverDownloadAndroid())) || (appPlatform.equals(AppPlatform.IOS) && "true".equals(gameDB.getGameDBCover().getCoverDownload()))) {
                Set<GameDBChannelInfo> set = gameDB.getChannelInfoSet();
                for (GameDBChannelInfo gameDBChannelInfo : set) {
                    if (GameDBUtils.getGameDbDeviceByPlatform(appPlatform).equals(gameDBChannelInfo.getDevice()) && gameDBChannel != null && gameDBChannel.getChannelId() == Long.valueOf(gameDBChannelInfo.getChannel_id())) {
                        downLink = gameDBChannelInfo.getChannelDownloadInfo().getDownload();
                        break;
                    }
                }
            }
        }
        return downLink;
    }

    //游戏封面
    public boolean gameCoverExistGift(GameDB gameDB) {
        boolean exist = false;
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gameDB.getGameDbId()));
        queryExpress.add(QueryCriterions.eq(GameDBRelationField.TYPE, GameDBRelationType.GIFT.getCode()));
        queryExpress.add(QueryCriterions.eq(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode()));
        try {
            GameDBRelation gameDBRelation = GameResourceServiceSngl.get().getGameDbRelation(queryExpress);
            if (gameDBRelation != null) {
                exist = true;
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return exist;
    }

}
