/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Liu Hao</a>
 * Create time: 12-5-04 下午5:34
 * Description: 文章交互实体类
 */
public class ContentInteraction implements Serializable {
    //
    private String interactionId;//数据库主键（评论id、喜欢id）
    private String interactionUno; //交互人uno（评论人uno、喜欢人uno）

     //content info
    private String contentId;
    private String contentUno;

    //parent id (ex:reply parent)
    private String parentId;
    private String parentUno;

    //root id(ex:reply root)
    private String rootId;
    private String rootUno;

    //floorNo-->楼号，只有评论才有值
    private long floorNo;
    //回复次数
    private int replyTimes;

    //interaction contnet(ex:replybody)
    private String interactionContent;

    // image
    private ImageContentSet interactionImages;

    // type: image video audio app
    private InteractionContentType interactionContentType = new InteractionContentType();

    //
    private InteractionType interactionType;//交互类型（评论 喜欢）
    private IntetractionDisplayType intetractionDisplayType=new IntetractionDisplayType();//显示类型

    //
    private ActStatus removeStatus = ActStatus.UNACT;

    private ActStatus processAtStatus = ActStatus.ACTED;
    //
    private Date createDate;
    private String createDateStr;
    private String createIp;

    //审核标记
    private ContentReplyAuditStatus auditStatus;

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public String getInteractionContent() {
        return interactionContent;
    }

    public void setInteractionContent(String interactionContent) {
        this.interactionContent = interactionContent;
    }

    public ImageContentSet getInteractionImages() {
        return interactionImages;
    }

    public void setInteractionImages(ImageContentSet interactionImages) {
        this.interactionImages = interactionImages;
    }

    public InteractionContentType getInteractionContentType() {
        return interactionContentType;
    }

    public void setInteractionContentType(InteractionContentType interactionContentType) {
        this.interactionContentType = interactionContentType;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getInteractionUno() {
        return interactionUno;
    }

    public void setInteractionUno(String interactionUno) {
        this.interactionUno = interactionUno;
    }

    public ActStatus getProcessAtStatus() {
        return processAtStatus;
    }

    public void setProcessAtStatus(ActStatus processAtStatus) {
        this.processAtStatus = processAtStatus;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public ContentReplyAuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(ContentReplyAuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public IntetractionDisplayType getIntetractionDisplayType() {
        return intetractionDisplayType;
    }

    public void setIntetractionDisplayType(IntetractionDisplayType intetractionDisplayType) {
        this.intetractionDisplayType = intetractionDisplayType;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getRootUno() {
        return rootUno;
    }

    public void setRootUno(String rootUno) {
        this.rootUno = rootUno;
    }

    public long getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(long floorNo) {
        this.floorNo = floorNo;
    }

    public int getReplyTimes() {
        return replyTimes;
    }

    public void setReplyTimes(int replyTimes) {
        this.replyTimes = replyTimes;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
