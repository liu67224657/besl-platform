package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
public interface VoteService extends EventReceiver {

    //发布投票
    @RPC(partitionHashing = 0)
    public Vote postVote(String subjectId, Vote vote) throws ServiceException;

    //获得投票
    @RPC(partitionHashing = 0)
    public Vote getVote(String subjectId) throws ServiceException;

    //参与投票
    @RPC(partitionHashing = 0)
    public boolean partVote(String subjectId, String voteUno, String voteIp, VoteDomain voteDomain, VoteRecordSet recordSet) throws ServiceException;

    //得到用户投票结果
    @RPC(partitionHashing = 0)
    public VoteUserRecord getVoteUserRecord(String voteUno, String subjectId) throws ServiceException;

    //我关注的人投票结果
    @RPC(partitionHashing = 0)
    public Map<String, VoteUserRecord> queryVoteUserRecordBySubjectId(String subjectId, Set<String> voteUnoSet) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean receiveEvent(Event event) throws ServiceException;

    @RPC
    public WikiVote getWikiVote(String url, String pic, String name, String num, Long articleId, String keyWords, Integer voteSum) throws ServiceException;

    @RPC
    public boolean incWikiVote(String url, Long articleId, BasicDBObject query, BasicDBObject update) throws ServiceException;

    @RPC
    public List<WikiVote> queryWikiVotes(Set<Long> idSet) throws ServiceException;

    @RPC
    public List<WikiVote> queryWikiVoteByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws ServiceException;

    /**
     * 查询投票数量
     *
     * @param keySets keySe
     * @return
     * @throws ServiceException
     */
    @RPC
    public Map<String, Integer> queryVoteNum(Set<String> keySets) throws ServiceException;

    @RPC
    public boolean getVoteHistory(String profileId, String key) throws ServiceException;

    @RPC
    public VoteUserRecord insertVoteUserRecord(VoteUserRecord voteUserRecord) throws ServiceException;
}
