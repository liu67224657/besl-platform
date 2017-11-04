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
public class SendoutRemoveGameContentRelationProcessor implements ViewLineItemRemoveProcessor {

    private ViewLineLogic viewLineLogic;


    public SendoutRemoveGameContentRelationProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    @Override
    public void process(ViewLineItemRemoveEvent removeEvent) {
        if (!removeEvent.getViewCategoryAspect().equals(ViewCategoryAspect.GAME_RELATION)) {
            return;
        }

        try {
            //get category
            ViewLine relationLine = viewLineLogic.getViewLine(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, removeEvent.getLineId())));
            if (relationLine == null) {
                return;
            }

            //话版或者游戏自定义模块

            QueryExpress relationExpress = new QueryExpress();
            relationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(relationLine.getLineId())));
            relationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BOARD.getCode()));
            GameRelation gameRelation = GameResourceServiceSngl.get().getGameRelation(relationExpress);
            if (gameRelation == null) {
                GAlerter.lan(getClass().getName() + "game relation is not exists.relationLine: " + relationLine.getLineId());
                return;
            }

            //自定义模块和相关现在才能加入
            if (!gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_ARTICLE)
                    && !gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_DOWNLOAD)) {
                return;
            }

            GameResource gameRescource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameRelation.getResourceId())));
            if (gameRescource == null) {
                GAlerter.lan(getClass().getName() + "gameRescource is not exists.gameRescource: " + gameRelation.getResourceId());
                return;
            }

            ContentRelationRemoveEvent crcEvent = new ContentRelationRemoveEvent();
            crcEvent.setContentId(removeEvent.getDirectId());
            crcEvent.setContentRelationType(ContentRelationType.GAME);
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
