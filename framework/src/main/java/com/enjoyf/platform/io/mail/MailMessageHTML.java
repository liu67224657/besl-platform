/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

/**
 * This class is for creating HTML mail messages.  An instance of this
 * class will be one message.
 *
 * @author Yin Pengyi
 */
public class MailMessageHTML extends MailMessageSMTP {
    protected String bodyHTML = null;     // mail contents (html)
    protected String bodyText = null;     // mail contents (text)

    /**
     * default constructor
     */
    public MailMessageHTML() {
    }

    /**
     * Set the body of the message.
     */
    public void setBody(String html) {
        bodyHTML = html;
    }

    /**
     * Set the text part of the message body.  This text will only be
     * displayed if the mail reader does not support html
     */
    public void setText(String text) {
        bodyText = text;
    }

    /**
     * Get the message body.
     */
    public String getBody() {
        return bodyHTML;
    }

    /**
     * Get the text part of the message body.
     */
    public String getText() {
        return bodyText;
    }

    public void accept(MailMessageVisitor v) {
        v.visitMessageHTML(this);
    }
}