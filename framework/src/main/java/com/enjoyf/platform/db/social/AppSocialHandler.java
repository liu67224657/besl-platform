package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class AppSocialHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ProfileRelationAccessor profileRelationAccessor;
    private ObjectRelationAccessor objectRelationAccessor;
    private UserRelationAccessor userRelationAccessor;

    public AppSocialHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the data source
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        profileRelationAccessor = TableAccessorFactory.get().factoryAccessor(ProfileRelationAccessor.class, dataBaseType);
        objectRelationAccessor = TableAccessorFactory.get().factoryAccessor(ObjectRelationAccessor.class, dataBaseType);

        userRelationAccessor = TableAccessorFactory.get().factoryAccessor(UserRelationAccessor.class, dataBaseType);
    }


    public ObjectRelation insertObjectRelation(ObjectRelation relation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return objectRelationAccessor.insert(relation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateObjectRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return objectRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ObjectRelation getObjectRelation(QueryExpress queryExpress) throws ServiceException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return objectRelationAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ProfileRelation createProfileRelation(ProfileRelation relation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            relation.setRelationId(StringUtil.isEmpty(relation.getRelationId()) ? SocialUtil.getSocialRelationId(relation.getSrcProfileId(), relation.getDestProfileId(), relation.getProfileKey(), relation.getType()) : relation.getRelationId());
            //判断src---dest是否存在，如果存在update srcstatus，如果不存在insert
            ProfileRelation srcRelation = profileRelationAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(ProfileRelationField.PROFILE_RELATION_ID, relation.getRelationId())), conn);

            if (srcRelation != null && srcRelation.getSrcStatus().equals(IntValidStatus.VALID)) {
                return null;
            }
            boolean result = true;
            if (srcRelation == null) {
                relation = profileRelationAccessor.insert(relation, conn);
            } else {
                result = profileRelationAccessor.update(new UpdateExpress()
                                .set(ProfileRelationField.SRC_STATUS, relation.getSrcStatus().getCode())
                                .set(ProfileRelationField.DEST_STATUS, relation.getDestStatus().getCode()),
                        new QueryExpress().add(QueryCriterions.eq(ProfileRelationField.PROFILE_RELATION_ID, srcRelation.getRelationId())), conn) > 0;
            }

            String destRelationId = SocialUtil.getSocialRelationId(relation.getDestProfileId(), relation.getSrcProfileId(), relation.getProfileKey(), relation.getType());
            ProfileRelation destRelation = profileRelationAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(ProfileRelationField.PROFILE_RELATION_ID, destRelationId)), conn);
            if (destRelation != null) {
                relation.setDestStatus(destRelation.getSrcStatus());
            }

            //判断dest---src是否存在，如果存在update deststatus，
            //否则返回(当A关注B的时候 A的destrelation(b->a)是null.B关注A时候,B的destrealiton(a->b)不是null修改状态)
            if (result && destRelation != null) {
                result = profileRelationAccessor.update(new UpdateExpress()
                                .set(ProfileRelationField.DEST_STATUS, relation.getSrcStatus().getCode()),
                        new QueryExpress().add(QueryCriterions.eq(ProfileRelationField.PROFILE_RELATION_ID, destRelation.getRelationId())), conn) > 0;
            }

            return relation;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean breakRelation(String srcProfileId, String destProfileId, ObjectRelationType type, String profileKey) throws DbException {
        boolean returnValue = false;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ProfileRelationField.SRC_STATUS, IntValidStatus.UNVALID.getCode());

            returnValue = profileRelationAccessor.update(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(ProfileRelationField.SRC_PROFILEID, srcProfileId))
                    .add(QueryCriterions.eq(ProfileRelationField.DEST_PROFILEID, destProfileId))
                    .add(QueryCriterions.eq(ProfileRelationField.RELATIONTYPE, type.getCode()))
                    .add(QueryCriterions.eq(ProfileRelationField.PROFILEKEY, profileKey)), conn) > 0;

            if (returnValue) {
                profileRelationAccessor.update(updateExpress, new QueryExpress()
                        .add(QueryCriterions.eq(ProfileRelationField.SRC_PROFILEID, destProfileId))
                        .add(QueryCriterions.eq(ProfileRelationField.DEST_PROFILEID, srcProfileId))
                        .add(QueryCriterions.eq(ProfileRelationField.RELATIONTYPE, type.getCode()))
                        .add(QueryCriterions.eq(ProfileRelationField.PROFILEKEY, profileKey)), conn);
            }

            return returnValue;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ProfileRelation> queryProfileRelation(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ProfileRelation> list = profileRelationAccessor.query(queryExpress, pagination, conn);

            PageRows<ProfileRelation> pageRows = new PageRows<ProfileRelation>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ObjectRelation> queryObjectRelation(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ObjectRelation> list = objectRelationAccessor.query(queryExpress, pagination, conn);

            PageRows<ObjectRelation> pageRows = new PageRows<ObjectRelation>();
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ObjectRelation> queryObjectRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return objectRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserRelation buildUserRelation(UserRelation userRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            userRelation.setRelationId(StringUtil.isEmpty(userRelation.getRelationId()) ? SocialUtil.getSocialRelationId(userRelation.getSrcProfileid(), userRelation.getDestProfileid(), userRelation.getProfilekey(), userRelation.getRelationType()) : userRelation.getRelationId());
            //判断src---dest是否存在，如果存在update srcstatus，如果不存在insert
            UserRelation srcRelation = userRelationAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(UserRelationField.RELATION_ID, userRelation.getRelationId())), conn);
            if (srcRelation != null && (srcRelation.getSrcStatus().equals(RelationStatus.FOCUS))) {
                return null;
            }
            boolean result = true;
            if (srcRelation == null) {
                userRelation = userRelationAccessor.insert(userRelation, conn);
            } else {
                result = userRelationAccessor.update(new UpdateExpress()
                                .set(UserRelationField.SRC_STATUS, userRelation.getSrcStatus().getCode()),
                        new QueryExpress().add(QueryCriterions.eq(UserRelationField.RELATION_ID, srcRelation.getRelationId())), conn) > 0;
            }

            if (!result) {
                return null;
            }

            String destRelationId = SocialUtil.getSocialRelationId(userRelation.getDestProfileid(), userRelation.getSrcProfileid(), userRelation.getProfilekey(), userRelation.getRelationType());
            UserRelation destRelation = userRelationAccessor.get(new QueryExpress()
                    .add(QueryCriterions.eq(UserRelationField.RELATION_ID, destRelationId)), conn);
            if (destRelation != null) {

            }

            //判断dest---src是否存在，如果存在update deststatus，
            //否则返回(当A关注B的时候 A的destrelation(b->a)是null.B关注A时候,B的destrealiton(a->b)不是null修改状态)
            if (destRelation != null) {
                destRelation.setDestStatus(userRelation.getSrcStatus());
                result = userRelationAccessor.update(new UpdateExpress()
                                .set(UserRelationField.DEST_STATUS, userRelation.getSrcStatus().getCode()),
                        new QueryExpress().add(QueryCriterions.eq(UserRelationField.RELATION_ID, destRelation.getRelationId())), conn) > 0;
            } else {
                destRelation = new UserRelation();
                destRelation.setRelationId(destRelationId);
                destRelation.setSrcProfileid(userRelation.getDestProfileid());
                destRelation.setModifyTime(userRelation.getModifyTime());
                destRelation.setDestProfileid(userRelation.getSrcProfileid());
                destRelation.setExtstring(userRelation.getExtstring());
                destRelation.setDestStatus(userRelation.getSrcStatus());
                destRelation.setSrcStatus(userRelation.getDestStatus());
                destRelation.setRelationType(userRelation.getRelationType());
                destRelation.setProfilekey(userRelation.getProfilekey());
                destRelation.setModifyIp(userRelation.getModifyIp());
                userRelationAccessor.insert(destRelation, conn);
            }

            return userRelation;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean breakUserRelation(String srcProfileId, String destProfileId, ObjectRelationType type) throws DbException {
        boolean returnValue = false;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            UpdateExpress updateExpress = new UpdateExpress()
                    .set(UserRelationField.SRC_STATUS, RelationStatus.UNFOCUS.getCode());
            returnValue = userRelationAccessor.update(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, srcProfileId))
                    .add(QueryCriterions.eq(UserRelationField.DEST_PROFILEID, destProfileId))
                    .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode())), conn) > 0;

            if (returnValue) {
                UpdateExpress destStatusUpdateExpress = new UpdateExpress()
                        .set(UserRelationField.DEST_STATUS, RelationStatus.UNFOCUS.getCode());
                returnValue=userRelationAccessor.update(destStatusUpdateExpress, new QueryExpress()
                        .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, destProfileId))
                        .add(QueryCriterions.eq(UserRelationField.DEST_PROFILEID, srcProfileId))
                        .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode())), conn)>0;
            }

            return returnValue;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserRelation getUserRelation(String srcUno, String destUno, ObjectRelationType type) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userRelationAccessor.get(srcUno, destUno, type, conn);
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

    public PageRows<UserRelation> queryUserRelationByDestProfileId(String srcUno, ObjectRelationType type, Pagination page) throws DbException {
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

    public PageRows<UserRelation> queryUserRelationBySrcProfileId(String srcUno, ObjectRelationType type, Pagination page) throws DbException {
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

    public List<UserRelation> queryUserRelation(QueryExpress queryExpress) throws DbException {
        PageRows<UserRelation> returnValue = new PageRows<UserRelation>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userRelationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }
}
