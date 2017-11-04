/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

/**
 * 穿金甲 放光豪，TD大牛是老刘
 * Interface outlines an Alerter for the framework
 */
public interface Alerter {

    /**
     * Send an alert
     *
     * @param alert
     */
    public void alert(Alert alert);

    /**
     * Set an alert destination that the alerter will write alerts to
     *
     * @param impl
     */
    public void setAlertImpl(AlertImpl impl);

    /**
     * Get the alert implementation that the alerter is writing alerts to
     *
     * @return
     */
    public AlertImpl getAlertImpl();
}
