/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class SocialInviteDetail implements Serializable {
    private long inviteId;
    private String gid;
    private String inviteUno;
    private InviteDomain inviteDomain;
    private String directUno;
    private Date createDate;
    private String createIp;

    public long getInviteId() {
        return inviteId;
    }

    public void setInviteId(long inviteId) {
        this.inviteId = inviteId;
    }

    public InviteDomain getInviteDomain() {
        return inviteDomain;
    }

    public void setInviteDomain(InviteDomain inviteDomain) {
        this.inviteDomain = inviteDomain;
    }

    public String getDirectUno() {
        return directUno;
    }

    public void setDirectUno(String directUno) {
        this.directUno = directUno;
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

    public String getInviteUno() {
        return inviteUno;
    }

    public void setInviteUno(String inviteUno) {
        this.inviteUno = inviteUno;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
