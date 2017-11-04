package com.enjoyf.platform.util.sql.mongodb;

import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterionLogic;
import org.apache.openjpa.util.Serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 上午9:49
 * Description:
 */
public class MongoQueryCriterions implements Serializable {
    //    private MongoCriterion mongoCriterion;
    //the logic
    private MongoQueryCriterionLogic logic = MongoQueryCriterionLogic.LOGIC_AND;

    private List<MongoCriterion> criterionList = new ArrayList<MongoCriterion>();
    private MongoCriterion criterion = null;

    public MongoQueryCriterions() {
    }

    private MongoQueryCriterions(MongoCriterion criterion) {
        criterionList.add(criterion);
    }

    public static MongoQueryCriterions eq(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.EQ, value));
    }

    private MongoQueryCriterions(MongoCriterion[] criterionses) {
        Collections.addAll(this.criterionList, criterionses);
    }

    //field >= ?
    public static MongoQueryCriterions geq(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.GEQ, value));
    }

    //field <= ?
    public static MongoQueryCriterions leq(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.LEQ, value));
    }

    //field > ?
    public static MongoQueryCriterions gt(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.GT, value));

    }

    //field < ?
    public static MongoQueryCriterions lt(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.LT, value));
    }

    //field <> ?
    public static MongoQueryCriterions ne(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.NE, value));
    }

    //field in (?, ?, ?, ...)
    public static MongoQueryCriterions in(ObjectField field, Object[] value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.IN, value));
    }

    //field not in (?, ?, ?, ...)
    public static MongoQueryCriterions notIn(ObjectField field, Object[] value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.NOT_IN, value));
    }

    public static MongoQueryCriterions like(ObjectField field, Object value) {
        return new MongoQueryCriterions(new MongoCriterion(field, MongoOperators.LIKE, value));
    }

    //AND logic operation
    public static MongoQueryCriterions and(MongoCriterion[] criterions) {
        MongoQueryCriterions returnValue = new MongoQueryCriterions();

        returnValue.setLogic(MongoQueryCriterionLogic.LOGIC_AND);
        for (MongoCriterion criterion : criterions) {
            returnValue.getCriterionList().add(criterion);
        }

        return returnValue;
    }

    public static MongoQueryCriterions or(MongoCriterion[] criterions) {
        MongoQueryCriterions returnValue = new MongoQueryCriterions();

        returnValue.setLogic(MongoQueryCriterionLogic.LOGIC_OR);
        for (MongoCriterion criterion : criterions) {
            returnValue.getCriterionList().add(criterion);
        }

        return returnValue;
    }


    public List<MongoCriterion> getCriterionList() {
        return criterionList;
    }

    public MongoCriterion getCriterion() {
        return criterion;
    }

    public MongoQueryCriterionLogic getLogic() {
        return logic;
    }

    public void setLogic(MongoQueryCriterionLogic logic) {
        this.logic = logic;
    }


    public boolean isMultiCriterion() {
        return criterion == null && criterionList.size() > 1;
    }

    public boolean hasCriterion() {
        return criterion != null || criterionList.size() > 1;
    }
}
