/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.mail.test;

import com.enjoyf.platform.io.mail.MailMessageText;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.mail.EmailServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Utility;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        MailMessageText mail = new MailMessageText();

        mail.setBody("send from invite account.");
        mail.setFrom("java_server", "java_server@staff.joyme.com");
        for(String mailto:HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getTipsMail()){
            mail.setTo("", mailto);
        }
        mail.setSubject("Test mail from invite.");
        try {
            EmailServiceSngl.get().send(mail);
        } catch (Exception e) {
        }

//        MailMessageText mail2 = new MailMessageText();
//
//        mail2.setBody("send from service account.");
//        mail2.setFrom("Service", "service@joyme.com");
//        mail2.setTo("YPY", "ypy@enjoyfound.com");
//        mail2.setSubject("Test mail from service.");

//        for (int i = 0; i < 10; i++) {
//            try {
//                EmailServiceSngl.get().send(mail);
//                EmailServiceSngl.get().send(mail2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Utility.sleep(10000);
//        }

    }
}
