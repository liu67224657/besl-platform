package com.enjoyf.platform.service.comment;

import java.io.Serializable;
import java.util.Date;

import com.enjoyf.platform.service.ValidStatus;

/**
 * 图文直播用户权限封装类
 *
 * @author huazhang
 */
public class CommentPermission implements Serializable {

    private static final long serialVersionUID = -2211650136885445111L;
    private String permissionId;//权限
    private String profileId; //用户id
    private CommentPermissionType permissionType;//用户权限
    private ValidStatus status;
    private Date createTime;
    private int createUserId;
    private String nick;//昵称

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public CommentPermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(CommentPermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public ValidStatus getStatus() {
        return status;
    }

    public void setStatus(ValidStatus status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
