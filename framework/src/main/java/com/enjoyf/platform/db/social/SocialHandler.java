/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.service.social.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class SocialHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private SocialRelationAccessor socialRelationAccessor;
    private RelationCategoryAccessor relationCategoryAccessor;
    private InviteImportInfoAccessor inviteImportInfoAccessor;

    private SocialInviteInfoAccessor socialInviteInfoAccessor;
    private SocialInviteDetailAccessor socialInviteDetailAccessor;

    private SocialRecommendAccessor socialRecommendAccessor;

    private SocialBlackAccessor socialBlackAccessor;

    public SocialHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        socialRelationAccessor = SocialAccessorFactory.factorySocialRelationAccessor(dataBaseType);
        relationCategoryAccessor = SocialAccessorFactory.factoryRelationCategoryAccessor(dataBaseType);
        inviteImportInfoAccessor = SocialAccessorFactory.factoryInviteAccessor(dataBaseType);

        socialInviteInfoAccessor = TableAccessorFactory.get().factoryAccessor(SocialInviteInfoAccessor.class, dataBaseType);
        socialInviteDetailAccessor = TableAccessorFactory.get().factoryAccessor(SocialInviteDetailAccessor.class, dataBaseType);
        socialRecommendAccessor = TableAccessorFactory.get().factoryAccessor(SocialRecommendAccessor.class, dataBaseType);

        socialBlackAccessor = TableAccessorFactory.get().factoryAccessor(SocialBlackAccessor.class, dataBaseType);
    }

    public SocialRelation buildRelation(SocialRelation srcRelation) throws DbException {
        SocialRelation returnValue = null;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            //the src user's relation table.
            SocialRelation srcExitRelation = socialRelationAccessor.get(srcRelation.getSrcUno(), srcRelation.getDestUno(), srcRelation.getRelationType(), conn);

            if (srcExitRelation != null) {
                socialRelationAccessor.updateSrcStatus(srcRelation.getSrcUno(), srcRelation.getDestUno(), srcRelation.getRelationType(), ActStatus.ACTED, conn);

                srcExitRelation.setSrcStatus(ActStatus.ACTED);

                returnValue = srcExitRelation;
            } else {
                srcRelation.setSrcDate(new Date());
                returnValue = socialRelationAccessor.insert(srcRelation, conn);

                // if profile follow
                if (RelationType.FOCUS.equals(srcRelation.getRelationType())) {
                    relationCategoryAccessor.remove(srcRelation.getSrcUno(), srcRelation.getDestUno(), srcRelation.getRelationType(), conn);
                    relationCategoryAccessor.insert(srcRelation.getSrcUno(), srcRelation.getDestUno(), srcRelation.getRelationType(), srcRelation.getCategoryIds(), conn);
                }

            }

            //the dest user's relation table.
            SocialRelation destExitRelation = socialRelationAccessor.get(srcRelation.getDestUno(), srcRelation.getSrcUno(), srcRelation.getRelationType(), conn);
            if (destExitRelation != null) {
                socialRelationAccessor.updateDestStatus(srcRelation.getDestUno(), srcRelation.getSrcUno(), srcRelation.getRelationType(), ActStatus.ACTED, conn);
            } else {
                SocialRelation destRelation = new SocialRelation();

                destRelation.setSrcStatus(ActStatus.UNACT);
                destRelation.setDestStatus(ActStatus.ACTED);
                destRelation.setSrcUno(srcRelation.getDestUno());
                destRelation.setDestUno(srcRelation.getSrcUno());
                destRelation.setRelationType(srcRelation.getRelationType());
                destRelation.setDestDate(new Date());

                socialRelationAccessor.insert(destRelation, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public boolean breakRelation(String srcUno, String destUno, RelationType type) throws DbException {
        boolean returnValue = false;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = socialRelationAccessor.updateSrcStatus(srcUno, destUno, type, ActStatus.UNACT, conn);

            socialRelationAccessor.updateDestStatus(destUno, srcUno, type, ActStatus.UNACT, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public SocialRelation getRelation(String srcUno, String destUno, RelationType type) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRelationAccessor.get(srcUno, destUno, type, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialRelation> querySrcRelations(String srcUno, RelationType type, ActStatus status, Pagination page) throws DbException {
        PageRows<SocialRelation> returnValue = new PageRows<SocialRelation>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialRelationAccessor.querySrcRelations(srcUno, type, status, page, conn));
            returnValue.setPage(page);

            if (!type.getCode().equals(RelationType.SFOCUS.getCode())) {
                for (SocialRelation relation : returnValue.getRows()) {
                    relation.getCategoryIds().addAll(relationCategoryAccessor.queryRelationCategories(srcUno, relation.getDestUno(), relation.getRelationType(), conn));
                }
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<SocialRelation> queryAllSrcRelations(String srcUno, RelationType type, ActStatus status) throws DbException {
        List<SocialRelation> returnValue = new ArrayList<SocialRelation>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = socialRelationAccessor.queryAllSrcRelations(srcUno, type, status, conn);

            if (RelationType.FOCUS.equals(type)) {
                for (SocialRelation relation : returnValue) {
                    relation.getCategoryIds().addAll(relationCategoryAccessor.queryRelationCategories(srcUno, relation.getDestUno(), relation.getRelationType(), conn));
                }
            }

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<SocialRelation> querySrcCategoryRelations(String srcUno, RelationType type, ActStatus status, Long cateId, Pagination page) throws DbException {
        PageRows<SocialRelation> returnValue = new PageRows<SocialRelation>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialRelationAccessor.querySrcCategoryRelations(srcUno, type, status, cateId, page, conn));
            returnValue.setPage(page);

            for (SocialRelation relation : returnValue.getRows()) {
                relation.getCategoryIds().addAll(relationCategoryAccessor.queryRelationCategories(srcUno, relation.getDestUno(), relation.getRelationType(), conn));
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<SocialRelation> queryDestRelations(String srcUno, RelationType type, ActStatus status, Pagination page) throws DbException {
        PageRows<SocialRelation> returnValue = new PageRows<SocialRelation>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialRelationAccessor.queryDestRelations(srcUno, type, status, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public Map<String, SocialRelation> checkRelationsByDestUnos(String srcUno, RelationType type, Set<String> destUnos) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRelationAccessor.queryRelationsByDestUnos(srcUno, type, destUnos, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateRelationDescription(String srcUno, String destUno, RelationType type, String desc) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRelationAccessor.updateSrcDescription(srcUno, destUno, type, desc, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //set the relation categories
    public boolean setRelationCategories(String srcUno, String destUno, RelationType type, Set<Long> categoryIds) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            relationCategoryAccessor.remove(srcUno, destUno, type, conn);

            return relationCategoryAccessor.insert(srcUno, destUno, type, categoryIds, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get the categories
    public Set<Long> queryRelationCategories(String srcUno, String destUno, RelationType type) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return relationCategoryAccessor.queryRelationCategories(srcUno, destUno, type, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //remove somebody from the category
    public boolean removeRelationCategory(String srcUno, String destUno, RelationType type, Long cateId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return relationCategoryAccessor.remove(srcUno, destUno, type, cateId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean removeRelationCategories(String srcUno, RelationType type, Long cateId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return relationCategoryAccessor.remove(srcUno, type, cateId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public void createInviteImportInfo(String inviteUno, String destUno, String destEmail) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            //get InviteInfo by inviteUno and destEmail
            InviteImportInfo inviteImportInfo = inviteImportInfoAccessor.getInvite(inviteUno, destEmail, conn);
            if (inviteImportInfo == null) {
                inviteImportInfo = new InviteImportInfo();
                inviteImportInfo.setSrcUno(inviteUno);
                inviteImportInfo.setDestUno(destUno);
                inviteImportInfo.setDestEmail(destEmail);
                inviteImportInfo.setInvateStatus(ActStatus.ACTED);
                inviteImportInfo.setInviteRelationType(InviteRelationType.SOCIAL);
                inviteImportInfoAccessor.insert(inviteImportInfo, conn);
            } else {
                inviteImportInfoAccessor.updateInviteStatus(inviteUno, destUno, destEmail, ActStatus.ACTED, conn);
            }

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public void insertInviteImportInfos(InviteImportInfo inviteImportInfo) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            inviteImportInfoAccessor.insert(inviteImportInfo, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<InviteImportInfo> queryInviteImportInfo(String uno, ActStatus inviteStatus, Date startDate, Date endDate) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return inviteImportInfoAccessor.selectByDateField(uno, inviteStatus, startDate, endDate, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    public SocialInviteInfo getSocialInviteInfo(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialInviteInfoAccessor.get(getExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialInviteInfo insertSocialInviteInfo(SocialInviteInfo inviteInfo) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialInviteInfoAccessor.insert(inviteInfo, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialInviteDetail insertSocialInviteDetail(SocialInviteDetail socialInviteDetail) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialInviteDetailAccessor.insert(socialInviteDetail, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialInviteDetail> queryInviteDetail(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<SocialInviteDetail> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<SocialInviteDetail> list = socialInviteDetailAccessor.query(queryExpress, pagination, conn);

            returnObj = new PageRows<SocialInviteDetail>();
            returnObj.setRows(list);
            returnObj.setPage(pagination);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnObj;
    }

    public List<SocialInviteDetail> queryInviteDetail(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialInviteDetailAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<SocialRelation> querySocialRealtion(String srcUno, RelationType type, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRelationAccessor.query(srcUno, type, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialRecommend insertSocialRecommend(SocialRecommend socialRecommend) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRecommendAccessor.insert(socialRecommend, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialRecommend getSocialRecommend(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRecommendAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialRecommend(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialRecommendAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public SocialBlack inserSocialBlack(SocialBlack socialBlack) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBlackAccessor.insert(socialBlack, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialBlack getSocialBlack(String ownUno, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBlackAccessor.getSocialBlack(ownUno, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialBlack(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBlackAccessor.updateSocialBlack(ownUno, updateExpress, queryExpress, conn) == 1;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialBlack> querySocialBlackList(String ownUno, QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        PageRows<SocialBlack> returnObj = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialBlack> list = socialBlackAccessor.querySocialBlackList(ownUno, queryExpress, page, conn);
            returnObj = new PageRows<SocialBlack>();
            returnObj.setRows(list);
            returnObj.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return returnObj;
    }

    public List<SocialBlack> queryAllSocialBlackByUno(String ownUno, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBlackAccessor.queryAllSocialBlackList(ownUno, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //todo not use
    public List<SocialRelation> querySocialRelationByRelationTypeAndTable_NO(RelationType relationType,int tablNo)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialRelationAccessor.querySocialRelationByRelationTypeAndTable_NO(relationType, tablNo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
