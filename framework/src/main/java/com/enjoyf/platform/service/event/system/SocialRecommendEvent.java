package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.social.RecommendType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-29
 * Time: 上午11:49
 * To change this template use File | Settings | File Templates.
 */
public class SocialRecommendEvent extends SystemEvent {
    private String srcUno;
    private RecommendType recommendType = RecommendType.FOLLOW_EACHOTHER;

    public SocialRecommendEvent() {
        super(SystemEventType.SOCIAL_RECOMMEND_EVENT);
    }

    public String getSrcUno() {
        return srcUno;
    }

    public void setSrcUno(String srcUno) {
        this.srcUno = srcUno;
    }

    public RecommendType getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(RecommendType recommendType) {
        this.recommendType = recommendType;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return srcUno.hashCode();
    }

}
