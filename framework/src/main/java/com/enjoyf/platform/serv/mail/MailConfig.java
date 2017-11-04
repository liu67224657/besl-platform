/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.io.mail.DisplayableEmailAddress;
import com.enjoyf.platform.service.mail.MailConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to configure the server. It is typically a specific
 * class for the server.
 */

class MailConfig {
    /**
     * The send delay in msecs. Ie, we will wait 'sendDelay' msecs in
     * between sending each msg to the mail server.
     */
    private int emailSendDelay = 500;

    private String queueDiskStorePath;
    private int numberOfSendThreads = 5;

    private String statsFileName;
    private int statsNumPeriods;
    private int statsPeriod;
    private int statsReportPeriod;
    private List<DisplayableEmailAddress> statsReportUsers = new ArrayList<DisplayableEmailAddress>();
    private int statsStartTime;

    ////////////////////////////////////////////////
    //The multi sender smtp configure
    ////////////////////////////////////////////////
    private Map<String, JavaMailServiceConfig> mailServiceConfigMap = new HashMap<String, JavaMailServiceConfig>();
    private String defaultMailSenderAddress = "default@mail.com";

    private static final String KEY_SMTP_SENDER_ADDRESSES = "smtp.sender.addresses";
    private static final String KEY_SMTP_SENDER_DEFAULT = "smtp.sender.default";

    private static final String SUFFIX_KEY_HOSTNAME = ".smtp.hostname";
    private static final String SUFFIX_KEY_AUTH_NEEDED = ".smtp.auth.needed";
    private static final String SUFFIX_KEY_AUTH_USER = ".smtp.auth.user";
    private static final String SUFFIX_KEY_AUTH_PWD = ".smtp.auth.pwd";

    /**
     * This method will construct the object from the passed in
     * props object.
     */
    MailConfig(FiveProps props) {
        init();

        int ival = props.getInt("server.sendDelay");
        if (ival != 0) {
            emailSendDelay = ival;
        }

        if (emailSendDelay > 5000) {
            emailSendDelay = 5000;
            GAlerter.lab("MailServer. SendDelay is set to more than 5 secs!");
        }

        queueDiskStorePath = props.get(MailConstants.SERVICE_PREFIX + ".NAME");

        ival = props.getInt("server.numberOfSendThreads");
        if (ival != 0) {
            numberOfSendThreads = ival;
        }

        //////////////////////////////////////////////
        //start to read the sender smtps
        List<String> senderAddresses = props.getList(KEY_SMTP_SENDER_ADDRESSES);

        for (String senderAddress : senderAddresses) {
            JavaMailServiceConfig mailServiceConfig = new JavaMailServiceConfig(senderAddress.toLowerCase());

            mailServiceConfig.setSmtpHostname(props.get(senderAddress + SUFFIX_KEY_HOSTNAME));
            mailServiceConfig.setNeedAuth(props.getBoolean(senderAddress + SUFFIX_KEY_AUTH_NEEDED, false));
            mailServiceConfig.setAuthUser(props.get(senderAddress + SUFFIX_KEY_AUTH_USER));
            mailServiceConfig.setAuthPwd(props.get(senderAddress + SUFFIX_KEY_AUTH_PWD));

            //
            mailServiceConfigMap.put(mailServiceConfig.getSenderMailAddress(), mailServiceConfig);
        }

        defaultMailSenderAddress = props.get(KEY_SMTP_SENDER_DEFAULT, defaultMailSenderAddress);


        String sval = props.get("server.statsFileName");
        if (sval != null) {
            statsFileName = sval;
        }

        ival = props.getInt("server.statsNumPeriods");
        if (ival != 0) {
            statsNumPeriods = ival;
        }

        ival = props.getInt("server.statsPeriod");
        if (ival != 0) {
            statsPeriod = ival * 60 * 1000;
        }

        ival = props.getInt("server.statsReportPeriod");
        if (ival != 0) {
            statsReportPeriod = ival * 3600 * 1000;
        }

        ival = props.getInt("server.statsStartTime");
        if (ival != 0) {
            statsStartTime = ival;
        }

        List<String> reportEmails = props.getList("server.statsReportUsers");
        if (sval != null) {
            for (String email : reportEmails) {
                statsReportUsers.add(new DisplayableEmailAddress(email));
            }
        }
    }

    void setSendDelay(int sendDelay) {
        emailSendDelay = sendDelay;
    }

    int getSendDelay() {
        return emailSendDelay;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    int getNumberOfSendThreads() {
        return numberOfSendThreads;
    }

    void setNumberOfSendThreads(int numberOfSendThreads) {
        this.numberOfSendThreads = numberOfSendThreads;
    }

    public String getStatsFileName() {
        return statsFileName;
    }

    public int getStatsNumPeriods() {
        return statsNumPeriods;
    }

    public int getStatsPeriod() {
        return statsPeriod;
    }

    public int getStatsReportPeriod() {
        return statsReportPeriod;
    }

    public List<DisplayableEmailAddress> getStatsReportUsers() {
        return statsReportUsers;
    }

    public int getStatsStartTime() {
        return statsStartTime;
    }

    public JavaMailServiceConfig getDefaultJavaMailServiceConfig() {
        return mailServiceConfigMap.get(defaultMailSenderAddress);
    }

    public Map<String, JavaMailServiceConfig> getMailServiceConfigMap() {
        return mailServiceConfigMap;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    /**
     * Put default settings here.
     */
    private void init() {
        emailSendDelay = 500;
        numberOfSendThreads = 5;
        statsFileName = "stats.dat";
        statsNumPeriods = 6;
        statsPeriod = 2 * 3600 * 1000;
        statsReportPeriod = 12 * 3600 * 1000;
        statsReportUsers = new ArrayList<DisplayableEmailAddress>();
//        statsReportUsers.add(new DisplayableEmailAddress("yinpy@platform.com"));
        statsStartTime = 0;
    }
}
