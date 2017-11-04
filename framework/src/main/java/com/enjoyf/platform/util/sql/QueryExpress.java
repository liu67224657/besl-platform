/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午3:00
 * Description:
 */
public class QueryExpress implements Serializable {
    //
    private QueryCriterions queryCriterions = new QueryCriterions();
    private List<QuerySort> querySorts = new ArrayList<QuerySort>();

    //
    public QueryExpress() {
        //
    }

    public QueryExpress add(QueryCriterions criterions) {
        queryCriterions.getCriterionsList().add(criterions);

        return this;
    }

    public QueryExpress add(QuerySort querySort) {
        querySorts.add(querySort);

        return this;
    }

    public QueryCriterions getQueryCriterions() {
        return queryCriterions;
    }

    public List<QuerySort> getQuerySorts() {
        return querySorts;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
