package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ContentRelationCreateEvent;
import com.enjoyf.platform.service.event.system.ViewLineItemInsertEvent;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午8:58
 * To change this template use File | Settings | File Templates.
 */
public class SendoutCreateGameContentRelationProcessor implements ViewLineItemInsertProcessor {
    private ViewLineLogic viewLineLogic;

    public SendoutCreateGameContentRelationProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    @Override
    public void process(ViewLineItemInsertEvent insertEvent) {
        if (!insertEvent.getViewCategoryAspect().equals(ViewCategoryAspect.GAME_RELATION)) {
            return;
        }

        try {
            //get category
            ViewLine relationLine = viewLineLogic.getViewLine(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, insertEvent.getLineId())));
            if (relationLine == null) {
                return;
            }

            //话版或者游戏自定义模块

            QueryExpress relationExpress = new QueryExpress();
            relationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(relationLine.getLineId())));
            List<GameRelation> gameRelationList = GameResourceServiceSngl.get().queryGameRelation(relationExpress);
            for (GameRelation gameRelation : gameRelationList) {
                //自定义模块和相关现在才能加入
                if (!gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_ARTICLE)
                        && !gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_DOWNLOAD)) {
                    continue;
                }

                GameResource gameRescource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gameRelation.getResourceId())));
                if (gameRescource == null) {
                    GAlerter.lan(getClass().getName() + "gameRescource is not exists.gameRescource: " + gameRelation.getResourceId());
                    continue;
                }

                ContentRelationCreateEvent crcEvent = new ContentRelationCreateEvent();
                crcEvent.setContentId(insertEvent.getDirectId());
                crcEvent.setContentRelationType(ContentRelationType.GAME);
                crcEvent.setRelationId(String.valueOf(gameRescource.getResourceId()));

                EventDispatchServiceSngl.get().dispatch(crcEvent);
            }
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
