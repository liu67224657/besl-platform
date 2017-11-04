/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterionRelation;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class SearchCriterion implements Serializable {
    //
    private ObjectField field;

    //
    private QueryCriterionRelation relation = QueryCriterionRelation.EQ;
    private QueryCriterionRelation relation2;

    //
    private Object value;

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
