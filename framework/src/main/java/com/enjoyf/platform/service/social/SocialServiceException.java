package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SocialServiceException extends ServiceException {
    public static final SocialServiceException USERTYPE_HAS_EXISTS = new SocialServiceException(BASE_SOCIAL + 1, "usertype has exists");

    public static final SocialServiceException USERFOCUSTYPE_HAS_EXISTS = new SocialServiceException(BASE_SOCIAL + 2, "userfocustype has exists");

    public static final SocialServiceException FOCUS_IS_SAME_USER=new SocialServiceException(BASE_SOCIAL + 3, "focus is same user");
    public SocialServiceException(int i, String s) {
        super(i, s);
    }
}
