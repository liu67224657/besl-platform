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
public class TopicSocialRelationDTO {
    //
    private GameResource topicResource;

    //
    private SocialRelation relation;

    public GameResource getTopicResource() {
        return topicResource;
    }

    public void setTopicResource(GameResource topicResource) {
        this.topicResource = topicResource;
    }

    public SocialRelation getRelation() {
        return relation;
    }

    public void setRelation(SocialRelation relation) {
        this.relation = relation;
    }
}
