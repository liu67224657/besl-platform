package com.enjoyf.webapps.tools.weblogic.content;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterionRelation;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.text.*;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.webapps.tools.weblogic.dto.ContentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午4:38
 * Desc:
 */
@Service(value = "contentWebLogic")
public class ContentWebLogic {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public PageRows<ContentDTO> queryContents(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("QueryExpress: " + queryExpress + "; Pagination : " + pagination);
        }

        PageRows<ContentDTO> returnValue = new PageRows<ContentDTO>();

        //
        Set<String> unosParam = new HashSet<String>();
        //


        PageRows<Content> contentPageRows = ContentServiceSngl.get().queryContentByQueryExpress(queryExpress, pagination);
        //
        for (Content content : contentPageRows.getRows()) {
            //
            ContentDTO returnContentDTO = new ContentDTO();

            //
//            if(!StringUtil.isEmpty(getContentTextByFormatType(content, ContentFormatType.FORMAT_PREVIEW))){
            returnContentDTO.setContent(content);
            returnValue.getRows().add(returnContentDTO);
            unosParam.add(content.getUno());

            if (content.getContentType().hasPhrase() && content.getContentType().hasImage()) {
                content.setContent(content.getContent() + "<br/>[本短文含有图片]");
            }

            if (content.getContentType().hasText() && StringUtil.isEmpty(getContentTextByFormatType(content, ContentFormatType.FORMAT_PREVIEW))
                    && content.getContentType().hasImage()) {
                content.setContent("[本长文只含有图片]");
            }

//            }

        }

        Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unosParam);
        //
        for (ContentDTO entity : returnValue.getRows()) {
            entity.setProfile(profileMap.get(entity.getContent().getUno()));
        }

        returnValue.setPage(contentPageRows.getPage());

        return returnValue;
    }


    public PageRows<ContentDTO> queryContentReplys(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("QueryExpress: " + queryExpress + "; Pagination : " + pagination);
        }

        PageRows<ContentDTO> returnValue = new PageRows<ContentDTO>();

        //
        Set<String> unosParam = new HashSet<String>();
        //


        PageRows<ContentInteraction> replyPageRows = ContentServiceSngl.get().queryContentReply(queryExpress, pagination);
        //
        for (ContentInteraction contentReply : replyPageRows.getRows()) {
            Content content = ContentServiceSngl.get().getContentById(contentReply.getContentId());
            if (content == null) {
                continue;
            }

            //
            ContentDTO returnContentDTO = new ContentDTO();

            //
            returnContentDTO.setContentReply(contentReply);
            returnValue.getRows().add(returnContentDTO);
            unosParam.add(contentReply.getInteractionUno());

            if (content.getContentType().hasPhrase() && content.getContentType().hasImage()) {
                content.setContent(content.getContent() + "<br/>[本短文含有图片]");
            }
            String contentBody = content.getContent();
            if (!StringUtil.isEmpty(contentBody) && contentBody.length() > 50) {
                content.setContent(contentBody.substring(0, 50) + "... ...");
            }
            returnContentDTO.setContent(content);

            ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByUno(contentReply.getContentUno());
            Profile profile = new Profile(contentReply.getContentUno());
            profile.setBlog(profileBlog);
            returnContentDTO.setAuthorProfile(profile);

        }

        Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unosParam);
        //
        for (ContentDTO entity : returnValue.getRows()) {
            entity.setProfile(profileMap.get(entity.getContentReply().getInteractionUno()));
        }

        returnValue.setPage(replyPageRows.getPage());

        return returnValue;
    }


    public PageRows<ContentDTO> queryContentImages(QueryExpress queryExpress, Pagination pagination, Integer auditStatus) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("QueryExpress: " + queryExpress + "; Pagination : " + pagination);
        }

        PageRows<ContentDTO> returnValue = new PageRows<ContentDTO>();
        Set<String> unos = new HashSet<String>();
        //如果查看的是：已通过的图片
        if (auditStatus.equals(ContentAuditStatus.AUDIT_IMG)) {
            queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.NE, ContentAuditStatus.ILLEGAL_IMG, ContentAuditStatus.ILLEGAL_IMG));
        }
        int Deleted = ContentAuditStatus.AUDIT_IMG + ContentAuditStatus.ILLEGAL_IMG;

        PageRows<Content> contents = ContentServiceSngl.get().queryContentByQueryExpress(queryExpress, pagination);

        for (Content content : contents.getRows()) {
            ContentDTO returnContentDTO = new ContentDTO();
            //如果查看的是：已删除的图片
            if (auditStatus.equals(Deleted)) {
                Iterator<ImageContent> it = content.getImages().getImages().iterator();
                int count = 0;
                while (it.hasNext()) {
                    if (!it.next().getValidStatus()) {
                        break;
                    } else {
                        count++;
                    }
                }
                if (count == content.getImages().getImages().size()) {
                    continue;
                }
            }

            returnContentDTO.setContent(content);
            returnValue.getRows().add(returnContentDTO);

            unos.add(content.getUno());
        }

        Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unos);

        //
        for (ContentDTO entity : returnValue.getRows()) {
            entity.setProfile(profileMap.get(entity.getContent().getUno()));
        }

        returnValue.setPage(contents.getPage());

        return returnValue;
    }


    public boolean modifyReplyAuditStatus(String replyId, String uno, String contentId, Map<ObjectField, Object> fieldObjectMap) {

        try {
//            profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);

            return ContentServiceSngl.get().auditContentReply(replyId, uno, contentId, fieldObjectMap);

        } catch (ServiceException e) {

            GAlerter.lab("update Content AuditStatus caught an Exception:" + e);
        }

        return false;
    }

    public boolean modifyAuditStatus(String contentId, String uno, Map<ObjectField, Object> fieldObjectMap) {

        try {
            ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByUno(uno);

            if (profileBlog != null) {

                return ContentServiceSngl.get().modifyContent(contentId, fieldObjectMap);
            }
        } catch (ServiceException e) {

            GAlerter.lab("update Content AuditStatus caught an Exception:" + e);
        }

        return false;

    }

    public String getUnoByScreenName(String screenName) {
        ProfileBlog profileBlog = null;
        try {
            profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);
        } catch (ServiceException e) {
            GAlerter.lab("when Profile getUnoByScreenName caught an Exception:" + e);
        }
        return (profileBlog != null ? profileBlog.getUno() : null);
    }

    /**
     * modify date: 2012-01-04
     *
     * @param contentId id of the content
     * @param map       ObjectField Map
     * @return ImageContentSet
     * @throws ServiceException
     */
    public void changeIMGStatus(String contentId, String uno, Map<ObjectField, Object> map) throws ServiceException {
        ContentServiceSngl.get().modifyContent(contentId, map);
    }

    //通过文章id，郁闷查询文章DTO
    public PageRows<ContentDTO> queryContentByContentIdDomain(String contentId, String domain, Pagination pagination, String type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("contentId=" + contentId + "; domain=" + domain);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));

        if ("image".equals(type)) {
            return queryContentImages(queryExpress, pagination, 0);
        }

        return queryContents(queryExpress, pagination);

    }

    //通过文章名，域名查找该片文章的回复
    public PageRows<ContentDTO> queryContentReplyByCidDomain(String contentId, String domain, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("contentId=" + contentId + "; domain=" + domain + "Pagination:" + pagination);
        }
        //
        ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));

        //
        if (profileBlog == null) {
            return null;
        } else {
            return queryContentReplys(queryExpress, pagination);
        }
    }

    //通过文章Id，作者名，查找文章实例
    public Content queryContentByIdUno(String contentId, String uno) {
        if (logger.isDebugEnabled()) {
            logger.debug("contentId :" + contentId + "; uno:" + uno);
        }
        try {

            return ContentServiceSngl.get().getContentById(contentId);

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return null;
    }


    //通过不同类型解析
    private String getContentTextByFormatType(Content content, ContentFormatType type) {
        String contentText;
        if (type.equals(ContentFormatType.FORMAT_PREVIEW)) {
            contentText = content.getContentType().hasText() ? processContentByKey(WordProcessorKey.KEY_PRIVIEW_TEXT, content) : processContentByKey(WordProcessorKey.KEY_PRIVIEW_CHAT, content);
        } else if (type.equals(ContentFormatType.FORMAT_TEMPLATE)) {
            contentText = content.getContentType().hasText() ? processContentByKey(WordProcessorKey.KEY_ALL_TEXT_HIDE, content) : processContentByKey(WordProcessorKey.KEY_ALL_CHAT, content);
        } else {
            contentText = content.getContent();
        }
        return contentText;
    }


    private String processContentByKey(WordProcessorKey key, Content content) {
        ResolveContent resolveContent = ResolveContent.transferByContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

}
