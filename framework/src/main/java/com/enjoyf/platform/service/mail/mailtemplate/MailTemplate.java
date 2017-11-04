/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.mail.mailtemplate;

import java.util.List;
import java.util.Map;

import com.enjoyf.platform.io.mail.DisplayableEmailAddress;
import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.io.mail.MailMessageHTML;
import com.enjoyf.platform.io.mail.MailMessageText;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class MailTemplate {
    private MailType type;

    //
    private String subject;

    private MailContextType contextType;
    private NamedTemplate bodyTemplate;

    private DisplayableEmailAddress senderAddress;

    //////////////////////////////////////////////////////////////
    public MailTemplate(MailType type) {
        this.type = type;
    }

    public MailType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MailContextType getContextType() {
        return contextType;
    }

    public void setContextType(MailContextType contextType) {
        this.contextType = contextType;
    }

    public NamedTemplate getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(NamedTemplate bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public DisplayableEmailAddress getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(DisplayableEmailAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    public MailMessage generateMailMessage(List<DisplayableEmailAddress> tos, Map<String, String> kvs) {
        MailMessage message = null;

        if (contextType.equals(MailContextType.HTML)) {
            message = new MailMessageHTML();
        } else if (contextType.equals(MailContextType.TEXT)) {
            message = new MailMessageText();
        }

        message.setFrom(senderAddress.getEmailAddress(), senderAddress.getDisplayAddress());
        message.setToList(tos);

        message.setSubject(subject);

        message.setBody(bodyTemplate.format(kvs));

        return message;
    }

    //
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
