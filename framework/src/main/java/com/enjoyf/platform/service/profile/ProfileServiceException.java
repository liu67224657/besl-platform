package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:Email Auth Service Exception
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class ProfileServiceException extends ServiceException {

    public static final ProfileServiceException CREATE_BY_GENDOMAIN_REPEAT = new ProfileServiceException(BASE_PROFILE + 1, "create profile gen domain repaeat");


    public ProfileServiceException(int i, String s) {
        super(i, s);
    }
}
