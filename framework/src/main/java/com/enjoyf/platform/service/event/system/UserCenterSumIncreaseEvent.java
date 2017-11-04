/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.usercenter.ModifyTimeJson;
import com.enjoyf.platform.service.usercenter.ProfileSumField;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class UserCenterSumIncreaseEvent extends SystemEvent {
    //
    private String profileId;
    private int likeSum;
    private int likedSum;
    private int likePicSum;
    private int likeGameSum;
    private String playingGame;
    private ModifyTimeJson modifyTimeJson;
    //
    public UserCenterSumIncreaseEvent() {
        super(SystemEventType.USERCENTER_SUM_INCREASE);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getLikeSum() {
        return likeSum;
    }

    public void setLikeSum(int likeSum) {
        this.likeSum = likeSum;
    }

    public int getLikedSum() {
        return likedSum;
    }

    public void setLikedSum(int likedSum) {
        this.likedSum = likedSum;
    }

    public int getLikePicSum() {
        return likePicSum;
    }

    public void setLikePicSum(int likePicSum) {
        this.likePicSum = likePicSum;
    }

    public int getLikeGameSum() {
        return likeGameSum;
    }

    public void setLikeGameSum(int likeGameSum) {
        this.likeGameSum = likeGameSum;
    }

    public String getPlayingGame() {
        return playingGame;
    }

    public void setPlayingGame(String playingGame) {
        this.playingGame = playingGame;
    }

    public ModifyTimeJson getModifyTimeJson() {
        return modifyTimeJson;
    }

    public void setModifyTimeJson(ModifyTimeJson modifyTimeJson) {
        this.modifyTimeJson = modifyTimeJson;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return profileId.hashCode();
    }
}
