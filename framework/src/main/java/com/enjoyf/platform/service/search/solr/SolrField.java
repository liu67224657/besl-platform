package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-14
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class SolrField implements Serializable {
    private String fieldName = "searchtext";
    private String fieldType = "textIKAnalyze";
    private String fieldValue = "text";
    private String query;

    public SolrField() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
