/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import com.enjoyf.platform.service.alert.AlertImplService;

/**
 * Class represents a global alerter for the framework. The alerter can be set to
 * alert to the alert service, or the logs globally, depending on requirement.
 */
public class GAlerter {
    /**
     * Alerter object - Initializes the default alerting to log to the alert service
     */
    private static Alerter alerter = new AlerterDefault(new AlertImplService());

    public static void set(Alerter altr) {
        alerter = altr;
    }

    public static void setImpl(AlertImpl altImpl) {
        alerter = new AlerterDefault(altImpl);
    }

    /**
     * Log an alert msg. The routines lai,lab,lab,lan are more convenient
     * methods to use. Alerts end up going to the "alert object".
     * As far as this object is concerned the alert object is abstract, but
     * in practice this is a server that receives the alert and logs it to
     * a local file.
     */
    public static void alert(Alert alert) {
        alerter.alert(alert);
    }

    /**
     * Log an alert with BUG severity. This means that there is probably
     * some sort of programming bug somewhere that needs attention.
     * NOTE: Do not pass in variable data in the msg! See the version
     * that takes variable data instead.
     *
     * @param msg The msg to log.
     */
    public static void lab(String msg) {
        alert(new Alert(AlertType.BUG, msg));
    }

    /**
     * Log an alert with BUG severity. NOTE: Do not pass in variable
     * data in the msg!
     *
     * @param msg The msg to log.
     * @param t   An associated throwable to log. This means the
     *            stack trace is also printed at the destination.
     */
    public static void lab(String msg, Throwable t) {
        alert(new Alert(AlertType.BUG, msg, t));
    }

    /**
     * Log an alert that contains variable data. Since GAlerter.lab() alerts
     * are automatically grabbed by a tool that inputs them into the
     * bug db, we want to separate the bug string from any variable
     * in the bug string.
     *
     * @param msg          The constant msg that is logged for a specific type
     *                     of alert.
     * @param variableData Any variable info in the msg (eg, a username)
     *                     that we don't want the alert tool to pick up.
     */
    public static void lab(String msg, String variableData) {
        lab(msg, variableData, null);
    }

    /**
     * Log an alert that contains variable data. Since GAlerter.lab() alerts
     * are automatically grabbed by a tool that inputs them into the
     * bug db, we want to separate the bug string from any variable
     * in the bug string.
     *
     * @param msg          The constant msg that is logged for a specific type
     *                     of alert.
     * @param variableData Any variable info in the msg (eg, a username)
     *                     that we don't want the alert tool to pick up.
     * @param t            A throwable whose stack trace will be printed.
     */
    public static void lab(String msg, String variableData, Throwable t) {
        alert(new Alert(AlertType.BUG, msg + " %var% " + variableData, t));
    }

    /**
     * Log an alert with NOISE severity. This means that we do expect
     * these kinds of alerts now and then, but only if we get a bunch
     * of them over and over do we consider something to be messed up.
     *
     * @param msg The msg to log.
     */
    public static void lan(String msg) {
        lan(msg, null);
    }

    /**
     * Log an alert with NOISE severity.
     *
     * @param msg The msg to log.
     * @param t   An associated throwable to log. This means the
     *            stack trace is also printed at the destination.
     */
    public static void lan(String msg, Throwable t) {
        alert(new Alert(AlertType.NOISE, msg, t));
    }

    public static void lacb(String msg) {
        alert(new Alert(AlertType.CLIENT_BUG, msg));
    }


    /**
     * Log an alert with DEBUG severity. This is intended for
     * logging msgs to one place that are useful in debugging complex
     * problems that are more system-wide as opposed to localized to one
     * jvm. It is not intended for "normal" logging. In other words, avoid
     * using this if you can.
     *
     * @param msg The msg to log.
     */
    public static void lad(String msg) {
        lad(msg, null);
    }

    /**
     * Log an alert with DEBUG severity. This is intended for
     * logging msgs to one place that are useful in debugging complex
     * problems that are more system-wide as opposed to localized to one
     * jvm. It is not intended for "normal" logging. In other words, avoid
     * using this if you can.
     *
     * @param msg The msg to log.
     * @param t   An associated throwable to log.
     */
    public static void lad(String msg, Throwable t) {
        alert(new Alert(AlertType.DEBUG, msg, t));
    }

    //send out the alert to mail.
    public static void lam(String msg) {
        lam(msg, null);
    }

    public static void lam(String msg, Throwable t) {
        alert(new Alert(AlertType.EMAIL, msg, t));
    }

    public static void latd(String msg) {
        latd(msg, null);
    }

    public static void latd(String msg, Throwable t) {
        alert(new Alert(AlertType.TIMER_DEBUG, msg, t));
    }


}
