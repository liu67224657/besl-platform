package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ContentServiceException extends ServiceException {
    public static final ContentServiceException BLOG_CONTENT_VIDEO_NOT_APPLY = new ContentServiceException(BASE_CONTENT + 1, "video url not apply");

    public static final ContentServiceException HAVE_NOT_PRIVILAGE_DELETE_CONTENT = new ContentServiceException(BASE_CONTENT + 3, "have not privilage delete conent");

    public static final ContentServiceException HAVE_NOT_PRIVILAGE_DELETE_REPLY = new ContentServiceException(BASE_CONTENT + 4, "have not privilage delete feedback");

    public static final ContentServiceException SHARE_BLOG_ORIGN_NOT_EXISTS = new ContentServiceException(BASE_CONTENT + 5, "share blog orign not exsits");

    public static final ContentServiceException UPLOAD_IMAGE_ILLIGEL_EXCEPTION = new ContentServiceException(BASE_CONTENT + 6, "upload image illigel.");
    public static final ContentServiceException UPLOAD_IMAGE_FILETYPE_ERROR = new ContentServiceException(BASE_CONTENT + 7, "upload image file type not be allow.");
    public static final ContentServiceException UPLOAD_IMAGE_OUT_SIZE = new ContentServiceException(BASE_CONTENT + 8, "upload image file out size.");


    public static final ContentServiceException OAUTH_EXCEPTION = new ContentServiceException(ServiceException.BASE_CONTENT + 9, "oath error");
    public static final ContentServiceException API_CODE_NOT_APPLY = new ContentServiceException(ServiceException.BASE_CONTENT + 10, "syncout code not apply");
    public static final ContentServiceException API_CODE_NOT_SUPPORT_CLASS = new ContentServiceException(ServiceException.BASE_CONTENT + 11, "syncout code not support class");

    public static final ContentServiceException TAG_HAS_EXSITS = new ContentServiceException(ServiceException.BASE_CONTENT + 12, "tag has exists");

    public static final ContentServiceException CONTENT_NOT_EXISTS = new ContentServiceException(BASE_CONTENT + 13, "content not exists");

    public static final ContentServiceException POST_FORIGN_CONTENTREPLY_LIMIT = new ContentServiceException(BASE_CONTENT + 14, "forign content has limit ");


    public static final ContentServiceException UPLOAD_IMAGE_OPEN_IMAEG = new ContentServiceException(BASE_CONTENT + 15, "upload image open  image error.");

    public ContentServiceException(int i, String s) {
        super(i, s);
    }
}
