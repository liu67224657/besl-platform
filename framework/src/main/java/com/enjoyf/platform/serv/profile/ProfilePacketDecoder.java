/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.profile;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class ProfilePacketDecoder extends PacketDecoder {
    private ProfileLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    ProfilePacketDecoder(ProfileLogic logic) {
        processLogic = logic;

        setTransContainer(ProfileConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            //profile apis
            case ProfileConstants.PROFILE_GET_BY_UNO:
                wp.writeSerializable(processLogic.getProfileByUno(rPacket.readString()));
                break;
            case ProfileConstants.PROFILE_QUERY_BY_UNOS:
                wp.writeSerializable((Serializable) processLogic.queryProfilesByUnos((Set<String>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOGS_QUERY_BY_LIKE_SCREENNAME:
                wp.writeSerializable((Serializable) processLogic.queryProfileBlogsByLikeScreenName(rPacket.readStringUTF()));
                break;

            //detail
            case ProfileConstants.PROFILE_DETAIL_CREATE:
                wp.writeSerializable(processLogic.createProfileDetail((ProfileDetail) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_DETAIL_UPDATE:
                wp.writeBooleanNx(processLogic.updateProfileDetail((ProfileDetail) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_DETAIL_UPDATE_BY_MAP:
                wp.writeBooleanNx(processLogic.updateProfileDetail(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_DETAIL_GET:
                wp.writeSerializable(processLogic.getProfileDetail(rPacket.readString()));
                break;

            //sum
            case ProfileConstants.PROFILE_SUM_INCREASE:
                wp.writeBooleanNx(processLogic.increaseProfileSum(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_SUM_UPDATE_BY_MAP:
                wp.writeBooleanNx(processLogic.updateProfileSum(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_SUM_GET:
                wp.writeSerializable(processLogic.getProfileSum(rPacket.readString()));
                break;

            //setting
            case ProfileConstants.PROFILE_SETTING_CREATE:
                wp.writeSerializable(processLogic.saveProfileSetting((ProfileSetting) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_SETTING_UPDATE:
                wp.writeBooleanNx(processLogic.updateProfileSetting((ProfileSetting) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_SETTING_UPDATE_BY_MAP:
                wp.writeBooleanNx(processLogic.updateProfileSetting(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_SETTING_GET:
                wp.writeSerializable(processLogic.getProfileSetting(rPacket.readString()));
                break;

            //blog
            case ProfileConstants.PROFILE_BLOG_CREATE:
                wp.writeSerializable(processLogic.createProfileBlog((ProfileBlog) rPacket.readSerializable()));
                break;
//            case ProfileConstants.PROFILE_BLOG_CREATE_GENDOMAIN:
//                wp.writeSerializable(processLogic.createProfileBlogGenDomain((ProfileBlog) rPacket.readSerializable()));
//                break;
            case ProfileConstants.PROFILE_BLOG_UPDATE:
                wp.writeBooleanNx(processLogic.updateProfileBlog((ProfileBlog) rPacket.readSerializable()));
            case ProfileConstants.PROFILE_BLOG_UPDATE_BY_MAP:
                wp.writeBooleanNx(processLogic.updateProfileBlog(rPacket.readString(), (Map<ObjectField, Object>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOG_SEARCH:
                wp.writeSerializable(processLogic.searchProfileBlogs(rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOG_GET_BY_UNO:
                wp.writeSerializable(processLogic.getProfileBlogByUno(rPacket.readString()));
                break;
            case ProfileConstants.PROFILE_BLOG_GET_BY_DOMAIN:
                wp.writeSerializable(processLogic.getProfileBlogByDomain(rPacket.readString()));
                break;
            case ProfileConstants.PROFILE_BLOG_GET_BY_SCREENNAME:
                wp.writeSerializable(processLogic.getProfileBlogByScreenName(rPacket.readStringUTF()));
                break;
            case ProfileConstants.PROFILE_BLOG_QUERY_BY_SCREENNAME_SET:
                wp.writeSerializable((Serializable) processLogic.queryProfileBlogByScreenNamesMap((Set<String>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOG_QUERY_BY_DATE_STEP:
                wp.writeSerializable((Serializable) processLogic.queryProfileBlogsByDateStep((Date) rPacket.readSerializable(), (Date) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOG_UPDATE_BY_EXPRESS:
                wp.writeBooleanNx(processLogic.updateProfileBlog(rPacket.readString(), (UpdateExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_BLOG_QUERY_BY_PHONENUM_STATUS:
                wp.writeSerializable(processLogic.queryProfileblogByPhoneNum(rPacket.readStringUTF(), (ActStatus) rPacket.readSerializable()));
                break;

            //profile experience
            case ProfileConstants.PROFILE_EXPERIENCE_CREATE:
                wp.writeSerializable((Serializable) processLogic.createProfileExperience(rPacket.readString(), (List<ProfileExperience>) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_EXPERIENCE_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryProfileExperienceByUno(rPacket.readString()));
                break;
            case ProfileConstants.PROFILE_EXPERIENCE_REMOVE:
                wp.writeSerializable(processLogic.removeProfileExperience(rPacket.readString(), (ProfileExperienceType) rPacket.readSerializable()));
                break;


            // tools query view
//            case ProfileConstants.VIEW_ACCOUNT_PROFILE_BLOG_BY_ACCOUNT_QUERY_BY_PARAM:
//                wp.writeSerializable((Serializable)processLogic.queryViewAccountProfileBlogListByAccount((ProfileQueryParam) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;
//
//            case ProfileConstants.VIEW_ACCOUNT_PROFILE_BLOG_BY_BLOG_QUERY_BY_PARAM:
//                wp.writeSerializable((Serializable)processLogic.queryViewAccountProfileBlogListByBlog((ProfileQueryParam) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
//                break;

            //recieve event
            case ProfileConstants.RECIEVE_EVENT:
                wp.writeBooleanNx(processLogic.receiveEvent((Event) rPacket.readSerializable()));
                break;

            //common update
            case ProfileConstants.COMMON_UPDATE:
                wp.writeBooleanNx(processLogic.modifyBlog(rPacket.readStringUTF(), (UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable(), rPacket.readBooleanNx()));
                break;

            //common query
            case ProfileConstants.COMMON_QUERY:
                wp.writeSerializable(processLogic.query((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            // ProfileMobileDevice
            case ProfileConstants.PROFILE_MOBILE_DEVICE_CREATE:
                wp.writeSerializable(processLogic.increaseProfileMobileDevice((ProfileMobileDevice) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_MOBILE_DEVICE_GET:
                wp.writeSerializable(processLogic.getProfileMobileDevice((QueryExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_MOBILE_DEVICE_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryProfileMobileDevice((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_MOBILE_DEVICE_QUERY_BY_RANGE:
                wp.writeSerializable(processLogic.queryProfileMobileDevice((QueryExpress) rPacket.readSerializable(), (Rangination) rPacket.readSerializable()));
                break;
            case ProfileConstants.PROFILE_MOBILE_DEVICE_UPDATE:
                wp.writeBooleanNx(processLogic.modifyProfileMobileDevice((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            case ProfileConstants.CREATE_PROFILE_NEWRELEASE:
                wp.writeSerializable(processLogic.createProfileDeveloper((ProfileDeveloper) rPacket.readSerializable()));
                break;
            case ProfileConstants.GET_PROFILE_NEWRELEASE:
                wp.writeSerializable(processLogic.getProfileDeveloper((QueryExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.QUERY_PROFILE_NEWRELEASE:
                wp.writeSerializable((Serializable) processLogic.queryProfileDeveloper((QueryExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.QUERY_PROFILE_NEWRELEASE_BY_PAGE:
                wp.writeSerializable(processLogic.queryProfileDeveloperByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ProfileConstants.MODIFY_PROFILE_NEWRELEASE:
                wp.writeSerializable(processLogic.modifyProfileDeveloper((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.SAVE_CODE_FOR_MEMCACHED:
                wp.writeStringUTF(processLogic.saveMobileCode(rPacket.readStringUTF(), rPacket.readStringUTF()));
                break;
            case ProfileConstants.GET_CODE_FOR_MEMCACHED:
                wp.writeSerializable(processLogic.getMobileCode(rPacket.readStringUTF()));
                break;
            case ProfileConstants.DELETE_CODE_FOR_MEMCACHED:
                wp.writeBooleanNx(processLogic.removeMobileCode(rPacket.readStringUTF()));
                break;

            case ProfileConstants.CREATE_PROFILE_CLIENT_MOBILE_DEVICE:
                wp.writeSerializable(processLogic.createProfileClientMobileDevice((ProfileClientMobileDevice) rPacket.readSerializable()));
                break;
            case ProfileConstants.GET_PROFILE_CLIENT_MOBILE_DEVICE:
                wp.writeSerializable(processLogic.getProfileClientMobileDevice((QueryExpress) rPacket.readSerializable()));
                break;
            case ProfileConstants.SEND_PUSHMESSAGE:
                wp.writeSerializable(processLogic.sendPushMessage((PushMessage) rPacket.readSerializable()));
                break;

            //////////////////
            case ProfileConstants.CREATE_SOCIAL_PROFILE:
                wp.writeSerializable(processLogic.createSocialProfile((SocialProfileBlog) rPacket.readSerializable()));
                break;
            case ProfileConstants.GET_SOCIAL_PROFIEL_BLOG_BY_UNO:
                wp.writeSerializable(processLogic.getSocialProfileByUno(rPacket.readStringUTF()));
                break;
            case ProfileConstants.GET_SOCIAL_PROFIEL_BLOG_BY_SCREENNAME:
                wp.writeSerializable(processLogic.getSocialProfileByScreenName(rPacket.readStringUTF()));
                break;
            case ProfileConstants.QUERY_SOCIAL_PROFILE_MAP_BY_UNOS:
                wp.writeSerializable((Serializable) processLogic.querySocialProfilesByUnosMap((Set<String>) rPacket.readSerializable()));
                break;
            case ProfileConstants.MODIFY_SOCIAL_PROFIEL_BLOG_BY_UNO:
                wp.writeBooleanNx(processLogic.modifySocialProfileBlogByUno((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case ProfileConstants.MODIFY_SOCIAL_PROFIEL_DETAIL_BY_UNO:
                wp.writeBooleanNx(processLogic.modifySocialProfileDetailByUno((UpdateExpress) rPacket.readSerializable(), rPacket.readStringUTF()));
                break;
            case ProfileConstants.QUERY_SOCIAL_PROFIELS_BY_UNOS:
                wp.writeSerializable((Serializable) processLogic.querySocialProfilesByUnos((Set<String>) rPacket.readSerializable()));
                break;
            case ProfileConstants.QUERY_NEWEST_SOCIAL_PROFILE:
                wp.writeSerializable(processLogic.queryNewestSocialProfile((QueryExpress)rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            default:
                GAlerter.lab("ProfilePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
