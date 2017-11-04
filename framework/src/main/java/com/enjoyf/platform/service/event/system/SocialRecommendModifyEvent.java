package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.social.RecommendType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-3
 * Description:
 */
public class SocialRecommendModifyEvent extends SystemEvent {
    private String srcUno;
    private RecommendType recommendType = RecommendType.FOLLOW_EACHOTHER;
    private String destUno;

    public SocialRecommendModifyEvent() {
        super(SystemEventType.SOCIAL_RECOMMEND_MODIFY_EVENT);
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

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
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
