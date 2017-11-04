/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline.autofillrule;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillRule;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="taijunli@staff.joyme.com">taijunli</a>
 * Create time: 12-2-15 下午1:13
 * Description: line fill rule
 */
public class ViewLineAutoFillProfileRule implements Serializable, ViewLineAutoFillRule {
    //
    private VerifyType verifyTypeCode;

    //
    private int fansNumLimit;
    private int postTimesLimit;
    private int replyTimesLimit;

    //
    private Set<String> favorKeywords = new HashSet<String>();

    //
    public ViewLineAutoFillProfileRule() {
    }

    public VerifyType getVerifyTypeCode() {
        return verifyTypeCode;
    }

    public void setVerifyTypeCode(VerifyType verifyTypeCode) {
        this.verifyTypeCode = verifyTypeCode;
    }

    public int getFansNumLimit() {
        return fansNumLimit;
    }

    public void setFansNumLimit(int fansNumLimit) {
        this.fansNumLimit = fansNumLimit;
    }

    public int getPostTimesLimit() {
        return postTimesLimit;
    }

    public void setPostTimesLimit(int postTimesLimit) {
        this.postTimesLimit = postTimesLimit;
    }

    public int getReplyTimesLimit() {
        return replyTimesLimit;
    }

    public void setReplyTimesLimit(int replyTimesLimit) {
        this.replyTimesLimit = replyTimesLimit;
    }

    public Set<String> getFavorKeywords() {
        return favorKeywords;
    }

    public void setFavorKeywords(Set<String> favorKeywords) {
        this.favorKeywords = favorKeywords;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
