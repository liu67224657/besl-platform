package com.enjoyf.webapps.joyme.weblogic.usercenter;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.point.LotteryType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileSum;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.webapps.joyme.dto.usercenter.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
@Service(value = "userCenterWebLogic")
public class UserCenterWebLogic extends AbstractUserCenterWebLogic {

    public String getLotteryType() {
        return LOTTERY_TYPE;
    }

    public GameClientProfileDTO builGameClientDTO(Profile profile, String platform) {
        GameClientProfileDTO returnObject = new GameClientProfileDTO();

        returnObject.setUid(profile.getUid());
        returnObject.setDesc(profile.getDescription());
        returnObject.setIconurl(URLUtils.getJoymeDnUrl(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true)));
        returnObject.setNick(profile.getNick());
        returnObject.setUno(profile.getUno());
        try {
            ProfileSum profileSum = UserCenterServiceSngl.get().getProfileSum(profile.getProfileId());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        returnObject.setMessagenoticetime(getMessagenoticetime(platform));
        return returnObject;
    }


    public Map<String, ProfileDTO> buildProfileDTOByProfileIDs(Set<String> profileIds) throws ServiceException {
        Map<String, ProfileDTO> dtoMap = new HashMap<String, ProfileDTO>();


        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIds);
        Map<String, ProfileSum> profileSumMap = UserCenterServiceSngl.get().queryProfileSumByProfileids(profileIds);
        for (Profile profile : profileMap.values()) {
            ProfileDTO dto = new ProfileDTO();
            dto.setUid(profile.getUid());
            dto.setUno(profile.getUno());
            dto.setDesc(profile.getDescription());
            dto.setNick(profile.getNick());
            dto.setIconurl(URLUtils.getJoymeDnUrl(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true)));
            ProfileSum profileSum = profileSumMap.get(profile.getProfileId());
            dtoMap.put(profile.getProfileId(), dto);
        }

        return dtoMap;
    }

    public long getMessagenoticetime(String platform) {
        try {
            return MessageServiceSngl.get().getMessageNoticeTime(MessageConstants.WANBA_KEY_MESSAGE_NOTICETIME + platform);
        } catch (ServiceException e) {
            return 0L;
        }
    }

    /**
     * @param profileIdList
     * @param flag          是否查询编辑数 true为查询 false为不查询
     * @return
     * @throws ServiceException
     */
    public Map<String, UserinfoDTO> buildUserinfoMap(List<String> profileIdList, boolean flag) throws ServiceException {
        Set<String> profileIdSet = new HashSet<String>();
        for (String profileId : profileIdList) {
            profileIdSet.add(profileId);
        }
        Date date=new Date();
        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
        GAlerter.lan(" profilemap="+(new Date().getTime()-date.getTime()));
        Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);
//        Map<String, Map<String, String>> skinMap = PointServiceSngl.get().queryChooseLottery(profileIdSet);
        Map<String, ProfileSum> profileSumMap = UserCenterServiceSngl.get().queryProfileSumByProfileids(profileIdSet);

        Map<String, UserinfoDTO> userinfoMap = new HashMap<String,UserinfoDTO>();
        for (String profileId : profileIdList) {
            if (profileMap.get(profileId) != null) {
                UserinfoDTO userinfoDTO = buildUserInfoDTO(profileMap.get(profileId), verifyProfileMap.get(profileId), profileSumMap.get(profileId), flag);
                userinfoMap.put(profileId, userinfoDTO);
            }
        }
        return userinfoMap;
    }


    private static final String LOTTERY_TYPE = "lotteryType_";

    /**
     * @param profile       用户信息
     * @param verifyProfile 认证信息
     * @param profileSum    关注数量
     * @param flag          是否查询编辑数  true 为查询 false为不查询
     * @return
     */
    public UserinfoDTO buildUserInfoDTO(Profile profile, VerifyProfile verifyProfile, ProfileSum profileSum, boolean flag) throws ServiceException {
        UserinfoDTO userinfoDTO = new UserinfoDTO();
        userinfoDTO.setProfileId(profile.getProfileId());
        userinfoDTO.setDesc(profile.getDescription());
        userinfoDTO.setIcon(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true));
        userinfoDTO.setNick(profile.getNick());
        userinfoDTO.setUid(profile.getUid());
        userinfoDTO.setUno(profile.getUno());
        userinfoDTO.setSex(profile.getSex());
        userinfoDTO.setProfileId(profile.getProfileId());
        if (verifyProfile != null) {
            userinfoDTO.setVtype(verifyProfile.getVerifyType());
            userinfoDTO.setVdesc(verifyProfile.getDescription());
            userinfoDTO.setVtitle(StringUtil.isEmpty(verifyProfile.getVerifyTitle()) ? "" : verifyProfile.getVerifyTitle());
        }
        if (profileSum != null) {
            userinfoDTO.setFans(profileSum.getFansSum());
            userinfoDTO.setFollows(profileSum.getFollowSum());
        }

//        if (skinChooseMap != null && !skinChooseMap.isEmpty()) {
//            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()))) {
//                userinfoDTO.setHeadskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.HEAD.getCode()));
//            }
//            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()))) {
//                userinfoDTO.setCardskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.BG.getCode()));
//            }
//            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()))) {
//                userinfoDTO.setBubbleskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.CHAT.getCode()));
//            }
//            if (!StringUtil.isEmpty(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()))) {
//                userinfoDTO.setReplyskin(skinChooseMap.get(LOTTERY_TYPE + LotteryType.COMMENT.getCode()));
//            }
//        }
        if (flag) {
            userinfoDTO.setEdits(UserCenterWebUtil.getEditNum(profile.getUid()));
        }
        return userinfoDTO;
    }


    public static UserMWikiDTO getUserMWikiDTO(Long uid) {
        UserMWikiDTO userMWikiDTO = new UserMWikiDTO();
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult httpResult = httpClientManager.post("http://wiki." + WebappConfig.get().DOMAIN + "/home/api.php", new HttpParameter[]{
                new HttpParameter("action", "jusermwiki"),
                new HttpParameter("format", "json"),
                new HttpParameter("userid", uid)
        }, null);
        if (httpResult.getReponseCode() != 200) {
            return userMWikiDTO;
        }
        String result = httpResult.getResult();
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (!jsonObject.containsKey("jusermwiki")) {
            return userMWikiDTO;
        }
        JSONObject mwikiJsonObject = jsonObject.getJSONObject("jusermwiki");
        if (!mwikiJsonObject.containsKey("rs") || !"1".equals(mwikiJsonObject.get("rs").toString())) {
            return userMWikiDTO;
        }

        userMWikiDTO.setCount(!mwikiJsonObject.containsKey("mwcount") ? "" : mwikiJsonObject.get("mwcount").toString());  //管理的WIKI总数
        if (!mwikiJsonObject.containsKey("mwiki")) {
            return userMWikiDTO;
        }
        JSONArray jsonArray = mwikiJsonObject.getJSONArray("mwiki");
        List<MWikiInfoDTO> mWikiInfoDTOs = new ArrayList<MWikiInfoDTO>(); //管理的WIKI列表
        for (int i = 0; i < jsonArray.size(); i++) {
            MWikiInfoDTO mWikiInfoDTO = new MWikiInfoDTO();
            mWikiInfoDTO.setIcon(jsonArray.getJSONObject(i).get("site_icon").toString());
            mWikiInfoDTO.setUrl(jsonArray.getJSONObject(i).get("site_url").toString());
            mWikiInfoDTOs.add(mWikiInfoDTO);
        }
        userMWikiDTO.setmWikiInfo(mWikiInfoDTOs);
        return userMWikiDTO;
    }

    public static void main(String agres[]) {
        UserCenterWebLogic userCenterWebLogic = new UserCenterWebLogic();
        UserMWikiDTO userMWikiDTO = userCenterWebLogic.getUserMWikiDTO(1064942l);
        System.out.println(userMWikiDTO.toString());
    }
}
