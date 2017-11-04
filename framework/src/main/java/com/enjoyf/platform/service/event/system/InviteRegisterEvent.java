/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.social.InviteDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">eric liu</a>
 * Create time: 11-8-25 下午8:15
 * Description:邀请注册事件
 */
public class InviteRegisterEvent extends SystemEvent {
    //
    private Long inviteId;
    private String gid;
    private String destUno;
    private String destEmail;
    private Date regDate;
    private String regIp;

    //
    public InviteRegisterEvent() {
        super(SystemEventType.INVITE_REGISTER);
    }

    public Long getInviteId() {
        return this.inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public String getDestUno() {
        return destUno;
    }

    public void setDestUno(String destUno) {
        this.destUno = destUno;
    }

    public String getDestEmail() {
        return destEmail;
    }

    public void setDestEmail(String destEmail) {
        this.destEmail = destEmail;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getRegIp() {
        return regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return this.inviteId.hashCode();
    }
}
