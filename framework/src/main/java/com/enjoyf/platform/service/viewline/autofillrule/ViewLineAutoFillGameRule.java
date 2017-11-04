/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline.autofillrule;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentType;
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
public class ViewLineAutoFillGameRule implements Serializable, ViewLineAutoFillRule {


    private Set<String> includeUnos = new HashSet<String>();
    private Set<String> includeKeywords = new HashSet<String>();
    private Integer contentTypeCode;
    private String publishTypeCode;


    public ViewLineAutoFillGameRule() {
    }

    public Set<String> getIncludeUnos() {
        return includeUnos;
    }

    public void setIncludeUnos(Set<String> includeUnos) {
        this.includeUnos = includeUnos;
    }

    public Set<String> getIncludeKeywords() {
        return includeKeywords;
    }

    public void setIncludeKeywords(Set<String> includeKeywords) {
        this.includeKeywords = includeKeywords;
    }

    public ContentPublishType getByCode(String code) {
        return ContentPublishType.getByCode(code);
    }

    public ContentType getByCode(Integer code) {
        return ContentType.getByValue(code);
    }

    public Integer getContentTypeCode() {
        return contentTypeCode;
    }

    public void setContentTypeCode(Integer contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }

    public String getPublishTypeCode() {
        return publishTypeCode;
    }

    public void setPublishTypeCode(String publishTypeCode) {
        this.publishTypeCode = publishTypeCode;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
