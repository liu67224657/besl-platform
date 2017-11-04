/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.CommentType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.timeline.TimeLineContentType;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class TimeLineInsertEvent extends SystemEvent {
    //
    private String ownUno;

    //the time line domain.
    private TimeLineDomain domain;

    //the timeline content type. content or reply.
    private TimeLineContentType type = TimeLineContentType.CONTENT;

    private TimeLineFilterType filterType = new TimeLineFilterType();
    private ActStatus processAtStatus = ActStatus.ACTED;
    private SocialRelation relation;

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


    private Date timeLineDate;

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

    //
    public TimeLineInsertEvent() {
        super(SystemEventType.TIMELINE_INSERT);
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

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
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

    public Date getTimeLineDate() {
        return timeLineDate;
    }

    public void setTimeLineDate(Date timeLineDate) {
        this.timeLineDate = timeLineDate;
    }

    public TimeLineContentType getType() {
        return type;
    }

    public void setType(TimeLineContentType type) {
        this.type = type;
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

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return ownUno.hashCode();
    }
}
