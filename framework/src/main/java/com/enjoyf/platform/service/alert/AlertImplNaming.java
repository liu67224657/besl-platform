package com.enjoyf.platform.service.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.PacketReaderAdvttl;
import com.enjoyf.platform.io.PacketWriterAdvttl;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressGetter;
import com.enjoyf.platform.io.Transactor;
import com.enjoyf.platform.io.TtlException;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.HelloInfo;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.util.log.Alert;
import com.enjoyf.platform.util.log.AlertImpl;

/**
 * An alert implementation that talks directly to the alert server,
 * without going through the service layers. The primary purpose for
 * this class is so that the naming server can alert, without
 * recursively calling itself to find out where the alert server is.
 */
public class AlertImplNaming implements AlertImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertImplNaming.class);
	
    private Transactor transactor;
    private ServiceAddressGetter serviceAddressGetter;
    private String clientName;

    public AlertImplNaming(String client, ServiceAddressGetter saddrGetter) {
        clientName = client;
        serviceAddressGetter = saddrGetter;
    }

    private synchronized boolean connect() {
        if (transactor != null && transactor.isConnected()) {
            return true;
        }

        if (transactor == null) {
            transactor = new Transactor(new PacketReaderAdvttl(), new PacketWriterAdvttl());
        }

        ServiceAddress saddr = serviceAddressGetter.get();
        if (saddr == null) {
            return false;
        }

        try {
            transactor.connect(saddr);
        } catch (TtlException e) {
            transactor.disconnect();
            transactor = null;
            return false;
        }

        WPacket wp = new WPacket();
        wp.writeSerializable(new HelloInfo(clientName, true));
        try {
            transactor.syncRequest(ServiceConstants.HELLO, wp);
        } catch (TtlException e) {
            transactor.disconnect();
            transactor = null;
            return false;
        }

        return true;
    }

    public void log(Alert alert) {
        if (!connect()) {
            logger.error("Could not alert: CONNECT");
            return;
        }

        WPacket wp = new WPacket();
        wp.writeSerializable(alert);

        try {
            transactor.sendAndForget(AlertConstants.REPORT, wp);
        } catch (TtlException e) {
            logger.error("AlertImplNaming.println: Could not alert.", e);
        }
    }

    public void flush() {
    }

    public void close() {
        Transactor t = transactor;

        if (t != null) {
            t.disconnect();
        }
    }
}
