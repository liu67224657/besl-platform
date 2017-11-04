/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(value = {"domain", "removeStatus", ""})
public class CommentBean implements Serializable {

    private String commentId;   //PK md5(uniqueKey+domain)
    private String uniqueKey;  //业务唯一标识
    private String uri;  //链接地址 通过uri进行索引，如果是prifilepic就是profileid
    private CommentDomain domain;  //来源
    private String title; //标题
    private String pic; // 图片 进来存path
    private String expandstr;// todo 扩展字段 玩霸里作为图片储存json格式
    private String description;   //描述

    private Date createTime;    //创建时间
    private Date modifyTime;    //修改时间
    private ActStatus removeStatus = ActStatus.UNACT;    //删除状态

    private int totalRows;     //评论的数据记录数，只增不减
    private int commentSum;      //可用的评论数  有增有减 TODO:玩霸的评论数

    private int longCommentSum;    //长评数,星星数   TODO:玩霸的阅读数
    private double averageScore;      //平均评分total_rows/reply_num
    private long displayOrder;       //排序
    private int scoreCommentSum;     //评分+评论 数  TODO:玩霸的点赞数  TODO:直播的点赞数

    private double scoreSum;  //总评分
    private int scoreTimes;   //评分总次数
    private int fiveUserSum;   //5星 评分 人数   TODO:玩霸的真实阅读数
    private int fourUserSum;  //4星 评分 人数    TODO:玩霸的真实点赞数
    private int threeUserSum;   //3星 评分 人数
    private int twoUserSum;    //2星 评分 人数
    private int oneUserSum;    //1星 评分 人数 TODO:玩霸置顶 -1置顶
    private int shareSum;    //分享次数
    private Long groupId; //圈子ID
    private String profileId;

    private List<CommentVoteOption> voteList = new ArrayList<CommentVoteOption>();   //用于wiki vote
    private Profile profile;     //关联创建者，用于wik vote

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<CommentVoteOption> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<CommentVoteOption> voteList) {
        this.voteList = voteList;
    }

    public String getCommentId() {
        return buildCommentId(uniqueKey, domain);
    }

    public String buildCommentId(String uniqueKey, CommentDomain domain) {
        return MD5Util.Md5(uniqueKey + domain.getCode());
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public CommentDomain getDomain() {
        return domain;
    }

    public void setDomain(CommentDomain domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }

    public int getLongCommentSum() {
        return longCommentSum;
    }

    public void setLongCommentSum(int longCommentSum) {
        this.longCommentSum = longCommentSum;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getScoreCommentSum() {
        return scoreCommentSum;
    }

    public void setScoreCommentSum(int scoreCommentSum) {
        this.scoreCommentSum = scoreCommentSum;
    }

    public double getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(double scoreSum) {
        this.scoreSum = scoreSum;
    }

    public int getScoreTimes() {
        return scoreTimes;
    }

    public void setScoreTimes(int scoreTimes) {
        this.scoreTimes = scoreTimes;
    }

    public int getFiveUserSum() {
        return fiveUserSum;
    }

    public void setFiveUserSum(int fiveUserSum) {
        this.fiveUserSum = fiveUserSum;
    }

    public int getFourUserSum() {
        return fourUserSum;
    }

    public void setFourUserSum(int fourUserSum) {
        this.fourUserSum = fourUserSum;
    }

    public int getThreeUserSum() {
        return threeUserSum;
    }

    public void setThreeUserSum(int threeUserSum) {
        this.threeUserSum = threeUserSum;
    }

    public int getTwoUserSum() {
        return twoUserSum;
    }

    public void setTwoUserSum(int twoUserSum) {
        this.twoUserSum = twoUserSum;
    }

    public int getOneUserSum() {
        return oneUserSum;
    }

    public void setOneUserSum(int oneUserSum) {
        this.oneUserSum = oneUserSum;
    }

    public int getShareSum() {
        return shareSum;
    }

    public void setShareSum(int shareSum) {
        this.shareSum = shareSum;
    }

    public static String toJson(CommentBean bean) {
        return JsonBinder.buildNormalBinder().toJson(bean);
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getExpandstr() {
        return expandstr;
    }

    public void setExpandstr(String expandstr) {
        this.expandstr = expandstr;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public static List<String> fromJson(String jsonString) {
        List<String> resultList = new ArrayList<String>();
        try {
            resultList = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static CommentBean parse(String jsonStr) {
        CommentBean returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<CommentBean>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
