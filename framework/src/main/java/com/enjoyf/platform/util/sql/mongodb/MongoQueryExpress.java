package com.enjoyf.platform.util.sql.mongodb;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 上午11:59
 * Description:
 */
public class MongoQueryExpress implements Serializable {
    //
    private List<MongoQueryCriterions> queryCriterions = new ArrayList<MongoQueryCriterions>();

//    private MongoSort mongoSort;
    
    private MongoSort[] mongoSortArray;

    //
    public MongoQueryExpress() {
        //
    }

    public MongoQueryExpress add(MongoQueryCriterions criterions) {
        queryCriterions.add(criterions);

        return this;
    }

    public MongoQueryExpress add(MongoSort[] mongoSortArray) {
//        this.mongoSort = mongoSort;
        this.mongoSortArray=mongoSortArray;
        return this;
    }

    public List<MongoQueryCriterions> getQueryCriterions() {
        return queryCriterions;
    }

//    public MongoSort getMongoSort() {
//        return mongoSort;
//    }
//
//    public void setMongoSort(MongoSort mongoSort) {
//        this.mongoSort = mongoSort;
//    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

	public MongoSort[] getMongoSortArray() {
		return mongoSortArray;
	}

	public void setMongoSortArray(MongoSort[] mongoSortArray) {
		this.mongoSortArray = mongoSortArray;
	}
    
}
