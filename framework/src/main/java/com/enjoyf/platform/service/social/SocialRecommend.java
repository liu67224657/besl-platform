package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-29
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public class SocialRecommend implements Serializable {
    private String srcUno;
    private RecommendType recommendType;
    private RecommendDetailSet recommendDetailSet;
    private RecommendBlackSet blackSet;

    private Date calDate;
    private ActStatus calStatus = ActStatus.UNACT;

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

    public RecommendDetailSet getRecommendDetailSet() {
        return recommendDetailSet;
    }

    public void setRecommendDetailSet(RecommendDetailSet recommendDetailSet) {
        this.recommendDetailSet = recommendDetailSet;
    }

    public Date getCalDate() {
        return calDate;
    }

    public void setCalDate(Date calDate) {
        this.calDate = calDate;
    }

    public ActStatus getCalStatus() {
        return calStatus;
    }

    public void setCalStatus(ActStatus calStatus) {
        this.calStatus = calStatus;
    }

    public RecommendBlackSet getBlackSet() {
        return blackSet;
    }

    public void setBlackSet(RecommendBlackSet blackSet) {
        this.blackSet = blackSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
