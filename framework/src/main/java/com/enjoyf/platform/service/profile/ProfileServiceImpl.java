/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a> ,zx
 * Create time: 11-8-21 下午4:18
 * Description:         没有UNO？？？？
 */
class ProfileServiceImpl implements ProfileService {
    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    public ProfileServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ProfileServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
        numOfPartitions = EnvConfig.get().getServicePartitionNum(ProfileConstants.SERVICE_SECTION);
    }

    @Override
    public Profile getProfileByUno(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_GET_BY_UNO, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (Profile) rp.readSerializable();
    }

    @Override
    public List<Profile> queryProfilesByUnos(Set<String> unos) throws ServiceException {
        List<Profile> returnValue = new ArrayList<Profile>();

        //map
        Map<Integer, Set<String>> unosMap = new HashMap<Integer, Set<String>>();
        for (String u : unos) {
            Integer idx = Math.abs(u.hashCode()) % numOfPartitions;

            Set<String> us = unosMap.get(idx);
            if (us == null) {
                us = new HashSet<String>();
                unosMap.put(idx, us);
            }

            us.add(u);
        }

        //reduce.
        for (Map.Entry<Integer, Set<String>> entry : unosMap.entrySet()) {
            returnValue.addAll(queryProfileByUnos(entry.getKey(), entry.getValue()));
        }

        return returnValue;
    }

    @Override
    public Map<String, Profile> queryProfilesByUnosMap(Set<String> unos) throws ServiceException {
        Map<String, Profile> returnValue = new HashMap<String, Profile>();

        //map
        Map<Integer, Set<String>> unosMap = new HashMap<Integer, Set<String>>();
        for (String u : unos) {
            Integer idx = Math.abs(u.hashCode()) % numOfPartitions;

            Set<String> us = unosMap.get(idx);
            if (us == null) {
                us = new HashSet<String>();
                unosMap.put(idx, us);
            }

            us.add(u);
        }

        //reduce.
        for (Map.Entry<Integer, Set<String>> entry : unosMap.entrySet()) {
            List<Profile> list = queryProfileByUnos(entry.getKey(), entry.getValue());

            for (Profile p : list) {
                if (p != null && p.getBlog() != null)
                    returnValue.put(p.getBlog().getUno(), p);
            }
        }

        return returnValue;
    }

    private List<Profile> queryProfileByUnos(Integer idx, Set<String> unos) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) unos);

        Request req = new Request(ProfileConstants.PROFILE_QUERY_BY_UNOS, wp);
        req.setPartition(idx);

        RPacket rp = reqProcessor.process(req);

        return (List<Profile>) rp.readSerializable();
    }

    @Override
    public ProfileBlog getProfileBlogByUno(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_GET_BY_UNO, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileBlog) rp.readSerializable();
    }

    @Override
    public ProfileBlog getProfileBlogByDomain(String domain) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(domain);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_GET_BY_DOMAIN, wp);

        RPacket rp = reqProcessor.process(req);

        return (ProfileBlog) rp.readSerializable();
    }

    @Override
    public ProfileBlog getProfileBlogByScreenName(String screenName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(screenName);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_GET_BY_SCREENNAME, wp);

        RPacket rp = reqProcessor.process(req);

        return (ProfileBlog) rp.readSerializable();
    }

    @Override
    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(screenName);

        Request req = new Request(ProfileConstants.PROFILE_BLOGS_QUERY_BY_LIKE_SCREENNAME, wp);

        RPacket rp = reqProcessor.process(req);

        return (Map<String, ProfileBlog>) rp.readSerializable();
    }

    @Override
    public Map<String, ProfileBlog> queryProfileBlogByScreenNamesMap(Set<String> screenNames) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) screenNames);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_QUERY_BY_SCREENNAME_SET, wp);

        RPacket rp = reqProcessor.process(req);

        return (Map<String, ProfileBlog>) rp.readSerializable();
    }

    @Override
    public PageRows<Profile> searchProfileBlogs(String keyWord, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(keyWord);
        wp.writeSerializable(page);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_SEARCH, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Profile>) rp.readSerializable();
    }

    @Override
    public PageRows<Profile> queryProfileBlogsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(startDate);
        wp.writeSerializable(endDate);
        wp.writeSerializable(page);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_QUERY_BY_DATE_STEP, wp);

        RPacket rp = reqProcessor.process(req);

        return (PageRows<Profile>) rp.readSerializable();
    }

    @Override
    public ProfileBlog createProfileBlog(ProfileBlog profileBlog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profileBlog);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_CREATE, wp);
        req.setPartition(Math.abs(profileBlog.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileBlog) rp.readSerializable();
    }

//    @Override
//    public ProfileBlog createProfileBlogGenDomain(ProfileBlog profileBlog) throws ServiceException {
//        WPacket wp = new WPacket();
//        wp.writeSerializable(profileBlog);
//
//        Request req = new Request(ProfileConstants.PROFILE_BLOG_CREATE_GENDOMAIN, wp);
//        req.setPartition(Math.abs(profileBlog.getUno().hashCode()) % numOfPartitions);
//
//        RPacket rp = reqProcessor.process(req);
//
//        return (ProfileBlog) rp.readSerializable();
//    }

    @Override
    public boolean updateProfileBlog(ProfileBlog profileBlog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profileBlog);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_UPDATE, wp);
        req.setPartition(Math.abs(profileBlog.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean updateProfileBlog(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_UPDATE_BY_MAP, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }


    @Override
    public boolean updateProfileBlog(String uno, UpdateExpress updateExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable(updateExpress);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_UPDATE_BY_EXPRESS, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ProfileBlog queryProfileblogByPhoneNum(String phone, ActStatus actStatus) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(phone);
        wp.writeSerializable(actStatus);

        Request req = new Request(ProfileConstants.PROFILE_BLOG_QUERY_BY_PHONENUM_STATUS, wp);
        req.setPartition(Math.abs(phone.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileBlog) rp.readSerializable();
    }

    @Override
    public ProfileDetail getProfileDetail(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_DETAIL_GET, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileDetail) rp.readSerializable();
    }

    @Override
    public ProfileDetail createProfileDetail(ProfileDetail profileDetail) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profileDetail);

        Request req = new Request(ProfileConstants.PROFILE_DETAIL_CREATE, wp);
        req.setPartition(Math.abs(profileDetail.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileDetail) rp.readSerializable();
    }

    @Override
    public boolean updateProfileDetail(ProfileDetail profileDetail) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profileDetail);

        Request req = new Request(ProfileConstants.PROFILE_DETAIL_UPDATE, wp);
        req.setPartition(Math.abs(profileDetail.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean updateProfileDetail(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ProfileConstants.PROFILE_DETAIL_UPDATE_BY_MAP, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ProfileSetting getProfileSetting(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_SETTING_GET, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileSetting) rp.readSerializable();
    }

    @Override
    public ProfileSetting saveProfileSetting(ProfileSetting setting) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(setting);

        Request req = new Request(ProfileConstants.PROFILE_SETTING_CREATE, wp);
        req.setPartition(Math.abs(setting.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileSetting) rp.readSerializable();
    }

    @Override
    public boolean updateProfileSetting(ProfileSetting setting) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(setting);

        Request req = new Request(ProfileConstants.PROFILE_SETTING_UPDATE, wp);
        req.setPartition(Math.abs(setting.getUno().hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean updateProfileSetting(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ProfileConstants.PROFILE_SETTING_UPDATE_BY_MAP, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public ProfileSum getProfileSum(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_SUM_GET, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ProfileSum) rp.readSerializable();
    }

    @Override
    public boolean updateProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ProfileConstants.PROFILE_SUM_UPDATE_BY_MAP, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean increaseProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ProfileConstants.PROFILE_SUM_INCREASE, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public List<ProfileExperience> createProfileExperience(String uno, List<ProfileExperience> list) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable((Serializable) list);

        Request req = new Request(ProfileConstants.PROFILE_EXPERIENCE_CREATE, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (List<ProfileExperience>) rp.readSerializable();
    }

    @Override
    public boolean removeProfileExperience(String uno, ProfileExperienceType experienceType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeSerializable(experienceType);

        Request req = new Request(ProfileConstants.PROFILE_EXPERIENCE_REMOVE, wp);
        req.setPartition(Math.abs(uno.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    public List<ProfileExperience> queryProfileExperienceByUno(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);

        Request req = new Request(ProfileConstants.PROFILE_EXPERIENCE_QUERY, wp);

        RPacket rp = reqProcessor.process(req);

        return (List<ProfileExperience>) rp.readSerializable();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);

        Request req = new Request(ProfileConstants.RECIEVE_EVENT, wp);
        req.setPartition(Math.abs(event.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

//    // tools ops
//    public PageRows<ViewAccountProfile> queryViewAccountProfileBlogListByAccount(ProfileQueryParam param, Pagination page) throws ServiceException{
//        WPacket wp = new WPacket();
//
//        wp.writeSerializable(param);
//        wp.writeSerializable(page);
//
//        Request req = new Request(ProfileConstants.VIEW_ACCOUNT_PROFILE_BLOG_BY_ACCOUNT_QUERY_BY_PARAM, wp);
//
//        RPacket rp = reqProcessor.process(req);
//
//        return (PageRows<ViewAccountProfile>)rp.readSerializable();
//    }
//     public PageRows<ViewAccountProfile> queryViewAccountProfileBlogListByBlog(ProfileQueryParam param, Pagination page) throws ServiceException{
//        WPacket wp = new WPacket();
//
//        wp.writeSerializable(param);
//        wp.writeSerializable(page);
//
//        Request req = new Request(ProfileConstants.VIEW_ACCOUNT_PROFILE_BLOG_BY_BLOG_QUERY_BY_PARAM, wp);
//
//        RPacket rp = reqProcessor.process(req);
//
//        return (PageRows<ViewAccountProfile>)rp.readSerializable();
//    }

    @Override
    public boolean modifyBlog(String profileUno, UpdateExpress updateExpress, QueryExpress queryExpress, boolean isQueued) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(profileUno);
        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);
        wPacket.writeBooleanNx(isQueued);

        Request req = new Request(ProfileConstants.COMMON_UPDATE, wPacket);

        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public PageRows<ProfileBlog> query(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(page);

        Request request = new Request(ProfileConstants.COMMON_QUERY, wPacket);

        RPacket rPacket = reqProcessor.process(request);

        return (PageRows<ProfileBlog>) rPacket.readSerializable();
    }

    @Override
    public ProfileMobileDevice increaseProfileMobileDevice(ProfileMobileDevice entry) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(entry);

        Request request = new Request(ProfileConstants.PROFILE_MOBILE_DEVICE_CREATE, wPacket);

        RPacket rPacket = reqProcessor.process(request);

        return (ProfileMobileDevice) rPacket.readSerializable();

    }

    @Override
    public ProfileMobileDevice getProfileMobileDevice(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request request = new Request(ProfileConstants.PROFILE_MOBILE_DEVICE_GET, wPacket);

        RPacket rPacket = reqProcessor.process(request);

        return (ProfileMobileDevice) rPacket.readSerializable();
    }

    @Override
    public PageRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(page);

        Request request = new Request(ProfileConstants.PROFILE_MOBILE_DEVICE_QUERY_BY_PAGE, wPacket);

        RPacket rPacket = reqProcessor.process(request);

        return (PageRows<ProfileMobileDevice>) rPacket.readSerializable();
    }

    @Override
    public RangeRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Rangination range) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(range);

        Request request = new Request(ProfileConstants.PROFILE_MOBILE_DEVICE_QUERY_BY_RANGE, wPacket);

        RPacket rPacket = reqProcessor.process(request);

        return (RangeRows<ProfileMobileDevice>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyProfileMobileDevice(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ProfileConstants.PROFILE_MOBILE_DEVICE_UPDATE, wPacket);

        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public ProfileDeveloper createProfileDeveloper(ProfileDeveloper profileDeveloper) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(profileDeveloper);
        Request req = new Request(ProfileConstants.CREATE_PROFILE_NEWRELEASE, wPacket);

        RPacket rPacket = reqProcessor.process(req);
        return (ProfileDeveloper) rPacket.readSerializable();
    }

    @Override
    public ProfileDeveloper getProfileDeveloper(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        Request req = new Request(ProfileConstants.GET_PROFILE_NEWRELEASE, wPacket);

        RPacket rPacket = reqProcessor.process(req);
        return (ProfileDeveloper) rPacket.readSerializable();
    }

    @Override
    public List<ProfileDeveloper> queryProfileDeveloper(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        Request req = new Request(ProfileConstants.QUERY_PROFILE_NEWRELEASE, wPacket);

        RPacket rPacket = reqProcessor.process(req);
        return (List<ProfileDeveloper>) rPacket.readSerializable();
    }

    @Override
    public PageRows<ProfileDeveloper> queryProfileDeveloperByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(pagination);
        Request req = new Request(ProfileConstants.QUERY_PROFILE_NEWRELEASE_BY_PAGE, wPacket);

        RPacket rPacket = reqProcessor.process(req);
        return (PageRows<ProfileDeveloper>) rPacket.readSerializable();
    }

    @Override
    public boolean modifyProfileDeveloper(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();
        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);
        Request req = new Request(ProfileConstants.MODIFY_PROFILE_NEWRELEASE, wPacket);

        RPacket rPacket = reqProcessor.process(req);
        return rPacket.readBooleanNx();
    }

    @Override
    public String saveMobileCode(String uno, String code) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        wp.writeString(code);
        Request req = new Request(ProfileConstants.SAVE_CODE_FOR_MEMCACHED, wp);

        RPacket rp = reqProcessor.process(req);
        return rp.readStringUTF();
    }

    @Override
    public String getMobileCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        Request req = new Request(ProfileConstants.GET_CODE_FOR_MEMCACHED, wp);

        RPacket rp = reqProcessor.process(req);
        return (String) rp.readSerializable();
    }

    @Override
    public boolean removeMobileCode(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(uno);
        Request req = new Request(ProfileConstants.DELETE_CODE_FOR_MEMCACHED, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public ProfilePlayingGames createProfilePlayingGames(ProfilePlayingGames profilePlayingGames) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profilePlayingGames);
        Request req = new Request(ProfileConstants.CREATE_PROFILE_PLAYING_GAMES, wp);
        RPacket rp = reqProcessor.process(req);
        return (ProfilePlayingGames) rp.readSerializable();
    }

    @Override
    public ProfilePlayingGames getProfilePlayingGames(String uno, long gameId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        wp.writeLongNx(gameId);
        Request req = new Request(ProfileConstants.GET_PROFILE_PLAYING_GAMES, wp);
        RPacket rp = reqProcessor.process(req);
        return (ProfilePlayingGames) rp.readSerializable();
    }

    @Override
    public boolean modifyProfilePlayingGames(String uno, long gameId, UpdateExpress updateExpress) throws ServiceException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ProfilePlayingGames> queryProfilePlayingGames(String uno) throws ServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ProfileClientMobileDevice createProfileClientMobileDevice(ProfileClientMobileDevice mobileDevice) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(mobileDevice);
        Request req = new Request(ProfileConstants.CREATE_PROFILE_CLIENT_MOBILE_DEVICE, wp);
        RPacket rp = reqProcessor.process(req);
        return (ProfileClientMobileDevice) rp.readSerializable();
    }

    @Override
    public ProfileClientMobileDevice getProfileClientMobileDevice(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(ProfileConstants.GET_PROFILE_CLIENT_MOBILE_DEVICE, wp);
        RPacket rp = reqProcessor.process(req);
        return (ProfileClientMobileDevice) rp.readSerializable();
    }

    @Override
    public boolean sendPushMessage(PushMessage pushMessage) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(pushMessage);
        Request req = new Request(ProfileConstants.GET_PROFILE_CLIENT_MOBILE_DEVICE, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public SocialProfile getSocialProfileByScreenName(String screenName) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(screenName);
        Request req = new Request(ProfileConstants.GET_SOCIAL_PROFIEL_BLOG_BY_SCREENNAME, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialProfile) rp.readSerializable();
    }

    @Override
    public SocialProfile createSocialProfile(SocialProfileBlog profileBlog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(profileBlog);
        Request req = new Request(ProfileConstants.CREATE_SOCIAL_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialProfile) rp.readSerializable();
    }

    @Override
    public SocialProfile getSocialProfileByUno(String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(uno);
        Request req = new Request(ProfileConstants.GET_SOCIAL_PROFIEL_BLOG_BY_UNO, wp);
        RPacket rp = reqProcessor.process(req);
        return (SocialProfile) rp.readSerializable();
    }

    @Override
    public Map<String, SocialProfile> querySocialProfilesByUnosMap(Set<String> unoSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) unoSet);
        Request req = new Request(ProfileConstants.QUERY_SOCIAL_PROFILE_MAP_BY_UNOS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, SocialProfile>) rp.readSerializable();
    }

    @Override
    public boolean modifySocialProfileBlogByUno(UpdateExpress updateExpress, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(uno);
        Request req = new Request(ProfileConstants.MODIFY_SOCIAL_PROFIEL_BLOG_BY_UNO, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public boolean modifySocialProfileDetailByUno(UpdateExpress updateExpress, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(uno);
        Request req = new Request(ProfileConstants.MODIFY_SOCIAL_PROFIEL_DETAIL_BY_UNO, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public List<SocialProfile> querySocialProfilesByUnos(Set<String> unoSet) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) unoSet);
        Request req = new Request(ProfileConstants.QUERY_SOCIAL_PROFIELS_BY_UNOS, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<SocialProfile>) rp.readSerializable();
    }

    @Override
    public PageRows<SocialProfile> queryNewestSocialProfile(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(ProfileConstants.QUERY_NEWEST_SOCIAL_PROFILE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<SocialProfile>) rp.readSerializable();
    }
}
