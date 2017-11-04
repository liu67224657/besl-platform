package com.enjoyf.webapps.joyme.weblogic.usercenter;

import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialServiceException;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.usercenter.PersonInfoDTO;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service(value = "userRelationWebLogic")
public class UserRelationWebLogic extends AbstractUserCenterWebLogic {

    private static Logger logger = LoggerFactory.getLogger(UserRelationWebLogic.class);

    public boolean unfollow(String srcUno, String destUno, String modifyIp) throws ServiceException {
        return SocialServiceSngl.get().removeUserRelation(srcUno, destUno, ObjectRelationType.WIKI_PROFILE, modifyIp);
    }

    public UserRelation follow(String srcUno, String destUno, String destDemo, String userIp) throws ServiceException {
        logger.debug("add focus srcUno." + srcUno + " ,destUno:" + destUno + " ,destDemo" + destDemo);

        if (srcUno.equals(destUno)) {
            throw new ServiceException(SocialServiceException.FOCUS_IS_SAME_USER, "focus is same user.uno:" + srcUno);
        }


        //建立和被关注人关系
        UserRelation srcRelation = new UserRelation();

        srcRelation.setSrcProfileid(srcUno);
        srcRelation.setModifyTime(new Timestamp(new Date().getTime()));
        srcRelation.setDestProfileid(destUno);
        srcRelation.setExtstring(destDemo);
        srcRelation.setDestStatus(RelationStatus.UNFOCUS);
        srcRelation.setSrcStatus(RelationStatus.FOCUS);
        srcRelation.setRelationType(ObjectRelationType.WIKI_PROFILE);
        srcRelation.setProfilekey("www");
        srcRelation.setModifyIp(userIp);

        return SocialServiceSngl.get().buildUserRelation(srcRelation);
    }

    public PageRows<PersonInfoDTO> queryFollowProfileid(String srcProfileId, ObjectRelationType relationType, Pagination page, String sessionPid) throws ServiceException {
        PageRows<String> pageRows = SocialServiceSngl.get().queryFollowProfileid(srcProfileId, relationType, page);
        Set<String> focusProfileidSet = new HashSet<String>(pageRows.getRows());

        Map<String, Profile> proflieMap = UserCenterServiceSngl.get().queryProfiles(focusProfileidSet);
        Set<String> followStatus = new HashSet<String>();
        Set<String> fansStatus = new HashSet<String>();
        if (!StringUtil.isEmpty(sessionPid)) {
            followStatus = SocialServiceSngl.get().checkFollowStatus(sessionPid, pageRows.getRows());
            fansStatus = SocialServiceSngl.get().checkFansStatus(sessionPid, pageRows.getRows());
        }


        PageRows<PersonInfoDTO> returnObj = new PageRows<PersonInfoDTO>();
        List<PersonInfoDTO> returnList = new ArrayList<PersonInfoDTO>();
        for (String profileId : pageRows.getRows()) {
            Profile profile = proflieMap.get(profileId);
            if (profile != null) {
                returnList.add(buildPersonInfo(profile, followStatus, fansStatus));
            }
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(returnList);
        return returnObj;
    }

    public PageRows<UserinfoDTO> queryFollowUserinfoProfileid(String profileId, ObjectRelationType relationType, Pagination page, String sessionPid, boolean flag) throws ServiceException {
        PageRows<String> pageRows = SocialServiceSngl.get().queryFollowProfileid(profileId, relationType, page);
        Set<String> fansProfileidSet = new HashSet<String>(pageRows.getRows());

        Map<String, Profile> proflieMap = UserCenterServiceSngl.get().queryProfiles(fansProfileidSet);
        //用户所选皮肤查询
        Map<String, Map<String, String>> skinMap = PointServiceSngl.get().queryChooseLottery(fansProfileidSet);
        //认证信息查询
        Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(fansProfileidSet);
        Map<String, ProfileSum> profileSumMap = UserCenterServiceSngl.get().queryProfileSumByProfileids(fansProfileidSet);

        Set<String> followStatus = new HashSet<String>();
        Set<String> fansStatus = new HashSet<String>();
        if (!StringUtil.isEmpty(sessionPid)) {
            followStatus = SocialServiceSngl.get().checkFollowStatus(sessionPid, pageRows.getRows());
            fansStatus = SocialServiceSngl.get().checkFansStatus(sessionPid, pageRows.getRows());
        }

        PageRows<UserinfoDTO> returnObj = new PageRows<UserinfoDTO>();
        List<UserinfoDTO> returnList = new ArrayList<UserinfoDTO>();
        for (String profileid : pageRows.getRows()) {
            Profile profile = proflieMap.get(profileid);
            VerifyProfile verifyProfile = verifyProfileMap.get(profileid);
            Map<String, String> skinChooseMap = skinMap.get(profileid);
            ProfileSum profileSum = profileSumMap.get(profileid);
            if (profile != null) {
                returnList.add(buildUsrInfo(profile, followStatus, fansStatus, skinChooseMap, verifyProfile, sessionPid, profileSum, flag));
            }
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(returnList);
        return returnObj;
    }


    public PageRows<PersonInfoDTO> queryFansProfileid(String profileid, ObjectRelationType relationType, Pagination pagination, String sessionPid) throws ServiceException {
        PageRows<String> pageRows = SocialServiceSngl.get().queryFansProfileid(profileid, relationType, pagination);
        Set<String> fansProfileidSet = new HashSet<String>(pageRows.getRows());

        Map<String, Profile> proflieMap = UserCenterServiceSngl.get().queryProfiles(fansProfileidSet);
        Set<String> followStatus = new HashSet<String>();
        Set<String> fansStatus = new HashSet<String>();
        if (!StringUtil.isEmpty(sessionPid)) {
            followStatus = SocialServiceSngl.get().checkFollowStatus(sessionPid, pageRows.getRows());
            fansStatus = SocialServiceSngl.get().checkFansStatus(sessionPid, pageRows.getRows());
        }


        PageRows<PersonInfoDTO> returnObj = new PageRows<PersonInfoDTO>();
        List<PersonInfoDTO> returnList = new ArrayList<PersonInfoDTO>();
        for (String profileId : pageRows.getRows()) {
            Profile profile = proflieMap.get(profileId);
            if (profile != null) {
                returnList.add(buildPersonInfo(profile, followStatus, fansStatus));
            }
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(returnList);
        return returnObj;
    }

    public PageRows<UserinfoDTO> queryFansUserinfoProfileid(String profileId, ObjectRelationType relationType, Pagination pagination, String sessionPid, boolean flag) throws ServiceException {
        PageRows<String> pageRows = SocialServiceSngl.get().queryFansProfileid(profileId, relationType, pagination);
        Set<String> fansProfileidSet = new HashSet<String>(pageRows.getRows());

        Map<String, Profile> proflieMap = UserCenterServiceSngl.get().queryProfiles(fansProfileidSet);
        //用户所选皮肤查询
        Map<String, Map<String, String>> skinMap = PointServiceSngl.get().queryChooseLottery(fansProfileidSet);
        //认证信息查询
        Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(fansProfileidSet);

        Map<String, ProfileSum> profileSumMap = UserCenterServiceSngl.get().queryProfileSumByProfileids(fansProfileidSet);

        Set<String> followStatus = new HashSet<String>();
        Set<String> fansStatus = new HashSet<String>();
        if (!StringUtil.isEmpty(sessionPid)) {
            followStatus = SocialServiceSngl.get().checkFollowStatus(sessionPid, pageRows.getRows());
            fansStatus = SocialServiceSngl.get().checkFansStatus(sessionPid, pageRows.getRows());
        }


        PageRows<UserinfoDTO> returnObj = new PageRows<UserinfoDTO>();
        List<UserinfoDTO> returnList = new ArrayList<UserinfoDTO>();
        for (String profileid : pageRows.getRows()) {
            Profile profile = proflieMap.get(profileid);
            VerifyProfile verifyProfile = verifyProfileMap.get(profileid);
            Map<String, String> skinChooseMap = skinMap.get(profileid);
            ProfileSum profileSum = profileSumMap.get(profileid);
            if (profile != null) {
                returnList.add(buildUsrInfo(profile, followStatus, fansStatus, skinChooseMap, verifyProfile, sessionPid, profileSum, flag));
            }
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(returnList);
        return returnObj;
    }

    public UserinfoDTO getUser(String profileId) throws ServiceException {
        Profile profile = null;
        UserinfoDTO userinfoDTO = new UserinfoDTO();
        try {
            profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if (profile != null) {
            Map<String, String> skinChooseMap = PointServiceSngl.get().getChooseLottery(profileId);
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profileId);
            VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(profileId);
            return buildUserDto(profile, skinChooseMap, profileSum, verifyProfile);
        }
        return userinfoDTO;
    }


}
