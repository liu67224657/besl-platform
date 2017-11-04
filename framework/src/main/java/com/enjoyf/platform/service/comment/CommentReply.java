/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(value = {"removeStatus","domain","customerStatus","displayHot",""})
public class CommentReply implements Serializable {

    private long replyId;//评论ID
    private String replyUno;

    private String replyProfileId;
    private String replyProfileKey;

    private String commentId;

    private long parentId;//父类ID
    private String parentUno;
    private String parentProfileId;
    private String parentProfileKey;

    private long rootId;//跟ID 如果是评论=0
    private String rootUno;
    private String rootProfileId;
    private String rootProfileKey;

    private int subReplySum;//回复数
    private int agreeSum;//支持
    private int disagreeSum;//不支持

    private ReplyBody body;

    private Date createTime;
    private Date modifyTime;
    private String createIp;

    private ActStatus removeStatus = ActStatus.UNACT;

    private int floorNum;//用redis去维护的

    private int totalRows;

    private double score;//评分
    private long displayOrder;//排序
    private CommentDomain domain;//评论的类型
    private int replyAgreeSum;//点赞 + 回复
    private int displayHot = CommentReplyDisplayHotType.ALLOW.getCode(); //是否可以出现在热门，默认0,0--可以，1--不可以

    //客服回复状态
    private int customerStatus = CommentReplyCustomerStatus.NOT.getCode();  //默认0，0--未回复，1--已回复   ,2 本身即是客服的回复

    //wiki的二级搜索key
    private String subKey;

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public String getReplyUno() {
        return replyUno;
    }

    public void setReplyUno(String replyUno) {
        this.replyUno = replyUno;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public long getRootId() {
        return rootId;
    }

    public void setRootId(long rootId) {
        this.rootId = rootId;
    }

    public String getRootUno() {
        return rootUno;
    }

    public void setRootUno(String rootUno) {
        this.rootUno = rootUno;
    }

    public int getSubReplySum() {
        return subReplySum;
    }

    public void setSubReplySum(int subReplySum) {
        this.subReplySum = subReplySum;
    }

    public int getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(int agreeSum) {
        this.agreeSum = agreeSum;
    }

    public int getDisagreeSum() {
        return disagreeSum;
    }

    public void setDisagreeSum(int disagreeSum) {
        this.disagreeSum = disagreeSum;
    }

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
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

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public CommentDomain getDomain() {
        return domain;
    }

    public void setDomain(CommentDomain domain) {
        this.domain = domain;
    }

    public String getReplyProfileId() {
        return replyProfileId;
    }

    public void setReplyProfileId(String replyProfileId) {
        this.replyProfileId = replyProfileId;
    }

    public String getReplyProfileKey() {
        return replyProfileKey;
    }

    public void setReplyProfileKey(String replyProfileKey) {
        this.replyProfileKey = replyProfileKey;
    }

    public String getParentProfileId() {
        return parentProfileId;
    }

    public void setParentProfileId(String parentProfileId) {
        this.parentProfileId = parentProfileId;
    }

    public String getParentProfileKey() {
        return parentProfileKey;
    }

    public void setParentProfileKey(String parentProfileKey) {
        this.parentProfileKey = parentProfileKey;
    }

    public String getRootProfileId() {
        return rootProfileId;
    }

    public void setRootProfileId(String rootProfileId) {
        this.rootProfileId = rootProfileId;
    }

    public String getRootProfileKey() {
        return rootProfileKey;
    }

    public void setRootProfileKey(String rootProfileKey) {
        this.rootProfileKey = rootProfileKey;
    }

    public static String toJson(CommentReply reply) {
        return JsonBinder.buildNormalBinder().toJson(reply);
    }

    public int getReplyAgreeSum() {
        return replyAgreeSum;
    }

    public void setReplyAgreeSum(int replyAgreeSum) {
        this.replyAgreeSum = replyAgreeSum;
    }

    public int getDisplayHot() {
        return displayHot;
    }

    public void setDisplayHot(int displayHot) {
        this.displayHot = displayHot;
    }

    public int getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(int customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public static CommentReply parse(String jsonStr) {
        CommentReply returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<CommentReply>() {
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
