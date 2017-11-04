/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.mail;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Fivewh.com
 *
 * @author Yin Pengyi
 */
public class EmailServiceSngl {
    private static EmailService service = null;

    public static void set(EmailService service) {
        EmailServiceSngl.service = service;
    }

    public synchronized static EmailService get() {
        if (service == null) {
            create();
        }

        return service;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                MailConstants.SERVICE_SECTION, MailConstants.SERVICE_TYPE
        );

        service = new EmailServiceBeslImpl(cfg);
    }
}
