/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.comment;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.vote.VoteHandlerMongo;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.comment.quartz.TranscodeVideoQuartzCronTrigger;
import com.enjoyf.platform.serv.vote.VoteCache;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.event.system.MiyouCommentSumEvent;
import com.enjoyf.platform.service.gameclient.dto.GameClientPicDTO;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBSimpleDTO;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.GameClientProfileDTO;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.vote.WikiVote;
import com.enjoyf.platform.service.vote.WikiVoteField;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.util.HttpClientManager;
import com.enjoyf.util.HttpParameter;
import com.mongodb.BasicDBObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

/**
 * The JoymeAppLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * JoymeAppLogic is called by JoymeAppPacketDecoder.
 */
public class CommentLogic implements CommentService {
    //
    private static final Logger logger = LoggerFactory.getLogger(CommentLogic.class);
    private static final int COMMENT_REPLY_PAGE_SIZE = 5;
    private static final String KEY_SERVICE_LPUSH = "comment";
    private static final String KEY_LPUSH_COMMENT_BEAN = "_comment_bean";
    private static final String KEY_LPUSH_COMMENT_REPLY = "_comment_reply";
    private static final String KEY_CACHE_COMMENT_BEAN_LIST = "_commentbean_list";

    private static final int FOREGROUND = 2;
    private static final int BACKGROUP = 1;

    private CommentConfig config;

    private QueueThreadN eventQueueThreadN = null;
    private CommentHandler writeAbleHandler;
    private HandlerPool<CommentHandler> readonlyHandlersPool;

    private CommentCache commentCache;
    private CommentRedis commentRedis;

    private VoteHandlerMongo writeVoteHandlerMongo;
    private HandlerPool<VoteHandlerMongo> readonlyVoteHandlersPoolMongo;
    private VoteCache voteCache;


    public CommentLogic(CommentConfig cfg) {
        this.config = cfg;
        eventQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof MiyouCommentSumEvent) {
                    MiyouCommentSumEvent miyouCommentSumEvent = (MiyouCommentSumEvent) obj;
                    try {
                        int val = (int) (Math.random() * 50 + 50);
                        modifyCommentBeanById(miyouCommentSumEvent.getCommentId(), new UpdateExpress().increase(CommentBeanField.LONG_COMMENT_SUM, val));
                    } catch (ServiceException e) {
                        GAlerter.lab("miyou TimeTask CommentBean add longCommentSum " + this.getClass(), e);
                    }
                } else if (obj instanceof WebCacheWrap) {
                    WebCacheWrap webCacheWrap = (WebCacheWrap) obj;
                    webCacheWrap.processWebcache();
                } else {
                    GAlerter.lab("In eventQueueThreadN, there is a unknown obj.");
                }
            }


        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));
        //initialize the handler.
        try {
            writeAbleHandler = new CommentHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyHandlersPool = new HandlerPool<CommentHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new CommentHandler(dsn, config.getProps()));
            }

            writeVoteHandlerMongo = new VoteHandlerMongo(this.config.getMongoDbWriteAbleDateSourceName(), this.config.getProps());
            readonlyVoteHandlersPoolMongo = new HandlerPool<VoteHandlerMongo>();
            for (String dsn : this.config.getMongoDbReadonlyDataSourceNames()) {
                readonlyVoteHandlersPoolMongo.add(new VoteHandlerMongo(dsn, this.config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        commentCache = new CommentCache(config.getMemCachedConfig());
        voteCache = new VoteCache(config.getMemCachedConfig());
        commentRedis = new CommentRedis(config.getProps());
        try {
            TranscodeVideoQuartzCronTrigger cronTrigger = new TranscodeVideoQuartzCronTrigger(this, config);
            cronTrigger.init();
            cronTrigger.start();
        } catch (Exception e) {
            GAlerter.lab("EventQuartzCronTrigger start error.", e);
        }
    }

    @Override
    public CommentBean getCommentBeanById(String commentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentBeanById.commentId:" + commentId);
        }
        CommentBean commentBean = commentCache.getCommentBean(commentId);
        if (commentBean == null) {
            commentBean = readonlyHandlersPool.getHandler().getCommentBean(new QueryExpress()
                    .add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));
//                    .add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
            if (commentBean != null) {
                commentCache.putCommentBean(commentId, commentBean);
            }
        }
        return commentBean;
    }

    @Override
    public boolean modifyCommentBeanById(String commentId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic modifyCommentBeanById.commentId:" + commentId);
        }
        boolean bool = false;
        if (writeAbleHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)))) {
            commentCache.removeCommentBean(commentId);
            bool = true;
        }
        return bool;
    }

    @Override
    public CommentBean createCommentBean(CommentBean comment) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic createCommentBean:" + comment);
        }
        int unikeyLength = comment.getUniqueKey() == null ? 0 : comment.getUniqueKey().length();
        int uriLength = comment.getUri() == null ? 0 : comment.getUri().length();
        int titleLength = comment.getTitle() == null ? 0 : comment.getTitle().length();
        int picLength = comment.getPic() == null ? 0 : comment.getPic().length();
        int descLength = comment.getDescription() == null ? 0 : comment.getDescription().length();
        //不符合要求的数据不入库
        if (unikeyLength == 0 || unikeyLength > 64 || uriLength > 512 || titleLength > 128 || picLength > 256 || descLength > 2048) {
            return null;
        }

        comment = writeAbleHandler.createCommentBean(comment);
        if (comment != null && comment.getDomain().equals(CommentDomain.JOYME_LIVE)) {
            commentRedis.putJoymeLiveCache(comment.getDomain(), comment.getGroupId(), comment.getCommentId(), comment.getCreateTime().getTime() / 1000);
        }
        return comment;
    }

    @Override
    public Map<String, CommentBean> queryCommentBeanByIds(Set<String> ids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBeanByIds picId:" + ids);
        }

        //get db and put queryid
        Map<String, CommentBean> resultMap = new HashMap<String, CommentBean>();
        Set<String> queryIds = new HashSet<String>();
        for (String id : ids) {
            CommentBean commentBean = commentCache.getCommentBean(id);
            if (commentBean == null) {
                queryIds.add(id);
            } else {
                resultMap.put(id, commentBean);
            }
        }
        List<CommentBean> list = new ArrayList<CommentBean>();
        //query by queryid
        if (!CollectionUtil.isEmpty(queryIds)) {
            list = readonlyHandlersPool.getHandler().queryCommentBean(new QueryExpress().add(QueryCriterions.in(CommentBeanField.COMMENT_ID, queryIds.toArray())));
        }

        Map<String, CommentBean> dbMap = new HashMap<String, CommentBean>();
        if (!CollectionUtil.isEmpty(list)) {
            for (CommentBean commentBean : list) {
                dbMap.put(commentBean.getCommentId(), commentBean);
            }
        }

        for (String id : ids) {
            CommentBean commentBean = resultMap.get(id);

            if (commentBean == null) {
                commentBean = dbMap.get(id);
                if (commentBean != null) {
                    resultMap.put(id, commentBean);
                    commentCache.putCommentBean(id, commentBean);
                }
            }
        }

        return resultMap;
    }

    @Override
    public List<GameClientPicDTO> queryCommentBeanByCache(int page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBeanByCache:");
        }
        Map<Integer, Integer> sum = commentCache.getCommentBeanSum();
        if (CollectionUtil.isEmpty(sum)) {
            return null;
        }

        int param = sum.get(page);
        if (param == 0) {
            return null;
        }
        int random = (int) (Math.random() * param);
        List<GameClientPicDTO> list = commentCache.getCommentBeanList(KEY_CACHE_COMMENT_BEAN_LIST + "_" + page + "_" + (random + 1));
        return list;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refreshCommentBeanCache() throws ServiceException {

    }

    @Override
    public CommentReply createCommentReply(CommentReply reply, Integer totalRows) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic createCommentReply:" + reply);
        }
        CommentBean commentBean = getCommentBeanById(reply.getCommentId());
        //todo ugcwiki else???? 计数不准
        if (commentBean != null) {
            if (reply.getRootId() == 0) {
                commentBean.setTotalRows(commentBean.getTotalRows() + 1);
            }
            commentBean.setCommentSum(commentBean.getCommentSum() + 1);
            commentCache.putCommentBean(reply.getCommentId(), commentBean);
        }

        //获取楼层号,如果rootid为0说明是主楼,调用incrFolorNum获取主楼的楼层号;如果rootid不为0,是楼中楼调用incrRootFolorNum获取楼中楼的楼层号
        long folorNum = reply.getRootId() == 0 ?
                commentRedis.incrFolorNum(reply.getCommentId()) :
                commentRedis.incrRootFolorNum(reply.getCommentId(), reply.getRootId());
        reply.setFloorNum((int) folorNum);

        //write to db
        CommentReply commentReply = writeAbleHandler.createCommentReply(reply);
        if (commentReply != null) {
            //treply.getCreateTime().getTime() / 1000 是为了兼容老数据,因为老数据是用的reply.getCreateTime().getTime() / 1000.
            double replyIdScore = reply.getReplyId() + (reply.getCreateTime().getTime() / 1000);
            commentRedis.putCommentReplyIds(reply.getCommentId(), reply.getRootId(), reply.getReplyId(), replyIdScore);
            commentCache.putCommentInterval(reply.getReplyProfileId(), reply.getBody().getText()); //短时间内相同的内容的评论不能再发
            commentCache.putLastCommentReply(reply.getCommentId(), reply);
            commentCache.putCommentSeconds(reply.getReplyProfileId()); //用于检验15秒内不能重复发言

            //todo ????
            commentCache.removeReplyIdListByOrderField(reply.getCommentId(), reply.getRootId(), CommentReplyField.AGREE_SUM.getColumn(), QuerySortOrder.DESC.getCode(), 1);
            if (commentReply.getRootId() == 0) {
                //主楼
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentReply.getCommentId()));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.COMMENT_SUM, 1);
                updateExpress.increase(CommentBeanField.TOTAL_ROWS, 1);
                boolean bool = writeAbleHandler.modifyCommentBean(updateExpress, queryExpress);
                if (bool) {
                    commentCache.removeCommentBean(reply.getCommentId());
                }
            } else if (commentReply.getRootId() > 0l) {
                //楼中楼 修改commentbean的评论数
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.COMMENT_SUM, 1);
                boolean bool = writeAbleHandler.modifyCommentBean(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentReply.getCommentId())));
                if (bool) {
                    commentCache.removeCommentBean(reply.getCommentId());
                }

                //修改评论的自评论数
                UpdateExpress updateExpress2 = new UpdateExpress();
                updateExpress2.increase(CommentReplyField.SUB_REPLY_SUM, 1);
                updateExpress2.increase(CommentReplyField.TOTAL_ROWS, 1);
                updateExpress2.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
                boolean bool2 = writeAbleHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, commentReply.getRootId())), updateExpress2);
                if (bool2) {
                    commentCache.removeCommentReply(commentReply.getCommentId(), commentReply.getRootId());
                }
            }
            //webcache 评论数+1 todo 为什么用线程做 要事件是干嘛的!!!
            if (commentBean != null) {
                eventQueueThreadN.add(new WebCacheWrap(commentBean.getUniqueKey(), String.valueOf(commentBean.getDomain().getCode()), 1));
            }
        }
        return commentReply;
    }


    @Override
    public CommentReply getCommentReplyById(String commentId, Long replyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentReplyById:" + replyId);
        }
        CommentReply reply = commentCache.getCommentReply(commentId, replyId);
        if (reply == null) {
            reply = readonlyHandlersPool.getHandler().getCommentReply(new QueryExpress()
                    .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId))
                    .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                    .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
            if (reply != null) {
                commentCache.putCommentReply(commentId, replyId, reply);
            }
        }
        return reply;
    }

    @Override
    public boolean modifyCommentReplyById(String commentId, Long replyId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic modifyCommentReplyById:" + replyId);
        }
        boolean bool = writeAbleHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId)), updateExpress);
        if (bool) {
            commentCache.removeCommentReply(commentId, replyId);
        }
        return bool;
    }

    @Override
    public CommentHistory getCommentHistoryByProfileId(String commentId, CommentDomain domain, String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentHistoryByUno.commentId:" + commentId + ",domain:" + domain + ",profileId:" + profileId);
        }
        CommentHistory history = commentCache.getCommentHistoryByUno(commentId, domain.getCode(), profileId);
        if (history == null) {
            history = readonlyHandlersPool.getHandler().getCommentHistory(new QueryExpress()
                    .add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profileId))
                    .add(QueryCriterions.eq(CommentHistoryField.OBJECT_ID, commentId))
                    .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, domain.getCode())));
            if (history != null) {
                commentCache.putCommentHistoryByUno(commentId, domain.getCode(), profileId, history);
            }
        }
        return history;
    }

    @Override
    public CommentHistory createCommentHistory(CommentHistory commentHistory, CommentBean commentBean, CommentReply reply) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic createCommentHistory.commentHistory:" + commentHistory);
        }

        return writeAbleHandler.createCommentHistory(commentHistory);
    }

    @Override
    public boolean modifyCommentHistoryByProfileId(String commentId, CommentDomain domain, String profileId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic modifyCommentHistoryByUno.commentId:" + commentId + ",domain:" + domain + ",uno:" + profileId);
        }
        boolean bool = writeAbleHandler.modifyCommentHistory(new QueryExpress()
                .add(QueryCriterions.eq(CommentHistoryField.OBJECT_ID, commentId))
                .add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profileId))
                .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, domain.getCode())), updateExpress);
        if (bool) {
            commentCache.removeCommentHistoryByUno(commentId, domain.getCode(), profileId);
        }
        return bool;
    }

    @Override
    public void removeCommentReply(String commentId, Long replyId, Long rootId, UpdateExpress updateExpressParam) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic removeCommentReply.replyId:" + replyId);
        }
        UpdateExpress updateReply = updateExpressParam;
        if (updateReply == null) {
            updateReply = new UpdateExpress().set(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        }

        boolean bool = writeAbleHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId))
                .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())), updateReply);
        if (bool) {
            commentRedis.removeCommentReplyIds(commentId, rootId, replyId);
            commentCache.removeLastCommentReply(commentId);
            if (rootId != null && rootId == 0) {
                int cp = 0;
                Pagination page = null;
                int size = 1;
                //TODO 删除所有的楼中楼? 什么逻辑呀..评论大了以后有崩的风险
                do {
                    cp += 1;
                    page = new Pagination(100 * cp, cp, 100);
                    PageRows<CommentReply> pageRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                            .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, replyId))
                            .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())), page);
                    page = (pageRows == null ? page : pageRows.getPage());
                    if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                        page = pageRows.getPage();
                        for (CommentReply reply : pageRows.getRows()) {
                            boolean boo = writeAbleHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, reply.getReplyId())),
                                    new UpdateExpress().set(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
                            if (boo) {
                                commentCache.removeCommentReply(reply.getCommentId(), reply.getReplyId());
                                size += 1;
                            }
                        }
                    }

                } while (!page.isLastPage());
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId));
                queryExpress.add(QueryCriterions.geq(CommentBeanField.COMMENT_SUM, size));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.COMMENT_SUM, -size);
                boolean bool2 = writeAbleHandler.modifyCommentBean(updateExpress, queryExpress);
                if (bool2) {
                    commentCache.removeCommentBean(commentId);
                }

                UpdateExpress updateExpress2 = new UpdateExpress();
                updateExpress2.set(CommentReplyField.SUB_REPLY_SUM, 0);
                writeAbleHandler.modifyCommentReply(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId)), updateExpress2);
            } else if (rootId != null && rootId > 0) {
                QueryExpress queryExpress2 = new QueryExpress();
                queryExpress2.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId));
                queryExpress2.add(QueryCriterions.gt(CommentBeanField.COMMENT_SUM, 0));
                UpdateExpress updateExpress2 = new UpdateExpress();
                updateExpress2.increase(CommentBeanField.COMMENT_SUM, -1);
                boolean bool2 = writeAbleHandler.modifyCommentBean(updateExpress2, queryExpress2);
                if (bool2) {
                    commentCache.removeCommentBean(commentId);
                }

                UpdateExpress updateExpress3 = new UpdateExpress();
                updateExpress3.increase(CommentReplyField.SUB_REPLY_SUM, -1);
                writeAbleHandler.modifyCommentReply(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, rootId)).add(QueryCriterions.gt(CommentReplyField.SUB_REPLY_SUM, 0)), updateExpress3);
                commentCache.removeCommentReply(commentId, rootId);
            }
            commentCache.removeCommentReply(commentId, replyId);
            commentCache.removeHotReplyIdList(commentId);

            CommentBean commentBean = getCommentBeanById(commentId);
            eventQueueThreadN.add(new WebCacheWrap(commentBean.getUniqueKey(), String.valueOf(commentBean.getDomain().getCode()), -1));
        }
    }

    @Override
    public String getRightHtmlByArticleId(CommentBean commentBean) throws ServiceException {
        String returnObj = commentCache.getRightHtml(commentBean.getCommentId());
        if (returnObj == null) {
            //缓存为空 取righthtml 存缓存
            try {
                String url = commentBean.getUri();
                String s = URLUtils.getHtmlStringByURl(url);
                Document document = Jsoup.parse(s);
                if (document != null) {
                    Elements elements = document.getElementsByClass("subpage-right");
                    if (elements != null && elements.size() > 0) {
                        returnObj = elements.get(0).html();
                        Document doc = Jsoup.parse(returnObj);
                        Elements elements1 = doc.getElementsByTag("img");
                        for (Iterator iterator = elements1.iterator(); iterator.hasNext(); ) {
                            Element element = (Element) iterator.next();
                            String src = element.attr("src");
                            if (!src.startsWith("http://")) {
                                element.attr("src", "http://article.joyme.com" + src);
                            }
                        }
                        returnObj = doc.html();
                        commentCache.putRightHtml(commentBean.getCommentId(), returnObj);
                    }
                } else {
                    returnObj = "";
                }
            } catch (IOException e) {
                GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            }
        }
        return returnObj;
    }

    @Override
    public List<CommentBean> queryCommentBeanByAvgScore(String keyWords) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBeanByAvgScore");
        }
        List<CommentBean> list = commentCache.getCommentBeanByAvgScore(keyWords);
        if (CollectionUtil.isEmpty(list)) {
            list = readonlyHandlersPool.getHandler().queryByAvgScore(keyWords);
            if (!CollectionUtil.isEmpty(list)) {
                commentCache.putCommentBeanByAvgScore(keyWords, list);
            }
        }
        return list;
    }

    @Override
    public List<CommentBean> queryCommentBean(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBean.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentBean(queryExpress);
    }

    @Override
    public PageRows<CommentBean> queryCommentBeanByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBean.queryExpress:" + queryExpress + " Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryCommentBean(queryExpress, pagination);
    }


    @Override
    public CommentHistory getCommentHistoryByCache(String profileId, String objectId, CommentHistoryType historyType, CommentDomain commentDomain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentHistoryByCache.profileId:" + profileId + ",objectId:" + objectId + ",type:" + historyType.getCode() + ",domain:" + commentDomain.getCode());
        }
        CommentHistory history = commentCache.getCommentHistory(profileId, objectId, historyType.getCode(), commentDomain.getCode());
        if (history == null) {
            history = readonlyHandlersPool.getHandler().getCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, profileId))
                    .add(QueryCriterions.eq(CommentHistoryField.OBJECT_ID, objectId))
                    .add(QueryCriterions.eq(CommentHistoryField.ACTION_TYPE, historyType.getCode()))
                    .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, commentDomain.getCode())));
            if (history != null) {
                commentCache.putCommentHistory(profileId, objectId, historyType.getCode(), commentDomain.getCode(), history);
            }
        }
        return history;
    }

    @Override
    public List<CommentHistory> queryCommentHistory(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCommentHistory ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentHistory(queryExpress);
    }

    @Override
    public CommentBean getCommentBean(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentBean.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getCommentBean(queryExpress);
    }

    @Override
    public PageRows<CommentReply> queryCommentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentReplyByPage.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentReplyByPage(queryExpress, pagination);
    }

    @Override
    public List<CommentReply> queryCommentReply(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentReply.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentReply(queryExpress);
    }

    @Override
    public CommentReply getCommentReply(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentReply.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getCommentReply(queryExpress);
    }

    @Override
    public boolean modifyCommentReply(QueryExpress add, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic modifyCommentReply.queryExpress:" + add);
        }
        return writeAbleHandler.modifyCommentReply(add, updateExpress);
    }

    @Override
    public void recoverCommentReply(String commentId, Long replyId, Long rootId, Date createTime, UpdateExpress updateExpressParam) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic recoverCommentReply.replyId:" + replyId);
        }
        UpdateExpress updateReply = updateExpressParam;
        if (updateReply == null) {
            updateReply = new UpdateExpress().set(CommentReplyField.REMOVE_STATUS, ActStatus.ACTING.getCode());
        }

        boolean bool = writeAbleHandler.modifyCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())),
                updateReply);
        if (bool) {
            double replyIdScore = replyId + (createTime.getTime() / 1000);
            commentRedis.putCommentReplyIds(commentId, rootId, replyId, replyIdScore);
            if (rootId != null && rootId == 0) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId));
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.COMMENT_SUM, 1);
                boolean bool2 = writeAbleHandler.modifyCommentBean(updateExpress, queryExpress);
                if (bool2) {
                    commentCache.removeCommentBean(commentId);
                }
            } else if (rootId != null && rootId > 0) {
                UpdateExpress updateExpress2 = new UpdateExpress();
                updateExpress2.increase(CommentReplyField.SUB_REPLY_SUM, 1);
                updateExpress2.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
                writeAbleHandler.modifyCommentReply(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.REPLY_ID, rootId)), updateExpress2);
                commentCache.removeCommentReply(commentId, rootId);
            }
        }
    }

    @Override
    public List<CommentReply> queryHotReplyByCache(String commentId, Pagination pagination, boolean isCache) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryHotReplyByCache.commentId:" + commentId);
        }
        List<CommentReply> returnList = new ArrayList<CommentReply>();
        List<Long> idList = commentCache.getHotReplyIdList(commentId);
        if (idList == null || CollectionUtil.isEmpty(idList) || !isCache) {
            PageRows<CommentReply> hotRows = null;
            idList = new ArrayList<Long>();
            if (!isCache) {
                //综艺迷只需要按主楼点赞数倒叙排列
                hotRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                        .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, 0L))
                        .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.eq(CommentReplyField.DISPLAY_HOT, CommentReplyDisplayHotType.ALLOW.getCode()))
                        .add(QuerySort.add(CommentReplyField.AGREE_SUM, QuerySortOrder.DESC))
                        .add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC)), pagination);
            } else {
                hotRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                        .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, 0L))
                        .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.eq(CommentReplyField.DISPLAY_HOT, CommentReplyDisplayHotType.ALLOW.getCode()))
                        .add(QuerySort.add(CommentReplyField.REPLY_AGREE_SUM, QuerySortOrder.DESC)), pagination);
            }

            if (hotRows != null && !CollectionUtil.isEmpty(hotRows.getRows())) {
                for (CommentReply reply : hotRows.getRows()) {
                    idList.add(reply.getReplyId());
                    returnList.add(reply);
                }
                if (!CollectionUtil.isEmpty(idList)) {
                    commentCache.putHotReplyIdList(commentId, idList);
                }
            }
        } else {
            for (Long replyId : idList) {
                CommentReply hotReply = getCommentReplyById(commentId, replyId);
                //不为null，未删除，可以出现在热门
                if (hotReply != null && !ActStatus.ACTED.equals(hotReply.getRemoveStatus()) && CommentReplyDisplayHotType.ALLOW.getCode() == hotReply.getDisplayHot()) {
                    returnList.add(hotReply);
                }
            }
        }
        return returnList;
    }

    @Override
    public PageRows<CommentReply> queryCommentReplyByOrderField(String commentId, Long rootId, Pagination page, CommentReplyField orderField, QuerySortOrder order) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentReplyByCache.commentId:" + commentId + ",rootId:" + rootId);
        }
        PageRows<CommentReply> returnObj = null;
        List<CommentReply> returnList = new ArrayList<CommentReply>();
        //get by cache
        List<Long> replyIdList = commentCache.getReplyIdListByOrderField(commentId, rootId, orderField.getColumn(), order.getCode(), page.getCurPage());
        if (replyIdList == null || CollectionUtil.isEmpty(replyIdList)) {
            PageRows<CommentReply> dbRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                    .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                    .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, rootId))
                    .add(QuerySort.add(orderField, order)), page);
            if (dbRows != null && !CollectionUtil.isEmpty(dbRows.getRows())) {
                replyIdList = new ArrayList<Long>();
                for (CommentReply reply : dbRows.getRows()) {
                    returnList.add(reply);
                    replyIdList.add(reply.getReplyId());
                }
            }
            if (!CollectionUtil.isEmpty(replyIdList)) {
                commentCache.putReplyIdListByOrderField(commentId, rootId, orderField.getColumn(), order.getCode(), page.getCurPage(), replyIdList);
            }
        } else {
            for (Long id : replyIdList) {
                CommentReply reply = getCommentReplyById(commentId, id);
                if (reply != null) {
                    returnList.add(reply);
                }
            }
        }

        returnObj = new PageRows<CommentReply>();
        returnObj.setPage(page);
        returnObj.setRows(returnList);
        return returnObj;
    }

    @Override
    public PageRows<CommentReply> queryCommentReplyByCache(String commentId, Long rootId, Pagination page, Boolean desc) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentReplyByCache.commentId:" + commentId + ",rootId:" + rootId);
        }
        List<CommentReply> returnList = new ArrayList<CommentReply>();

        List<Long> idList = commentRedis.getCommentReplyIds(commentId, rootId, page, desc);
        if (CollectionUtil.isEmpty(idList)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.eq(CommentReplyField.ROOT_ID, rootId));
            queryExpress.add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            if (desc) {
                queryExpress.add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.ASC));
            }
            List<CommentReply> replyList = queryCommentReply(queryExpress);
            if (!CollectionUtil.isEmpty(replyList)) {
                int i = 0;
                for (CommentReply commentReply : replyList) {
                    if (page.getStartRowIdx() <= i && i <= page.getEndRowIdx()) {
                        returnList.add(commentReply);
                    }
                    i++;
                    double replyIdScore = commentReply.getReplyId() + (commentReply.getCreateTime().getTime() / 1000);
                    commentRedis.putCommentReplyIds(commentId, rootId, commentReply.getReplyId(), commentReply.getCreateTime().getTime() / 1000);
                }
                page.setTotalRows(replyList.size());
            }
        } else {
            Map<Long, CommentReply> commentReplyMap = queryCommentReplyByIdSet(commentId, new HashSet<Long>(idList));
            for (Long replyId : idList) {
                CommentReply contentReply = commentReplyMap.get(replyId);
                if (contentReply != null) {
                    returnList.add(contentReply);
                }
            }
        }

        PageRows<CommentReply> returnObj = new PageRows<CommentReply>();
        returnObj.setPage(page);
        returnObj.setRows(returnList);

        return returnObj;
    }

    @Override
    public PageRows<CommentReply> queryCommentReplyByReplyIdFromCache(String commentId, Long rootId, Long replyId, Integer size, Boolean desc) throws ServiceException {

        //get page from redis
        int index = commentRedis.getReplyIdIndex(commentId, rootId, replyId, desc);
        Pagination page = index < 0 ? new Pagination(size, 1, size) : new Pagination(index + 1, 1, size);

        Pagination queryPage = new Pagination(commentRedis.replyIdCount(commentId, rootId, replyId), page.getMaxPage(), size);
        //get data
        List<Long> idList = commentRedis.getCommentReplyIds(commentId, rootId, queryPage, desc);

        //load obj from memcached and mysql
        Map<Long, CommentReply> commentReplyMap = queryCommentReplyByIdSet(commentId, new HashSet<Long>(idList));

        List<CommentReply> replyLisd = new ArrayList<CommentReply>();
        for (Long reId : idList) {
            CommentReply contentReply = commentReplyMap.get(reId);
            if (contentReply != null) {
                replyLisd.add(contentReply);
            }
        }

        PageRows<CommentReply> returnObj = new PageRows<CommentReply>();
        returnObj.setPage(queryPage);
        returnObj.setRows(replyLisd);
        return returnObj;
    }

//CommentForbid表start

    @Override
    public CommentForbid createCommentForbid(CommentForbid commentForbid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createCommentForbid ,CommentForbid " + commentForbid);
        }
        return writeAbleHandler.createCommentForbid(commentForbid);

    }

    @Override
    public CommentForbid getCommentForbid(QueryExpress queryExpress) throws ServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("getCommentForbid ,queryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getCommentForbid(queryExpress);
    }

    @Override
    public List<CommentForbid> queryCommentForbid(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCommentForbid ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentForbid(queryExpress);

    }

    @Override
    public PageRows<CommentForbid> queryCommentForbidByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCommentForbidByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryCommentForbidByPage(queryExpress, pagination);

    }

    @Override
    public boolean modifyCommentForbid(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyCommentForbid ,QueryExpress " + queryExpress + "UpdateExpress  " + updateExpress);
        }
        return writeAbleHandler.modifyCommentForbid(updateExpress, queryExpress);

    }

    @Override
    public boolean deleteCommentForbid(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic deleteCommentForbid,profileId " + profileId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profileId));
        boolean bool = writeAbleHandler.deleteCommentForbid(queryExpress);
        if (bool) {
            commentCache.removeCommentForbid(profileId);
        }
        return bool;
    }

    @Override
    public CommentForbid getCommentForbidByCache(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentForbidByCache.profileId:" + profileId);
        }
        CommentForbid commentForbid = commentCache.getCommentForbid(profileId);
        if (commentForbid == null) {
            commentForbid = readonlyHandlersPool.getHandler().getCommentForbid(new QueryExpress().add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profileId)));
            if (commentForbid != null) {
                if (commentForbid.getStartTime() == null) {
                    return null;
                } else if (commentForbid.getLength() == 0) {
                    commentCache.putCommentForbid(profileId, commentForbid);
                    return commentForbid;
                } else {
                    long forbidTime = commentForbid.getStartTime().getTime() + (commentForbid.getLength() * 1000l);
                    long nowTime = System.currentTimeMillis();
                    if (forbidTime < nowTime) {
                        writeAbleHandler.deleteCommentForbid(new QueryExpress().add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profileId)));
                        return null;
                    } else {
                        commentCache.putCommentForbid(profileId, commentForbid);
                        return commentForbid;
                    }
                }
            }
        } else {
            if (commentForbid.getStartTime() == null) {
                return null;
            } else if (commentForbid.getLength() == 0) {
                return commentForbid;
            } else {
                long forbidTime = commentForbid.getStartTime().getTime() + (commentForbid.getLength() * 1000l);
                long nowTime = System.currentTimeMillis();
                if (forbidTime < nowTime) {
                    writeAbleHandler.deleteCommentForbid(new QueryExpress().add(QueryCriterions.eq(CommentForbidField.PROFILE_ID, profileId)));
                    commentCache.removeCommentForbid(profileId);
                    return null;
                } else {
                    return commentForbid;
                }
            }
        }
        return null;
    }

    @Override
    public String getCommentIntervalCache(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentInterval.profileId:" + profileId);
        }
        if (commentCache.getCommentSeconds(profileId)) {
            return profileId;
        }

        return commentCache.getCommentInterval(profileId);
    }


    private Map<Long, CommentReply> queryCommentReplyByIdSet(String commentId, HashSet<Long> idSet) throws ServiceException {
        if (CollectionUtil.isEmpty(idSet)) {
            return null;
        }
        Map<Long, CommentReply> replyMap = new HashMap<Long, CommentReply>();

        Set<Long> queryDbSet = new HashSet<Long>();

        //query by memached
        for (Long replyId : idSet) {
            CommentReply reply = commentCache.getCommentReply(commentId, replyId);

            if (reply != null && !reply.getRemoveStatus().equals(ActStatus.UNACT)) {
                replyMap.put(replyId, reply);
            } else {
                queryDbSet.add(replyId);
            }
        }

        if (CollectionUtil.isEmpty(queryDbSet)) {
            return replyMap;
        }

        //query by db
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
        queryExpress.add(QueryCriterions.in(CommentReplyField.REPLY_ID, queryDbSet.toArray()));
        queryExpress.add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
        List<CommentReply> replyList = readonlyHandlersPool.getHandler().queryCommentReply(queryExpress);

        for (CommentReply reply : replyList) {
            replyMap.put(reply.getReplyId(), reply);
            commentCache.putCommentReply(commentId, reply.getReplyId(), reply);
        }

        return replyMap;
    }

    private PageRows<Long> queryCommentReplyIds(String commentId, Long rootId, Pagination page, Boolean desc) throws ServiceException {
        PageRows<Long> pageRows = new PageRows<Long>();
        List<Long> replyList = new ArrayList<Long>();
        Set<Long> replySet = new HashSet<Long>();

        //get content by contentid get totalNums
        int fetchCacheTimes = page.getPageSize() / COMMENT_REPLY_PAGE_SIZE;

        for (int times = 0; times < fetchCacheTimes; times++) {
            //得到分页的参数 第N页就从缓存取 总页数-N页*times+1,根据外面传入的pageSize可以得到需要从缓存取多少次
            Pagination pageByCache = new Pagination(page.getTotalRows(), 1, COMMENT_REPLY_PAGE_SIZE);
            if (desc) {
                pageByCache.setCurPage(pageByCache.getMaxPage() - (page.getCurPage() - 1) * fetchCacheTimes - times);
            } else {
                pageByCache.setCurPage((page.getCurPage() - 1) * fetchCacheTimes + 1 + times);
            }

            //get by cache
            List<Long> replyIdList = commentCache.getCommentReplyIdList(commentId, rootId, pageByCache.getCurPage());

            //get by db
            if (CollectionUtil.isEmpty(replyIdList)) {
                PageRows<CommentReply> dbRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                        .add(QueryCriterions.eq(CommentReplyField.ROOT_ID, rootId))
                        .add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.ASC)), pageByCache);
                if (dbRows != null && !CollectionUtil.isEmpty(dbRows.getRows())) {
                    List<CommentReply> dbList = dbRows.getRows();
                    replyIdList = new ArrayList<Long>();
                    for (int i = dbList.size() - 1; i >= 0; i--) {
                        replyIdList.add(dbList.get(i).getReplyId());
                    }
                }
            }
            if (!CollectionUtil.isEmpty(replyIdList)) {
                commentCache.putCommentReplyIdList(commentId, rootId, pageByCache.getCurPage(), replyIdList);
                replySet.addAll(replyIdList);
            }

            if ((desc && pageByCache.getCurPage() <= 1) || (!desc && pageByCache.getCurPage() >= pageByCache.getMaxPage())) {
                break;
            }
        }
        replyList.addAll(replySet);

        if (!CollectionUtil.isEmpty(replyList)) {
            Collections.sort(replyList, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return o1 < o2 ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        pageRows.setPage(page);
        pageRows.setRows(replyList);
        return pageRows;
    }

    //CommentVoteOption表start
    @Override
    public int countCommentVoteOption(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("countCommentVoteOption,CommentVoteOption " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().countCommentVoteOption(queryExpress);
    }


    @Override
    public CommentVoteOption createCommentVoteOption(CommentVoteOption commentVoteOption) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createCommentVoteOption ,CommentVoteOption " + commentVoteOption);
        }
        return writeAbleHandler.createCommentVoteOption(commentVoteOption);

    }

    @Override
    public CommentVoteOption getCommentVoteOption(QueryExpress queryExpress) throws ServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("getCommentVoteOption ,queryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getCommentVoteOption(queryExpress);
    }

    @Override
    public List<CommentVoteOption> queryCommentVoteOption(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCommentVoteOption ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCommentVoteOption(queryExpress);

    }

    @Override
    public PageRows<CommentVoteOption> queryCommentVoteOptionByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryCommentVoteOptionByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryCommentVoteOptionByPage(queryExpress, pagination);

    }

    @Override
    public boolean modifyCommentVoteOption(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyCommentVoteOption ,QueryExpress " + queryExpress + "UpdateExpress  " + updateExpress);
        }
        return writeAbleHandler.modifyCommentVoteOption(updateExpress, queryExpress);

    }

    @Override
    public boolean deleteCommentVoteOption(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic deleteCommentVoteOption,QueryExpress " + queryExpress);
        }

        return writeAbleHandler.deleteCommentVoteOption(queryExpress);
    }

    @Override
    public WikiVote getWikiVote(String url, String pic, String name, String num, Long articleId, String keyWords, Integer voteSum) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWikiVote.url:" + url);
        }
        WikiVote wikiVote = voteCache.getWikiVote(articleId);
        if (wikiVote == null) {
            wikiVote = readonlyVoteHandlersPoolMongo.getHandler().getWikiVote(new MongoQueryExpress().add(MongoQueryCriterions.eq(WikiVoteField.ARTICLE_ID, articleId)));
            if (wikiVote == null) {
                wikiVote = new WikiVote();
                wikiVote.setArticleId(articleId);
                wikiVote.setUrl(url);
                wikiVote.setPic(pic);
                wikiVote.setName(name);
                wikiVote.setNoStr(num);
                wikiVote.setKeyWords(keyWords);
                wikiVote.setVotesSum(voteSum);
                wikiVote.setCreateDate(new Date());

                wikiVote = writeVoteHandlerMongo.createWikiVote(wikiVote);
            }

            if (wikiVote != null) {
                voteCache.putWikiVote(articleId, wikiVote);
            }
        }
        return wikiVote;
    }

    @Override
    public boolean incWikiVote(String url, Long articleId, BasicDBObject query, BasicDBObject update) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " incWikiVote.url:" + url);
        }
        boolean bool = writeVoteHandlerMongo.updateWikiVote(query, update);
        if (bool) {
            voteCache.removeWikiVote(articleId);
        }
        return bool;
    }

    @Override
    public List<WikiVote> queryWikiVotes(Set<Long> idSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWikiVotes.idSet:" + idSet.toArray().toString());
        }
        List<WikiVote> voteList = new ArrayList<WikiVote>();
        for (Long id : idSet) {
            if (id > 0l) {
                WikiVote wikiVote = voteCache.getWikiVote(id);
                if (wikiVote == null) {
                    wikiVote = readonlyVoteHandlersPoolMongo.getHandler().getWikiVote(new MongoQueryExpress().add(MongoQueryCriterions.eq(WikiVoteField.ARTICLE_ID, id)));
                }
                if (wikiVote != null) {
                    voteCache.putWikiVote(id, wikiVote);
                    voteList.add(wikiVote);
                }
            }
        }
        return voteList;
    }

    @Override
    public List<WikiVote> queryWikiVoteByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWikiVoteByPage.mongoQueryExpress:" + mongoQueryExpress);
        }
        return readonlyVoteHandlersPoolMongo.getHandler().queryWikiVoteByPage(mongoQueryExpress, pagination);
    }

    @Override
    public CommentReply getLastCommentReply(String commentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getLastReply.commentId:" + commentId);
        }
        CommentReply reply = commentCache.getLastCommentReply(commentId);
        if (reply == null) {
            reply = readonlyHandlersPool.getHandler().getCommentReply(new QueryExpress()
                    .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                    .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC)));
            if (reply != null) {
                commentCache.putLastCommentReply(commentId, reply);
            }
        }
        return reply;
    }

    @Override
    public Map<String, CommentReply> queryHotReplyCacheByAgreeSum(Set<String> commentIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryHotReplyCacheByAgreeSum.commentIdSet:" + commentIdSet.toString());
        }
        if (CollectionUtil.isEmpty(commentIdSet)) {
            return null;
        }
        Map<String, CommentReply> returnMap = new HashMap<String, CommentReply>();
        for (String commentId : commentIdSet) {
            CommentReply commentReply = commentCache.getCommentReplyByAgree(commentId);
            if (commentReply == null) {
                PageRows<CommentReply> commentReplyPageRows = readonlyHandlersPool.getHandler().queryCommentReplyByPage(new QueryExpress()
                        .add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId))
                        .add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QuerySort.add(CommentReplyField.AGREE_SUM, QuerySortOrder.DESC))
                        .add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC)), new Pagination(1, 1, 1));
                if (commentReplyPageRows != null && !CollectionUtil.isEmpty(commentReplyPageRows.getRows())) {
                    commentCache.putCommentReplyByAgree(commentId, commentReplyPageRows.getRows().get(0));
                    returnMap.put(commentId, commentReplyPageRows.getRows().get(0));
                }
            } else {
                returnMap.put(commentId, commentReply);
            }
        }
        return returnMap;
    }

    @Override
    public CommentBean getCommentBeanByUniKey(String uniKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentBeanByUniKey:" + uniKey);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(CommentBeanField.UNI_KEY, uniKey));
        return readonlyHandlersPool.getHandler().getCommentBean(queryExpress);
    }

    @Override
    public PageRows<CommentBean> queryCommentBeanByGroup(CommentDomain domain, Long groupId, Pagination page, String sort) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBeanByGroup:domain:" + domain.getCode() + ",groupId:" + groupId + ",curPage:" + page.getCurPage());
        }
        PageRows<CommentBean> pageRows = null;
        CommentBean commentBeanTop = commentCache.getCommentBeanTop(domain, groupId);
//        if(page.getCurPage()==1 && null!=commentBeanTop){//如果是第一页且有置顶内容只取9条记录
//        	page.setPageSize(9);
//        }
        PageRows<String> idRows = commentRedis.getJoymeLiveCache(domain, groupId, page, sort);
        if (idRows != null && !CollectionUtil.isEmpty(idRows.getRows())) {
            pageRows = new PageRows<CommentBean>();
            List<CommentBean> list = new ArrayList<CommentBean>();
            for (String commentId : idRows.getRows()) {
                CommentBean commentBean = getCommentBeanById(commentId);
                if (commentBean != null) {
                    list.add(commentBean);
                }
            }
            pageRows.setRows(list);
            pageRows.setPage(idRows.getPage());
        }
        return pageRows;
    }

    @Override
    public void setCommentBeanTop(String commentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic setCommentBeanTop,commentId:" + commentId);
        }
        CommentBean commentBean = getCommentBeanById(commentId);
        commentCache.setCommentBeanTop(commentBean);
    }

    @Override
    public CommentBean getCommentBeanTop(CommentDomain domain, Long groupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentBeanTop,domain:" + domain.getCode() + ",groupId:" + groupId);
        }
        return commentCache.getCommentBeanTop(domain, groupId);
    }

    @Override
    public void deleteCommentBeanTop(CommentDomain domain, Long groupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic deleteCommentBeanTop,domain:" + domain.getCode() + ",groupId:" + groupId);
        }
        commentCache.deleteCommentBeanTop(domain, groupId);
    }

    @Override
    public List<CommentBean> queryCommentBeanByScore(CommentDomain domain, Long groupId, Long timeFlag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentBeanByScore:domain:" + domain.getCode() + ",groupId:" + groupId + ",timeFlag:" + timeFlag);
        }
        List<CommentBean> list = null;
        Set<String> idSet = commentRedis.getJoymeLiveCacheByScore(domain, groupId, timeFlag);
        if (!CollectionUtil.isEmpty(idSet)) {
            list = new ArrayList<CommentBean>();
            for (String commentId : idSet) {
                CommentBean commentBean = getCommentBeanById(commentId);
                if (commentBean != null) {
                    list.add(commentBean);
                }
            }
        }
        return list;
    }

    @Override
    public boolean deleteCommentBean(String commentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic deleteCommentBean:commentId:" + commentId);
        }
        CommentBean commentBean = getCommentBeanById(commentId);
        if (commentBean == null) {
            return false;
        }
        boolean bool = writeAbleHandler.deleteCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));
        if (bool) {
            GAlerter.lab("========================live:" + commentId);
            commentRedis.removeJoymeLiveCache(commentBean.getDomain(), commentBean.getGroupId(), commentId);
            commentCache.removeCommentBean(commentId);
        }
        return bool;
    }

    @Override
    public PageRows<CommentVideo> queryCommentVideoPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentVideoPage:queryExpress:" + queryExpress + " pagination:" + pagination);
        }

        return readonlyHandlersPool.getHandler().queryCommentVideo(queryExpress, pagination);
    }

    @Override
    public List<CommentVideo> queryCommentVideoByList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryCommentVideoPage:queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryCommentVideo(queryExpress);
    }

    @Override
    public CommentVideo createCommentVideo(CommentVideo commentVideo) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic createCommentVideo:commentVideo:" + commentVideo);
        }

        return writeAbleHandler.createCommentVideo(commentVideo);
    }

    @Override
    public CommentVideo getCommentVideo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentVideo:queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getCommentVideo(queryExpress);
    }

    @Override
    public boolean modifyCommentVideo(UpdateExpress updateExpress, String commentVideoId, String type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic modifyCommentVideo:updateExpress:" + updateExpress + " commentVideoId=" + commentVideoId);
        }

        CommentVideo commentVideo = writeAbleHandler.getCommentVideo(new QueryExpress().add(QueryCriterions.eq(CommentVideoField.COMMENT_VIDEO_ID, Long.parseLong(commentVideoId))));

        boolean b = writeAbleHandler.modifyCommentVideo(updateExpress, new QueryExpress().add(QueryCriterions.eq(CommentVideoField.COMMENT_VIDEO_ID, Long.parseLong(commentVideoId))));
        if (b) {
            if (!StringUtil.isEmpty(type)) {
                commentRedis.deleteCommenVideoHotList(commentVideo);
            }
        }
        return b;
    }

    @Override
    public void putHotVideoList(List<Long> commentVideoList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic putHotVideoList:commentVideoList:" + commentVideoList);
        }
        List<CommentVideo> commentVideos = readonlyHandlersPool.getHandler().queryCommentVideo(new QueryExpress()
                .add(QueryCriterions.in(CommentVideoField.COMMENT_VIDEO_ID, commentVideoList.toArray())));
        for (CommentVideo commentVideo : commentVideos) {
            commentRedis.putCommentVideoHotList(commentVideo);
        }
    }

    /**
     * @param gamesdk
     * @param type    1为后台工具调用redis 2为前台展示页面调用的redis
     * @throws ServiceException
     */
    @Override
    public List<CommentVideo> queryHotVideoListByRedis(String gamesdk, int type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic queryHotVideoListByRedis:gamesdk:" + gamesdk + " type=" + type);
        }
        List<CommentVideo> commentVideos = null;
        if (BACKGROUP == type) {
            commentVideos = commentRedis.queryCommentVideoHotList(gamesdk);
        } else if (FOREGROUND == type) {
            commentVideos = commentRedis.queryCommetVideoHots(gamesdk);
        }

        return commentVideos;
    }

    @Override
    public void sortHotVideoListByRedis(String gameSdk, String oldIndex, String newIndex) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic sortHotVideoListByRedis:oldIndex:" + oldIndex + " newIndex=" + newIndex);
        }
        commentRedis.CommentVideoSort(gameSdk, oldIndex, newIndex);
    }

    @Override
    public int countCommentReply(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic countCommentReply");
        }
        return readonlyHandlersPool.getHandler().countCommentReply(queryExpress);
    }

    @Override
    public String updateHotList(String gameSdk) throws ServiceException {
        List<CommentVideo> redisCommentVideos = commentRedis.queryCommentVideoHotList(gameSdk);
        commentRedis.deleteCommentVideoHots(gameSdk);
        if (!CollectionUtil.isEmpty(redisCommentVideos)) {
            //todo codereview 出于性能考虑可以用批量更新的方式 将redisCommentVideos 转成一个json数组然后批量更新减少打开redis的次数
            for (CommentVideo commentVideo : redisCommentVideos) {
                commentRedis.putCommentVideoHots(commentVideo);
            }
        }
        return "success";
    }


    private String parseUserCenterHeadIcon(String headIcon, String sex, String headIconSize, boolean validStatus) {

        String reValue = headIcon;
        if (StringUtil.isEmpty(headIcon)) {
            String boyURL = "http://lib.joyme.com/static/theme/default/img/head_boy_";
            String girlURL = "http://lib.joyme.com/static/theme/default/img/head_girl_";
            String defaultURL = "http://lib.joyme.com/static/theme/default/img/head_is_";
            if (StringUtil.isEmpty(sex)) {
                return defaultURL + headIconSize + ".jpg";
            } else if ("0".equals(sex)) {
                return girlURL + headIconSize + ".jpg";
            } else {
                return boyURL + headIconSize + ".jpg";
            }
        }
        return reValue;
    }

    private ProfilePicDTO buildProfilePicDTO(CommentBean commentBean) {
        ProfilePicDTO picDTO = new ProfilePicDTO();

        picDTO.setPicid(commentBean.getCommentId());
        picDTO.setPicurl(URLUtils.getJoymeDnUrl(commentBean.getPic()));

        return picDTO;
    }

    @Override
    public Map<String, PageRows<CommentReply>> queryCommentReplyFromCacheByCommentIds(List<String> commentIds, long rootId,
                                                                                      Pagination pagination, Boolean desc) throws ServiceException {
        Map<String, PageRows<CommentReply>> retMap = new HashMap<String, PageRows<CommentReply>>();
        if (!CollectionUtil.isEmpty(commentIds)) {
            for (String commentId : commentIds) {
                CommentBean commentBean = getCommentBeanById(commentId);
                if (null != commentBean) {
                    Pagination page = new Pagination();
                    page.setCurPage(pagination.getCurPage());
                    page.setPageSize(pagination.getPageSize());
                    page.setTotalRows(commentBean.getCommentSum());
                    PageRows<CommentReply> commentReplyPageRows = queryCommentReplyByCache(commentId, rootId, page, true);
                    retMap.put(commentId, commentReplyPageRows);
                }
            }
        }
        return retMap;
    }

    @Override
    public CommentPermission getPermissionByProfileId(String permissionId) throws ServiceException {

        CommentPermission permission = commentCache.getCommentPermission(permissionId);
        if (null == permission) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentPermissionField.PERMISSIONID, permissionId));
            permission = writeAbleHandler.getPermission(queryExpress);
            if (null != permission) {
                commentCache.putCommentPermission(permission);
            }
        }
        return permission;
    }

    @Override
    public List<CommentPermission> queryCommentPermissionList() throws ServiceException {
        List<CommentPermission> commentPermissionList = writeAbleHandler.queryPermissionList(new QueryExpress());
        return commentPermissionList;
    }

    @Override
    public CommentPermission createCommentPermission(String permissionId, String profileId, CommentPermissionType permissionType,
                                                     int createUserId) throws ServiceException {
        CommentPermission permission = commentCache.getCommentPermission(permissionId);
        if (null == permission) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentPermissionField.PERMISSIONID, permissionId));
            permission = writeAbleHandler.getPermission(queryExpress);
            if (null == permission) {
                permission = new CommentPermission();
                permission.setPermissionId(permissionId);
                permission.setProfileId(profileId);
                permission.setStatus(ValidStatus.VALID);
                permission.setPermissionType(permissionType);
                permission.setCreateUserId(createUserId);

                CommentPermission newPermission = writeAbleHandler.insertPermission(permission);
                commentCache.putCommentPermission(newPermission);
                return newPermission;
            } else {
                if (ValidStatus.REMOVED.equals(permission.getStatus())) {
                    permission.setStatus(ValidStatus.VALID);
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(CommentPermissionField.STATUS, ValidStatus.VALID.getCode());

                    QueryExpress updateQueryExpress = new QueryExpress();
                    updateQueryExpress.add(QueryCriterions.eq(CommentPermissionField.PERMISSIONID, permissionId));

                    int ret = writeAbleHandler.updatePermission(updateExpress, updateQueryExpress);
                    if (ret > 0) {
                        commentCache.putCommentPermission(permission);
                    }
                }
                return permission;
            }
        } else {
            return permission;
        }
    }

    @Override
    public boolean deleteCommentPermission(String permissionId) throws ServiceException {
        CommentPermission permission = commentCache.getCommentPermission(permissionId);
        if (null != permission) {
            commentCache.removeCommentPermission(permissionId);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(CommentPermissionField.STATUS, ValidStatus.REMOVED.getCode());
        QueryExpress updateQueryExpress = new QueryExpress();
        updateQueryExpress.add(QueryCriterions.eq(CommentPermissionField.PERMISSIONID, permissionId));

        int ret = writeAbleHandler.updatePermission(updateExpress, updateQueryExpress);
        if (ret > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void createTranscodeVideo(String commentId, String persistentId) throws ServiceException {
        commentRedis.putTranscodeVideo(commentId, persistentId);
    }

    public Map<String, String> getTranscodeVideoMap() {
        return commentRedis.getTranscodeVideoMap();
    }

    public long removeTranscodeVide(String commentId) {
        return commentRedis.removeTranscodeVideo(commentId);
    }

    @Override
    public void addCommentOperReason(String id, String reason,int type) {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic addCommentDeleteReason:id:" + id + " reason=" + reason);
        }
        commentRedis.addCommentDeleteReason(id, reason,type);
    }


    @Override
    public String getCommentOperReason(String id,int type) {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic getCommentDeleteReason:id:" + id);
        }
        return commentRedis.getCommentDeleteRason(id,type);
    }
}

