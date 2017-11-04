package com.enjoyf.platform.util.sql.mongodb;


import com.enjoyf.platform.util.CollectionUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:02
 * Description:
 */
public class MongoUtil {

    public static DBObject generatorQueryCondition(MongoQueryExpress queryExpress) {
        BasicDBObject object = new BasicDBObject();

        BasicDBList values = new BasicDBList();
        for (MongoQueryCriterions criterions : queryExpress.getQueryCriterions()) {
            values.add(generatorQueryCondition(criterions));
        }
        if (!CollectionUtil.isEmpty(values)) {
            object.put(MongoQueryCriterionLogic.LOGIC_AND.getCode(), values);
        }
        return object;
    }

    public static DBObject generatorSort(MongoSort mongoSort) {
        DBObject object = new BasicDBObject();

        object.put(mongoSort.getObjectField().getColumn(), mongoSort.getOrder().getCode());

        return object;
    }


    /**
     * 多字段排序
     * @param mongoSorts
     * @return
     */
    public static DBObject generatorSort(MongoSort[] mongoSorts) {
        DBObject object = new BasicDBObject();

        for(MongoSort mongoSort:mongoSorts){
        	if (null!=mongoSort) {
        		object.put(mongoSort.getObjectField().getColumn(), mongoSort.getOrder().getCode());	
			}
        }
        return object;
    }
    
    public static DBObject generatorQueryCondition(MongoQueryCriterions queryCriterions) {
        List<MongoCriterion> queryCriterionsList = queryCriterions.getCriterionList();

        BasicDBObject object = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        for (MongoCriterion criterion : queryCriterionsList) {
            if (criterion.getOperators().equals(MongoOperators.EQ)) {
                object.put(criterion.getField().getColumn(), criterion.getValue());
            } else if (criterion.getOperators().equals(MongoOperators.LIKE)) {
                Pattern pattern = Pattern.compile("^.*" + criterion.getValue() + ".*$", Pattern.CASE_INSENSITIVE);
                object.put(criterion.getField().getColumn(), pattern);
            } else {
                object.put(criterion.getField().getColumn(), new BasicDBObject(criterion.getOperators().getCode(), criterion.getValue()));
            }
        }
        if (queryCriterions.isMultiCriterion()) {
            object.put(queryCriterions.getLogic().getCode(), values);
        }
        return object;
    }

}
