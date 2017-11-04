package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class MessageServiceException extends ServiceException {
    public static final MessageServiceException SEND_MESSAGE_REPEAT = new MessageServiceException(BASE_MESSAGE + 1, "message repeat");

    public MessageServiceException(int i, String s) {
        super(i, s);
    }
}
