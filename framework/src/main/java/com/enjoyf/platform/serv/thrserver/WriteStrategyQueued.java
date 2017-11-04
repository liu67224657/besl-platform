/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * An implementation of WriteStrategy that queues and performs
 * wedge checking.
 */
public class WriteStrategyQueued implements WriteStrategy {
    private QueuedSender queueSender;

    public WriteStrategyQueued(QueuedSender sender) {
        if (sender == null) {
            String errmsg = "WriteStrategyQueued: queueSender is null! ";
            GAlerter.lab(errmsg);

            throw new IllegalArgumentException(errmsg);
        }

        queueSender = sender;
    }

    public void write(ConnThreadBase conn, WPacketBase wp) throws IOException {
        QueuedSender.Message msg = new QueuedSender.MessageDef(conn, wp);

        queueSender.send(msg);
    }
}
