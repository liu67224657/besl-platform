package com.enjoyf.webapps.tools.weblogic.gameres;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.dto.GroupRoleDTO;
import com.enjoyf.webapps.tools.weblogic.dto.game.GamePrivacyDTO;
import com.enjoyf.webapps.tools.weblogic.dto.game.GameRelationDTO;
import com.enjoyf.webapps.tools.weblogic.dto.game.GroupRolePrivilegeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 后台条目管理 WebLogic
 *
 * @author litaijun
 */
@Service(value = "gameResourceWebLogic")
public class GameResourceWebLogic {
    //
    Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    public boolean isGameResourceNameExist(String resourceName, String resourceDomain, long resourceIdItself) throws ServiceException {
        //todo 通过sql语句判断 and resourceId<>xxxx
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCENAME, resourceName));
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, resourceDomain));

        GameResource gameResource = GameResourceServiceSngl.get().getGameResource(getExpress);

        return resourceIdItself == 0 ? gameResource != null : (gameResource != null && (gameResource.getResourceId() != resourceIdItself));
    }

    public Map<String, Boolean> isSynonymsesExist(Set<String> synonymses, long resourceId) throws ServiceException {
        Map<String, Boolean> returnMap = new java.util.HashMap<String, Boolean>();
        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, resourceId));
        GameResource gameResource = GameResourceServiceSngl.get().getGameResource(getExpress);

        //
        Map<String, List<GameResource>> map = GameResourceServiceSngl.get().queryGameResourceMapBySynonymses(synonymses);

        for (Map.Entry<String, List<GameResource>> entry : map.entrySet()) {
            List<GameResource> gameResourceList = entry.getValue();
            returnMap.put(entry.getKey(),
                    gameResourceList.size() != 0 && (gameResourceList.size() != 1 || !gameResourceList.get(0).equals(gameResource)));
        }

        return returnMap;
    }

    public List<String> getOtherGameResourceName(Set<String> synonymses, long resourceId) {
        List<String> otherGameResourceNames = new ArrayList<String>();
        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, resourceId));

        try {

            GameResource gameResource = GameResourceServiceSngl.get().getGameResource(getExpress);
            Map<String, List<GameResource>> map = GameResourceServiceSngl.get().queryGameResourceMapBySynonymses(synonymses);

            for (Map.Entry<String, List<GameResource>> entry : map.entrySet()) {
                List<GameResource> gameResourceList = entry.getValue();
                if (gameResourceList.size() > 0) {
                    for (GameResource gr : gameResourceList) {
                        if (!gr.equals(gameResource)) {
                            otherGameResourceNames.add(gr.getResourceName());
                        }
                    }
                }
            }

        } catch (ServiceException e) {
            GAlerter.lab("GameResourceWeblogic caught an Exception :", e);
            return otherGameResourceNames;
        }

        return otherGameResourceNames;
    }

    public GameResource saveGameResource(GameResource entry, PrivilegeUser currentUser, List<GameProperty> gamePropertyList) throws ServiceException {
        entry = GameResourceServiceSngl.get().createGameResource(entry);

        for (GameProperty gameProperty : gamePropertyList) {
            gameProperty.setResourceId(entry.getResourceId());
        }
        GameResourceServiceSngl.get().batchCreateGameProperts(gamePropertyList);

        //相关下载
        ViewLine downLoadLine = new ViewLine();
        downLoadLine.setCategoryId(0);
        downLoadLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
        downLoadLine.setLineName(String.valueOf(entry.getResourceId()));
        downLoadLine.setItemType(ViewItemType.CONTENT);

        downLoadLine.setCreateDate(new Date());
        downLoadLine.setCreateUserid(currentUser.getUserid());
        downLoadLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        downLoadLine.setValidStatus(ValidStatus.VALID);
        downLoadLine = ViewLineServiceSngl.get().createViewLine(downLoadLine);

        GameRelation gameRelation = new GameRelation();
        gameRelation.setResourceId(entry.getResourceId());
        gameRelation.setSortNum(1);
        gameRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_DOWNLOAD);
        gameRelation.setRelationValue(String.valueOf(downLoadLine.getLineId()));
        gameRelation = GameResourceServiceSngl.get().createRelation(gameRelation);

        //相关文章
        ViewLine aboutGroupLine = new ViewLine();
        aboutGroupLine.setCategoryId(0);
        aboutGroupLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
        aboutGroupLine.setLineName(String.valueOf(entry.getResourceId()));
        aboutGroupLine.setItemType(ViewItemType.CONTENT);
        aboutGroupLine.setCreateDate(new Date());
        aboutGroupLine.setCreateUserid(currentUser.getUserid());
        aboutGroupLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        aboutGroupLine.setValidStatus(ValidStatus.VALID);
        aboutGroupLine = ViewLineServiceSngl.get().createViewLine(aboutGroupLine);

        GameRelation aboutGroupRelation = new GameRelation();
        aboutGroupRelation.setResourceId(entry.getResourceId());
        aboutGroupRelation.setSortNum(1);
        aboutGroupRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_GROUPCONTENT);
        aboutGroupRelation.setRelationValue(String.valueOf(aboutGroupLine.getLineId()));
        GameResourceServiceSngl.get().createRelation(aboutGroupRelation);

        //初始化line
        try {
            ViewLine profileLine = new ViewLine();
            profileLine.setCategoryId(0);
            profileLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            profileLine.setLineName(String.valueOf(entry.getResourceId()));
            profileLine.setItemType(ViewItemType.PROFILE);
            profileLine.setCreateDate(new Date());
            profileLine.setCreateUserid(currentUser.getUserid());
            profileLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            profileLine.setValidStatus(ValidStatus.VALID);
            profileLine = ViewLineServiceSngl.get().createViewLine(profileLine);

            GameRelation profileRelation = new GameRelation();
            profileRelation.setResourceId(entry.getResourceId());
            profileRelation.setSortNum(1);
            profileRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_CONTRIBUTE);
            profileRelation.setRelationValue(String.valueOf(profileLine.getLineId()));
            GameResourceServiceSngl.get().createRelation(profileRelation);
        } catch (Exception e) {
            GAlerter.lab(" create profileLine error.gameId:" + entry.getResourceId() + "e:", e);
        }

        try {
            ViewLine talentLine = new ViewLine();
            talentLine.setCategoryId(0);
            talentLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            talentLine.setLineName(String.valueOf(entry.getResourceId()));
            talentLine.setItemType(ViewItemType.PROFILE);
            talentLine.setCreateDate(new Date());
            talentLine.setCreateUserid(currentUser.getUserid());
            talentLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            talentLine.setValidStatus(ValidStatus.VALID);
            talentLine = ViewLineServiceSngl.get().createViewLine(talentLine);

            GameRelation talentRelation = new GameRelation();
            talentRelation.setResourceId(entry.getResourceId());
            talentRelation.setSortNum(1);
            talentRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_TALENT);
            talentRelation.setRelationValue(String.valueOf(talentLine.getLineId()));
            GameResourceServiceSngl.get().createRelation(talentRelation);
        } catch (ServiceException e) {
            GAlerter.lab(" create talentLine error.gameId:" + entry.getResourceId() + "e:", e);
        }

        try {
            ViewLine menuLine = new ViewLine();
            menuLine.setCategoryId(0);
            menuLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            menuLine.setLineName(WebappConfig.get().getGameMenuLineName());
            menuLine.setItemType(ViewItemType.CUSTOM);
            menuLine.setCreateDate(new Date());
            menuLine.setCreateUserid(currentUser.getUserid());
            menuLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            menuLine.setValidStatus(ValidStatus.VALID);
            menuLine = ViewLineServiceSngl.get().createViewLine(menuLine);

            GameRelation menuRelation = new GameRelation();
            menuRelation.setValidStatus(ValidStatus.VALID);
            menuRelation.setResourceId(entry.getResourceId());
            menuRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_MENU);
            menuRelation.setRelationValue(String.valueOf(menuLine.getLineId()));
            menuRelation = GameResourceServiceSngl.get().createRelation(menuRelation);
        } catch (ServiceException e) {
            GAlerter.lab(" create menuLine error.gameId:" + entry.getResourceId() + "e:", e);
        }

        try {
            ViewLine headImageLine = new ViewLine();
            headImageLine.setCategoryId(0);
            headImageLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            headImageLine.setLineName(String.valueOf(entry.getResourceId()));
            headImageLine.setItemType(ViewItemType.CUSTOM);
            headImageLine.setCreateDate(new Date());
            headImageLine.setCreateUserid(currentUser.getUserid());
            headImageLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            headImageLine.setValidStatus(ValidStatus.VALID);
            headImageLine = ViewLineServiceSngl.get().createViewLine(headImageLine);

            GameRelation headImageRelation = new GameRelation();
            headImageRelation.setValidStatus(ValidStatus.VALID);
            headImageRelation.setResourceId(entry.getResourceId());
            headImageRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_HEADIMAGE);
            headImageRelation.setRelationValue(String.valueOf(headImageLine.getLineId()));
            headImageRelation = GameResourceServiceSngl.get().createRelation(headImageRelation);
        } catch (ServiceException e) {
            GAlerter.lab(" create headImageline error.gameId:" + entry.getResourceId() + "e:", e);
        }

        GameLayoutSetting gameLayoutSetting = new GameLayoutSetting();
        List<GameViewLayout> viewList = new ArrayList<GameViewLayout>();
        GameViewLayout descLayout = new GameViewLayout();
        descLayout.setType(GameViewLayoutType.GAMEDESC);
        descLayout.setLayoutName("游戏介绍");
        viewList.add(descLayout);

        GameViewLayout updateInfoLayout = new GameViewLayout();
        updateInfoLayout.setType(GameViewLayoutType.UPDATEINFO);
        updateInfoLayout.setLayoutName("最近更新");
        viewList.add(updateInfoLayout);

        int disploayOrder = Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000);

        try {
            ViewLine articleLine = new ViewLine();
            articleLine.setCategoryId(0);
            articleLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            articleLine.setLineName(String.valueOf(entry.getResourceId()));
            articleLine.setItemType(ViewItemType.CONTENT);
            articleLine.setCreateDate(new Date());
            articleLine.setCreateUserid(currentUser.getUserid());
            articleLine.setDisplayOrder(disploayOrder - 2);
            articleLine.setValidStatus(ValidStatus.VALID);
            articleLine.setItemMinCount(4);
            articleLine = ViewLineServiceSngl.get().createViewLine(articleLine);

            GameRelation articleRelation = new GameRelation();
            articleRelation.setResourceId(entry.getResourceId());
            articleRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_ARTICLE);
            articleRelation.setRelationValue(String.valueOf(articleLine.getLineId()));
            articleRelation.setValidStatus(ValidStatus.VALID);
            articleRelation = GameResourceServiceSngl.get().createRelation(articleRelation);

            GameViewLayout articleViewLayout = new GameViewLayout();
            articleViewLayout.setLayoutName("精彩新闻");
            articleViewLayout.setRelationId(articleRelation.getRelationId());
            articleViewLayout.setType(GameViewLayoutType.ARTICLE);
            articleViewLayout.setExtraField1(GamePostType.NEWS.getCode());
            articleViewLayout.setSize(articleLine.getItemMinCount());
            viewList.add(articleViewLayout);
        } catch (ServiceException e) {
            GAlerter.lab(" create default news custom module error.gameId:" + entry.getResourceId() + "e:", e);
        }

        //初始化3个自定义模块
        try {
            ViewLine imageLine = new ViewLine();
            imageLine.setCategoryId(0);
            imageLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            imageLine.setLineName(String.valueOf(entry.getResourceId()));
            imageLine.setItemType(ViewItemType.CONTENT);
            imageLine.setCreateDate(new Date());
            imageLine.setCreateUserid(currentUser.getUserid());
            imageLine.setDisplayOrder(disploayOrder - 1);
            imageLine.setValidStatus(ValidStatus.VALID);
            imageLine.setItemMinCount(8);
            imageLine = ViewLineServiceSngl.get().createViewLine(imageLine);

            GameRelation imageRelation = new GameRelation();
            imageRelation.setResourceId(entry.getResourceId());
            imageRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_ARTICLE);
            imageRelation.setRelationValue(String.valueOf(imageLine.getLineId()));
            imageRelation.setValidStatus(ValidStatus.VALID);
            imageRelation = GameResourceServiceSngl.get().createRelation(imageRelation);

            GameViewLayout imageViewLayout = new GameViewLayout();
            imageViewLayout.setLayoutName("精彩图片");
            imageViewLayout.setRelationId(imageRelation.getRelationId());
            imageViewLayout.setType(GameViewLayoutType.IMAGE);
            imageViewLayout.setExtraField1(GamePostType.IMAGE.getCode());
            imageViewLayout.setSize(imageLine.getItemMinCount());
            viewList.add(imageViewLayout);

        } catch (ServiceException e) {
            GAlerter.lab(" create default image custom module error.gameId:" + entry.getResourceId() + "e:", e);
        }

        try {
            ViewLine videoLine = new ViewLine();
            videoLine.setCategoryId(0);
            videoLine.setCategoryAspect(ViewCategoryAspect.GAME_RELATION);
            videoLine.setLineName(String.valueOf(entry.getResourceId()));
            videoLine.setItemType(ViewItemType.CONTENT);
            videoLine.setCreateDate(new Date());
            videoLine.setCreateUserid(currentUser.getUserid());
            videoLine.setDisplayOrder(disploayOrder - 1);
            videoLine.setValidStatus(ValidStatus.VALID);
            videoLine.setItemMinCount(8);
            videoLine = ViewLineServiceSngl.get().createViewLine(videoLine);

            GameRelation videoRelation = new GameRelation();
            videoRelation.setResourceId(entry.getResourceId());
            videoRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_ARTICLE);
            videoRelation.setRelationValue(String.valueOf(videoLine.getLineId()));
            videoRelation.setValidStatus(ValidStatus.VALID);
            videoRelation = GameResourceServiceSngl.get().createRelation(videoRelation);

            GameViewLayout newsViewLayout = new GameViewLayout();
            newsViewLayout.setLayoutName("精彩视频");
            newsViewLayout.setRelationId(videoRelation.getRelationId());
            newsViewLayout.setType(GameViewLayoutType.VIDEO);
            newsViewLayout.setExtraField1(GamePostType.VIDEO.getCode());
            newsViewLayout.setSize(videoLine.getItemMinCount());
            viewList.add(newsViewLayout);
        } catch (ServiceException e) {
            GAlerter.lab(" create default video custom module error.gameId:" + entry.getResourceId() + "e:", e);
        }

        //create default baike layout
        GameViewLayout baikeLayout = new GameViewLayout();
        baikeLayout.setType(GameViewLayoutType.BAIKE);
        viewList.add(baikeLayout);

        gameLayoutSetting.setViewList(viewList);

        GameLayout gameLayout = new GameLayout();
        gameLayout.setResourceId(entry.getResourceId());
        gameLayout.setGameLayoutSetting(gameLayoutSetting);

        GameResourceServiceSngl.get().createGameLayout(gameLayout);

        return entry;
    }


    public GameResource saveGroupResource(GameResource entry, PrivilegeUser currentUser, GameResourceLinkSet gameResourceLinkSet) throws ServiceException {
        entry = GameResourceServiceSngl.get().createGameResource(entry);

        ViewCategory boardCategory = new ViewCategory();
        ViewLine boardViewLine = new ViewLine();
        ViewCategory talkCategory = new ViewCategory();
        ViewLine talkLine = new ViewLine();
        ViewCategory essCategory = new ViewCategory();
        ViewLine essLine = new ViewLine();

        boardCategory.setCategoryName(entry.getResourceName());
        boardCategory.setLocationCode(LocationCode.DEFAULT.getCode());
        boardCategory.setCategoryCode(entry.getGameCode());
        boardCategory.setCreateDate(new Date());
        boardCategory.setCreateUserid(currentUser.getUserid());
        boardCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        boardCategory.setParentCategoryId(0);
        boardCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        boardCategory.setValidStatus(ValidStatus.VALID);
        boardCategory.setPublishStatus(ActStatus.ACTED);
        boardCategory = ViewLineServiceSngl.get().createCategory(boardCategory);

        boardViewLine.setCategoryId(boardCategory.getCategoryId());
        boardViewLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        boardViewLine.setLineName(boardCategory.getCategoryName());
        boardViewLine.setItemType(ViewItemType.CUSTOM);
        boardViewLine.setLocationCode(LocationCode.GAME_LINK.getCode());
        boardViewLine.setCreateDate(new Date());
        boardViewLine.setCreateUserid(currentUser.getUserid());
        boardViewLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        boardViewLine.setValidStatus(ValidStatus.VALID);
        boardViewLine = ViewLineServiceSngl.get().createViewLine(boardViewLine);
        if (gameResourceLinkSet != null) {
            Set<GameResourceLink> links = gameResourceLinkSet.getLinks();
            Iterator<GameResourceLink> it = links.iterator();
            while (it.hasNext()) {
                GameResourceLink gameResourceLink = it.next();

                ViewLineItem lineItem = new ViewLineItem();
                lineItem.setCategoryAspect(boardViewLine.getCategoryAspect());
                lineItem.setCategoryId(boardViewLine.getCategoryId());
                lineItem.setCreateDate(new Date());
                lineItem.setCreateUno(currentUser.getUserid());
                lineItem.setItemCreateDate(entry.getCreateDate());
                lineItem.setLineId(boardViewLine.getLineId());
                lineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

                ViewLineItemDisplayInfo itemDisplayInfo = new ViewLineItemDisplayInfo();
                itemDisplayInfo.setSubject(gameResourceLink.getLinkName());
                itemDisplayInfo.setLinkUrl(gameResourceLink.getLinkUrl());
                lineItem.setDisplayInfo(itemDisplayInfo);
                lineItem.setValidStatus(ValidStatus.VALID);
                ViewLineServiceSngl.get().addLineItem(lineItem);
            }
        }

        talkCategory.setCategoryName("话版");
        talkCategory.setLocationCode(LocationCode.TALK_BOARD.getCode());
        talkCategory.setCategoryCode(boardCategory.getCategoryCode() + "board");
        talkCategory.setParentCategoryId(boardCategory.getCategoryId()); //father
        talkCategory.setCreateDate(new Date());
        talkCategory.setCreateUserid(currentUser.getUserid());
        talkCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        talkCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        talkCategory.setValidStatus(ValidStatus.VALID);
        talkCategory.setPublishStatus(ActStatus.ACTED);
        talkCategory = ViewLineServiceSngl.get().createCategory(talkCategory);

        talkLine.setCategoryId(talkCategory.getCategoryId());
        talkLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        talkLine.setLineName(talkCategory.getCategoryName());
        talkLine.setItemType(ViewItemType.CONTENT);
        talkLine.setLocationCode(LocationCode.DEFAULT.getCode());
        talkLine.setCreateDate(new Date());
        talkLine.setCreateUserid(currentUser.getUserid());
        talkLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        talkLine.setValidStatus(ValidStatus.VALID);
        ViewLineServiceSngl.get().createViewLine(talkLine);

        essCategory.setCategoryName("精华区");
        essCategory.setLocationCode(LocationCode.ESS_BOARD.getCode());
        essCategory.setCategoryCode(boardCategory.getCategoryCode() + "ess");
        essCategory.setParentCategoryId(boardCategory.getCategoryId()); //father
        essCategory.setCreateDate(new Date());
        essCategory.setCreateUserid(currentUser.getUserid());
        essCategory.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        essCategory.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        essCategory.setValidStatus(ValidStatus.VALID);
        essCategory.setPublishStatus(ActStatus.ACTED);
        essCategory = ViewLineServiceSngl.get().createCategory(essCategory);

        essLine.setCategoryId(essCategory.getCategoryId());
        essLine.setCategoryAspect(ViewCategoryAspect.CONTENT_BOARD);
        essLine.setLineName(essCategory.getCategoryName());
        essLine.setItemType(ViewItemType.CONTENT);
        essLine.setLocationCode(LocationCode.DEFAULT.getCode());
        essLine.setCreateDate(new Date());
        essLine.setCreateUserid(currentUser.getUserid());
        essLine.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
        essLine.setValidStatus(ValidStatus.VALID);
        ViewLineServiceSngl.get().createViewLine(essLine);

        //
        GameRelation gameRelation = new GameRelation();
        gameRelation.setResourceId(entry.getResourceId());
        gameRelation.setSortNum(1);
        gameRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_BOARD);
        gameRelation.setRelationValue(String.valueOf(boardCategory.getCategoryId()));
        gameRelation = GameResourceServiceSngl.get().createRelation(gameRelation);

        return entry;
    }

    public GameResource getGameResourceById(long id) throws ServiceException {
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, id));

        return GameResourceServiceSngl.get().getGameResource(getExpress);
    }

    public GameResource getGameResourceByName(String name) throws ServiceException {
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(GameResourceField.RESOURCENAME, name)).add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode()));

        return GameResourceServiceSngl.get().getGameResource(getExpress);
    }

    public PageRows<GameResource> queryGameResources(QueryExpress queryExpress, Pagination page) throws ServiceException {
        return GameResourceServiceSngl.get().queryGameResourceByPage(queryExpress, page);
    }

    public boolean modifyGameResource(UpdateExpress updateExpress, long gameResourceId, Map<GamePropertyDomain, List<GameProperty>> properyDomainMap) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameResourceId));

        boolean returnBval = GameResourceServiceSngl.get().modifyGameResource(updateExpress, queryExpress);

        if (returnBval) {
            for (Map.Entry<GamePropertyDomain, List<GameProperty>> entry : properyDomainMap.entrySet()) {
                returnBval = GameResourceServiceSngl.get().modifyGameProperty(entry.getValue(), gameResourceId, entry.getKey());
            }
        }
        return returnBval;
    }

    public boolean modifyGroupResource(UpdateExpress updateExpress, long gameResourceId) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameResourceId));

        boolean returnBval = GameResourceServiceSngl.get().modifyGameResource(updateExpress, queryExpress);
        return returnBval;
    }

    public boolean removeGameResource(QueryExpress queryExpress) throws ServiceException {
        return GameResourceServiceSngl.get().deleteGameResource(queryExpress);
    }

    public List<GameRelationDTO> queryGameRelationDTObyResourceId(long id) throws ServiceException {
        List<GameRelationDTO> returnObj = new ArrayList<GameRelationDTO>();

        GameResource gameResource = getGameResourceById(id);
        if (gameResource == null) {
            return returnObj;
        }
        List<GameRelation> gameRelationList = gameResource.getGameRelationSet().getGameRelationList();

        GameLayout gameLayout = GameResourceServiceSngl.get().getGameLayout(new QueryExpress().add(QueryCriterions.eq(GameLayoutField.RESOURCEID, id)));

        //get relation->layout for name
        List<GameViewLayout> gameViewLayoutList = new ArrayList<GameViewLayout>();
        if (gameLayout != null) {
            gameViewLayoutList = gameLayout.getGameLayoutSetting().getViewList();
        }
        Map<Long, GameViewLayout> map = new HashMap<Long, GameViewLayout>();
        for (GameViewLayout viewLayout : gameViewLayoutList) {
            if (viewLayout.getRelationId() > 0) {
                map.put(viewLayout.getRelationId(), viewLayout);
            }
        }

        for (GameRelation gameRelation : gameRelationList) {
            GameViewLayout viewLayout = map.get(gameRelation.getRelationId());

            GameRelationDTO relationDTO = new GameRelationDTO();
            relationDTO.setRelationId(gameRelation.getRelationId());

            if (viewLayout != null) {
                if (viewLayout.getType().equals(GameViewLayoutType.BAIKE) && StringUtil.isEmpty(viewLayout.getLayoutName())) {
                    relationDTO.setRelationName(gameResource.getResourceName() + "攻略");
                } else {
                    relationDTO.setRelationName(viewLayout.getLayoutName());
                }
            } else {
                relationDTO.setRelationName(gameRelation.getRelationName());
            }

            relationDTO.setRelationValue(gameRelation.getRelationValue());
            relationDTO.setGameRelationType(gameRelation.getGameRelationType());
            if (viewLayout != null) {
                relationDTO.setLayoutType(viewLayout.getType());
            }
            relationDTO.setValidStatus(ValidStatus.VALID);
            relationDTO.setResourceId(id);
            relationDTO.setSortNum(gameRelation.getSortNum());
            returnObj.add(relationDTO);
        }

        return returnObj;
    }

    public GameRelationDTO getGameRelationDTObyResourceId(GameResource gameResource, long relationId) throws ServiceException {
        GameRelationDTO returnObj = null;

        GameRelation gameRelation = GameResourceServiceSngl.get().getGameRelation(new QueryExpress().add(QueryCriterions.eq(GameRelationField.RELATIONID, relationId)));

        returnObj = new GameRelationDTO();
        returnObj.setRelationId(gameRelation.getRelationId());
        returnObj.setRelationValue(gameRelation.getRelationValue());
        returnObj.setGameRelationType(gameRelation.getGameRelationType());
        returnObj.setValidStatus(gameRelation.getValidStatus());
        returnObj.setResourceId(gameResource.getResourceId());
        returnObj.setSortNum(gameRelation.getSortNum());

        GameLayout gameLayout = GameResourceServiceSngl.get().getGameLayout(new QueryExpress().add(QueryCriterions.eq(GameLayoutField.RESOURCEID, gameResource.getResourceId())));
        List<GameViewLayout> gameViewLayoutList = new ArrayList<GameViewLayout>();
        if (gameLayout != null) {
            gameViewLayoutList = gameLayout.getGameLayoutSetting().getViewList();
        }
        for (GameViewLayout viewLayout : gameViewLayoutList) {
            if (viewLayout.getRelationId() != gameRelation.getRelationId()) {
                continue;
            }

            if (viewLayout != null) {
                returnObj.setLayoutType(viewLayout.getType());
            }
            if (viewLayout != null) {
                if (viewLayout.getType().equals(GameViewLayoutType.BAIKE) && StringUtil.isEmpty(viewLayout.getLayoutName())) {
                    returnObj.setRelationName(gameResource.getResourceName() + "攻略");
                } else {
                    returnObj.setRelationName(viewLayout.getLayoutName());
                }
            } else {
                returnObj.setRelationName(gameRelation.getRelationName());
            }
        }

        return returnObj;
    }

    /**
     * 必须要传入gameId gameRelationId，修改gameRelation如果对应的layout有值修改相应的gameLayout
     *
     * @param gameRelation
     * @return
     * @throws ServiceException
     */
    public boolean modifyGameRelation(GameRelation gameRelation) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONID, gameRelation.getRelationId()));
        UpdateExpress updateExpress = new UpdateExpress();
//        updateExpress.set(GameRelationField.RELATIONTYPE, gameRelation.getGameRelationType().getCode());
        updateExpress.set(GameRelationField.RELATIONVALUE, gameRelation.getRelationValue());
        updateExpress.set(GameRelationField.RELATIONNAME, gameRelation.getRelationName());
        updateExpress.set(GameRelationField.SORTNUM, gameRelation.getSortNum());

        boolean bVal = GameResourceServiceSngl.get().modifyGameRelation(updateExpress, queryExpress);

        if (!bVal) {
            return bVal;
        }

        GameLayout gameLayout = GameResourceServiceSngl.get().getGameLayout(new QueryExpress().add((QueryCriterions.eq(GameLayoutField.RESOURCEID, gameRelation.getResourceId()))));
        if (gameLayout == null) {
            return bVal;
        }

        List<GameViewLayout> viewLayoutList = gameLayout.getGameLayoutSetting().getViewList();
        for (GameViewLayout gameViewLayout : viewLayoutList) {
            if (gameViewLayout.getRelationId() == gameRelation.getRelationId()) {
                gameViewLayout.setLayoutName(gameRelation.getRelationName());
            }
        }

        bVal = GameResourceServiceSngl.get().modifyGameLayout(new UpdateExpress()
                .set(GameLayoutField.LAYOUTSETTING, gameLayout.getGameLayoutSetting().toJson()),
                new QueryExpress().add(QueryCriterions.eq(GameLayoutField.RESOURCEID, gameRelation.getResourceId())));
        return bVal;
    }

    //
    public boolean isGameResourceNameExistBySynonym(Set<String> synonymses) throws ServiceException {
        for (String asResourceName : synonymses) {
            GameResource gameResource = getGameResourceByName(asResourceName);
            if (gameResource != null) {
                return true;
            }
        }
        return false;
    }

    public GameRelation saveGameRelation(GameRelation gameRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Parameter GameRelation is:" + gameRelation);
        }
        return GameResourceServiceSngl.get().createRelation(gameRelation);
    }

    ///////////////////////////////////////////////////////////////////////////
    public List<GamePrivacyDTO> queryPrivacy(long resourceId, ResourceDomain resourceDomain) throws ServiceException {
        List<GamePrivacyDTO> returnObj = new ArrayList<GamePrivacyDTO>();

        List<GamePrivacy> gamePrivacyList = GameResourceServiceSngl.get().queryGamePrivacies(new QueryExpress()
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEID, resourceId))
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEDOMAIN, resourceDomain.getCode())));


        Set<String> unoSet = new HashSet<String>();
        for (GamePrivacy gamePrivacy : gamePrivacyList) {
            unoSet.add(gamePrivacy.getUno());
        }

        Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSet);
        for (GamePrivacy gamePrivacy : gamePrivacyList) {
            GamePrivacyDTO dto = new GamePrivacyDTO();
            dto.setGamePrivacy(gamePrivacy);
            if (profileMap.containsKey(gamePrivacy.getUno())) {
                dto.setScreenName(profileMap.get(gamePrivacy.getUno()).getBlog().getScreenName());
            }

            returnObj.add(dto);
        }

        return returnObj;
    }

    public boolean privacyHasExists(long resourceId, String uno, GamePrivacyLevel level, ResourceDomain domain) throws ServiceException {
        return GameResourceServiceSngl.get().getGamePrivacy(new QueryExpress()
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEID, resourceId))
                .add(QueryCriterions.eq(GamePrivacyField.UNO, uno))
                .add(QueryCriterions.eq(GamePrivacyField.PRIVCAYLEVEL, level.getCode()))
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEDOMAIN, domain.getCode()))) != null;
    }

    public boolean deleteUserPrivacy(long resourceId, String uno, GamePrivacyLevel level, ResourceDomain domain) throws ServiceException {
        return GameResourceServiceSngl.get().deleteGamePrivacy(new QueryExpress()
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEID, resourceId))
                .add(QueryCriterions.eq(GamePrivacyField.UNO, uno))
                .add(QueryCriterions.eq(GamePrivacyField.PRIVCAYLEVEL, level.getCode()))
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEDOMAIN, domain.getCode())));
    }

    public GamePrivacyDTO getPrivacyDTO(long resrouceId, String uno, GamePrivacyLevel level, ResourceDomain domain) throws ServiceException {
        GamePrivacyDTO returnObj = null;

        GamePrivacy gamePrivacy = GameResourceServiceSngl.get().getGamePrivacy(new QueryExpress()
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEID, resrouceId))
                .add(QueryCriterions.eq(GamePrivacyField.UNO, uno))
                .add(QueryCriterions.eq(GamePrivacyField.PRIVCAYLEVEL, level.getCode()))
                .add(QueryCriterions.eq(GamePrivacyField.RESOURCEDOMAIN, domain.getCode())));
        if (gamePrivacy == null) {
            return returnObj;
        }

        Profile profile = ProfileServiceSngl.get().getProfileByUno(gamePrivacy.getUno());
        if (profile == null) {
            return returnObj;
        }

        returnObj = new GamePrivacyDTO();
        returnObj.setGamePrivacy(gamePrivacy);
        returnObj.setScreenName(profile.getBlog().getScreenName());

        return returnObj;
    }

    public List<GameResource> queryGroupResources(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceWebLogic queryGroupResources:queryExpress" + queryExpress);
        }
        List<GameResource> list = GameResourceServiceSngl.get().queryGameResources(queryExpress);
        return list;
    }

    public GroupRoleDTO getGroupRoleDTO(Long groupId, Long roleId) throws ServiceException {
        GameResource group = getGameResourceById(groupId);
        Role role = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.ROLE_ID, roleId)));
        GroupRoleDTO dto = new GroupRoleDTO();
        dto.setGroup(group);
        dto.setRole(role);
        return dto;
    }

    public List<GroupPrivilege> queryGroupPrivilege(int type, String status) throws ServiceException {
        List<GroupPrivilege> list = GameResourceServiceSngl.get().queryGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_TYPE, type)).add(QueryCriterions.eq(GroupPrivilegeField.STATUS, status)));
        return list;
    }

    public List<GroupRolePrivilegeDTO> queryRolePrivilege(Long groupId, Long roleId, Integer type) throws ServiceException {
        GameResource group = getGameResourceById(groupId);
        Role role = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.ROLE_ID, roleId)));
        List<PrivilegeRoleRelation> relationList = GameResourceServiceSngl.get().queryPrivilegeRoleRelation(roleId);
        if (CollectionUtil.isEmpty(relationList)) {
            return null;
        }
        List<GroupRolePrivilegeDTO> dtoList = new LinkedList<GroupRolePrivilegeDTO>();
        GroupRolePrivilegeDTO dto = null;
        Map<Integer, List<GroupRolePrivilegeDTO>> map = new LinkedHashMap<Integer, List<GroupRolePrivilegeDTO>>();
        for (PrivilegeRoleRelation relation : relationList) {
            GroupPrivilege privilege = GameResourceServiceSngl.get().getGroupPrivilege(new QueryExpress().add(QueryCriterions.eq(GroupPrivilegeField.PRIVILEGE_ID, relation.getPrivilegeId())));
            if (privilege != null && !map.containsKey(privilege.getPrivilegeType().getCode())) {
                map.put(privilege.getPrivilegeType().getCode(), dtoList);
            }
            dto = new GroupRolePrivilegeDTO();
            dto.setGroup(group);
            dto.setRole(role);
            dto.setPrivilege(privilege);
            dto.setRelation(relation);
            map.get(privilege.getPrivilegeType().getCode()).add(dto);
        }
        return map.get(type);
    }
}
