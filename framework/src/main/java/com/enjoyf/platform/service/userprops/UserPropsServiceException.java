/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropsServiceException extends ServiceException {
    public static final UserPropsServiceException NO_DB_HANDLER =
            new UserPropsServiceException(ServiceException.BASE_USERPROPS + 1, "No Database Handler for the domain.");

    public static final UserPropsServiceException INCREASE_FAILED =
            new UserPropsServiceException(ServiceException.BASE_USERPROPS + 2, "Increase user props failed.");

    public UserPropsServiceException(ServiceException e) {
        super(e);
    }

    public UserPropsServiceException(int val, String name) {
        super(val, name);
    }
}

