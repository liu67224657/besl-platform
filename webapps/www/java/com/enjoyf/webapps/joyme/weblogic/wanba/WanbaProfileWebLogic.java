package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WanbaProfileSum;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.webapps.joyme.dto.Wanba.MyDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaProfileSumDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/14
 */
@Service
public class WanbaProfileWebLogic extends AbstractWanbaWebLogic {

    public WanbaProfileDTO getProfileDTOByProfileId(String profileId) throws ServiceException {
        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
        if (profile == null) {
            return null;
        }

        VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);

        return wanbaProfileDTO(profile, wanbaProfile);
    }

    /**
     * @param tagId tagid
     * @param page  分页参数
     * @throws ServiceException
     */
    public PageRows<WanbaProfileDTO> queryProfileByTagId(long tagId, Pagination page) throws ServiceException {
        PageRows<VerifyProfile> wanbaProfilePageRows = UserCenterServiceSngl.get().queryVerifyProfileByTag(tagId, page);

        Set<String> pIds = new HashSet<String>();
        for (VerifyProfile profile : wanbaProfilePageRows.getRows()) {
            pIds.add(profile.getProfileId());
        }
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pIds);

        List<WanbaProfileDTO> profileDTOList = new ArrayList<WanbaProfileDTO>();
        for (VerifyProfile wanbaProfile : wanbaProfilePageRows.getRows()) {
            Profile profile = profileMap.get(wanbaProfile.getProfileId());
            if (profile == null) {
                continue;
            }
            WanbaProfileDTO dto = wanbaProfileDTO(profile, wanbaProfile);
            if (dto != null) {
                profileDTOList.add(dto);
            }
        }

        PageRows<WanbaProfileDTO> returnObj = new PageRows<WanbaProfileDTO>();
        returnObj.setPage(wanbaProfilePageRows.getPage());
        returnObj.setRows(profileDTOList);
        return returnObj;
    }

    /**
     * 根据questionId 获取用户被邀请状态的状态
     *
     * @param questionId qid
     * @param profiles   查询
     * @return
     * @throws ServiceException
     */
    public List<WanbaProfileDTO> setInviteStatusByQuestion(long questionId, List<WanbaProfileDTO> profiles) throws ServiceException {
        Set<String> pids = new HashSet<String>();
        for (WanbaProfileDTO profile : profiles) {
            pids.add(profile.getPid());
        }
        Set<String> inviteProfiles = AskServiceSngl.get().checkInviteProfiles(pids, questionId);

        for (WanbaProfileDTO profile : profiles) {
            profile.setHasinvited(inviteProfiles.contains(profile.getPid()) ? WanbaProfileDTO.DTO_WANBA_PROFILEID_HASINVITED_STATUS_YES : WanbaProfileDTO.DTO_WANBA_PROFILEID_HASINVITED_STATUS_NO);
        }

        return profiles;
    }

    public ScoreRangeRows<WanbaProfileDTO> queryFollowProfileByQuestionId(long questionId, ScoreRange range) throws ServiceException {
        Set<String> profileIdS = AskServiceSngl.get().queryFollowProfileIds(questionId, range);
        ScoreRangeRows<VerifyProfile> wanbaProfilePageRows = UserCenterServiceSngl.get().queryFollowProfile(profileIdS, range);

        Set<String> pIds = new HashSet<String>();
        for (VerifyProfile profile : wanbaProfilePageRows.getRows()) {
            pIds.add(profile.getProfileId());
        }
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pIds);

        List<WanbaProfileDTO> profileDTOList = new ArrayList<WanbaProfileDTO>();
        for (VerifyProfile wanbaProfile : wanbaProfilePageRows.getRows()) {
            Profile profile = profileMap.get(wanbaProfile.getProfileId());
            if (profile == null) {
                continue;
            }
            WanbaProfileDTO dto = wanbaProfileDTO(profile, wanbaProfile);
            if (dto != null) {
                profileDTOList.add(dto);
            }
        }

        ScoreRangeRows<WanbaProfileDTO> returnObj = new ScoreRangeRows<WanbaProfileDTO>();
        returnObj.setRange(wanbaProfilePageRows.getRange());
        returnObj.setRows(profileDTOList);
        return returnObj;
    }


    public MyDTO getMyDTO(UserPoint userPoint, String pid) throws ServiceException {
        MyDTO returnDTO = new MyDTO();

        VerifyProfile wanbaProfile = UserCenterServiceSngl.get().getVerifyProfileById(pid);
        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
        returnDTO.setProfile(wanbaProfileDTO(profile, wanbaProfile));

        WanbaProfileSum wanbaProfileSum = AskServiceSngl.get().getWanProfileSum(pid);
        long getQuestionPoint = PointServiceSngl.get().getWanbaQuestionPoint(pid); //玩霸通过回答问题获得的积分

        WanbaProfileSumDTO wanbaProfileSumDTO = new WanbaProfileSumDTO();
        if (userPoint != null) {
            wanbaProfileSumDTO.setUserpoint(userPoint.getUserPoint());
            wanbaProfileSumDTO.setAnswerpoint((int) getQuestionPoint);
        }
        if (wanbaProfileSum != null) {
            wanbaProfileSumDTO.setFavoritesum(wanbaProfileSum.getFavoriteSum());
            wanbaProfileSumDTO.setQuestionfollowsum(wanbaProfileSum.getQuestionFollowSum());
            wanbaProfileSumDTO.setAnswersum(wanbaProfileSum.getAnswerSum());
        }
        returnDTO.setProfilesum(wanbaProfileSumDTO);
        return returnDTO;
    }
}
