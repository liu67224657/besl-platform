package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentRelation;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 下午4:54
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelationBuilder {
    public static RelationMiniResourceDTO buildReousrceDTO(ContentRelation contentRelation) {
        RelationMiniResourceDTO returnObj = null;
        if (StringUtil.isEmpty(contentRelation.getRelationId())) {
            return returnObj;
        }

        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEID, Long.parseLong(contentRelation.getRelationId())));
            queryExpress.add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode()));

            GameResource gameResource = GameResourceServiceSngl.get().getGameResource(queryExpress);

            if (gameResource != null) {
                returnObj = new RelationMiniResourceDTO();
                returnObj.setGameCode(gameResource.getGameCode());
                returnObj.setGameName(gameResource.getResourceName());
                returnObj.setResourceDomain(gameResource.getResourceDomain());
                if (gameResource.getIcon() != null && !CollectionUtil.isEmpty(gameResource.getIcon().getImages())) {
                    returnObj.setIcon(gameResource.getIcon().getImages().iterator().next().getLl());
                }
            }

        } catch (Exception e) {
            GAlerter.lab(ContentRelationBuilder.class + "build RelationMiniResourceDTO occuerd error.contentId" + contentRelation.getContentId() + " e: ", e);
        }

        return returnObj;
    }
}
