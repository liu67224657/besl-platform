package com.enjoyf.webapps.joyme.weblogic.comment;


import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.point.LotteryType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.dto.comment.*;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 上午11:39
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "commentWebLogic")
public class CommentWebLogic {

    private static final Logger logger = LoggerFactory.getLogger(CommentWebLogic.class);
    private final Long TAGID = -2l; //迷友圈标识ID

    /**
     * 查询主楼的逻辑
     *
     * @param commentBean
     * @param pageNum
     * @param pageSize
     * @param desc
     * @return
     * @throws ServiceException
     */
    public PageRows<MainReplyDTO> queryMainReplyDTO(CommentBean commentBean, int pageNum, int pageSize, boolean desc) throws ServiceException {
        PageRows<MainReplyDTO> returnObj = new PageRows<MainReplyDTO>();
        if (commentBean == null) {
            return returnObj;
        }
        //主楼评论
        PageRows<CommentReply> mainReplyRows = buildCommentReply(commentBean.getCommentId(), 0, commentBean.getTotalRows(), pageNum, pageSize, desc);
        if (mainReplyRows == null) {
            return returnObj;
        } else if (CollectionUtil.isEmpty(mainReplyRows.getRows())) {
            returnObj.setPage(mainReplyRows.getPage());
            return returnObj;
        }

        if (commentBean.getDomain().equals(CommentDomain.ZONGYI_MI)) {
            desc = false;
        }

        //查询楼中楼
        returnObj = buildMainReplyDTO(commentBean, mainReplyRows, pageSize, desc, -1, -1);
        return returnObj;
    }

    /**
     * @param commentBean
     * @param queryReplyId
     * @param pageSize
     * @param desc
     * @return
     * @throws ServiceException
     */
    public PageRows<MainReplyDTO> queryMainReplyDTOByQueryReplyId(CommentBean commentBean, long queryReplyId, int pageSize, boolean desc) throws ServiceException {
        PageRows<MainReplyDTO> returnObj = new PageRows<MainReplyDTO>();

        //获取查询的评论,得到replyId和rootId
        CommentReply commentReply = CommentServiceSngl.get().getCommentReplyById(commentBean.getCommentId(), queryReplyId);
        long replyId = -1l;
        long rootId = -1l;
        PageRows<CommentReply> mainReplyRows;
        if (commentReply != null) {
            replyId = commentReply.getReplyId();
            rootId = commentReply.getRootId();

            //如果评论是个主楼rootid==0,则用replyId当成主楼查询,否则,主楼的ID是rootid
            if (rootId == 0) {
                mainReplyRows = buildCommentReplyByReplyId(commentBean.getCommentId(), rootId, replyId, pageSize, desc);
            } else {
                mainReplyRows = buildCommentReplyByReplyId(commentBean.getCommentId(), 0, rootId, pageSize, desc);
            }
        } else {
            //评论不存在
            mainReplyRows = buildCommentReply(commentBean.getCommentId(), 0, commentBean.getTotalRows(), 1, pageSize, desc);
        }

        if (mainReplyRows == null) {
            return returnObj;
        } else if (CollectionUtil.isEmpty(mainReplyRows.getRows())) {
            returnObj.setPage(mainReplyRows.getPage());
            return returnObj;
        }

        if (commentBean.getDomain().equals(CommentDomain.ZONGYI_MI)) {
            desc = false;
        }

        //查询楼中楼
        returnObj = buildMainReplyDTO(commentBean, mainReplyRows, pageSize, desc, rootId, replyId);
        return returnObj;
    }

    /**
     * 根据主楼的评论加载楼中楼的逻辑,返回MainReplyDTO
     * 如果rootId<=0 || replyId<=0 则查询rootid对应的主楼的第一页数据
     *
     * @param commentBean
     * @param mainReplyRows
     * @param subPageSize
     * @param desc
     * @param rootId        主楼的ID
     * @param replyId       楼中楼的ID
     * @return
     * @throws ServiceException
     */
    private PageRows<MainReplyDTO> buildMainReplyDTO(CommentBean commentBean, PageRows<CommentReply> mainReplyRows, int subPageSize, boolean desc, long rootId, long replyId) throws ServiceException {
        PageRows<MainReplyDTO> returnObj = new PageRows<MainReplyDTO>();

        Set<String> profileIdSet = new HashSet<String>();
        Map<Long, PageRows<CommentReply>> map = new HashMap<Long, PageRows<CommentReply>>();

        for (CommentReply mainReply : mainReplyRows.getRows()) {
            if (mainReply == null) {
                continue;
            }
            profileIdSet.add(mainReply.getReplyProfileId());
            //主楼的二级回复
            //综艺迷的需要评论正序
            if (commentBean.getDomain().equals(CommentDomain.ZONGYI_MI)) {
                desc = false;
            }

            PageRows<CommentReply> subReplyRows = null;
            if (rootId > 0 && mainReply.getReplyId() == rootId && replyId > 0) {
                //如果rootId>0,并且replyId>0,并且rootid==主楼的ID,通过replyId获取楼中楼,对应的那一页数据
                subReplyRows = buildCommentReplyByReplyId(commentBean.getCommentId(), mainReply.getReplyId(), replyId, subPageSize, desc);
            } else {
                //获取该主楼对应的楼中楼的那一页数据
                subReplyRows = buildCommentReply(commentBean.getCommentId(), mainReply.getReplyId(), mainReply.getTotalRows(), 1, subPageSize, desc);
            }
            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
                for (CommentReply subReply : subReplyRows.getRows()) {
                    profileIdSet.add(subReply.getReplyProfileId());
                    if (!StringUtil.isEmpty(subReply.getParentProfileId()) && !subReply.getParentProfileId().equals(subReply.getRootProfileId())) {
                        profileIdSet.add(subReply.getParentProfileId());
                    }
                }

                map.put(mainReply.getReplyId(), subReplyRows);
            }
        }

        //query user
        Map<String, UserEntity> userEntityMap = queryUserEntity(profileIdSet);

        //build dto
        List<MainReplyDTO> returnList = new ArrayList<MainReplyDTO>();
        for (CommentReply reply : mainReplyRows.getRows()) {
            MainReplyDTO dto = buildMainReplyDTO(reply, userEntityMap, map);
            if (dto != null) {
                returnList.add(dto);
            }
        }

        returnObj.setRows(returnList);
        returnObj.setPage(mainReplyRows.getPage());
        return returnObj;
    }
//
//    private PageRows<MainReplyDTO> loadMainReplyDTOByRootIdAndReplyId(CommentBean commentBean, PageRows<CommentReply> mainReplyRows, long rootId, long replyId, int subPageSize, boolean desc) throws ServiceException {
//        PageRows<MainReplyDTO> returnObj = new PageRows<MainReplyDTO>();
//
//        Set<String> profileIdSet = new HashSet<String>();
//        Map<Long, PageRows<CommentReply>> map = new HashMap<Long, PageRows<CommentReply>>();
//
//        for (CommentReply mainReply : mainReplyRows.getRows()) {
//            if (mainReply == null) {
//                continue;
//            }
//            profileIdSet.add(mainReply.getReplyProfileId());
//            //主楼的二级回复
//            //综艺迷的需要评论正序
//            if (commentBean.getDomain().equals(CommentDomain.ZONGYI_MI)) {
//                desc = false;
//            }
//
//            PageRows<CommentReply> subReplyRows = buildCommentReplyByReplyId(commentBean.getCommentId(), mainReply.getReplyId(), mainReply.getTotalRows(), 1, subPageSize, desc);
//            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
//                for (CommentReply subReply : subReplyRows.getRows()) {
//                    profileIdSet.add(subReply.getReplyProfileId());
//                    if (!StringUtil.isEmpty(subReply.getParentProfileId()) && !subReply.getParentProfileId().equals(subReply.getRootProfileId())) {
//                        profileIdSet.add(subReply.getParentProfileId());
//                    }
//                }
//
//                map.put(mainReply.getReplyId(), subReplyRows);
//            }
//        }
//
//        //query user
//        Map<String, UserEntity> userEntityMap = queryUserEntity(profileIdSet);
//
//        //build dto
//        List<MainReplyDTO> returnList = new ArrayList<MainReplyDTO>();
//        for (CommentReply reply : mainReplyRows.getRows()) {
//            MainReplyDTO dto = buildMainReplyDTO(reply, userEntityMap, map);
//            if (dto != null) {
//                returnList.add(dto);
//            }
//        }
//
//        returnObj.setRows(returnList);
//        returnObj.setPage(mainReplyRows.getPage());
//        return returnObj;
//    }

    /**
     * @param commentBean
     * @param pageSize
     * @param isCache     是否取缓存数据 true false
     * @return
     * @throws ServiceException
     */
    public List<MainReplyDTO> queryHotMainReplyDTO(CommentBean commentBean, int pageSize, boolean isCache) throws ServiceException {
        List<MainReplyDTO> returnObj = new ArrayList<MainReplyDTO>();
        //主楼评论
        Pagination pagination = new Pagination(commentBean.getTotalRows(), 1, pageSize);
        List<CommentReply> mainReplyRows = CommentServiceSngl.get().queryHotReplyByCache(commentBean.getCommentId(), pagination, isCache);
        if (mainReplyRows == null || CollectionUtil.isEmpty(mainReplyRows)) {
            return returnObj;
        }

        Set<String> profileIdSet = new HashSet<String>();
        Map<Long, PageRows<CommentReply>> map = new HashMap<Long, PageRows<CommentReply>>();

        for (CommentReply mainReply : mainReplyRows) {
            if (mainReply == null) {
                continue;
            }
            profileIdSet.add(mainReply.getReplyProfileId());
            //主楼的二级回复      todo
            boolean desc = true;
            //综艺迷需要按升序排序
            if (commentBean.getDomain().equals(CommentDomain.ZONGYI_MI)) {
                desc = false;
            }
            PageRows<CommentReply> subReplyRows = buildCommentReply(commentBean.getCommentId(), mainReply.getReplyId(), mainReply.getTotalRows(), 1, 10, desc);
            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
                for (CommentReply subReply : subReplyRows.getRows()) {
                    profileIdSet.add(subReply.getReplyProfileId());
                    if (!StringUtil.isEmpty(subReply.getParentProfileId()) && !subReply.getParentProfileId().equals(subReply.getRootProfileId())) {
                        profileIdSet.add(subReply.getParentProfileId());
                    }
                }

                map.put(mainReply.getReplyId(), subReplyRows);
            }
        }

        //query user
        Map<String, UserEntity> userEntityMap = queryUserEntity(profileIdSet);

        //build dto
        for (CommentReply reply : mainReplyRows) {
            MainReplyDTO dto = buildMainReplyDTO(reply, userEntityMap, map);
            if (dto != null) {
                returnObj.add(dto);
            }
        }
        return returnObj;
    }

    public PageRows<MainReplyDTO> queryReplyByOrderField(CommentBean commentBean, int pageNum, int pageSize, CommentReplyField orderField, QuerySortOrder sortOrder) throws ServiceException {
        PageRows<MainReplyDTO> returnObj = new PageRows<MainReplyDTO>();
        //主楼评论
        PageRows<CommentReply> mainReplyRows = buildReplyByOrderField(commentBean.getCommentId(), 0, commentBean.getTotalRows(), pageNum, pageSize, orderField, sortOrder);
        if (mainReplyRows == null) {
            return returnObj;
        } else if (CollectionUtil.isEmpty(mainReplyRows.getRows())) {
            returnObj.setPage(mainReplyRows.getPage());
            return returnObj;
        }

        Set<String> profileIdSet = new HashSet<String>();
        Map<Long, PageRows<CommentReply>> map = new HashMap<Long, PageRows<CommentReply>>();

        for (CommentReply mainReply : mainReplyRows.getRows()) {
            if (mainReply == null) {
                continue;
            }
            profileIdSet.add(mainReply.getReplyProfileId());
            //主楼的二级回复      todo
            PageRows<CommentReply> subReplyRows = buildReplyByOrderField(commentBean.getCommentId(), mainReply.getReplyId(), mainReply.getTotalRows(), 1, pageSize, orderField, sortOrder);
            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
                for (CommentReply subReply : subReplyRows.getRows()) {
                    profileIdSet.add(subReply.getReplyProfileId());
                    if (!StringUtil.isEmpty(subReply.getParentProfileId()) && !subReply.getParentProfileId().equals(subReply.getRootProfileId())) {
                        profileIdSet.add(subReply.getParentProfileId());
                    }
                }

                map.put(mainReply.getReplyId(), subReplyRows);
            }
        }

        //query user
        Map<String, UserEntity> userEntityMap = queryUserEntity(profileIdSet);

        //build dto
        List<MainReplyDTO> returnList = new ArrayList<MainReplyDTO>();
        for (CommentReply reply : mainReplyRows.getRows()) {
            MainReplyDTO dto = buildMainReplyDTO(reply, userEntityMap, map);
            if (dto != null) {
                returnList.add(dto);
            }
        }

        returnObj.setRows(returnList);
        returnObj.setPage(mainReplyRows.getPage());

        //查询楼中楼
        return returnObj;
    }

    private MainReplyDTO buildMainReplyDTO(CommentReply reply, Map<String, UserEntity> userEntityMap, Map<Long, PageRows<CommentReply>> reReplyMap) {
        MainReplyDTO returnObj = null;

        //主楼
        ReplyDTO mainDTO = buildReplyDTO(reply, userEntityMap);
        if (mainDTO == null) {
            return null;
        }

        returnObj = new MainReplyDTO();
        returnObj.setReply(mainDTO);

        //二级回复
        PageRows<CommentReply> subReplyRows = reReplyMap.get(reply.getReplyId());
        if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
            PageRows<ReplyDTO> subDTOs = new PageRows<ReplyDTO>();
            subDTOs.setPage(subReplyRows == null ? null : subReplyRows.getPage());

            List<ReplyDTO> subReplyDTOList = new ArrayList<ReplyDTO>();
            for (CommentReply subReply : subReplyRows.getRows()) {
                ReplyDTO subDTO = buildReplyDTO(subReply, userEntityMap);
                if (subDTO == null) {
                    continue;
                }
                subReplyDTOList.add(subDTO);
            }
            subDTOs.setRows(subReplyDTOList);

            returnObj.setSubreplys(subDTOs);
        }

        return returnObj;
    }

    public ReplyDTO buildReplyDTO(CommentReply reply, Map<String, UserEntity> profileMap) {
        UserEntity userEntity = profileMap.get(reply.getReplyProfileId());
        if (userEntity == null) {
            return null;
        }

        ReplyEntity replyEntity = buildReplyEntry(reply);

        ReplyDTO replyDTO = new ReplyDTO();

        replyDTO.setUser(userEntity);
        replyDTO.setReply(replyEntity);
        if (reply.getParentId() != 0 && reply.getParentId() != reply.getRootId()) {
            replyDTO.setPuser(profileMap.get(reply.getParentProfileId()));
        }
        return replyDTO;
    }

    public ReplyEntity buildReplyEntry(CommentReply reply) {
        ReplyEntity replyEntity = new ReplyEntity();
        replyEntity.setRid(reply.getReplyId());
        replyEntity.setPid(reply.getParentId());
        replyEntity.setOid(reply.getRootId());
        replyEntity.setAgree_sum(reply.getAgreeSum());
        String text = getBodyText(reply.getBody().getText());
        if (reply.getDomain().equals(CommentDomain.ZONGYI_MI)) {
            text = reply.getBody().getText();
        }

        //数字站评论  ##aaaa##   过滤
        if (reply.getDomain().equals(CommentDomain.DIGITAL_COMMENT)) {
            Pattern chinesePattern = Pattern.compile("[#|＃]{2}(.*?)[#|＃]{2}");
            Matcher matcher = chinesePattern.matcher(text);
            while (matcher.find()) {
                String matcherText = matcher.group(0);
                String replaceText = matcherText.replaceAll("[#|＃]", "");
                if (replaceText.length() > 3) {
                    replaceText = replaceText.substring(0, 3) + "***";
                }
                text = text.replace(matcherText, replaceText);
            }
        }

        reply.getBody().setText(text);
        replyEntity.setBody(reply.getBody());

        replyEntity.setDisagree_sum(reply.getDisagreeSum());
        replyEntity.setFloor_num(reply.getFloorNum());
        replyEntity.setPost_date(DateUtil.parseDate(reply.getCreateTime()));
        replyEntity.setPost_time(reply.getCreateTime().getTime());
        replyEntity.setSub_reply_sum(reply.getSubReplySum());
        return replyEntity;
    }

    private String getBodyText(String text) {
        String regex = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getCommentReplyNonEscapeHtml();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        Map<String, String> param = new HashMap<String, String>();
        int i = 0;
        while (matcher.find()) {
            String findHtml = matcher.group(0);
            param.put("value" + i, findHtml);
            text = text.replace(findHtml, "${value" + i + "}");
            i++;
        }
        text = processTextByKey(WordProcessorKey.KEY_PRIVIEW_REPLAY, text);
        text = NamedTemplate.parse(text).format(param);
        return text;
    }

    private static String processTextByKey(WordProcessorKey key, String content) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

    public Map<String, UserEntity> queryUserEntity(Set<String> profileIdSet) throws ServiceException {
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
        Map<String, Map<String, String>> profileChooseMap = PointServiceSngl.get().queryChooseLottery(profileIdSet);

        Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);

        Map<String, UserEntity> returnMap = new HashMap<String, UserEntity>();

        if (!CollectionUtil.isEmpty(profileMap)) {
            for (String profileId : profileMap.keySet()) {
                UserEntity userEntity = buildUserEntity(profileMap.get(profileId), profileChooseMap.get(profileId), verifyProfileMap.get(profileId));
                returnMap.put(profileId, userEntity);
            }
        }
        return returnMap;
    }

    public PageRows<CommentReply> buildCommentReply(String commentId, long rootId, int totalRows, int pageNum, int pageSize, boolean desc) throws ServiceException {
        PageRows<CommentReply> replyRows = new PageRows<CommentReply>();
        Pagination pagination = new Pagination(pageSize * pageNum, pageNum, pageSize);
        //查询
        replyRows = CommentServiceSngl.get().queryCommentReplyByCache(commentId, rootId, pagination, desc);

        if (replyRows == null || CollectionUtil.isEmpty(replyRows.getRows())) {
            return replyRows;
        }
        if (desc) {
            Collections.sort(replyRows.getRows(), new Comparator<CommentReply>() {
                @Override
                public int compare(CommentReply o1, CommentReply o2) {
                    return o1.getReplyId() < o2.getReplyId() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        } else {
            Collections.sort(replyRows.getRows(), new Comparator<CommentReply>() {
                @Override
                public int compare(CommentReply o1, CommentReply o2) {
                    return o1.getReplyId() > o2.getReplyId() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        return replyRows;
    }


    public PageRows<CommentReply> buildCommentReplyByReplyId(String commentId, long rootId, long replyId, int pageSize, boolean desc) throws ServiceException {
        //查询
        PageRows<CommentReply> replyRows = CommentServiceSngl.get().queryCommentReplyByReplyIdFromCache(commentId, rootId, replyId, pageSize, desc);
        return replyRows;
    }

    public PageRows<CommentReply> buildReplyByOrderField(String commentId, long rootId, int totalRows, int pageNum, int pageSize, CommentReplyField orderField, QuerySortOrder order) throws ServiceException {
        PageRows<CommentReply> replyRows = new PageRows<CommentReply>();

        if (totalRows <= 0) {
            //没有评论
            return replyRows;
        }
        Pagination pagination = new Pagination(totalRows, 1, pageSize);

        if (pagination.getMaxPage() < pageNum) {
            //超过查询范围
            return replyRows;
        }
        pagination.setCurPage(pageNum);
        //查询
        replyRows = CommentServiceSngl.get().queryCommentReplyByOrderField(commentId, rootId, pagination, orderField, order);

        if (QuerySortOrder.DESC.equals(order) && CommentReplyField.AGREE_SUM.equals(orderField)) {
            Collections.sort(replyRows.getRows(), new Comparator<CommentReply>() {
                @Override
                public int compare(CommentReply o1, CommentReply o2) {
                    return o1.getAgreeSum() < o2.getAgreeSum() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        } else if (QuerySortOrder.DESC.equals(order) && CommentReplyField.CREATE_TIME.equals(orderField)) {
            Collections.sort(replyRows.getRows(), new Comparator<CommentReply>() {
                @Override
                public int compare(CommentReply o1, CommentReply o2) {
                    return o1.getReplyId() < o2.getReplyId() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        } else if (QuerySortOrder.ASC.equals(order) && CommentReplyField.CREATE_TIME.equals(orderField)) {
            Collections.sort(replyRows.getRows(), new Comparator<CommentReply>() {
                @Override
                public int compare(CommentReply o1, CommentReply o2) {
                    return o1.getReplyId() > o2.getReplyId() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        return replyRows;
    }

    public ScoreEntity buildScoreEntity(CommentBean commentBean) {
        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setUri(commentBean.getUri() == null ? "" : commentBean.getUri());
        scoreEntity.setTitle(commentBean.getTitle() == null ? "" : commentBean.getTitle());
        scoreEntity.setDescription(commentBean.getDescription() == null ? "" : commentBean.getDescription());
        scoreEntity.setPic(commentBean.getPic() == null ? "" : commentBean.getPic());
        scoreEntity.setPost_date(commentBean.getCreateTime());
        scoreEntity.setPost_time(commentBean.getCreateTime().getTime());

        scoreEntity.setScore_sum(commentBean.getScoreSum());
        scoreEntity.setTimes_sum(commentBean.getScoreTimes());
        DecimalFormat df = new DecimalFormat("#.0");
        if (commentBean.getScoreTimes() > 0) {
            scoreEntity.setAverage_score(Double.valueOf(df.format(commentBean.getScoreSum() / (double) commentBean.getScoreTimes())));
            scoreEntity.setFive_percent(df.format(((double) commentBean.getFiveUserSum() / (double) commentBean.getScoreTimes()) * 100) + "%");
            scoreEntity.setFour_percent(df.format(((double) commentBean.getFourUserSum() / (double) commentBean.getScoreTimes()) * 100) + "%");
            scoreEntity.setThree_percent(df.format(((double) commentBean.getThreeUserSum() / (double) commentBean.getScoreTimes()) * 100) + "%");
            scoreEntity.setTwo_percent(df.format(((double) commentBean.getTwoUserSum() / (double) commentBean.getScoreTimes()) * 100) + "%");
            scoreEntity.setOne_percent(df.format(((double) commentBean.getOneUserSum() / (double) commentBean.getScoreTimes()) * 100) + "%");
        } else {
            scoreEntity.setAverage_score(0);
            scoreEntity.setFive_percent("0%");
            scoreEntity.setFour_percent("0%");
            scoreEntity.setThree_percent("0%");
            scoreEntity.setTwo_percent("0%");
            scoreEntity.setOne_percent("0%");
        }
        return scoreEntity;
    }

    private static final String LOTTERY_TYPE = "lotteryType_";

    public UserEntity buildUserEntity(Profile profile, Map<String, String> chooseMap, VerifyProfile verifyProfile) throws ServiceException {
        if (profile == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUno(profile.getUno());
        userEntity.setUid(profile.getUid());
        userEntity.setName(profile.getNick());
        userEntity.setDomain(profile.getDomain());
        userEntity.setVerify(VerifyType.N_VERIFY.getCode());
        userEntity.setPid(profile.getProfileId());
        userEntity.setSex(profile.getSex());
        if (chooseMap != null && !chooseMap.isEmpty()) {
            if (!StringUtil.isEmpty(chooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                userEntity.setHeadskin(chooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
            }
            if (!StringUtil.isEmpty(chooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                userEntity.setCardskin(chooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
            }
            if (!StringUtil.isEmpty(chooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                userEntity.setBubbleskin(chooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
            }
            if (!StringUtil.isEmpty(chooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                userEntity.setReplyskin(chooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
            }
        }
        if (!StringUtil.isEmpty(profile.getIcon())) {
            userEntity.setIcon(URLUtils.getJoymeDnUrl(profile.getIcon()));
        } else {
            if ("1".equals(profile.getSex())) {
                userEntity.setIcon("http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg");
            } else if ("0".equals(profile.getSex())) {
                userEntity.setIcon("http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg");
            } else {
                userEntity.setIcon("http://lib.joyme.com/static/theme/default/img/head_is_m.jpg");
            }
        }


        if (verifyProfile != null) {
            userEntity.setVdesc(StringUtil.isEmpty(verifyProfile.getDescription()) ? "" : verifyProfile.getDescription());//认证原因
            userEntity.setVtitle(StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());//认证title
            userEntity.setVtype(verifyProfile.getVerifyType());//认证类型
        }

        return userEntity;
    }

    public CommentBean createCommentBean(String uniKey, CommentDomain commentDomain, CommentJsonParam param) {
        CommentBean comment = new CommentBean();
        comment.setCommentId(CommentUtil.genCommentId(uniKey, commentDomain));
        comment.setUniqueKey(uniKey);
        comment.setDomain(commentDomain);
        if (param != null) {
            comment.setUri(param.getUri());
            comment.setTitle(param.getTitle());
            comment.setPic(param.getPic());
            comment.setDescription(param.getDescription());
        }
        comment.setCreateTime(new Date());
        comment.setRemoveStatus(ActStatus.UNACT);
        comment.setTotalRows(0);
        comment.setCommentSum(0);
        comment.setScoreSum(0);
        comment.setScoreTimes(0);
        comment.setOneUserSum(0);
        comment.setTwoUserSum(0);
        comment.setThreeUserSum(0);
        comment.setFourUserSum(0);
        comment.setFiveUserSum(0);
        comment.setShareSum(0);
        try {
            comment = CommentServiceSngl.get().createCommentBean(comment);
        } catch (ServiceException e) {
            GAlerter.lab("CommentWebLogic createCommentBean occur Exception.e:", e);
        }
        return comment;
    }

    public static void main(String[] args) {
        //数字站评论  ##aaaa##   过滤   \\u-\u4e00\u9fa5-\uffff
        String regex = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getCommentReplyNonEscapeHtml();
        String src = "a<img src=''/>bb<br/>ccc<img src=''/>dddd<input name='' />eeeee<div></div>ffffff";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);
        Map<String, String> param = new HashMap<String, String>();
        int i = 0;
        while (matcher.find()) {
            String findHtml = matcher.group(0);
            param.put("value" + i, findHtml);
            src = src.replace(findHtml, "${value" + i + "}");
            i++;
        }
        String text = processTextByKey(WordProcessorKey.KEY_POST_REPLY, src);
        src = NamedTemplate.parse(text).format(param);
        System.out.println(src);
    }

    public String postreply(String text) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(text);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_POST_REPLY);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }


    /**
     * @param commentBeanMap
     * @param groupMap       圈子的信息 用来得到圈子名称
     * @param uidParam       用户UID 用来查询是否点过赞
     * @return
     * @throws ServiceException
     */
    public List<MiyouDTO> buildMiyou(Map<String, CommentBean> commentBeanMap, Map<Long, String> groupMap, String uidParam) throws ServiceException {
        List<MiyouDTO> returnList = new ArrayList<MiyouDTO>();
        if (commentBeanMap != null) {
            List<CommentBean> miyouList = new ArrayList<CommentBean>(commentBeanMap.values());
            //存放我点赞过的帖子ID
            Set<String> historySet = new HashSet<String>();
            //存用户ID
            Set<String> profileSet = new HashSet<String>();

            for (CommentBean commentBean : miyouList) {
                profileSet.add(commentBean.getUri());
                historySet.add(commentBean.getCommentId());
            }

            List<CommentHistory> commentHistories = null;
            //查询“我”点过赞的
            if (!CollectionUtil.isEmpty(historySet)) {
                commentHistories = CommentServiceSngl.get().queryCommentHistory(new QueryExpress().add(QueryCriterions.eq(CommentHistoryField.PROFILE_ID, uidParam))
                        .add(QueryCriterions.eq(CommentHistoryField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()))
                        .add(QueryCriterions.in(CommentHistoryField.OBJECT_ID, historySet.toArray())));
            }
            Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileSet);


            for (CommentBean commentBean : miyouList) {
                MiyouDTO miyouDTO = new MiyouDTO();
                miyouDTO.setCommentnum(String.valueOf(commentBean.getCommentSum()));
                miyouDTO.setAgreenum(String.valueOf(commentBean.getScoreCommentSum()));
                miyouDTO.setCommentid(commentBean.getCommentId());
                miyouDTO.setDesc(commentBean.getDescription());
                miyouDTO.setGroupid(String.valueOf(commentBean.getGroupId()));
                miyouDTO.setGroupname(groupMap.get(commentBean.getGroupId()));
                miyouDTO.setType("1");//1是帖子
                Profile profile = profilesMap.get(commentBean.getUri());
                if (profile != null) {
                    miyouDTO.setUid(String.valueOf(profile.getUid()));
                    miyouDTO.setPicurl(profile.getIcon());
                    miyouDTO.setNick(profile.getNick());
                }
                //1是显示推荐 0不显示
                miyouDTO.setIsrecommend(String.valueOf(commentBean.getOneUserSum() == -1 ? 1 : 0));
                miyouDTO.setIsagree("0");
                for (CommentHistory commentHistory : commentHistories) {
                    if (commentHistory.getCommentId().equals(commentBean.getCommentId())) {
                        miyouDTO.setIsagree("1");
                        break;
                    }
                }
                miyouDTO.setTime(String.valueOf(commentBean.getCreateTime().getTime()));
                String pics = commentBean.getExpandstr();
                if (!StringUtil.isEmpty(pics)) {
                    List<String> imgs = new AbstractGameClientBaseController().fromJson(pics);
                    List<String> returnImgs = new ArrayList<String>();
                    for (String img : imgs) {

                        try {
                            img = img + "?imageView2/1/w/212/h/212" + URLEncoder.encode("|", "utf-8") + "imageMogr2/thumbnail/212x212" + URLEncoder.encode("!", "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            returnImgs.add(img);
                            continue;
                        }
                        returnImgs.add(img);
                    }
                    miyouDTO.setImgs(returnImgs);
                } else {
                    miyouDTO.setImgs(new ArrayList<String>());
                }

                miyouDTO.setJi("http://api." + WebUtil.getDomain() + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + commentBean.getCommentId());
                miyouDTO.setJt("-2");//圈子首页ID
                returnList.add(miyouDTO);
            }
        }

        return returnList;
    }

    /**
     * 加载直播内容的评论列表
     *
     * @param liveCommentRows TYPE
     * @return
     * @throws ServiceException
     */
    public List<LiveCommentDTO> queryReplyDTOList(List<LiveCommentDTO> liveCommentRows, int type) throws ServiceException {
        List<String> commentIds = new ArrayList<String>();
        for (LiveCommentDTO liveCommentDTO : liveCommentRows) {
            commentIds.add(liveCommentDTO.getCommentId());
        }

        //所有的评论
        int replyPageSize = 10;
        if (type == 2) {
            replyPageSize = 3;
        }
        Pagination pagination = new Pagination(0, 1, replyPageSize);
        Map<String, PageRows<CommentReply>> replyMap = CommentServiceSngl.get().queryCommentReplyFromCacheByCommentIds(commentIds, 0l, pagination, true);
        for (LiveCommentDTO liveCommentDTO : liveCommentRows) {
            PageRows<CommentReply> commentReplyPageRows = replyMap.get(liveCommentDTO.getCommentId());

            if (commentReplyPageRows != null) {
                //query user
                Set<String> profileIdSet = new HashSet<String>();
                for (CommentReply mainReply : commentReplyPageRows.getRows()) {
                    if (mainReply == null) {
                        continue;
                    }
                    profileIdSet.add(mainReply.getReplyProfileId());
                }
                Map<String, UserEntity> userEntityMap = queryUserEntity(profileIdSet);

                //build dto
                List<ReplyDTO> returnList = new ArrayList<ReplyDTO>();
                for (CommentReply reply : commentReplyPageRows.getRows()) {
                    ReplyDTO dto = buildReplyDTO(reply, userEntityMap);
                    if (dto != null) {
                        returnList.add(dto);
                    }
                }

                PageRows<ReplyDTO> replyPageRows = new PageRows<ReplyDTO>();
                replyPageRows.setRows(returnList);
                replyPageRows.setPage(commentReplyPageRows.getPage());


                liveCommentDTO.setReplyRows(replyPageRows);
            }

        }
        return liveCommentRows;
    }
}
