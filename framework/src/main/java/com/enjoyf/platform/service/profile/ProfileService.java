package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProfileService extends EventReceiver {
    //profile apis
    public Profile getProfileByUno(String uno) throws ServiceException;

    public List<Profile> queryProfilesByUnos(Set<String> unos) throws ServiceException;

    public Map<String, Profile> queryProfilesByUnosMap(Set<String> unos) throws ServiceException;

    //blog apis
    public ProfileBlog getProfileBlogByUno(String uno) throws ServiceException;

    public ProfileBlog getProfileBlogByDomain(String domain) throws ServiceException;

    public ProfileBlog getProfileBlogByScreenName(String screenName) throws ServiceException;

    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName) throws ServiceException;

    public Map<String, ProfileBlog> queryProfileBlogByScreenNamesMap(Set<String> screenNames) throws ServiceException;

    public PageRows<Profile> searchProfileBlogs(String keyWord, Pagination page) throws ServiceException;

    public PageRows<Profile> queryProfileBlogsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException;

    public ProfileBlog createProfileBlog(ProfileBlog profileBlog) throws ServiceException;

    public boolean updateProfileBlog(ProfileBlog profileBlog) throws ServiceException;

    public boolean updateProfileBlog(String uno, Map<ObjectField, Object> keyValues) throws ServiceException;

    public boolean updateProfileBlog(String uno, UpdateExpress updateExpress) throws ServiceException;

    public ProfileBlog queryProfileblogByPhoneNum(String phone, ActStatus actStatus) throws ServiceException;


    //profile apis
    public ProfileDetail getProfileDetail(String uno) throws ServiceException;

    public ProfileDetail createProfileDetail(ProfileDetail profileDetail) throws ServiceException;

    public boolean updateProfileDetail(ProfileDetail profileDetail) throws ServiceException;

    public boolean updateProfileDetail(String uno, Map<ObjectField, Object> keyValues) throws ServiceException;

    //profile setting apis
    public ProfileSetting getProfileSetting(String uno) throws ServiceException;

    public ProfileSetting saveProfileSetting(ProfileSetting setting) throws ServiceException;

    public boolean updateProfileSetting(ProfileSetting setting) throws ServiceException;

    public boolean updateProfileSetting(String uno, Map<ObjectField, Object> keyValues) throws ServiceException;

    //profile sum apis
    public ProfileSum getProfileSum(String uno) throws ServiceException;

    public boolean updateProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException;

    public boolean increaseProfileSum(String uno, Map<ObjectField, Object> keyValues) throws ServiceException;

    //profile experience
    public List<ProfileExperience> createProfileExperience(String uno, List<ProfileExperience> list) throws ServiceException;

    public boolean removeProfileExperience(String uno, ProfileExperienceType experienceType) throws ServiceException;

    public List<ProfileExperience> queryProfileExperienceByUno(String uno) throws ServiceException;

    //common modify
    public boolean modifyBlog(String profileUno, UpdateExpress updateExpress, QueryExpress queryExpress, boolean isQueued) throws ServiceException;

    //common query
    public PageRows<ProfileBlog> query(QueryExpress queryExpress, Pagination page) throws ServiceException;

    /*----------------   ProfileMobileDevice -----------------------*/

    public ProfileMobileDevice increaseProfileMobileDevice(ProfileMobileDevice entry) throws ServiceException;

    //get ProfileMobileDevice
    public ProfileMobileDevice getProfileMobileDevice(QueryExpress queryExpress) throws ServiceException;

    //query ProfileMobileDevice
    public PageRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Pagination page) throws ServiceException;

    public RangeRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Rangination range) throws ServiceException;

    //update ProfileMobileDevice
    public boolean modifyProfileMobileDevice(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*新游帐号*/

    public ProfileDeveloper createProfileDeveloper(ProfileDeveloper profileDeveloper) throws ServiceException;

    public ProfileDeveloper getProfileDeveloper(QueryExpress queryExpress) throws ServiceException;

    public List<ProfileDeveloper> queryProfileDeveloper(QueryExpress queryExpress) throws ServiceException;

    public PageRows<ProfileDeveloper> queryProfileDeveloperByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyProfileDeveloper(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    public String saveMobileCode(String uno, String code) throws ServiceException;

    public String getMobileCode(String uno) throws ServiceException;

    public boolean removeMobileCode(String uno) throws ServiceException;

    /**
     * 社交端 正在玩的游戏
     */

    public ProfilePlayingGames createProfilePlayingGames(ProfilePlayingGames profilePlayingGames) throws ServiceException;

    public ProfilePlayingGames getProfilePlayingGames(String uno, long gameId) throws ServiceException;

    public boolean modifyProfilePlayingGames(String uno, long gameId, UpdateExpress updateExpress) throws ServiceException;

    public List<ProfilePlayingGames> queryProfilePlayingGames(String uno) throws ServiceException;

    public ProfileClientMobileDevice createProfileClientMobileDevice(ProfileClientMobileDevice mobileDevice) throws ServiceException;

    public ProfileClientMobileDevice getProfileClientMobileDevice(QueryExpress queryExpress) throws ServiceException;

    public boolean sendPushMessage(PushMessage pushMessage) throws ServiceException;


    /////////////////////////
    //todo
    public SocialProfile getSocialProfileByScreenName(String screenName) throws ServiceException;

    //todo
    public SocialProfile createSocialProfile(SocialProfileBlog profileBlog)throws ServiceException;

    //profile apis
    public SocialProfile getSocialProfileByUno(String uno) throws ServiceException;

    public Map<String,SocialProfile> querySocialProfilesByUnosMap(Set<String> unoSet)throws ServiceException;

    public boolean modifySocialProfileBlogByUno(UpdateExpress updateExpress,String uno)throws ServiceException;

    public boolean modifySocialProfileDetailByUno(UpdateExpress updateExpress,String uno)throws ServiceException;

    public List<SocialProfile> querySocialProfilesByUnos(Set<String> unoSet) throws ServiceException;

    public PageRows<SocialProfile> queryNewestSocialProfile(QueryExpress queryExpress, Pagination pagination) throws ServiceException;
}
