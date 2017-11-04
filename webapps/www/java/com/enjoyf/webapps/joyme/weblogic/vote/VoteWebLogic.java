package com.enjoyf.webapps.joyme.weblogic.vote;

import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.service.misc.InterFlowAccountField;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.service.timeline.TimeLineItem;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.vote.*;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.Comment;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.webapps.joyme.dto.VoteDto;
import com.enjoyf.webapps.joyme.dto.VoteUserRecordDTO;
import com.enjoyf.webapps.joyme.dto.vote.WikiVoteDTO;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-25
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "voteWebLogic")
public class VoteWebLogic {
    @Resource(name = "voteWebLogic")
    private VoteWebLogic voteWebLogic;

    public Vote postVote(Vote vote, boolean sync) throws ServiceException {
        vote = VoteServiceSngl.get().postVote(vote.getVoteSubject().getSubjectId(), vote);
        return vote;
    }

    public VoteDto partVote(String subjectId, String voteUno, String voteIp, String directId, VoteDomain voteDomain, VoteRecordSet recordSet, Integer size) throws ServiceException {
        VoteDto voteDto = null;

        boolean b = VoteServiceSngl.get().partVote(subjectId, voteUno, voteIp, voteDomain, recordSet);

        if (b) {
            //
            voteDto = getVoteDto(subjectId, voteUno, directId, size);
        }

        return voteDto;
    }

    /**
     * @param subjectId
     * @param uno
     * @param relationId
     * @param size       -1 all or size
     * @return
     * @throws ServiceException
     */
    public VoteDto getVoteDto(String subjectId, String uno, String relationId, int size) throws ServiceException {
        VoteDto voteDto = null;
        List<VoteUserRecordDTO> userRecordDTOList = new ArrayList<VoteUserRecordDTO>();

        Vote vote = voteWebLogic.getVoteBySubjectId(subjectId);
        if (vote != null) {
            voteDto = new VoteDto();
            voteDto.setVote(vote);
        }

        if (!StringUtil.isEmpty(uno)) {
            //是否已投票
            VoteUserRecord userRecord = VoteServiceSngl.get().getVoteUserRecord(uno, subjectId);
            if (userRecord != null) {
                voteDto.setVoteUserRecord(userRecord);

                for (VoteRecord voteRecord : userRecord.getRecordSet().getRecords()) {
                    VoteOption voteOption = vote.getVoteOptionMap().get(voteRecord.getOptionId());
                    if (voteOption != null) {
                        voteOption.setChecked(true);
                    }
                }
            }

            //好友参与结果
            if (size != -1) {
                Pagination pagination = new Pagination(0, 1, size);
                PageRows<TimeLineItem> timeLineItemPageRows = TimeLineServiceSngl.get().queryTimeLinesByFilterTypeRelationId(uno, relationId, TimeLineDomain.HOME, TimeLineFilterType.getByValue(TimeLineFilterType.VOTE_FORWARD), pagination);
                if (timeLineItemPageRows.getRows().size() > 0) {
                    Set<String> voteUnoSet = new HashSet<String>();
                    for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                        if (!uno.equals(timeLineItem.getDirectUno())) {
                            voteUnoSet.add(timeLineItem.getDirectUno());
                        }
                    }

                    if (voteUnoSet.size() > 0) {
                        Map<String, VoteUserRecord> map = VoteServiceSngl.get().queryVoteUserRecordBySubjectId(subjectId, voteUnoSet);

                        Map<String, VoteUserRecordDTO> userRecordDTOMap = new HashMap<String, VoteUserRecordDTO>();
                        for (VoteUserRecord voteUserRecord : map.values()) {
                            VoteUserRecordDTO userRecordDTO = new VoteUserRecordDTO();

                            voteUserRecord.setDateStr(DateUtil.parseDate(voteUserRecord.getVoteDate()));

                            for (VoteRecord voteRecord : voteUserRecord.getRecordSet().getRecords()) {
                                voteRecord.setOptionValue(vote.getVoteOptionMap().get(voteRecord.getOptionId()).getDescription());
                            }

                            userRecordDTO.setVoteUserRecord(voteUserRecord);

                            ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(voteUserRecord.getVoteUno());
                            userRecordDTO.setBlog(blog);

                            if (blog != null) {
                                userRecordDTOMap.put(userRecordDTO.getBlog().getUno(), userRecordDTO);
                            }
                        }

                        for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                            if (!uno.equals(timeLineItem.getDirectUno()) && userRecordDTOMap.get(timeLineItem.getDirectUno()) != null) {
                                userRecordDTOList.add(userRecordDTOMap.get(timeLineItem.getDirectUno()));
                            }
                        }
                    }

                    voteDto.setUserRecordDTOList(userRecordDTOList);
                }
            } else {
                // all
                Pagination pagination = new Pagination(0, 1, 100);
                PageRows<TimeLineItem> timeLineItemPageRows = TimeLineServiceSngl.get().queryTimeLinesByFilterTypeRelationId(uno, relationId, TimeLineDomain.HOME, TimeLineFilterType.getByValue(TimeLineFilterType.VOTE_FORWARD), pagination);
                if (timeLineItemPageRows.getRows().size() > 0) {
                    Set<String> voteUnoSet = new HashSet<String>();
                    for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                        if (!uno.equals(timeLineItem.getDirectUno())) {
                            voteUnoSet.add(timeLineItem.getDirectUno());
                        }
                    }

                    if (voteUnoSet.size() > 0) {
                        Map<String, VoteUserRecord> map = VoteServiceSngl.get().queryVoteUserRecordBySubjectId(subjectId, voteUnoSet);

                        Map<String, VoteUserRecordDTO> userRecordDTOMap = new HashMap<String, VoteUserRecordDTO>();
                        for (VoteUserRecord voteUserRecord : map.values()) {
                            VoteUserRecordDTO userRecordDTO = new VoteUserRecordDTO();

                            voteUserRecord.setDateStr(DateUtil.parseDate(voteUserRecord.getVoteDate()));

                            for (VoteRecord voteRecord : voteUserRecord.getRecordSet().getRecords()) {
                                voteRecord.setOptionValue(vote.getVoteOptionMap().get(voteRecord.getOptionId()).getDescription());
                            }

                            userRecordDTO.setVoteUserRecord(voteUserRecord);

                            ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(voteUserRecord.getVoteUno());
                            userRecordDTO.setBlog(blog);

                            if (blog != null) {
                                userRecordDTOMap.put(userRecordDTO.getBlog().getUno(), userRecordDTO);
                            }
                        }

                        for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                            if (!uno.equals(timeLineItem.getDirectUno()) && userRecordDTOMap.get(timeLineItem.getDirectUno()) != null) {
                                userRecordDTOList.add(userRecordDTOMap.get(timeLineItem.getDirectUno()));
                            }
                        }
                    }
                }

                while (timeLineItemPageRows.getPage().hasNextPage()) {
                    timeLineItemPageRows.getPage().setCurPage(timeLineItemPageRows.getPage().getCurPage() + 1);
                    timeLineItemPageRows = TimeLineServiceSngl.get().queryTimeLinesByFilterTypeRelationId(uno, relationId, TimeLineDomain.HOME, TimeLineFilterType.getByValue(TimeLineFilterType.VOTE_FORWARD), timeLineItemPageRows.getPage());

                    if (timeLineItemPageRows.getRows().size() > 0) {
                        Set<String> voteUnoSet = new HashSet<String>();
                        for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                            if (!uno.equals(timeLineItem.getDirectUno())) {
                                voteUnoSet.add(timeLineItem.getDirectUno());
                            }
                        }
                        if (voteUnoSet.size() > 0) {
                            Map<String, VoteUserRecord> map = VoteServiceSngl.get().queryVoteUserRecordBySubjectId(subjectId, voteUnoSet);

                            Map<String, VoteUserRecordDTO> userRecordDTOMap = new HashMap<String, VoteUserRecordDTO>();
                            for (VoteUserRecord voteUserRecord : map.values()) {
                                VoteUserRecordDTO userRecordDTO = new VoteUserRecordDTO();

                                voteUserRecord.setDateStr(DateUtil.parseDate(voteUserRecord.getVoteDate()));

                                for (VoteRecord voteRecord : voteUserRecord.getRecordSet().getRecords()) {
                                    voteRecord.setOptionValue(vote.getVoteOptionMap().get(voteRecord.getOptionId()).getDescription());
                                }

                                userRecordDTO.setVoteUserRecord(voteUserRecord);

                                ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(voteUserRecord.getVoteUno());
                                userRecordDTO.setBlog(blog);

                                if (blog != null) {
                                    userRecordDTOMap.put(userRecordDTO.getBlog().getUno(), userRecordDTO);
                                }
                            }

                            for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {
                                if (!uno.equals(timeLineItem.getDirectUno()) && userRecordDTOMap.get(timeLineItem.getDirectUno()) != null) {
                                    userRecordDTOList.add(userRecordDTOMap.get(timeLineItem.getDirectUno()));
                                }
                            }
                        }
                    }
                }

            }


        }

        voteDto.setUserRecordDTOList(userRecordDTOList);
        return voteDto;
    }

    public Vote getVoteBySubjectId(String subjectId) {
        Vote vote = null;
        try {
            vote = VoteServiceSngl.get().getVote(subjectId);
            if (vote != null && vote.getVoteSubject().getExpiredDate().getTime() < System.currentTimeMillis()) {
                vote.setExpired(true);
            }

            if (!StringUtil.isEmpty(vote.getVoteSubject().getDirection())) {
                vote.getVoteSubject().setDirection(processReplyByKey(WordProcessorKey.KEY_PRIVIEW_REPLAY, vote.getVoteSubject().getDirection()));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getVoteBySubjectId service exception " + e);
        }

        return vote;
    }

    private String processReplyByKey(WordProcessorKey key, String replyBody) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(replyBody);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

    public WikiVoteDTO getWikiVote(String url, String pic, String name, String num, Long articleId, String keyWords, int voteSum) throws ServiceException {
        WikiVote wikiVote = CommentServiceSngl.get().getWikiVote(url, pic, name, num, articleId, keyWords, voteSum);
        if (wikiVote == null) {
            return null;
        }
        WikiVoteDTO dto = buildWikiVoteDTO(wikiVote);
        return dto;
    }

    private WikiVoteDTO buildWikiVoteDTO(WikiVote wikiVote) {
        if (wikiVote != null) {
            WikiVoteDTO dto = new WikiVoteDTO();
            dto.setId(wikiVote.getArticleId());
            dto.setVotesum(wikiVote.getVotesSum());
            dto.setNonum(wikiVote.getNoStr());
            dto.setName(wikiVote.getName());
            dto.setPic(wikiVote.getPic());
            dto.setUrl(wikiVote.getUrl());
            return dto;
        } else {
            return null;
        }
    }

    public void incWikiVote(String url, Long articleId) throws ServiceException {
        BasicDBObject query = new BasicDBObject();
        query.put(WikiVoteField.ARTICLE_ID.getColumn(), articleId);
        BasicDBObject update = new BasicDBObject();
        update.append("$inc", new BasicDBObject(WikiVoteField.VOTESSUM.getColumn(), 1));
        CommentServiceSngl.get().incWikiVote(url, articleId, query, update);
    }

    public List<WikiVoteDTO> queryWikiVotes(Set<Long> idSet) throws ServiceException {
        List<WikiVoteDTO> returnList = new ArrayList<WikiVoteDTO>();

        List<WikiVote> wikiVoteList = CommentServiceSngl.get().queryWikiVotes(idSet);
        if (!CollectionUtil.isEmpty(wikiVoteList)) {
            for (WikiVote wikiVote : wikiVoteList) {
                WikiVoteDTO dto = buildWikiVoteDTO(wikiVote);
                if (dto != null) {
                    returnList.add(dto);
                }
            }
            Collections.sort(returnList, new Comparator<WikiVoteDTO>() {
                @Override
                public int compare(WikiVoteDTO o1, WikiVoteDTO o2) {
                    return o1.getVotesum() < o2.getVotesum() ? 1 : (o1.getVotesum() == o2.getVotesum() ? 0 : -1);
                }
            });
        }

        return returnList;
    }

    public List<WikiVoteDTO> queryWikiVoteByKey(String keyWords) throws ServiceException {
        List<WikiVoteDTO> returnList = new ArrayList<WikiVoteDTO>();

        MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
        mongoQueryExpress.add(MongoQueryCriterions.eq(WikiVoteField.KEYWORDS, keyWords));
        
        mongoQueryExpress.add(new MongoSort[]{new MongoSort(WikiVoteField.VOTESSUM, MongoSortOrder.DESC)});

        Pagination pagination = new Pagination(10, 1, 10);

        List<WikiVote> wikiVoteList = CommentServiceSngl.get().queryWikiVoteByPage(mongoQueryExpress, pagination);
        if (!CollectionUtil.isEmpty(wikiVoteList)) {
            for (WikiVote wikiVote : wikiVoteList) {
                WikiVoteDTO dto = buildWikiVoteDTO(wikiVote);
                if (dto != null) {
                    returnList.add(dto);
                }
            }
            Collections.sort(returnList, new Comparator<WikiVoteDTO>() {
                @Override
                public int compare(WikiVoteDTO o1, WikiVoteDTO o2) {
                    return o1.getVotesum() < o2.getVotesum() ? 1 : (o1.getVotesum() == o2.getVotesum() ? 0 : -1);
                }
            });
        }

        return returnList;
    }
}
