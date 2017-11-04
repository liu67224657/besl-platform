/**
 * 
 */
package com.enjoyf.webapps.joyme.weblogic.comm.mail;

import com.enjoyf.platform.io.mail.MailMessageHTML;
import com.enjoyf.platform.service.mail.EmailServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.template.FreemarkerTemplateGenerator;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 发送邮件
 *
 * @author zhaoxin
 *
 */
@Component
public class MailEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(MailEngine.class);

//    @Resource(name="freemarkerConfig")
//	private FreeMarkerConfigurer freeMarkerConfigurer;
    

//    public void setFreeMarkerConfigurer(
//            FreeMarkerConfigurer freeMarkerConfigurer) {
//        this.freeMarkerConfigurer = freeMarkerConfigurer;
//    }

    /**
     * 通过模板产生邮件正文
     * @param templateName    邮件模板名称
     * @param map            模板中要填充的对象
     * @return 邮件正文（HTML）
     */
    public String generateEmailContent(String templateName, Map map) {
       
    	//使用FreeMaker模板
        try {
//            Configuration configuration =
//            Template t = configuration.getTemplate(templateName);
            return FreemarkerTemplateGenerator.get().generateTemplate(templateName,map);
        } catch (TemplateException e) {
        	logger.error("Error while processing FreeMarker template ", e);
        } catch (FileNotFoundException e) {
            logger.error("Error while open template file ", e);
        } catch (IOException e) {
        	logger.error("Error while generate Email Content ", e);
        }
		return null;
        
    }
//
//    /**
//     * 发送简单邮件
//     * @param msg
//     */
//    public void send(SimpleMailMessage msg) {
//
//    }
//
//
//    /**
//     * 使用模版发送HTML格式的邮件
//     *
//     * @param msg          装有to,from,subject信息的SimpleMailMessage
//     * @param templateName 模版名,模版根路径已在配置文件定义于freemakarengine中
//     * @param model        渲染模版所需的数据
//     * @throws javax.mail.MessagingException
//     */
//    public void send(SimpleMailMessage msg, String templateName, Map model)  {
//
//    }


    /**
     * 使用模版,调用低型发送邮件的逻辑
     *
     * @param mailInfo          装有to,from,subject信息的mailInfo
     * @throws javax.mail.MessagingException
     */
    public boolean sendMailToFW(MailInfo mailInfo)  {

        boolean returnBool = false;
    	//生成html邮件内容
        String content = generateEmailContent(mailInfo.getFtlUrl(), mailInfo.getParamMap());
        MimeMessage mimeMsg = null;

         try {
            //send mail
            MailMessageHTML mailMessageHTML = new MailMessageHTML();
            String fromName = "";
            try {
                fromName = javax.mail.internet.MimeUtility.encodeText(mailInfo.getFromName(), "utf-8","B");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            mailMessageHTML.setFrom(fromName,mailInfo.getFromAdd());
            mailMessageHTML.setTo(mailInfo.getToName()!=null ? mailInfo.getToName()[0]:null,mailInfo.getTo()!=null ? mailInfo.getTo()[0]:null);
            mailMessageHTML.setSubject(mailInfo.getSubject());
            mailMessageHTML.setBody(content);
            EmailServiceSngl.get().send(mailMessageHTML);
        } catch (ServiceException e) {
           logger.error(e.getMessage(),e);
        }
        return returnBool;
    }
}

