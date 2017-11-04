package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.db.viewline.ViewLineHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.*;
import org.apache.activemq.kaha.impl.async.Location;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-11
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class ImportGameResource {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImportGameResource.class);

    private static GameResourceHandler handler;
    private static ViewLineHandler lineHandler;

    private static final String CATEGORY_CODE_CMODULE = "cmodule";

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            handler = new GameResourceHandler("gameres", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("init gameHandler error.");
        }

        try {
            lineHandler = new ViewLineHandler("viewLine", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("init ViewLineHandler error.");
        }

        List<GameResource> gameResourceList = null;
        try {
            gameResourceList = querygameResources(handler);

            for (GameResource gameResource : gameResourceList) {
                transferGameData(gameResource);
            }

        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        logger.info("================finished import data=====================" + gameResourceList.size());
    }

    private static List<GameResource> querygameResources(GameResourceHandler handler) throws DbException {
        return handler.queryGameResources(new QueryExpress()
                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode()))
                .add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode())));
    }

    private static PageRows<GameResource> querygameResourcesByPage(GameResourceHandler handler, Pagination pagination) throws DbException {
        return handler.queryGameResourcesByPage(new QueryExpress()
                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode()))
                .add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode())), pagination);
    }

    private static void transferGameData(GameResource gameResource) throws DbException {
        GameRelation boardRelation = handler.getGameRelation(new QueryExpress()
                .add(QueryCriterions.eq(GameRelationField.RESOURCEID, gameResource.getResourceId()))
                .add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BOARD.getCode())));

        //如果为空直接返回
        if (boardRelation == null) {
            logger.info("boardRelation ==null :" + gameResource.getResourceId());
            return;
        }

        Integer boardCategoryId = null;
        try {
            boardCategoryId = Integer.parseInt(boardRelation.getRelationValue());
        } catch (NumberFormatException e) {
        }
        if (boardCategoryId == null) {
            logger.error("boardCategoryId NumberFormatException.e: " + gameResource.getResourceId());
            return;
        }

        List<ViewCategory> boardChildCategory = lineHandler.queryCategories(new QueryExpress()
                .add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, boardCategoryId))
                .add(QuerySort.add(ViewCategoryField.DISPLAYORDER, QuerySortOrder.ASC))
        );

        //init gameviewLayout
        GameLayout gameLayout = new GameLayout();
        gameLayout.setResourceId(gameResource.getResourceId());
        gameLayout.setGameLayoutSetting(new GameLayoutSetting());
        gameLayout.getGameLayoutSetting().setViewList(new ArrayList<GameViewLayout>());

        GameViewLayout descLayout = new GameViewLayout();
        descLayout.setType(GameViewLayoutType.GAMEDESC);
        descLayout.setLayoutName("游戏介绍");
        gameLayout.getGameLayoutSetting().getViewList().add(descLayout);

        GameViewLayout updateInfoLayout = new GameViewLayout();
        updateInfoLayout.setType(GameViewLayoutType.UPDATEINFO);
        updateInfoLayout.setLayoutName("最近更新");
        gameLayout.getGameLayoutSetting().getViewList().add(updateInfoLayout);

        //自定义模块 和该分类下面的子模块（相关下载，小组内容）
        for (ViewCategory viewCategory : boardChildCategory) {
            ViewLine line = lineHandler.getLine(new QueryExpress()
                    .add(QueryCriterions.eq(ViewLineField.CATEGORYID, viewCategory.getCategoryId()))
                    .add(QueryCriterions.eq(ViewLineField.ITEMTYPE, ViewItemType.CONTENT.getCode()))
                    .add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()))
            );
            if (line == null) {
                continue;
            }

            if (LocationCode.COUSTOM_MODULE.getCode().equals(viewCategory.getLocationCode())) {
                GameViewLayout moduleViewLayout = null;
                try {
                    moduleViewLayout = createModuleRelationAndLine(line, gameResource.getResourceId(), viewCategory);
                } catch (Exception e) {
                    logger.error("module view layout.occured Excpetion.game:" + gameResource.getResourceId() + ":e", e + "");
                }
                if (moduleViewLayout != null) {
                    gameLayout.getGameLayoutSetting().getViewList().add(moduleViewLayout);
                }
            } else if (LocationCode.DOWNLOAD_LINK.getCode().equals(viewCategory.getLocationCode())) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_DOWNLOAD);
            } else if (LocationCode.GROUP_CONTENT_LINK.getCode().equals(viewCategory.getLocationCode())) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_GROUPCONTENT);
            }
        }

        GameRelation baikeRelation = handler.getGameRelation(new QueryExpress()
                .add(QueryCriterions.eq(GameRelationField.RESOURCEID, gameResource.getResourceId()))
                .add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BAIKE.getCode())));
        GameViewLayout baikeLayout = new GameViewLayout();
        baikeLayout.setType(GameViewLayoutType.BAIKE);
        baikeLayout.setLayoutName(gameResource.getResourceName() + "攻略");
        if (baikeRelation != null) {
            baikeLayout.setRelationId(baikeRelation.getRelationId());
        }
        gameLayout.getGameLayoutSetting().getViewList().add(baikeLayout);

        //插入layout
        handler.insertGameLayout(gameLayout);

        //分类下的line 头图 等
        List<ViewLine> lineList = lineHandler.queryLines(new QueryExpress()
                .add(QueryCriterions.eq(ViewLineField.CATEGORYID, boardCategoryId))
//                .add(QueryCriterions.eq(ViewLineField.ITEMTYPE, ViewItemType.CONTENT.getCode()))
                .add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()))
        );
        for (ViewLine line : lineList) {
            if (line.getLocationCode().equals("profile")) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_CONTRIBUTE);
            } else if (line.getLocationCode().equals("news")) {
                createUpdateInfoProperty(line, gameResource.getResourceId());
            } else if (line.getLocationCode().equals("headline")) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_HEADIMAGE);
            } else if (line.getLocationCode().equals("talent")) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_TALENT);
            } else if (line.getLocationCode().equals("menu")) {
                createRelation(line, gameResource.getResourceId(), GameRelationType.GAME_RELATION_TYPE_MENU);
            }
        }
    }

    ///////////////////////////////
    //导入自定义模块
    private static GameViewLayout createModuleRelationAndLine(ViewLine line, long resourceid, ViewCategory moduleCategory) throws DbException {

        UpdateExpress updateExpress = new UpdateExpress()
                .set(ViewLineField.LOCATIONCODE, String.valueOf(line.getLineId()))
                .set(ViewLineField.LINENAME, String.valueOf(resourceid))
                .set(ViewLineField.CATEGORYASPECT, ViewCategoryAspect.GAME_RELATION.getCode())
                .set(ViewLineField.CATEGORYID, 0);
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, line.getLineId()));

        int i = lineHandler.updateLine(updateExpress, queryExpress);
        if (i < 0) {
            return null;
        }
        lineHandler.updateLineItem(new UpdateExpress()
                .set(ViewLineItemField.CATEGORYASPECT, ViewCategoryAspect.GAME_RELATION.getCode())
                .set(ViewLineItemField.CATEGORYID, 0), new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.LINEID, line.getLineId())));

        GameRelation articleRelation = new GameRelation();
        articleRelation.setResourceId(resourceid);
        articleRelation.setGameRelationType(GameRelationType.GAME_RELATION_TYPE_ARTICLE);
        articleRelation.setRelationValue(String.valueOf(line.getLineId()));
        articleRelation.setValidStatus(ValidStatus.VALID);
        articleRelation = handler.insertGameRelation(articleRelation);

        GamePostType postTypeCode = (moduleCategory.getDisplaySetting() == null
                || GamePostType.getByCode(moduleCategory.getDisplaySetting().getExtraField2()) == null) ? GamePostType.NOTE : GamePostType.getByCode(moduleCategory.getDisplaySetting().getExtraField2());

        GameViewLayout returnObj = new GameViewLayout();
        returnObj.setLayoutName(moduleCategory.getCategoryName());
        returnObj.setRelationId(articleRelation.getRelationId());

        if (moduleCategory.getDisplaySetting() == null) {
            returnObj.setType(GameViewLayoutType.ARTICLE);
        } else if (moduleCategory.getDisplaySetting().getExtraField1().equals("image")) {
            returnObj.setType(GameViewLayoutType.IMAGE);
        } else if (moduleCategory.getDisplaySetting().getExtraField1().equals("video")) {
            returnObj.setType(GameViewLayoutType.VIDEO);
        } else {
            returnObj.setType(GameViewLayoutType.ARTICLE);
        }

        returnObj.setExtraField1(postTypeCode.getCode());
        returnObj.setSize(line.getItemMinCount());

        return returnObj;
    }

    private static void createRelation(ViewLine line, long resourceid, GameRelationType type) throws DbException {
        UpdateExpress updateExpress = new UpdateExpress()
                .set(ViewLineField.LOCATIONCODE, String.valueOf(line.getLineId()))
                .set(ViewLineField.LINENAME, String.valueOf(resourceid))
                .set(ViewLineField.CATEGORYASPECT, ViewCategoryAspect.GAME_RELATION.getCode())
                .set(ViewLineField.CATEGORYID, 0);
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, line.getLineId()));

        int i = lineHandler.updateLine(updateExpress, queryExpress);
        if (i < 0) {
            return;
        }
        lineHandler.updateLineItem(new UpdateExpress()
                .set(ViewLineItemField.CATEGORYASPECT, ViewCategoryAspect.GAME_RELATION.getCode())
                .set(ViewLineItemField.CATEGORYID, 0), new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.LINEID, line.getLineId())));

        GameRelation gameRelation = new GameRelation();
        gameRelation.setResourceId(resourceid);
        gameRelation.setSortNum(1);
        gameRelation.setGameRelationType(type);
        gameRelation.setRelationValue(String.valueOf(line.getLineId()));
        gameRelation = handler.insertGameRelation(gameRelation);
    }

    private static void createUpdateInfoProperty(ViewLine line, long resourceid) throws DbException {
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.LINEID, line.getLineId()));

        ViewLineItem item = lineHandler.getLineItem(queryExpress);

        String itemInfo = item == null ? null : item.getDisplayInfo().getDesc();
        GameProperty updateInfoProperty = new GameProperty();
        updateInfoProperty.setGamePropertyDomain(GamePropertyDomain.UPDATEINFO);
        updateInfoProperty.setValue(itemInfo);

        handler.insertGameProperty(updateInfoProperty);

        lineHandler.deleteLineItem(queryExpress);
        lineHandler.deleteLine(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, line.getLineId())));
    }

}

