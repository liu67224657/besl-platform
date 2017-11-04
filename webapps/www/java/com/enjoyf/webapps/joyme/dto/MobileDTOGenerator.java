package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.content.AppsContent;
import com.enjoyf.platform.service.content.AppsContentSet;
import com.enjoyf.platform.service.content.AudioContent;
import com.enjoyf.platform.service.content.AudioContentSet;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.VideoContent;
import com.enjoyf.platform.service.content.VideoContentSet;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.text.ContentFormatType;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-17
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
public class MobileDTOGenerator {

    /**
     * 将Profile变成MobileProfileMiniDTO
     * @param profile
     * @return
     */
    public static MobileProfileMiniDTO buildProfileMiniClientDTO(ProfileBlog profile) {
        MobileProfileMiniDTO returnValue = new MobileProfileMiniDTO();

        // 带http://地址的头像
        String headicon = ImageResolveUtil.genImageByTemplate(profile.getHeadIcon(), ImageSize.IMAGE_SIZE_S);
        returnValue.setIcon(headicon);

        returnValue.setUno(profile.getUno());
        returnValue.setName(profile.getScreenName());

        return returnValue;
    }

    /**
     * 将Content转成MobileContentDTO
     * @param content
     * @param type
     * @return
     */
    public static MobileContentDTO buildMobileContentDTO(Content content, ContentFormatType type) {
        MobileContentDTO mobileContentDTO = new MobileContentDTO();

        // if format
        if (ContentFormatType.FORMAT_PREVIEW.equals(type)) {
            TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_IOS_TIMELINE_PRIVIEW_TEXT);

            ResolveContent resolveContent = ResolveContent.transferByContent(content);
            resolveContent.setContentUno(content.getUno()); // short url 使用

            resolveContent = textProcessor.process(resolveContent);

            mobileContentDTO.setBd(resolveContent.getContent());
            mobileContentDTO.setImg(resolveImagesMap(resolveContent.getImages(), type));

        } else if (ContentFormatType.FORMAT_TEMPLATE.equals(type)) {
            TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_IOS_TIMELINE_TEXT);

            ResolveContent resolveContent = ResolveContent.transferByContent(content);
            resolveContent.setContentUno(content.getUno()); // short url 使用

            resolveContent = textProcessor.process(resolveContent);

            mobileContentDTO.setBd(resolveContent.getContent());
            mobileContentDTO.setImg(resolveImagesMap(resolveContent.getImages(), type));

        } else {
            //
            mobileContentDTO.setBd(content.getContent());
        }


        mobileContentDTO.setCid(content.getContentId());
        mobileContentDTO.setTt(content.getSubject());
        mobileContentDTO.setVd(resolveVideoContentSet(content.getVideos()));


        mobileContentDTO.setPdt(content.getPublishDate().getTime());
        mobileContentDTO.setHot(content.getFavorTimes());

        return mobileContentDTO;

    }

    /**
     * 将Content转成MobileContentDTO
     * @param contentId  文章的id
     * @param contentMap timeline中的文章map，key为文章的id
     * @param type DTO显示的类型：previw or 单页
     * @return
     */
    public static MobileContentDTO buildMobileContentDTO(String contentId,
                                               Map<String, Content> contentMap,
                                               ContentFormatType type) {

        return buildMobileContentDTO(contentMap.get(contentId), type);

    }

    /**
     * 生成timelineDTO
     * @param contentUno
     * @param contentId
     * @param profileMap  这个map可以减少访问数据库此时，否则每次循环都会查出profile
     * @param contentMap  这个map可以减少访问数据库此时，否则每次循环都会查出content
     * @param type
     * @return
     */
    public static MobileLineItemDTO buildMobileLineItemDTO(String contentUno, String contentId,
                                                   Map<String, Profile> profileMap, Map<String, Content> contentMap,
                                                   ContentFormatType type) {
        MobileLineItemDTO returnValue = new MobileLineItemDTO();

        //check the profile and content.
        Profile profile = profileMap.get(contentUno);
        Content content = contentMap.get(contentId);

        if (profile == null || content == null) {
            return null;
        }

        returnValue.setMini(MobileDTOGenerator.buildProfileMiniClientDTO(profile.getBlog()));  //todo

        returnValue.setCt(MobileDTOGenerator.buildMobileContentDTO(contentId, contentMap, type));

        //如果是转贴
        if(ContentPublishType.FORWARD.equals(content.getPublishType())){
            MobileLineItemDTO lineItemDTO = buildMobileLineItemDTO(content.getRootContentUno(), content.getRootContentId(), profileMap, contentMap, ContentFormatType.FORMAT_PREVIEW);

            MobileContentDTO mobileContentDTO = MobileDTOGenerator.buildMobileContentDTO(content.getRootContentId(), contentMap, type);
            Profile rootProfile = null;
            try {
                rootProfile = ProfileServiceSngl.get().getProfileByUno(content.getRootContentUno());
            } catch (ServiceException e) {
                GAlerter.lab("get a Profile by uno occurred exception:", e);
            }

            if (mobileContentDTO != null && rootProfile != null) {
                lineItemDTO.setMini(MobileDTOGenerator.buildProfileMiniClientDTO(rootProfile.getBlog()));
                lineItemDTO.setCt(mobileContentDTO);
            }

            returnValue.setRoot(lineItemDTO);
        }

        return returnValue;
    }

    /**
     * 构建单篇文章DTO的方法
     * @param type
     * @return
     */
    public static MobileLineItemDTO buildMobileLineItemDTO(String uno, String contentId, ContentFormatType type){
        MobileLineItemDTO returnValue = new MobileLineItemDTO();

        //check the profile and content.
        ProfileBlog profile = null;
        Content content = null;
        try {

            profile = ProfileServiceSngl.get().getProfileBlogByUno(uno);
            content = ContentServiceSngl.get().getContentById(contentId);

        } catch (ServiceException e) {
            GAlerter.lab("get a profileBlog or content caught an exception: ", e);
        }

        if (profile == null || content == null) {
            return null;
        }

        returnValue.setMini(MobileDTOGenerator.buildProfileMiniClientDTO(profile));  //todo

        returnValue.setCt(MobileDTOGenerator.buildMobileContentDTO(content, type));

        //如果是转贴
        if(ContentPublishType.FORWARD.equals(content.getPublishType())){
            MobileLineItemDTO lineItemDTO = buildMobileLineItemDTO(content.getRootContentUno(), content.getRootContentId(), ContentFormatType.FORMAT_PREVIEW);
            Content rootContent = null;
            try {
                rootContent = ContentServiceSngl.get().getContentById(content.getRootContentId());

                MobileContentDTO mobileContentDTO = MobileDTOGenerator.buildMobileContentDTO(rootContent, type);


                ProfileBlog rootProfile = null;
                try {
                    rootProfile = ProfileServiceSngl.get().getProfileBlogByUno(content.getRootContentUno());
                } catch (ServiceException e) {
                    GAlerter.lab("get a Profile by uno occurred exception:", e);
                }

                if (mobileContentDTO != null && rootProfile != null) {
                    lineItemDTO.setMini(MobileDTOGenerator.buildProfileMiniClientDTO(rootProfile));
                    lineItemDTO.setCt(mobileContentDTO);
                }

                returnValue.setRoot(lineItemDTO);

            } catch (ServiceException e) {
                GAlerter.lab("get a root content caught an exception:", e);
            }
        }

        return returnValue;
    }


    /*******--------------------------以下是私有方法------------------------**********/

    /**
     * 如果是preview类型，选择ss类型的图，其余选择url中M图
     * @param images
     * @param contentFormatType
     * @return
     */
    private static ImageClientSet resolveImagesMap(Map<Integer, ImageContent> images, ContentFormatType contentFormatType) {
        ImageClientSet returnValue = new ImageClientSet();

        if(!images.isEmpty()){
            for(ImageContent imageContent : images.values()){

                MediaDTO imageDTO = new MediaDTO();
                if(contentFormatType.equals(ContentFormatType.FORMAT_PREVIEW)){

                    imageDTO.setUrl(ImageURLTag.parseWallThumbImgLink(imageContent.getSs()));
                } else {

                    imageDTO.setUrl(ImageURLTag.parseWallThumbImgLink(imageContent.getUrl()));
                }

                returnValue.add(imageDTO);

            }
            returnValue.setTotal(images.size());

            return returnValue;
        }
        return null;//如果为空不返回，所以返回null
    }


    //处理视频
    private static MediaDTO resolveVideoContentSet(VideoContentSet videoContentSet) {
        MediaDTO returnValue = null;

        if(videoContentSet.getVideos().size() != 0){
            returnValue = new MediaDTO();
            for(VideoContent videoContent : videoContentSet.getVideos()){

                returnValue.setThumb(videoContent.getUrl()); //封面图的地址
                returnValue.setUrl(videoContent.getOrgUrl()); //网页地址，不是swf地址

                break;
            }
        }

        return  returnValue;
    }

    //处理app
    private static MediaDTO resolvApp(AppsContentSet appsContentSet) {
        MediaDTO returnValue = null;

        if(appsContentSet.getApps().size() != 0){
            returnValue = new MediaDTO();
            for(AppsContent appsContent : appsContentSet.getApps()){

                returnValue.setThumb(appsContent.getAppSrc());
                returnValue.setUrl(appsContent.getResourceUrl());

                break;
            }
        }

        return returnValue;
    }
    //处理音频
    private static MediaDTO resolveAudioContentSet(AudioContentSet audioContentSet) {
        MediaDTO returnValue = null;

        if(audioContentSet.getAudios().size() != 0){
            returnValue = new MediaDTO();

            for(AudioContent audioContent : audioContentSet.getAudios()){

                returnValue.setThumb(audioContent.getM());
                returnValue.setUrl(audioContent.getFlashUrl());//todo

                break;
            }
        }

        return  returnValue;
    }

}
