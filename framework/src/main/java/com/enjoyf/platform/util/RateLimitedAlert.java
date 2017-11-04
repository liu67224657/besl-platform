package com.enjoyf.platform.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.Alert;
import com.enjoyf.platform.util.log.AlertType;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * RateLimitedAlert is a convenience class that provides a
 * rate limit on alerts.  Sometimes we want to know if a condition
 * ever occurs, but not be overwhelmed by a flood of messages
 * while the condition exists. <p>
 * <p/>
 * The class remembers the time of the last alert via this object,
 * and converts new alerts that come in within the
 * next interval to local log messages.  Thus it is appropriate
 * for general warning messages but not for specific instances.
 */

public class RateLimitedAlert {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitedAlert.class);
    // Time of the last message that was alerted.
    private long lastMessageTime;

    // Rate interval in millis
    private long rateMillis;
    private AlertType alertType;

    /**
     * Constructor takes the minimum time between messages. Alerts
     * are sent to the NOISE bucket by default.
     *
     * @param rateSecs gap between messages if being spammed
     */
    public RateLimitedAlert(int rateSecs) {
        this(rateSecs, AlertType.NOISE);
    }

    /**
     * Constructor takes the minimum time between messages. Alerts
     * are sent to the passed in alert bucket.
     *
     * @param rateSecs gap between messages if being spammed
     * @param type     The type of the alert bucket to send the msg to.
     */
    public RateLimitedAlert(int rateSecs, AlertType type) {
        rateMillis = rateSecs * 1000;
        alertType = type;
    }

    /**
     * Send an alert *if* we have exceed the threshold.
     *
     * @param msg       The message to alert.
     * @param alertType The type of alert we are sending.
     */
    public void alert(String msg, AlertType alertType) {
        long now = System.currentTimeMillis();
        if (now - lastMessageTime < rateMillis) {
            logger.warn("[alert suppressed] " + msg);
            return;
        }

        lastMessageTime = now;
        GAlerter.alert(new Alert(alertType, msg));
    }

    /**
     * Send the alert to the default alert bucket *if* we have not
     * exceeded the threshhold.
     */
    public void alert(String msg) {
        alert(msg, alertType);
    }
}
