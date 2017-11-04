package com.enjoyf.platform.service.profile.socialclient;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-7-17
 * Time: 下午6:48
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileBlog implements Serializable {
	//
	private String uno;

	//
	private String screenName;
	private String domain;

	//
	private String headIcon;
	private String description;

	//
	private String templateId;
	private ActStatus selfSetStatus = ActStatus.UNACT;
	private BlogSelfSetData selfSetData = new BlogSelfSetData();

	private Date createDate;//init日期
	private Date updateDate;//更新日期

	private ProfileBlogHeadIconSet headIconSet;


	//审核标记
	private ProfileAuditStatus auditStatus = ProfileAuditStatus.getByValue(0);       //审核标记
	private Date auditDate;                      //审核时间
	private String auditUserId;                 //审核者

	// 博客限制
	private ProfileActiveStatus activeStatus = ProfileActiveStatus.INIT;            //限制类型
	private Date inactiveTillDate;                                                         //到期时间

	//
	private ProfileDomain profileDomain = ProfileDomain.DEFAULT;

	private long userid;


	private String phoneNum;
	private ActStatus phoneVerifyStatus = ActStatus.UNACT;
	private Date phoneBindDate;

	private String playingGames;
	private String backgroundPic;
	//
	public String getUno() {
		return uno;
	}

	public void setUno(String uno) {
		this.uno = uno;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public ActStatus getSelfSetStatus() {
		return selfSetStatus;
	}

	public void setSelfSetStatus(ActStatus selfSetStatus) {
		this.selfSetStatus = selfSetStatus;
	}

	public BlogSelfSetData getSelfSetData() {
		return selfSetData;
	}

	public void setSelfSetData(BlogSelfSetData selfSetData) {
		this.selfSetData = selfSetData;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public ProfileBlogHeadIconSet getHeadIconSet() {
		return headIconSet;
	}

	public void setHeadIconSet(ProfileBlogHeadIconSet headIconSet) {
		this.headIconSet = headIconSet;
	}

	public ProfileAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(ProfileAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public ProfileDomain getProfileDomain() {
		return profileDomain;
	}

	public void setProfileDomain(ProfileDomain profileDomain) {
		this.profileDomain = profileDomain;
	}

	public Date getInactiveTillDate() {
		return inactiveTillDate;
	}

	public void setInactiveTillDate(Date inactiveTillDate) {
		this.inactiveTillDate = inactiveTillDate;
	}

	public ProfileActiveStatus getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(ProfileActiveStatus activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getTableNo() {
		return Math.abs(hashCode()) % 100;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public ActStatus getPhoneVerifyStatus() {
		return phoneVerifyStatus;
	}

	public void setPhoneVerifyStatus(ActStatus phoneVerifyStatus) {
		this.phoneVerifyStatus = phoneVerifyStatus;
	}

	public Date getPhoneBindDate() {
		return phoneBindDate;
	}

	public void setPhoneBindDate(Date phoneBindDate) {
		this.phoneBindDate = phoneBindDate;
	}

	public String getPlayingGames() {
		return playingGames;
	}

	public void setPlayingGames(String playingGames) {
		this.playingGames = playingGames;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	//
	@Override
	public int hashCode() {
		return uno.hashCode();
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
