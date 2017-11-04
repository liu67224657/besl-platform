package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ContentRelationRemoveEvent;
import com.enjoyf.platform.service.event.system.ViewLineItemRemoveEvent;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class SendoutRemoveGroupContentRelationProcessor implements ViewLineItemRemoveProcessor {

    private ViewLineLogic viewLineLogic;


    public SendoutRemoveGroupContentRelationProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    @Override
    public void process(ViewLineItemRemoveEvent removeEvent) {
        if (!removeEvent.getViewCategoryAspect().equals(ViewCategoryAspect.CONTENT_BOARD)) {
            return;
        }

        try {
            //get category
            ViewCategory viewCategory = viewLineLogic.getCategoryByCategoryIdFromCache(removeEvent.getCategoryId());
            if (viewCategory == null) {
                viewCategory = viewLineLogic.getCategory(new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, removeEvent.getCategoryId())));
            }
            if (viewCategory == null) {
                return;
            }

            //话版或者游戏自定义模块
            if (!viewCategory.getLocationCode().equals(LocationCode.TALK_BOARD.getCode())) {
                return;
            }

            if (viewCategory.getParentCategory() == null) {
                viewLineLogic.loadParentCategory(viewCategory);
            }

            ViewCategory gameCategory = getTopCategory(viewCategory);
            QueryExpress relationExpress = new QueryExpress();
            relationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(gameCategory.getCategoryId())));
            relationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BOARD.getCode()));
            GameRelation gameRelation = GameResourceServiceSngl.get().getGameRelation(relationExpress);
            if (gameRelation == null) {
                GAlerter.lan(getClass().getName() + "game relation is not exists.gameCategoryId: " + gameCategory.getCategoryId());
                return;
            }

            GameResource gameRescource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameRelation.getResourceId())));
            if (gameRescource == null) {
                 GAlerter.lan(getClass().getName() + "game relation is not exists.gameCategoryId: " + gameCategory.getCategoryId());
                return;
            }

            ContentRelationRemoveEvent crcEvent = new ContentRelationRemoveEvent();
            crcEvent.setContentId(removeEvent.getDirectId());
            crcEvent.setContentRelationType( ContentRelationType.GROUP);
            crcEvent.setRelationId(String.valueOf(gameRescource.getResourceId()));

            EventDispatchServiceSngl.get().dispatch(crcEvent);
        } catch (Exception e) {
            GAlerter.lab(getClass().getName() + "process occured Exception.event: " + removeEvent + " e :", e);
        }
    }

    private ViewCategory getTopCategory(ViewCategory viewCategory) {
        ViewCategory returnObj = viewCategory;
        if (viewCategory.getParentCategory() != null) {
            returnObj = getTopCategory(viewCategory.getParentCategory());
        }
        return returnObj;
    }
}
