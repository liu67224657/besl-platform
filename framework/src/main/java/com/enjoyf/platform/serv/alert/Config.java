package com.enjoyf.platform.serv.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * This class is used to configure the server. It is typically a specific
 * class for the server.
 */
class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private String logfileName;
    private int heartbeatInterval = 5 * 60 * 1000;
    private int minFileSize = 1024 * 1024;

    Config() {
        init();
    }

    /**
     * This method will construct the object from the passed in
     * cfgfile object.
     */
    Config(FiveProps props) {
        init();

        logger.info("AlertProps=" + props);
        String sval = props.get("server.logfilename");
        if (sval != null) {
            logfileName = sval;
        }


        int ival = props.getInt("server.heartbeat", 0);
        if (ival != 0) {
            heartbeatInterval = ival * 1000;
        }

        //--
        // The property is in kbytes. If 0, this means rotation is attempted
        // no matter how big the file is.
        //--
        sval = props.get("server.minFileSize");

        if (sval != null) {
            ival = -1;

            try {
                ival = Integer.parseInt(sval);
            }
            catch (Exception e) {
                GAlerter.lab("Parse error for 'server.minFileSize' property "
                        + " Defaulting to 1Mb");
            }

            if (ival != -1) {
                minFileSize = ival * 1024;
            }
        }
    }

    /**
     * The min file size before making the rotate decision. Ie, the
     * file must > getMinFileSize() in order for it to be rotated.
     */
    int getMinFileSize() {
        return minFileSize;
    }

    void setLogfileName(String logfileName) {
        this.logfileName = logfileName;
    }

    String getLogfileName() {
        return logfileName;
    }

    void setHeartbeat(int heartbeat) {
        heartbeatInterval = heartbeat;
    }

    int getHeartbeat() {
        return heartbeatInterval;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("logFileName=" + logfileName + ":");
        sb.append("heartbeat=" + heartbeatInterval + ":");
        sb.append("minFileSize=" + minFileSize + ":");
        return sb.toString();
    }

    /**
     * Put default settings here.
     */
    private void init() {
        logfileName = "logfile.txt";
        heartbeatInterval = 5 * 60 * 1000;
        minFileSize = 1024 * 1024;
    }
}
