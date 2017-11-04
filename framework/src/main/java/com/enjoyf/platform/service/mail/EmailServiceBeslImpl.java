/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.mail;

import java.util.List;

import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.mail.DisplayableEmailAddress;
import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
public class EmailServiceBeslImpl implements EmailService {
    private ReqProcessor reqProcessor;

    /**
     * Ctor the object using the passed in config object.
     */
    public EmailServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("MailService.ctor: cfg is null!");
        }

        if (!scfg.isValid()) {
            throw new RuntimeException("MailService.ctor: cfg is invalid!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    /**
     * Send an email message.
     */
    public void send(MailMessage message) throws ServiceException {
        if (message == null) {
            throw new IllegalArgumentException("EmailService.send: message is null!!");
        }

        List<DisplayableEmailAddress> toAddr = message.getToList();
        //--
        // Quietly ignore bad addresses.
        //--
        if (toAddr == null || toAddr.size() == 0) {
            return;
        }

        WPacket wp = new WPacket();

        wp.writeSerializable(message);
        Request req = new Request(MailConstants.SEND, wp);

        reqProcessor.process(req);
    }

    /**
     * Set the send rate. This method should not normally be used, it
     * is for debug/recovery purposes.
     */
    public void setSendRate(int rate) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeIntNx(rate);

        Request req = new Request(MailConstants.SET_DELAY, wp);

        reqProcessor.process(req);
    }
}