/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

/**
 * This class is for creating text mail messages.  An instance of this
 * class will be one message.  This uses java mail.
 *
 * @author Yin Pengyi
 */
public class MailMessageText extends MailMessageSMTP {
    protected String bodyText = null;

    /**
     * default constructor
     */
    public MailMessageText() {
    }

    /**
     * Set the body of the message.
     */
    public void setBody(String text) {
        bodyText = text;
    }

    /**
     * Get the body of the message.
     */
    public String getBody() {
        return bodyText;
    }

    public void accept(MailMessageVisitor v) {
        v.visitMessageText(this);
    }
}