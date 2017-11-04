/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.mail.mailtemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.enjoyf.platform.io.mail.DisplayableEmailAddress;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.HotdeployFile;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class MailTemplateManager {
    private static MailTemplateManager instance;

    // configure file relative keys
    // ////////////////////////////////////////////////////////////////////
    private String mailTemplateConfigDir = "/hotdeploy/props/mail/template/";

    private static final String DEFAULT_FILE_NAME = "def.properties";
    private static final String FILE_EXT = ".properties";

    //
    private Map<MailType, MailTemplate> mailTemplateMap = new HashMap<MailType, MailTemplate>();

    //
    private static final String KEY_MAIL_TEMPLATE_CODE_LIST = "mail.template.code.list";

    private static final String KEY_MAIL_TEMPLATE_SUBJECT = "mail.subject";
    private static final String KEY_MAIL_TEMPLATE_BODY = "mail.body";
    private static final String KEY_MAIL_TEMPLATE_CONTEXT_TYPE = "mail.context.type";
    private static final String KEY_MAIL_TEMPLATE_SENDER_NAME = "mail.sender.name";
    private static final String KEY_MAIL_TEMPLATE_SENDER_MAIL = "mail.sender.mail";


    //the file
    private Map<String, HotdeployFile> hotdeployFiles = new HashMap<String, HotdeployFile>();

    private MailTemplateManager() {
        mailTemplateConfigDir = EnvConfig.get().getMailTemplateConfigHotdeployDir();

        init();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MailTemplateConfigReloadTask(), EnvConfig.get().getHotdeployCheckIntervalMsec(), EnvConfig.get().getHotdeployCheckIntervalMsec());

    }

    public static synchronized MailTemplateManager get() {
        if (instance == null) {
            instance = new MailTemplateManager();
        }

        return instance;
    }

    private synchronized void clear() {
        mailTemplateMap.clear();
    }

    // initialize all currentTableId from currentTableId information
    private void init() {

        //load the default configure files.
        String defaultPropsFile = mailTemplateConfigDir + DEFAULT_FILE_NAME;

        //load the root attributes
        FiveProps props = new FiveProps(defaultPropsFile);
        hotdeployFiles.put(defaultPropsFile, new HotdeployFile(defaultPropsFile));

        List<String> vals = props.getList(KEY_MAIL_TEMPLATE_CODE_LIST);
        for (String val : vals) {
            MailType type = MailType.getByCode(val);

            if (type != null) {
                loadMailTemplate(type);
            } else {
                GAlerter.lab("There was a configure error in the mail template configure file, type:" + type);
            }
        }

    }

    private void loadMailTemplate(MailType mailType) {

        //the hotdeploy file.
        String templateFile = mailTemplateConfigDir + mailType.getCode() + FILE_EXT;
        hotdeployFiles.put(templateFile, new HotdeployFile(templateFile));

        //load the props file.
        FiveProps props = new FiveProps(templateFile);

        MailTemplate template = new MailTemplate(mailType);

        //
        template.setSubject(props.get(KEY_MAIL_TEMPLATE_SUBJECT));

        //
        DisplayableEmailAddress address = new DisplayableEmailAddress(props.get(KEY_MAIL_TEMPLATE_SENDER_MAIL), props.get(KEY_MAIL_TEMPLATE_SENDER_NAME));
        template.setSenderAddress(address);

        //
        template.setContextType(MailContextType.getByCode(props.get(KEY_MAIL_TEMPLATE_CONTEXT_TYPE)));

        //
        NamedTemplate nt = NamedTemplate.parse(props.get(KEY_MAIL_TEMPLATE_BODY));
        template.setBodyTemplate(nt);

        //
        mailTemplateMap.put(mailType, template);

    }

    public MailTemplate getMailTemplate(MailType type) {
        return mailTemplateMap.get(type);
    }

    class MailTemplateConfigReloadTask extends TimerTask {

        public void run() {

            boolean modified = false;

            for (HotdeployFile file : hotdeployFiles.values()) {
                modified = modified || file.isModified();
            }

            //reload the files.
            if (modified) {

                clear();
                init();
            }
        }
    }
}
