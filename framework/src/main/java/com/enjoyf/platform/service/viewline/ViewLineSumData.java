/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: view category
 */
public class ViewLineSumData implements Serializable {
    //
    public static final String SUM_TYPE_ALL = "all";

    //the srcid．
    private int srcId;

    //the src domain, the viewline or the category.
    private ViewLineSumDomain sumDomain;

    //all, or the YYYY-MM-dd str.
    private String sumTypeCode;

    //the sum info.
    private int viewTimes;
    private int postTimes;
    private int replyTimes;
    private int favorTimes;
    private int forwardTimes;


    //
    public ViewLineSumData() {
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public ViewLineSumDomain getSumDomain() {
        return sumDomain;
    }

    public void setSumDomain(ViewLineSumDomain sumDomain) {
        this.sumDomain = sumDomain;
    }

    public String getSumTypeCode() {
        return sumTypeCode;
    }

    public void setSumTypeCode(String sumTypeCode) {
        this.sumTypeCode = sumTypeCode;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public int getPostTimes() {
        return postTimes;
    }

    public void setPostTimes(int postTimes) {
        this.postTimes = postTimes;
    }

    public int getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(int replyTimes) {
        this.replyTimes = replyTimes;
    }

    public int getFavorTimes() {
        return favorTimes;
    }

    public void setFavorTimes(int favorTimes) {
        this.favorTimes = favorTimes;
    }

    public int getForwardTimes() {
        return forwardTimes;
    }

    public void setForwardTimes(int forwardTimes) {
        this.forwardTimes = forwardTimes;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
