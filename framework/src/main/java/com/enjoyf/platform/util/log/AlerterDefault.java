/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import org.slf4j.LoggerFactory;

/**
 * This class contains the implementation of the alerting functionality
 */
public class AlerterDefault implements Alerter {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AlerterDefault.class);
	
    // To which alert logger to alert to 
    private AlertImpl alertImpl = null;

    /**
     * Public constructor
     *
     * @param impl AlertImpl object
     */
    public AlerterDefault(AlertImpl impl) {
        alertImpl = impl;
    }

    /**
     * Redirects the alerting to the given implementation of alerting
     *
     * @param impl Implementation of alerting to which AlerterDefault will
     *             be set to
     */
    public void setAlertImpl(AlertImpl impl) {
        alertImpl = impl;
    }

    /**
     * Sets the destination of the alerting functionality to the specified alerter
     */
    public AlertImpl getAlertImpl() {
        return alertImpl;
    }

    /**
     * Sends an alert
     */
    public void alert(Alert alert) {
        alert.setMsg(getAlertString(alert.getMsg()));
        // The first thing we do is spit the alert msg to the local
        logger.error(alert.getMsg(), alert.getThrowable());

        // Then we send the alert to the Alert Service
        if (alertImpl != null) {
            alertImpl.log(alert);
        } else {
            logger.error("AlertLoggerImpl: alert called but no alert logger set!");
        }
    }

    /**
     * Gets alert string to be printed
     *
     * @param s
     * @return
     */
    private String getAlertString(String s) {
        String serverId = ServerId.getId();
        if (serverId == null) {
            return s;
        }

        StringBuffer sb = new StringBuffer(serverId + ":");
        sb.append(s);
        return new String(sb);
    }
}
