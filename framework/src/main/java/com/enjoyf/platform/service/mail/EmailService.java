/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.mail;

import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
public interface EmailService {

    /**
     * Send an email message.
     */
    public void send(MailMessage message) throws ServiceException;

    /**
     * Set the send rate. This method should not normally be used, it
     * is for debug/recovery purposes.
     */
    public void setSendRate(int rate) throws ServiceException;
}
