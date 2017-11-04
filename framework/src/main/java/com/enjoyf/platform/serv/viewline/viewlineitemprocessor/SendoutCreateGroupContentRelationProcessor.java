package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ContentRelationCreateEvent;
import com.enjoyf.platform.service.event.system.ViewLineItemInsertEvent;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.viewline.LocationCode;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午8:58
 * To change this template use File | Settings | File Templates.
 */
public class SendoutCreateGroupContentRelationProcessor implements ViewLineItemInsertProcessor {
    private ViewLineLogic viewLineLogic;

    public SendoutCreateGroupContentRelationProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    @Override
    public void process(ViewLineItemInsertEvent insertEvent) {
        if (!insertEvent.getViewCategoryAspect().equals(ViewCategoryAspect.CONTENT_BOARD)) {
            return;
        }

        try {
            //get category
            ViewCategory viewCategory = viewLineLogic.getCategoryByCategoryIdFromCache(insertEvent.getCategoryId());
            if (viewCategory == null) {
                viewCategory = viewLineLogic.getCategory(new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, insertEvent.getCategoryId())));
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

            GameResource gameRescource = GameResourceServiceSngl.get().getGameResource(new QueryExpress()
                    .add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameRelation.getResourceId()))
            );
            if (gameRescource == null) {
                GAlerter.lan(getClass().getName() + "game relation is not exists.gameCategoryId: " + gameCategory.getCategoryId());
                return;
            }

            ContentRelationCreateEvent crcEvent = new ContentRelationCreateEvent();
            crcEvent.setContentId(insertEvent.getDirectId());
            crcEvent.setContentRelationType(ContentRelationType.GROUP);
            crcEvent.setRelationId(String.valueOf(gameRescource.getResourceId()));

            EventDispatchServiceSngl.get().dispatch(crcEvent);

        } catch (Exception e) {
            GAlerter.lab(getClass().getName() + "process occured Exception.event: " + insertEvent + " e :", e);
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
