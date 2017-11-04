package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.apache.solr.client.solrj.SolrQuery;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-14
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class SolrOrder implements Serializable{
    private String fieldName;
    private SolrQuery.ORDER order = SolrQuery.ORDER.asc;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SolrQuery.ORDER getOrder() {
        return order;
    }

    public void setOrder(SolrQuery.ORDER order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
