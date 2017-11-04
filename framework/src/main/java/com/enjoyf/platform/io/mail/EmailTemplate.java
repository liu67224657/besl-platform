/**
 * (c) 2008 Fivewh.com
 */
package com.enjoyf.platform.io.mail;

import java.util.HashMap;
import java.util.Map;

import com.enjoyf.platform.props.I18nMessageProperties;
import com.enjoyf.platform.props.I18nServiceType;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class EmailTemplate {
    private static Map<String, NamedTemplate> emailTemplates = new HashMap<String, NamedTemplate>();
    private static Map<String, String> emailTexts = new HashMap<String, String>();

    private static EmailTemplate instance = new EmailTemplate();

    public static EmailTemplate get() {
        return instance;
    }

    public NamedTemplate getNamedTemplate(String name) {
        NamedTemplate template = emailTemplates.get(name);

        if (template == null) {
            I18nMessageProperties templateProps = I18nMessageProperties.forService(I18nServiceType.MAIL_TEMPLATE, null);
            template = NamedTemplate.parse(templateProps.get(name));

            if (template != null) {
                emailTemplates.put(name, template);
            } else {
                GAlerter.lab("There isn't the email template for " + name);
            }
        }

        return template;
    }

    public String getText(String name) {
        String text = emailTexts.get(name);

        if (text == null) {
            I18nMessageProperties templateProps = I18nMessageProperties.forService(I18nServiceType.MAIL_TEMPLATE, null);
            text = templateProps.get(name);

            if (text != null) {
                emailTexts.put(name, text);
            } else {
                GAlerter.lab("There isn't the email text for " + name);
                text = "";
            }
        }

        return text;
    }
}
