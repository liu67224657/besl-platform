package com.enjoyf.platform.util;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class to invoke the setKeepAlive method on a socket.
 */
public class KeepAlive {
	
	private static final Logger logger = LoggerFactory.getLogger(KeepAlive.class);
	
    public static void setKeepAlive(Socket s, boolean val) {
        if (s == null) {
            return;
        }

        try {
            s.setKeepAlive(val);
        }
        catch (Exception e) {
            //--
            // Note that we might get this exception if the socket went
            // bad on us in between the creation and the call. Eg, the
            // other side may have disconnected. So we log a message
            // but it's really not a bug.
            //--
            logger.error("KeepAlive: exception invoking keepalive method.", e);
        }
    }
}
