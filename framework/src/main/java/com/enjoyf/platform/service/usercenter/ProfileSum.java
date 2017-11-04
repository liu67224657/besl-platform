package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/11
 * Description:
 */
public class ProfileSum implements Serializable {

	private String profileId;
    private int followSum;
    private int fansSum;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getFollowSum() {
        return followSum;
    }

    public void setFollowSum(int followSum) {
        this.followSum = followSum;
    }

    public int getFansSum() {
        return fansSum;
    }

    public void setFansSum(int fansSum) {
        this.fansSum = fansSum;
    }

    @Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
