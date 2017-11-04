/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline.autofillrule;

import com.enjoyf.platform.service.JsonBinder;
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
public class ViewLineAutoFillContentRule implements Serializable, ViewLineAutoFillRule {
    //
    private Set<String> includeUnos = new HashSet<String>();
    private Set<String> includeKeywords = new HashSet<String>();
    private Set<Integer> contentTypeValues = new HashSet<Integer>();
    private Set<String> publishTypeCodes = new HashSet<String>();

    //
    public ViewLineAutoFillContentRule() {
    }

    //
    public Set<String> getIncludeUnos() {
        return includeUnos;
    }

    public Set<String> getIncludeKeywords() {
        return includeKeywords;
    }

    public Set<Integer> getContentTypeValues() {
        return contentTypeValues;
    }

    public Set<String> getPublishTypeCodes() {
        return publishTypeCodes;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
