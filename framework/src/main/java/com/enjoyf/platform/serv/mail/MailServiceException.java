/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * This is an exception thrown when there is a problem with a MailService object.
 */
public class MailServiceException extends ServiceException {
    /**
     * error constants
     */
    public static final MailServiceException JAVA_MAIL_ERROR =
            new MailServiceException(ServiceException.BASE_MAIL + 1, "Java Mail error");

    public static final MailServiceException TRANSPORT_NOT_CONNECTED =
            new MailServiceException(ServiceException.BASE_MAIL + 2, "Not connected to transport");

    public static final MailServiceException TRANSPORT_FAILED =
            new MailServiceException(ServiceException.BASE_MAIL + 3, "IO transport failed");

    public static final MailServiceException PROTOCOL_ERROR =
            new MailServiceException(ServiceException.BASE_MAIL + 4, "Protocol error");

    public static final MailServiceException REQUEST_FAILED =
            new MailServiceException(ServiceException.BASE_MAIL + 5, "Request failed");

    public static final MailServiceException RTM_INVALID_RESPONSE =
            new MailServiceException(ServiceException.BASE_MAIL + 6, "Invalid RTM XML response");

    public static final MailServiceException RTM_TRIGGER_FAILED =
            new MailServiceException(ServiceException.BASE_MAIL + 7, "RTM message trigger failed");

    public MailServiceException(ServiceException err) {
        super(BASE_MAIL, err);
    }

    public MailServiceException(int code, String msg) {
        super(code, msg);
    }

}
