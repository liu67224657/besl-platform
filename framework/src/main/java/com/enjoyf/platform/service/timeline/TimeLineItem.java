/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.CommentType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:47
 * Description: this a composite object which includes many messages.
 */
public class TimeLineItem implements Serializable {
    //sequence id
    private Long tlId;

    //owner uno
    private String ownUno;

    //the time line domain.
    private TimeLineDomain domain;

    //the timeline content type. content or reply.
    private TimeLineContentType type = TimeLineContentType.CONTENT;

    //the direct uno and id,
    // such the content id or the reply id.
    private String directUno;
    private String directId;

    //
    private String parentUno;
    private String parentId;


    //the relation uno and id,
    // such the original content id or the content id.
    private String relationUno;
    private String relationId;

    //过滤类型
    private TimeLineFilterType filterType;
    private ActStatus processAtStatus = ActStatus.ACTED;
    //relation
    private SocialRelation relation;

    // private String
    private String description;

    //insert into the time line date.
    private Date createDate;

    //read flag
    private ActStatus removeStatus = ActStatus.UNACT;
    private Date removeDate;

    private ItemSpreadType spreadType = new ItemSpreadType().has(ItemSpreadType.DEF);
    private PageRows<TimeLineItemDetail> favDtail=new PageRows<TimeLineItemDetail>();
    private int favSum=0;

    //constructors;
    public TimeLineItem() {
    }

    public TimeLineItem(String ownUno) {
        this.ownUno = ownUno;
    }

    //Comment ID and commentator's uno
    private String rootId;
    private String rootUno;
    //The type of article source,localsource and foreignsource
    private CommentType source = CommentType.LOCALSOURCE;

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

    public CommentType getSource() {
        return source;
    }

    public void setSource(CommentType source) {
        this.source = source;
    }

    public Long getTlId() {
        return tlId;
    }

    public void setTlId(Long tlId) {
        this.tlId = tlId;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public TimeLineDomain getDomain() {
        return domain;
    }

    public void setDomain(TimeLineDomain domain) {
        this.domain = domain;
    }

    public TimeLineContentType getType() {
        return type;
    }

    public void setType(TimeLineContentType type) {
        this.type = type;
    }

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
    }

    public String getDirectId() {
        return directId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public String getRelationUno() {
        return relationUno;
    }

    public void setRelationUno(String relationUno) {
        this.relationUno = relationUno;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    public TimeLineFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(TimeLineFilterType filterType) {
        this.filterType = filterType;
    }

    public ActStatus getProcessAtStatus() {
        return processAtStatus;
    }

    public void setProcessAtStatus(ActStatus processAtStatus) {
        this.processAtStatus = processAtStatus;
    }

    public SocialRelation getRelation() {
        return relation;
    }

    public void setRelation(SocialRelation relation) {
        this.relation = relation;
    }

    public ItemSpreadType getSpreadType() {
        return spreadType;
    }

    public void setSpreadType(ItemSpreadType spreadType) {
        this.spreadType = spreadType;
    }

    public PageRows<TimeLineItemDetail> getFavDtail() {
        return favDtail;
    }

    public void setFavDtail(PageRows<TimeLineItemDetail> favDtail) {
        this.favDtail = favDtail;
    }

    public int getFavSum() {
        return favSum;
    }

    public void setFavSum(int favSum) {
        this.favSum = favSum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
