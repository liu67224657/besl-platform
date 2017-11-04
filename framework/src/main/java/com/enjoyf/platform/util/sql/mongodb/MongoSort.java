package com.enjoyf.platform.util.sql.mongodb;

import com.enjoyf.platform.util.sql.ObjectField;
import com.mongodb.BasicDBObject;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 上午9:49
 * Description:
 */
public class MongoSort implements Serializable {
    private ObjectField objectField;
    private MongoSortOrder order;


    public MongoSort(ObjectField field, MongoSortOrder order) {
        this.objectField = field;
        this.order = order;
    }

    public ObjectField getObjectField() {
        return objectField;
    }

    public void setObjectField(ObjectField objectField) {
        this.objectField = objectField;
    }

    public MongoSortOrder getOrder() {
        return order;
    }

    public void setOrder(MongoSortOrder order) {
        this.order = order;
    }
}
