/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.advertise.AdvertiseIdGenerator;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.point.ActivityHotRanksAccessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.content.stats.TopInteractionContent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-16 下午10:56
 * Description:   博文与回应接口方法
 */
public class ContentHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private ContentAccessor contentAccessor;
    private ResourceFileAccessor resourceFileAccessor;
    private ContentInteractionAccessor contentInteractionAccessor;
    private ContentInteractionStatAccessor contentInteractionStatAccessor;
    private ContentRelationAccessor contentRelationAccessor;
    private ActivityAccessor activityAccessor;
    private ActivityRelationAccessor activityRelationAccessor;

    private ForignContentReplyAccessor forignContentReplyAccessor;
    private ForignContentReplyLogAccessor forignContentReplyLogAccessor;
    private ForignContentAccessor forignContentAccessor;

    private SocialContentReplyAccessor socialContentReplyAccessor;

    private SocialContentAccessor socialContentAccessor;
    private SocialContentActionAccessor socialContentActionAccessor;
    private SocialHotContentAccessor socialHotContentAccessor;

    private SocialReportAccessor socialReportAccessor;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private SocialActivityAccessor socialActivityAccessor;
    private SocialContentActivityAccessor socialContentActivityAccessor;
    private SocialTagAccessor socialTagAccessor;
    private SocialWatermarkAccessor socialWatermarkAccessor;
    private SocialBackgroundAudioAccessor socialBackgroundAudioAccessor;

    public ContentHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        contentAccessor = ContentAccessorFactory.factoryContentAccessor(dataBaseType);
        resourceFileAccessor = ContentAccessorFactory.factoryResourceFileAccessor(dataBaseType);
        contentInteractionAccessor = ContentAccessorFactory.factoryInteractionAccessorAccessor(dataBaseType);
        contentInteractionStatAccessor = ContentAccessorFactory.factoryInteractionStatAccessorAccessor(dataBaseType);
        contentRelationAccessor = TableAccessorFactory.get().factoryAccessor(ContentRelationAccessor.class, dataBaseType);
        activityAccessor = TableAccessorFactory.get().factoryAccessor(ActivityAccessor.class, dataBaseType);
        activityRelationAccessor = TableAccessorFactory.get().factoryAccessor(ActivityRelationAccessor.class, dataBaseType);

        forignContentAccessor = TableAccessorFactory.get().factoryAccessor(ForignContentAccessor.class, dataBaseType);
        forignContentReplyAccessor = TableAccessorFactory.get().factoryAccessor(ForignContentReplyAccessor.class, dataBaseType);
        forignContentReplyLogAccessor = TableAccessorFactory.get().factoryAccessor(ForignContentReplyLogAccessor.class, dataBaseType);

        socialContentReplyAccessor = TableAccessorFactory.get().factoryAccessor(SocialContentReplyAccessor.class, dataBaseType);

        socialContentAccessor = TableAccessorFactory.get().factoryAccessor(SocialContentAccessor.class, dataBaseType);
        socialContentActionAccessor = TableAccessorFactory.get().factoryAccessor(SocialContentActionAccessor.class, dataBaseType);
        socialHotContentAccessor = TableAccessorFactory.get().factoryAccessor(SocialHotContentAccessor.class, dataBaseType);
        socialReportAccessor = TableAccessorFactory.get().factoryAccessor(SocialReportAccessor.class, dataBaseType);

        socialActivityAccessor = TableAccessorFactory.get().factoryAccessor(SocialActivityAccessor.class, dataBaseType);
        socialContentActivityAccessor = TableAccessorFactory.get().factoryAccessor(SocialContentActivityAccessor.class, dataBaseType);
        socialTagAccessor = TableAccessorFactory.get().factoryAccessor(SocialTagAccessor.class, dataBaseType);
        socialWatermarkAccessor = TableAccessorFactory.get().factoryAccessor(SocialWatermarkAccessor.class, dataBaseType);
        socialBackgroundAudioAccessor = TableAccessorFactory.get().factoryAccessor(SocialBackgroundAudioAccessor.class, dataBaseType);
    }

    /**
     * 新建博文
     *
     * @param content
     * @throws DbException
     */
    public Content insertContent(Content content) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.insert(content, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Content> queryContentByQuery(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.query(queryExpress, null, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 删除博文
     */
    public Boolean removeContent(String contentId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.remove(contentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 更新博文中收藏数或转发数或回复数
     */
    public Boolean updateContentNum(String contentId, ObjectField field, Integer value) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.updateContentNum(contentId, field, value, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 更新博文内容
     */
    public Boolean updateContent(String contentId, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentAccessor.updateContent(contentId, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     * 查询博文内容，根据ID,UNO
     */
    public Content getContentByCidUno(String contentId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            Content content = contentAccessor.getByCidUno(contentId, conn);
            if (content != null) {
                content.setRelationSet(buildRelationSet(contentId, conn));
            }
            return content;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Content> queryContents(String uno, Pagination page) throws DbException {
        PageRows<Content> returnValue = new PageRows<Content>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.queryContents(uno, page, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            returnValue.setRows(contentList);
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }


    public PageRows<Content> queryContentsByDateStep(Date startDate, Date endDate, Pagination page) throws DbException {
        PageRows<Content> returnValue = new PageRows<Content>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.queryContentsByTimeStep(startDate, endDate, page, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            returnValue.setRows(contentList);
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }


    public List<Content> queryContents(Set<String> contentIds) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.queryContents(contentIds, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            return contentList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Content> queryLastestContents(ContentPublishType type, ContentType contentType, Integer size) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.queryLastestContents(type, contentType, size, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            return contentList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Content> queryLastestContents(String uno, Integer size) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.queryLastestContents(uno, size, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            return contentList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Content> query(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Content> contentList = contentAccessor.query(queryExpress, page, conn);

            //build relation
            for (Content content : contentList) {
                if (content != null) {
                    content.setRelationSet(buildRelationSet(content.getContentId(), conn));
                }
            }

            return contentList;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    //The resource file handler apis.
    ///////////////////////////////////////////////////
    //insert ResourceFile
    public ResourceFile insertResourceFile(ResourceFile file) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return resourceFileAccessor.insert(file, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //increate the file used times
    public boolean increaseResourceFileUsedTimes(String fileId, int delta) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return resourceFileAccessor.increaseUsedTimes(fileId, delta, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //update resource file
    public boolean updateResourceFile(String fileId, Map<ObjectField, Object> map) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return resourceFileAccessor.update(fileId, map, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////
    public ContentInteraction insertInterAction(ContentInteraction interaction) throws DbException {
        Connection conn = null;

        ContentInteraction returnObj = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            int TRYTIMES = 3;
            int i = 0;
            do {
                i++;
                try {
                    if (interaction.getInteractionType().equals(InteractionType.REPLY) && StringUtil.isEmpty(interaction.getRootId())) {
                        interaction.setFloorNo(contentInteractionAccessor.getMaxFloorNum(interaction.getContentId(), conn) + 1);
                    } else {
                        interaction.setFloorNo(contentInteractionAccessor.getFloorNum(conn));
                    }

                    returnObj = contentInteractionAccessor.insert(interaction, conn);

                    break;
                } catch (DbException e) {
                    GAlerter.lab(" insert interaction error.trytimes:" + i, e);
                }
            } while (i < TRYTIMES);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }


    public ContentInteraction getInteraction(String contentId, QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.getInteraction(contentId, getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateInteraction(String contentId, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return contentInteractionAccessor.updateInteraction(contentId, updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ContentInteraction> queryInteractionsByPage(String contentId, QueryExpress queryExpress, Pagination page) throws DbException {

        Connection conn = null;
        try {
            PageRows<ContentInteraction> returnValue = new PageRows<ContentInteraction>();
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(contentInteractionAccessor.queryInteraction(contentId, queryExpress, page, conn));
            returnValue.setPage(page);

            return returnValue;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<ContentInteraction> queryInteractions(List<String> iIds, String cid) throws DbException {

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.queryByIIdsCid(iIds, cid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ContentInteraction> queryInteraction(String contentId, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.queryInteraction(contentId, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination p) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.queryContentReply(queryExpress, p, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<TopInteractionContent> queryTopInteractionContents(InteractionType type, Date from, Date to, int size) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionStatAccessor.queryTopInteractionContents(type, from, to, size, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int countByQueryExpress(String contentId, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.countByQueryExpress(contentId, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public long queryContentReplyTimes(String contentId, Date from, Date to) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentInteractionAccessor.queryReplyTimes(contentId, from, to, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentRelation insertRelation(ContentRelation contentRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentRelationAccessor.insert(contentRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ContentRelation getRelation(QueryExpress getExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentRelationAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return contentRelationAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    private ContentRelationSet buildRelationSet(String contentId, Connection connection) {
        ContentRelationSet returnObj = null;

        try {
            List<ContentRelation> relationSetList = contentRelationAccessor.query(new QueryExpress()
                    .add(QueryCriterions.eq(ContentRelationField.CONTENTID, contentId))
                    .add(QueryCriterions.eq(ContentRelationField.REMOVESTATUS, ActStatus.UNACT.getCode())), connection);

            if (CollectionUtil.isEmpty(relationSetList)) {
                return returnObj;
            }

            returnObj = new ContentRelationSet();
            Map<String, ContentRelation> gameRelationMap = new HashMap<String, ContentRelation>();
            for (ContentRelation contentRelation : relationSetList) {
                if (ContentRelationType.GAME.equals(contentRelation.getRelationType())) {
                    gameRelationMap.put(contentRelation.getRelationId(), contentRelation);
                } else if (ContentRelationType.GROUP.equals(contentRelation.getRelationType())) {
                    returnObj.setGroupRelation(contentRelation);
                } else if (contentRelation.getRelationType().equals(ContentRelationType.ADMIN_ADJUST_POINT)) {
                    returnObj.setGroupPointRelation(contentRelation);
                }
            }
            returnObj.setGameRelationMap(gameRelationMap);
        } catch (DbException e) {
            GAlerter.lab("build content relation set occured DbException.e: ", e);
        }
        return returnObj;
    }


    public Activity insertActivity(Activity activity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);
            AdvertiseAppUrl advertiseAppUrl = new AdvertiseAppUrl();
            advertiseAppUrl.setIosUrl(activity.getIosDownloadUrl());
            advertiseAppUrl.setAndroidUrl(activity.getAndroidDownloadUrl());
            advertiseAppUrl.setRemoveStatus(ActStatus.UNACT);
            advertiseAppUrl.setCreateId(activity.getCreateUserId());
            advertiseAppUrl.setCreateIp(activity.getCreateIp());
            advertiseAppUrl.setCreateTime(new Date());
            advertiseAppUrl.setCode(AdvertiseIdGenerator.generateAppUrlId(System.currentTimeMillis()));
            advertiseAppUrl = AdvertiseServiceSngl.get().insertAppUrl(advertiseAppUrl);
            if (!"".equals(advertiseAppUrl.getCode())) {
                activity.setQrUrl(advertiseAppUrl.getCode());
            }

            activity = activityAccessor.insert(activity, conn);
            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(e);
        } catch (ServiceException e) {
            DataBaseUtil.rollbackConnection(conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return activity;
    }

    public Activity getActivity(Long acticityId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityAccessor.get(new QueryExpress().add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, acticityId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);

        }
    }

    public List<Activity> listActivity(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityAccessor.select(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Activity> pageActivity(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<Activity> page = new PageRows<Activity>();
        try {

            conn = DbConnFactory.factory(dataSourceName);
            List<Activity> list = activityAccessor.select(queryExpress, pagination, conn);
            page.setRows(list);
            page.setPage(pagination);
            return page;

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyActivity(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityAccessor.modify(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ActivityRelation insertActivityRelation(ActivityRelation activityRelation) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityRelationAccessor.insert(activityRelation, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ActivityRelation> queryActivityRelationList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityRelationAccessor.select(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityRelation getActivityRelation(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityRelationAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////
    public ForignContentReply insertForignContentReply(ForignContentReply reply) throws DbException {
        ForignContentReply returnObj = null;

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            //get Max follorNo
            int follor = forignContentReplyAccessor.getMaxFollorNum(reply.getRootId(), reply.getContentId(), conn) + 1;
            reply.setFloorNum(follor);

            returnObj = forignContentReplyAccessor.insert(reply, conn);

            conn.commit();
        } catch (DbException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw e;
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return returnObj;
    }

    public ForignContentReply getForignContentReplyById(long replyId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentReplyAccessor.getById(replyId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ForignContentReply> queryContentReplyList(long contentId, long rootId, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ForignContentReply> pageRows = new PageRows<ForignContentReply>();
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<ForignContentReply> list = forignContentReplyAccessor.queryByRootId(contentId, rootId, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public List<ForignContentReply> queryForignContentReply(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentReplyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Long> queryHotForignReplyId(long contentId, int size) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentReplyAccessor.queryHotReplyId(contentId, size, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateForignContentReply(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentReplyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public ForignContent insertForignContent(ForignContent forignContent) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentAccessor.insert(forignContent, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ForignContent queryForignContentById(String fid, ForignContentDomain domain) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentAccessor.getByFroignId(fid, domain, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateForignContent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ForignContent getForignContentById(long contentId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentAccessor.getByContentId(contentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<ForignContentReply> queryForignContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ForignContentReply> pageRows = new PageRows<ForignContentReply>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ForignContentReply> list = forignContentReplyAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public boolean modifyForignContentReplay(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return forignContentReplyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ForignContentReplyLog createForignContentReplyLog(ForignContentReplyLog replyLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return forignContentReplyLogAccessor.insert(replyLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ForignContentReplyLog getForignContentReplyLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return forignContentReplyLogAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int getForignContentReplySum(long contentId, long rootId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return forignContentReplyAccessor.getMaxFollorNum(rootId, contentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ForignContent> queryForignContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return forignContentAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////
    public SocialContent insertSocialContent(SocialContent socialContent) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentAccessor.insert(socialContent, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContent getSocialContentByContentId(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentAccessor.getByContentId(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContent getSocialContentByContentId(long contentId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentAccessor.getByContentId(contentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateSocialContent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialContent> querySocialContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialContent> querySocialContentByUno(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        PageRows<SocialContent> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows = new PageRows<SocialContent>();
            pageRows.setRows(socialContentAccessor.queryByUno(queryExpress, page, conn));
            pageRows.setPage(page);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContentReply insertSocialContentReply(SocialContentReply socialContentReply) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentReplyAccessor.insert(socialContentReply, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialContentReply(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentReplyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<SocialContentReply> querySocialContentReplyList(long contentId, Long rootId, Pagination page) throws DbException {
        Connection conn = null;
        PageRows<SocialContentReply> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            pageRows = new PageRows<SocialContentReply>();
            pageRows.setRows(socialContentReplyAccessor.queryByRootId(contentId, rootId, page, conn));
            pageRows.setPage(page);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<SocialContentReply> querySocialContentReply(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentReplyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialContentReply> querySocialContentReplyByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        PageRows<SocialContentReply> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            pageRows = new PageRows<SocialContentReply>();
            pageRows.setRows(socialContentReplyAccessor.queryByPage(queryExpress, page, conn));
            pageRows.setPage(page);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContentAction insertSocialContentAction(SocialContentAction action) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActionAccessor.insert(action, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialContentAction> querySocialContentAction(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NextPageRows<SocialContentAction> querySocialContentAction(long contentId, SocialContentActionType type, NextPagination page) throws DbException {
        Connection conn = null;
        NextPageRows<SocialContentAction> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialContentAction> actionList = socialContentActionAccessor.query(contentId, type, page, conn);

            pageRows = new NextPageRows<SocialContentAction>();
            pageRows.setRows(actionList);
            pageRows.setPage(page);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }


    public boolean updateSocialContentAction(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActionAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialHotContent> querySocialHotContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        PageRows<SocialHotContent> pageRows = new PageRows<SocialHotContent>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialHotContentAccessor.query(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public NextPageRows<SocialHotContent> querySocialHotContentByPage(NextPagination pagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialHotContent> pageRows = new NextPageRows<SocialHotContent>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialHotContent> list = socialHotContentAccessor.query(pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialHotContent> querySocialHotContentByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialHotContent> pageRows = new PageRows<SocialHotContent>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialHotContent> list = socialHotContentAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialHotContent insertSocialHotContent(SocialHotContent socialHotContent) throws DbException {
        Connection conn = null;
        SocialHotContent returnObj = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);
            int maxValue = socialHotContentAccessor.getMaxValue(conn);
            socialHotContent.setDisplayOrder(maxValue + 1);
            returnObj = socialHotContentAccessor.insert(socialHotContent, conn);
            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return returnObj;
    }


    public List<SocialContentAction> querySocialContentActionByQuery(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActionAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialHotContent getSocialHotContent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialHotContentAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialHotContent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialHotContentAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialReport> querySocialReport(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialReportAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialReport> querySocialReportByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<SocialReport> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialReport> list = socialReportAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<SocialReport>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialReport getSocialReport(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialReportAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialReport createSocialRepost(SocialReport socialReport) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialReportAccessor.insert(socialReport, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySocialReport(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialReportAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContentAction getSocialContentAction(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActionAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialActivity insertSocialActivity(SocialActivity socialActivity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            int maxDisplayOrder = socialActivityAccessor.getMaxDisplayOrder(conn) + 1;
            socialActivity.setDisplayOrder(maxDisplayOrder);

            socialActivity = socialActivityAccessor.insert(socialActivity, conn);

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
        } catch (DbException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return socialActivity;
    }

    public SocialActivity getSocialActivity(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialActivityAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialActivity> querySocialActivity(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialActivityAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialActivity> querySocialActivityByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialActivity> pageRows = new PageRows<SocialActivity>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialActivity> list = socialActivityAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySocialActivity(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialActivityAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialContentActivity> querySocialContentActivity(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialContentActivity> pageRows = new PageRows<SocialContentActivity>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialContentActivity> list = socialContentActivityAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialTag> querySocialTagByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialTag> pageRows = new PageRows<SocialTag>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialTag> list = socialTagAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContentActivity getSocialContentActivity(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActivityAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialContentActivity insertSocialContentActivity(SocialContentActivity socialContentActivity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            long minDisplayOrder = socialContentActivityAccessor.getMinDisplayOrder(conn);
            if (minDisplayOrder == 0l) {
                minDisplayOrder = (long) Integer.MAX_VALUE;
            } else {
                minDisplayOrder = minDisplayOrder - 1l;
            }
            socialContentActivity.setDisplayOrder(minDisplayOrder);
            socialContentActivity = socialContentActivityAccessor.insert(socialContentActivity, conn);

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
        } catch (DbException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return socialContentActivity;
    }

    public boolean updateSocialContentActivity(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActivityAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialWatermark> querySocialWatermarkByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialWatermark> pageRows = new PageRows<SocialWatermark>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialWatermark> list = socialWatermarkAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialWatermark insertSocialWatermark(SocialWatermark watermark) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            int maxDisplayOrder = socialWatermarkAccessor.getMaxDisplayOrder(conn) + 1;
            watermark.setDisplayOrder(maxDisplayOrder);
            watermark = socialWatermarkAccessor.insert(watermark, conn);

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
        } catch (DbException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return watermark;
    }

    public SocialWatermark getSocialWatermark(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialWatermarkAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySocialWatermark(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialWatermarkAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NextPageRows<SocialWatermark> querySocialWatermarkByNext(NextPagination nextPagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialWatermark> pageRows = new NextPageRows<SocialWatermark>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialWatermark> list = socialWatermarkAccessor.query(nextPagination, conn);
            pageRows.setPage(nextPagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialBackgroundAudio> querySocialBgAudio(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialBackgroundAudio> pageRows = new PageRows<SocialBackgroundAudio>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialBackgroundAudio> list = socialBackgroundAudioAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialBackgroundAudio insertSocialBgAudio(SocialBackgroundAudio bgAudio) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            int maxDisplayOrder = socialBackgroundAudioAccessor.getMaxDisplayOrder(conn) + 1;
            bgAudio.setDisplayOrder(maxDisplayOrder);
            bgAudio = socialBackgroundAudioAccessor.insert(bgAudio, conn);

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
        } catch (DbException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return bgAudio;
    }

    public SocialBackgroundAudio getSocialBgAudio(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBackgroundAudioAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySocialBgAudio(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialBackgroundAudioAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NextPageRows<SocialBackgroundAudio> querySocialBgAudioByNext(NextPagination nextPagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialBackgroundAudio> pageRows = new NextPageRows<SocialBackgroundAudio>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialBackgroundAudio> list = socialBackgroundAudioAccessor.query(nextPagination, conn);
            pageRows.setPage(nextPagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NextPageRows<SocialActivity> querySocialActivityByNext(NextPagination nextPagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialActivity> pageRows = new NextPageRows<SocialActivity>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialActivity> list = socialActivityAccessor.query(nextPagination, conn);
            pageRows.setPage(nextPagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public NextPageRows<SocialContentActivity> querySocialContentActivityByNext(long activityId, NextPagination nextPagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialContentActivity> pageRows = new NextPageRows<SocialContentActivity>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialContentActivity> list = socialContentActivityAccessor.query(activityId, nextPagination, conn);
            pageRows.setPage(nextPagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySocialContentActivity(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialContentActivityAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ForignContent> queryForignContent(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        PageRows<ForignContent> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ForignContent> list = forignContentAccessor.query(queryExpress, page, conn);
            pageRows = new PageRows<ForignContent>();
            pageRows.setRows(list);
            pageRows.setPage(page);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
