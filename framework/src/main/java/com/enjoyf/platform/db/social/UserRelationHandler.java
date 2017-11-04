package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.social.SocialUtil;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.social.UserRelationField;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="wengangsai@straff.joyme.com">saiwengang</a>
 * Create time: 14/12/10
 * Description:
 */
public class UserRelationHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private UserRelationAccessor userRelationAccessor;

    public UserRelationHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the data source
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        userRelationAccessor = TableAccessorFactory.get().factoryAccessor(UserRelationAccessor.class, dataBaseType);
    }


    public UserRelation buildRelation(UserRelation relation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            relation.setRelationId(StringUtil.isEmpty(relation.getRelationId()) ? SocialUtil.getSocialRelationId(relation.getSrcProfileid(), relation.getDestProfileid(), relation.getProfilekey(), relation.getRelationType()) : relation.getRelationId());

            relation = userRelationAccessor.insert(relation, conn);

            return relation;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean cancelRelation(String srcProfileId, String destProfileId, ObjectRelationType type, String profileKey) throws DbException {
        boolean returnValue = false;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            UpdateExpress updateExpress = new UpdateExpress()
                    .set(UserRelationField.SRC_STATUS, IntValidStatus.UNVALID.getCode());

            returnValue = userRelationAccessor.update(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, srcProfileId))
                    .add(QueryCriterions.eq(UserRelationField.DEST_PROFILEID, destProfileId))
                    .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode())), conn) > 0;

            if (returnValue) {
                userRelationAccessor.update(updateExpress, new QueryExpress()
                        .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, destProfileId))
                        .add(QueryCriterions.eq(UserRelationField.DEST_PROFILEID, srcProfileId))
                        .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode())), conn);
            }

            return returnValue;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserRelation getRelation(String srcUno, String destUno, ObjectRelationType type) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userRelationAccessor.get(srcUno, destUno, type, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserRelation> queryUserRelation(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<UserRelation> list = userRelationAccessor.query(queryExpress, pagination, conn);

            PageRows<UserRelation> pageRows = new PageRows<UserRelation>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int updateUserRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userRelationAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserRelation> queryUserRelationList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserRelation> queryFocusUser(String srcUno, ObjectRelationType type, Pagination page) throws DbException {
        PageRows<UserRelation> returnValue = new PageRows<UserRelation>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(userRelationAccessor.queryByDestProfileId(srcUno, type, page, conn));
            returnValue.setPage(page);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<UserRelation> queryFollowUser(String srcUno, ObjectRelationType type, Pagination page) throws DbException {
        PageRows<UserRelation> returnValue = new PageRows<UserRelation>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(userRelationAccessor.queryBySrcProfileId(srcUno, type, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }
}
