package com.enjoyf.webapps.joyme.weblogic.usercenter;

import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.point.LotteryType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.joyme.dto.usercenter.PersonInfoDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2017/1/3
 */
public abstract class AbstractUserCenterWebLogic {
    private static final String LOTTERY_TYPE = "lotteryType_";

    public String getLotteryType() {
        return LOTTERY_TYPE;
    }

    /**
     * @param profile         用户信息
     * @param followProfileId 关注的PID
     * @param fansProfileId   粉丝的PID
     * @param skinChooseMap   选择的皮肤
     * @param verifyProfile   认证信息
     * @param sessionPid      本人的PID
     * @param profileSum      关注信息
     * @param flag            是否查询编辑数 true 查询 false不查询
     * @return
     * @throws ServiceException
     */
    protected UserinfoDTO buildUsrInfo(Profile profile, Set<String> followProfileId, Set<String> fansProfileId, Map<String, String> skinChooseMap, VerifyProfile verifyProfile, String sessionPid, ProfileSum profileSum, boolean flag) throws ServiceException {
        UserinfoDTO userInfoDTO = new UserinfoDTO();
        userInfoDTO.setProfileId(profile.getProfileId());
        userInfoDTO.setDesc(profile.getDescription());
        userInfoDTO.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
        userInfoDTO.setNick(profile.getNick());
        userInfoDTO.setUid(profile.getUid());
        userInfoDTO.setUno(profile.getUno());
        userInfoDTO.setFollows(profileSum == null ? 0 : profileSum.getFollowSum());
        userInfoDTO.setFans(profileSum == null ? 0 : profileSum.getFansSum());

        userInfoDTO.setSex(profile.getSex());
//        userInfoDTO.setWorship(String.valueOf(userPoint.getWorshipNum()));
//        userInfoDTO.setPrestige(userPoint.getPrestige());
        if (flag) {
            userInfoDTO.setEdits(UserCenterWebUtil.getEditNum(profile.getUid()));
        }
        if (skinChooseMap != null && !skinChooseMap.isEmpty()) {
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                userInfoDTO.setHeadskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                userInfoDTO.setCardskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                userInfoDTO.setBubbleskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                userInfoDTO.setReplyskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
            }
        }

        if (verifyProfile != null) {    //认证
            userInfoDTO.setVtype(verifyProfile.getVerifyType());
            userInfoDTO.setVdesc(verifyProfile.getDescription());
            userInfoDTO.setVtitle(StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());
        }
        if (followProfileId.contains(profile.getProfileId()) && fansProfileId.contains(profile.getProfileId())) {
            userInfoDTO.setFollowStatus(String.valueOf(RelationStatus.EACH_FOCUS.getCode()));
        } else if (followProfileId.contains(profile.getProfileId())) {
            userInfoDTO.setFollowStatus(String.valueOf(RelationStatus.FOCUS.getCode()));
        } else {
            if (profile.getProfileId().equals(sessionPid))
                userInfoDTO.setFollowStatus("");
            else
                userInfoDTO.setFollowStatus(String.valueOf(RelationStatus.UNFOCUS.getCode()));
        }
        return userInfoDTO;
    }

    // simpilDTO

    protected PersonInfoDTO buildPersonInfo(Profile profile, Set<String> followProfileId, Set<String> fansProfileId) {
        PersonInfoDTO personInfo = new PersonInfoDTO();
        personInfo.setProfileId(profile.getProfileId());
        personInfo.setDesc(profile.getDescription());
        personInfo.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
        personInfo.setNick(profile.getNick());
        personInfo.setUid(profile.getUid());
        personInfo.setUno(profile.getUno());

        if (followProfileId.contains(profile.getProfileId()) && fansProfileId.contains(profile.getProfileId())) {
            personInfo.setFollowStatus(String.valueOf(RelationStatus.EACH_FOCUS.getCode()));
        } else if (followProfileId.contains(profile.getProfileId())) {
            personInfo.setFollowStatus(String.valueOf(RelationStatus.FOCUS.getCode()));
        } else {
            personInfo.setFollowStatus(String.valueOf(RelationStatus.UNFOCUS.getCode()));
        }
        return personInfo;
    }

    public String headerSkin(String profileId) throws ServiceException {
        String skin = "";
        Map<String, String> skinChooseMap = PointServiceSngl.get().getChooseLottery(profileId);
        if (skinChooseMap != null && !skinChooseMap.isEmpty()) {
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                skin = skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode());
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                skin = skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode());
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                skin = skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode());
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                skin = skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode());
            }
        }
        return skin;
    }

    protected UserinfoDTO buildUserDto(Profile profile, Map<String, String> skinChooseMap, ProfileSum proileSum, VerifyProfile verifyProfile) {
        UserinfoDTO userInfoDTO = new UserinfoDTO();
        userInfoDTO.setProfileId(profile.getProfileId());
        userInfoDTO.setDesc(profile.getDescription());
        userInfoDTO.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
        userInfoDTO.setNick(profile.getNick());
        userInfoDTO.setUid(profile.getUid());
        userInfoDTO.setUno(profile.getUno());

        userInfoDTO.setFollows(proileSum.getFollowSum() < 0 ? 0 : proileSum.getFollowSum());
        userInfoDTO.setFans(proileSum.getFansSum() < 0 ? 0 : proileSum.getFansSum());

        userInfoDTO.setSex(profile.getSex());
//        userInfoDTO.setWorship(String.valueOf(userPoint.getWorshipNum()));
//        userInfoDTO.setPrestige(userPoint.getPrestige());
        userInfoDTO.setEdits(UserCenterWebUtil.getEditNum(profile.getUid()));

        if (skinChooseMap != null && !skinChooseMap.isEmpty()) {
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
                userInfoDTO.setHeadskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
                userInfoDTO.setCardskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
                userInfoDTO.setBubbleskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
            }
            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
                userInfoDTO.setReplyskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
            }
        }

        if (verifyProfile != null) {    //认证
            userInfoDTO.setVtype(verifyProfile.getVerifyType());
            userInfoDTO.setVdesc(verifyProfile.getDescription());
            userInfoDTO.setVtitle(StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());
        }

        return userInfoDTO;
    }

}
