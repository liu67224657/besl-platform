package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * <p/>
 * Description:邀请链接的信息
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SocialInviteInfo implements Serializable{
    private long inviteId;
    private String srcUno;
    private InviteDomain inviteDomain;
    private String destId;
    private Date createDate;
    private String createIp;

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

    public InviteDomain getInviteDomain() {
        return inviteDomain;
    }

    public void setInviteDomain(InviteDomain inviteDomain) {
        this.inviteDomain = inviteDomain;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
