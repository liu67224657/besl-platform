package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.service.content.ContentField;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <a href="mailto:yinpengyi@platform.com">Yin Pengyi</a>
 */
public class ObjectFieldUtil {

    /////////////////////////////////////////////////////
    //Map APIs
    /////////////////////////////////////////////////////
    public static String generateMapWhereClause(Map<ObjectField, Object> keyValues) {
        StringBuffer returnValue = new StringBuffer();

        int columnNum = keyValues.size();
        int i = 1;

        for (ObjectField field : keyValues.keySet()) {
            returnValue.append(field.getColumn()).append(" = ? ");
            if (i < columnNum) {
                returnValue.append("AND ");
            }
            i++;
        }

        return returnValue.toString();
    }

    public static String generateMapSetClause(Map<ObjectField, Object> keyValues) {
        StringBuffer returnValue = new StringBuffer();

        int columnNum = keyValues.size();
        int i = 1;
        for (ObjectField field : keyValues.keySet()) {
            returnValue.append(field.getColumn()).append(" = ? ");
            if (i < columnNum) {
                returnValue.append(", ");
            }
            i++;
        }

        return returnValue.toString();
    }

    public static String generateMapIncreaseClause(Map<ObjectField, Object> keyValues) {
        StringBuffer returnValue = new StringBuffer();

        int columnNum = keyValues.size();
        int i = 1;
        for (ObjectField field : keyValues.keySet()) {
            returnValue.append(field.getColumn()).append(" = ").append(field.getColumn()).append(" + ? ");
            if (i < columnNum) {
                returnValue.append(", ");
            }
            i++;
        }

        return returnValue.toString();
    }

    public static String generateMapSumClause(Map<ObjectField, Object> keyValues) {
        StringBuffer returnValue = new StringBuffer();

        int columnNum = keyValues.size();
        int i = 1;

        for (ObjectField field : keyValues.keySet()) {
            returnValue.append(field.getColumn()).append(" >= 0 ");
            if (i < columnNum) {
                returnValue.append("AND ");
            }
            i++;
        }

        return returnValue.toString();
    }

    public static int setStmtParams(PreparedStatement stmt, final int startIdx, Map<ObjectField, Object> keyValues) throws SQLException {
        int i = 0;
        for (ObjectField field : keyValues.keySet()) {
            setStmtParam(stmt, i + startIdx, field, keyValues.get(field));

            i++;
        }

        return i + startIdx;
    }

    public static void setStmtParams(PreparedStatement stmt, final int idx, ObjectField key, Object value) throws SQLException {
        setStmtParam(stmt, idx, key, value);
    }

    private static void setStmtParam(PreparedStatement stmt, int index, ObjectField field, Object value) throws SQLException {
        setStmtParam(stmt, index, field.getType(), value);
    }

    private static void setStmtParam(PreparedStatement stmt, int index, ObjectFieldDBType fieldType, Object value) throws SQLException {
        if (fieldType.equals(ObjectFieldDBType.LONG)) {
            if(value == null){
                stmt.setNull(index, Types.BIGINT);
            } else {
                stmt.setLong(index, (Long)value);
            }
        } else if (fieldType.equals(ObjectFieldDBType.INT)) {
            if (value == null) {
                stmt.setNull(index, Types.INTEGER);
            } else {
                stmt.setInt(index, (Integer) value);
            }
        } else if (fieldType.equals(ObjectFieldDBType.BOOLEAN)) {
            if (value == null) {
                stmt.setNull(index, Types.BOOLEAN);
            } else {
                stmt.setString(index, (Boolean) value ? "1" : "0");
            }
        } else if (fieldType.equals(ObjectFieldDBType.STRING)) {
            stmt.setString(index, value == null ? null : (String) value);
        } else if (fieldType.equals(ObjectFieldDBType.TIMESTAMP)) {
            stmt.setTimestamp(index, value == null ? null : new Timestamp(((Date) value).getTime()));
        } else if (fieldType.equals(ObjectFieldDBType.DATE)) {
            stmt.setDate(index, value == null ? null : new java.sql.Date(((Date) value).getTime()));
        } else if (fieldType.equals(ObjectFieldDBType.FLOAT)) {
            if (value == null) {
                stmt.setNull(index, Types.FLOAT);
            } else {
                stmt.setFloat(index, (Float) value);
            }
        } else if (fieldType.equals(ObjectFieldDBType.DOUBLE)) {
            if (value == null) {
                stmt.setNull(index, Types.DOUBLE);
            } else {
                stmt.setDouble(index, (Double) value);
            }
        } else {
            throw new IllegalArgumentException("Not supported field type " + fieldType);
        }
    }

    /////////////////////////////////////////////////////
    //QueryExpress APIs
    /////////////////////////////////////////////////////
    public static String generateQueryClause(QueryExpress express, boolean includeSortClause) {
        if (express == null) {
            return "";
        }

        //
        StringBuilder returnValue = new StringBuilder();

        //append the where key word
        if (express.getQueryCriterions().hasCriterion()) {
            returnValue.append("WHERE ");

            //
            appendQueryWhereClause(returnValue, express.getQueryCriterions(), 1);
        }

        //if the criterion is not null, append " "
        if (express.getQueryCriterions().hasCriterion()) {
            returnValue.append(" ");
        }

        //append the sort clause.
        if (includeSortClause && express.getQuerySorts().size() > 0) {
            appendQuerySortClause(returnValue, express.getQuerySorts());
        }

        return returnValue.toString();
    }

    private static void appendQueryWhereClause(StringBuilder stringBuilder, QueryCriterions criterions, int layer) {
        //append the next
        if (criterions.isMultiCriterion()) {
            //
            int index = 1;
            int size = criterions.getCriterionsList().size();

            //
            for (QueryCriterions next : criterions.getCriterionsList()) {
                //add the (
                if (size > 1 && index == 1 && layer > 1) {
                    stringBuilder.append("(");
                }

                //append the logic key: OR or AND
                if (index > 1) {
                    stringBuilder.append(" ").append(criterions.getLogic().getCode()).append(" ");
                }

                appendQueryWhereClause(stringBuilder, next, layer + 1);

                //add the )
                if (size > 1 && index == size && layer > 1) {
                    stringBuilder.append(")");
                }

                index++;
            }
        } else {
            appendQueryColumnClause(stringBuilder, criterions.getCriterion());
        }
    }

    private static void appendQueryColumnClause(StringBuilder stringBuilder, QueryCriterion criterion) {
        stringBuilder.append(criterion.getField().getColumn()).append(" ")
                .append(criterion.getRelation().getCode());

        //
        if (criterion.getRelation().equals(QueryCriterionRelation.BETWEEN)) {
            stringBuilder.append(" ");

            //
            for (int i = 0; i < 2; i++) {
                stringBuilder.append("?");

                if (i < 1) {
                    stringBuilder.append(" AND ");
                }
            }
        } else if (criterion.getRelation().equals(QueryCriterionRelation.IS_NOT_NULL) ||
                criterion.getRelation().equals(QueryCriterionRelation.IS_NULL)) {
            //do nothing.
        } else if (criterion.getRelation().equals(QueryCriterionRelation.BITEWISE_AND) ||
                criterion.getRelation().equals(QueryCriterionRelation.BITEWISE_OR)) {
            //
            stringBuilder.append(" ").append("?").append(" ").append(criterion.getRelation2().getCode()).append(" ").append("?");
        } else if (criterion.getRelation().equals(QueryCriterionRelation.IN) ||
                criterion.getRelation().equals(QueryCriterionRelation.NOT_IN)) {
            //
            Object[] value = (Object[]) criterion.getValue();

            //
            stringBuilder.append(" ").append("(");

            //
            for (int i = 0; i < value.length; i++) {
                stringBuilder.append("?");

                if (i < value.length - 1) {
                    stringBuilder.append(", ");
                }
            }

            //
            stringBuilder.append(")");
        } else {
            stringBuilder.append(" ").append("?");
        }
    }


    private static void appendQuerySortClause(StringBuilder stringBuilder, List<QuerySort> querySorts) {
        //add ORDER BY
        if (querySorts.size() > 0) {
            stringBuilder.append("ORDER BY ");
        }

        //
        int i = 1;
        for (QuerySort sort : querySorts) {
            stringBuilder.append(sort.getField().getColumn()).append(" ").append(sort.getOrder().getCode());

            if (i < querySorts.size()) {
                stringBuilder.append(", ");
            }

            i++;
        }
    }

    public static int setStmtParams(PreparedStatement stmt, final int startIdx, QueryExpress express) throws SQLException {
        int returnValue = startIdx;

        if (express == null) {
            return returnValue;
        }

        //append the where key word
        if (express.getQueryCriterions().hasCriterion()) {
            returnValue = setStmtParams(stmt, returnValue, express.getQueryCriterions());
        }

        return returnValue;
    }


    private static int setStmtParams(PreparedStatement stmt, final int startIdx, QueryCriterions criterions) throws SQLException {
        int returnValue = startIdx;

        //append the next
        if (criterions.isMultiCriterion()) {
            //
            for (QueryCriterions next : criterions.getCriterionsList()) {
                returnValue = setStmtParams(stmt, returnValue, next);
            }
        } else {
            //
            returnValue = setColumnStmtParams(stmt, returnValue, criterions.getCriterion());
        }

        return returnValue;
    }


    private static int setColumnStmtParams(PreparedStatement stmt, final int startIdx, QueryCriterion criterion) throws SQLException {
        int returnValue = startIdx;

        //
        if (criterion.getRelation().equals(QueryCriterionRelation.BETWEEN)) {
            Object[] vs = (Object[]) criterion.getValue();

            //
            ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), vs[0]);
            ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), vs[1]);
        } else if (criterion.getRelation().equals(QueryCriterionRelation.IS_NOT_NULL) ||
                criterion.getRelation().equals(QueryCriterionRelation.IS_NULL)) {
            //do nothing.
        } else if (criterion.getRelation().equals(QueryCriterionRelation.BITEWISE_AND) ||
                criterion.getRelation().equals(QueryCriterionRelation.BITEWISE_OR)) {
            //
            ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), criterion.getValue());
            ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), criterion.getValue2());
        } else if (criterion.getRelation().equals(QueryCriterionRelation.IN) ||
                criterion.getRelation().equals(QueryCriterionRelation.NOT_IN)) {
            //
            Object[] value = (Object[]) criterion.getValue();

            //
            for (Object v : value) {
                ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), v);
            }
        } else {
            ObjectFieldUtil.setStmtParams(stmt, returnValue++, criterion.getField(), criterion.getValue());
        }

        return returnValue;
    }

    /////////////////////////////////////////////////////
    //UpdateExpress APIs
    /////////////////////////////////////////////////////
    public static String generateUpdateClause(UpdateExpress express) {
        if (express == null) {
            return "";
        }

        //
        StringBuilder returnValue = new StringBuilder();

        //append the where key word
        if (express.getUpdateAttributes().size() > 0) {
            returnValue.append("SET ");

            int index = 1;
            int size = express.getUpdateAttributes().size();
            for (UpdateAttribute attribute : express.getUpdateAttributes()) {
                if (attribute.getUpdateType().equals(UpdateType.INCREASE)) {
                    returnValue.append(attribute.getField().getColumn()).append(" = ").append(attribute.getField().getColumn()).append(" + ?");
                } else {
                    returnValue.append(attribute.getField().getColumn()).append(" = ?");
                }

                if (index < size) {
                    returnValue.append(", ");
                }

                index++;
            }
        }


        return returnValue.toString();
    }


    public static int setStmtParams(PreparedStatement stmt, final int startIdx, UpdateExpress express) throws SQLException {
        int returnValue = startIdx;

        if (express == null) {
            return returnValue;
        }

        //append the where key word
        if (express.getUpdateAttributes().size() > 0) {
            //
            for (UpdateAttribute attribute : express.getUpdateAttributes()) {
                ObjectFieldUtil.setStmtParams(stmt, returnValue, attribute.getField(), attribute.getValue());

                returnValue++;
            }
        }

        return returnValue;
    }

    //test example.
    public static void main(String[] args) {
        QueryExpress express = new QueryExpress();

        express.add(QueryCriterions.eq(ContentField.CONTENTTAG, "tag"))
                .add(QueryCriterions.gt(ContentField.CONTENTBODY, "body"))
                .add(QueryCriterions.bitwiseAnd(ContentField.CONTENTTYPE, QueryCriterionRelation.EQ, 10, 0))
                .add(QueryCriterions.or(
                        QueryCriterions.in(ContentField.DINGIMES, new Object[]{"", "", "", ""}),
                        QueryCriterions.between(ContentField.FAVORTIMES, 1, 23)
                ))
                .add(QueryCriterions.or(
                        new QueryCriterions[]{
                                QueryCriterions.in(ContentField.FORWARDTIMES, new Object[]{"", ""}),
                                QueryCriterions.between(ContentField.IMAGES, 1, 23),
                                QueryCriterions.and(new QueryCriterions[]{
                                        QueryCriterions.eq(ContentField.CONTENTTAG, "tag"),
                                        QueryCriterions.eq(ContentField.CONTENTTAG, "tag2"),
                                        QueryCriterions.eq(ContentField.CONTENTTAG, "tag3")
                                })
                        }
                ))
                .add(QueryCriterions.isNotNull(ContentField.AUDIOS))
                .add(QuerySort.add(ContentField.APPS, QuerySortOrder.DESC))
                .add(QuerySort.add(ContentField.AUDIOS, QuerySortOrder.DESC));


        System.out.println(generateQueryClause(express, false));


        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(ContentField.APPS, "sdfdsf");
        updateExpress.increase(ContentField.FAVORTIMES, 123);
        updateExpress.increase(ContentField.CAITIMES, 234);
        updateExpress.set(ContentField.DINGIMES, "687");


        System.out.println(generateUpdateClause(updateExpress));
    }
}
