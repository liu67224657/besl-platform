package com.enjoyf.webapps.joyme.weblogic.joymeapp.socialclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.SocialMessageEvent;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.SocialMessage;
import com.enjoyf.platform.service.message.SocialMessageType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.ProfileSum;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.thirdapi.friends.FriendsInfo;
import com.enjoyf.platform.thirdapi.friends.FriendsMessage;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppAdvertiseDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.api.MessageListDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.socialclient.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-17
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "socialClientWebLogic")
public class SocialClientWebLogic {

    private static String WANBA_MESSAGELIST_2_2_0 = "我发布的图片主题";

    public NextPageRows<SocialProfileContentDTO> queryHotList(String uno, NextPagination pagination, int platform, String appId, long startNum) throws ServiceException {
        NextPageRows<SocialProfileContentDTO> returnObj = new NextPageRows<SocialProfileContentDTO>();
        List<SocialProfileContentDTO> list = new LinkedList<SocialProfileContentDTO>();

        NextPageRows<SocialHotContent> contentPageRows = ContentServiceSngl.get().querySocialHotContentByPage(pagination);
        if (contentPageRows == null || CollectionUtil.isEmpty(contentPageRows.getRows())) {
            return null;
        }

        Set<Long> contentIdSet = new HashSet<Long>();
        Set<String> unoSet = new HashSet<String>();
        for (SocialHotContent content : contentPageRows.getRows()) {
            contentIdSet.add(content.getContentId());
            unoSet.add(content.getUno());
        }
        Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(contentIdSet);

        Set<Long> activityIdSet = new HashSet<Long>();
        for (Long cid : contentIdSet) {
            if (contentMap.containsKey(cid)) {
                SocialContent socialContent = contentMap.get(cid);
                if (socialContent.getActivityId() > 0L) {
                    activityIdSet.add(socialContent.getActivityId());
                }
            }
        }
        Map<Long, SocialActivity> activityMap = ContentServiceSngl.get().querySocialActivityByIdSet(activityIdSet);

        Map<String, SocialProfile> profileMap = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unoSet);

        Set<Long> agreeContentIdSet = ContentServiceSngl.get().checkSocialContentAction(uno, SocialContentActionType.AGREE, contentIdSet);

        Map<Integer, List<AppAdvertiseDTO>> adMap = new HashMap<Integer, List<AppAdvertiseDTO>>();
        if (startNum == 0l) {
            adMap = queryAdList(appId, AppAdvertisePublishType.ARTICLE_LIS, platform);
        }

        //查询文章分享模板
        long shareflag = getSocialsharetemplate(appId, platform, SocialShareType.SOCIAL_CONTENT_TYPE, -1L);

        int i = 0;
        NextPagination np = new NextPagination(0L, 7, true);
        for (SocialHotContent hotContent : contentPageRows.getRows()) {
            SocialContent content = contentMap.get(hotContent.getContentId());
            if (content == null) {
                continue;
            }

            List<AppAdvertiseDTO> advertiseDTOList = adMap.get(i);
            if (!CollectionUtil.isEmpty(advertiseDTOList)) {
                for (AppAdvertiseDTO advertiseDTO : advertiseDTOList) {
                    SocialProfileContentDTO spc = new SocialProfileContentDTO();
                    spc.setType(SocialResultType.AD_RESULT.getCode());
                    spc.setAdvertise(advertiseDTO);
                    list.add(spc);
                }
            }

            SocialProfileContentDTO spcDTO = new SocialProfileContentDTO();

            ContentDTO contentDTO = buildContentDTO(content, agreeContentIdSet, platform, activityMap, shareflag);
            spcDTO.setContent(contentDTO);

            SocialProfile profile = profileMap.containsKey(content.getUno()) ? profileMap.get(content.getUno()) : null;
            if (profile == null || profile.getBlog() == null) {
                continue;
            }

            if (profile != null && profile.getBlog() != null && profile.getDetail() != null) {
                ProfileDTO profileDTO = buildProfileDTO(profile, null, content);
                spcDTO.setProfile(profileDTO);
            }
            spcDTO.setType(SocialResultType.CONTENT_RESULT.getCode());

            //agress list
            NextPageRows<SocialContentAction> sa = ContentServiceSngl.get().querySocialContentAction(content.getContentId(), SocialContentActionType.AGREE, np);
            if (!CollectionUtil.isEmpty(sa.getRows())) {
                AgreeListDTO agreeListDTO = buildHotListAgreeList(sa.getRows(), content);
                spcDTO.setAgress(agreeListDTO);
            }

            list.add(spcDTO);
            i++;
        }

        // TODO EVENT 下次版本x
        MessageServiceSngl.get().modifySocialNotice(uno, SocialMessageType.HOT.getCode());

        returnObj.setPage(contentPageRows.getPage());
        returnObj.setRows(list);
        return returnObj;
    }

    private AgreeListDTO buildHotListAgreeList(List<SocialContentAction> rows, SocialContent content) throws ServiceException {
        AgreeListDTO returnDTO = new AgreeListDTO();
        List<ProfileDTO> list = new ArrayList<ProfileDTO>();
        for (SocialContentAction sca : rows) {
            ProfileDTO dto = new ProfileDTO();
            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(sca.getUno());
            if (profile == null) {
                continue;
            }
            dto.setUsername(profile.getBlog().getScreenName());
            dto.setUno(sca.getUno());
            dto.setBirthday(profile.getDetail().getBirthday());
            dto.setSex(profile.getDetail().getSex());
            dto.setSignature(profile.getBlog().getDescription());
            String head = ImageURLTag.parseFirstFacesInclude(profile.getBlog().getHeadIconSet(), profile.getDetail().getSex(), "m", true);
            dto.setIcon(head);
            dto.setBlogsum(profile.getSum().getSocialBlogSum());
            dto.setPlaysum(profile.getSum().getSocialPlaySum());
            dto.setFanssum(profile.getSum().getSocialFansSum());
            dto.setFocussum(profile.getSum().getSocialFocusSum());
            dto.setPlay(profile.getBlog().getPlayingGames());
            dto.setBackpic(profile.getBlog().getBackgroundPic());
            list.add(dto);
        }
        returnDTO.setRows(list);
        returnDTO.setAgreenum(content.getAgreeNum());
        return returnDTO;
    }

    private Map<Integer, List<AppAdvertiseDTO>> queryAdList(String appId, AppAdvertisePublishType publishType, int platform) throws ServiceException {
        Map<Integer, List<AppAdvertiseDTO>> returnMap = new HashMap<Integer, List<AppAdvertiseDTO>>();
        List<AppAdvertisePublish> publishList = AdvertiseServiceSngl.get().queryAppAdvertisePublishByCache(appId, publishType, null);
        if (CollectionUtil.isEmpty(publishList)) {
            return returnMap;
        }
        Set<Long> advertiseIdSet = new HashSet<Long>();
        for (AppAdvertisePublish publish : publishList) {
            advertiseIdSet.add(publish.getAdvertiseId());
        }

        Map<Long, AppAdvertise> advertiseMap = AdvertiseServiceSngl.get().queryAppAdvertiseByIdSet(advertiseIdSet, platform);

        for (AppAdvertisePublish publish : publishList) {
            AppAdvertise advertise = advertiseMap.get(publish.getAdvertiseId());
            if (advertise == null) {
                continue;
            }
            AppAdvertiseDTO appAdvertiseDTO = buildAppAdvertiseDTO(advertise, publish);
            if (returnMap.containsKey(appAdvertiseDTO.getIndex())) {
                returnMap.get(appAdvertiseDTO.getIndex()).add(appAdvertiseDTO);
            } else {
                returnMap.put(appAdvertiseDTO.getIndex(), new ArrayList<AppAdvertiseDTO>());
                returnMap.get(appAdvertiseDTO.getIndex()).add(appAdvertiseDTO);
            }
        }
        return returnMap;
    }

    private AppAdvertiseDTO buildAppAdvertiseDTO(AppAdvertise advertise, AppAdvertisePublish publish) {
        AppAdvertiseDTO appAdvertiseDTO = new AppAdvertiseDTO();
        appAdvertiseDTO.setAdvertiseid(advertise.getAdvertiseId());
        appAdvertiseDTO.setName(advertise.getAdvertiseName());
        appAdvertiseDTO.setDescription(advertise.getAdvertiseDesc());
        appAdvertiseDTO.setIos4pic(advertise.getPicUrl1());
        appAdvertiseDTO.setIos5pic(advertise.getPicUrl2());
        if (StringUtil.isEmpty(advertise.getUrl().trim())) {
            appAdvertiseDTO.setRurl("");
        } else {
            appAdvertiseDTO.setRurl(WebappConfig.get().getUrlWww() + "/joymeapp/app/advertise/click/" + publish.getPublishId() + "/" + advertise.getAdvertiseId());
        }
        appAdvertiseDTO.setIndex(publish.getPublishParam().getNumberParam());
        appAdvertiseDTO.setPublishId(publish.getPublishId());
        appAdvertiseDTO.setRedirecttype(advertise.getAppAdvertiseRedirectType());
        appAdvertiseDTO.setViewtime(publish.getPublishParam().getLongtime());
        return appAdvertiseDTO;
    }

    private ProfileDTO buildProfileDTO(SocialProfile profile, Map<String, SocialRelation> focusRelationMap, SocialContent content) throws ServiceException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUno(profile.getUno());
        profileDTO.setBirthday(profile.getDetail().getBirthday());
        profileDTO.setIcon(ImageURLTag.parseFirstFacesInclude(profile.getBlog().getHeadIconSet(), profile.getDetail().getSex(), "m", true));
        profileDTO.setUsername(StringUtil.isEmpty(profile.getBlog().getScreenName()) ? "" : profile.getBlog().getScreenName());
        profileDTO.setSex(profile.getDetail().getSex());
        profileDTO.setSignature(StringUtil.isEmpty(profile.getBlog().getDescription()) ? "" : profile.getBlog().getDescription());

        profileDTO.setPlay(StringUtil.isEmpty(profile.getBlog().getPlayingGames()) ? "" : profile.getBlog().getPlayingGames());
        profileDTO.setBackpic(StringUtil.isEmpty(profile.getBlog().getBackgroundPic()) ? "" : profile.getBlog().getBackgroundPic());

        ProfileSum profileSum = ProfileServiceSngl.get().getProfileSum(profile.getUno());
        profileDTO.setBlogsum(profileSum == null ? 0 : profileSum.getSocialBlogSum());
        profileDTO.setFanssum(profileSum == null ? 0 : profileSum.getSocialFansSum());
        profileDTO.setPlaysum(profileSum == null ? 0 : profileSum.getSocialPlaySum());
        profileDTO.setFocussum(profileSum == null ? 0 : profileSum.getSocialFocusSum());
        if (CollectionUtil.isEmpty(focusRelationMap) || !focusRelationMap.containsKey(content.getUno())) {
            profileDTO.setFocus(FocusType.UNFOCUS.getCode());
        } else {
            SocialRelation socialRelation = focusRelationMap.get(content.getUno());
            if (socialRelation.getSrcStatus().getCode().equals(ActStatus.ACTED.getCode())
                    && socialRelation.getDestStatus().getCode().equals(ActStatus.ACTED.getCode())) {
                profileDTO.setFocus(FocusType.ALL.getCode());
            } else if (socialRelation.getSrcStatus().getCode().equals(ActStatus.ACTED.getCode())) {
                profileDTO.setFocus(FocusType.FOLLOW.getCode());
            } else {
                profileDTO.setFocus(FocusType.UNFOCUS.getCode());
            }
        }
        return profileDTO;
    }

    public ContentDTO buildContentDTO(SocialContent content, Set<Long> agreeContentIdSet, int platform, Map<Long, SocialActivity> activityMap, long shareflag) throws ServiceException {
        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setTitle(content.getTitle() == null ? "" : content.getTitle());
        contentDTO.setAgreenum(content.getAgreeNum());
        contentDTO.setAudio(content.getAudio());
        contentDTO.setAudiotime(content.getAudioLen());
        contentDTO.setBody(content.getBody() == null ? "" : content.getBody());
        contentDTO.setCid(content.getContentId());
        contentDTO.setCreatetime(content.getCreateTime().getTime());
        contentDTO.setLat(content.getLat());
        contentDTO.setLon(content.getLon());
        contentDTO.setPic(content.getPic());
        contentDTO.setUno(content.getUno());
        contentDTO.setReplynum(content.getReplyNum());
        contentDTO.setReadnum(content.getReadNum());
        contentDTO.setPlaynum(content.getPlayNum());
        contentDTO.setShareflag(shareflag);
        if (!CollectionUtil.isEmpty(agreeContentIdSet)) {
            contentDTO.setIsagree(agreeContentIdSet.contains(content.getContentId()));
        }
        if (!CollectionUtil.isEmpty(activityMap) && activityMap.containsKey(content.getActivityId())) {
            SocialActivity activity = activityMap.get(content.getActivityId());
            ActivityDTO activityDTO = buildActivityDTO(platform, activity);
            contentDTO.setActivity(activityDTO);
        }
        return contentDTO;
    }

    public NextPageRows<SocialProfileContentDTO> querySocialContentConcernList(String uno, String desuno, NextPagination nextPagination, SocialTimeLineDomain domain, int platform, String appkey) throws ServiceException {
        NextPageRows<SocialProfileContentDTO> returnObj = new NextPageRows<SocialProfileContentDTO>();
        List<SocialProfileContentDTO> list = new LinkedList<SocialProfileContentDTO>();
        String blogUno = StringUtil.isEmpty(desuno) ? uno : desuno;
        NextPageRows<SocialTimeLineItem> itemNextPageRows = TimeLineServiceSngl.get().querySocialTimeLineItemNextList(domain, blogUno, nextPagination);
        if (itemNextPageRows == null || CollectionUtil.isEmpty(itemNextPageRows.getRows())) {
            return null;
        }

        Set<Long> contentIdSet = new HashSet<Long>();
        Set<String> unoSet = new HashSet<String>();
        for (SocialTimeLineItem item : itemNextPageRows.getRows()) {
//            contentIdSet.add(Long.valueOf(item.getContentId()));
//            unoSet.add(item.getContentUno());
        }
        Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(contentIdSet);

        Set<Long> activityIdSet = new HashSet<Long>();
        for (Long cid : contentIdSet) {
            if (contentMap.containsKey(cid)) {
                SocialContent socialContent = contentMap.get(cid);
                if (socialContent.getActivityId() > 0L) {
                    activityIdSet.add(socialContent.getActivityId());
                }
            }
        }
        Map<Long, SocialActivity> activityMap = ContentServiceSngl.get().querySocialActivityByIdSet(activityIdSet);

        Map<String, SocialProfile> profileMap = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unoSet);

        Set<Long> agreeContentIdSet = ContentServiceSngl.get().checkSocialContentAction(uno, SocialContentActionType.AGREE, contentIdSet);


        //查询文章分享模板
        long shareflag = getSocialsharetemplate(appkey, platform, SocialShareType.SOCIAL_CONTENT_TYPE, -1L);

        NextPagination np = new NextPagination(0L, 7, true);
        for (SocialTimeLineItem lineItem : itemNextPageRows.getRows()) {
//            SocialContent content = contentMap.containsKey(lineItem.getContentId()) ? contentMap.get(lineItem.getContentId()) : null;
            SocialContent content = null;
            if (content == null) {
                continue;
            }

            SocialProfileContentDTO dto = new SocialProfileContentDTO();

            ContentDTO contentDTO = buildContentDTO(content, agreeContentIdSet, platform, activityMap, shareflag);
            dto.setContent(contentDTO);

            SocialProfile profile = profileMap.containsKey(content.getUno()) ? profileMap.get(content.getUno()) : null;
            if (profile == null || profile.getBlog() == null) {
                continue;
            }


            ProfileDTO profileDTO = buildProfileDTO(profile, null, content);
            dto.setProfile(profileDTO);

            //agress list
            NextPageRows<SocialContentAction> sa = ContentServiceSngl.get().querySocialContentAction(content.getContentId(), SocialContentActionType.AGREE, np);
            if (!CollectionUtil.isEmpty(sa.getRows())) {
                AgreeListDTO agreeListDTO = buildHotListAgreeList(sa.getRows(), content);
                dto.setAgress(agreeListDTO);
            }

            list.add(dto);
        }
        returnObj.setPage(itemNextPageRows.getPage());
        returnObj.setRows(list);
        return returnObj;
    }


    public PageRows<SocialContentReplyDTO> querySocialContentReply(long contentId, Pagination pagination) throws ServiceException {
        PageRows<SocialContentReply> pageRows = ContentServiceSngl.get().querySocialContentReply(contentId, pagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }

        PageRows<SocialContentReplyDTO> returnObj = new PageRows<SocialContentReplyDTO>();
        if (!CollectionUtil.isEmpty(pageRows.getRows())) {
            List<SocialContentReplyDTO> listDTO = new ArrayList<SocialContentReplyDTO>();
            for (SocialContentReply scr : pageRows.getRows()) {
                listDTO.add(rsSocialContentReplyDTO(scr));
            }
            returnObj.setRows(listDTO);
            returnObj.setPage(pageRows.getPage());
        }
        return returnObj;
    }

    private SocialContentReplyDTO rsSocialContentReplyDTO(SocialContentReply sc) throws ServiceException {
        SocialContentReplyDTO dto = new SocialContentReplyDTO();
        dto.setReplyid(sc.getReplyId());
        dto.setReplyuno(sc.getReplyUno());
        dto.setCid(sc.getContentId());
        dto.setCuno(sc.getContentUno());
        if (!com.enjoyf.util.StringUtil.isEmpty(sc.getParentUno())) {
            dto.setPid(sc.getParentId());
            dto.setPuno(sc.getParentUno());
            SocialProfile pprofile = ProfileServiceSngl.get().getSocialProfileByUno(sc.getParentUno());
            if (pprofile != null) {
                dto.setPname(pprofile.getBlog().getScreenName());
            }
        }

        if (!com.enjoyf.util.StringUtil.isEmpty(sc.getRootUno())) {
            dto.setRid(sc.getRootId());
            dto.setRuno(sc.getRootUno());
            SocialProfile rprofile = ProfileServiceSngl.get().getSocialProfileByUno(sc.getRootUno());
            if (rprofile != null) {
                dto.setRname(rprofile.getBlog().getScreenName());
            }
        }

        dto.setBody(sc.getBody());
        dto.setCreatetime(sc.getCreateTime().getTime());
        dto.setLat(sc.getLat());
        dto.setLon(sc.getLon());

        SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(sc.getReplyUno());
        if (profile != null) {
            dto.setBirthday(StringUtil.isEmpty(profile.getDetail().getBirthday()) ? "" : profile.getDetail().getBirthday());
            dto.setHeadicon(ImageURLTag.parseFirstFacesInclude(profile.getBlog().getHeadIconSet(), profile.getDetail().getSex(), "m", true));
            dto.setName(StringUtil.isEmpty(profile.getBlog().getScreenName()) ? "" : profile.getBlog().getScreenName());
            dto.setSex(StringUtil.isEmpty(profile.getDetail().getSex()) ? "" : profile.getDetail().getSex());
        }

        return dto;
    }

    public NextPageRows<WatermarkDTO> querySocialWatermark(int platform, NextPagination nextPagination) throws ServiceException {
        NextPageRows<SocialWatermark> pageRows = ContentServiceSngl.get().querySocialWatermarkByNext(nextPagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }
        Set<Long> aidSet = new HashSet<Long>();
        for (SocialWatermark watermark : pageRows.getRows()) {
            if (watermark.getActivityId() > 0l) {
                aidSet.add(watermark.getActivityId());
            }
        }

        NextPageRows<WatermarkDTO> returnPageRows = new NextPageRows<WatermarkDTO>();
        List<WatermarkDTO> returnList = new ArrayList<WatermarkDTO>();

        for (SocialWatermark watermark : pageRows.getRows()) {
            WatermarkDTO watermarkDTO = buildWatermarkDTO(platform, watermark);
            returnList.add(watermarkDTO);
        }
        returnPageRows.setPage(pageRows.getPage());
        returnPageRows.setRows(returnList);
        return returnPageRows;
    }

    public ActivityDTO buildActivityDTO(int platform, SocialActivity activity) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setType(0);
        activityDTO.setActivityid(activity.getActivityId());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setTitle(activity.getTitle());
        if (platform == 0) {
            activityDTO.setIcon(activity.getIosIcon());
            activityDTO.setSmallpic(activity.getIosSmallPic());
            activityDTO.setBigpic(activity.getIosBigPic());
        } else {
            activityDTO.setIcon(activity.getAndroidIcon());
            activityDTO.setSmallpic(activity.getAndroidSmallPic());
            activityDTO.setBigpic(activity.getAndroidBigPic());
        }
        activityDTO.setUsesum(activity.getUseSum());
        activityDTO.setReplysum(activity.getReplySum());
        activityDTO.setAgreesum(activity.getAgreeSum());
        activityDTO.setGiftsum(activity.getGiftSum());
        if (activity.getShareId() > 0l) {
            activityDTO.setShareid(activity.getShareId());
        }
        activityDTO.setRedirecttype(activity.getRedirectType());
        activityDTO.setRurl(activity.getRedirectUrl());

        SubscriptDTO subscriptDTO = buildSubscriptDTO(activity.getSubscript());
        activityDTO.setSubscript(subscriptDTO);
        return activityDTO;
    }

    public WatermarkDTO buildWatermarkDTO(int platform, SocialWatermark watermark) {
        WatermarkDTO watermarkDTO = new WatermarkDTO();
        watermarkDTO.setWatermarkid(watermark.getWatermarkId());
        watermarkDTO.setTitle(watermark.getTitle());
        watermarkDTO.setDescription(watermark.getDescription());
        if (platform == 0) {
            watermarkDTO.setIcon(watermark.getIosIcon());
        } else {
            watermarkDTO.setIcon(watermark.getAndroidIcon());
        }
        if (platform == 0) {
            watermarkDTO.setPic(watermark.getIosPic());
        } else {
            watermarkDTO.setPic(watermark.getAndroidPic());
        }
        if (watermark.getActivityId() > 0l) {
            watermarkDTO.setActivityid(watermark.getActivityId());
        }

        SubscriptDTO subscriptDTO = buildSubscriptDTO(watermark.getSubscript());

        watermarkDTO.setSubscript(subscriptDTO);

        return watermarkDTO;
    }

    public SubscriptDTO buildSubscriptDTO(Subscript subscript) {
        SubscriptDTO subscriptDTO = new SubscriptDTO();
        subscriptDTO.setType(subscript.getType());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtil.isEmpty(subscript.getStartDate())) {
            try {
                subscriptDTO.setStarttime(df.parse(subscript.getStartDate()).getTime());
            } catch (ParseException e) {
                GAlerter.lab(this.getClass().getName() + " ParseException.e:", e);
            }
        }
        if (!StringUtil.isEmpty(subscript.getEndDate())) {
            try {
                subscriptDTO.setEndtime(df.parse(subscript.getEndDate()).getTime());
            } catch (ParseException e) {
                GAlerter.lab(this.getClass().getName() + " ParseException.e:", e);
            }
        }
        return subscriptDTO;
    }

    public BgAudioDTO buildBgAudioDTO(SocialBackgroundAudio bgAudio, int platform) {
        BgAudioDTO returnDTO = new BgAudioDTO();
        returnDTO.setAudioid(bgAudio.getAudioId());
        returnDTO.setAudioname(bgAudio.getAudioName());
        returnDTO.setAudiopic(bgAudio.getAudioPic());
        if (platform == 0) {
            returnDTO.setAudiosrc(bgAudio.getMp3Src());
        } else {
            returnDTO.setAudiosrc(bgAudio.getMp3Src());
        }
        returnDTO.setSinger(bgAudio.getSinger());
        returnDTO.setDescription(bgAudio.getAudioDescription());
        returnDTO.setUsesum(bgAudio.getUseSum());
        SubscriptDTO subscriptDTO = buildSubscriptDTO(bgAudio.getSubscript());
        returnDTO.setSubscript(subscriptDTO);
        return returnDTO;
    }


    private FriendsMessage toShowFriendsInfo(String json) {
        FriendsMessage fr = new FriendsMessage();
        if (StringUtil.isEmpty(json)) {
            return fr;
        }
        JSONObject ob = JSONObject.fromObject(json);
        if (ob.containsKey("error")) {
            return null;
        }
        fr.setId(ob.getLong("id"));
        fr.setIdstr(ob.getString("idstr"));
        fr.setScreen_name(ob.getString("screen_name"));
        fr.setProfile_image_url(ob.getString("profile_image_url"));
        return fr;
    }


    private FriendsInfo toFriendsInfo(String json) {
        FriendsInfo friendsInfo = new FriendsInfo();
        if (StringUtil.isEmpty(json)) {
            return friendsInfo;
        }
        JSONObject obj = JSONObject.fromObject(json);
        if (obj.containsKey("next_cursor")) {
            friendsInfo.setNext_curso(obj.getInt("next_cursor"));
        }
        if (obj.containsKey("previous_cursor")) {
            friendsInfo.setPrevious_cursor(obj.getInt("previous_cursor"));
        }
        if (obj.containsKey("total_number")) {
            friendsInfo.setTotal_number(obj.getInt("total_number"));
        }

        JSONArray array = (JSONArray) obj.get("users");
        if (array != null) {
            List<FriendsMessage> users = new ArrayList<FriendsMessage>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject ob = array.getJSONObject(i);
                FriendsMessage fr = new FriendsMessage();
                fr.setId(ob.getLong("id"));
                fr.setIdstr(ob.getString("idstr"));
                fr.setScreen_name(ob.getString("screen_name"));
                fr.setName(ob.getString("name"));
                fr.setProvince(ob.getInt("province"));
                fr.setCity(ob.getInt("city"));
                fr.setLocation(ob.getString("location"));
                fr.setDescription(ob.getString("description"));
                fr.setUrl(ob.getString("url"));
                fr.setProfile_image_url(ob.getString("profile_image_url"));
                fr.setDomain(ob.getString("domain"));
                fr.setWeihao(ob.getString("weihao"));
                fr.setGender(ob.getString("gender"));
                fr.setFollowers_count(ob.getInt("followers_count"));
                fr.setFriends_count(ob.getInt("friends_count"));
                fr.setStatuses_count(ob.getInt("statuses_count"));
                fr.setFavourites_count(ob.getInt("favourites_count"));
                fr.setCreated_at(ob.getString("created_at"));
                fr.setFollowing(ob.getBoolean("following"));
                fr.setAllow_all_act_msg(ob.getBoolean("allow_all_act_msg"));
                fr.setGeo_enabled(ob.getBoolean("geo_enabled"));
                fr.setVerified(ob.getBoolean("verified"));
                fr.setRemark(ob.getString("remark"));
                fr.setAllow_all_comment(ob.getBoolean("allow_all_comment"));
                fr.setAvatar_large(ob.getString("avatar_large"));
                fr.setAvatar_hd(ob.getString("avatar_hd"));
                fr.setFollow_me(ob.getBoolean("follow_me"));
                fr.setOnline_status(ob.getInt("online_status"));
                fr.setBi_followers_count(ob.getInt("bi_followers_count"));
                fr.setLang(ob.getString("lang"));
                users.add(fr);
            }
            friendsInfo.setUsers(users);
        }
        return friendsInfo;
    }

    public NextPageRows<BgAudioDTO> querySocialBgAudio(int platform, NextPagination nextPagination) throws ServiceException {
        NextPageRows<SocialBackgroundAudio> pageRows = ContentServiceSngl.get().querySocialBgAudioByNext(nextPagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }

        NextPageRows<BgAudioDTO> returnPageRows = new NextPageRows<BgAudioDTO>();
        List<BgAudioDTO> returnList = new ArrayList<BgAudioDTO>();

        for (SocialBackgroundAudio bgAudio : pageRows.getRows()) {
            BgAudioDTO bgAudioDTO = buildBgAudioDTO(bgAudio, platform);
            returnList.add(bgAudioDTO);
        }
        returnPageRows.setPage(pageRows.getPage());
        returnPageRows.setRows(returnList);
        return returnPageRows;
    }

    public NextPageRows<ActivityDTO> querySocialActivity(String appId, int platform, NextPagination nextPagination, int startNum) throws ServiceException {
        NextPageRows<SocialActivity> pageRows = ContentServiceSngl.get().querySocialActivityByNext(nextPagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }

        NextPageRows<ActivityDTO> returnPageRows = new NextPageRows<ActivityDTO>();
        List<ActivityDTO> returnList = new ArrayList<ActivityDTO>();

        Map<Integer, List<AppAdvertiseDTO>> adMap = new HashMap<Integer, List<AppAdvertiseDTO>>();
        if (startNum == 0) {
            adMap = queryAdList(appId, AppAdvertisePublishType.ACTIVITY_LIS, platform);
        }

        int i = 0;
        for (SocialActivity activity : pageRows.getRows()) {

            List<AppAdvertiseDTO> advertiseDTOList = adMap.get(i);
            if (!CollectionUtil.isEmpty(advertiseDTOList)) {
                for (AppAdvertiseDTO advertiseDTO : advertiseDTOList) {
                    ActivityDTO advertise = new ActivityDTO();
                    advertise.setType(1);
                    advertise.setAdvertiseid(advertiseDTO.getAdvertiseid());
                    advertise.setTitle(advertiseDTO.getName());
                    advertise.setDescription(advertiseDTO.getDescription());
                    advertise.setIos4pic(advertiseDTO.getIos4pic());
                    advertise.setIos5pic(advertiseDTO.getIos5pic());
                    advertise.setRedirecttype(advertiseDTO.getRedirecttype());
                    advertise.setRurl(advertiseDTO.getRurl());
                    advertise.setIndex(advertiseDTO.getIndex());
                    advertise.setPublishId(advertiseDTO.getPublishId());
                    returnList.add(advertise);
                }

            }

            ActivityDTO activityDTO = buildActivityDTO(platform, activity);
            returnList.add(activityDTO);
            i++;
        }
        returnPageRows.setPage(pageRows.getPage());
        returnPageRows.setRows(returnList);
        return returnPageRows;
    }

    public ActivityDTO getSocialActivity(int platform, long activityId, String appkey) throws ServiceException {
        ActivityDTO activityDTO = null;
        SocialActivity activity = ContentServiceSngl.get().getSocialActivity(activityId);
        if (activity == null) {
            return null;
        }


        activityDTO = buildActivityDTO(platform, activity);

        //查询活动分享模板
        long shareflag = getSocialsharetemplate(appkey, platform, SocialShareType.SOCIAL_ACTIVITY_TYPE, activityId);
        activityDTO.setShareflag(shareflag);

        return activityDTO;
    }

    public NextPageRows<SocialProfileContentDTO> querySocialContentByActivity(String uno, long activityId, int platform, NextPagination nextPagination, String appkey) throws ServiceException {
        NextPageRows<SocialProfileContentDTO> returnObj = new NextPageRows<SocialProfileContentDTO>();
        List<SocialProfileContentDTO> list = new LinkedList<SocialProfileContentDTO>();

        NextPageRows<SocialContentActivity> pageRows = ContentServiceSngl.get().querySocialContentActivityByNext(activityId, nextPagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }

        Set<Long> contentIdSet = new HashSet<Long>();
        Set<String> unoSet = new HashSet<String>();
        for (SocialContentActivity socialContentActivity : pageRows.getRows()) {
            contentIdSet.add(socialContentActivity.getContentId());
            unoSet.add(socialContentActivity.getContentUno());
        }

        Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(contentIdSet);

        Set<Long> activityIdSet = new HashSet<Long>();
        for (Long cid : contentIdSet) {
            if (contentMap.containsKey(cid)) {
                SocialContent socialContent = contentMap.get(cid);
                if (socialContent.getActivityId() > 0L) {
                    activityIdSet.add(socialContent.getActivityId());
                }
            }
        }
        Map<Long, SocialActivity> activityMap = ContentServiceSngl.get().querySocialActivityByIdSet(activityIdSet);

        Map<String, SocialProfile> profileMap = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unoSet);

        Set<Long> agreeContentIdSet = ContentServiceSngl.get().checkSocialContentAction(uno, SocialContentActionType.AGREE, contentIdSet);

        //查询活动分享模板
        long shareflag = 0L;//  list不需要shareflag getSocialsharetemplate(appkey, platform, SocialShareType.SOCIAL_ACTIVITY_TYPE, activityId);

        for (SocialContentActivity socialContentActivity : pageRows.getRows()) {
            SocialContent content = contentMap.get(socialContentActivity.getContentId());
            if (content == null) {
                continue;
            }

            SocialProfileContentDTO dto = new SocialProfileContentDTO();

            ContentDTO contentDTO = buildContentDTO(content, agreeContentIdSet, platform, activityMap, shareflag);
            dto.setContent(contentDTO);

            SocialProfile profile = profileMap.containsKey(content.getUno()) ? profileMap.get(content.getUno()) : null;
            if (profile != null && profile.getBlog() != null && profile.getDetail() != null) {
                ProfileDTO profileDTO = buildProfileDTO(profile, null, content);
                dto.setProfile(profileDTO);
            }
            list.add(dto);
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(list);
        return returnObj;
    }


    /**
     * 获取用户可邀请列表
     *
     * @param friendsInfo
     * @return
     */
    public List<FriendsInfoDto> getInviteFriends(FriendsInfo friendsInfo) {
        List<FriendsInfoDto> friendsInfoDtoList = new ArrayList<FriendsInfoDto>();
        for (FriendsMessage friendsMessage : friendsInfo.getUsers()) {
            FriendsInfoDto friendsInfoDto = new FriendsInfoDto();
            friendsInfoDto.setScreen_name(friendsMessage.getScreen_name());
            friendsInfoDto.setProfile_image_url(friendsMessage.getProfile_image_url());
            friendsInfoDto.setUid(friendsMessage.getId());
            friendsInfoDtoList.add(friendsInfoDto);
        }
        return friendsInfoDtoList;
    }


    public PageRows<SocialMessageDTO> querySocialMessageList(String uno, SocialMessageType socialMessageType, Pagination pagination, boolean desc) throws ServiceException {
        PageRows<SocialMessageDTO> returnPageRows = new PageRows<SocialMessageDTO>();
        List<SocialMessageDTO> dtoList = new ArrayList<SocialMessageDTO>();

        PageRows<SocialMessage> pageRows = MessageServiceSngl.get().querySocialMessageList(uno, socialMessageType, pagination, desc);
        returnPageRows.setPage(pageRows == null ? pagination : pageRows.getPage());
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return returnPageRows;
        }

        Set<String> unoSet = new HashSet<String>();
        Set<Long> contentIdSet = new HashSet<Long>();
        for (SocialMessage message : pageRows.getRows()) {
            if (!StringUtil.isEmpty(message.getSendUno())) {
                unoSet.add(message.getSendUno());
            }
            if (!StringUtil.isEmpty(message.getReceiveUno())) {
                unoSet.add(message.getReceiveUno());
            }
            if (!StringUtil.isEmpty(message.getContentUno())) {
                unoSet.add(message.getContentUno());
            }
            if (!StringUtil.isEmpty(message.getRootUno())) {
                unoSet.add(message.getRootUno());
            }
            if (!StringUtil.isEmpty(message.getParentUno())) {
                unoSet.add(message.getParentUno());
            }
            if (!StringUtil.isEmpty(message.getReplyUno())) {
                unoSet.add(message.getReplyUno());
            }
            if (message.getContentId() > 0l) {
                contentIdSet.add(message.getContentId());
            }
        }

        Map<String, SocialProfile> profileMap = ProfileServiceSngl.get().querySocialProfilesByUnosMap(unoSet);

        //todo
        Map<Long, SocialContent> contentMap = ContentServiceSngl.get().querySocialContentMapByIdSet(contentIdSet);
        if (CollectionUtil.isEmpty(contentMap)) {
            for (SocialMessage message : pageRows.getRows()) {
                MessageServiceSngl.get().removeSocialMessage(message.getMsgId(), message.getOwnUno());
            }
            return returnPageRows;
        }

        for (SocialMessage message : pageRows.getRows()) {
            SocialContent content = contentMap.get(message.getContentId());
            if (content == null) {
                MessageServiceSngl.get().removeSocialMessage(message.getMsgId(), message.getOwnUno());
                continue;
            }

            SocialMessageDTO messageDTO = new SocialMessageDTO();
            if (content != null && content.getPic() != null) {
                messageDTO.setFromuicon(content.getPic().getPic_s());
            }
            messageDTO.setMsgbody(message.getMsgBody());
            messageDTO.setMsgid(message.getMsgId());
            messageDTO.setMsgtype(message.getMsgType().getCode());
            messageDTO.setMsgtime(message.getCreateDate().getTime());

            messageDTO.setCid(message.getContentId());
            messageDTO.setCuno(message.getContentUno());

            messageDTO.setReplyid(message.getReplyId());

            messageDTO.setParentid(message.getParentId());
            messageDTO.setParentuno(message.getParentUno());

            messageDTO.setRootid(message.getRootId());
            messageDTO.setRootuno(message.getRootUno());

            if (message.getMsgType().equals(SocialMessageType.AGREE)) {
                SocialProfile sendProfile = profileMap.containsKey(message.getSendUno()) ? profileMap.get(message.getSendUno()) : null;
                if (sendProfile != null && sendProfile.getBlog() != null && sendProfile.getDetail() != null) {
                    messageDTO.setFromuno(sendProfile.getUno());
                    messageDTO.setFromuname(sendProfile.getBlog().getScreenName());
//                    messageDTO.setFromuicon(ImageURLTag.parseFirstFacesInclude(sendProfile.getBlog().getHeadIconSet(), sendProfile.getDetail().getSex(), "m", true));
                }
            } else if (message.getMsgType().equals(SocialMessageType.REPLY)) {
                SocialProfile replyProfile = profileMap.containsKey(message.getReplyUno()) ? profileMap.get(message.getReplyUno()) : null;
                if (replyProfile != null && replyProfile.getBlog() != null && replyProfile.getDetail() != null) {
                    messageDTO.setFromuno(replyProfile.getUno());
//                    messageDTO.setFromuicon(ImageURLTag.parseFirstFacesInclude(replyProfile.getBlog().getHeadIconSet(), replyProfile.getDetail().getSex(), "m", true));
                    messageDTO.setFromuname(replyProfile.getBlog().getScreenName());
                }

                SocialProfile parentProfile = profileMap.containsKey(message.getParentUno()) ? profileMap.get(message.getParentUno()) : null;
                if (parentProfile != null && parentProfile.getBlog() != null && parentProfile.getDetail() != null) {
                    messageDTO.setParentuname(parentProfile.getBlog().getScreenName());
                }

            }
            dtoList.add(messageDTO);
        }
        returnPageRows.setPage(pageRows.getPage());
        returnPageRows.setRows(dtoList);
        return returnPageRows;
    }



    public long getSocialsharetemplate(String appkey, int platform, SocialShareType socialShareType, Long activityId) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        appkey = this.getAppKey(appkey);
        queryExpress.add(QueryCriterions.eq(SocialShareField.APPKEY, appkey));
        queryExpress.add(QueryCriterions.eq(SocialShareField.PLATFORM, platform));

        List<Long> activityids = new ArrayList<Long>();
        activityids.add(-1L);
        if (socialShareType.equals(SocialShareType.SOCIAL_ACTIVITY_TYPE)) {
            activityids.add(activityId);
            queryExpress.add(QueryCriterions.in(SocialShareField.ACTIVITYID, activityids.toArray()));
        }

        queryExpress.add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, socialShareType.getCode()));
        queryExpress.add(QueryCriterions.eq(SocialShareField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        List<SocialShare> list = JoymeAppServiceSngl.get().querySocialShare(queryExpress, appkey, platform, socialShareType, activityId);
        if (CollectionUtil.isEmpty(list)) {
            return 0L;
        }
        return list.get(0).getUpdate_time_flag().getTime();
    }

    private static String getAppKey(String appKey) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

    public PageRows<MessageListDTO> queryWanbaSocialMessageList(String uid, SocialMessageType socialMessageType, Pagination pagination) throws ServiceException {
        PageRows<MessageListDTO> returnPageRows = new PageRows<MessageListDTO>();
        List<MessageListDTO> dtoList = new ArrayList<MessageListDTO>();

        Profile userprofile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
        if (userprofile == null) {
            return returnPageRows;
        }

        PageRows<SocialMessage> pageRows = MessageServiceSngl.get().queryWanbaSocialMessageList(userprofile.getProfileId(), socialMessageType, pagination);
        returnPageRows.setPage(pageRows == null ? pagination : pageRows.getPage());
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return returnPageRows;
        }

        //用户集合
        Set<String> profielIdSet = new HashSet<String>();
        //帖子集合
        Set<String> commentidSet = new HashSet<String>();
        //圈子集合
        Set<Long> tagidSet = new HashSet<Long>();
        for (SocialMessage message : pageRows.getRows()) {
            if (!StringUtil.isEmpty(message.getSendUno())) {
                profielIdSet.add(message.getOwnUno());
            }
            if (!StringUtil.isEmpty(message.getReceiveUno())) {
                profielIdSet.add(message.getSendUno());
            }
            if (!StringUtil.isEmpty(message.getContentUno())) {
                commentidSet.add(message.getContentUno());
            }
        }

        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profielIdSet);
        Map<String, CommentBean> contentMap = CommentServiceSngl.get().queryCommentBeanByIds(commentidSet);

        Profile profile = null;
        for (SocialMessage message : pageRows.getRows()) {
            CommentBean commentBean = contentMap.get(message.getContentUno());
            profile = profileMap.get(message.getSendUno());
            if (commentBean == null || profile == null) {
                continue;
            }
            MessageListDTO dto = new MessageListDTO();
            dto.setJt(String.valueOf(AppRedirectType.DEFAULT_WEBVIEW.getCode()));
            dto.setJi("http://api." + WebUtil.getDomain() + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + commentBean.getCommentId());
            dto.setCommentid(message.getContentUno());
            dto.setReplyid(String.valueOf(message.getReplyId()));
            dto.setUid(String.valueOf(profile.getUid()));
            dto.setNick(profile.getNick());
            dto.setPicurl(URLUtils.getJoymeDnUrl(ImageURLTag.parseUserCenterHeadIcon(profile.getIcon(), profile.getSex(), "m", true)));
            QueryExpress queryTag = new QueryExpress();
            queryTag.add(QueryCriterions.eq(AnimeTagField.TAG_ID, commentBean.getGroupId()));
            AnimeTag animeTag = JoymeAppServiceSngl.get().getAnimeTag(commentBean.getGroupId(), queryTag);
            if (animeTag != null) {
                dto.setGroupid(String.valueOf(animeTag.getTag_id()));
                dto.setGroupname(String.valueOf(animeTag.getTag_name()));
            }
            dto.setReplydesc(message.getMsgBody());
            dto.setDesc(StringUtil.isEmpty(commentBean.getDescription()) ? WANBA_MESSAGELIST_2_2_0 : commentBean.getDescription());
            dto.setTime(String.valueOf(message.getCreateDate().getTime()));
            dtoList.add(dto);
        }

        returnPageRows.setPage(pageRows.getPage());
        returnPageRows.setRows(dtoList);
        return returnPageRows;
    }

    //玩霸2.2.0评论成功后调用的方法
    public void sendWanbaMessage(CommentBean commentBean, CommentReply commentReply) {
        try {
            //屏蔽自己恢复自己
            if (commentBean.getUri().equals(commentReply.getReplyProfileId())) {
                return;
            }
            SocialMessageEvent event = new SocialMessageEvent();
            event.setOwnUno(commentBean.getUri());
            event.setSendUno(commentReply.getReplyProfileId());
            event.setReceiveUno(commentBean.getUri());
            event.setType(SocialMessageType.WANBA_MESSAGE_LIST.getCode());
            String msgBody = commentBean.getDescription() == null ? WANBA_MESSAGELIST_2_2_0 : commentBean.getDescription();
            event.setMsgBody(StringUtil.isEmpty(msgBody) ? WANBA_MESSAGELIST_2_2_0 : StringUtils.abbreviate(msgBody.trim().replaceAll(" +", " ").replaceAll("\n+", " "), 13));
            event.setContentUno(commentBean.getCommentId());
            event.setReplyId(0l);
            event.setReplyUno("");
            event.setRootId(0l);
            event.setRootUno(commentReply.getBody() == null ? WANBA_MESSAGELIST_2_2_0 : commentReply.getBody().getText());
            event.setParentId(0l);
            event.setParentUno("");
            event.setCreateDate(new Date());
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
