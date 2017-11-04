package com.enjoyf.platform.serv.alert;

import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.Alert;
import com.enjoyf.platform.util.log.LoggerFileRotating;
import com.enjoyf.platform.util.log.SeverityLevel;
import com.enjoyf.platform.util.thread.ThreadNotifier;


class AlertProcessorFile implements AlertProcessor.Listener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AlertProcessorFile.class);
    private LoggerFileRotating loggerFile;

    /**
     * Rotate with a frequency of one day.
     */
    private static final int ONE_DAY = 24 * 3600 * 1000;
    /**
     * Rotate the file 5 minutes after midnight.
     */
    private static final int ROTATE_TIME = 0005;

    AlertProcessorFile(String fname, int minFileSize) {
        if (logger.isDebugEnabled()) {
            logger.debug("AlertProcessorFile: Creating file: " + fname + " to rotate if > " + minFileSize);
        }
        try {
            //--
            // Create a rotating file logger.
            //--
            ThreadNotifier notifier = new ThreadNotifier(ROTATE_TIME, ONE_DAY);
            loggerFile = new LoggerFileRotating(fname, minFileSize, notifier);
            loggerFile.setPrintThreadInfo(false);
            notifier.start();
        }
        catch (Exception e) {
            logger.error("COULD NOT open up alert log file: '" + fname + "'", e);
        }
    }

    public void process(ServerAlert serverAlert) {
        if (logger == null) {
            return;
        }

        Client client = serverAlert.getClient();
        String clientName = "UNKNOWN";
        if (client != null) {
            clientName = client.getClientInfo();
        }

        Alert alert = serverAlert.getAlert();
        loggerFile.println(SeverityLevel.HIGH, clientName + ":" + alert.getMsg());

        if (alert.hasThrowable()) {
            StringBuffer sb = new StringBuffer();
            sb.append(alert.getExceptionMessage());
            sb.append("\n");
            sb.append(alert.getTrace());

            loggerFile.println(SeverityLevel.HIGH, clientName + ":trace:" + sb.toString());
        }
    }

}
