package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AskServiceException extends ServiceException {
    public static final AskServiceException USER_COLLECT_EXIST = new AskServiceException(BASE_FAVORITE + 1, "user.collect.exist");

    public AskServiceException(int i, String s) {
        super(i, s);
    }
}
