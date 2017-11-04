package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:Email Auth Service Exception
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class VoteServiceException extends ServiceException {

    public static final VoteServiceException VOTE_HAS_EXISTS = new VoteServiceException(BASE_VOTE + 1, "vote has exists");

    public VoteServiceException(int i, String s) {
        super(i, s);
    }
}
