/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.social.SocialRelation;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-7 下午3:12
 * Description:
 */
public class GameSocialRelationDTO {
    //
    private GameResource gameResource;

    //
    private SocialRelation relation;

    public GameResource getGameResource() {
        return gameResource;
    }

    public void setGameResource(GameResource gameResource) {
        this.gameResource = gameResource;
    }

    public SocialRelation getRelation() {
        return relation;
    }

    public void setRelation(SocialRelation relation) {
        this.relation = relation;
    }
}
