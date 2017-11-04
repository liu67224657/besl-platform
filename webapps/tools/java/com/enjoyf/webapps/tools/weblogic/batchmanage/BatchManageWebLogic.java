package com.enjoyf.webapps.tools.weblogic.batchmanage;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.log.LogWebLogic;
import com.enjoyf.webapps.tools.weblogic.mail.MailEngine;
import com.enjoyf.webapps.tools.weblogic.mail.MailInfo;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: lijing
 * Date: 12-8-16
 * Time: 下午1:06
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "batchManageWebLogic")
public class BatchManageWebLogic {
    @Resource(name = "logWebLogic")
    private LogWebLogic logWebLogic;

    @Resource(name = "mailEngine")
    private MailEngine mailEngine;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private TemplateHotdeployConfig config = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    public Boolean startSendThread(String[] userNames, String[] priceCodes, String noticeRadio, String notice, String sendUno, String email, String emailName, String userid, String ip) {
        new SendThread(userNames, priceCodes, noticeRadio, notice, sendUno, email, emailName, userid, ip).start();
        return true;
    }

    class SendThread extends Thread {
        private String[] userNames;
        private String[] priceCodes;
        private String noticeRadio;
        private String notice;
        private String sendUno;
        private String email;
        private String userid;
        private String ip;
        private String emailName;

        public SendThread(String[] userNames, String[] priceCodes, String noticeRadio, String notice, String sendUno, String email, String emailName, String userid, String ip) {
            this.userNames = userNames;
            this.priceCodes = priceCodes;
            this.noticeRadio = noticeRadio;
            this.notice = notice;
            this.sendUno = sendUno;
            this.email = email;
            this.userid = userid;
            this.ip = ip;
            this.emailName = emailName;
        }

        public void run() {
            //得到接收方UNO
            String[] receiveUno = new String[userNames.length];
            for (int i = 0; i < userNames.length; i++) {
                try {
                    if (priceCodes == null) {
                        priceCodes[i] = "";
                    }
                    receiveUno[i] = ProfileServiceSngl.get().getProfileBlogByScreenName(userNames[i]).getUno();
                } catch (Exception e) {
                    receiveUno[i] = "";
                }
            }

            //打印log日志头部
            ToolsLog log = new ToolsLog();
            log.setOpUserId(userid);
            log.setOperType(LogOperType.BATCHMANAGE_BATCHCODE);
            log.setOpTime(new Date());
            log.setOpIp(ip);
            log.setDescription(noticeRadio + i18nSource.getMessage("batchmanage.batchcode.result", null, Locale.CANADA));
            log.setOpBefore("");

            List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();//保存发码结果
            //循环发码
            int successPerson = 0, failPerson = 0;
            for (int i = 0; i < userNames.length; i++) {
                //存放当前用户名单及奖品码
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("text1", userNames[i]);
                paramMap.put("text2", priceCodes[i]);
                //存放当前消息信息
                Message message = new Message();
                message.setSendDate(new Date());
                message.setBody(NamedTemplate.parse(notice).format(paramMap));
                message.setOwnUno(receiveUno[i]); //消息所有者
                message.setRecieverUno(receiveUno[i]);
                if (sendUno != "") {
                    message.setMsgType(MessageType.PRIVATE); //发送类型-私信
                    message.setSenderUno(sendUno);
                } else {
                    message.setMsgType(MessageType.OPERATION); //发送类型-系统消息
                }
                //存放当前用户发码信息结果
                Map<String, String> resultMap = new HashMap<String, String>();
                resultMap.put("username", userNames[i]);
                resultMap.put("pricecode", priceCodes[i]);

                if (!StringUtil.isEmpty(receiveUno[i])) {
                    try {
                        //发送消息并保存发送结果
                        MessageServiceSngl.get().postMessage(message.getOwnUno(), message);
                        resultMap.put("sendresult", i18nSource.getMessage("batchmanage.success", null, Locale.CANADA));
                        successPerson++;
                        log.setOpAfter(userNames[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                priceCodes[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                i18nSource.getMessage("batchmanage.success", null, Locale.CANADA));
                    } catch (ServiceException e) {
                        resultMap.put("sendresult", i18nSource.getMessage("error.batchmanage.service.error", null, Locale.CANADA));
                        failPerson++;
                        log.setOpAfter(userNames[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                priceCodes[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                i18nSource.getMessage("error.batchmanage.service", null, Locale.CANADA));
                    } catch (Exception e) {
                        resultMap.put("sendresult", i18nSource.getMessage("error.batchmanage.fail", null, Locale.CANADA));
                        failPerson++;
                        log.setOpAfter(userNames[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                priceCodes[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                i18nSource.getMessage("error.batchmanage.fail", null, Locale.CANADA));
                    }
                    resultList.add(resultMap);
                    try {
                        logWebLogic.saveLog(log);
                    } catch (Exception e) {
                        GAlerter.lab(this.getClass().getName() + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                userNames[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                                priceCodes[i] + "|error| save log Exception.e:", e);
                    }
                    continue;
                }

                resultMap.put("sendresult", i18nSource.getMessage("error.batchmanage.name", null, Locale.CANADA));
                resultList.add(resultMap);
                failPerson++;
                log.setOpAfter(userNames[i] + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) + priceCodes[i] +
                        i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) +
                        i18nSource.getMessage("error.batchmanage.name", null, Locale.CANADA));
                try {
                    logWebLogic.saveLog(log);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) + userNames[i] +
                            i18nSource.getMessage("batchmanage.batchcode.space", null, Locale.CANADA) + priceCodes[i] + "|error|" + " save log Exception.e:", e);
                }
            }

            //生成邮件发送模板发送到指定邮件
            String[] emailNames = email.split(",");
            try {
                MailInfo mailInfo = new MailInfo();
                Map<String, Object> mailParamMap = new HashMap<String, Object>();
                mailParamMap.put("senddate", DateUtil.getCurrentDateTime(DateUtil.DEFAULT_DATE_FORMAT2));
                mailParamMap.put("sumperson", resultList.size());
                mailParamMap.put("successperson", successPerson);
                mailParamMap.put("failperson", failPerson);
                mailParamMap.put("resultlist", resultList);
                if(userNames.length<priceCodes.length){
                    mailParamMap.put("sendcode", false);
                }else {
                    mailParamMap.put("sendcode", true);
                }
                mailInfo.setParamMap(mailParamMap);
                mailInfo.setFromName(i18nSource.getMessage("batchmanage.batchcode.title", null, Locale.CANADA));
                mailInfo.setTo(emailNames);
                mailInfo.setFromAdd("service@joyme.com");
                mailInfo.setFtlUrl(config.getMailBatchCodeFtlUri());
                mailInfo.setSubject(emailName);
                mailEngine.sendMailToFW(mailInfo);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " sendMailToFW Exception.e:", e);
            }

        }

    }

}
