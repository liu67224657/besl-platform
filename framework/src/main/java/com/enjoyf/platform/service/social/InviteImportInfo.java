package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * <p/>
 * Description:邀请信息
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class InviteImportInfo implements Serializable{
    private long inviteId;
    private String srcUno;

    private String destEmail;
    private String destUno;

    private ActStatus invateStatus=ActStatus.UNACT;//unact未邀请成功 acted邀请成功
    private InviteRelationType inviteRelationType;//邀请类型

    private Date createDate;
    private Date lastModifyDate;

    public long getInviteId() {
        return inviteId;
    }

    public void setInviteId(long inviteId) {
        this.inviteId = inviteId;
    }

    public String getSrcUno() {
        return srcUno;
    }

    public void setSrcUno(String srcUno) {
        this.srcUno = srcUno;
    }

    public String getDestEmail() {
        return destEmail;
    }

    public void setDestEmail(String destEmail) {
        this.destEmail = destEmail;
    }

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
    }

    public ActStatus getInvateStatus() {
        return invateStatus;
    }

    public void setInvateStatus(ActStatus invateStatus) {
        this.invateStatus = invateStatus;
    }

    public InviteRelationType getInviteRelationType() {
        return inviteRelationType;
    }

    public void setInviteRelationType(InviteRelationType inviteRelationType) {
        this.inviteRelationType = inviteRelationType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
