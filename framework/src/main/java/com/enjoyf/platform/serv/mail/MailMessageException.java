/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * This is an exception thrown when there is a problem with a MailMessage
 */
@SuppressWarnings("serial")
public class MailMessageException extends ServiceException {
	
    // ServiceException code for MailMessageException starts from the base for mail service plus 100.
    protected static final int BASE_MAILMESSAGE = ServiceException.BASE_MAIL + 100;

    public static final int NULL_MAIL_MESSAGE = BASE_MAILMESSAGE + 1;

    public MailMessageException(int code, String msg) {
        super(code, msg);
    }
}
