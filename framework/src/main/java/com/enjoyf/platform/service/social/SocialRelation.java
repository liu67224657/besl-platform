/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:58
 * Description: 将各种相互关系放在一张表中。并把两个人间的相互关系用一条记录来记录，比方说关注和粉丝。
 */
public class SocialRelation implements Serializable {
    //the sequence
    private long relationId;

    //the reletion type, such as 关注(粉丝)，好友等。 (互为相互的动作归为一类)
    private RelationType relationType;

    //
    private Set<Long> categoryIds = new HashSet<Long>();

    //
    private String srcUno;
    private String destUno;
    private String description;

    //relation status.
    private ActStatus srcStatus = ActStatus.ACTED;
    private ActStatus destStatus = ActStatus.UNACT;

    //relation quality
    private int relationRate;

    //
    private Date srcDate;
    private Date destDate;

    //
    private Date srcLastActDate;
    private Date destLastActDate;

    //
    public SocialRelation() {
    }

    public long getRelationId() {
        return relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getSrcUno() {
        return srcUno;
    }

    public void setSrcUno(String srcUno) {
        this.srcUno = srcUno;
    }

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActStatus getSrcStatus() {
        return srcStatus;
    }

    public void setSrcStatus(ActStatus srcStatus) {
        this.srcStatus = srcStatus;
    }

    public ActStatus getDestStatus() {
        return destStatus;
    }

    public void setDestStatus(ActStatus destStatus) {
        this.destStatus = destStatus;
    }

    public int getRelationRate() {
        return relationRate;
    }

    public void setRelationRate(int relationRate) {
        this.relationRate = relationRate;
    }

    public Date getSrcDate() {
        return srcDate;
    }

    public void setSrcDate(Date srcDate) {
        this.srcDate = srcDate;
    }

    public Date getDestDate() {
        return destDate;
    }

    public void setDestDate(Date destDate) {
        this.destDate = destDate;
    }

    public Date getSrcLastActDate() {
        return srcLastActDate;
    }

    public void setSrcLastActDate(Date srcLastActDate) {
        this.srcLastActDate = srcLastActDate;
    }

    public Date getDestLastActDate() {
        return destLastActDate;
    }

    public void setDestLastActDate(Date destLastActDate) {
        this.destLastActDate = destLastActDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
