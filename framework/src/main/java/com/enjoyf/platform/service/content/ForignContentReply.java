/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description:  回复博文
 */
@JsonIgnoreProperties(value = {"removeStatus",""})
public class ForignContentReply implements Serializable {

    private long replyId;//评论ID
    private String replyUno;

    private long contentId;//文章的ID

    private long partentId;//父类ID
    private long rootId;//跟ID 如果是评论=0
    private int replyNum;//回复数
    private int agreeNum;//支持
	private int disagreeNum;//不支持

    private String rootUno;
    private String parentUno;

    private String body;
    private String pic;

    private Date createTime;
    private String createIp;

    private ActStatus removeStatus = ActStatus.UNACT;

    private String contentLink;

    private int floorNum;

    private String publistDate;

    private int totalRows;

    private String keyWords;

	private double score;//评分
	private long display_order;//排序
	private ForignContentDomain forignContentDomain;//评论的类型


	public void setPublistDate(String publistDate) {
		this.publistDate = publistDate;
	}

	public long getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(long display_order) {
		this.display_order = display_order;
	}

	public String getRootUno() {
        return rootUno;
    }

    public void setRootUno(String rootUno) {
        this.rootUno = rootUno;
    }
    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public long getPartentId() {
        return partentId;
    }

    public void setPartentId(long partentId) {
        this.partentId = partentId;
    }

    public long getRootId() {
        return rootId;
    }

    public void setRootId(long rootId) {
        this.rootId = rootId;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getReplyUno() {
        return replyUno;
    }

    public void setReplyUno(String replyUno) {
        this.replyUno = replyUno;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public String getPublistDate() {
        return DateUtil.parseDate(createTime);
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public ForignContentDomain getForignContentDomain() {
		return forignContentDomain;
	}

	public void setForignContentDomain(ForignContentDomain forignContentDomain) {
		this.forignContentDomain = forignContentDomain;
	}

	public int getDisagreeNum() {
		return disagreeNum;
	}

	public void setDisagreeNum(int disagreeNum) {
		this.disagreeNum = disagreeNum;
	}

	@Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
