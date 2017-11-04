package com.enjoyf.webapps.joyme.dto.comment;

import java.io.Serializable;
import java.util.List;

import com.enjoyf.platform.util.PageRows;

/**
 * Created by zhitaoshi on 2016/1/5.
 */
public class LiveCommentDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;// todo 没意义可以去掉
	private String commentId;   //PK md5(uniqueKey+domain)
    private String uniqueKey;  //业务唯一标识
    private String uri;  //链接地址 通过uri进行索引，如果是prifilepic就是profileid
    private int domain;  //来源
    private String title; //标题
    private String pic; // 图片 进来存path
    private String expandstr;// todo 扩展字段 玩霸里作为图片储存json格式
    private String description;   //描述
    private int scoreCommentSum;     //  TODO:直播的点赞数
    private long createTime;    //创建时间

    private long groupId; //圈子ID

    private String dateStr;
    
    private PageRows<ReplyDTO> replyRows;//单条直播内容回复列表
    
    public String getCommentId() {
        return commentId;
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

    public int getDomain() {
        return domain;
    }

    public void setDomain(int domain) {
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

    public String getExpandstr() {
        return expandstr;
    }

    public void setExpandstr(String expandstr) {
        this.expandstr = expandstr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
	
	public PageRows<ReplyDTO> getReplyRows() {
		return replyRows;
	}

	public void setReplyRows(PageRows<ReplyDTO> replyRows) {
		this.replyRows = replyRows;
	}

    public int getScoreCommentSum() {
        return scoreCommentSum;
    }

    public void setScoreCommentSum(int scoreCommentSum) {
        this.scoreCommentSum = scoreCommentSum;
    }
}
