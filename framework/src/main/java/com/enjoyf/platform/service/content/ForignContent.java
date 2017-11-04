/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@fivewh.com">Eric Liu</a>
 * Create time: 11-8-17 下午5:21
 * Description:  外部文章表
 */
public class ForignContent implements Serializable {

    private long contentId;   //PK
    private String forignId;  //链接ID唯一标识
    private String contentUrl;  //链接地址
    private ForignContentDomain contentDomain;  //来源
    private int replyNum;       //评论数
    private String contentTitle; //标题
    private String contentDesc;   //描述
    private Date createTime;    //创建时间
    private ActStatus removeStatus = ActStatus.UNACT;

    private int totalRows;

    private String keyWords;

	private int long_comment_num;//长评数
	private double average_score;//平均评分total_rows/reply_num
	private long display_order;//排序
	private int scorereply_num;//有评分又评论数

	public int getScorereply_num() {
		return scorereply_num;
	}

	public void setScorereply_num(int scorereply_num) {
		this.scorereply_num = scorereply_num;
	}

	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	public String getForignId() {
		return forignId;
	}

	public void setForignId(String forignId) {
		this.forignId = forignId;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public ForignContentDomain getContentDomain() {
		return contentDomain;
	}

	public void setContentDomain(ForignContentDomain contentDomain) {
		this.contentDomain = contentDomain;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public int getLong_comment_num() {
		return long_comment_num;
	}

	public void setLong_comment_num(int long_comment_num) {
		this.long_comment_num = long_comment_num;
	}

	public double getAverage_score() {
		return average_score;
	}

	public void setAverage_score(double average_score) {
		this.average_score = average_score;
	}

	public long getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(long display_order) {
		this.display_order = display_order;
	}

	@Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
